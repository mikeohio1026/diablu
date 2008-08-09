/*
 * DiABlu Mailman
 * Copyright (C) 2008-2009, CITAR (Research Centre for Science and Technology in Art)
 *
 * This is part of the DiABlu Project, created by Jorge Cardoso - http://diablu.jorgecardoso.eu
 *
 *
 * Contributors:
 * - Pedro Santos <psantos@porto.ucp.pt>
 * - Jorge Cardoso <jccardoso@porto.ucp.pt>
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */
 
import netP5.*;
import oscP5.*;

OscP5 osc;
NetAddress myRemoteLocation;

OscProperties oscp;
OscP5 oscClient;
OscP5 oscServer;

OscMessage message; 


void setup() 
{
  // Change address and port to where the OSC messages should be sent
  oscClient = new OscP5(this, "127.0.0.1", 12000, OscP5.UDP);
  myRemoteLocation = new NetAddress("127.0.0.1", 12000);
  
  // Choose the port to listen for OSC Messages
  oscServer = new OscP5(this, 12001, OscP5.UDP);
}

void draw() {}

void mousePressed()
{
  
  // Change the filepath and the device UUID acordingly
  //                                                                 |      File path     |  | device UUID |
  //message = new OscMessage("/Diablu/Mailman/SendPath", new Object[] {"c:\\MailMan\\img.jpg", "000b0d05f9b0"});
  
  // Change the filepath, the mimetype and the device UUID of the files acordingly
  //                                                                           |      File path     |  |mimetype|  | device UUID |
  message = new OscMessage("/Diablu/Mailman/SendPathWithMime", new Object[] {"c:\\MailMan\\img.jpg", "img/jpeg", "000b0d05f9b0"});


  oscClient.send(message, myRemoteLocation);
  println("Sent     - " + message.addrPattern());

}

void oscEvent(OscMessage theOscMessage)
{
  int i;
  println("Received - " + theOscMessage.addrPattern());
  
  // Responses to "SendPath" Commands
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/OK") == 0)
  {
    // Action to take if sent successfull
    println("Sent with success");
    message = new OscMessage("/Diablu/Mailman/GetSentFiles", new Object[] {"000b0d05f9b0"});
    oscClient.send(message, myRemoteLocation);
    println("Sent     - " + message.addrPattern());
  }
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/SendFailed") == 0)
  {
    // Action to take if sent failed
    print("Sent failed for devices: ");
    for(i = 1; i < theOscMessage.arguments().length; i++)
    {
      print((String) theOscMessage.arguments()[i] + " ");
    }
    println("");
  }
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/FileNotFound") == 0)
  {
    // Action to take if file not found
    print("File not found: " + theOscMessage.arguments()[0]);
  }
  
  
  // Responses to "GetSentFiles" Command
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/FilesSent") == 0)
  {
    // Action to take when there are sent files
    println("Files sent:");
    for(i = 0; i < theOscMessage.arguments().length; i++)
    {
      println(theOscMessage.arguments()[i]);
    }
  }
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/UnknownDevice") == 0)
  {
    // Action to take if device is unknown
    print("Wrong arguments");
  }
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/NoFilesSent") == 0)
  {
    // Action to take if no files were sent to device
    print("No file sent to this device");
  }
  

  // Response to any command
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/WrongArguments") == 0)
  {
    // Action to take if arguments are wrong
    print("Wrong arguments");
  }
}

