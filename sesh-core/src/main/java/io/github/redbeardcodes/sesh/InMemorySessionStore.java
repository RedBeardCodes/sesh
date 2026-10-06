package io.github.redbeardcodes.sesh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemorySessionStore implements SessionStore {

	private ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	private AtomicLong idGenerator = new AtomicLong();

	@Override
	public Session load(String id) {
		return sessions.get(id);
	}

	@Override
	public Session create() {
		String id = "Session" + String.valueOf(idGenerator.incrementAndGet());
		Session session = new InMemorySession(id);
		this.save(session);
		return session;
	}

	@Override
	public void save(Session session) {
		sessions.put(session.id(), session);
	}

	@Override
	public void delete(String id) {
		sessions.remove(id);
	}
}
