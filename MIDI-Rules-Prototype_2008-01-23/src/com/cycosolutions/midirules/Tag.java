//	MIDI Rules | A Rule-Based MIDI Processing System
//	http://www.sourceforge.net/projects/midi-rules/
//
//	Copyright (C) 2007  Christoph Gerkens 
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


/**
 * Tag a object with a name and value.
 * 
 * @author Christoph Gerkens
 *
 */
public class Tag {
	
	private Object taggedObject;
	
	private String name;
	
	private Object value;

	public Tag(Object objectToTag, String name, Object value) {
		super();
		this.taggedObject = objectToTag;
		this.name = name;
		this.value = value;
	}

	public Object getTaggedObject() {
		return taggedObject;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((taggedObject == null) ? 0 : taggedObject.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Tag other = (Tag) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (taggedObject == null) {
			if (other.taggedObject != null)
				return false;
		} else if (!taggedObject.equals(other.taggedObject))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
}
