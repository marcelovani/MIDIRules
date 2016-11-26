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
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class ReceiverMidiOut {

	private Receiver recv;
	private long timeStamp = 0;
	
	public ReceiverMidiOut(Receiver recv) {
		this.recv = recv;
	}

	public int intValue(int value) {
		return value;
	}

	public int intValue(Integer value) {
		return value.intValue();
	}
	
	private synchronized long getNextTimeStamp() {
		return timeStamp++;
	}

	public void send(int channel, int command, int data1, int data2) throws InvalidMidiDataException {
		ShortMessage message = new ShortMessage();
		message.setMessage(command, channel, data1, data2);
		recv.send(message, getNextTimeStamp());
	}
}
