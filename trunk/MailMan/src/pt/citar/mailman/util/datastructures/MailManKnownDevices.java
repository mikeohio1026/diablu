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
package pt.citar.mailman.util.datastructures;

import pt.citar.mailman.MailMan;
import java.util.Hashtable;


public class MailManKnownDevices {
    
    MailMan mailman;
    private Hashtable<String, MailManDevice> knownDevices = new Hashtable<String, MailManDevice>();;

    public MailManKnownDevices(MailMan mailman) {
        this.mailman = mailman;
    }

    public Hashtable<String, MailManDevice> getDevices() {
        return knownDevices;
    }

    public void add(String deviceUUID) {
        if (!knownDevices.containsKey(deviceUUID)) {
            MailManDevice device = new MailManDevice(new MailManRemoteDevice(deviceUUID));
            knownDevices.put(deviceUUID, device);
            mailman.getDiscovery().searchServices(device);
           
        }
        else
        {
            MailManDevice device = knownDevices.get(deviceUUID);
            device.setHasOBEX(false);
            mailman.getDiscovery().searchServices(device);
        }
    }
    
    
    public MailManDevice get(String deviceUUID) {
        return knownDevices.get(deviceUUID);
    }
    
    public void addReceivedFiles(String device, String filename)
    {
        knownDevices.get(device.toUpperCase()).getReceivedFiles().add(filename);
    }
    
    public void addSentFiles(String device, String filename)
    {
        knownDevices.get(device.toUpperCase()).getSentFiles().add(filename);
    }
    
    
           
    

}
