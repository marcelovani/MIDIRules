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

import javax.sound.midi.InvalidMidiDataException;

public class ShortMessage {
	
	private javax.sound.midi.ShortMessage message;
	protected Long time;
	private Integer midiPort;
	
	public ShortMessage(Long time) {
		this.time = time;
		this.message = new javax.sound.midi.ShortMessage();
	}
	
	public ShortMessage(javax.sound.midi.ShortMessage message, Long time, Integer midiPort) {
		this.time = time;
		this.message = message;
		this.midiPort = midiPort;
	}
	
	javax.sound.midi.ShortMessage getDelegate() {
		return message;
	}

	public int getChannel() {
		return message.getChannel() + 1;
	}

	public int getCommand() {
		return message.getCommand();
	}

	public int getData1() {
		return message.getData1();
	}

	public int getData2() {
		return message.getData2();
	}

	public byte[] getMessage() {
		return message.getMessage();
	}

	public int getStatus() {
		return message.getStatus();
	}

	public Long getTime() {
		return time;
	}
	
	public Integer getMidiPort() {
		return midiPort;
	}
	
	public void setMessage(int command, int channel, int data1, int data2) throws InvalidMidiDataException {
		message.setMessage(command, channel - 1, data1, data2);
	}

	public void setMessage(int status, int data1, int data2) throws InvalidMidiDataException {
		message.setMessage(status, data1, data2);
	}

	public void setMessage(int status) throws InvalidMidiDataException {
		message.setMessage(status);
	}


	public String toString() {
		return message.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result
				+ ((midiPort == null) ? 0 : midiPort.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		final ShortMessage other = (ShortMessage) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.getMessage().equals(message.getMessage()))
			return false;
		if (midiPort == null) {
			if (other.midiPort != null)
				return false;
		} else if (!midiPort.equals(other.midiPort))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

}
