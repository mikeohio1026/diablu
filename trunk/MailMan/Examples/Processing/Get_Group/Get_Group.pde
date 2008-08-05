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
  
  // Get the devices that belong to a group
  //                                                                  Major Device Class | Minor Device Class | Brand 
  message = new OscMessage("/Diablu/Mailman/GetGroup", new Object[] {         "*"        ,         "*"        ,  "*" });

  oscClient.send(message, myRemoteLocation);
  println("Sent     - " + message.addrPattern());

}

void oscEvent(OscMessage theOscMessage)
{
  println("Received - " + theOscMessage.addrPattern());
  
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/GroupDefinition") == 0)
  {
    
    // Send the file to each device that belongs to the group
    for(int i = 0; i < theOscMessage.arguments().length; i++)
    {
      println("Sending file to device: " + theOscMessage.arguments()[i]);
      message = new OscMessage("/Diablu/Mailman/SendPath", new Object[] {"c:\\MailMan\\img.jpg", (String) theOscMessage.arguments()[i]});
      oscClient.send(message, myRemoteLocation);
      println("Sent     - " + message.addrPattern());
    }
  }
  
}

