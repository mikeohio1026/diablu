/*
 * DiABluMapperBTDeviceDiscovery.java
 *
 * Created on 9 de Maio de 2006, 11:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.mapper.model.com.bt;

// j2se
import java.lang.*;
import java.io.*;
import java.util.Vector;
import java.util.logging.Logger;

// jsr82
import javax.bluetooth.*;

// DiABlu Model Data
import citar.diablu.mapper.model.data.DiABluAnchorPoint;
       
// Controller (Model Listener)
import citar.diablu.mapper.controller.MapperBTController;

// Model (Bluetooth sub-Model)
import citar.diablu.mapper.controller.MapperModelBTController;


/**
 *
 * @author nrodrigues
 */
public class DiABluMapperBTDeviceDiscovery  implements DiscoveryListener,MapperBTController {
    
    /*
     * This class serves as ligth model since it's from it we get the
     * settings data (btDelay)
     */
   // DiABluServerBTModelListener model;
    
    /*
     * This controller notifies the Parent Model of the new information
     * avaiable (newDeviceList)
     */
    MapperModelBTController controller;
    
    private static Logger logger = Logger.getLogger("mapper.log");
           
    private static Vector <RemoteDevice> remoteDeviceList = new Vector <RemoteDevice> (); // List of Discovered Bluetooth Remote Devices  
    private Vector <DiABluAnchorPoint> anchorPointList = new Vector <DiABluAnchorPoint> ();        // List of Discovered DiABlu Devices
    
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
   private boolean automaticDiscovery = false;
   private int btDelay = 0;
   
    
    /**
     * Creates a new instance of DiABluMapperBTDeviceDiscovery
     */
    public DiABluMapperBTDeviceDiscovery(MapperModelBTController controller,boolean automaticDiscovery,int btDelay) throws BluetoothStateException {

        logger.entering("MapperBTDiscovery","Constructor");
        
        this.controller = controller;                                           // This class controller
        this.automaticDiscovery = automaticDiscovery;
        this.btDelay = btDelay;
        this.remoteDeviceList = new Vector <RemoteDevice> ();                        // Remote Bluetooth devices detected list
        this.anchorPointList = new Vector <DiABluAnchorPoint> ();                    // Converted DiABlu Anchor Point detected list
        
        logger.finest("DiABluServerBTDeviceDiscovery started");
                 run();
    
    } 
    
    // start the discovery engine
    public void manualSearch(){
        
        logger.finest("Performing manual search...");
       // this.automaticDiscovery = false;
        run();
        
    }
    
    // define the engine state with the appropriate delay
    public void automatic(int btDelay){
     
        this.automaticDiscovery = true;
        this.btDelay = btDelay;
        //run();
    
    }
    
    // stop the discovery engine
    public void stopSearch(){
    
     logger.finest("Stopping discovery...");
     this.automaticDiscovery=false;
     logger.finest("Automatic Search?:"+this.automaticDiscovery);
     
    }
    
     
    
    public void run() {
        
       while (this.automaticDiscovery){
          
        try {
                    
            // get our bluetooth local device & agent
            logger.finer("Getting the local device");
            localDev = LocalDevice.getLocalDevice();
            logger.finer("Getting the discovery agent");
            agent = localDev.getDiscoveryAgent();
               
            // Start the INQUIRY    
            try { 
                
                logger.finer("Starting inquiry");
                agent.startInquiry(DiscoveryAgent.GIAC, this);
                
                // wait until inquiry is done
                synchronized (this) {
                    
                    try {
                        
                        logger.finest("Waiting for inquiry");
                        this.wait();
                        
                    } catch (Exception e) {
                        
                        logger.throwing(this.toString(),"Waiting for Inquiry",e);
                        
                    }
                }
                                    
            } catch (BluetoothStateException e) {
            
                      logger.throwing(this.toString(),"Starting inquiry",e);
                        
              
            }
                  
          
          logger.finest("Inquiry Completed - Returning "+anchorPointList.size()+" anchor points"); 
            
          controller.newAnchorPointList(anchorPointList);
          anchorPointList.removeAllElements();
        
        } catch (Exception e) {
            
            logger.throwing(this.toString(),"Starting Bluetooth System",e);
            e.printStackTrace();
            
        }
        
        try {
            
            logger.finest("Sleeping "+btDelay+" seconds");
            Thread.sleep(btDelay*1000);
            
        } catch (Exception e){
            
            logger.throwing(this.toString(),"Sleeping BT delay",e);
        }
       
       logger.finest("Do another inquiry ?:"+this.automaticDiscovery);
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
       logger.entering("MapperBTDiscovery","deviceDiscovered()");
       if (!remoteDeviceList.contains(btDevice)) {
           
           //log(//log_DETAILED,"Found device @"+getSafeBTAddress(btDevice));
           remoteDeviceList.addElement(btDevice);   
           
       } else {
           
           logger.warning("Found duplicate device");
           // SYSTEM ERROR:WE'VE FOUND A DUPLICATE BT DEVICE!'
           /* PARANOÎD
            * [RemoteDevice].getBluetoothAddress() - should be a safe non-blocking method...
            * ...but we shouldn't be here anyway
            * 
            */
           //String deviceAddress="[NOT_AVAIABLE!]");
           
               
           //deviceAddress = getSafeBTAddress(btDevice);
      
           
           //log(//log_DEBUG,"Discovered_duplicated_device")+deviceAddress);
           
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
        DiABluAnchorPoint tempDD = new DiABluAnchorPoint();      
                
        // clean the diablu device list
        //diABluDeviceList.removeAllElements();
        
        // report return code
        //log(//log_DEBUG,"[Inquiry_Completed]_")+totalDevicesFound+"Return_Code")+discType);
                                                             
        // Discovery Agent has finished so let's process the data            
        if ( totalDevicesFound > 0 ){
        
            // let's convert our found devices'
            for (RemoteDevice rd:remoteDeviceList){
                
                tempDD = safeRemoteToDiABlu(rd);
                
                // second duplicates verify
                if (!anchorPointList.contains(tempDD)){
                    
                    anchorPointList.addElement(tempDD);
                    //log(//log_DEBUG,"[BT-inquiryCompleted()] "+"Adding device:"+tempDD.toString());               
                    
                } else {
                    
                    //log(//log_DEBUG,"[BT-inquiryCompleted()] "+"Second cleaning discarded duplicated:"+tempDD.toString());
                }
                    
            } 
                
        }     
        
        // empty the lists 
        //log(//log_DEBUG,"[BT-inquiryCompleted()] "+"Cleaning device lists...");

        // clean the remote device list
        remoteDeviceList.removeAllElements();
        


        // implement the btDelay
        try {
            
            Thread.sleep(10000);
            
        } catch (Exception e) {
            
            //log(//log_COM_ERROR,"[Inquiry_Completed]_")+e.getLocalizedMessage());
            e.printStackTrace();
            
        }
        
         // synchronize bt threads
        synchronized (this) {
            
            try {
                
                //log(//log_DEBUG,"[Inquiry_Completed]_")+"Sincronizing"));
                this.notifyAll();
                  
            } catch (Exception e) {
                
                //log(//log_DEBUG,"[Inquiry_Completed]_")+e.getLocalizedMessage());
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
    
    private DiABluAnchorPoint safeRemoteToDiABlu(RemoteDevice rD){
        String temp;
        DiABluAnchorPoint tempDD = new DiABluAnchorPoint();
        
        tempDD.setUUID(getSafeBTAddress(rD));
        tempDD.setFname(getSafeBTFriendlyName(rD));
        tempDD.setDeviceClass(getSafeBTDeviceClass(rD));
        
        //log(//log_DEBUG,"Safe method returned:"+getSafeBTFriendlyName(rD)+"@"+getSafeBTAddress(rD)+"-"+getSafeBTMajorDeviceClass(rD)+"|"+getSafeBTMinorDeviceClass(rD));
        return tempDD;
        
    }
    
    private String getSafeBTDeviceClass(RemoteDevice rD){
        
        String deviceClass = "unknown";
        
        if ( rD == null ) { 
            
            //log(//log_DEBUG,"[getSafeBTMinorDeviceClass]_")+"Invalid,Empty_or_Null_argument"));
            
            
        } else {
        
            try {
            
              int minorDeviceClass = rD.getDeviceClass().getMinorDeviceClass();
              int majorDeviceClass = rD.getDeviceClass().getMajorDeviceClass();
              
               switch (majorDeviceClass) {
            
                // 	Computer (desktop,notebook, PDA, organizers, .... )
                case 256: {
                
                    switch (minorDeviceClass){
                    
                        case 4 : return "Desktop_workstation";
                        case 8 : return "Server_class_computer";
                        case 12: return "Laptop";
                        case 16: return "Handheld_PCPDA";
                        case 20: return "Palm_sized_PCPDA";
                        case 24: return "Wearable_computer";
                        default: return "Uncategorized";
                    
                    }
                
                }
            
                // Phone (cellular, cordless, payphone, modem, ...)
                case 512: {
                
                    switch (minorDeviceClass){
                
                        case 4 : return "Cellular_Phone";
                        case 8 : return "Cordless_Phone";
                        case 12: return "Smart_phone";
                        case 16: return "Wired_modem_or_voice_gateway";
                        case 20: return "Common_ISDN_Access";
                        default: return "Uncategorized";
                                        
                    }
                }
            
                // LAN /Network Access point
                case 768: {
                
                    return "LAN/Network_Acess_Point";
                
                }            
            
                // Audio/Video (headset,speaker,stereo, video display, vcr...
                case 1024: {
                
                    return "Audio/Video_Device";
                
                }
            
                // Peripheral (mouse, joystick, keyboards, ..... 
                case 1280:
                {
                    return "Peripheral_Device";
             
                }
            
                // Imaging (printing, scanner, camera, display, ...
                case 1536: {
                
                    return "Imaging_Device";
                
                }                        
            
                default: {
                
                    return "not_classified";
                
                }
            
            }    
       
            } catch ( Exception e ) {
            
                //log(//log_DETAILED,"Error trying to get minor device class");
                //log( //log_DEBUG,"[getSafeBTMinorDeviceClass]_")+ e.getLocalizedMessage() );
                e.printStackTrace();
            
            }
        }
        
        return deviceClass;
        
    }
    
    
    private String getSafeBTAddress(RemoteDevice rD){           
        
        String safeBTAddress = "Invalid,Empty_or_Null_argument";
        
        // ...safer dies older, paranoid is still alive:)
        if ( rD == null ) {
            
            //log(//log_DEBUG,"[getSafeBTAddress]_")+ safeBTAddress);
            return safeBTAddress;
            
        }
        
        safeBTAddress = "Unable_to_get_bluetooth_address";        
        
        try {
            
            safeBTAddress = rD.getBluetoothAddress();
            // //log(//log_DEBUG,"[getSafeBTAddress]_")+"Found")+safeBTAddress);
            
        } catch (Exception e){
            
            // TODO: update the //log sub-system
            //log(//log_DEBUG,"[getSafeBTAddress]_")+ e.getLocalizedMessage());
            //log(//log_COM_ERROR,"[getSafeBTAddress]_")+safeBTAddress);
            e.printStackTrace(); // avaiable info if you start diablu from command line
            
        }
           
        return safeBTAddress;
        
    }
    
    private String getSafeBTFriendlyName (RemoteDevice rD){
        
        
        String safeBTFriendlyName = "Invalid_Empty_or_Null_argument";
        
        // ...safer get's older, paranoid is still alive:)
        if ( rD == null ) {
            
            //log(//log_DEBUG,"[getSafeFriendlyName]_")+"Returning")+safeBTFriendlyName);
            return safeBTFriendlyName;
            
        }
        
        safeBTFriendlyName = "Unable_to_get_Bluetooth_address.";
        
        try {
            
            safeBTFriendlyName = rD.getFriendlyName(true);
            ////log(//log_DEBUG,"[getSafeFriendlyName]_")+"Found")+safeBTFriendlyName);
            
        } catch (Exception e){
            
            //log(//log_DEBUG,"[getSafeFriendlyName]_")+e.getLocalizedMessage());                 
            //log(//log_COM_ERROR,"[getSafeFriendlyName]_")+"Making_second_attempt"));
            e.printStackTrace(); // avaiable info if you start diablu from command line
            
            try {
                
              safeBTFriendlyName = rD.getFriendlyName(false);  
              //log(//log_DEBUG,"[getSafeFriendlyName]_")+"Found")+safeBTFriendlyName);
              
            } catch (Exception e2){
                
               //log(//log_DEBUG,"[getSafeFriendlyName]_")+e2.getLocalizedMessage());                 
               //log(//log_COM_ERROR,"[getSafeFriendlyName]_")+"Second_attempt_failed")+safeBTFriendlyName);
               e2.printStackTrace(); // avaiable info if you start diablu from command line
                
            }
        }
        
        return safeBTFriendlyName;    
        
    }
    

    
}
