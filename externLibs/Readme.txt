=====================================
DiABlu Scout 0.99 (README FILE)
=====================================

LEGAL NOTICE
------------
Copyright(C) 2006 CITAR - Jorge Cardoso (jccardoso@porto.ucp.pt),Nuno Rodrigues (nunoalexandre.rodrigues@gmail.com)

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software 
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA



Statement
---------

The DiABlu Scout is an application that detects the presence of Bluetooth enabled devices and communicates their names and ids 
to other applications, using the Open Sound Control protocol.

It's intended to be used in art installations by digital artists who wish to give their audience a novel way to interact with 
their pieces.



Running
-------
In most cases just double click the Scout.jar file.

If you use a library other than the one distributed, you should put it under the 'lib' directory and use the following 
command line to launch Scout:
java -classpath lib/[yourBTlibrary.jar];Scout.jar  citar.diablu.bc.DiABluBC


Bluetooth library
-----------------

Scout is distributed with the BlueCove (http://bluecove.sourceforge.net/java) library for Bluetooth. 
This library only works in Windows SP2.
If your wish to use Scout in other platforms we recomend you use the Avetana library:
http://www.avetana-gmbh.de/avetana-gmbh/produkte/jsr82.eng.xml.


Manufacturer Database
---------------------

Scout uses the 'oui.txt' (from http://standards.ieee.org/regauth/oui/oui.txt) to figure out the manufacturer of 
the Bluetooth hardware. The file is parsed at startup so you can update it if you wish (just keep the name).



Command Line Parameters
-----------------------

The system supports the following command line parameters:

hide - This option hides the Graphics User Interface from displaying

btdelay:xxxx - This specifies the Bluetooth Device Discovery delay in xxxx seconds

address:xxx.xxx.xxx.xxx - Specify the Target Machine Address to xxx.xxx.xxx.xxx

port:xxxx - Specify the Target Machine Listening Port to xxxx


OSC Messages
------------


Scout sends the following OSC Messages:

OSC Message       | Parameter Types    | Paramter Names - Description
/DeviceIn         | sssss              | UUID, Friendly Name, Major, Minor, Manufacturer - One message per device that entered the sorroundings. 

/DeviceListIn     | sssss sssss ...    | The same parameters as /DeviceIn, but concatenated for all devices that entered at the same time.

/DeviceOut        | sssss              | Same parameters - One message per device that exited.

/DeviceListOut    | sssss sssss ...    | Works the same way as /DeviceListIn but for devices that exited.

/DeviceList       | sssss sssss ...    | Works the same way as /DeviceListIn but for all devices present. Sent when there is a change in the set of current devices.

/NameChanged      | sssss              | UUID, New Name, Major, Minor, Manufacturer - Sent when a present device changes friendly name.

/DeviceCount      | i                  | Count - Sends the current count of present devices, when there's any change in the set of current devices.


Some messages are redundant and are only meant to facilitate the implementation of the target application.



Bluetooth Device Class Names
----------------------------

The following are the possible values for "major" and "minor" parameters in the OSC messages. 
This list was taken from the "Bluetooth for Java" book by Bruce Hopkins.

Major Classes 
	- Minor Class(es)

"Simulated" 
	- No minor class
"Misc. major device" 
	- No minor class
"Computer"
	- "Unassigned, misc"
	- "Desktop"
	- "Server"
	- "Laptop"
	- "Sub-laptop"
	- "PDA"
	- "Watch size"
	- "Not classified"
"Phone"
	- "Unassigned, misc"
	- "Cellular"
	- "Household cordless"
	- "Smartphone"
	- "Not classified"
"LAN/network acess point"
	- "Fully available"
	- "1-17% utilized"
	- "17-33% utilized"
	- "33-50% utilized"
	- "50-76% utilized"
	- "76-83% utilized"
	- "83-99% utilized"
	- "100% utilized, no service available"
	- "Not classified"
"Audio/video device"
	- "Unassigned, misc"
	- "Headset"
	- "Hands-free device"
	- "Microphone"
	- "VCR"
	- "Video game system"
	- "Not classified"
"Computer peripheral"
	- "Keyboard"
	- "Mouse, trackball, etc"
	- "Remote control"
	- "Not classified"
"Imaging Device"
	- "Display device"
	- "Camera"
	- "Scanner"
	- "Printer"
	- "Not classified"
"Unclassified major device"
	- No minor class
"Not classified"
	- No minor class


Download
--------

You can check the news & download the latest version at:
http://diablu.jorgecardoso.eu


Thanks
------
Hannz Holger Rutz for the NetUtil (OSC) library.
