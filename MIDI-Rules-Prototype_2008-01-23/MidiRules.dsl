# Domain-specific rule language (DSL) for processing midi messages. 
#
# MIDI Rules | A Rule-Based MIDI Processing System
# http://www.sourceforge.net/projects/midi-rules/
#
# Copyright (C) 2006  Christoph Gerkens 
# chgerkens@users.sourceforge.net
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
#
[then]Consume received msg=retract(received_message);
[when]Receive msg with channel : {channel} , command : {command} , data1 : {data1} , data2 : {data2}=received_message : com.cycosolutions.midirules.ShortMessage(channel == {channel}, command == {command}, data1 == {data1}, data2 == {data2}, received_message_midi_port : midiPort, received_message_time : time); #Implicit variables received_message_midi_port, received_message_time
[when]Receive msg on midi in {midi_in_number} with channel : {channel} , command : {command} , data1 : {data1} , data2 : {data2}=received_message : com.cycosolutions.midirules.ShortMessage(channel == {channel}, command == {command}, data1 == {data1}, data2 == {data2}, midiPort == {midi_in_number}, received_message_time : time); #Implicit variable received_message_time
[then]Send msg to midi out {midi_out_number} with channel : {channel} , command : {command} , data1 : {data1} , data2 : {data2}=com.cycosolutions.midirules.Processor.getInstance().send({midi_out_number}, {channel}, {command}, {data1}, {data2});
[when]Receive msg=received_message : com.cycosolutions.midirules.ShortMessage(received_message_channel : channel, received_message_command: command, received_message_data1 : data1, received_message_data2 : data2, received_message_midi_port : midiPort, received_message_time : time);# Implicit variables received_message_channel, received_message_command, received_message_data1, received_message_data2, received_message_midi_port, received_message_time
[when]midi in is equal to {midi_in_number}=eval(received_message_midi_port == {midi_in_number}) 
[when]channel is equal to {channel}=eval(received_message_channel == {channel})
[when]channel is less than {channel}=eval(received_message_channel < {channel})
[when]channel is greater than {channel}=eval(received_message_channel > {channel})
[when]command is equal to {command}=eval(received_message_command.intValue() == {command})
[when]data1 is equal to {data1}=eval(received_message_data1 == {data1})
[when]data1 is less than {data1}=eval(received_message_data1 < {data1})
[when]data1 is greater than {data1}=eval(received_message_data1  > {data1})
[when]data2 is equal to {data2}=eval(received_message_data2  == {data2})
[when]data2 is less than {data2}=eval(received_message_data2 < {data2})
[when]data2 is greater than {data2}=eval(received_message_data2 > {data2})
[when]tagged with {name}=com.cycosolutions.midirules.Tag(taggedObject == received_message, name == "{name}")
[when]tagged with {name} : {value}=com.cycosolutions.midirules.Tag(taggedObject == received_message, name == "{name}", value == {value})
[when]time is at least {delay}ms later then {other_time}=eval(received_message_time - {other_time} >= {delay})
[when]time is at most {ms} later then {other_time}=eval(received_message_time - other_time <= {ms})
[then]Pass msg through to midi out {midi_out_number}=com.cycosolutions.midirules.Processor.getInstance().send({midi_out_number}, received_message.getChannel(), received_message.getCommand(), received_message.getData1(), received_message.getData2());
[when]Initialization=com.cycosolutions.midirules.Variable (name == "init")
[then]Set variable "{name}" to {value}=com.cycosolutions.midirules.Processor.getInstance().getVariable("{name}").setValue({value});
[then]Create variable "{name}"=assert(com.cycosolutions.midirules.Processor.getInstance().getVariable("{name}"), true);
[then]Send msg "{key}" in {delay}ms to midi out {midi_out_number} with channel : {channel} , command : {command} , data1 : {data1} , data2 : {data2}=com.cycosolutions.midirules.SendMidiMessageTimer timer = com.cycosolutions.midirules.Processor.getInstance().getSendMidiMessageTimer("{key}"); timer.setMidiOut(com.cycosolutions.midirules.Processor.getInstance()); timer.setMidiOutNumber({midi_out_number}); timer.setChannel({channel}); timer.setCommand({command}); timer.setData1({data1}); timer.setData2({data2}); timer.setExpirationTime(System.currentTimeMillis() + {delay});
[then]Send msg in {delay}ms to midi out {midi_out_number} with channel : {channel} , command : {command} , data1 : {data1} , data2 : {data2}=com.cycosolutions.midirules.Processor.getInstance().sendDelayed({midi_out_number}, {channel}, {command}, {data1}, {data2}, {delay});
[when]Variable "{name}" is equal to {value}=com.cycosolutions.midirules.Variable (name == "{name}", value == {value});
[when]Variable "{name}" is not equal to {value}=com.cycosolutions.midirules.Variable (name == "{name}", value != {value});
[when]Variable "{name}" is less than {value}=com.cycosolutions.midirules.Variable (name == "{name}", value < ({value}));
[when]Variable "{name}" is greater than {value}=com.cycosolutions.midirules.Variable (name == "{name}", value > ({value}));
[then]Trigger timer "{name}" at time {time}=com.cycosolutions.midirules.Processor.getInstance().scheduleTimer("{name}", {time});
[then]Trigger timer "{name}"=com.cycosolutions.midirules.Processor.getInstance().scheduleTimer("{name}", System.currentTimeMillis());
[then]Trigger timer "{name}" in {delay}=com.cycosolutions.midirules.Processor.getInstance().scheduleTimer("{name}", System.currentTimeMillis() + {delay});
[when]Timer "{name}" triggered=com.cycosolutions.midirules.EventTimer (name == "{name}", timer_time : expirationTime, expired == true); #Implicit variable timer_time
[then]Consume timer "{name}"=com.cycosolutions.midirules.Processor.getInstance().consumeTimer("{name}");
[then]Tag received message with {name}=assert(new com.cycosolutions.midirules.Tag(received_message, "{name}", null));
[then]Tag received message with {name} : {tag_value}=assert(new com.cycosolutions.midirules.Tag(received_message, "{name}", {value}));
[then]Log "{message}"=System.out.println("{message}");// Use this log expression only during development. Remove log statements from production rule file.
