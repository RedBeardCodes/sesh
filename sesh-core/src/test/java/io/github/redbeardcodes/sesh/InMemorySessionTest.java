package io.github.redbeardcodes.sesh;

import java.time.LocalTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemorySessionTest {

	@Test
	public void testThatNewSessionsAreSuccessfullyCreated() {
		Session s = new Session("TestSessionSuccess");
		LocalTime created = LocalTime.ofInstant(s.createdAt(), ZoneId.systemDefault());
		LocalTime lastAccessed = LocalTime.ofInstant(s.updatedAt(), ZoneId.systemDefault());
		LocalTime expiry = LocalTime.ofInstant(s.expires(), ZoneId.systemDefault());

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
		Session s = new Session("TestAddingSessionData").withAttributes("foo", "bar");
		Assertions.assertEquals(s.get("foo", String.class), "bar");
	}

	@Test
	public void testThatRemovedDataIsRetrievedAsNull() {
		Session s = new Session("TestRemovingSessionData").withAttributes("foo", "bar");
		Assertions.assertEquals(s.get("foo", String.class), "bar");
		Session updated = s.remove("foo");
		Assertions.assertNull(updated.get("foo", String.class));
	}

	@Test
	public void testThatInvalidatedSessionsAreEmpty() {
		Session s = new Session("TestInvalidatedSession")
			.withAttributes("foo", "bar")
			.withAttributes("fizz", "buzz");
		Assertions.assertEquals(s.attributes().get("foo").toString(), "bar");
		Assertions.assertEquals(s.attributes().get("fizz").toString(), "buzz");
		Session updated = s.invalidate();
		Assertions.assertThrows(IllegalStateException.class, () -> updated.get("foo", String.class));
		Assertions.assertTrue(updated.invalidated());
	}
}
