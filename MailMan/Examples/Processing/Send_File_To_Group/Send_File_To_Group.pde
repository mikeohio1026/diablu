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
  
  // Change the arguments acordingly
  //                                                                                 File path      | Major Device Class | Minor Device Class | Brand 
  message = new OscMessage("/Diablu/Mailman/SendPathToGroup", new Object[] {"c:\\MailMan\\img.jpg",           "*"        ,         "*"        ,  "*" });
  
  // Change the arguments acordingly
  //                                                                                         File path      | mimetype | Major Device Class | Minor Device Class | Brand 
  //message = new OscMessage("/Diablu/Mailman/SendPathWithMimeToGroup", new Object[] {"c:\\MailMan\\img.jpg","img/jpeg",       "Phone"      ,         "*"        ,"Nokia"});
  
  // Change the arguments acordingly
  //                                                                             File path      | Major Device Class | Minor Device Class | Brand | Total time | Interval 
  //  message = new OscMessage("/Diablu/Mailman/Broadcast", new Object[] {"c:\\MailMan\\img.jpg",         "*"        ,         "*"        ,  "*"  ,    "150"   ,   "50"  });
 
  // Change the arguments acordingly
  //                                                                                 File path      |  mimetype  | Major Device Class | Minor Device Class | Brand | Total time | Interval 
  //message = new OscMessage("/Diablu/Mailman/BroadcastWithMime", new Object[] {"c:\\MailMan\\img.jpg","text/plain",         "*"        ,         "*"        ,  "*",    "100"   ,   "40"  });


  oscClient.send(message, myRemoteLocation);
  println("Sent     - " + message.addrPattern());

}

void oscEvent(OscMessage theOscMessage)
{
  int i;
  println("Recieved - " + theOscMessage.addrPattern());
  
  // Responses to "SendToGroup" Commands
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/NoFilesSent") == 0)
  {
    // Action to take if no files were sent to any devices
    println("No files were sent");
  }
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/SentToDevices") == 0)
  {
    // Action to take if sent failed
    print("Sent files to devices devices: ");
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
  
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/UnknownDevice") == 0)
  {
    // Action to take if device is unknown
    print("Wrong arguments");
  }


  // Response to any command
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/WrongArguments") == 0)
  {
    // Action to take if arguments are wrong
    print("Wrong arguments");
  }
}

