package com.boot.jx.logger.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.AppConfig;
import com.boot.jx.AppContext;
import com.boot.jx.AppContextUtil;
import com.boot.jx.logger.AbstractEvent;
import com.boot.jx.logger.AbstractEvent.EventMarker;
import com.boot.jx.logger.AbstractEvent.EventType;
import com.boot.jx.logger.AuditActor;
import com.boot.jx.logger.AuditEvent;
import com.boot.jx.logger.AuditLoggerResponse;
import com.boot.jx.logger.AuditService;
import com.boot.jx.logger.events.ApiAuditEvent;
import com.boot.jx.session.SessionContextService;
import com.boot.jx.tunnel.ITunnelService;
import com.boot.utils.ContextUtil;
import com.boot.utils.JsonUtil;
import com.boot.utils.TimeUtils;

@Component
public class AuditServiceClient implements AuditService {

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);
	private static final Logger LOGGER2 = LoggerFactory.getLogger(AuditServiceClient.class);
	private static final Marker auditmarker = MarkerFactory.getMarker(EventMarker.AUDIT.toString());
	private static final Marker trackmarker = MarkerFactory.getMarker(EventMarker.TRACK.toString());
	private static final Marker gaugemarker = MarkerFactory.getMarker(EventMarker.GAUGE.toString());
	private static final Marker excepmarker = MarkerFactory.getMarker(EventMarker.EXCEP.toString());
	private static final Marker noticemarker = MarkerFactory.getMarker(EventMarker.NOTICE.toString());
	private static final Marker alertmarker = MarkerFactory.getMarker(EventMarker.ALERT.toString());
	private static final Marker debugmarker = MarkerFactory.getMarker("DEBUG");
	private static final Map<String, Boolean> allowedMarkersMap = new HashMap<String, Boolean>();
	private final Map<String, AuditFilter<AuditEvent<?>>> filtersMap = new HashMap<>();
	private static boolean FILTER_MAP_DONE = false;
	private static String APP_NAME = null;
	private static boolean AUDIT_LOGGER_ENABLED = false;

	private static ITunnelService ITUNNEL_SERVICE;

	@Autowired
	SessionContextService sessionContextService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Autowired
	public AuditServiceClient(AppConfig appConfig, List<AuditFilter> filters,
			@Autowired(required = false) ITunnelService iTunnelService) {

		String[] allowedMarkersList = appConfig.getPrintableAuditMarkers();
		String[] skippedMarkersList = appConfig.getSkipAuditMarkers();

		for (String markerString : allowedMarkersList) {
			allowedMarkersMap.put(markerString, Boolean.TRUE);
		}

		for (String markerString : skippedMarkersList) {
			allowedMarkersMap.put(markerString, Boolean.FALSE);
		}

		if (!FILTER_MAP_DONE) {
			APP_NAME = appConfig.getAppName();
			AUDIT_LOGGER_ENABLED = appConfig.isAudit();
			for (AuditFilter filter : filters) {
				Matcher matcher = pattern.matcher(filter.getClass().getGenericInterfaces()[0].getTypeName());
				if (matcher.find()) {
					filtersMap.put(matcher.group(1), filter);
				}
			}
			ITUNNEL_SERVICE = iTunnelService;
			FILTER_MAP_DONE = true;
			LOGGER2.warn("Audit Filters scanned");
		} else {
			LOGGER2.warn("Skipping Audit Filters scanning");
		}
	}

	private void excuteFilters(AuditEvent<?> event) {
		if (filtersMap.containsKey(event.getClass().getName())) {
			AuditFilter<AuditEvent<?>> filter = filtersMap.get(event.getClass().getName());
			try {
				filter.doFilter(event);
			} catch (Exception e) {
				LOGGER2.error("Exception while executing Filters", e);
			}
		}
	}

	private static AuditEvent<?> captureException(AuditEvent<?> event, Throwable e) {
		event.setExceptionType(e.getClass().getName());
		event.setException(e.getMessage());
		return event;
	}

	private static AuditEvent<?> captureDetails(AuditEvent<?> event) {
		event.setComponent(APP_NAME);
		Long traceTime = AppContextUtil.getTraceTime();
		if (traceTime != null) {
			event.setTraceTime(TimeUtils.timeSince(AppContextUtil.getTraceTime()));
		}
		event.setEventTime(TimeUtils.timeSince(event.getTimestamp()));
		event.setActorId(AppContextUtil.getActorId());
		event.setFlow(AppContextUtil.getFlow());
		return event;
	}

	public static void publishAbstractEvent(Map<String, Object> map) {
		try {
			AppContext appContext = AppContextUtil.getContext();
			map.put(AbstractEvent.PROP_TRC_ID, appContext.getTraceId());
			map.put(AbstractEvent.PROP_TRX_ID, appContext.getTranxId());
			map.put("tnt", appContext.getTenant());
			ITUNNEL_SERVICE.audit(AUDIT_EVENT_TOPIC, map);
		} catch (Exception e) {
			LOGGER2.error("Exception while Publishing Event", e);
		}
	}

	public static AuditLoggerResponse logAbstractEvent(Marker marker, AbstractEvent event, boolean capture) {
		event.setComponent(APP_NAME);
		event.clean();
		String json = JsonUtil.toJson(event);

		if (event instanceof ApiAuditEvent) {
			ContextUtil.map().put("api_event", event);
		} else {
			String marketName = marker.getName();
			if (allowedMarkersMap.getOrDefault(marketName, Boolean.FALSE).booleanValue()) {
				if (event.isDebugEvent()) {
					LOGGER.debug(debugmarker, json);
				} else {
					LOGGER.info(marker, json);
				}
			}
			if (capture && ITUNNEL_SERVICE != null && AUDIT_LOGGER_ENABLED) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = JsonUtil.fromJson(json, Map.class);
				publishAbstractEvent(map);
			}
		}

		return null;
	}

	/**
	 * 
	 * @param marker
	 * @param event
	 * @param capture - true to capture value in logger service, default is false
	 * @return
	 */
	public static AuditLoggerResponse logAuditEvent(Marker marker, AuditEvent<?> event, boolean capture) {
		try {
			captureDetails(event);
			event.setClientStr(AppContextUtil.getUserClient().toString());
			event.setTranxId(AppContextUtil.getTranxId());
			return logAbstractEvent(marker, event, capture);
		} catch (Exception e) {
			LOGGER2.error("Exception while logAuditEvent {}", event.getType(), e);
		}
		return null;
	}

	/**
	 * 
	 * @param marker
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse logAuditEvent(Marker marker, AuditEvent<?> event) {
		return logAuditEvent(marker, event, false);
	}

	public static AuditLoggerResponse logAuditEvent(EventMarker emarker, AuditEvent<?> event) {
		Marker marker = auditmarker;
		boolean capture = false;
		if (emarker == EventMarker.AUDIT) {
			capture = true;
		} else if (emarker == EventMarker.TRACK) {
			marker = trackmarker;
		} else if (emarker == EventMarker.GAUGE) {
			marker = gaugemarker;
		} else if (emarker == EventMarker.EXCEP) {
			marker = excepmarker;
		} else if (emarker == EventMarker.ALERT) {
			marker = alertmarker;
		} else if (emarker == EventMarker.NOTICE) {
			marker = noticemarker;
		} else {
			capture = true;
		}
		return logAuditEvent(marker, event, capture);
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse logStatic(AuditEvent<?> event) {
		EventType eventType = event.getType();
		if (eventType == null) {
			LOGGER2.error("Exception while logStatic {}", JsonUtil.toJson(event));
			return null;
		}
		EventMarker eventMarker = event.getTypeMarker();
		if (eventMarker == null) {
			eventMarker = EventMarker.AUDIT;
		}
		return logAuditEvent(eventMarker, event);
	}

	@Override
	public AuditLoggerResponse log(AuditEvent<?> event) {
		excuteFilters(event);
		return logStatic(event);
	}

	// TRACK LOGS

	public static AuditLoggerResponse trackStatic(AuditEvent<?> event) {
		return logAuditEvent(trackmarker, event);
	}

	@Override
	public AuditLoggerResponse track(AuditEvent<?> event) {
		excuteFilters(event);
		return trackStatic(event);
	}

	// GAUGE LOGS
	/**
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse gaugeStatic(AuditEvent<?> event) {
		return logAuditEvent(gaugemarker, event);
	}

	@Override
	public AuditLoggerResponse gauge(AuditEvent<?> event) {
		excuteFilters(event);
		return gaugeStatic(event);
	}

	// Excep LOGS

	/**
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse excepStatic(AuditEvent<?> event) {
		event.setResult(AuditEvent.Result.ERROR);
		return logAuditEvent(excepmarker, event);
	}

	public AuditLoggerResponse excep(AuditEvent<?> event) {
		excuteFilters(event);
		return excepStatic(event);
	}

	@Override
	public AuditLoggerResponse excep(AuditEvent<?> event, Throwable e) {
		AuditServiceClient.captureException(event, e);
		return this.excep(event);
	}

	@Override
	public AuditLoggerResponse log(AuditEvent<?> event, Throwable e) {
		AuditServiceClient.captureException(event, e);
		return this.log(event);
	}

	@Override
	public AuditLoggerResponse excep(AuditEvent<?> event, Logger logger, Throwable e) {
		logger.error(event.getType().toString(), e);
		return this.excep(event, e);
	}

	public static boolean isDebugEnabled() {
		return LOGGER.isDebugEnabled();
	}

	@Override

	public AuditActor getActor() {
		return sessionContextService.getContext(AuditActor.class);
	}

	@Override
	public <T extends AuditActor> T getActor(Class<T> class1) {
		return sessionContextService.getContext(class1);
	}

	@Override
	public List<AuditEvent<?>> queue(AuditEvent<?> event) {
		@SuppressWarnings("unchecked")
		List<AuditEvent<?>> list = (List<AuditEvent<?>>) ContextUtil.map().getOrDefault("auditq",
				new LinkedList<AuditEvent<?>>());
		list.add(event);
		ContextUtil.map().put("auditq", list);
		return list;
	}

	@Override
	public List<AuditEvent<?>> commit() {
		@SuppressWarnings("unchecked")
		LinkedList<AuditEvent<?>> list = (LinkedList<AuditEvent<?>>) ContextUtil.map().getOrDefault("auditq",
				new LinkedList<AuditEvent<?>>());
		for (AuditEvent<?> auditEvent : list) {
			this.log(auditEvent);
		}
		ContextUtil.map().remove("auditq");
		return list;
	}

}
