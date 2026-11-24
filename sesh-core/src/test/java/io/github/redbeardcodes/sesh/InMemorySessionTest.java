package io.github.redbeardcodes.sesh;

import java.time.LocalTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemorySessionTest {

	@Test
	public void testThatNewSessionsAreSuccessfullyCreated() {
		Session s = new InMemorySession("TestSessionSuccess");
		LocalTime created = LocalTime.ofInstant(s.createdAt(), ZoneId.systemDefault());
		LocalTime lastAccessed = LocalTime.ofInstant(s.lastAccessAt(), ZoneId.systemDefault());
		LocalTime expiry = LocalTime.ofInstant(s.expiresAt(), ZoneId.systemDefault());

		// We convert the session time fields to LocalTime and do comparisons.
		Assertions.assertEquals(LocalTime.now().getHour(), created.getHour(), "created hour is not correct");
		Assertions.assertEquals(LocalTime.now().getMinute(), created.getMinute(), "created hour is not correct");
		Assertions.assertEquals(LocalTime.now().getHour(), lastAccessed.getHour(), "lastAccessed hour is not correct");
		Assertions.assertEquals(LocalTime.now().getMinute(), lastAccessed.getMinute(), "lastAccessed minute is not correct");

		if (LocalTime.now().getMinute() >= 50)
			Assertions.assertEquals(created.plusHours(1).getHour(), expiry.getHour(), "expiry hour is not correct");
		else
			Assertions.assertEquals(created.getHour(), expiry.getHour(), "expiry hour is not correct");

		Assertions.assertEquals(created.plusMinutes(10).getMinute(), expiry.getMinute(), "expiry minute is not correct");
	}

	@Test
	public void testThatDataCanBeAddedToAndRetrievedFromSessions() {
		Session s = new InMemorySession("TestAddingSessionData");
		s.set("foo", "bar");
		Assertions.assertEquals(s.get("foo", String.class), "bar");
	}

	@Test
	public void testThatRemovedDataIsRetrievedAsNull() {
		Session s = new InMemorySession("TestRemovingSessionData");
		s.set("foo", "bar");
		Assertions.assertEquals(s.get("foo", String.class), "bar");
		s.remove("foo");
		Assertions.assertNull(s.get("foo", String.class));
	}

	@Test
	public void testThatInvalidatedSessionsAreEmpty() {
		InMemorySession s = new InMemorySession("TestInvalidatedSession");
		s.set("foo", "bar");
		s.set("fizz", "buzz");
		Assertions.assertEquals(s.get("foo", String.class), "bar");
		Assertions.assertEquals(s.get("fizz", String.class), "buzz");
		s.invalidate();
		Assertions.assertThrows(IllegalStateException.class, () -> s.get("foo", String.class));
		Assertions.assertTrue(s.getSessionData().isEmpty());
	}
}
