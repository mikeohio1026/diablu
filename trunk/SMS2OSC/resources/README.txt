=====================================
DiABlu SMS2OSC  (README FILE)
=====================================

LEGAL NOTICE
------------

DiABlu SMS2OSC
Copyright (C) 2008-2009, CITAR (Research Centre for Science and Technology in Art)

This is part of the DiABlu Project, created by Jorge Cardoso - http://diablu.jorgecardoso.eu

Contributors:
- Pedro Santos <psantos@porto.ucp.pt>
- Jorge Cardoso <jccardoso@porto.ucp.pt>

This program is free software; you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation; either version 2 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program;
if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
MA 02111-1307 USA
 





Running
-------
In most cases just double click the SMS2OSC.jar file.

You can also run it in the command line as "java -jar SMS2OSC.jar".

Mailman has been tested on Windows.


OSC Messages
------------


The list of currently implemented OSC messages that Mailman uses is the following.

Messages your application can listen to:

/diablu/sms2osc/sms ss
Arguments: phone number, message.
Description: Indicates that a sms was received. 

/diablu/sms2osc/"command"
Arguments: the arguments of the command
Description: indicates that a sms with a valid command was received


/diablu/sms2osc/argumentsError s
Arguments: message
Description: indicates that a sms with a valid command was received but with invalid arguments

/diablu/sms2osc/commandNotFound s
Arguments: message
Description: indicates that a sms with an invalid command was received


 
Messages that SMS2OSC understans:

/Diablu/Mailman/sendSms ss

Arguments: number, message
Description: Sends a sms with "message" to "number"


Files
-----


config.ini - This file contains the properties of the application and can be used
to configure the followin parameters:
	
	- BaudRate
	- UseLoopback
	- RemoteIP
	- Gateway
	- OutgoingPort
	- IncomingPort
	- ComPort


Download
--------

You can check the news & download the latest version at:
http://diablu.jorgecardoso.eu


Known Issues
------------



Thanks
------
Hannz Holger Rutz for the NetUtil (OSC) library.
