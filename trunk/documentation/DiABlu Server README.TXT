=====================================
DiABlu Server (README FILE)
=====================================


Statement
---------

The DiABlu System is an application that detects the presence of Bluetooth enabled devices and communicates their names and ids to other applications. 

It's intended to be used in art installations by digital artists who wish to give their audience a novel way to interact with their pieces.

WARNING! THIS SOFTWARE IS A ALPHA VERSION AND COMES WITHOUT ANY WARRANTIES OF ANY TYPE


Requirements
------------

Our project requires two libraries: 

1 - Bluetooth Stack

	WIN SP2/Mac OS	
	Avetana Bluetooth Stack - http://www.avetana-gmbh.de/avetana-gmbh/produkte/jsr82.eng.xml
	It's a commercial one, but you can use their free demo for 14 days.

	WIN SP2 with Windows Bluetooth Stack
	BlueCove Bluetooth Stack - http://bluecove.sourceforge.net/
	Open Source under GNU Library or Lesser General Public License (LGPL) 	


2 - OSC Api

	Platform independent
	NetUtil - http://sciss.de/netutil/
	Open Source under GNU Library or Lesser General Public License (LGPL)  (http://sciss.de/netutil/licenses/NetUtil-License.txt).

To install the libraries simply copy the jar files into the "lib" folder. Within this version we already left a copy of NetUtil(our many many thanks to Hannz Holger Rutz)


Installation
------------

To install the project simply unzip the file "DiABlu Server.zip".

To run the project from the command line, simply type the following:

java -jar "citar.jar" 


Download
--------

You can download the latest version at:
http://soundserver.porto.ucp.pt/diablu/Paginas/downloads.php


