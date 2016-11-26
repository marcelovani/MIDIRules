//	MIDI Rules | A Rule-Based MIDI Processing System
//	http://www.sourceforge.net/projects/midi-rules/
//
//	Copyright (C) 2006  Christoph Gerkens 
//	chgerkens@users.sourceforge.net
//	
//	This program is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//	
//	This program is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//	
//	You should have received a copy of the GNU General Public License along
//	with this program; if not, write to the Free Software Foundation, Inc.,
//	51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
    
package com.cycosolutions.midirules;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import org.drools.WorkingMemory;

public class EventTimer extends Timer {
	
	private final PropertyChangeSupport changes = new PropertyChangeSupport( this );
	
	private WorkingMemory wm;
	
	protected EventTimer(String name, WorkingMemory wm) {
		super();
		setName(name);
		this.wm = wm;
	}

	public void addPropertyChangeListener(final PropertyChangeListener l) {
	    this.changes.addPropertyChangeListener( l );
	}

	public void removePropertyChangeListener(final PropertyChangeListener l) {
	    this.changes.removePropertyChangeListener( l );
	}
	
	private static Map<String, EventTimer> timers = new HashMap<String, EventTimer>();
	
	public static EventTimer getEventTimer(String timerId, WorkingMemory wm) {
		EventTimer timer = (EventTimer) timers.get(timerId);
		if(timer == null) {
			synchronized(timers) {
				timer = (EventTimer) timers.get(timerId);
				if(timer == null) {
					timer = new EventTimer(timerId, wm);
					timer.start();
					timers.put(timerId, timer);
					wm.assertObject(timer, true);
				}
			}
		}
		return timer;
	}

	
	@Override
	public void setExpired(boolean expired) {
		final boolean oldExpired = isExpired();
		super.setExpired(expired);
		this.changes.firePropertyChange("expired", oldExpired, expired);
	}

	@Override
	public void setExpirationTime(long expirationTime) {
		super.setExpirationTime(expirationTime);
	}

	protected void timerAction() {
		wm.fireAllRules();
		setExpired(false);
	}
}
