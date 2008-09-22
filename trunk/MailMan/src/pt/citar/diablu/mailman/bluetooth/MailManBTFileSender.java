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

package pt.citar.diablu.mailman.bluetooth;

import pt.citar.diablu.mailman.MailMan;
import pt.citar.diablu.mailman.util.datastructures.MailManDevice;
import pt.citar.diablu.mailman.util.MailManLogger;
import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.obex.*;
import java.io.*;
import java.util.StringTokenizer;

public class MailManBTFileSender {
    
    private MailMan mailman;

    public MailManBTFileSender(MailMan mailman)
    {
        this.mailman = mailman;
    }
    
    public int send(String deviceUUID, String filepath)
    {
        synchronized(mailman.getSendLock())
        {
        Connection connection;
          
        try {
            File file = new File(filepath);
            mailman.getKnownDevices().add(deviceUUID);
            
            
            MailManDevice device = mailman.getKnownDevices().get(deviceUUID);
            
            if(!device.isHasOBEX())
            {
                mailman.getLogger().log(MailManLogger.BT_FILE_TRANSFER, "The service \"OBEX Push\" is not available on the device " + device.getUuid());
                return -1;
            }
            
            connection = Connector.open(device.getBtgoepAdress());
            
            ClientSession cs = (ClientSession) connection;
            HeaderSet hs = cs.createHeaderSet();
            cs.connect(hs);
            
            byte[] fileBytes = readFileBytes(file);

            hs = setHeaderSet(hs, file, fileBytes);
            
            
            
            sendFile(cs, hs, fileBytes);
            connection.close();
            
            

            mailman.getKnownDevices().addSentFiles(deviceUUID, file.getName());
            mailman.getLogger().log(MailManLogger.BT_FILE_TRANSFER, "Sent file " +  file.getName() + " to " + deviceUUID);
            mailman.updateDeviceFiles();
            return 0;
            
        } catch (IOException ex) {
            mailman.getLogger().log(MailManLogger.BT_FILE_TRANSFER, "Failed trying to send " +  filepath + " to " + deviceUUID);
            return -1;
        }
        }
        
    }
    
    public int sendWithMime(String deviceUUID, String filepath, String mimetype)
    {
        synchronized(mailman.getSendLock())
        {
        try {
            File file = new File(filepath);
            mailman.getKnownDevices().add(deviceUUID);
            
            MailManDevice device = mailman.getKnownDevices().get(deviceUUID);
           
            if(!device.isHasOBEX())
                return -1;
            
            Connection connection = Connector.open(device.getBtgoepAdress());
            ClientSession cs = (ClientSession) connection;
            HeaderSet hs = cs.createHeaderSet();
            cs.connect(hs);
           
            byte[] fileBytes = readFileBytes(file);

            hs = setHeaderSet(hs, file, mimetype, fileBytes);
            
            sendFile(cs, hs, fileBytes);
            connection.close();

            mailman.getKnownDevices().getDevices().get(deviceUUID).getSentFiles().add(file.getName());
            mailman.getLogger().log(MailManLogger.BT_FILE_TRANSFER, "Sent file " +  file.getName() + " to " + deviceUUID);
            mailman.updateDeviceFiles();
            return 0;
            
        } catch (IOException ex) {
            mailman.getLogger().log(MailManLogger.BT_FILE_TRANSFER, "Failed trying to send " +  filepath + " to " + deviceUUID);
            return -1;
        }
        }
    }
        
    private byte[] readFileBytes(File file) throws IOException
    {
        InputStream is = new FileInputStream(file);
        byte[] fileBytes = new byte[is.available()];
        is.read(fileBytes);
        is.close();
        return fileBytes;
    }

    private String fileMimetype(File file) {
        String extension = file.getAbsolutePath();
        StringTokenizer st = new StringTokenizer(extension, ".", true);

        while (st.hasMoreElements()) {
            extension = st.nextToken();
        }
        extension = "." + extension;
        String mimetype = mailman.getMimetypes().get(extension);
        return mimetype;
    }

    private void sendFile(ClientSession cs, HeaderSet hs, byte[] fileBytes) throws IOException {
        Operation putOperation = cs.put(hs);
        OutputStream outputStream = putOperation.openOutputStream();
        outputStream.write(fileBytes);

        outputStream.close();
        putOperation.close();
        cs.disconnect(null);

    }
    
    private HeaderSet setHeaderSet(HeaderSet hs, File file, byte[] fileBytes)
    {
        hs.setHeader(HeaderSet.NAME, file.getName());
        if(mailman.hasMimetypes())
            hs.setHeader(HeaderSet.TYPE, fileMimetype(file));
        hs.setHeader(HeaderSet.LENGTH, new Long(fileBytes.length));
        return hs;
    }
    
    private HeaderSet setHeaderSet(HeaderSet hs, File file, String mimetype, byte[] fileBytes)
    {
        hs.setHeader(HeaderSet.NAME, file.getName());
        hs.setHeader(HeaderSet.TYPE, mimetype);
        hs.setHeader(HeaderSet.LENGTH, new Long(fileBytes.length));
        return hs;
        
    }
}
    
