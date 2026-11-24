package io.github.redbeardcodes.sesh;

import java.util.Optional;

/**
 * Provides storage and retrieval of sessions.
 *
 * <p>Implementations must support concurrent access from multiple threads.
 * Session creation, retrieval, persistence, and deletion must be
 * thread-safe.
 *
 * <p>Implementations must prevent invalidated sessions from being
 * persisted or resurrected.
 */
public interface SessionStore {
	Session create();
	Optional<Session> load(String id);
	void save(Session session);
	void delete(String id);
}
