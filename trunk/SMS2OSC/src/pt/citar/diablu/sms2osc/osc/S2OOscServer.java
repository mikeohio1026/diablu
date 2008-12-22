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


import de.sciss.net.OSCServer;
import java.io.IOException;
import java.util.logging.Level;
import pt.citar.diablu.sms2osc.S2O;


public class S2OOscServer {

    private S2O s2o;
    private S2OOscListener oscListener;
    private OSCServer oscServer;
    private boolean connected;

    public S2OOscServer(S2O s2o) {
        this.s2o = s2o;
        connected = false;
        this.oscListener = new S2OOscListener(s2o);

    }

    public void start() {

        try {
            oscServer = OSCServer.newUsing(OSCServer.UDP, Integer.parseInt(s2o.getGui().getServerPort()), true);
            oscServer.addOSCListener(oscListener);
            oscServer.start();
            connected = true;
            s2o.getGui().serverButtonStop(connected);
            s2o.getProperties().setProperty("OutgoingPort", s2o.getGui().getServerPort());
            s2o.getLogger().log(Level.INFO, "OSC Server started on port " + s2o.getGui().getServerPort());
        } catch (IOException ex) {
            oscServer.dispose();
            connected = false;
            s2o.getGui().serverButtonStop(!connected);
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        }
    }

    public void stop() {
       
        oscServer.dispose();
        while (!oscServer.isActive())
        {
            connected = false;
            s2o.getGui().serverButtonStop(connected);
            break;
        }
        s2o.getLogger().log(Level.INFO, "OSC Server stopped");
    }

    public boolean isConnected() {
        return connected;
    }
    
    
}
