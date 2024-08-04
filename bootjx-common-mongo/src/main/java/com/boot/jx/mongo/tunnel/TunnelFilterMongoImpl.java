package com.boot.jx.mongo.tunnel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.boot.jx.AppConfig;
import com.boot.jx.AppContext;
import com.boot.jx.AppContextUtil;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex;
import com.boot.jx.mongo.CommonMongoTemplate;
import com.boot.jx.rest.RestService;
import com.boot.jx.scope.tnt.Tenants;
import com.boot.jx.tunnel.ChronoTask;
import com.boot.jx.tunnel.ITunnelDefs.TunnelFilter;
import com.boot.jx.tunnel.TunnelEvent;
import com.boot.jx.tunnel.TunnelMessage;
import com.boot.model.MapModel;
import com.boot.model.MapModel.MapPathEntry;
import com.boot.utils.ArgUtil;
import com.boot.utils.JsonUtil;

@Service
@ConditionalOnProperty(name = "bootjx.tunnel.filter", havingValue = "mongo")
public class TunnelFilterMongoImpl implements TunnelFilter {

	private Logger LOGGER = LoggerFactory.getLogger(TunnelFilterMongoImpl.class);

	Map<String, String> myTopics = null;

	@Value("${bootjx.tunnel.cross.url}")
	private String crossUrl;

	@Value("${bootjx.tunnel.scheduler}")
	private String scheduler;

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Autowired
	CommonMongoTemplate commonMongoTemplate;

	@Override
	public boolean postSubscriptions(List<String> topics) {

		LOGGER.info("TunnelFilterMongoImpl postSubscriptions");

		List<TunnelTaskDoc> tasks = commonMongoTemplate.findAll(TunnelTaskDoc.class);

		TimeStampIndex now = TimeStampIndex.now();

		for (TunnelTaskDoc task : tasks) {
			if (ArgUtil.is(task.getPlatform(), "java") && ArgUtil.is(task.getService(), appConfig.getAppType())) {
				String foundTopic = null;
				for (String topic : topics) {
					if (ArgUtil.is(task.getTopic(), topic)) {
						foundTopic = topic;
					}
				}
				if (ArgUtil.is(foundTopic) && !task.isActive()) {
					task.setActive(true);
					task.setUpdated(now);
				} else if (!ArgUtil.is(foundTopic)) {
					task.setActive(false);
					task.setUpdated(now);
				}

			}
		}

		for (String topic : topics) {
			TunnelTaskDoc foundTask = null;
			for (TunnelTaskDoc task : tasks) {
				if (ArgUtil.is(task.getPlatform(), "java") && ArgUtil.is(task.getService(), appConfig.getAppType())) {
					if (ArgUtil.is(task.getTopic(), topic)) {
						foundTask = task;
					}
				}
			}
			if (ArgUtil.is(foundTask) && !foundTask.isActive()) {
				foundTask.setActive(true);
				foundTask.setUpdated(now);
			} else if (!ArgUtil.is(foundTask)) {
				TunnelTaskDoc newTask = new TunnelTaskDoc();
				newTask.setTopic(topic);
				newTask.setPlatform("java");
				newTask.setService(appConfig.getAppType());
				newTask.setActive(true);
				newTask.setUpdated(now);
				newTask.setId(newTask.getPlatform() + ":" + newTask.getService() + ":" + newTask.getTopic());
				tasks.add(newTask);
			}
		}

		boolean updated = false;
		for (TunnelTaskDoc task : tasks) {
			if (ArgUtil.is(task.getPlatform(), "java") && ArgUtil.is(task.getService(), appConfig.getAppType())) {
				if (ArgUtil.is(task.getUpdated()) && ArgUtil.is(task.getUpdated().getStamp(), now.getStamp())) {
					LOGGER.info("TunnelFilterMongoImpl updating:{}", task.getTopic());
					commonMongoTemplate.save(task);
					updated = true;
				}
			}
		}
		return updated;
	}

	@Override
	public <T> boolean beforeTaskPublish(String topic, T messagePayload, AppContext context) {
		String myTopicKey = topic + "#java";
		if (myTopics == null) {
			myTopics = new HashMap<String, String>();
			String tnt = AppContextUtil.getTenant();
			AppContextUtil.setTenant(Tenants.getDefault());
			List<TunnelTaskDoc> tasks = commonMongoTemplate.findAll(TunnelTaskDoc.class);
			for (TunnelTaskDoc task : tasks) {
				myTopics.put(task.getTopic() + "#" + task.getPlatform(), task.getPlatform());
				if (!ArgUtil.is(task.getPlatform(), "java")) {
					myTopics.put(task.getTopic(), task.getPlatform());
				}
			}
			AppContextUtil.setTenant(tnt);
		}
		return myTopics.containsKey(myTopicKey);
	}

	public <T> void afterTaskPublic(String topic, T messagePayload, AppContext context) {
		if (myTopics.containsKey(topic) && ArgUtil.is(crossUrl)) {
			TunnelMessage<Object> t = new TunnelMessage<Object>(new HashMap<String, Object>());
			t.setAppType(appConfig.getAppType());
			t.setContext(context);
			t.setTopic(topic);
			t.setData(messagePayload);
			restService.ajax(crossUrl).postJson(t).asNone();
		}
	}

	@PostConstruct
	public void init() {
		LOGGER.info("TunnelFilterMongoImpl init");
	}

	@Override
	public void onMasterUpdate(TunnelEvent message) {
		LOGGER.info("======onMasterUpdate==={}", JsonUtil.toJson(message));
		this.myTopics = null;
	}

	@Override
	public ChronoTask schedule(ChronoTask chronoTask) {
		if (ArgUtil.is(scheduler)) {
			MapModel resp = restService.ajax(crossUrl).postJson(chronoTask).asMapModel();
			MapPathEntry id = resp.keyEntry("id");
			if (id.exists()) {
				chronoTask.setTaskId(id.asString());
			}
		}
		return chronoTask;
	}
}
