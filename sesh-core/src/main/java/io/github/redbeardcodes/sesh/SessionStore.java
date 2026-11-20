package io.github.redbeardcodes.sesh;

import java.util.Optional;

public interface SessionStore {
	Optional<Session> load(String id);
	Session create();
	void save(Session session);
	void delete(String id);
}
