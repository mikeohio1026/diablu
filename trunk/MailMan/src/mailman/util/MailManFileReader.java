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
package mailman.util;

import mailman.*;
import mailman.util.datastructures.MailManDevice;
import mailman.util.datastructures.MailManRemoteDevice;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;


public class MailManFileReader {
    
    private static String OUI_FILE = "oui.txt";
    private static String MIMETYPES_FILE = "mimetypes.txt";
    private static String DEVICES_FILE = "devices.txt";
    
    MailMan mailman;

    public MailManFileReader(MailMan mailman)
    {
        this.mailman = mailman;
        
    }
    
    public void read()
    {
        readOUI();
        readMimetypes();
        readDevices();
    }
    
   
    
    public void readOUI()
    {
        FileReader filereader = null;
        try {
            filereader = new FileReader(OUI_FILE);
            BufferedReader buffRead = new BufferedReader(filereader);
            String line;
            StringTokenizer st;
            while ((line = buffRead.readLine())!= null) {
                st = new StringTokenizer(line, "()");
                if(st.countTokens() >= 3)
                {
                    String oui = st.nextToken().trim();
                    String token2 = st.nextToken().trim();
                    
                    String manufacturer = st.nextToken("").substring(1);
                    if(token2.compareToIgnoreCase("base 16") == 0)
                    {
                        mailman.getManufacturerOUI().put(oui.toLowerCase(), manufacturer.toLowerCase());
                    }
                }

                mailman.setOUI(true);
            }
            buffRead.close();
        } catch (IOException ex) {
            mailman.getLogger().log(MailManLogger.OTHER, "File not Found: \"oui.txt\". Some features might not be available. Read \"README.txt\" for more info");
            
        } finally {
            if(mailman.hasOUI())
            {   try {
                    filereader.close();
                } catch (IOException ex) {
                    mailman.getLogger().log(MailManLogger.OTHER, "Error closing file: \"oui.txt\"");
                    System.exit(-1);
                }
            }
        }
    }
    public void readMimetypes() {
        FileReader filereader = null;
        try {
            filereader = new FileReader(MIMETYPES_FILE);
            BufferedReader buffRead = new BufferedReader(filereader);
            String line;
            StringTokenizer st;
            while ((line = buffRead.readLine()) != null) {
                st = new StringTokenizer(line);
                if (st.countTokens() == 2) {
                    String extenssion = st.nextToken().trim();
                    String mimetype = st.nextToken().trim();
                    mailman.getMimetypes().put(extenssion, mimetype);
                }
            }
            buffRead.close();
            mailman.setMimetypes(true);
        } catch (IOException ex) {
            mailman.getLogger().log(MailManLogger.OTHER, "File not Found: \"mimetypes.txt\". Some features might not be available. Read \"README.txt\" for more info");
            
        } finally {
            if (mailman.hasMimetypes()) {
                try {
                    filereader.close();
                } catch (IOException ex) {
                    mailman.getLogger().log(MailManLogger.OTHER, "Error closing file: \"mimetypes.txt\"");
                }
            }
        }

    }

    public void addDevice(String deviceUUID) {
        try {
            if (!mailman.getKnownDevices().getDevices().containsKey(deviceUUID)) {
                MailManDevice device = new MailManDevice((RemoteDevice) new MailManRemoteDevice(deviceUUID));
                mailman.getKnownDevices().getDevices().put(deviceUUID, device);
            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
    }

    public void readDevices() {

        FileReader filereader = null;
        File f = new File(DEVICES_FILE);
        if (f.exists()) {
            try {
                filereader = new FileReader(DEVICES_FILE);
                BufferedReader buffRead = new BufferedReader(filereader);
                String line;
                StringTokenizer st;
                while ((line = buffRead.readLine()) != null) {
                    st = new StringTokenizer(line);
                    String uuid = st.nextToken().trim();
                    addDevice(uuid);
                    line = buffRead.readLine();
                    st = new StringTokenizer(line);
                    while (st.hasMoreElements()) {
                        mailman.getKnownDevices().get(uuid).getRecievedFiles().add(st.nextToken().trim());


                    }

                    line = buffRead.readLine();
                    st = new StringTokenizer(line);
                    while (st.hasMoreElements()) {
                        mailman.getKnownDevices().get(uuid).getSentFiles().add(st.nextToken().trim());
                    }




                }
                buffRead.close();
                mailman.updateDeviceFiles();

            } catch (IOException ex) {

            } finally {
                try {
                    filereader.close();
                } catch (IOException ex) {
                    Logger.getLogger(MailMan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else
        {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                mailman.getLogger().log(MailManLogger.OTHER, "Error creating file: \"devices.txt\"");
                System.exit(-1);
            }
        }
    }
}
