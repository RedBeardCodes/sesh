package io.github.redbeardcodes.sesh;

public interface SessionStore {
	Session load(String id);
	Session create();
	void save(Session session);
	void delete(String id);
}
