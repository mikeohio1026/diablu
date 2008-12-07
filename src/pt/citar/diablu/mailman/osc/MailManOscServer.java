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
import pt.citar.diablu.mailman.gui.MailManGUI;
import de.sciss.net.OSCServer;
import java.io.IOException;


public class MailManOscServer {

    private MailMan mailman;
    private MailManOscListener oscListener;
    private OSCServer oscServer;

    public MailManOscServer(MailMan mailman) {
        this.mailman = mailman;
        this.oscListener = new MailManOscListener(mailman);

    }

    public void start() {

        try {
            oscServer = OSCServer.newUsing(OSCServer.UDP, Integer.parseInt(mailman.getGui().getServerPort()), true);
            oscServer.addOSCListener(oscListener);
            oscServer.start();
            mailman.getGui().changeStartButtonState(MailManGUI.STOP);
            mailman.getLogger().log(MailManLogger.OTHER, "Server started on port " + mailman.getGui().getServerPort());

        } catch (IOException ex) {

            oscServer.dispose();
            mailman.getGui().changeStartButtonState(MailManGUI.START);
            mailman.getLogger().log(MailManLogger.OTHER, "Port " + mailman.getGui().getServerPort() + " already in use.");

        }
    }

    public void stop() {
       
        oscServer.dispose();
        while (!oscServer.isActive())
        {
            mailman.getGui().changeStartButtonState(MailManGUI.START);
            mailman.getLogger().log(MailManLogger.OTHER, "Server closed on port " + mailman.getGui().getServerPort());
            break;
        }
    }
}
