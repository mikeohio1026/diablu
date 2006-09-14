/*
 * DiABluServerModel.java
 *
 * Created on 9 août 2006, 17:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model;

// j2se
import java.util.Vector;
import java.util.Iterator;

// data classes
import citar.diablu.server.model.data.*;

// settings
import citar.diablu.server.model.settings.DiABluServerSettings;
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*;

// server view
import citar.diablu.server.controller.in.view.DiABluServerViewControllerListener;
import citar.diablu.server.controller.out.view.DiABluServerViewModelListener;
import citar.diablu.server.view.main.DiABluServerView;

// general model listener
import citar.diablu.server.controller.out.DiABluServerModelListener;

// bt 
import citar.diablu.server.controller.out.bt.DiABluServerBTModelListener;
import citar.diablu.server.model.com.bt.*;
import citar.diablu.server.controller.in.devices.bt.DiABluServerBTControllerListener;

// jsr82
import javax.bluetooth.*;

// osc
import citar.diablu.server.controller.out.osc.DiABluServerOSCModelListener;
import citar.diablu.server.model.com.osc.DiABluServerOSC;

// simulator 
import citar.diablu.server.controller.out.simulator.DiABluSimulatorModelListener;
import citar.diablu.server.view.simulator.DiABluSimulatorView;
import citar.diablu.server.controller.in.devices.simulator.DiABluSimulatorControllerListener;

// global
import java.util.ResourceBundle;
import java.util.Locale;


/**
 *
 * @author nrodrigues
 */
public class DiABluServerModel implements DiABluServerViewControllerListener, DiABluServerBTControllerListener, DiABluSimulatorControllerListener {
 
    // Settings
    private DiABluServerSettings settings;
    
    // General Model Listeners (View,OSC)
    private Vector <DiABluServerModelListener> generalListeners ;
    
    // BT Listener
    private DiABluServerBTModelListener btListener;
    private boolean BT_SERVER_LISTENER_READY = false;
    
    // Simulator Listener
    private DiABluSimulatorModelListener simListener;
    private boolean SIMULATOR_LISTENER_READY = false;
    
    // OSC Listener
    private DiABluServerOSCModelListener oscListener;
    private boolean OSC_LISTENER_READY = false;
    
    // Main View
    private DiABluServerViewModelListener serverView;
    private boolean SERVER_VIEW_LISTENER_READY = false;
    
    // Simulator View
    /*
     * Need because the model may hide/unhide it
     */
    private DiABluSimulatorView simView;
    
    // Model Data
    public String serviceName;
    public  String serviceDescription;
    public int btDelay;
    public int btVCycles;
    public String targetAddress;
    public String targetPort;
    public String country;
    public String language;
    public ResourceBundle diabluBundle;
    public boolean isSimulatorAuto;
    public boolean isSimulatorRunning=false;
    public int logDetail;
    
    // Device List
    private Vector <DiABluDevice> currentDiABluDevices;
    
    // Device's Black List
    private Vector <DiABluID> blackList;
    
    // Verify Cycles Device List
    private Vector <DiABluDevice> verifyDiABluDevices;
  
    /** Creates a new instance of DiABluServerModel */
    public DiABluServerModel(String args[]) {
 
        // general view's'
        generalListeners = new Vector <DiABluServerModelListener>();
        
        // device's data
        currentDiABluDevices = new Vector <DiABluDevice> ();
        blackList = new Vector <DiABluID> ();
        verifyDiABluDevices = new Vector <DiABluDevice> ();
        
        settings = new DiABluServerSettings(args);
        settings.updateModel(this);
 
    }
    
    /*
     * This method starts
     */
    public void startDiABluSystem() {

        // Main View
        System.out.println("View...");
        
        try {
        
       java.awt.EventQueue.invokeAndWait(new Runnable() {
                    
          public void run() {
                
              initializeGUI();   
            
          }
        
       });} catch (Exception e){
           
           System.out.println("view error:"+e.getLocalizedMessage());
       }
       
        if (serverView != null){
        
           initializeServerView(serverView);
           registerGeneralListener(serverView);
        
        } else {
           
           System.out.println("NULL VIEW ERROR!!!");
        }            
    
        // OSC Listener
        System.out.println("OSC...");
        this.oscListener = new DiABluServerOSC(this);
        registerGeneralListener(oscListener);
        OSC_LISTENER_READY = true;
               
        // Simulator
        /*
        * Need because the model may hide/unhide it
        * TODO:Thread the simulator
        *
        */
        System.out.println("Simulator");
        this.simView = new DiABluSimulatorView(this);
        this.simListener = simView;
        SIMULATOR_LISTENER_READY = true;
        
        if (this.isSimulatorAuto) {
            
            simView.setVisible(true);
            serverView.simulatorIsRunning(true);
            
        }
        
        // BT 
        System.out.println("Starting BT...");
        this.btListener = new DiABluServerBT(this);
        initializeBTListener(btListener);
        btListener.startSystem();
        BT_SERVER_LISTENER_READY = true;

        try {
           
            // now the discovery
            DiABluServerBTDeviceDiscovery diABluDiscovery = new DiABluServerBTDeviceDiscovery(this);
            // set visible the serverView
            serverView.clearLog();
            serverView.setVisible(true);
            SERVER_VIEW_LISTENER_READY = true;
            System.out.println("Starting Device Discovery...");
            diABluDiscovery.run();            
            log(LOG_DETAILED,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DSBT|startSystem()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Started_device_discovery"));
                
        } catch (BluetoothStateException bte1){
            
            serverView.clearLog();
            serverView.setVisible(true);
            SERVER_VIEW_LISTENER_READY = true;
            log(LOG_UNEXPECTED_ERROR,"WARNING!!Bluetooth device not reachable!!!!");
            log(LOG_SIMPLE,"Check your bluetooth hardware and restart DiABlu System");
            log(LOG_SIMPLE,"SIMULATION ONLY MODE");
            // TODO: Force the simulator not matter what settings.
            bte1.printStackTrace();
            
        }
         
    }
    
    /**
     *  IN
     *  ------------------------
     *  Model Controller Methods (citar.diablu.server.controller.in.*;)
     *
     */  
    
    /**
     *  DiABluServerModelControllerListener
     *  -----------------------------------
     *  This interface is the parent of all, since all the other classes need to log
     *  If we need to add a general method to all we should do it here
     *
     */
    public void log(int priority, String logMsg){
        
        // TODO:process & filter messages accordanly with priority 
        // System.out.println("MODEL["+priority+"]"+logMsg);
        if (serverView!=null)serverView.log(priority, logMsg);
        
    }
    
    /**
     *
     *  DiABluServerDevicesListener
     *  ---------------------------
     *  This interface contains common methods related to the information gathered on devices found
     *  Both the DiABluServerView & DiABluServerOSC classes listen to them since it's the parent
     *  interface on their own specific devices   
     *
     */
    
    // Arrival of a new DiABlu Msg
    public void newMsg (DiABluMsg newMsg){
        
        serverView.newMsg(newMsg);
        oscListener.newMsg(newMsg);
        
    }
    
    // Arrival of a new DiABlu Key
    public void newKey (DiABluKey newKey){
        
        serverView.newKey(newKey);
        oscListener.newKey(newKey);
        
    }   
    
    /*
     * DiABluServerBTControllerListener
     * --------------------------------
     * This interface contains Bluetooth specific methods
     * 
     */
    
    /**
     * This method is called after a Bluetooth Device Discovery has been completed.
     * The BT class will send the list of found DiABlu Devices so that this model processes it
     * and takes the appropriate actions
     *
     */
    public void newDeviceList (int deviceType, Vector <DiABluDevice> newDeviceList){
        
        // MAKE SURE OF NEW CHANGES
        Vector <DiABluDevice> finalDeviceList = new Vector <DiABluDevice> ();   // The final list with all the devices
        Vector <DiABluDevice> namesChangedList = new Vector <DiABluDevice> ();  // The list with the devices that have changed their friendly name
        Vector <DiABluDevice> devicesOutList = new Vector <DiABluDevice> ();    // The list with the exited devices       
        Vector <DiABluDevice> devicesInList = new Vector <DiABluDevice> ();     // The list with the newly discovered devices
        Vector <DiABluDevice> oldDeviceList = new Vector <DiABluDevice> ();     // Our last device list of your specific type [need to add multiple input protocol]
        Vector <DiABluDevice> entireOldDeviceList = new Vector <DiABluDevice> (); // Our complete last device list   
        Vector <DiABluDevice> removedDevicesList = new Vector <DiABluDevice> (); // Definitelly removed device list
        Vector <DiABluDevice> newVerifyDiABluDevicesList = new Vector <DiABluDevice> (); // Updated verify cycles list
       
        int oldCount = currentDiABluDevices.size();                             // needed to check on device count changes
        int newCount = 0;                                                       // final size of finalDeviceList
        boolean newDevice = false;                                              // flag that helps telling it's a new device
        boolean verifiedDevice = false;                                         // flag that tells if the device has been verified
        int complexCompare;                                                     // comparation factor between two diablu devices
        boolean notifyUpdatedDeviceList = false;                                // flag that if we need to output a new device list
        DiABluDevice oldDD = new DiABluDevice();                                // temporary single DiABluDevice buffer.used to compare list elements
        DiABluDevice removedDD = new DiABluDevice();                            //  " " " "
        DiABluDevice verifyDD = new DiABluDevice();                            // " " " "
        
        /**
         * 0 Special Cases first
         * If we get, or already have an empty list we
         * can shortcut the processing
         */
        
        // the usual paranoia
        if (newDeviceList == null) {
            
            log(LOG_UNEXPECTED_ERROR,"[Model-newDeviceList()] "+"Null argument!");
            return;
            
        } else {
            
            log(LOG_DEBUG,"[Model-newDeviceList()] "+"Received new device list:"+newDeviceList.size());
            
        }
        
        /**
         * DEBUG ONLY
         * Remove comments if you wish to check your newly arrived list
         *
            for (DiABluDevice dd:newDeviceList){
            
                log(LOG_DEBUG,"UUID:"+dd.getID().getUUID());
                log(LOG_DEBUG,"Fname:"+dd.getID().getFName());
                log(LOG_DEBUG,"MajorDeviceClass:"+dd.getMajorDeviceClass());
                log(LOG_DEBUG,"StringDeviceClass:"+dd.getStringDevice());
            
            }
        */
        
        if (!currentDiABluDevices.isEmpty()){ 
            
            log(LOG_DEBUG,"[Model-newDeviceList()] "+"Current devices:"+currentDiABluDevices.size());
            
            // get & filter the current data
            entireOldDeviceList = this.currentDiABluDevices;        
        
            // get all of this device's type 
            oldDeviceList = cropDeviceList(deviceType,entireOldDeviceList);            
        
            // add all other device types into the final list
            finalDeviceList = stripDeviceList(deviceType,entireOldDeviceList);                  
        
        }
        
        // search came in empty pockets
        if (newDeviceList.isEmpty()){
            
            log(LOG_DEBUG,"[Model-newDeviceList()] "+"Empty new device list.");
            // nothing to do here

            
        } else {
            
            // we already have some devices in dah house :)
            //check if we have any of these devices
            if (oldDeviceList.isEmpty()){
                                
                log(LOG_DEBUG,"[Model-newDeviceList()] "+"Old device list empty.Adding entire new device list.");     
                
                for (DiABluDevice dd:newDeviceList){
                
                    devicesInList.addElement(dd);
                    finalDeviceList.addElement(dd);
                    
                }
            
            } else {
             
                log(LOG_DEBUG,"[Model-newDeviceList()] "+"Comparing both old and new lists...");
                for (DiABluDevice newDD:newDeviceList){
                                        
                    // reset the newDevice flag
                    newDevice = true;
                   
                    
                    for ( Iterator oldI = oldDeviceList.iterator(); oldI.hasNext(); ){
                       
                        // get our "old" device
                        try {
                                
                            oldDD = (DiABluDevice) oldI.next();
                                
                        } catch (Exception e){
                            
                            log(LOG_UNEXPECTED_ERROR,"[Model-newDeviceList()] "+"Cast Exception converting Object to DiABlu Device");
                            log(LOG_DEBUG,"[Model-newDeviceList()] "+ e.getLocalizedMessage());
                            e.printStackTrace();
                            
                        }
                        
                        // compare the devices
                        complexCompare = newDD.complexCompareTo(oldDD);
                        
                        // analise the result
                        switch (complexCompare) {
                            
                            // 100% match
                            case 0: {
                                
                                // add it to the final list
                                finalDeviceList.addElement(newDD);
                                
                                // remove it from the old so that the next devices don't need to compare to this one'
                                // oldDeviceList.remove(oldDD); DEPRECATED
                                // new one:
                                try {
                                        oldI.remove();
                                        
                                } catch (Exception e) {
                                    
                                    log(LOG_DEBUG,"[Model - newDeviceList()] "+"Exception while trying to remove device.");
                                    log(LOG_DEBUG,"[Model - newDeviceList()] "+"Message:"+e.getLocalizedMessage());
                                    e.printStackTrace();
                                    
                                }
                                
                                // it isn't a new device
                                newDevice = false;
                                
                                // detailed log
                                log(LOG_DETAILED,"Device already in the system.Adding to final list:"+newDD.toString());
                                break;
                                
                            }
                            
                            // diferent fname
                            case 1: {
                                
                                // add it to the changed names list
                                namesChangedList.addElement(newDD);
                                
                                // add it to the final list
                                finalDeviceList.addElement(newDD);
                                
                                // remove it from the old so that the next devices don't need to compare to this one'
                                oldDeviceList.remove(oldDD);                                
                                
                                // it isn't a new device                                
                                newDevice = false;
                                
                                // detailed log
                                log(LOG_DETAILED,"Device:"+oldDD.toString()+" has changed it's name to:"+newDD.toString());
                                break;
                            }
                            // no match
                            default: {
                                
                                log(LOG_DEBUG,"No match between:"+oldDD.toString()+" & "+ newDD.toString());
                                break;
                            }
                                
                        }
                                                
                    }
                
                    // check if it's a new device
                    if (newDevice){
                        
                        // add it to the devices in list
                        devicesInList.addElement(newDD);
                        
                        // add it to the final list
                        finalDeviceList.addElement(newDD);
                        
                        // detailed log
                        log(LOG_DETAILED,"Found new device:"+newDD.toString());
                        
                    }
                    
                
                }
            
            }
        } 
        removedDevicesList = oldDeviceList;
        /**
         * DEBUG
         */
        log(LOG_DEBUG,"Finished processing list.Analysing results");
        log(LOG_DEBUG,"--------------------------------------------");
        log(LOG_DEBUG,"Add device list:"+devicesInList.size());
        log(LOG_DEBUG,"Remove device list:"+removedDevicesList.size());
        log(LOG_DEBUG,"Changed names:"+namesChangedList.size());
        log(LOG_DEBUG,"Final device list:"+finalDeviceList.size());
        log(LOG_DEBUG,"--------------------------------------------");
        
        
        // Verify cycles check        
        
        // DEBUG
        btVCycles = 0;
        
        // first make sure we have something to do
        if (btVCycles > 0){
                    
           // let's check the list
           if (verifyDiABluDevices.isEmpty()){
                        
              log(LOG_DEBUG,"[Model-newBTDeviceList()] "+"Found empty verify list.Filling "+oldDeviceList.size()+" elements");
              // since the list is empty we only have to add the removed devices
              for (int i = 0;i<oldDeviceList.size();i++){
                  
                 // get the device                    
                 removedDD = oldDeviceList.elementAt(i);                                                        
                  
                 // update the verify counter
                 removedDD.incrementDetectionCounter();
                 
                 // add it to the list
                 verifyDiABluDevices.addElement(removedDD);
                                        
              }
     
           } else {
                                            
                // search & compare lists                
                for ( int i = 0; i < oldDeviceList.size(); i++ ){
                    
                    // get the old device
                    removedDD = oldDeviceList.elementAt(i);
                                       
                    for ( int j = 0; j < verifyDiABluDevices.size(); j++ )   {
                        
                        verifyDD = verifyDiABluDevices.elementAt(j);
                        
                        if (removedDD.equals(verifyDD)){
                                                                        
                           // found a match.let's check if it's still under vCycle'
                           if (verifyDD.getDetectionCounter() > this.btVCycles) {
                                                                                
                                        log(LOG_DEBUG,"[Model-newBTDeviceList()] "+"Tottally removing:"+verifyDD.toString());
                                        // it's time to get out
                                        removedDevicesList.addElement(verifyDD);
                                        // clean the verify list
                                        verifyDiABluDevices.remove(verifyDD); 
                                        
                                    
                           } else {
                                        
                                        // device is still under vCycle limit
                                        // update it's counter
                                        // TODO:make sure this updates the verifyDiABluDevices vector
                                        log(LOG_DEBUG,"[Model-newBTDeviceList()] "+"Incrementing verify counter on:"+verifyDD.toString());
                                        verifyDD.incrementDetectionCounter();                                        
                                   
                           }
                                    
                                    // check the flag
                                    verifiedDevice = true;                       
                        }
                
                    }
                    // let's check if this device has already been verified
                         
                    if (!verifiedDevice){
                                
                            log(LOG_DEBUG,"[Model-newBTDeviceList()] "+"Adding "+removedDD.toString()+" to verify table");
                            // first time removed
                            removedDD.incrementDetectionCounter();
                            // give it another chance ;)
                            verifyDiABluDevices.addElement(removedDD);
                            finalDeviceList.addElement(removedDD);
                                                                       
                    }                             
                      
                }                                               
                  
           }                                        
         
        }          
        // End of verify
        
                    
        /**
         * DEBUG
         */
        log(LOG_DEBUG,"Finished verifying lists.Analysing results");
        log(LOG_DEBUG,"--------------------------------------------");
        log(LOG_DEBUG,"Add device list:"+devicesInList.size());
        log(LOG_DEBUG,"Remove device list:"+removedDevicesList.size());
        log(LOG_DEBUG,"Changed names:"+namesChangedList.size());
        log(LOG_DEBUG,"Final device list:"+finalDeviceList.size());
        log(LOG_DEBUG,"--------------------------------------------");
              
        // Process the results
                   
        log(LOG_DEBUG,"[Model-newDeviceList()] "+"Copying devices into memory:"+finalDeviceList.size());         
        currentDiABluDevices.removeAllElements();
              
        if (!finalDeviceList.isEmpty()){
                                                  
                  
            for (DiABluDevice dd:finalDeviceList){
                      
                // copy the final devices into memory                      
                log(LOG_DEBUG,"[Model-newDeviceList()] "+"Copying device:"+dd.toString());                      
                currentDiABluDevices.addElement(dd);
                      
                // DEBUG:check                     
                DiABluDevice ddCheck = currentDiABluDevices.lastElement();                            
                log(LOG_DEBUG,"[Model-newDeviceList()] "+"Checked added element "+ddCheck.toString()+"|"+ddCheck.getStringDevice());
                      
                  
            }
                  
              
        }
        currentDiABluDevices = finalDeviceList;
        
        // devicesout
        if (!removedDevicesList.isEmpty()) {
            
                    
            log(LOG_DETAILED,"[Model-newDeviceList()] "+"Removing "+removedDevicesList.size()+" devices");
            // TODO:update this to update multiple general views listeners 
            if ( SERVER_VIEW_LISTENER_READY ) serverView.removeDiABluDevices(removedDevicesList);                    
            // Make sure we don't send black listed devices'                    
            if ( OSC_LISTENER_READY ) oscListener.removeDiABluDevices(filterBlackListed(removedDevicesList));
                    
            notifyUpdatedDeviceList = true;
            
              
        }
        
        // devicesin
        if (!devicesInList.isEmpty()){
            
                    log(LOG_DETAILED,"[Model-newDeviceList()] "+"Adding "+devicesInList.size()+" devices");
                    // TODO:update this to update multiple general views listeners                  
                    serverView.newDiABluDevices(devicesInList);
                    
                    Vector <DiABluDevice> diabluOSCout = new Vector <DiABluDevice> ();
                    diabluOSCout = filterBlackListed(devicesInList);
                    log(LOG_DEBUG,"[Model-newDeviceList()] "+"Sendind "+diabluOSCout.size()+" devices to osc devicesin");
                    
                    if ( OSC_LISTENER_READY ) oscListener.newDiABluDevices(diabluOSCout);
            
                    notifyUpdatedDeviceList = true;
              }
            
              // nameschanged
              if (!namesChangedList.isEmpty()){
            
                    log(LOG_DETAILED,"[Model-newDeviceList()] "+namesChangedList.size()+" devices have changed friendly name");                        
                    serverView.editDiABluDevices(namesChangedList);
                    if ( OSC_LISTENER_READY ) oscListener.editDiABluDevices(filterBlackListed(namesChangedList));
                    
                    notifyUpdatedDeviceList = true;
                    
              }
              
              // devicelist
              if (notifyUpdatedDeviceList && !finalDeviceList.isEmpty()) {
            
                    log(LOG_DETAILED,"[Model-newDeviceList()] "+"Sending the new device list("+finalDeviceList.size()+")");
                    if ( OSC_LISTENER_READY ) oscListener.newDeviceList(filterBlackListed(finalDeviceList));
            
              }
            
              // devicecount
              newCount = finalDeviceList.size();
              if ( oldCount != newCount ) {
            
                    log(LOG_DETAILED,"[Model-newDeviceList()] "+"Sending new device count:"+newCount);
                    if ( OSC_LISTENER_READY ) oscListener.newDeviceCount(newCount);
            
              }
              
              // update our own data 
              
              // Paranoid filter
              /*
              for (DiABluDevice oDD : currentDiABluDevices){
              
                  
                  if (!finalDeviceList.contains(oDD)) {
                      log(LOG_DEBUG,"Removing element...");
                      currentDiABluDevices.remove(oDD);
                  }
                  
              }
              for (DiABluDevice nDD: finalDeviceList){
                  
                  if (!currentDiABluDevices.contains(nDD)){
                      log(LOG_DEBUG,"");
                      currentDiABluDevices.add(nDD);
                  }
                  
              }
              */

        
              
              
        /**
         * DEBUG
         */
        log(LOG_DEBUG,"FINISHED NEW DEVICE LIST.Analysing results");
        log(LOG_DEBUG,"--------------------------------------------");
        log(LOG_DEBUG,"Add device list:"+devicesInList.size());
        log(LOG_DEBUG,"Remove device list:"+removedDevicesList.size());
        log(LOG_DEBUG,"Changed names:"+namesChangedList.size());
        log(LOG_DEBUG,"Final device list:"+finalDeviceList.size());
        log(LOG_DEBUG,"--------------------------------------------");
    }
    
    /*
     * DiABluSimulatorControllerListener
     * ---------------------------------
     * These methods are called by the Simulator Controller  
     *
     */
    
    // New Simulated Device
    public void newSimDiABluDevice(DiABluDevice addDiABlu){
        
        if (!currentDiABluDevices.contains(addDiABlu)) {
            
            // update model data
            currentDiABluDevices.add(addDiABlu);
            
            // update related views
            
            // server view
            Vector <DiABluDevice> tempDBVector = new Vector <DiABluDevice> ();
            tempDBVector.addElement(addDiABlu);
            serverView.newDiABluDevices(tempDBVector);
            
            // osc View
            if ( OSC_LISTENER_READY ) oscListener.newDiABluDevices(tempDBVector);
            if ( OSC_LISTENER_READY ) oscListener.newDeviceList(currentDiABluDevices);
            if ( OSC_LISTENER_READY ) oscListener.newDeviceCount(currentDiABluDevices.size());
            
        }
        
    }
    
    // Edit Simulated Device
    public void editSimDiABluDevice (DiABluDevice editDiABlu){
          
        // paranoid
        if ( editDiABlu == null ){
         
            log(LOG_DEBUG,"[Model -editSimDiABluDevice] "+"Null argument");
            return;
            
        }
         
        DiABluDevice dd = new DiABluDevice();
        for ( Iterator i = currentDiABluDevices.iterator(); i.hasNext(); ){
            
       
            dd = (DiABluDevice) i.next();
            
            // if (dd.getID().getUUID().equalsIgnoreCase(editDiABlu.getID().getUUID())) {
            if ( dd.equals(editDiABlu) ) { 
                
                // remove the element
                // currentDiABluDevices.remove(dd);
                i.remove();
            
                currentDiABluDevices.addElement(editDiABlu);
                
                
                // update related views
                Vector <DiABluDevice> tempDBVector = new Vector <DiABluDevice> ();
                tempDBVector.addElement(editDiABlu);  
                
                // server View                    
                serverView.editDiABluDevices(tempDBVector);
                
                // osc View
                oscListener.editDiABluDevices(tempDBVector);
                
                break;
            }
            
        }                        
        
    }
    
    // Remove Simulated Device
    public void removeSimDiABluDevice (DiABluDevice removeDiABlu){
        
        if (currentDiABluDevices.contains(removeDiABlu)) {
            
            // update the model data
            currentDiABluDevices.remove(removeDiABlu);
            
            // update related views            
            Vector <DiABluDevice> tempDBVector = new Vector <DiABluDevice> ();
            tempDBVector.addElement(removeDiABlu);
            
            // server View
            serverView.removeDiABluDevices(tempDBVector);
            
            // osc View
            oscListener.removeDiABluDevices(tempDBVector);
            
        }    
        
    }
    
    /**
     *  DiABluServerViewControllerListener
     *  ----------------------------------
     *  These interface contains specific methods of the Server's View
     * 
     *
     */
    
     public void newSelectedDevice(DiABluID did){
   
       // paranoid
       if ( did == null ) {
           
           log(LOG_UNEXPECTED_ERROR,"[Model - newSelectedDevice()] "+"Null argument");
           return;
           
       }
       
       // locate the device index
       int selectedIndex = getDeviceIndex(did);
       log(LOG_DEBUG,"[Model - newSelectedDevice()] "+"Device located @"+selectedIndex);
       
       
       // verify that we've found a match'
       if (selectedIndex == -1 || selectedIndex == -2) { return; }
       
       // paranoid check, for the very special case were a device is removed after being located
       int maximumIndex = currentDiABluDevices.size();
       if (selectedIndex > maximumIndex) { selectedIndex = maximumIndex-1; }
       
       // send the device info
       if ( SIMULATOR_LISTENER_READY ) simListener.setSelectedDevice(currentDiABluDevices.elementAt(selectedIndex));
        
    }
        
  /*
   *  i18n methods
   */
   public void newLanguage(String newLanguage){
    
        this.language = newLanguage;
        //UPDATE RESOURCE_BUNDLE
        
   }  
        
   public void newCountry(String ct){
       
       this.country = ct;
       // UPDATE RESOURCE BUNDLE & VIEWS
       
   }
   
   /*
    *
    *  black list methods
    *
    */
   public void addToBlackList(DiABluID did){
       
       if (did == null) {
           
           log(LOG_DEBUG,"[Model - addToBlackList()] "+"Null argument");
           return;
       }
       
       if (!blackList.contains(did)) {
           
           log(LOG_DEBUG,"[Model - addToBlackList()] "+"Blacklisting:"+did.toString()+"|"+did.getUUID());
           blackList.add(did);
           
       }
       
       DiABluDevice oldDD = getDiABluDevice(did);
       if (oldDD.getID().getUUID().equalsIgnoreCase("")) { 
                
            // device not found 
            log(LOG_DEBUG,"[Model - addToBlackList()] "+"Black listed device not found:"+did.toString());
            return;
       } 

       // found device
       // update view
       Vector <DiABluDevice> updateView = new Vector <DiABluDevice> ();
       updateView.add(oldDD);
       serverView.removeDiABluDevices(updateView);
       updateView.removeAllElements();
       oldDD.setStatus(2); // blacklisted status
       updateView.add(oldDD);
       serverView.newDiABluDevices(updateView);
       
   }
   
   public void removeFromBlackList(DiABluID did){
       
       if (did == null) {
           
           log(LOG_DEBUG,"[Model - removeFromBlackList()] "+"Null argument");
           return;
       }
       
       if (blackList.contains(did)){
            
           log(LOG_DEBUG,"[Model - removeFromBlackList()] "+"Unblacklisting:"+did.toString());
           blackList.remove(did);
           
       }     
       
       DiABluDevice oldDD = getDiABluDevice(did);
       if (oldDD.getID().getUUID().equalsIgnoreCase("")) { 
                
            // device not found 
            log(LOG_DEBUG,"[Model - removeFromBlackList()] "+"Black listed device not found:"+did.toString());
            return;
       } 

       // found device
       // update view
       Vector <DiABluDevice> updateView = new Vector <DiABluDevice> ();
       updateView.add(oldDD);
       serverView.removeDiABluDevices(updateView);
       updateView.removeAllElements();
       // TODO:reformat this
       oldDD.setStatus(1); // bluetooth status
       updateView.add(oldDD);
       serverView.newDiABluDevices(updateView);
   }
   
   public void clearBlackList(){
       
       blackList.removeAllElements();
       
   }
    
   // Simulator
    public void startSimulator(){
       
       //TODO
        simView.setVisible(true);
        isSimulatorRunning = true;
        serverView.simulatorIsRunning(isSimulatorRunning);
        SIMULATOR_LISTENER_READY = true;
      
    }
    
    public void stopSimulator(){
        
       simView.setVisible(false);
       isSimulatorRunning = false;
       serverView.simulatorIsRunning(isSimulatorRunning);
       SIMULATOR_LISTENER_READY = false;
       
    }
    
    public void autoSimulator(boolean as){
               
       this.isSimulatorAuto = as; 
        
    }
   
    // Bluetooth
     public void newServiceName(String newServiceName){
    
        btListener.setServiceName(newServiceName);
        
    }
    
    public void newServiceDescription(String newServiceDescription){
        
        System.out.println("Calling bt server");
        btListener.setServiceDescription(newServiceDescription);
    
    }
    
    public void newBluetoothDelay(int btDelay){
        
        btListener.setDelay(btDelay);
    
    }
    
    public void newVerifyCycles(int newVc){
     
        this.btVCycles = newVc;
        
    }
    
    // OSC
    public void newPort(String newPort){
    
        oscListener.setTargetPort(newPort);
        
    }
    
    public void newTargetAddress(String newTargetAddress){
    
        oscListener.setTargetAddress(newTargetAddress);
        
    }
    
    // Save/Load/Apply Settings
    public void loadSettings(){
    
         // TODO
        
    }
    
    public void applySettings(){
        
        // TODO
        
    }
    
    public void saveSettings(){
        
        // TODO
        
    }
    
    // Log
    public void newLogDetail(int newLogDetail){
        
        this.logDetail = newLogDetail;
        // Since the view automatic changes the log detail, there's no need to notify it'
        
    }
    
    public void clearLog(){
    
        serverView.clearLog();
        // TODO: internal model log, internal max and external max should be established
        
    }
    
    public void saveLog(){
        
        // TODO
    
    }
    
    // Exits the application   
    public void exit() {
    
        // Save Log ?
        // TODO:in next version:Automatic log saving
        System.exit(0);
        
    }
    
    public void showCredits() {
        
        // TODO
        
    }    
    /**
     *  Model Data Methods
     *  These methods are related to the manipulation set/get of the internal model data
     *
     *
     *
     */
   private void setResourceBundle(ResourceBundle newDiABluBundle){
       
       this.diabluBundle = newDiABluBundle;

       
   }   
    
    /**
     *  Internal Model Methods
     *  These private methods are necessary for this class processing only
     *
     */
    
   /**
     * This method registers a view "DiABluServerModelListener" into the generalListeners vector 
     * so that it get's notified of all this view's methods
     *
     */
    private void registerGeneralListener(final DiABluServerModelListener dModelListener){
        
        // paranoid
        if (dModelListener == null){
            
            log(LOG_DEBUG,"[Model - registerGeneralListener()] "+"Null argument");
            return;
            
        }
                
        // work
        if ( generalListeners!=null && !generalListeners.contains(dModelListener) ){
            generalListeners.add(dModelListener);
            initializeGeneralListener(dModelListener);
        }
        
    }
    
    /**
     * This method initializes de values of the general ServerModelListeners registered with this model
     *
     *
     *
     */
    private void initializeGeneralListener (DiABluServerModelListener dModelListener){
        
        // paranoid
        if (dModelListener == null){
            
            log(LOG_DEBUG,"[Model - initializeGeneralListener()] "+"Null argument");
            return;
            
        }
        
        // i18n l9n
        dModelListener.setResourceBundle(this.diabluBundle);
        
        // output
        dModelListener.setTargetAddress(this.targetAddress);
        dModelListener.setTargetPort(this.targetPort);
        
    }
  
   private int getDeviceIndex(DiABluID did){
       
       int index = -1;
       if (did == null){
           
           log(LOG_DEBUG,"[Model - getDeviceIndex()] "+"Null argument");
           
       } else {
        
            DiABluDevice dd = new DiABluDevice();
            
            for (int i = 0; i < currentDiABluDevices.size(); i++ ){  
           
                dd = currentDiABluDevices.elementAt(i);
            
                //if (dd.getID().getUUID().equalsIgnoreCase(did.getUUID())){
                if (dd.getID().equals(did)) { 
                    index = i;
                    break;
                }                                                           
         
            }
            
         }
              
        return index;
  
    }
   
   /**
    *  INITIALIZE VIEWS
    *
    *
    */
   private void initializeServerView(DiABluServerViewModelListener sV){
     
       // paranoid
       if (sV == null) {
           
           log(LOG_DEBUG,"[Model - initializeServerView()] "+"Null argument");
           return;
       }
       
       // Protocol Bluetooth     
       sV.setServiceName(this.serviceName);
    
       sV.setServiceDescription(this.serviceDescription);
    
       sV.setBluetoothDelay(this.btDelay);
    
       sV.setCountry(this.country);
    
       sV.setLanguage(this.language);
    
       // Log     
       sV.setLogDetail(this.logDetail);
        
       // Simulator    
       sV.setSimulatorAuto(this.isSimulatorAuto);
    
   }
   
   private void initializeBTListener(DiABluServerBTModelListener bL) {
       
       
       // paranoid
       if (bL == null) {
           
           log(LOG_DEBUG,"[Model - initializeBTListener()] "+"Null argument");
           return;
       }
       
       bL.setDelay(this.btDelay);
       
       bL.setServiceDescription(this.serviceDescription);
       
       bL.setServiceName(this.serviceName);
       
   }
   
   private void initializeGUI(){
       
       serverView = new DiABluServerView(this);
       
       if (serverView == null) {
           System.out.println("ERROR: Null server view1");
       }
       
   }
   
   /** 
    *   Vector simplify methods
    *
    *
    *
    */
   
   private Vector <DiABluDevice> removeBlackListed(Vector <DiABluDevice> rawDeviceList){
       
       Vector <DiABluDevice> filteredDeviceList = new Vector <DiABluDevice> ();
       
       // paranoid
       if (rawDeviceList == null){
           
           log(LOG_DEBUG,"[Model - removeBlackListed()] "+"Null argument");
           return filteredDeviceList;
           
       }
       
       if ( rawDeviceList.isEmpty() ){
           
           log(LOG_DEBUG,"[Model - removeBlackListed()] "+"Empty argument");
           return filteredDeviceList;
           
       }
       
       log(LOG_DEBUG,"[Model - removeBlackListed()] "+"Checking "+rawDeviceList.size()+" elements");
       /**
        * first check if we have any black listed devices
        * them check the list for the special ones
        *
        */
       if (!this.blackList.isEmpty()) {
             
            DiABluDevice tempDD = new DiABluDevice();
            for (DiABluID did:blackList){
                
                //for(DiABluDevice dd:rawDeviceList){
                for ( Iterator iDD = rawDeviceList.iterator(); iDD.hasNext(); ){
                    
                    // get our comparable id
                    tempDD = (DiABluDevice) iDD.next();                
                    DiABluID unCheckedID = tempDD.getID();
                    
                    if (did.equals(unCheckedID)) {
                        
                        // rawDeviceList.removeElement(dd);
                        iDD.remove();
                        log(LOG_DEBUG,"[Model - removeBlackListed()] "+"Removing "+unCheckedID.toString());
                        break;
                        
                    }
                }
   
            }            
            filteredDeviceList = rawDeviceList;
            
       }
       
       
       log(LOG_DEBUG,"[Model - removeBlackListed()] "+"Returning "+filteredDeviceList.size()+" elements");
       return filteredDeviceList;
               
   }
   
   private Vector <DiABluDevice> cropDeviceList(int status,Vector <DiABluDevice> rawDeviceList){
        
       Vector <DiABluDevice> croppedDeviceList = new Vector <DiABluDevice> ();
       
       // paranoid
       if (rawDeviceList == null || rawDeviceList.isEmpty()){
           
           log(LOG_DEBUG,"[Model-cropDeviceList()] "+"Null or Empty argument received");
           
       } else {
       
            log(LOG_DEBUG,"[Model-cropDeviceList()] "+"Checking "+rawDeviceList.size()+" elements");
       
            for (DiABluDevice dd:rawDeviceList){
           
                // match
                if (dd.getStatus()==status){
               
                    croppedDeviceList.addElement(dd);
               
                }
           
            }
       }
       
       return croppedDeviceList;
   }
   
   /**
    * This method strips the argument vector of devices with the given status
    * Note:Currently we only use DEVICE_BLUETOOTH but this method doesn't knows that
    * Returns the vector cleaned of devices with the provided status
    *
    */
   private Vector <DiABluDevice> stripDeviceList(int status,Vector <DiABluDevice> rawDeviceList){
       
           
       Vector <DiABluDevice> strippedDeviceList = new Vector <DiABluDevice> ();
       
       // paranoid
       if (rawDeviceList == null || rawDeviceList.isEmpty()){
           
           log(LOG_UNEXPECTED_ERROR,"[Model-stripDeviceList()] "+"Null or Empty argument received");
           
       } else {
       
            log(LOG_DEBUG,"[Model-stripDeviceList()] "+"Checking "+rawDeviceList.size()+" elements");
       
            for (DiABluDevice dd:rawDeviceList){
           
                // match
                if (dd.getStatus()!=status){
               
                    strippedDeviceList.addElement(dd);
               
                }
           
            }
       }
       
       return strippedDeviceList;
   }
   
   /**
    * This method cleans the verifyDiABluDevices vector from
    * all the DiABlu Devices in the argument vector [cleanDeviceList]
    * Usefull to make sure we remove new devices that may have been in 
    * verify list
    *
    */
   private void cleanVerifyList(Vector <DiABluDevice> cleanDeviceList) {
       
       // usual paranoia
       if (cleanDeviceList==null){
           
           log(LOG_UNEXPECTED_ERROR,"[Model-cleanVerifyList()] "+"Null argument.");
           return;
       }
       
       if (cleanDeviceList.isEmpty()){

           log(LOG_DEBUG,"[Model-cleanVerifyList()] "+"Empty argument.");
           return;
           
       }
       
       // are you sure there is any work to do ? :)
       if (verifyDiABluDevices.isEmpty()) return;
       
       // there's work to do :P
       DiABluDevice tempDD = new DiABluDevice();
       for (DiABluDevice cleanD:cleanDeviceList){
           
           // for (DiABluDevice verifyD:verifyDiABluDevices) {
           for ( Iterator iDD = verifyDiABluDevices.iterator(); iDD.hasNext(); ) {
               
               tempDD = (DiABluDevice) iDD.next();
               // found a match
               if (tempDD.equals(cleanD)){
                   //verifyDiABluDevices.remove(cleanD);
                   iDD.remove();
               }
           }
       }
       
   }
   /**
    * This method removes a given DiABlu Device from
    * the verify list
    *
    */
   private void cleanVerifyListElement(DiABluDevice cleanDevice){
       
       if (cleanDevice == null){
           
           log(LOG_DEBUG,"[Model - cleanVerifyListElement()] "+" Null argument");
           return;
           
       }
       
       if (verifyDiABluDevices.isEmpty()) { return; }
       
       DiABluDevice verifyDD = new DiABluDevice(); 
       //for (DiABluDevice verifyDD:verifyDiABluDevices){
       for ( Iterator iDD = verifyDiABluDevices.iterator(); iDD.hasNext(); ){
           
           verifyDD = (DiABluDevice) iDD.next();
           if (verifyDD.equals(cleanDevice)) { 
           
               //verifyDiABluDevices.remove(verifyDD); 
               iDD.remove();
               
           }
           
       }
              
   }
   
   /**
    *  This method removes black listed devices from
    * the argument list
    *
    */
   private Vector <DiABluDevice> filterBlackListed(Vector <DiABluDevice> rawDeviceList){
       
       Vector <DiABluDevice> filteredDeviceList = new Vector <DiABluDevice> ();
       
       // paranoid check
       if ( rawDeviceList == null ) {
           
           log(LOG_UNEXPECTED_ERROR,"[Model - filterBlackListed()] "+"Null argument");
           return filteredDeviceList;
           
       }
       
       if ( rawDeviceList.isEmpty()) {
           
           log(LOG_DEBUG,"[Model - filterBlackListed()] "+"Empty argument");
           return filteredDeviceList;
           
       }
       
       // everything ok
       filteredDeviceList = rawDeviceList;    
       
       if (!blackList.isEmpty()) {
           
           DiABluDevice tempDD = new DiABluDevice();
           //for (DiABluDevice dd:filteredDeviceList){
           for ( Iterator iDD = filteredDeviceList.iterator(); iDD.hasNext(); ) {
               
                tempDD = (DiABluDevice) iDD.next();           
                if (this.blackList.contains(tempDD.getID())) {
                    
                    //filteredDeviceList.remove(dd);
                    iDD.remove();
                    
                }
           }
           
           log(LOG_DEBUG,"[Model - filterBlackListed()] "+"Filtered list with "+filteredDeviceList.size()+" elements");
                  
       } else {
           
           log(LOG_DEBUG,"[Model - filterBlackListed()] "+"Black List is empty.Nothing to do here.");
           
       } 
              
       return filteredDeviceList;
   }
   
      
   /**
    *
    * This method searches/returns the current device list for 
    * an element with the given id.
    * Returns an empty device if didn't find none
    *
    */
   private DiABluDevice getDiABluDevice(DiABluID did){
       
       DiABluDevice foundDiABlu = new DiABluDevice();
       
       for (DiABluDevice dd:currentDiABluDevices){
           
           log(LOG_DEBUG,"[Model - getDiABluDevice()] "+"Inspecting:"+dd.toString()+"@"+dd.getID().getUUID());
           if (did.getUUID().equalsIgnoreCase(dd.getID().getUUID())){
                              
               foundDiABlu = dd;
               log(LOG_DEBUG,"[Model - getDiABluDevice()] "+"Found device:"+foundDiABlu.toString());
               
           }
           
       }
       
       return foundDiABlu;
   }
}
