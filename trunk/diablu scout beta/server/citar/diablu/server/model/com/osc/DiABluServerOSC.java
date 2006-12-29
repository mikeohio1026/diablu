/*
 * DiABluServerOSC.java
 *
 * Created on 11 de Maio de 2006, 16:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.com.osc;

// J2SE
import java.util.Vector;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

// j2se 1.5 - DiABlu System Constants - OSC Commands
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*;

// log
import java.util.logging.Logger;
import java.util.logging.Level;
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.LOG_MAIN_NAME;

//I18N & L9N
import java.util.ResourceBundle;

// OSC
import de.sciss.net.*;

// DiABlu System
import citar.diablu.server.model.data.*;
import citar.diablu.server.controller.out.osc.DiABluServerOSCModelListener;
import citar.diablu.server.controller.in.DiABluServerModelControllerListener;



/**
 *
 * @author nrodrigues
 */
public class DiABluServerOSC implements DiABluServerOSCModelListener {
    
    private static Logger logger = Logger.getLogger(LOG_MAIN_NAME); 
    private ResourceBundle diABluBundle;               // Resource Bundle
    private InetSocketAddress targetAddress;           // Target Address
    private int targetPort;                            // Target Port
    private DiABluServerModelControllerListener model; // Our Model Super Interface, needed to pass the log messages
       
    /**
     * Creates a new instance of DiABluServerOSC
     */
    public DiABluServerOSC() {
        
        // set the variables later on
        
    }
    
    
    public DiABluServerOSC(String targetAddress,int targetPort){
        
        // convert the String to InetSocketAddress
          try {
              
            this.targetAddress = new InetSocketAddress(targetAddress,targetPort);
            
        } catch (Exception e){
          //TODO:better implement this try to include ioexception and better handling  
            logger.warning("Error trying to resolve target address");
            
        }
 
        this.targetPort = targetPort;
                
    }
    
    
    /**
     *  This class listens to the model using the DiABluServerModelListener Interface
     *  
     */
    
    
    /* 
     * DiABlu Device's
     *
     */
    public void newDeviceList (Vector <DiABluDevice> updatedDiABlusList){
     

        
        if (updatedDiABlusList == null){
            
           logger.finest("[OSC -newDeviceList()] "+"Null argument");
            return;
            
        }
        
        if (updatedDiABlusList.isEmpty()) {
            
           logger.finest("[OSC -newDeviceList()] "+"Empty argument");
            return;
            
        }
        
        sendDeviceList(updatedDiABlusList);
        
    }
    
    public void newDiABluDevices (Vector <DiABluDevice> addDiABlus){               
     
        
        if (addDiABlus == null){
            
           logger.finest("[OSC - newDiABluDevices()] "+"Null argument");
            return;
            
        }
        
        if (addDiABlus.isEmpty()) {
            
           logger.finest("[OSC - newDiABluDevices()] "+"Empty argument");
            return;
            
        }
      
        sendAddDevices(addDiABlus);
        
    }
    
    public void editDiABluDevices (Vector <DiABluDevice> editDiABlus){
        
        if (editDiABlus == null){
            
           logger.finest("[OSC - editDiABluDevices()] "+"Null argument");
            return;
            
        }
        
        if (editDiABlus.isEmpty()) {
            
           logger.finest("[OSC - editDiABluDevices()] "+"Empty argument");
            return;
            
        }
        
        sendNamesChanged(editDiABlus);
        
    }
    
    public void removeDiABluDevices (Vector <DiABluDevice> removeDiABlus){
        
        if (removeDiABlus == null){
            
           logger.finest("[OSC - removeDiABluDevices()] "+"Null argument");
            return;
            
        }
        
        if (removeDiABlus.isEmpty()) {
            
           logger.finest("[OSC - removeDiABluDevices()] "+"Empty argument");
            return;
            
        }

                
        sendRemoveDevices(removeDiABlus);
        
    }
    
    public void newDeviceCount(int newDiABlusCount){
        
        sendDeviceCount(newDiABlusCount);
        
    }
    
    public void newMsg (DiABluMsg newMsg){
        
        sendMsg(newMsg);
        
    }
    
    public void newKey (DiABluKey newKey){
        
        sendKeys(newKey);
        
    }
    
    /* 
     * Settings
     */
    
    // Global    
    public void setResourceBundle(ResourceBundle rb){
        
        this.diABluBundle = rb;
        
    }
    
    // Output
    // Protocol OpenSoundControl
    public void setTargetAddress(String targetURL){
        
        // paranoid check
        if (targetURL==null || targetURL=="") { targetURL = LOCALHOST; }
        
        this.targetAddress = new InetSocketAddress(targetURL,this.targetPort);
        
    }
    
    public void setTargetPort(String targetPort){
        
        try {
                this.targetPort = Integer.parseInt(targetPort);
                
        } catch (Exception e) {
            
            this.targetPort = Integer.parseInt(OUT_DEFAULT_TARGET_PORT);
           logger.finest("Error trying to set:"+targetPort+"as OSC target port");
            
        }
        
    }
    
    /**
     * @Deprecated
     * Log
     * This method passes the log messages into the application model
     *   
    public void log(int priority,String log){
        
         model.log(priority,log);
        
    }
    */
    
    /** 
     * This method outputs the added devices present in the Vector
     * to 2 OSC commands /OSC_DEVICE_IN & /devicesin
     * /OSC_DEVICE_IN sends an OSC Bundle with all the devices
     * /devicesin sends a Single OSC Message with all the devices concatenated in
     *
     */    
    public void sendAddDevices(Vector <DiABluDevice> aDevices){
       
       InetSocketAddress addr = this.targetAddress;
       int totalDevicesAdded = 0;  // size of the list
       String[][] tempAddDevices1 = null;
       Object[] tempAddDevices2 = null;
       
       // paranoid check
       if (aDevices == null || aDevices.size() == 0){
          logger.finest("[DiABluOSC-sendAddDevices()] "+"Error!Received an empty list!!");
           return;
       }
       
       // get the vector's size'
       totalDevicesAdded = aDevices.size();
       
       tempAddDevices1 = new String[totalDevicesAdded][totalDevicesAdded]; // OSC_DEVICE_IN
       tempAddDevices2 = new Object[totalDevicesAdded];                    // devicesin
       
       // Convert the info
       tempAddDevices1 = vectorTOstring(aDevices);
       tempAddDevices2 = vectorTOobject(aDevices);
       
       
       // log it
       for (DiABluDevice dd:aDevices){
           
           logger.info("DeviceIn:["+dd.getID().getUUID()+"]["+dd.getID().getFName()+"]");           
                      
       }
       
       
       
       // Send it...
       
       // OSC /OSC_DEVICE_IN       
       sendBundle(OSC_DEVICE_IN,tempAddDevices1,addr);
       
       // OSC /devicesin
       sendMessage(OSC_DEVICES_IN,tempAddDevices2,addr);
       
    }
    
    /** 
     * This method outputs the removed devices present in the Vector
     * to 2 OSC commands /OSC_DEVICE_OUT & /OSC_DEVICES_OUT
     * /OSC_DEVICE_OUT sends an OSC Bundle with all the devices
     * /OSC_DEVICES_OUT sends a Single OSC Message with all the devices concatenated in
     *
     */ 
    public void sendRemoveDevices(Vector <DiABluDevice> rDevices){
       
       InetSocketAddress addr = this.targetAddress;
       int totalDevicesRemoved = rDevices.size();
       String[][] tempRemoveDevices1 = new String[totalDevicesRemoved][totalDevicesRemoved];
       Object[] tempRemoveDevices2 = new Object[totalDevicesRemoved];
       
       // Convert the info
       tempRemoveDevices1 = vectorTOstring(rDevices);
       tempRemoveDevices2 = vectorTOobject(rDevices);
       
       // log it
       for (DiABluDevice dd:rDevices){
           
           logger.info("DeviceOut:["+dd.getID().getUUID()+"]["+dd.getID().getFName()+"]");           
                      
       }
       
       
       // Send it...
       
       // OSC /OSC_DEVICE_OUT
       sendBundle(OSC_DEVICE_OUT,tempRemoveDevices1,addr);
       
       // OSC /OSC_DEVICES_OUT
       sendMessage(OSC_DEVICES_OUT,tempRemoveDevices2,addr);
       
    }
    
     /** 
     * This method sends the list of all devices detected by the DB System
     * to the OSC commands /devicelist
     *
     */ 
    public void sendDeviceList(Vector <DiABluDevice> lDevices){
        
        InetSocketAddress addr = this.targetAddress;
        // first verify that we've got some info
        if (lDevices != null) {
         
            int totalDevicesFound = lDevices.size();
            //Object[] tempListDevices = new Object();
        
            // Convert the info
            Object[] tempListDevices = vectorTOobject(lDevices);
        
            
            // OSC /devicelist
            sendMessage(OSC_DEVICE_LIST,tempListDevices,addr);
        } else return;
        
    }
        
    public void sendMsg(DiABluMsg newDMsg){
        
        InetSocketAddress addr = this.targetAddress;
        // get the sender's related ID
        String uuidT = newDMsg.getID().getUUID();
        String fnameT = newDMsg.getID().getFName();
        
        // get the message body
        String textT = newDMsg.getText();
        
        // convert the info
        Object[] oscMsg = new Object[] { uuidT, fnameT, textT } ;
        
        // log it
        logger.info("MessageIn:["+newDMsg.getID().getUUID()+"]["+newDMsg.getID().getFName()+"]["+newDMsg.getText()+"]");
        
        
        // send the message
        sendMessage(OSC_MESSAGE_IN,oscMsg,addr);
        
    }
    
    public void sendKeys(DiABluKey newDKey){
        
        InetSocketAddress addr = this.targetAddress;
        // get the sender's related id'
        String uuidT = newDKey.getID().getUUID();
        String fnameT = newDKey.getID().getFName();
        
        // get the keys
        String keypressedT = newDKey.getKeyPressed();
        String gactionT = newDKey.getGAction();
        
        // convert the info
        Object [] oscMsg = new Object[] { uuidT, fnameT, keypressedT, gactionT };
        
        // log it
        logger.info("KeyIn:["+newDKey.getID().getUUID()+"]["+newDKey.getID().getFName()+"]["+newDKey.getKeyPressed()+"]["+newDKey.getGAction()+"]");
        
        // send the keys
        sendMessage(OSC_KEY_IN,oscMsg,addr);        
        
    }
    
    public void sendNamesChanged(Vector <DiABluDevice> nDevices){
        
        
        InetSocketAddress addr = this.targetAddress;
        // paranoid :P
        if ( nDevices == null || nDevices.size() == 0 ) {
            
           logger.finest("[DiABluOSC-sendNamesChanged()] "+"Empty or null argument");
            return;
        }
        
        int totalNamesChanged = nDevices.size();
        
       logger.finest("[DiABluOSC-sendNamesChanged()] "+"Vector_size:"+totalNamesChanged);
        
       
        String uuidT = "";
        String fnameT = "";        
        Object[] oscMsg = null;
        
        for (DiABluDevice dbT:nDevices){
            
            // get the ID
            
            uuidT = dbT.getID().getUUID();
            fnameT = dbT.getID().getFName();
            
            // convert the info
            oscMsg = new Object[] { uuidT, fnameT };
            
            // log it
            logger.info("NameChanged:["+uuidT+"]["+fnameT+"]");
            
            // send it...
            sendMessage(OSC_NAME_CHANGED,oscMsg,addr);
        }
                              
    }
    
    /**
     *   Send's the total of devices present in the DiABlu System
     */
    public void sendDeviceCount(int dCount){
        
        logger.info("DeviceCount:"+dCount);
        sendMessage(OSC_DEVICE_COUNT,dCount,this.targetAddress);        
        
    }               
    
    /**
     *  This method converts a vector of DiABlu Devices into a String[][] array
     *  The returned array only contains the DiABlu's ID's (UUID & FName) ready to be sent
     *  by the sendMessage() method.
     *  TODO:Validation for an empty list
     */
    private String[][] vectorTOstring(Vector <DiABluDevice> xDevices) {
        
        String uuidT = "";
        String fnameT = "";
        DiABluDevice ddT;
        int tempSize = xDevices.size();        
        String[][] tempDBstring = new String[tempSize][2];
        
        for (int i=0; i<tempSize; i++){
            
            // get the device
            ddT = xDevices.get(i);
            
            // get the device info
            uuidT = ddT.getID().getUUID();
            fnameT = ddT.getID().getFName();
            
            // copy the device info
            tempDBstring[i][0]=uuidT;
            tempDBstring[i][1]=fnameT;
            
        }
        
        return tempDBstring;
    } 
    
    /**
     *  This method converts a vector of DiABlu Devices into a Object[] array
     *  The returned array only contains the DiABlu's ID's (UUID & FName) ready to be sent
     *  by the sendMessage() method.
     *  TODO:Further refine the checking of an empty or null vector
     */
    private Object[] vectorTOobject(Vector <DiABluDevice> xDevices){
        
        String uuidT = "";                 // temporary uuid
        String fnameT = "";                // temporary friendly name
        //DiABluDevice ddT;                  // temporary Diablu Device/
        int tempSize = 0;                  // size of arg vector
        int tempCounter = 0;               // counter for the returned Object[]
        Object[] tempDBobject = null;      // returned array
        
        if ( xDevices == null || xDevices.isEmpty() ) {
                    
           logger.finest("[DiABluOSC-vectorTOobject()] "+"Empty or null argument");
            return null;
            
        }  
        
        // get the size
        tempSize = xDevices.size();
        
        // initialize the returned array which has 2x nº elements of the incoming list
        tempDBobject = new Object[2*tempSize];
        
        // debug info
       logger.finest("[DiABluOSC-vectorTOobject()]List_size"+tempSize);        
                
        for (DiABluDevice ddT:xDevices){
                        
            // get the device info
            uuidT = ddT.getID().getUUID();
            fnameT = ddT.getID().getFName();
            
            // copy the device info
            logger.finest("Processing index:"+tempCounter);
            tempDBobject[tempCounter] = uuidT;
            tempDBobject[tempCounter+1] = fnameT;
            tempCounter+=2;
            
        }
       
         // debug info
       logger.finest("[DiABluOSC-vectorTOobject()] "+"Returned_list_size:"+tempDBobject.length);  
        
        return tempDBobject;
       
        
       }
                
    
    /**
     * This method sends a Bundle of several DiABlu Devices
     * Each device is sended on the form 
     * [UUID][FName][Key Pressed(if any)][gAction(if any)][text(if any)]
     *
     */ 
    
    public void sendBundle ( String oscCommand, String[][] brutus , InetSocketAddress addr ){
        
        final long serverLatency = 50;
        final long now = System.currentTimeMillis() + serverLatency;
        
        DatagramChannel dch = null;
        OSCBundle bundle;
        OSCTransmitter trns;
        
               
	try {
	        dch = DatagramChannel.open();
                dch.configureBlocking(true);
                trns = new OSCTransmitter(dch, addr);
                bundle = new OSCBundle (now);
                
                for ( int i = 0; i < brutus.length ; i++ ){
                    
                   logger.finest("[DiABluOSC-sendBundle()] "+"Processing_packets...["+brutus[i][0]+"]["+brutus[i][1]+"]\n");
                    Object[] objT = new Object[] { brutus[i][0] , brutus[i][1] };
                    bundle.addPacket( new OSCMessage (oscCommand, objT));
                
                }
                
                /* Sending message */
               logger.finest("[DiABluOSC-sendBundle()] "+ oscCommand + "| [BRUTUS]\n");
		trns.send(bundle, addr);
	}

	catch( Exception e1 ) {
            // TODO: catch this exception
            // printException( e1 );
	}
	finally {
            if( dch != null ) {
              try {
                  dch.close();
              } catch( Exception e2 ) {
                // TODO: catch this exception
                // printException( e2 );
              };
            }
        }  
     }
    
    /**
     * Sends a simple osc message
     */
    public static void sendMessage ( String oscCommand, String message, InetSocketAddress addr ){
        
        DatagramChannel dch = null;
        OSCTransmitter trns;
               
	try {
	        dch = DatagramChannel.open();
                trns = new OSCTransmitter(dch, addr);                
                
                /* Sending message */
                logger.config("[DiABluOSC - sendMessage()] "+"OSC Command:" + oscCommand + " | Content:" + message + "\n");
		trns.send( new OSCMessage( oscCommand, new Object[] { 
                                                                   message
                                                               }
                                          ),addr);
	}

	catch( Exception e1 ) {
            
                // TODO: catch this exception
                // printException( e1 );
	}
	finally {
            if( dch != null ) {
              try {
                  dch.close();
              } catch( Exception e2 ) {
                  
                  // TODO: catch this exception
                  //printException( e2 );
              };
            }
        }   
        
    }
    
    /**
     * Sends a composite Object[] message
     */
    public static void sendMessage ( String oscCommand, Object[] brutus , InetSocketAddress addr ){
        
        
        DatagramChannel dch = null;
        OSCTransmitter trns;
               
	try {
	        dch = DatagramChannel.open();
                trns = new OSCTransmitter(dch, addr);
                
                
                /* Sending message */
                System.out.println("[DiABluOSC - sendMessage()] "+"OSC Command:" + oscCommand + " | [List_Size:"+brutus.length+"]\n");
		trns.send( new OSCMessage( oscCommand, brutus
                                          ),addr);
	}

	catch( Exception e1 ) {
            //printException( e1 );
	}
	finally {
            if( dch != null ) {
              try {
                  dch.close();
              } catch( Exception e2 ) {
                  
                 //printException( e2 );
              };
            }
        }  
     }
     
    /**
     * Sends simple OSC message with a [int]
     * Suited for /devicecount
     */ 
    public static void sendMessage ( String oscCommand, int num , InetSocketAddress addr ){
        
        DatagramChannel dch = null;
        OSCTransmitter trns;
               
	try {
	        dch = DatagramChannel.open();
                trns = new OSCTransmitter(dch, addr);
                
                
                /* Sending message */
                System.out.println("[DiABluOSC -sendMessage()]"+"Sending OSC Command:" + oscCommand + " | [Count:"+num+"]\n");
		trns.send( new OSCMessage( oscCommand, new Object[] { 
                                                                        new Integer(num) 
                                                                    }
                                          ),addr);
	}

	catch( Exception e1 ) {
           //TODO:Handle this exception   
            e1.printStackTrace();
           //printException( e1 );
	}
        
	finally {
            if( dch != null ) {
              try {
                  dch.close();
              } catch( Exception e2 ) {
                  //TODO:Handle this exception
                  //printException( e2 );
                  e2.printStackTrace();
              };
            }
        }  
    }
    
    
    public void setLogLevel(Level newLevel){
        
        this.logger.setLevel(newLevel);
    }
}
