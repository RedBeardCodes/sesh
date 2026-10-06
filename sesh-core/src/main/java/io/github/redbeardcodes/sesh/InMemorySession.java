package io.github.redbeardcodes.sesh;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class InMemorySession implements Session {

	private String id;
	private Instant creationTime;
	private Instant lastAccess;
	private Instant expires;

	private Map<String, Object> session = new HashMap<String, Object>();

	public InMemorySession(String id) {
		Instant now = Instant.now();
		this.id = id;
		this.creationTime = now;
		this.lastAccess = now;
		this.expires = now.plus(24, ChronoUnit.HOURS);
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
	public <T> T get(String name, Class<T> type) {
		return type.cast(session.get(name));
	}

	@Override
	public void set(String name, Object value) {
		session.put(name, value);
	}

	@Override
	public void remove(String name) {
		session.remove(name);
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
		session.clear();
	}
}
