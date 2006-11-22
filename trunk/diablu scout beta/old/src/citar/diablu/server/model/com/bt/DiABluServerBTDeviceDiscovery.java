/*
 * DiABluServerBTDeviceDiscovery.java
 *
 * Created on 9 de Maio de 2006, 11:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.com.bt;

// j2se
import java.lang.*;
import java.io.*;
import java.util.Vector;


// j2se 1.5 DiABlu System Constants
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*;

// jsr82
import javax.bluetooth.*;

// DiABlu Model Data
import citar.diablu.server.model.data.DiABluDevice;
import citar.diablu.server.model.data.DiABluID;
import citar.diablu.server.model.data.DiABluKey;
import citar.diablu.server.model.data.DiABluMsg;
        
// Controller (Model Listener)
import citar.diablu.server.controller.in.devices.bt.DiABluServerBTControllerListener;

// Model (Bluetooth sub-Model)
import citar.diablu.server.controller.out.bt.DiABluServerBTModelListener;

// i18n
import java.util.ResourceBundle;

/**
 *
 * @author nrodrigues
 */
public class DiABluServerBTDeviceDiscovery  implements DiscoveryListener {
    
    /*
     * This class serves as ligth model since it's from it we get the
     * settings data (btDelay)
     */
   // DiABluServerBTModelListener model;
    
    /*
     * This controller notifies the Parent Model of the new information
     * avaiable (newDeviceList)
     */
    DiABluServerBTControllerListener controller;
           
    private static Vector <RemoteDevice> remoteDeviceList = new Vector <RemoteDevice> (); // List of Discovered Bluetooth Remote Devices  
    private Vector <DiABluDevice> diABluDeviceList = new Vector <DiABluDevice> ();        // List of Discovered DiABlu Devices
    
    // TEST PURPOSES ONLY
    // These vars are not, for the time being, to be passed among the system
    private long time_initial;                       // TIME COUNTER - initial time
    private long time_end;                           // TIME COUNTER - ended time
    private long time_elapsed;                       // TIME COUNTER - elapsed time
       
    // bluetooth
    private LocalDevice localDev;                     // our BT local device
    private DiscoveryAgent agent;                     // Our Discovery Agent
   
   // discovery restart flag
   // we can later use this in order to provide start/stop commands to the device discovery
   private boolean RestartInquiry = true;
    
    /**
     * Creates a new instance of DiABluServerBTDeviceDiscovery
     */
    public DiABluServerBTDeviceDiscovery(DiABluServerBTControllerListener controller) throws BluetoothStateException {
        
        //this.model = model;                                                     // This class model
        this.controller = controller;                                           // This class controller
                
        //this.remoteDeviceList = new Vector <RemoteDevice> ();                        // Remote Bluetooth devices detected list
        //this.diABluDeviceList = new Vector <DiABluDevice> ();                        // Converted DiABlu devices detected list
        
        System.out.println("DiABluServerBTDeviceDiscovery started");
                
    } 
    
    public void run() {
        
        log(LOG_DETAILED,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Starting_Bluetooth_Discovery_Engine"));

        try {
                    
            // get our bluetooth local device & agent
            localDev = LocalDevice.getLocalDevice();
            agent = localDev.getDiscoveryAgent();
               
            // Start the INQUIRY    
            searchDevices();
        
        } catch (Exception e) {
            
            log(LOG_DETAILED,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Failed_to_start_bluetooth_system"));
            log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerBTDeviceDiscovery]_")+e.getLocalizedMessage());
            e.printStackTrace();
            
        }        
    }
    
    public void searchDevices() {
         
        while ( RestartInquiry ) {
            
            try { 
                                        
                agent.startInquiry(DiscoveryAgent.GIAC, this);
                
                // wait until inquiry is done
                synchronized (this) {
                    
                    try {
                        
                        this.wait();
                        
                    } catch (Exception e) {
                        
                        log(LOG_DETAILED,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[searchDevices]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Error_while_waiting_for_Inquiry_Completed_Signal"));
                        log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[searchDevices]_")+e.getLocalizedMessage());
                        e.printStackTrace();
                        
                    }
                }
                                    
            } catch (BluetoothStateException e) {
            
                        log(LOG_SIMPLE,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[searchDevices]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Please_check_your_Bluetooth_Hardware"));
                        log(LOG_COM_ERROR,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[searchDevices]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("_Unable_to_start_Bluetooth_Detection"));
                        log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[searchDevices]_")+e.getLocalizedMessage());
                        e.printStackTrace();
              
            }
        
          // Report the list to this class controller
          log(LOG_DETAILED,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[Inquiry_Completed]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Returning_")+diABluDeviceList.size()+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("_devices"));        
          controller.newDeviceList(DEVICE_BLUETOOTH, diABluDeviceList);   
          diABluDeviceList.removeAllElements();
          log(LOG_DETAILED,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[Inquiry_Completed]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Wait_terminated.Restarting_inquiry"));
 
        }
    }  
    
    /*
     *  Discovery Listener Methods
     *
     */
    
    /* This method is called by the Discovery Listener everytime it finds a RemoteDevice
     * For stabillity i've added a device check to find n-plicates. 
     * For perfomance (with recommended testing on your system) you can remove that. 
     * 
     * I've removed the DiABluDevice conversion here since some Native BT Methods might BLOCK!!!
     * WARNING:WAIT UNTIL THE DISCOVERY IS FINISHED THEN CONVERT TO DIABLU DEVICE!!!
     */        
    public void deviceDiscovered (RemoteDevice btDevice, DeviceClass cod){
        
       /** for the paranoid.
        * Heard rumors of n-plicate discoverys? I did:(
        */
       log(LOG_DEBUG,"[Device Discovery Method]");
       if (!remoteDeviceList.contains(btDevice)) {
           
           log(LOG_DETAILED,"Found device @"+getSafeBTAddress(btDevice));
           remoteDeviceList.addElement(btDevice);   
           
       } else {
           // SYSTEM ERROR:WE'VE FOUND A DUPLICATE BT DEVICE!'
           /* PARANOÎD
            * [RemoteDevice].getBluetoothAddress() - should be a safe non-blocking method...
            * ...but we shouldn't be here anyway
            * 
            */
           String deviceAddress=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[NOT_AVAIABLE!]");
           
               
           deviceAddress = getSafeBTAddress(btDevice);
      
           
           log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Discovered_duplicated_device")+deviceAddress);
           
       }
   
    }        
    
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
   
        // NOTHING TO DO HERE - Needed for the Discovery Listener Interface
        
    }
    
    public void serviceSearchCompleted(int transID, int respCode) {
     
        // NOTHING TO DO HERE - Needed for the Discovery Listener Interface
        
    }
    
    public void inquiryCompleted(int discType){
        
        int totalDevicesFound = remoteDeviceList.size();
        DiABluDevice tempDD = new DiABluDevice();      
                
        // clean the diablu device list
        //diABluDeviceList.removeAllElements();
        
        // report return code
        log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[Inquiry_Completed]_")+totalDevicesFound+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Return_Code")+discType);
                                                             
        // Discovery Agent has finished so let's process the data            
        if ( totalDevicesFound > 0 ){
        
            // let's convert our found devices'
            for (RemoteDevice rd:remoteDeviceList){
                
                tempDD = safeRemoteToDiABlu(rd);
                
                // second duplicates verify
                if (!diABluDeviceList.contains(tempDD)){
                    
                    diABluDeviceList.addElement(tempDD);
                    log(LOG_DEBUG,"[BT-inquiryCompleted()] "+"Adding device:"+tempDD.toString());               
                    
                } else {
                    
                    log(LOG_DEBUG,"[BT-inquiryCompleted()] "+"Second cleaning discarded duplicated:"+tempDD.toString());
                }
                    
            } 
                
        }     
        
        // empty the lists 
        log(LOG_DEBUG,"[BT-inquiryCompleted()] "+"Cleaning device lists...");

        // clean the remote device list
        remoteDeviceList.removeAllElements();
        


        // implement the btDelay
        try {
            
            Thread.sleep(10000);
            
        } catch (Exception e) {
            
            log(LOG_COM_ERROR,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[Inquiry_Completed]_")+e.getLocalizedMessage());
            e.printStackTrace();
            
        }
        
         // synchronize bt threads
        synchronized (this) {
            
            try {
                
                log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[Inquiry_Completed]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Sincronizing"));
                this.notifyAll();
                  
            } catch (Exception e) {
                
                log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[Inquiry_Completed]_")+e.getLocalizedMessage());
                e.printStackTrace();
                                
            }            
            
        }   
        
    }

    /**
     * SAFER METHODS
     * For some knowned and a few unknowned reasons sometimes the 
     * JSR82 Methods fail. 
     * Since some of them, i use more than once,
     * i've factored out the code to handle exceptions and provide
     * a gracefully message. They always return an acceptable result 
     * to the caller. In case of an error, it simply returns a "desolé"
     * string without blocking the system. The method is also logging 
     * the entire action.
     */
    
    private DiABluDevice safeRemoteToDiABlu(RemoteDevice rD){
        String temp;
        DiABluDevice tempDD = new DiABluDevice();
        
        tempDD.getID().setUUID(getSafeBTAddress(rD));
        tempDD.getID().setFName(getSafeBTFriendlyName(rD));
        tempDD.setMajorDeviceClass(getSafeBTMajorDeviceClass(rD));
        tempDD.setMinorDeviceClass(getSafeBTMinorDeviceClass(rD));
        tempDD.setStatus(DEVICE_BLUETOOTH);
        log(LOG_DEBUG,"Safe method returned:"+getSafeBTFriendlyName(rD)+"@"+getSafeBTAddress(rD)+"-"+getSafeBTMajorDeviceClass(rD)+"|"+getSafeBTMinorDeviceClass(rD));
        return tempDD;
        
    }
    
    private int getSafeBTMinorDeviceClass(RemoteDevice rD){
        
        int safeBTMinorDeviceClass = -1;
        
        if ( rD == null ) { 
            
            log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeBTMinorDeviceClass]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Invalid,Empty_or_Null_argument"));
            
            
        } else {
        
            try {
            
              safeBTMinorDeviceClass = rD.getDeviceClass().getMinorDeviceClass();
            
            } catch ( Exception e ) {
            
                log(LOG_DETAILED,"Error trying to get minor device class");
                log( LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeBTMinorDeviceClass]_")+ e.getLocalizedMessage() );
                e.printStackTrace();
            
            }
        }
        
        return safeBTMinorDeviceClass;
        
    }
    
    private int getSafeBTMajorDeviceClass(RemoteDevice rD){
        
        int safeBTMajorDeviceClass = -1;
        
        if ( rD == null ) { 
            
            log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeBTMajorDeviceClass]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Invalid,Empty_or_Null_argument"));
            
            
        } else {
        
            try {
            
              safeBTMajorDeviceClass = rD.getDeviceClass().getMajorDeviceClass();
            
            } catch ( Exception e ) {
            
                log( LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeBTMajorDeviceClass]_")+ e.getLocalizedMessage() );
                e.printStackTrace();
            
            }
        }
        
        return safeBTMajorDeviceClass;
        
    }
    
    private String getSafeBTAddress(RemoteDevice rD){           
        
        String safeBTAddress = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Invalid,Empty_or_Null_argument");
        
        // ...safer dies older, paranoid is still alive:)
        if ( rD == null ) {
            
            log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeBTAddress]_")+ safeBTAddress);
            return safeBTAddress;
            
        }
        
        safeBTAddress = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Unable_to_get_bluetooth_address");        
        
        try {
            
            safeBTAddress = rD.getBluetoothAddress();
            // log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeBTAddress]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Found")+safeBTAddress);
            
        } catch (Exception e){
            
            // TODO: update the log sub-system
            log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeBTAddress]_")+ e.getLocalizedMessage());
            log(LOG_COM_ERROR,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeBTAddress]_")+safeBTAddress);
            e.printStackTrace(); // avaiable info if you start diablu from command line
            
        }
           
        return safeBTAddress;
        
    }
    
    private String getSafeBTFriendlyName (RemoteDevice rD){
        
        
        String safeBTFriendlyName = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Invalid_Empty_or_Null_argument");
        
        // ...safer get's older, paranoid is still alive:)
        if ( rD == null ) {
            
            log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeFriendlyName]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Returning")+safeBTFriendlyName);
            return safeBTFriendlyName;
            
        }
        
        safeBTFriendlyName = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Unable_to_get_Bluetooth_address.");
        
        try {
            
            safeBTFriendlyName = rD.getFriendlyName(true);
            //log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeFriendlyName]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Found")+safeBTFriendlyName);
            
        } catch (Exception e){
            
            log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeFriendlyName]_")+e.getLocalizedMessage());                 
            log(LOG_COM_ERROR,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeFriendlyName]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Making_second_attempt"));
            e.printStackTrace(); // avaiable info if you start diablu from command line
            
            try {
                
              safeBTFriendlyName = rD.getFriendlyName(false);  
              log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeFriendlyName]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Found")+safeBTFriendlyName);
              
            } catch (Exception e2){
                
               log(LOG_DEBUG,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeFriendlyName]_")+e2.getLocalizedMessage());                 
               log(LOG_COM_ERROR,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[getSafeFriendlyName]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Second_attempt_failed")+safeBTFriendlyName);
               e2.printStackTrace(); // avaiable info if you start diablu from command line
                
            }
        }
        
        return safeBTFriendlyName;    
        
    }
    
    /*
     * This method serves only to simplify source code
     * now instead of "controller.log" we can use just "log"
     */
    private void log(int priority,String logM){
        
        controller.log(priority,logM);
        
    }
    
}
