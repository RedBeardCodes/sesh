package io.github.redbeardcodes.sesh;

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

}
