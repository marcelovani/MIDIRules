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

public interface MidiOut {

//	public void send(Integer channel, Integer command, Integer data1, Integer data2) throws InvalidMidiDataException;
//	public void send(Integer channel, Integer command, Integer data1, int data2) throws InvalidMidiDataException;
//	public void send(Integer channel, Integer command, int data1, Integer data2) throws InvalidMidiDataException;
//	public void send(Integer channel, Integer command, int data1, int data2) throws InvalidMidiDataException;
//	public void send(Integer channel, int command, Integer data1, Integer data2) throws InvalidMidiDataException;
//	public void send(Integer channel, int command, Integer data1, int data2) throws InvalidMidiDataException;
//	public void send(Integer channel, int command, int data1, Integer data2) throws InvalidMidiDataException;
//	public void send(Integer channel, int command, int data1, int data2) throws InvalidMidiDataException;
//	public void send(int channel, Integer command, Integer data1, Integer data2) throws InvalidMidiDataException;
//	public void send(int channel, Integer command, Integer data1, int data2) throws InvalidMidiDataException;
//	public void send(int channel, Integer command, int data1, Integer data2) throws InvalidMidiDataException;
//	public void send(int channel, Integer command, int data1, int data2) throws InvalidMidiDataException;
//	public void send(int channel, int command, Integer data1, Integer data2) throws InvalidMidiDataException;
//	public void send(int channel, int command, Integer data1, int data2) throws InvalidMidiDataException;
//	public void send(int channel, int command, int data1, Integer data2) throws InvalidMidiDataException;
	public void send(int midiOutNumber, int channel, int command, int data1, int data2) throws InvalidMidiDataException;
	public void sendDelayed(int midiOutNumber, int channel, int command, int data1, int data2, int delayMillis) throws InvalidMidiDataException;
//	public int intValue(int value);
//	public int intValue(Integer value);
}