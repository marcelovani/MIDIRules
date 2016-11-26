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

public class Variable {
	
	private final PropertyChangeSupport changes = new PropertyChangeSupport( this );
	
	public void addPropertyChangeListener(final PropertyChangeListener l) {
	    this.changes.addPropertyChangeListener( l );
	}

	public void removePropertyChangeListener(final PropertyChangeListener l) {
	    this.changes.removePropertyChangeListener( l );
	}
	
	private String name;
	private Long value;

	Variable(String name, Long value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public Long getValue() {
		return value;
	}
	
	public void setValue(Long value) {
		Long oldValue = this.value;
		this.value = value;
		this.changes.firePropertyChange("value", oldValue, value);
	}
	
	public void setValue(Integer value) {
		Long oldValue = this.value;
		this.value = new Long(value);
		this.changes.firePropertyChange("value", oldValue, value);
	}
	
}
