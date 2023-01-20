package de.com.bruns.sample.sampleapp.callscope;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import lombok.Getter;

public class CallScope implements Scope {

	public static final String NAME = "CallScope";

	private final Map<String, Object> scopedObjectsHolder = new ConcurrentHashMap<>();
	private final Map<String, Runnable> destructionCallbacksHolder = new ConcurrentHashMap<>();

	private final AtomicInteger usageCounter = new AtomicInteger(0);

	@Getter
	private final String conversationId = UUID.randomUUID().toString();

	public void start() {
		usageCounter.incrementAndGet();
	}

	public void stop() {
		if (usageCounter.decrementAndGet() <= 0) {
			destructionCallbacksHolder.values().stream().forEach(Runnable::run);
		}
	}

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		return scopedObjectsHolder.computeIfAbsent(name, n -> objectFactory.getObject());
	}

	@Override
	public Object remove(String name) {
		destructionCallbacksHolder.remove(name);
		return scopedObjectsHolder.remove(name);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		destructionCallbacksHolder.put(name, callback);
	}

	@Override
	public Object resolveContextualObject(String key) {
		return scopedObjectsHolder.get(key);
	}

}
