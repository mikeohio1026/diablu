=====================================
DiABlu Mailman 0.5 (README FILE)
=====================================

LEGAL NOTICE
------------

DiABlu Mailman
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
In most cases just double click the DiABluMailman.jar file.

You can also run it in the command line as "java -jar DiABluMailman.jar".

To run without the GUI use the comand line "java -jar DiABluMailman.jar -hideGUI".





OSC Messages
------------


The list of currently implemented OSC messages that Mailman uses is the following.

Messages your application can listen to:

/Diablu/Mailman/ReceivePath ssss
Arguments: uuid, original file name, mimetype (may be empty), path to file.

/Diablu/Mailman/OK s
Atguments: path.
Description: Sent in response to other messages to indicate success.

/Diablu/Mailman/SendFailed s[s...s] 
Arguments: path, list of devices
Description: Sent in response to other messages. Indicates which devices didn't recieve the content.

/Diablu/Mailman/WrongArguments
Description: Sent in response to other messages to indicate that the message arguments are incorrect.

/Diablu/Mailman/GroupDefinition sss[s...s]
Arguments: major device class, minor device classe, brand, list of devices
Description: Sent in response to the "/Diablu/Mailman/GetGroup" message and indicates a list of devices that belong to a certain the group.

/Diablu/Mailman/FileNotFound
Description: Indicates that the file to be sent does not exist.

/Diablu/Mailman/SentToDevices [s...s]
Arguments: list of devices
Description: Indicates which devices received a given file.
   
/Diablu/Mailman/NoFilesSent
Description: Indicates that the file was not sent to any device.

/Diablu/Mailman/FilesSent [s...s]
Arguments: list of files
Description: Indicates the files that were sent to a device.

/Diablu/Mailman/UnknownDevice
Description: Indicates that the device specified is not known (malformed device UUID)

/Diablu/Mailman/NoFilesReceived
Description: Indicates that no file was received from the device.

/Diablu/Mailman/FilesSent [s...s]
Arguments: list of files
Description: Indicates the files that were received from a device.

Messages that Mailman understans:

/Diablu/Mailman/SendPath s[s...s]

Arguments: path to file, list of devices
Description: Sends a file indicated by a path to a device, or list o devices.
Responses:
    - /Diablu/Mailman/OK
    - /Diablu/Mailman/SendFailed
    - /Diablu/Mailman/WrongArguments
    - /Diablu/Mailman/FileNotFound


/Diablu/Mailman/SendPathToGroup ssss 

Arguments: path to file, major device classe, minor device class, brand
Description: Sends a file to a group o devices defined by de major device classe, minor device class and brand.
Responses: 
    - /Diablu/Mailman/WrongArguments 
    - /Diablu/Mailman/SentToDevices
    - /Diablu/Mailman/NoFilesSent


/Diablu/Mailman/SendPathWithMime ss[s...s]

Arguments: path to file, mimetype, list of devices
Description: Similar to /Diablu/Mailman/SendPath but enables the user to define the mimetype of the file.
Responses:
    - /Diablu/Mailman/OK
    - /Diablu/Mailman/SendFailed
    - /Diablu/Mailman/WrongArguments
    - /Diablu/Mailman/FileNotFound


/Diablu/Mailman/SendPathWithMimeToGroup sssss

Arguments: path to file, mimetype, major device classe, minor device class, brand
Description: Similar to /Diablu/Mailman/SendPathToGroup but enables the user to define the mimetype of the file.
Responses: 
    - /Diablu/Mailman/WrongArguments 
    - /Diablu/Mailman/SentToDevices
    - /Diablu/Mailman/NoFilesSent

/Diablu/Mailman/Broadcast sssii 
Arguments: path to file, major device class, minor device class, brand, total time, time between discoveries
Description: Sends the desired file to every device that is found that belongs to the defined group. The file will be sent 
Responses: 
    - /Diablu/Mailman/WrongArguments 
    - /Diablu/Mailman/SentToDevices
    - /Diablu/Mailman/NoFilesSent

/Diablu/Mailman/BroadcastWithMime ssssii
Arguments: path to file, mimetype, major device class, minor device class, brand, total time, time between discoveries
Description: Sends the desired file to every device that is found that belongs to the defined group. The file will be sent 
Responses: 
    - /Diablu/Mailman/WrongArguments 
    - /Diablu/Mailman/SentToDevices
    - /Diablu/Mailman/NoFilesSent

/Diablu/Mailman/GetGroup sss
Arguments: major device class, minor device class, brand
Description: Searches for all devices that belong to the specified group
Respones:
    - /Diablu/Mailman/GroupDefinition
    - /Diablu/Mailman/WrongArguments 
 

/Diablu/Mailman/GetSentFiles s
Arguments: device
Description: Returns all files sent to a device
Respones:
    - /Diablu/Mailman/FilesSent
    - /Diablu/Mailman/NoFilesSent
    - /Diablu/Mailman/UnknownDevice
    - /Diablu/Mailman/WrongArguments 

/Diablu/Mailman/GetRecievedFiles s 
Arguments: device
Description: Returns all files received from a device
Respones:
    - /Diablu/Mailman/FilesReceived
    - /Diablu/Mailman/NoFilesReceived
    - /Diablu/Mailman/UnknownDevice
    - /Diablu/Mailman/WrongArguments 


Download
--------

You can check the news & download the latest version at:
http://diablu.jorgecardoso.eu


Thanks
------
Hannz Holger Rutz for the NetUtil (OSC) library.
