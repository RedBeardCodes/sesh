package io.github.redbeardcodes.sesh;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemorySessionStoreTest {

	@Test
	public void testThatSessionStoreCreatesAndDestroysNewSession() {
		InMemorySessionStore sessionStore = new InMemorySessionStore();
		Session session = sessionStore.create();
		Assertions.assertFalse(session.invalidated());
		sessionStore.delete(session.id());
		Session updated = session.invalidate();
		Assertions.assertTrue(updated.invalidated());
	}

	@Test
	public void testThatSessionsThatAreInvalidatedAreNullWhenCalledViaLoad() {
		InMemorySessionStore sessionStore = new InMemorySessionStore();
		Session session = sessionStore.create();
		Assertions.assertFalse(session.invalidated());
		Session invalidated = session.invalidate();
		sessionStore.save(invalidated);
		Optional<Session> retrieved = sessionStore.load(session.id());
		Assertions.assertTrue(retrieved.isEmpty());
	}
}
