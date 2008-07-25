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

import mailman.MailMan;
import mailman.util.datastructures.MailManDevice;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.obex.*;
import java.io.*;
import java.util.Vector;

public class MailManDiscovery implements Runnable {

    private MailMan mailman;
    private DiscoveryAgent agent;
    private MailManDescoveryListner listner;
    private Vector<MailManDevice> currentDevices;
    private Object inquiryLock = new Object();
    private Object serviceLock = new Object();

    public MailManDiscovery(MailMan mailman) {
        this.mailman = mailman;
        this.currentDevices = new Vector();
        listner = new MailManDescoveryListner(mailman);
        try {
            agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
        } catch (BluetoothStateException ex) {
        }
    }

    public void startDeviceInquiry() {

        boolean started = false;

        mailman.getDiscovery().getCurrentDevices().clear();

        try {
            started = agent.startInquiry(DiscoveryAgent.GIAC, listner);

        } catch (BluetoothStateException ex) {
            Logger.getLogger(MailManDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (started) {
            objectWait(inquiryLock);
        }

    }

    public void run() {
        startDeviceInquiry();
    }

    public void notifyInquiry() {
        synchronized (inquiryLock) {
            inquiryLock.notify();
        }

    }

    public void notifyServiceDiscovery() {
        synchronized (serviceLock) {
            serviceLock.notify();
        }
    }

    public void searchServices(MailManDevice device) {
        try {
            int[] attrSet = new int[1];
            attrSet[0] = 0x0005;

            UUID[] uuids = new UUID[1];
            uuids[0] = new UUID(0x1105);

            agent.searchServices(attrSet, uuids, device.getRemoteDevice(), listner);
        } catch (BluetoothStateException ex) {
            Logger.getLogger(MailManDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }

        objectWait(serviceLock);
    }

    public Vector<MailManDevice> getCurrentDevices() {
        return this.currentDevices;
    }

    public void objectWait(Object object) {
        synchronized (object) {
            try {
                object.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(MailManDiscovery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
