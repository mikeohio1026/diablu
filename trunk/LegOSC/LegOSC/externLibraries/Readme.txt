=====================================
DiABlu LegOSC 0.5 (README FILE)
=====================================

LEGAL NOTICE
------------
Copyright(C) 2006 CITAR - Jorge Cardoso (jccardoso@porto.ucp.pt)

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software 
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA



Statement
---------

The DiABlu LegOSC is an application that allows controlling the Lego Mindstorms NXT system using Open Sound Control commands.



Running
-------
In most cases just double click the LegOSC.jar file.

You can also run it in the command line as "java -jar LegOSC.jar".

In order to connect to the NXT Brick you will need to pair it with your computer first and take
note of the name of the COM port that your system is using for serial communication with 
the NXT. Enter that COM port name in LegOSC window.



OSC Messages
------------


The list of currently implemented OSC messages that LegOSC understands is:

/motorForward ii -- The first integer argument is the motor number and the second is the power to apply to the motor. This message will make the specified motor start to rotate at the specified power.
	
/motorSlowStop i -- The integer argument is the motor number. This message stops the specified motor without aplying ``brakes''.
	
/motorBrake i -- The integer argument is the motor number. This message stops the specified motor and aplies ``brakes''.
	
/resetMotor i -- The integer argument is the motor number. This message resets the tachometer of the specified motor.
	
/getMotorTachoCount i -- The integer argument is the motor number. This message asks for the current tacho count of the specified motor and originates a /motorTachoCount message as the reply.
	
/getButtonState i -- The integer argument is the port to which the pressure sensor is attached. This message asks for the current state of the pressure sensor and originates a /buttonState message as the reply.
	
/getLightLevel i -- The integer argument is the port to which the light sensor is attached. This message asks for the current value of the light sensor and originates a /lightLevel message as the reply.
	
/getSoundLevel i -- The integer argument is the port to which the sound sensor is attached. This message asks for the current value of the sound sensor and originates a /soundLevel message as the reply.
	
/getProximityLevel i -- The integer argument is the port to which the ultrasonic sensor is attached. This message asks for the current value of the ultrasonic sensor and originates a /proximityLevel message as the reply.
	
/getBatteryLevel -- This message asks for the current voltage of the battery of the NXT.


Some of the above messages generate a response that LegOSC sends to your application:



/motorTachoCount ii -- Response to /getMotorTachoCount. The first integer argument is the motor number; the second integer argument is the current tacho count for that motor.
  
/buttonState ii -- Response to /getButtonState. The first integer argument is the port number to which the pressure sensor is attached; the second integer argument if the current state of the pressure sensor (0 -- not pressed; 1 -- pressed).
	
/lightLevel ii -- Response to /getLightLevel. The first integer argument is the port number to which the light sensor is attached; the second integer argument if the current value of light sensor.
	
/soundLevel ii -- Response to /getSoundLevel. The first integer argument is the port number to which the sound sensor is attached; the second integer argument if the current value of sound sensor.
	
/proximityLevel ii -- Response to /getProximityLevel. The first integer argument is the port number to which the ultrasonic sensor is attached; the second integer argument if the current value of ultrasonic sensor.
	
/batteryLevel i -- Response to /getBatteryLevel. The integer argument is the current voltage in millivolts.



Download
--------

You can check the news & download the latest version at:
http://diablu.jorgecardoso.eu


Thanks
------
Hannz Holger Rutz for the NetUtil (OSC) library.
