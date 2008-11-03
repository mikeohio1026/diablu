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

package pt.citar.diablu.sms2osc.osc;


import de.sciss.net.OSCClient;
import de.sciss.net.OSCMessage;
import java.io.IOException;
import java.net.InetSocketAddress;
import pt.citar.diablu.sms2osc.S2O;


public class S2OOscClient{
    
    private OSCClient oscClient;
    private S2O s2o;
    
    
    public S2OOscClient(S2O s2o)
    {
        try {
            this.s2o = s2o;
            if (this.s2o.getProperties().getUseLoopback().equalsIgnoreCase("true") ) {
                oscClient = OSCClient.newUsing(OSCClient.UDP, 12345, true);
            } else {
                oscClient = OSCClient.newUsing(OSCClient.UDP);
            }
            
        } catch (IOException ex) {
        }
    }
     
    public void send(OSCMessage oscMessage)
    {
        String message = oscMessage.getName();
        for (int i = 0; i < oscMessage.getArgCount(); i++) {
            message += " " + oscMessage.getArg(i);
        }
        
        try {
            InetSocketAddress isa = new InetSocketAddress(s2o.getGui().getHostname(), Integer.parseInt(s2o.getGui().getClientPort()));
            oscClient.setTarget(isa);
            oscClient.start();
            oscClient.send(oscMessage);
            oscClient.stop();
        
        } catch (IOException ex) {
                ex.printStackTrace();
                
        }
    }
    

}
