/*
 * DiABlu SMS2OSC
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


import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import java.net.SocketAddress;
import java.util.logging.Level;
import pt.citar.diablu.sms2osc.S2O;

public class S2OOscListener implements OSCListener {

    S2O s2o;

    public S2OOscListener(S2O s2o) {
        this.s2o = s2o;
    }

    public void messageReceived(OSCMessage msg, SocketAddress sender, long time) {
        s2o.getLogger().log(Level.INFO, "OSC message received: " +  msg.getName());
        if(msg.getName().compareTo("/diablu/sms2osc/sendsms") == 0)
            sendSms(msg);
   }
   
    private void sendSms(OSCMessage msg)
    {
        s2o.getBtConnection().sendMessage((String) msg.getArg(0), (String) msg.getArg(1));

    }
}




