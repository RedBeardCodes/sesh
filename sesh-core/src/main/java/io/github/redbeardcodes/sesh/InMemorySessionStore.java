package io.github.redbeardcodes.sesh;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySessionStore implements SessionStore {

	private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	@Override
	public synchronized Optional<Session> load(String id) {
		return Optional.ofNullable(sessions.get(id));
	}

	@Override
	public synchronized Session create() {
		Session session;
		do {
			String id = "Session" + UUID.randomUUID();
			session = new InMemorySession(id);
		} while (sessions.putIfAbsent(session.id(), session) != null);
		return session;
	}

	@Override
	public synchronized void save(Session session) {
		if (session.isInvalidated()) {
			sessions.remove(session.id());
			return;
		}
		sessions.put(session.id(), session);
	}

	@Override
	public synchronized void delete(String id) {
		sessions.remove(id);
	}
}
