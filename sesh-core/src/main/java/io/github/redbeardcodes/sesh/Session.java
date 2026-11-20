package io.github.redbeardcodes.sesh;

import java.time.Instant;

/**
 * Represents an individual session.
 *
 * <p>Implementations must support concurrent access from multiple threads.
 * Implementations must ensure thread-safe access to session attributes
 * and lifecycle operations.
 *
 * <p>Invalidation must be atomic and irreversible. Once invalidated,
 * subsequent operations must not permit reading or modifying session data.
 */
public interface Session {

	String id();

	<T> T get(String name, Class<T> type);
	void set(String name, Object value);
	void remove(String name);

	Instant createdAt();
	Instant lastAccessAt();
	Instant expiresAt();

	void invalidate();
	boolean isInvalidated();

}
