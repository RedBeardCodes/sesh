package io.github.redbeardcodes.sesh;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySessionStore implements SessionStore {

	private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	@Override
	public Optional<Session> load(String id) {
		Session s = sessions.get(id);
		if (s.invalidated() || s == null)
			return Optional.ofNullable(null);
		return Optional.ofNullable(s);
	}

	@Override
	public Session create() {
		Session session;
		do {
			String id = "Session" + UUID.randomUUID();
			session = new Session(id);
		} while (sessions.putIfAbsent(session.id(), session) != null);
		return session;
	}

	@Override
	public void save(Session session) {
		if (session.invalidated())
			this.delete(session.id());
		else
			sessions.put(session.id(), session);
	}

	@Override
	public void delete(String id) {
		Session session = sessions.remove(id);
		if (session != null)
			session.invalidate();
	}
}
