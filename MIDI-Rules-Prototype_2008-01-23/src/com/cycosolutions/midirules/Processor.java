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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.event.BeforeActivationFiredEvent;
import org.drools.event.DefaultAgendaEventListener;

public class Processor implements Runnable, MidiOut, Timers {

	class MidiMessageReceiver implements Receiver {

		Integer midiInNo;
		
		MidiMessageReceiver(int midiInNo) {
			this.midiInNo = midiInNo;
		}
		
		public void send(final MidiMessage message, long timeStamp) {
			FactHandle handle = null;
			if (message instanceof javax.sound.midi.ShortMessage) {
				handle = wm.assertObject(new ShortMessage(
						(javax.sound.midi.ShortMessage) message, System.currentTimeMillis(), midiInNo));
			} else {
				handle = wm.assertObject(message);
			}
			wm.fireAllRules();

			wm.retractObject(handle);
		}

		public void close() {
			// nothing to do
		}
	}

	private Processor() {
	}

	private static Processor instance = new Processor();

	private WorkingMemory wm;
	private MidiDevice[] midiInDevice;
	private MidiDevice[] midiOutDevice;
	private Receiver[] receiver;
	private Transmitter[] transm;
	private int timeStamp = 0;
	private Map<String, Variable> variables = new HashMap<String, Variable>();
	private Map<String, SendMidiMessageTimer> sendMidiMessageTimers = new HashMap<String, SendMidiMessageTimer>();

	public static Processor getInstance() {
		return instance;
	}

	public Variable getVariable(String name) {
		Variable variable = variables.get(name);
		// If no Variable with given name exists, create a new one with value =
		// ""
		if (variable == null) {
			synchronized (variables) {
				variable = variables.get(name);
				if (variable == null) {
					variable = new Variable(name, -1l);
					variables.put(name, variable);
				}
			}
		}
		return variable;
	}

	public SendMidiMessageTimer getSendMidiMessageTimer(String name) {
		SendMidiMessageTimer timer = sendMidiMessageTimers.get(name);
		if (timer == null) {
			synchronized (sendMidiMessageTimers) {
				timer = sendMidiMessageTimers.get(name);
				if (timer == null) {
					timer = new SendMidiMessageTimer();
					timer.start();
					sendMidiMessageTimers.put(name, timer);
				}
			}
		}
		return timer;
	}

	private synchronized long getNextTimeStamp() {
		return timeStamp++;
	}

	public MidiDevice[] getMidiInDevice() {
		return midiInDevice;
	}

	public void setMidiInDevices(MidiDevice[] midiInDevice)
			throws MidiUnavailableException {
		this.midiInDevice = midiInDevice;
		this.transm = new Transmitter[midiInDevice.length];
		for (int i = 0; i < midiInDevice.length; i++) {
			MidiDevice device = midiInDevice[i];
			if (!device.isOpen()) {
				device.open();
			}
			transm[i] = device.getTransmitter();
		}
	}

	public MidiDevice[] getMidiOutDevices() {
		return midiOutDevice;
	}

	public void setMidiOutDevices(MidiDevice[] midiOutDevices)
			throws MidiUnavailableException {
		this.midiOutDevice = midiOutDevices;
		this.receiver = new Receiver[midiOutDevices.length];
		for (int i = 0; i < midiOutDevices.length; i++) {
			MidiDevice device = midiOutDevices[i];
			if (!device.isOpen()) {
				device.open();
			}
			receiver[i] = device.getReceiver();
		}
	}

	public WorkingMemory getWm() {
		return wm;
	}

	public void setWm(WorkingMemory wm) {
		this.wm = wm;
	}

	public void finialize() {
		if (midiInDevice != null) {
			for (MidiDevice device : midiInDevice) {
				if (device.isOpen()) {
					device.close();
				}
			}
		}
		if (midiOutDevice != null) {
			for (MidiDevice device : midiInDevice) {
				if (device.isOpen()) {
					device.close();
				}
			}
		}
	}

	public void run() {
		for (int i = 0; i < transm.length; i++) {
			Transmitter tm = transm[i];
			tm.setReceiver(new MidiMessageReceiver(i + 1));
		}

		try {
			FactHandle initHandle = wm.assertObject(getVariable("init"));
			wm.fireAllRules();
			wm.retractObject(initHandle);

			try {
				while (!Thread.interrupted()) {
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			finialize();
		}
	}

	public void send(int midiOutNumber, int channel, int command, int data1,
			int data2) throws InvalidMidiDataException {
		ShortMessage message = new ShortMessage(System.currentTimeMillis());
		message.setMessage(command, channel, data1, data2);
		try {
			getMidiOut(midiOutNumber).send(message.getDelegate(), getNextTimeStamp());
		} catch(IllegalStateException e) {
			System.out.println("Cannot send message: " + e.getMessage());
		}
	}

	public Receiver getMidiOut(int midiOutNumber) {
		try {
			return receiver[midiOutNumber - 1];
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new IllegalStateException("Midi Out " + midiOutNumber
					+ " is undefined.");
		}
	}

	public void sendDelayed(int midiOutNumber, int channel, int command,
			int data1, int data2, int delayMillis)
			throws InvalidMidiDataException {
		SendMidiMessageTimer timer = new SendMidiMessageTimer();
		timer.setExpirationTime(System.currentTimeMillis() + delayMillis);
		timer.setMidiOut(this);
		timer.setMidiOutNumber(midiOutNumber);
		timer.setChannel(channel);
		timer.setCommand(command);
		timer.setData1(data1);
		timer.setData2(data2);
		timer.start();
	}

	public void scheduleTimer(String timerName, long expirationTime) {
		EventTimer timer = EventTimer.getEventTimer(timerName, this.wm);
		timer.setExpirationTime(expirationTime);
	}

	public void consumeTimer(String timerName) {
		final EventTimer timer = EventTimer.getEventTimer(timerName, this.wm);
		timer.setExpired(false);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("MIDI Rules | Prototype 2007-12-09. Copyright(C) 2006-2007 Christoph Gerkens.");
		
		File ruleFile = null;
		final Properties props = new Properties();
		final String propsFilename = System.getProperty("user.home")
				+ System.getProperty("file.separator") + ".MIDI-Rules";
		final File propFile = new File(propsFilename);
		if (propFile.exists()) {
			props.load(new FileInputStream(propFile));
		}

		if (args.length > 0) {
			ruleFile = new File(args[0]);
		}
		if (ruleFile == null || !ruleFile.exists()) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			final JFileChooser fc = new JFileChooser();
			final String recentRuleFile = props.getProperty("recentRuleFile");
			if (recentRuleFile != null) {
				fc.setSelectedFile(new File(recentRuleFile));
			}

			fc.setDialogTitle("Open rule file");
			fc.addChoosableFileFilter(new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (file.isDirectory())
						return true;
					String filename = file.getName().toLowerCase();
					return filename.endsWith(".drl");
				}

				@Override
				public String getDescription() {
					return "Rule Files (*.drl)";
				}
			});
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				ruleFile = fc.getSelectedFile();
			} else {
				System.exit(0);
			}
		}
		if (ruleFile == null || !ruleFile.exists()) {
			System.err.println("Rule file doesn't exist: " + ruleFile);
			System.exit(1);
		}
		props.setProperty("recentRuleFile", ruleFile.getAbsolutePath());
		props.store(new FileOutputStream(propsFilename), null);
		System.out.println("Using rule file " + ruleFile);
		Processor processor = Processor.getInstance();

		List<MidiDevice> midiIns = new ArrayList<MidiDevice>();
		List<MidiDevice> midiOuts = new ArrayList<MidiDevice>();

		MidiDevice.Info[] deviceInfos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < deviceInfos.length; i++) {
			MidiDevice.Info deviceInfo = deviceInfos[i];
			MidiDevice device;
			try {
				device = MidiSystem.getMidiDevice(deviceInfo);
				if (!(device instanceof Sequencer)
						&& !(device instanceof Synthesizer)) {
					if (device.getMaxTransmitters() == -1
							|| device.getMaxTransmitters() > 0) {
						midiIns.add(device);
					}
					if (device.getMaxReceivers() == -1
							|| device.getMaxReceivers() > 0) {
						midiOuts.add(device);
					}
				}
			} catch (MidiUnavailableException e) {
				System.err.println(deviceInfo + " not availabe.");
			}
		}

		// midi ins
		ArrayList<MidiDevice> selectedIns = new ArrayList<MidiDevice>();
		int numbIn = -1;
		do {
			System.out.println("\nAvailable midi inputs:");
			for (int i = 0; i < midiIns.size(); i++) {
				System.out.println((i + 1)
						+ " = "
						+ ((MidiDevice) midiIns.get(i)).getDeviceInfo().toString());
			}
			System.out.println("<enter> = Finish midi-in selection.");
			System.out.print("\nChoose midi-in #" + (selectedIns.size() + 1)
					+ ": ");
			String chooseIn = new BufferedReader(new InputStreamReader(
					System.in)).readLine();
			try {
				if(chooseIn.equals("")) {
					numbIn = 0;
				} else {
					numbIn = Integer.parseInt(chooseIn);
					final MidiDevice deviceIn = (MidiDevice) midiIns.get(numbIn - 1);
					selectedIns.add(deviceIn);
				}
			} catch (Exception ex) {
				System.out.println("Invalid input: " + chooseIn);
			}
		} while (numbIn != 0);
		processor.setMidiInDevices(selectedIns.toArray(new MidiDevice[selectedIns.size()]));
		
		// midi outs
		ArrayList<MidiDevice> selectedOuts = new ArrayList<MidiDevice>();
		int numbOut = -1;
		do {
			System.out.println("\nAvailable midi outputs:");
			for (int i = 0; i < midiOuts.size(); i++) {
				System.out.println((i + 1)
						+ " = "
						+ ((MidiDevice) midiOuts.get(i)).getDeviceInfo().toString());
			}
			System.out.println("<enter> = Finish midi-out selection.");
			System.out.print("\nChoose midi-out #" + (selectedOuts.size() + 1)
					+ ": ");
			String chooseOut = new BufferedReader(new InputStreamReader(
					System.in)).readLine();
			try {
				if(chooseOut.equals("")) {
					numbOut = 0;
				} else {
					numbOut = Integer.parseInt(chooseOut);
					if (numbOut != 0) {
						final MidiDevice device = midiOuts.get(numbOut - 1);
						selectedOuts.add(device);
					}
				}
			} catch (Exception ex) {
				// invalid input
				System.out.println("Invalid input: " + chooseOut);
				numbOut = -1;
			}
		} while (numbOut != 0);
		processor.setMidiOutDevices(selectedOuts.toArray(new MidiDevice[selectedOuts.size()]));
		
		System.out.print("\nStarting MIDI Rules...");
		PackageBuilderConfiguration pbc = new PackageBuilderConfiguration();
		pbc.setJavaLanguageLevel("1.5");
		PackageBuilder builder = new PackageBuilder(pbc);
		builder.addPackageFromDrl(new InputStreamReader(new FileInputStream(
				ruleFile)), new InputStreamReader(Processor.class
				.getResourceAsStream("/MidiRules.dsl")));
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();
		ruleBase.addPackage(builder.getPackage());
		WorkingMemory wm = ruleBase.newWorkingMemory();
		if ("true".equals(System.getProperty("debug")))
			wm.addEventListener(new DefaultAgendaEventListener() {
				@Override
				public void beforeActivationFired(
						BeforeActivationFiredEvent event) {
					System.out.println("Activating rule: "
							+ event.getActivation().getRule().getName());
					super.beforeActivationFired(event);
				}

			});
		processor.setWm(wm);
		new Thread(processor).start();
		System.out.println("Ready.");
	}
}
