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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 *
 * @author nrodrigues
 */
public class DiABluDevice {
    
    private static Hashtable <String,String>manufacturer;
    
    /* We parse the manufacturers file the first time this class is loaded.*/
    static {
        manufacturer = new Hashtable<String,String>();
        parseOUI();
    }
    
    int status;             // status of the device [0-simulated device][1-bt device]DEPRECATED
    // TODO:Check out the enum types of java 1.5 [status]
    // TODO:Add a field for timestamp/detectedTimes
    
    private int majorDeviceClass;   // see BT assigned numbers
    private int minorDeviceClass;   // see BT assigned numbers
    
    DiABluID id;

    
    
    /** Creates a new instance of DiABluDevice */
    public DiABluDevice() {
        
        status = 1;
        id = null;

        //lastKey = null;
        setMajorDeviceClass(0);
        setMinorDeviceClass(0);
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
        this.setMajorDeviceClass(major);
        this.setMinorDeviceClass(minor);
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
    public void setID(DiABluID newID){
        
        this.id = newID;
    }
    
    public DiABluID getID(){
        
        return this.id;
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

    public DiABluID getId() {
        return id;
    }
    
    public boolean equals(Object d) {
        return ((DiABluDevice)d).getId().UUID.equalsIgnoreCase(this.getId().UUID);
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
     * Returns the String representation of the Major Device Class.
     * This list id based on the Bluetooth for Java Book by Bruce Hopkins.
     *
     * @return The String representation of the major device class.
     */
    public String getMajorDeviceClassString()  {
        switch (this.getMajorDeviceClass()) {
            // Simulated
            case 666:
                return "Simulated";
                
            case 0:
                return "Misc. major device";
                
                // 	Computer (desktop,notebook, PDA, organizers, .... )
            case 256:
                return "Computer";
                
                // Phone (cellular, cordless, payphone, modem, ...)
            case 512:
                return "Phone";
                
                // LAN /Network Access point
            case 768:
                return "LAN/network acess point";
                
                // Audio/Video (headset,speaker,stereo, video display, vcr...
            case 1024:
                return "Audio/video device";
                
                // Peripheral (mouse, joystick, keyboards, ..... )
            case 1280:
                return "Computer peripheral";
                
                // Imaging (printing, scanner, camera, display, ...)
            case 1536:
                return "Imaging Device";
                
            case 7936:
                return "Unclassified major device";
                
            default:
                return "Not classified";
        }
    }
    public String getMinorDeviceClassString()  {
        switch (this.getMajorDeviceClass()) {
            case 0:
                return "";
                
                // 	Computer (desktop,notebook, PDA, organizers, .... )
            case 256:
                switch (this.getMinorDeviceClass()) {
                    case 0:
                        return "Unassigned, misc";
                    case 4:
                        return "Desktop";
                    case 8:
                        return "Server";
                    case 12:
                        return "Laptop";
                    case 16:
                        return "Sub-laptop";
                    case 20:
                        return "PDA";
                    case 24:
                        return "Watch size";
                    default:
                        return "Not classified";
                }
                
                // Phone (cellular, cordless, payphone, modem, ...)
            case 512:
                switch (this.getMinorDeviceClass()) {
                    case 0:
                        return "Unassigned, misc";
                    case 4:
                        return "Cellular";
                    case 8:
                        return "Household cordless";
                    case 12:
                        return "Smartphone";
                    default:
                        return "Not classified";
                }
                
                // LAN /Network Access point
            case 768:
                switch (this.getMinorDeviceClass()) {
                    case 0:
                        return "Fully available";
                    case 32:
                        return "1-17% utilized";
                    case 64:
                        return "17-33% utilized";
                    case 96:
                        return "33-50% utilized";
                    case 128:
                        return "50-76% utilized";
                    case 160:
                        return "76-83% utilized";
                    case 192:
                        return "83-99% utilized";
                    case 224:
                        return "100% utilized, no service available";
                    default:
                        return "Not classified";
                }
                
                // Audio/Video (headset,speaker,stereo, video display, vcr...
            case 1024:
                switch (this.getMinorDeviceClass()) {
                    case 0:
                        return "Unassigned, misc";
                    case 4:
                        return "Headset";
                    case 8:
                        return "Hands-free device";
                    case 16:
                        return "Microphone";
                    case 44:
                        return "VCR";
                    case 72:
                        return "Video game system";
                    default:
                        return "Not classified";
                }
                
                // Peripheral (mouse, joystick, keyboards, ..... )
            case 1280:
                switch (this.getMinorDeviceClass()) {
                    case 64:
                        return "Keyboard";
                    case 128:
                        return "Mouse, trackball, etc";
                    case 12:
                        return "Remote control";
                        
                    default:
                        return "Not classified";
                }
                
                // Imaging (printing, scanner, camera, display, ...)
            case 1536:
                switch (this.getMinorDeviceClass()) {
                    case 16:
                        return "Display device";
                    case 32:
                        return "Camera";
                    case 64:
                        return "Scanner";
                    case 128:
                        return "Printer";
                        
                    default:
                        return "Not classified";
                }
                
            case 7936:
                return "";
                
            default:
                return "Not classified";
        }
    }
    /**
     * Returns the String representation of the Minor Device Class.
     * This list id based on the Bluetooth for Java Book by Bruce Hopkins.
     *
     * @return The String representation of the minor device class.
     */
    
    /**
     * This method returns the description of the DiABluDevice's BT Device Class
     *
     * @deprecated
     */
    public String getStringDevice() {
        
        switch (this.getMajorDeviceClass()) {
            
            // Simulated
            case 666: {
                
                return "Simulated";
            }
            
            // 	Computer (desktop,notebook, PDA, organizers, .... )
            case 256: {
                
                switch (this.getMinorDeviceClass()){
                    
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
                
                switch (this.getMinorDeviceClass()){
                    
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
    
    /**
     * Returns the manufacturer of the BT hardware.
     *
     * @return The name of the manufacturer of the BT hardware or an empty String if it could not be determined.
     */
    public String getManufacturer() {
        String uuidManufacturerPart = this.id.UUID.substring(0, 6).toLowerCase();
       // System.out.println(uuidManufacturerPart + " " + manufacturer.get(uuidManufacturerPart));
        String m = manufacturer.get(uuidManufacturerPart);
        return m != null ? m : "";        
    }
    
    /**
     * Parses the oui.txt file for BT manufacturers.
     *
     * The result is put into an hashtable.
     */
    private static void parseOUI() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("oui.txt")); 
            String s;
            StringTokenizer st; 
            //System.out.println("reading oui file");
            while ( (s = br.readLine()) != null) {
                st =  new StringTokenizer(s, "()");
                if (st.countTokens() >= 3) { //we are in a interesting line
                    String oui = st.nextToken().trim().toLowerCase();
                    st.nextToken();
                    String manu = st.nextToken().trim();
                    manufacturer.put(oui, manu);
                    //System.out.println(oui + " " + manu);
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }  catch (IOException ioe) {
            
        }
    }
    
    public int getMajorDeviceClass() {
        return majorDeviceClass;
    }
    
    public void setMajorDeviceClass(int majorDeviceClass) {
        this.majorDeviceClass = majorDeviceClass;
    }
    
    public int getMinorDeviceClass() {
        return minorDeviceClass;
    }
    
    public void setMinorDeviceClass(int minorDeviceClass) {
        this.minorDeviceClass = minorDeviceClass;
    }
}
