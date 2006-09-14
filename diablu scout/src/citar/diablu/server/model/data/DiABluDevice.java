/*
 * DiABluDevice.java
 *
 * Created on 11 de Maio de 2006, 15:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.data;


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
     *  2 - Black listed device - means this device is in the system black list and should not be outputted except for the serverView
     * 
     */
    private int status;           
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
        id = new DiABluID();
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
        this.detectionCounter = 1;
        
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
                    
             throw new ClassCastException(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("DiABlu_Device_expected"));  
             
        } 
       
        // second:compare the uuid's
        if (this.id.getUUID().equalsIgnoreCase(((DiABluDevice)anotherDiABluDevice).getID().getUUID())) {
            
            
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
     * other - unknown
     */
    public String getStringStatus(){
        
        switch (this.status){
            
            case 0: {
                
                return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Simulated");
                
            }
            case 1: {
                
                return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("BT_Device");
                
            }
            case 2: {
                
                return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Black_Listed");
                
            }
            default:{
                
                return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("unknown");
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
                    
                    case 4 : return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Desktop_workstation");
                    case 8 : return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Server_class_computer");
                    case 12: return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Laptop");
                    case 16: return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Handheld_PCPDA");
                    case 20: return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Palm_sized_PCPDA");
                    case 24: return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Wearable_computer");
                    default: return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Uncategorized");
                    
                }
                
            }
            
            // Phone (cellular, cordless, payphone, modem, ...)
            case 512: {
                
                switch (this.minorDeviceClass){
                
                    case 4 : return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Cellular_Phone");
                    case 8 : return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Cordless_Phone");
                    case 12: return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Smart_phone");
                    case 16: return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Wired_modem_or_voice_gateway");
                    case 20: return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Common_ISDN_Access");
                    default: return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Uncategorized");
                                        
                }
            }
            
            // LAN /Network Access point
            case 768: {
                
                return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("LAN/Network_Acess_Point");
                
            }            
            
            // Audio/Video (headset,speaker,stereo, video display, vcr...
            case 1024: {
                
                return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Audio/Video_Device");
                
            }
            
            // Peripheral (mouse, joystick, keyboards, ..... 
            case 1280:
            {
                return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Peripheral_Device");
             
            }
            
            // Imaging (printing, scanner, camera, display, ...
            case 1536: {
                
                return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Imaging_Device");
                
            }                        
            
            default: {
                
                return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("not_classified");
                
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
        if (minorDeviceClassString.equalsIgnoreCase(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Phone"))){
            this.majorDeviceClass = 0;
            this.minorDeviceClass = 1;
        }   
          
    }
}
