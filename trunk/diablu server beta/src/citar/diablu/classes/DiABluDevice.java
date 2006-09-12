/*
 * DiABluDevice.java
 *
 * Created on 11 de Maio de 2006, 15:24
 *
 * Copyright (C) 2006 CITAR
 * This is part of the DiABlu Project
 * http://diablu.jorgecardoso.org
 * Created by Jorge Cardoso(jccardoso@porto.ucp.pt) & Nuno Rodrigues(nunoalexandre.rodrigues@gmail.com)
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

package citar.diablu.classes;

/**
 *
 * @author nrodrigues
 */
public class DiABluDevice {
    
    
    
    int status;             // status of the device [0-simulated device][1-bt device]DEPRECATED
    // TODO:Check out the enum types of java 1.5 [status]
    // TODO:Add a field for timestamp/detectedTimes
    int majorDeviceClass;   // see BT assigned numbers
    int minorDeviceClass;   // see BT assigned numbers
    
    DiABluID id;
    DiABluMsg lastMessage;
    DiABluKey lastKey;
    
    
    /** Creates a new instance of DiABluDevice */
    public DiABluDevice() {
        
        status = 1; 
        id = null;
        lastMessage = null;
        lastKey = null;
        majorDeviceClass = 0;
        minorDeviceClass = 0;
    }
    
 
    public DiABluDevice(DiABluID dbid, int stat) {
        
        this.id = dbid;
        this.status = stat;
    }

    public DiABluDevice(DiABluID id) {
        
        this.id = id;
    }
    
    public DiABluDevice(DiABluID dbid,int major,int minor, int status) {
        
        this.id = dbid;
        this.majorDeviceClass=major;
        this.minorDeviceClass=minor;
        this.status = status;
    }
    
    public String toString(){
        
        return this.id.toString();
    }
    // getters & setters
    
    // status
    public void setStatus(int newStatus) {
        
        this.status = newStatus;
    }
    
    public int getStatus() {
        
        return this.status;
    }
    
    // ID
    public void setID (DiABluID newID){
        
        this.id = newID;
    }
    
    public DiABluID getID(){
        
        return this.id;
    }
    
    // Last Message
    public void setLastMessage (DiABluMsg newLastMessage) {
        
        this.lastMessage = newLastMessage;
    }
            
    public DiABluMsg getLastMessage() {
        
        return this.lastMessage;
    }
    
    // Last Key
    public void setLastKey (DiABluKey newKey) {
        
        this.lastKey = lastKey;
    }
    
    public DiABluKey getLastKey() {
        
        return this.lastKey;
    }
   
    
    /**
     * This method compares this DiABlu Device with another one 
     * supplied as a parameter. First it checks the uuid's and then the 
     * friendly names.
     * Output:
     * 0 - It's a diferent device (uuid's don't match)
     * 1 - It's equal (both uuid's and fname's match)
     * 2 - Aldo the uuid remains the same, the device's friendly name has changed
     *
     */
    public int compareDB(DiABluDevice compDB){
        
        // first we compare the uuid's
        if (!this.id.getUUID().equalsIgnoreCase(compDB.getID().getUUID())) {
            return 0;
        } else if (this.id.getFName().equalsIgnoreCase(compDB.getID().getFName())) {
            return 1;
        } else return 2;
        
        
    }
    
    /**
     * This method returns the string equivalent to the int code
     * 0 - Simulated
     * 1 - BT Device
     * other - unknown
     */
    public String getStringStatus(){
        
        switch (this.status){
            
            case 0: {
                
                return "Simulated";
                
            }
            case 1: {
                
                return "BT Device";
                
            }
            default:{
                
                return "unknown";
            }
        }
    }
    
    
    /**
     * This method returns the description of the DiABluDevice's BT Device Class
     * 
     */
    public String getStringDevice() {
        
        switch (this.majorDeviceClass) {
            
            // Simulated
            case 666: {
                
                return "Simulated";
            }
            
            // 	Computer (desktop,notebook, PDA, organizers, .... )
            case 256: {
                
                switch (this.minorDeviceClass){
                    
                    case 4 : return "Desktop workstation";
                    case 8 : return "Server class computer";
                    case 12: return "Laptop";
                    case 16: return "Handheld PC/PDA";
                    case 20: return "Palm sized PC/PDA";
                    case 24: return "Wearable computer";
                    default: return "Uncategorized";
                    
                }
                
            }
            
            // Phone (cellular, cordless, payphone, modem, ...)
            case 512: {
                
                switch (this.minorDeviceClass){
                
                    case 4 : return "Cellular Phone";
                    case 8 : return "Cordless Phone";
                    case 12: return "Smart phone";
                    case 16: return "Wired modem or voice gateway";
                    case 20: return "Common ISDN Access";
                    default: return "Uncategorized";
                                        
                }
            }
            
            // LAN /Network Access point
            case 768: {
                
                return "LAN/Network Acess Point";
                
            }            
            
            // Audio/Video (headset,speaker,stereo, video display, vcr...
            case 1024: {
                
                return "Audio/Video Device";
                
            }
            
            // Peripheral (mouse, joystick, keyboards, ..... )
            case 1280:
            {
                return "Peripheral Device";
             
            }
            
            // Imaging (printing, scanner, camera, display, ...)
            case 1536: {
                
                return "Imaging Device";
            }                        
            
            default: {
                return "not classified";
            }
            
        }
        
    
        
    }
}
