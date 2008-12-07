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

package pt.citar.diablu.mailman.osc;

import pt.citar.diablu.mailman.MailMan;
import pt.citar.diablu.mailman.util.MailManLogger;
import de.sciss.net.OSCClient;
import de.sciss.net.OSCMessage;
import java.io.IOException;
import java.net.InetSocketAddress;


public class MailManOscClient{
    
    private OSCClient oscClient;
    private MailMan mailman;
    
    
    public MailManOscClient(MailMan mailman)
    {
        try {
            this.mailman = mailman;
            if (this.mailman.getProperties().getProperty("UseLoopback", "true").equalsIgnoreCase("true") ) {
                oscClient = OSCClient.newUsing(OSCClient.UDP, 12345, true);
            } else {
                oscClient = OSCClient.newUsing(OSCClient.UDP);
            }
            
        } catch (IOException ex) {
             mailman.getLogger().log(MailManLogger.OTHER, "Error Creating OSC Client");
        }
    }
     
    public void send(OSCMessage oscMessage)
    {
        String message = oscMessage.getName();
        for (int i = 0; i < oscMessage.getArgCount(); i++) {
            message += " " + oscMessage.getArg(i);
        }
        
        try {
            InetSocketAddress isa = new InetSocketAddress(mailman.getGui().getIpAddress(), Integer.parseInt(mailman.getGui().getClientPort()));
            mailman.getLogger().log(MailManLogger.OTHER, "Sending OSC message to: " + isa.toString());
            oscClient.setTarget(isa);
            oscClient.start();
            oscClient.send(oscMessage);
            mailman.getLogger().log(MailManLogger.OSC_MESSAGE, "OSC message sent: " + message);
            oscClient.stop();
        
        } catch (IOException ex) {
                ex.printStackTrace();
                mailman.getLogger().log(MailManLogger.OTHER, "Error Sending OSC Message");
           
        }
    }
    

}
