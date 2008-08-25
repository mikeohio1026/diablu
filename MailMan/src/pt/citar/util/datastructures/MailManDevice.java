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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.RemoteDevice;


public class MailManDevice {
    
    public final String DEVICE_FILE = "devices.txt";

    private RemoteDevice remoteDevice;
    private DeviceClass deviceClass;
    private boolean active;
    private boolean hasOBEX;
    private boolean hasDeviceClass;
    private String btgoepAdress;
    
    
    private Vector<String> receivedFiles = new Vector<String>(); 
    private Vector<String> sentFiles = new Vector<String>();

    
    
    public MailManDevice() {
    }

    public MailManDevice(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        this.remoteDevice = remoteDevice;
        this.deviceClass = deviceClass;
        this.hasDeviceClass = true;
        this.active = true;
        this.hasOBEX = false;
        
        
        
        
        
    }
    
    public MailManDevice(RemoteDevice remoteDevice) {
        this.remoteDevice = remoteDevice;
        this.active = true;
        this.hasOBEX = false;
        this.hasDeviceClass = false;
        
    }
    
    
    
    public boolean isHasOBEX() {
        return hasOBEX;
    }
    
    public RemoteDevice getRemoteDevice() {
        return remoteDevice;
    }
    
    
    public void setHasOBEX(boolean hasOBEX) {
        this.hasOBEX = hasOBEX;
    }
    
    public int getMajorClass() {
        return deviceClass.getMajorDeviceClass();
    }

    public int getMinorClass() {
        return deviceClass.getMinorDeviceClass();
    }

    public String getUuid() {
        return remoteDevice.getBluetoothAddress();
    }

    
    public String getFriendlyName() {
        try {
            return remoteDevice.getFriendlyName(false);
        } catch (IOException ex) {
            return "Unknown";
        }
    }
    
    public void setActive(boolean active)
    {
        this.active = active;
    }
    
    public boolean getActive()
    {
        return this.active;
    }
    
    public String getBtgoepAdress() {
        return this.btgoepAdress;
        
    }

    public void setBtgoepAdress(String btgoepAdress) {
        this.btgoepAdress = btgoepAdress;
    }

    public Vector<String> getReceivedFiles() {
        return receivedFiles;
    }
    
    public Vector<String> getSentFiles() {
        return sentFiles;
    }
    
    public void toFile(String file)
    {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, true);
            fw.write(getUuid() + "\n");
            for(String sent: sentFiles)
            {
                fw.write(sent + " ");
                
            }
            fw.write("\n");
            for(String received: receivedFiles)
            {
                fw.write(received + " ");
                
            }
            fw.write("\n");
            
        } catch (IOException ex) {
            Logger.getLogger(MailManDevice.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(MailManDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public void addReceivedDevice(String filename) {
        receivedFiles.add(filename);
    }
    
    public void addSentDevice(String filename) {
        sentFiles.add(filename);
    }
    
            
    
   
}
