package io.github.redbeardcodes.sesh;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an individual session.
 *
 * <p>Sessions must be treated as immutable (hence, using a record) and any updates must be performed
 * on new sessions, rather than mutating state.
 */
public record Session(
		String id,
		Instant createdAt,
		Instant updatedAt,
		Instant expires,
		boolean invalidated,
		Map<String, Object> attributes
) {
	public Session {
		attributes = Map.copyOf(attributes);
	}

	/**
	 * Create a new Session from only an ID. The default expiry is 10 minutes.
	 *
	 * @param id the ID to use for the session
	 * @return the new {@link Session}
	 **/
	public Session(String id) {
		this(id, Instant.now(), Instant.now(), Instant.now().plus(10L, ChronoUnit.MINUTES), false, new HashMap<>());
	}

	/**
	 * Create a new Session from only an ID and expiration.
	 *
	 * @param id the ID to use for the session
	 * @param expiry {@link Instant} expiration time
	 * @return the new {@link Session}
	 **/
	public Session(String id, Instant expiry) {
		this(id, Instant.now(), Instant.now(), expiry, false, new HashMap<>());
	}

	/**
	 * Retrieve an attribute from the session's state and return a new {@link Session}.
	 *
	 * @param name the key to update
	 * @return the updated {@link Session}
	 **/
	public <T> T get(String name, Class<T> type) {
		checkValid();
		return type.cast(attributes().get(name));
	}

	/**
	 * Update a session's attributes and return a new {@link Session}.
	 *
	 * @param name the key to update
	 * @param value the object value to be updated
	 * @return the updated {@link Session}
	 **/
	public Session withAttributes(String name, Object value) {
		checkValid();
		Map<String, Object> copy = new HashMap<>(attributes);
		copy.put(name, value);
		return new Session(id, createdAt(), Instant.now(), expires(), invalidated(), copy);
	}

	/**
	 * Remove a session's attributes and return a new {@link Session}.
	 *
	 * @param name the key to update
	 * @return the updated {@link Session}
	 **/
	public Session remove(String name) {
		checkValid();
		Map<String, Object> copy = new HashMap<>(attributes);
		copy.remove(name);
		return new Session(id, createdAt(), Instant.now(), expires(), invalidated(), copy);
	}

	/**
	 * Invalidate a {@link Session}
	 *
	 * @return the invalidated {@link Session}
	 **/
	public Session invalidate() {
		return new Session(id, createdAt, updatedAt, expires, true, attributes);
	}

	private void checkValid() {
		if (invalidated)
			throw new IllegalStateException("Session has been invalidated.");
	}

}
