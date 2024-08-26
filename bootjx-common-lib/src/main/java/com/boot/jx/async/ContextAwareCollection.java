package com.boot.jx.async;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.MDC;

import com.boot.jx.AppContext;
import com.boot.jx.AppContextUtil;

public class ContextAwareCollection<T> {

	private final Collection<T> collection;
	private AppContext context = null;
	private Map<String, String> contextMap;

	public ContextAwareCollection(Collection<T> collection) {
		this.collection = collection;
		this.context = AppContextUtil.getContext(); // Capture the context at the time of wrapping
		this.contextMap = MDC.getCopyOfContextMap();
	}

	// Wrapper function to handle context propagation
	public Stream<T> ayncStream() {
		return collection.parallelStream().map(element -> {
			// Propagate context to each thread
			MDC.setContextMap(contextMap);
			if (context != null) {
				AppContextUtil.setContext(context);
				AppContextUtil.init();
			}

			return element;
		}).onClose(AppContextUtil::clear); // Cleanup context when stream is closed
	}

	// Optionally, you can provide other collection-like methods with context
	// awareness
	public List<T> collectToList(Function<T, T> mapper) {
		return ayncStream().map(mapper).collect(Collectors.toList());
	}

	public static <TE> ContextAwareCollection<TE> wrap(Collection<TE> collection) {
		return new ContextAwareCollection<TE>(collection);
	}

}
