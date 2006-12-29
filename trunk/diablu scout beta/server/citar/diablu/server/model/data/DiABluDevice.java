/*
 * DiABluDevice.java
 *
 * Created on 11 de Maio de 2006, 15:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.data;

import javax.bluetooth.RemoteDevice;
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*;

/**
 *
 * @author nrodrigues
 */
public class DiABluDevice implements Comparable {
    
    
    /**
     * status(type) of the device 
     *
     *  0 - Simulated device - means it's a virtual device created by the DiABlu Simulator
     *  1 - Bluetooth device - means it's a real device detected by the Bluetooth system
     *  2 - BT Black listed device - means this device is in the system black list and should not be outputted except for the serverView
     *  3 - BT Ignored device - represents a real BT device but that is ignored by the system until a certain point(VCin)
     *  4 - BT Recovered device - represents a real BT device but that was removed and recovered until VCout
     *
     */
    private int status;      
    /*  device status
     *
     *  0 - Simulated
     *  1 - Bluetooth
     *
     */
    private int deviceStatus;
    // TODO:Check out the enum types of java 1.5 [status]
    // TODO:Add a field for timestamp/detectedTimes
    
    private int majorDeviceClass;   // see BT assigned numbers
    private int minorDeviceClass;   // see BT assigned numbers    
    private int detectionCounter;  // number of times this diablu device has been detected
   
    private DiABluID id;
    private DiABluMsg lastMessage;
    private DiABluKey lastKey;
    
    
    /** Creates a new instance of DiABluDevice */
    public DiABluDevice() {
        
        status = 1; 
        setDeviceStatus(1);
        id = new DiABluID();
        lastMessage = null;
        lastKey = null;
        majorDeviceClass = 0;
        minorDeviceClass = 0;
        detectionCounter = 0;
        
    }
    
    public DiABluDevice(RemoteDevice rd){
        
       String uT,nT = "";
       
       uT = rd.getBluetoothAddress();       
       
       try {
           
           nT = rd.getFriendlyName(true);
           
       } catch (Exception e){
           
           System.out.println("!!Error getting device friendly name:"+e.getLocalizedMessage()+"Using none instead");
           
       }
        
       this.id = new DiABluID(uT,nT);
       this.majorDeviceClass = rd.getDeviceClass().getMajorDeviceClass();
       this.minorDeviceClass = rd.getDeviceClass().getMinorDeviceClass();
       this.detectionCounter = 0;
       this.lastKey = null;
       this.lastMessage = null;
        
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
        this.detectionCounter = 0;
        
    }
    
    public int getDetectionCounter(){
        
        return this.detectionCounter;
        
    }
    
    public void setDetectionCounter(int dc){
        
        this.detectionCounter = dc;
        
    }
    
    public void incrementDetectionCounter(){
        
        this.detectionCounter++;
        
    }
    
    public void decrementDetectionCounter(){
        
        this.detectionCounter--;
        
    }
    
    public void setMajorDeviceClass(int madc){
        
        this.majorDeviceClass = madc;
        
    }
    
    public int getMajorDeviceClass() {
        
        return this.majorDeviceClass;
        
    }
    
    public void setMinorDeviceClass(int midc){
        
        // TODO: validate & change if necessary the majordeviceclass
        this.minorDeviceClass = midc;
        
    }
    
    public int getMinorDeviceClass(){
        
        return this.minorDeviceClass;
        
    }
    
    public String toString(){
        
        return this.id.toString();
        
    }
    // getters & setters
    
    // status
    public void setStatus(int newStatus) {
        
        this.status = newStatus;
        if (newStatus==DEVICE_STATUS_SIMULATED){
            this.deviceStatus=DEVICE_STATUS_SIMULATED;
        }else {
            this.deviceStatus=DEVICE_STATUS_BT;
        }
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
    
    /**
     * This method returns the Last Key sended by this device
     */
    public DiABluKey getLastKey() {
        
        return this.lastKey;
        
    }
   
    
    /**
     * This method implements the Comparable interface and compares 
     * this DiABlu Device with another one supplied as a parameter. 
     * It uses the UUID as comparable parameter
     *
     * NOTE: That this does not respect the Comparable interface except 
     * in the equal value. 1 should mean greater and -1 smaller.
     *
     * Output:
     * -1 - It's a diferent device (uuid's don't match)
     * 0  - It's equal (uuid's match)

     *
     */
    public int compareTo(Object anotherDiABluDevice) throws ClassCastException {                  
    
        // first:make sure it's a DiABlu Device to compare with'
        if (!(anotherDiABluDevice instanceof DiABluDevice)) {
                    
             throw new ClassCastException("DiABlu Device expected");  
             
        } 
       
        // second:compare the uuid's
        if (this.id.getUUID().equalsIgnoreCase(((DiABluDevice)anotherDiABluDevice).getID().getUUID())) {
            
            /*
            if (this.id.getFName().equalsIgnoreCase(((DiABluDevice)anotherDiABluDevice).getID().getFName())){
                
                // 100% match
                return 1;
            }
             */
            //if (this.status==((DiABluDevice)anotherDiABluDevice).getStatus()) {
                return 0;
            //}
            
            
        } 
        
        return -1;
        
    }
    /*
     * This method does a more complex comparation
     * returns:
     * 0 - perfect match
     * 1 - diferent fname
     * 2 - no match
     *
     */
    public int complexCompareTo(DiABluDevice anotherDiABluDevice) {
        
        if (anotherDiABluDevice == null) 
        {
            System.out.println("Trying to compare null device");
            return -1; // null pointer error
            
        } else {
            
            // make sure we compare the right type of devices
           // if (this.status!=anotherDiABluDevice.getStatus()) return 2; // totally diferent
            
            if (this.id.getUUID().equalsIgnoreCase(anotherDiABluDevice.getID().getUUID())) {
                
                // uuid match
                if (this.id.getFName().equalsIgnoreCase(anotherDiABluDevice.getID().getFName())){
                    
                    // fname match
                    return 0; // 100% match
                    
                } else {
                    
                    return 1; // Diferent friendly name
                    
                }
                
            } else {
                
                // uuid don't match
                return 2; // totally diferent
                
            }
        }
  
    }
    
    /**
     * This method returns the string equivalent to the int code
     * 0 - Simulated
     * 1 - BT Device
     * 2 - Black Listed
     * 3 - Ignored
     *
     * other - unknown
     *
     */
    public String getStringStatus(){
        
        switch (this.status){
            
            case 0: {
                
                return "Simulated";
                
            }
            case 1: {
                
                return "BT Device";
                
            }
            case 2: {
                
                return "Black Listed";
                
            }
            case 3: {
                return "Ignored "+this.detectionCounter+" times";
            }
            case 4: {
                return "Recovered "+this.detectionCounter+" times";
                
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
            
            // 	Computer (desktop,notebook, PDA, organizers, .... )
            case 256: {
                
                switch (this.minorDeviceClass){
                    
                    case 4 : return "Desktop workstation";
                    case 8 : return "Server class computer";
                    case 12: return "Laptop";
                    case 16: return "Handheld PCPDA";
                    case 20: return "Palm sized PCPDA";
                    case 24: return "Wearable_computer";
                    default: return "Uncategorized";
                    
                }
                
            }
            
            // Phone (cellular, cordless, payphone, modem, ...)
            case 512: {
                
                switch (this.minorDeviceClass){
                
                    case 4 : return "Cellular Phone";
                    case 8 : return "Cordless Phone";
                    case 12: return "Smart Phone";
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
            
            // Peripheral (mouse, joystick, keyboards, ..... 
            case 1280:
            {
                return "Peripheral Device";
             
            }
            
            // Imaging (printing, scanner, camera, display, ...
            case 1536: {
                
                return "Imaging Device";
                
            }                        
            
            default: {
                
                return "not classified";
                
            }
            
        }    
        
    }
    
    /**
     * This method accepts a String Representation of the device's minor device class
     * and updates both major & minor device class accordanly
     *
     */
    public void setStringDevice(String minorDeviceClassString){
        
        // TODO:complete the method
        if (minorDeviceClassString.equalsIgnoreCase("Phone")){
            this.majorDeviceClass = 0;
            this.minorDeviceClass = 1;
        }   
          
    }
    
    // Convenience methods
    /**
     *  This method returns the device's 
     *  friendly name
     *
     */
    public String getFName(){
        
        return getID().getFName();
    }
    /**
     * This method set's the device's 
     * friendly name
     *
     */
    public void setFName(String n){
        
        getID().setFName(n);
        
    }
    /**
     * This method set's the device's
     * UUID
     */
    public void setUUID(String u){
        
        getID().setUUID(u);
    }
    
    /**
     * This convinience method returns
     * the uuid
     */
    public String getUUID(){
    
        return getID().getUUID();
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
    
}
