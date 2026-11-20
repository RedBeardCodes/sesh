package io.github.redbeardcodes.sesh;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class InMemorySession implements Session {

	private final String id;
	private Instant creationTime;
	private Instant lastAccess;
	private Instant expires;

	private boolean invalidated = false;

	private final Map<String, Object> sessionData = new HashMap<String, Object>();

	public InMemorySession(String id) {
		Instant now = Instant.now();
		this.id = id;
		this.creationTime = now;
		this.lastAccess = now;
		this.expires = now.plus(10, ChronoUnit.MINUTES);
	}

	public InMemorySession(String id, Instant expires) {
		this(id);
		this.expires = expires;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public synchronized <T> T get(String name, Class<T> type) {
		checkValid();
		return type.cast(sessionData.get(name));
	}

	@Override
	public synchronized void set(String name, Object value) {
		checkValid();
		sessionData.put(name, value);
	}

	@Override
	public synchronized void remove(String name) {
		checkValid();
		sessionData.remove(name);
	}

	@Override
	public Instant createdAt() {
		return creationTime;
	}

	@Override
	public Instant lastAccessAt() {
		return lastAccess;
	}

	@Override
	public Instant expiresAt() {
		return expires;
	}

	@Override
	public void invalidate() {
		if (invalidated)
			return;
		sessionData.clear();
		this.invalidated = true;
	}

	@Override
	public synchronized boolean isInvalidated() {
		return invalidated;
	}

	Map<String, Object> getSessionData() {
		return this.sessionData;
	}

	private void checkValid() {
		if (invalidated)
			throw new IllegalStateException("Session has been invalidated.");
	}

}
