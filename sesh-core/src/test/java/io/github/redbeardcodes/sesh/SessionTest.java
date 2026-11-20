package io.github.redbeardcodes.sesh;

import java.time.LocalTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SessionTest {

	@Test
	public void testThatNewSessionsAreSuccessfullyCreated() {
		Session s = new InMemorySession("TestSession");
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
		s.invalidate();
	}
}
