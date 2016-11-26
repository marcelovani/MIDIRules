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

public class SendMidiMessageTimer extends Timer {
	
	private MidiOut midiOut;
	private int midiOutNumber;
	private int channel;
	private int command;
	private int data1;
	private int data2;


	SendMidiMessageTimer() {}

	protected void timerAction() {
		try {
			midiOut.send(midiOutNumber, channel, command, data1, data2);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public int getChannel() {
		return channel;
	}


	public void setChannel(int channel) {
		this.channel = channel;
	}


	public int getCommand() {
		return command;
	}


	public void setCommand(int command) {
		this.command = command;
	}


	public int getData1() {
		return data1;
	}


	public void setData1(int data1) {
		this.data1 = data1;
	}


	public int getData2() {
		return data2;
	}


	public void setData2(int data2) {
		this.data2 = data2;
	}


	public int getMidiOutNumber() {
		return midiOutNumber;
	}


	public void setMidiOutNumber(int midiOutNumber) {
		this.midiOutNumber = midiOutNumber;
	}


	public MidiOut getMidiOut() {
		return midiOut;
	}


	public void setMidiOut(MidiOut midiOut) {
		this.midiOut = midiOut;
	}

}
