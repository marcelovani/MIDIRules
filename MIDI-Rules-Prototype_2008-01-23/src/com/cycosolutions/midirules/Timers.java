package com.cycosolutions.midirules;

public interface Timers {

	public void scheduleTimer(String timerName, long expirationTime);
	
	public void consumeTimer(String timerName);

}