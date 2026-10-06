package io.github.redbeardcodes.sesh;

import java.time.Instant;

public interface Session {

	String id();

	<T> T get(String name, Class<T> type);
	void set(String name, Object value);
	void remove(String name);

	Instant createdAt();
	Instant lastAccessAt();
	Instant expiresAt();

	void invalidate();

}
