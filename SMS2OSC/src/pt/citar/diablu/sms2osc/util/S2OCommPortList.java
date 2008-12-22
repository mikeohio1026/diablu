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

package pt.citar.diablu.sms2osc.util;

import java.util.Enumeration;
import java.util.Vector;
import org.smslib.helper.CommPortIdentifier;

public class S2OCommPortList {

    Vector<String> portList;

    public S2OCommPortList() {
        portList = new Vector<String>();
        Enumeration pList = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier commPort;
        while (pList.hasMoreElements()) {
            commPort = (CommPortIdentifier) pList.nextElement();
            if (commPort.getPortType() == 1) {
                portList.add(commPort.getName());
            }
        }
    }

    public Vector<String> getCommPorts() {
        return portList;
    }

    public String getFirst() {
        if (portList.size() > 0) {
            return portList.elementAt(0);
        }
        return "";
    }
}
