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

package mailman.bluetooth;

import mailman.*;
import mailman.util.MailManLogger;
import java.io.IOException;
import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.obex.SessionNotifier;
import mailman.util.MailManUtil;

public class MailManBTFileReceiver implements Runnable {

    private static final UUID OBEX_OBJECT_PUSH = new UUID(0x1105);
    private static final String SERVER_NAME = "DiABlu MailMan";
    private static final int OBJECT_TRANSFER_SERVICE = 0x100000;
    private static final short ATTR_SUPPORTED_FORMAT_LIST = 0x0303;
    private static final short UUID_PUBLIC_BROWSE_GROUP = 0x1002;
    private static final short ATTR_BROWSE_GROUP = 0x0005;
    
    private MailMan mailman;
    private LocalDevice localDevice;
    private SessionNotifier serverConnection;

    public MailManBTFileReceiver(MailMan mailman) {
        this.mailman = mailman;
    }

    public void run() {
        try {

            localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);
            serverConnection = (SessionNotifier) Connector.open("btgoep://localhost:" + OBEX_OBJECT_PUSH + ";name=" + SERVER_NAME);

            ServiceRecord record = localDevice.getRecord(serverConnection);
            record = configureServiceRecord(record);
            localDevice.updateRecord(record);

            while (true) {
                try {
                    MailManBTRequestHandler handler = new MailManBTRequestHandler(mailman);
                    Connection conn = serverConnection.acceptAndOpen(handler);
                    handler.setAddress(RemoteDevice.getRemoteDevice(conn).getBluetoothAddress());
                } catch (IOException ex) {
                    mailman.getLogger().log(MailManLogger.OTHER, "Error accepting connection from remote device");
                }
            }
            

        } catch (BluetoothStateException ex) {
            mailman.getLogger().log(MailManLogger.OTHER, "Local Bluetooth device not found");
        } catch (ServiceRegistrationException ex) {
            mailman.getLogger().log(MailManLogger.OTHER, "Unable to update local service record");
        } catch (IOException ex) {
            mailman.getLogger().log(MailManLogger.OTHER, "Local Bluetooth connection could not be initialized");
        }

    }
    


    private ServiceRecord configureServiceRecord(ServiceRecord record) {
        if(MailManUtil.useServiceDeviceClass())
            record.setDeviceServiceClasses(OBJECT_TRANSFER_SERVICE);

        DataElement bluetoothProfileDescriptorList = new DataElement(DataElement.DATSEQ);
        DataElement obexPushProfileDescriptor = new DataElement(DataElement.DATSEQ);
        obexPushProfileDescriptor.addElement(new DataElement(DataElement.UUID, OBEX_OBJECT_PUSH));
        obexPushProfileDescriptor.addElement(new DataElement(DataElement.U_INT_2, 0x100));
        bluetoothProfileDescriptorList.addElement(obexPushProfileDescriptor);
        record.setAttributeValue(0x0009, bluetoothProfileDescriptorList);

        DataElement supportedFormatList = new DataElement(DataElement.DATSEQ);
        supportedFormatList.addElement(new DataElement(DataElement.U_INT_1, 0xFF));
        record.setAttributeValue(ATTR_SUPPORTED_FORMAT_LIST, supportedFormatList);

        DataElement browseClassIDList = new DataElement(DataElement.DATSEQ);
        UUID browseClassUUID = new UUID(UUID_PUBLIC_BROWSE_GROUP);
        browseClassIDList.addElement(new DataElement(DataElement.UUID, browseClassUUID));
        record.setAttributeValue(ATTR_BROWSE_GROUP, browseClassIDList);
        return record;
    }
}

    
        

