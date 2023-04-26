package com.boot.jx.mongo.logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.boot.jx.AppConfig;
import com.boot.jx.mongo.CommonMongoQueryBuilder;
import com.boot.jx.mongo.CommonMongoTemplateAbstract;
import com.boot.jx.tunnel.ZQueueDefs.ZQueueElement;
import com.boot.jx.tunnel.ZQueueDefs.ZQueueStore;
import com.boot.utils.CollectionUtil;
import com.boot.utils.UniqueID;

@Component
public class QueueMongoStore extends CommonMongoTemplateAbstract implements ZQueueStore {

	@Autowired
	private AppConfig appConfig;

	@Override
	public void enqueue(ZQueueElement element) {
		if (element instanceof QueueElementDoc) {
			QueueElementDoc elementDoc = (QueueElementDoc) element;
			elementDoc.setTimestamp(System.currentTimeMillis());
			elementDoc.setAppType(appConfig.getAppType());
			elementDoc.setAppVenv(appConfig.getAppVenv());
		}
		save(element);
	}

	@Override
	public ZQueueElement dequeue(String queueType, String queueId) {
		String batchId = UniqueID.generateString();
		CommonMongoQueryBuilder builder = new CommonMongoQueryBuilder();
		builder.where(Criteria.where("appType").is(appConfig.getAppType())//
				.and("appVenv").is(appConfig.getAppVenv())//
				.and("queueType").is(queueType)//
				.and("queueId").is(queueId)//
				.and("batchId").exists(false))//
				.sortBy(new Sort(Direction.ASC, "itemOrder").and(new Sort(Direction.ASC, "timestamp"))).limit(1);
		builder.set("batchId", batchId);
		findAndModify(builder.getQuery(), builder.update(), QueueElementDoc.class);

		CommonMongoQueryBuilder builder2 = new CommonMongoQueryBuilder();
		builder2.where(Criteria.where("appType").is(appConfig.getAppType())//
				.and("appVenv").is(appConfig.getAppVenv())//
				.and("queueType").is(queueType)//
				.and("queueId").is(queueId)//
				.and("batchId").is(batchId));//
		List<QueueElementDoc> docs = findAllAndRemove(builder2.getQuery(), QueueElementDoc.class);
		return CollectionUtil.first(docs);
	}

	@Override
	public List<? extends ZQueueElement> dequeueAll(String queueType, String queueId) {
		String batchId = UniqueID.generateString();
		CommonMongoQueryBuilder builder = new CommonMongoQueryBuilder();
		builder.where(Criteria.where("appType").is(appConfig.getAppType())//
				.and("appVenv").is(appConfig.getAppVenv())//
				.and("queueType").is(queueType)//
				.and("queueId").is(queueId)//
				.and("batchId").exists(false));//
		// builder.sortBy(new Sort(Direction.ASC, "itemOrder").and(new
		// Sort(Direction.ASC, "timestamp"))).limit(1);
		builder.set("batchId", batchId);

		// findAndModify(builder.getQuery(), builder.update(), QueueElementDoc.class);
		updateMulti(builder.getQuery(), builder.update(), QueueElementDoc.class);

		CommonMongoQueryBuilder builder2 = new CommonMongoQueryBuilder();
		builder2.where(Criteria.where("appType").is(appConfig.getAppType())//
				.and("appVenv").is(appConfig.getAppVenv())//
				.and("queueType").is(queueType)//
				.and("queueId").is(queueId)//
				.and("batchId").is(batchId));//
		return findAllAndRemove(builder2.getQuery(), QueueElementDoc.class);
	}

}
