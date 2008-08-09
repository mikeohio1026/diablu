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

package mailman.bluetooth.discovery;

import mailman.*;
import mailman.util.MailManGroupGetter;
import mailman.util.datastructures.MailManDevice;
import javax.bluetooth.*;


public class MailManDescoveryListner implements DiscoveryListener {

    MailMan mailman;
    MailManGroupGetter groupGetter;

    public MailManDescoveryListner(MailMan mailman) {
        this.mailman = mailman;
    }
    
    public MailManDescoveryListner(MailMan mailman, MailManGroupGetter groupGetter) {
        this.mailman = mailman;
        this.groupGetter = groupGetter;
    }
    

    
    public void servicesDiscovered(int transID, ServiceRecord[] serviceRecord) {

        for (ServiceRecord sr : serviceRecord) {

            String btgoepAdress = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, true);
            MailManDevice device = mailman.getKnownDevices().get(sr.getHostDevice().getBluetoothAddress());
            if (btgoepAdress != null) {
                device.setBtgoepAdress(btgoepAdress);
                device.setHasOBEX(true);
            }
        }
    }

    public void serviceSearchCompleted(int arg0, int arg1) {
        mailman.getDiscovery().notifyServiceDiscovery();
    }

    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        groupGetter.getAllDevices().add(new MailManDevice(remoteDevice, deviceClass));
        //mailman.getGroupGetter().getAllDevices().add(new MailManDevice(remoteDevice, deviceClass));
    }

    public void inquiryCompleted(int discType) {
        mailman.getDiscovery().notifyInquiry();
    }
}
