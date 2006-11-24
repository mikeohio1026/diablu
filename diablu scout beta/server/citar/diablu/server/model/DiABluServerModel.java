/*
 * DiABluServerModel.java
 * NB5.5
 * Created on 9 août 2006, 17:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model;

// j2se
import java.util.Vector;
import java.util.Iterator;

// Logging API
import com.sun.corba.se.impl.orbutil.LogKeywords;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;

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

// flosc
import citar.diablu.server.model.com.flosc.DiABluServerFlosc;

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
    private DiABluServerBTDeviceDiscovery diABluDiscovery;
    private boolean BT_SERVER_LISTENER_READY = false;
    private boolean BT_DISCOVERY_LISTENER_READY = false;
    private boolean filterFriendlyNames = false;
    private boolean fastMode = false;
    private String serviceName = BT_DEFAULT_SERVICE_NAME;
    private String serviceDescription = BT_DEFAULT_SERVICE_DESCRIPTION;
    private int btDelay = BT_DEFAULT_DELAY;
    private int btVCyclesIN = BT_DEFAULT_VCYCLES_IN;
    private int btVCyclesOUT = BT_DEFAULT_VCYCLES_OUT;
    private boolean autoStartDiscovery = false;
    private boolean autoStartService = false;
    private boolean isDiscoveryRunning = false;
    private boolean isServiceRunning = false;
    
    // Simulator Listener
    private DiABluSimulatorModelListener simListener;
    private boolean SIMULATOR_LISTENER_READY = false;
    private boolean isSimulatorAuto;
    private boolean isSimulatorRunning=false;
    
    // OSC Listener
    private DiABluServerOSCModelListener oscListener;
    private boolean OSC_LISTENER_READY = false;
    private String protocol = PROTOCOL_DEFAULT;
    private String targetAddress;
    private String targetPort;
    
    // Flosc Listener
    private DiABluServerOSCModelListener dsft;
    
    // Main View
    private DiABluServerViewModelListener serverView;
    private boolean SERVER_VIEW_LISTENER_READY = false;
    private String preferredView = VIEW_DEFAULT;
    
    // Simulator View
    /*
     * Need because the model may hide/unhide it
     */
    private DiABluSimulatorView simView;
    
    // Model Data
    private String country;
    private String language;
    private String bundlePath;
    private ResourceBundle diabluBundle;
    
    // Log
    private int logDetail;   
    private static Logger logger = Logger.getLogger(LOG_MAIN_NAME);            
    private Handler fh;
    private ConsoleHandler ch;        
    
    // Device List
    private Vector <DiABluDevice> currentDiABluDevices;
    
    // Device's Black List
    private Vector <String> blackList = new Vector <String> ();
    
    // DiABluID's Cache
    private Vector <DiABluID> diabluCache = new Vector <DiABluID> ();
    
    // Verify Cycles Device List
    private Vector <DiABluDevice> verifyDiABluDevicesOUT;
    private Vector <DiABluDevice> verifyDiABluDevicesIN;
    
    /** Creates a new instance of DiABluServerModel */
    public DiABluServerModel(String args[]) {
        

        try {
            
            // Log file handler
            fh = new FileHandler(DEFAULT_LOG_FILE);
            fh.setLevel(Level.ALL);
            
            // Log console handler
            ch = new ConsoleHandler();
            ch.setLevel(Level.ALL);
            
            // Add the handlers to our log
            logger.addHandler(fh);
            logger.addHandler(ch);
            
        } catch (Exception e){
            
            logger.warning("Couldn't open log file!");
            e.printStackTrace();
            
        }
        logger.setLevel(Level.ALL);
        
        // general view's'
        generalListeners = new Vector <DiABluServerModelListener>();
        
        // device's data
        currentDiABluDevices = new Vector <DiABluDevice> ();
        blackList = new Vector <String> ();
        verifyDiABluDevicesOUT = new Vector <DiABluDevice> ();
        verifyDiABluDevicesIN = new Vector <DiABluDevice> ();
        
        // get saved/command line settings
        settings = new DiABluServerSettings(args,this);
        settings.updateModel();
        startDiABluSystem();
        
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
                
            });
            SERVER_VIEW_LISTENER_READY = true;
        
        } catch (Exception e){
                
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
        this.oscListener = new DiABluServerOSC();
        registerGeneralListener(oscListener);
        OSC_LISTENER_READY = true;
        
        // Flosc Listener
                
        // start the diablu flosc
        // dsft = new DiABluServerFlosc(this.targetAddress,Integer.parseInt(this.targetPort));
        
        
        
        // Simulator
        /*
         * Need because the model may hide/unhide it
         * TODO:Thread the simulator
         *
         */
         System.out.println("Simulator");
            this.simView = new DiABluSimulatorView(this);
            this.simListener = simView;
        if (this.isSimulatorAuto) {
                
            simView.setVisible(true);
            SIMULATOR_LISTENER_READY = true;
            serverView.simulatorIsRunning(true);            
        }
        
        // BT
        System.out.println("Starting BT...");
        this.btListener = new DiABluServerBT(this);
        initializeBTListener(btListener);
        BT_SERVER_LISTENER_READY = true;
        btListener.startSystem();
        this.isDiscoveryRunning = true;
        
        logger.fine("Is BT Server ready ?"+BT_SERVER_LISTENER_READY);
        serverView.setServiceStatus(BT_SERVER_LISTENER_READY);

        try {
            
            // now the discovery
            diABluDiscovery = new DiABluServerBTDeviceDiscovery(this,this);
            BT_DISCOVERY_LISTENER_READY = true;
            // set visible the serverView
            serverView.clearLog();
            serverView.setVisibleView(true);
            logger.info("Starting Device Discovery...");
            diABluDiscovery.run();
            this.isDiscoveryRunning = true;            
            logger.fine("Is BT Discovery ready ?"+BT_DISCOVERY_LISTENER_READY);
            serverView.setDiscoveryStatus(BT_DISCOVERY_LISTENER_READY);
            logger.finest("[DSBT|startSystem()] "+"Started_device_discovery");
            
        } catch (BluetoothStateException bte1){
            
            serverView.clearLog();
            serverView.setVisibleView(true);
            SERVER_VIEW_LISTENER_READY = true;
            logger.warning("WARNING!!Bluetooth device not reachable!!!!");
            logger.info("Check your bluetooth hardware and restart DiABlu System");
            logger.info("SIMULATION ONLY MODE");
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
     *  @deprecated
     *
    @Deprecated
    public void log(int priority, String logMsg){
        
        logger.warning("Using deprecated log:"+logMsg);
        // TODO:process & filter messages accordanly with priority
        // System.out.println("MODEL["+priority+"]"+logMsg);
        if (serverView!=null)serverView.log(priority, logMsg);
        
    }
    */
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
    public void newMsg(DiABluMsg newMsg){
        
        serverView.newMsg(newMsg);
        oscListener.newMsg(newMsg);
        
    }
    
    // Arrival of a new DiABlu Key
    public void newKey(DiABluKey newKey){
        
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
    public void newDeviceList(int deviceType, Vector <DiABluDevice> rawNewDeviceList){
        
        // MAKE SURE OF NEW CHANGES
        Vector <DiABluDevice> finalDeviceList = new Vector <DiABluDevice> ();   // The final list with all the devices
        Vector <DiABluDevice> namesChangedList = new Vector <DiABluDevice> ();  // The list with the devices that have changed their friendly name
        Vector <DiABluDevice> devicesOutList = new Vector <DiABluDevice> ();    // The list with the exited devices
        Vector <DiABluDevice> devicesInList = new Vector <DiABluDevice> ();     // The list with the newly discovered devices
        Vector <DiABluDevice> oldDeviceList = new Vector <DiABluDevice> ();     // Our last device list of your specific type [need to add multiple input protocol]
        Vector <DiABluDevice> entireOldDeviceList = new Vector <DiABluDevice> (); // Our complete last device list
        Vector <DiABluDevice> removedDevicesList = new Vector <DiABluDevice> (); // Definitelly removed device list
        Vector <DiABluDevice> newverifyDiABluDevicesOUTList = new Vector <DiABluDevice> (); // Updated OUT verify cycles list
        Vector <DiABluDevice> newverifyDiABluDevicesINList = new Vector <DiABluDevice> ();
        Vector <DiABluDevice> newDeviceList = new Vector <DiABluDevice> ();
        
        int oldCount = currentDiABluDevices.size();                             // needed to check on device count changes
        int newCount = 0;                                                       // final size of finalDeviceList
        boolean newDevice = false;                                              // flag that helps telling it's a new device
        boolean verifiedDevice = false;                                         // flag that tells if the device has been verified
        int complexCompare;                                                     // comparation factor between two diablu devices
        boolean notifyUpdatedDeviceList = false;                                // flag that if we need to output a new device list
        DiABluDevice oldDD = new DiABluDevice();                                // temporary single DiABluDevice buffer.used to compare list elements
        DiABluDevice removedDD = new DiABluDevice();                            //  " " " "
        DiABluDevice verifyDD = new DiABluDevice();                             // " " " "
        
        
        // the usual paranoia
        if (rawNewDeviceList == null) {
            
            logger.warning("Null argument!");
            logger.finer("Replacing null argument for an empty list");
            // repair this error
            // create empty list and continue
            newDeviceList = new Vector <DiABluDevice> ();
            
        } else {
            
            // Cache
            logger.info("Received new device list with "+rawNewDeviceList.size()+" devices(1)");
            
            if (this.filterFriendlyNames){
            
                logger.finest("Cache processing the list");
                newDeviceList = cacheProcessDeviceList(rawNewDeviceList);
            
            } else {
                
                newDeviceList = rawNewDeviceList;
                logger.finest("Processing "+newDeviceList.size()+"elements");
            }
            
        }
        
        /**
         * DEBUG ONLY
         * Remove comments if you wish to check your newly arrived list
         *
         * for (DiABluDevice dd:newDeviceList){
         *
         * logger.finest("UUID:"+dd.getID().getUUID());
         * logger.finest("Fname:"+dd.getID().getFName());
         * logger.finest("MajorDeviceClass:"+dd.getMajorDeviceClass());
         * logger.finest("StringDeviceClass:"+dd.getStringDevice());
         *
         * }
         */
        
        // Do we hold devices ?
        if (!currentDiABluDevices.isEmpty()){
            
            logger.finest("Current devices:"+currentDiABluDevices.size());
            
            // get & filter the current data
            entireOldDeviceList = this.currentDiABluDevices;
            logger.finest("Copied "+entireOldDeviceList.size()+" devices to a temporary list");
            
            // get all of this device's type
            oldDeviceList = cropDeviceList(deviceType,entireOldDeviceList);
            logger.finest("There are "+oldDeviceList.size()+" comparable devices of the corresponding device type");
            
            
            // add all other device types into the final list
            finalDeviceList = stripDeviceList(deviceType,entireOldDeviceList);
            logger.finest("Added "+finalDeviceList.size()+" other devices to the temporary final list");
            
        }
        
        /**
         * 0 Special Cases first
         * If we get, or already have an empty list we
         * can shortcut the processing
         */
        // search came in empty pockets
        if (newDeviceList.isEmpty()){
            
            logger.finest("[Model-newDeviceList()] "+"Empty new device list.");
            // nothing to do here
            
            
        } else {
            
            // we already have some devices in dah house :)
            //check if we have any of these devices
            if (oldDeviceList.isEmpty()){
                
                logger.finest("[Model-newDeviceList()] "+"Old device list empty.Adding entire new device list.");
                
                for (DiABluDevice dd:newDeviceList){
                    
                    devicesInList.addElement(dd);
                    finalDeviceList.addElement(dd);
                    
                }
                
            } else {
                
                logger.finest("[Model-newDeviceList()] "+"Comparing both old and new lists...");
                for (DiABluDevice newDD:newDeviceList){
                    
                    // reset the newDevice flag
                    newDevice = true;
                    
                    
                    for ( Iterator <DiABluDevice> oldI = oldDeviceList.iterator(); oldI.hasNext(); ){
                        
                        //
                        
                        // get our "old" device
                        try {
                            
                            oldDD = oldI.next();
                            
                        } catch (Exception e){
                            
                            logger.warning("[Model-newDeviceList()] "+"Cast Exception converting Object to DiABlu Device");
                            logger.finest("[Model-newDeviceList()] "+ e.getLocalizedMessage());
                            e.printStackTrace();
                            break;
                            
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
                                    
                                    logger.warning("[Model - newDeviceList()] "+"Exception while trying to remove device.");
                                    logger.warning("[Model - newDeviceList()] "+"Message:"+e.getLocalizedMessage());
                                    e.printStackTrace();
                                    
                                }
                                
                                // it isn't a new device
                                newDevice = false;
                                
                                // detailed log
                                logger.finest("Device already in the system.Adding to final list:"+newDD.toString());
                                break;
                                
                            }
                            
                            // diferent fname
                            case 1: {
                                
                                // add it to the changed names list
                                namesChangedList.addElement(newDD);
                                
                                // add it to the final list
                                finalDeviceList.addElement(newDD);
                                
                                // remove it from the old so that the next devices don't need to compare to this one'
                                //oldDeviceList.remove(oldDD); DEPRECATED
                                oldI.remove();
                                
                                // it isn't a new device
                                newDevice = false;
                                
                                // detailed log
                                logger.finest("Device:"+oldDD.toString()+" has changed it's name to:"+newDD.toString());
                                break;
                            }
                            // no match
                            default: {
                                
                                logger.finest("No match between:"+oldDD.toString()+" & "+ newDD.toString());
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
                        logger.finest("Found new device:"+newDD.toString());
                        
                    }
                    
                    
                }
                
            }
        }
        removedDevicesList = oldDeviceList;
        /**
         * DEBUG
         */
        logger.finest("Finished processing list.Analysing results");
        logger.finest("--------------------------------------------");
        logger.finest("Add device list:"+devicesInList.size());
        logger.finest("Remove device list:"+removedDevicesList.size());
        logger.finest("Changed names:"+namesChangedList.size());
        logger.finest("Final device list:"+finalDeviceList.size());
        logger.finest("--------------------------------------------");
        
        
        // Verify cycles check
        
        //OUT
        // DEBUG
        // btVCyclesOUT = 0;
        
        // first make sure we have something to do
        if (btVCyclesOUT > 0){
            
            // let's check the list
            if (verifyDiABluDevicesOUT.isEmpty()){
                
                logger.finest("[Model-newBTDeviceList()] "+"Found empty verify list.Filling "+oldDeviceList.size()+" elements");
                // since the list is empty we only have to add the removed devices
                for (int i = 0;i<oldDeviceList.size();i++){
                    
                    // get the device
                    removedDD = oldDeviceList.elementAt(i);
                    
                    // update the verify counter
                    removedDD.incrementDetectionCounter();
                    
                    // add it to the list
                    verifyDiABluDevicesOUT.addElement(removedDD);
                    
                }
                
            } else {
                
                // search & compare lists
                for ( int i = 0; i < oldDeviceList.size(); i++ ){
                    
                    // get the old device
                    removedDD = oldDeviceList.elementAt(i);
                    
                    for ( int j = 0; j < verifyDiABluDevicesOUT.size(); j++ )   {
                        
                        verifyDD = verifyDiABluDevicesOUT.elementAt(j);
                        
                        if (removedDD.compareTo(verifyDD)==0){
                            
                            // found a match.let's check if it's still under vCycle'
                            if (verifyDD.getDetectionCounter() > this.btVCyclesOUT) {
                                
                                logger.finest("[Model-newBTDeviceList()] "+"Tottally removing:"+verifyDD.toString());
                                // it's time to get out
                                removedDevicesList.addElement(verifyDD);
                                // clean the verify list
                                verifyDiABluDevicesOUT.remove(verifyDD);
                                
                                
                            } else {
                                
                                // device is still under vCycle limit
                                // update it's counter
                                // TODO:make sure this updates the verifyDiABluDevicesOUT vector
                                logger.finest("[Model-newBTDeviceList()] "+"Incrementing verify counter on:"+verifyDD.toString());
                                verifyDD.incrementDetectionCounter();
                                
                            }
                            
                            // check the flag
                            verifiedDevice = true;
                        }
                        
                    }
                    // let's check if this device has already been verified
                    
                    if (!verifiedDevice){
                        
                        logger.finest("[Model-newBTDeviceList()] "+"Adding "+removedDD.toString()+" to verify table");
                        // first time removed
                        removedDD.incrementDetectionCounter();
                        // give it another chance ;)
                        verifyDiABluDevicesOUT.addElement(removedDD);
                        finalDeviceList.addElement(removedDD);
                        
                    }
                    
                }
                
            }
            
        }
        
        
        // IN        
        
        // make sure we've got work to do'
        if (btVCyclesIN > 0){
 
           newverifyDiABluDevicesINList = processVCyclesIN(devicesInList);
           devicesInList = newverifyDiABluDevicesINList;
        }
        
        
        
        
        // End of verify
        
        
        /**
         * DEBUG
         */
        logger.finest("Finished verifying lists.Analysing results2");
        logger.finest("--------------------------------------------");
        logger.finest("Add device list:"+devicesInList.size());
        logger.finest("Remove device list:"+removedDevicesList.size());
        logger.finest("Changed names:"+namesChangedList.size());
        logger.finest("Final device list:"+finalDeviceList.size());
        logger.finest("--------------------------------------------");
        
        // Process the results
        
        logger.finest("[Model-newDeviceList()] "+"Copying devices into memory:"+finalDeviceList.size());
        currentDiABluDevices.removeAllElements();
        
        if (!finalDeviceList.isEmpty()){
            
            
            for (DiABluDevice dd:finalDeviceList){
                
                // copy the final devices into memory
                logger.finest("[Model-newDeviceList()] "+"Copying device:"+dd.toString());
                currentDiABluDevices.addElement(dd);
                
                // DEBUG:check
                DiABluDevice ddCheck = currentDiABluDevices.lastElement();
                logger.finest("[Model-newDeviceList()] "+"Checked added element "+ddCheck.toString()+"|"+ddCheck.getStringDevice());
                
                
            }
            
            
        }
        currentDiABluDevices = finalDeviceList;
        logger.finest("CURRENT DIABLUS:"+currentDiABluDevices.size());
        
        // devicesout
        if (!removedDevicesList.isEmpty()) {
            
            
            logger.finest("[Model-newDeviceList()] "+"Removing "+removedDevicesList.size()+" devices");
            // TODO:update this to update multiple general views listeners
            if ( SERVER_VIEW_LISTENER_READY ) serverView.removeDiABluDevices(removedDevicesList);
            // Make sure we don't send black listed devices' or unset fnames
            Vector <DiABluDevice> oscRemovedList = new Vector <DiABluDevice> ();
            
            if (filterFriendlyNames){
                     
                oscRemovedList = filterFNames(removedDevicesList);
                
            } else {
                
                oscRemovedList = removedDevicesList;
                
            }
                
            if ( OSC_LISTENER_READY ) oscListener.removeDiABluDevices(filterBlackListed(removedDevicesList));
            
            notifyUpdatedDeviceList = true;
            
            
        }
        
        // devicesin
        if (!devicesInList.isEmpty()){
            
            logger.finest("[Model-newDeviceList()] "+"Adding "+devicesInList.size()+" devices");
            // TODO:update this to update multiple general views listeners
            serverView.newDiABluDevices(filterBlackListed(devicesInList));
            serverView.editDiABluDevices(getBlackListed(devicesInList));
            
            Vector <DiABluDevice> diabluOSCin = new Vector <DiABluDevice> ();                                                
            if (filterFriendlyNames){
                
                diabluOSCin = filterFNames(devicesInList);
                
            } else {
                            
                diabluOSCin = devicesInList;
                
            }
                
            if ( OSC_LISTENER_READY ) oscListener.newDiABluDevices(filterBlackListed(diabluOSCin));
            
            notifyUpdatedDeviceList = true;
        }
        
        // nameschanged
        if (!namesChangedList.isEmpty()){
            
            logger.finest("[Model-newDeviceList()] "+namesChangedList.size()+" devices have changed friendly name");
            serverView.editDiABluDevices(namesChangedList);
            if ( OSC_LISTENER_READY ) oscListener.editDiABluDevices(filterBlackListed(filterFNames(namesChangedList)));
            
            notifyUpdatedDeviceList = true;
            
        }
        
        // devicelist
        if (notifyUpdatedDeviceList && !finalDeviceList.isEmpty()) {
            
            logger.finest("[Model-newDeviceList()] "+"Sending the new device list("+finalDeviceList.size()+")");
            
            Vector <DiABluDevice> diabluOSClist= new Vector <DiABluDevice> ();                                            
            if (filterFriendlyNames){
                
                diabluOSClist = filterFNames(finalDeviceList);
                
            } else {
                            
                diabluOSClist = finalDeviceList;
                
            }
            
            if ( OSC_LISTENER_READY ) oscListener.newDeviceList(filterBlackListed(finalDeviceList));
            
        }
        
        // devicecount
        
        if (filterFriendlyNames){
            
            newCount = filterBlackListed(filterFNames(finalDeviceList)).size();
            
        } else {
            
            newCount = filterBlackListed(finalDeviceList).size();
            
        }
        
        if ( oldCount != newCount ) {
            
            logger.finest("[Model-newDeviceList()] "+"Sending new device count:"+newCount);
            if ( OSC_LISTENER_READY ) oscListener.newDeviceCount(newCount);
            
        }
        
        // update our own data
        
        // Paranoid filter
              /*
              for (DiABluDevice oDD : currentDiABluDevices){
               
               
                  if (!finalDeviceList.contains(oDD)) {
                      logger.finest("Removing element...");
                      currentDiABluDevices.remove(oDD);
                  }
               
              }
              for (DiABluDevice nDD: finalDeviceList){
               
                  if (!currentDiABluDevices.contains(nDD)){
                      logger.finest("");
                      currentDiABluDevices.add(nDD);
                  }
               
              }
               */
        
        
        
        
        /**
         * DEBUG
         */
        logger.finest("FINISHED NEW DEVICE LIST.Analysing results");
        logger.finest("--------------------------------------------");
        logger.finest("Add device list:"+devicesInList.size());
        logger.finest("Remove device list:"+removedDevicesList.size());
        logger.finest("Changed names:"+namesChangedList.size());
        logger.finest("Final device list:"+finalDeviceList.size());
        logger.finest("--------------------------------------------");
    }
    
    /*
     * DiABluSimulatorControllerListener
     * ---------------------------------
     * These methods are called by the Simulator Controller
     *
     */
    
    // New Simulated Device
    public void newSimDiABluDevice(DiABluDevice addDiABlu){
        
        // first check if it already is in the system
        for (DiABluDevice ddT:currentDiABluDevices){
            
        
            if (ddT.compareTo(addDiABlu)==0){
                
                logger.warning("Device:"+addDiABlu.getID().toString() +"already in the system");
                return;
            }
        
        }
        
        logger.finest("FAST MODE device in:"+addDiABlu.getID().toString());
        
       
            
            // update model data
            currentDiABluDevices.add(addDiABlu);
            
            // update related views
            
            // server view
            Vector <DiABluDevice> tempDBVector = new Vector <DiABluDevice> ();
            tempDBVector.addElement(addDiABlu);
            
            if (SERVER_VIEW_LISTENER_READY){
            
                serverView.removeDiABluDevices(tempDBVector);
                serverView.newDiABluDevices(tempDBVector);
            
            }
            // check for black list elements
            if (!isBlackListed(addDiABlu)){
            
                // osc View
                if ( OSC_LISTENER_READY ) oscListener.newDiABluDevices(tempDBVector);
                if ( OSC_LISTENER_READY ) oscListener.newDeviceList(currentDiABluDevices);
                if ( OSC_LISTENER_READY ) oscListener.newDeviceCount(currentDiABluDevices.size());
                
            }
        
        
    }
    
    // Edit Simulated Device
    public void editSimDiABluDevice(DiABluDevice editDiABlu){
        
        // paranoid
        if ( editDiABlu == null ){
            
            logger.finest("[Model -editSimDiABluDevice] "+"Null argument");
            return;
            
        }
        
        DiABluDevice dd = new DiABluDevice();
        for ( Iterator <DiABluDevice> i = currentDiABluDevices.iterator(); i.hasNext(); ){
            
            
            dd = i.next();
            
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
    public void removeSimDiABluDevice(DiABluDevice removeDiABlu){
        
        if (removeDiABlu == null){
            
            logger.warning("Null argument");
            return;
            
        }
        
        DiABluDevice rdd = new DiABluDevice();        
        for (Iterator <DiABluDevice> itDD = currentDiABluDevices.iterator();itDD.hasNext();) {
            
            rdd = itDD.next();
            if (rdd.compareTo(removeDiABlu)==0){
            
                    // update the model data
                    itDD.remove();
            
                    // update related views
                    Vector <DiABluDevice> tempDBVector = new Vector <DiABluDevice> ();
                    tempDBVector.addElement(removeDiABlu);
            
                    // server View
                    serverView.removeDiABluDevices(tempDBVector);
            
                    // osc View
                    oscListener.removeDiABluDevices(tempDBVector);
            }
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
            
            logger.warning("[Model - newSelectedDevice()] "+"Null argument");
            return;
            
        }
        logger.finest("Checking for "+did.toString());
        
        // extract the uuid
        String uuidS = did.getUUID();
        
        // locate the device index
        int selectedIndex = getDeviceIndex(did);
        logger.finest("[Model - newSelectedDevice()] "+"Device located @"+selectedIndex);
        
        
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
    public void addToBlackList(String did){
        
        if (did == null) {
            
            logger.warning("Null argument");
            return;
        
        }
        
        for(String bl:this.blackList){
            
            if (bl.equalsIgnoreCase(did)){
                
                logger.config("Device already black listed!");
                return;
                
            }
        }
        
            
            logger.finest("Blacklisting:"+did);
            blackList.add(did);
            
        
        
        DiABluDevice oldDD = getDiABluDevice(did);
        if (oldDD.getID().getUUID().equalsIgnoreCase("")) {
            
            // device not found
            logger.warning("Black listed device not found:"+did.toString());            
            return;
            
        }
        
        // found device
        // update view
        Vector <DiABluDevice> updateView = new Vector <DiABluDevice> ();
        //updateView.add(oldDD);
        //serverView.removeDiABluDevices(updateView);
        //updateView.removeAllElements();
        oldDD.setStatus(DEVICE_STATUS_BLACKLISTED); // blacklisted status
        updateView.add(oldDD);
        serverView.editDiABluDevices(updateView);
        
    }
    
    /**
     * This method removes from the black list the argument uuid
     * and updates the server view to reflect that
     *
     */
    public void removeFromBlackList(String did){
        
        
        if (did == null || did.equalsIgnoreCase("")) {
            
            logger.finest("[Model - removeFromBlackList()] "+"Null argument");
            return;
        }
        
        
        if (isBlackListed(did)){
            
            // remove it from the black list
            logger.finest("[Model - removeFromBlackList()] "+"Unblacklisting:"+did);
            blackList.remove(did);
      
            // get the device
            DiABluDevice oldDD = getDiABluDevice(did);
            if (oldDD.getID().getUUID().equalsIgnoreCase("")) {
            
                // device not found
                logger.finest("[Model - removeFromBlackList()] "+"Black listed device not found:"+did);
                return;
                
            }
            
            // found device
            // update view
            Vector <DiABluDevice> updateView = new Vector <DiABluDevice> ();
            
            oldDD.setStatus(DEVICE_STATUS_BT);
        //   serverView.removeDiABluDevices(updateView);
        //updateView.removeAllElements();
        // TODO:reformat this
        //oldDD.setStatus(1); // bluetooth status
        updateView.add(oldDD);
        serverView.editDiABluDevices(updateView);
        
        }
        
    }
    
    public void clearBlackList(){
        
        // make sure there's work to do
        if (!blackList.isEmpty()){           
        
            blackList.removeAllElements();
        
            if (SERVER_VIEW_LISTENER_READY) serverView.resetDeviceList(currentDiABluDevices);
            
        } else {
            
            logger.warning("Trying to reset an empty black list");
            
        }
                
    }
    
    // Simulator
    public void startStopSimulator(){
        
        if (this.isSimulatorRunning){
                   
        
            simView.setVisible(false);
        
            isSimulatorRunning = false;
        
            serverView.simulatorIsRunning(isSimulatorRunning);
        
            SIMULATOR_LISTENER_READY = false;
        
        } else {
        //TODO
        simView.setVisible(true);
        isSimulatorRunning = true;
        serverView.simulatorIsRunning(isSimulatorRunning);
        SIMULATOR_LISTENER_READY = true;
        }
    }

    
    /**
     *  This method records in the model 
     *  the state of the automatic simulator
     *  start
     *
     */
    public void autoSimulator(boolean as){
        
        logger.finest("Changing value:"+isSimulatorAuto+" to "+as);
        this.isSimulatorAuto = as;
        
    }
    
    // Bluetooth
    public void newServiceName(String newServiceName){
        
       this.serviceName = newServiceName;
       if (BT_SERVER_LISTENER_READY) btListener.setServiceName(newServiceName);
        
    }
    
    public void newServiceDescription(String newServiceDescription){
       
        this.serviceDescription = newServiceDescription;
        if (BT_SERVER_LISTENER_READY){
        
            System.out.println("Calling bt server");
            btListener.setServiceDescription(newServiceDescription);
        
        }
        
    }
    
    public void newBluetoothDelay(int btDelay){
        
        this.btDelay = btDelay;
        if (BT_DISCOVERY_LISTENER_READY){
        
            btListener.setDelay(btDelay);
            
        }
        
        
    }
    
    public void newVerifyCyclesIN(int vCin){
        
        this.btVCyclesIN = vCin;
        
    }
    
    public void newVerifyCyclesOUT(int vCout){
        
        this.btVCyclesOUT = vCout;
        
    }
    
    public void setFilterFriendlyNames(boolean filter){
        
        this.filterFriendlyNames = filter;
        
    }
    
    public void setFastMode(boolean fastMode){
        
        this.fastMode = fastMode;
        if (BT_DISCOVERY_LISTENER_READY){
           diABluDiscovery.setFastMode(fastMode);
        }
    }
    
    public void startStopDiscovery(){
        
         if (BT_DISCOVERY_LISTENER_READY){
            
             if (this.isDiscoveryRunning){
            
                 diABluDiscovery.stopDiscovery();
                 this.isDiscoveryRunning = false;
                 
             } else {
                 
                 diABluDiscovery.startDiscovery();
                 this.isDiscoveryRunning = true;
                 
             }
            
            
        } else {
            
             
            logger.warning("Discovery Listener not ready");
            return;
            
        }
        
        
        if (SERVER_VIEW_LISTENER_READY){
            
            serverView.setDiscoveryStatus(this.isDiscoveryRunning);
            
        }
        
    }

    
    public void startStopService(){
        
       if (BT_SERVER_LISTENER_READY){
           
            if (this.isServiceRunning){
            
                 btListener.stopService();
                 this.isServiceRunning = false;
                 
             } else {
                 
                 btListener.startService();
                 this.isServiceRunning = true;
                 
             }
           
           
           if (SERVER_VIEW_LISTENER_READY){
            
                serverView.setServiceStatus(this.isServiceRunning);
            
            } else {
            
                logger.warning("Server View not ready");
            
            }
           
       } else {
        
           logger.warning("Service Server not ready");
       }
       
    }
      
    
    public void setAutoStartDiscovery(boolean autoDiscovery){
        
        logger.finest("Auto Start Discovery?:"+autoDiscovery);
        this.autoStartDiscovery = autoDiscovery;
        logger.finest("Setted start discovery ?"+this.autoStartDiscovery);
    }
    
    public void setAutoStartService(boolean autoService){
        
        this.autoStartService = autoService;
        
    }
    
    // OUT
    public void setProtocol(String prot){
        
         if (SERVER_VIEW_LISTENER_READY){
            
            serverView.setProtocol(prot);
            
            } else {
            
                logger.warning("Server View not ready");
            
            }
    }
    
    
    public void newPort(String newPort){
        
        this.targetPort = newPort;
        if (OSC_LISTENER_READY){
        
            oscListener.setTargetPort(newPort);
            
        }
        
        
    }
    
    public void newTargetAddress(String newTargetAddress){
      
        this.targetAddress = newTargetAddress;
        if (OSC_LISTENER_READY){
        
            oscListener.setTargetAddress(newTargetAddress);
            
        }
        
        
    }
    
    // Save/Load/Apply Settings
    public void loadSettings(){
        
        logger.fine("Loading settings...");
        settings.loadSettings();
        
    }
    
    public void applySettings(){
        
        logger.fine("Apply settings not needed");
        
    }
    
    public void saveSettings(){
        
        logger.fine("Saving settings");
        settings.saveSettings();
        
    }
    
    // Log
    public void newLogLevel(String newLogLevel){
       
        try {
                logger.setLevel(Level.parse(newLogLevel));
                fh.setLevel(Level.parse(newLogLevel));
                ch.setLevel(Level.parse(newLogLevel));
                
        } catch (Exception e) {
            
                logger.warning("Error trying to set log level:"+newLogLevel+"Error:"+e.getLocalizedMessage());
        }
        
    }
    
    public void clearLog(){
        
        if (SERVER_VIEW_LISTENER_READY){
        
            serverView.clearLog();
            
        }
        // TODO: internal model log, internal max and external max should be established
        
    }
    
    public void saveLog(){
        
        // TODO
        
    }
    
    /**
     * This method closes the actual view and creates
     * a new one with updated values. It's used to change the
     * layout
     *
     *
     */
    public void setView(String v){
        
        if (v.equalsIgnoreCase(VIEW_CLASSICAL)){
            
            //TODO:call the classic view and update the values
            
        } else {
            
            //TODO:call the compact view
            
        }
        
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
    
    public void setBundlePath(String newBundlePath){
        
        this.bundlePath = newBundlePath;
        
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
            
            logger.finest("[Model - registerGeneralListener()] "+"Null argument");
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
    private void initializeGeneralListener(DiABluServerModelListener dModelListener){
        
        // paranoid
        if (dModelListener == null){
            
            logger.finest("[Model - initializeGeneralListener()] "+"Null argument");
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
            
            logger.finest("[Model - getDeviceIndex()] "+"Null argument");
            
        } else {
            
            DiABluDevice dd = new DiABluDevice();
            
            for (int i = 0; i < currentDiABluDevices.size(); i++ ){
                
                dd = currentDiABluDevices.elementAt(i);
                
                //if (dd.getID().getUUID().equalsIgnoreCase(did.getUUID())){
                if (dd.getID().getUUID().equalsIgnoreCase(did.getUUID())) {
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
        
        logger.fine("Initializing server view...###############");
        
        // paranoid
        if (sV == null) {
            
            logger.finest("[Model - initializeServerView()] "+"Null argument");
            return;
        }
        
        sV.setCountry(this.country);        
        sV.setLanguage(this.language);
        
        /** DEPRECATED
         * Received orders to remove auto start simulator from the view
         * There's still methods and fields across the application.
        // Simulator
        sV.setSimulatorAuto(this.isSimulatorAuto);
        */
         
        // Protocol Bluetooth
        sV.setFilterFNames(this.filterFriendlyNames);
        sV.setSimulatorStatus(SIMULATOR_LISTENER_READY);
        logger.finest("Discovery STATUS:"+BT_DISCOVERY_LISTENER_READY);
        sV.setDiscoveryStatus(BT_DISCOVERY_LISTENER_READY);
        logger.finest("Is Discovery AUTO?:"+this.autoStartDiscovery);
        sV.setAutoDiscovery(this.autoStartDiscovery);
        sV.setServiceStatus(BT_SERVER_LISTENER_READY);
        logger.finest("Is Service Server AUTO?:"+this.autoStartService);
        sV.setAutoService(this.autoStartService);
        sV.setVCyclesIN(this.btVCyclesIN);
        sV.setVCyclesOUT(this.btVCyclesOUT);
        sV.setServiceName(this.serviceName);        
        sV.setServiceDescription(this.serviceDescription);        
        sV.setBluetoothDelay(this.btDelay);
        
        //OUT
        sV.setProtocol(this.protocol);
        sV.setTargetAddress(this.targetAddress);
        sV.setTargetPort(this.targetPort);
        
        // Log
        sV.setLogDetail(this.logDetail);
        
        
    }
    
    private void initializeBTListener(DiABluServerBTModelListener bL) {
        
        
        // paranoid
        if (bL == null) {
            
            logger.finest("[Model - initializeBTListener()] "+"Null argument");
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
    
    /**
     *   This method removes black listed devices from the argument list
     *   and returns the result list
     *
     */
    private Vector <DiABluDevice> removeBlackListed(Vector <DiABluDevice> rawDeviceList){
        
        Vector <DiABluDevice> filteredDeviceList = new Vector <DiABluDevice> ();
        
        // paranoid
        if (rawDeviceList == null){
            
            logger.finest("[Model - removeBlackListed()] "+"Null argument");
            return filteredDeviceList;
            
        }
        
        if ( rawDeviceList.isEmpty() ){
            
            logger.finest("[Model - removeBlackListed()] "+"Empty argument");
            return filteredDeviceList;
            
        }
        
        logger.finest("[Model - removeBlackListed()] "+"Checking "+rawDeviceList.size()+" elements");
        /**
         * first check if we have any black listed devices
         * them check the list for the special ones
         *
         */
        if (!this.blackList.isEmpty()) {
            
            DiABluDevice tempDD = new DiABluDevice();
            for (String did:blackList){
                
                //for(DiABluDevice dd:rawDeviceList){
                for ( Iterator <DiABluDevice> iDD = rawDeviceList.iterator(); iDD.hasNext(); ){
                    
                    // get our comparable id
                    tempDD = iDD.next();
                    // String unCheckedID = tempDD.getID().getUUID();
                    if (isBlackListed(tempDD)) iDD.remove();
                    /** DEPRECATED
                    if (did.equalsIgnoreCase(unCheckedID)) {
                        
                        // rawDeviceList.removeElement(dd);
                        iDD.remove();
                        logger.finest("[Model - removeBlackListed()] "+"Removing "+unCheckedID);
                        break;
                        
                    }
                     */
                }
                
            }
            filteredDeviceList = rawDeviceList;
            
        }
        
        
        logger.finest("[Model - removeBlackListed()] "+"Returning "+filteredDeviceList.size()+" elements");
        return filteredDeviceList;
        
    }
    
    /**
     *  This method returns an list of black listed devices inside
     *  the argument list
     *
     *
     */
    private Vector <DiABluDevice> getBlackListed(Vector <DiABluDevice> rawList){
        
        Vector <DiABluDevice> blackListedList = new Vector <DiABluDevice> ();
        
        
        if ( rawList != null ){
            
            if (!rawList.isEmpty()){
            
                logger.finest("Checking device list:"+rawList.size());
                DiABluDevice blackDevice = new DiABluDevice();
                for (DiABluDevice dd:rawList){
                    
                    if (isBlackListed(dd)){
                        blackDevice = dd;
                        blackListedList.add(dd);
                    }
                                                            
                }
                
                
            } else {
                
                logger.warning("Empty argument");
                
            }                        
            
        } else {
            
            logger.warning("Null argument");
            
        }
        
        logger.finest("Returning list:"+blackListedList);
        return blackListedList;        
        
    }
    
    /**
     *  This method checks if a given device is black listed
     *  Returns boolean;
     *
     */
    private boolean isBlackListed(DiABluDevice dd){
        
        String uuidT = dd.getID().getUUID();
        for (String blackS:blackList){
            
            if (blackS.equalsIgnoreCase(uuidT)){
                
                return true;
            }
            
        }
        return false;
        
    }    
    /**
     * This method checks if a given UUID is black listed
     * Same purpose as with a diablu device
     * Returns boolean; 
     *
     */
    private boolean isBlackListed(String uuidT){
        
        for (String blackS:blackList){
            
            if (blackS.equalsIgnoreCase(uuidT)){
                
                return true;
            }
            
        }
        return false;
        
        
        
    }
    
    /**
     *  This method returns a list of the choosen status ONLY from
     *  the argument list.
     *  Example: BLUETOOTH_STATUS
     *
     *
     */
    private Vector <DiABluDevice> cropDeviceList(int status,Vector <DiABluDevice> rawDeviceList){
        
        Vector <DiABluDevice> croppedDeviceList = new Vector <DiABluDevice> ();
        
        // paranoid
        if (rawDeviceList == null || rawDeviceList.isEmpty()){
            
            logger.finest("[Model-cropDeviceList()] "+"Null or Empty argument received");
            
        } else {
            
            logger.finest("[Model-cropDeviceList()] "+"Checking "+rawDeviceList.size()+" elements");
            
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
            
            logger.warning("[Model-stripDeviceList()] "+"Null or Empty argument received");
            
        } else {
            
            logger.finest("[Model-stripDeviceList()] "+"Checking "+rawDeviceList.size()+" elements");
            
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
     * This method cleans the verifyDiABluDevicesOUT vector from
     * all the DiABlu Devices in the argument vector [cleanDeviceList]
     * Usefull to make sure we remove new devices that may have been in
     * verify list
     *
     */
    private void cleanVerifyList(Vector <DiABluDevice> cleanDeviceList) {
        
        // usual paranoia
        if (cleanDeviceList==null){
            
            logger.warning("[Model-cleanVerifyList()] "+"Null argument.");
            return;
        }
        
        if (cleanDeviceList.isEmpty()){
            
            logger.finest("[Model-cleanVerifyList()] "+"Empty argument.");
            return;
            
        }
        
        // are you sure there is any work to do ? :)
        if (verifyDiABluDevicesOUT.isEmpty()) return;
        
        // there's work to do :P
        DiABluDevice tempDD = new DiABluDevice();
        for (DiABluDevice cleanD:cleanDeviceList){
            
            // for (DiABluDevice verifyD:verifyDiABluDevicesOUT) {
            for ( Iterator <DiABluDevice> iDD = verifyDiABluDevicesOUT.iterator(); iDD.hasNext(); ) {
                
                tempDD = iDD.next();
                // found a match
                if (tempDD.compareTo(cleanD)==0){
                    //verifyDiABluDevicesOUTIN.remove(cleanD);
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
            
            logger.finest("[Model - cleanVerifyListElement()] "+" Null argument");
            return;
            
        }
        
        if (verifyDiABluDevicesOUT.isEmpty()) { return; }
        
        DiABluDevice verifyDD = new DiABluDevice();
        //for (DiABluDevice verifyDD:verifyDiABluDevicesOUT){
        for ( Iterator <DiABluDevice> iDD = verifyDiABluDevicesOUT.iterator(); iDD.hasNext(); ){
            
            verifyDD = iDD.next();
            if (verifyDD.equals(cleanDevice)) {
                
                //verifyDiABluDevicesOUT.remove(verifyDD);
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
            
            logger.warning("[Model - filterBlackListed()] "+"Null argument");
            return filteredDeviceList;
            
        }
        
        if ( rawDeviceList.isEmpty()) {
            
            logger.warning("[Model - filterBlackListed()] "+"Empty argument");
            return filteredDeviceList;
            
        }
        
        // everything ok
        filteredDeviceList = rawDeviceList;
        
        if (!blackList.isEmpty()) {
            
            DiABluDevice tempDD = new DiABluDevice();
            String tempID = "";
            //for (DiABluDevice dd:filteredDeviceList){
            for ( Iterator <DiABluDevice> iDD = filteredDeviceList.iterator(); iDD.hasNext(); ) {
                
                tempDD = iDD.next();
                tempID = tempDD.getID().getUUID();
                for (String s:blackList){
                
                    if (s.equalsIgnoreCase(tempID)){
                        
                        // device is black listed
                        iDD.remove();
                        // no need to continue 
                        break;
                        
                    }
                
                }
                
                                
            }
            
            logger.finest("[Model - filterBlackListed()] "+"Filtered list with "+filteredDeviceList.size()+" elements of a total of "+currentDiABluDevices.size());
            
        } else {
            
            logger.finest("[Model - filterBlackListed()] "+"Black List is empty.Nothing to do here.");
            
        }
        
        return filteredDeviceList;
    }
    
    
    /**
     *
     * This method searches/returns the current device for the given id.
     * Returns an empty device if didn't find none
     *
     */
    private DiABluDevice getDiABluDevice(String did){
        
        logger.finest("Searching for "+did+" device in a total of "+currentDiABluDevices.size());
        DiABluDevice foundDiABlu = new DiABluDevice();
        
        for (DiABluDevice dd:currentDiABluDevices){
            
            logger.finest("[Model - getDiABluDevice()] "+"Inspecting:"+dd.toString()+"@"+dd.getID().getUUID());
            if (did.equalsIgnoreCase(dd.getID().getUUID())){
                
                foundDiABlu.setID(dd.getID());
                foundDiABlu = dd;
                logger.finest("[Model - getDiABluDevice()] "+"Found device:"+foundDiABlu.toString());
                break;
                
            }
            
        }
        
        return foundDiABlu;
    }
    
    /**
     *   Cache methods
     *  
     */
    
    /**
     *  This method checks the new list of DiABlu Device's
     *  one - check for new names to add to the id's cache
     *  two - fill empty names with names from the id's cache
     *
     */
    private Vector <DiABluDevice> cacheProcessDeviceList(Vector <DiABluDevice> newDevices){
        
        Vector <DiABluDevice> processedList = new Vector <DiABluDevice>();
        
        // paranoid check
        if (newDevices==null){
            
            logger.warning("Null argument");
            logger.finer("Returning empty list");
            return processedList;
            
        }
        
        
        DiABluDevice tempDD = new DiABluDevice();
        
        // get each device
        for (DiABluDevice dd:newDevices){
            
            // process it
            tempDD = cacheProcessDevice(dd);            
            processedList.add(tempDD);
            
        }
        
        return processedList;
        
    }
    
    
    /**
     *  This method receives a DiABluDevice and check's for two situations:
     *  The device doesn't have a friendly name set:checks the cache to see if there's any previous one
     *  The device has a friendly name set:either add it's id to cache, or update the cache
     *  Returns the same device with proper changes if applyable 
     *
     */
    private DiABluDevice cacheProcessDevice(DiABluDevice cdd){
        
        logger.finest("Cache Processing:"+cdd.getID().toString());
        if (cdd.getID().getFName().equalsIgnoreCase("")){
            
            logger.finest("Friendly name not set.Checking cache for previous one...");
            // device friendly name not set
            for (DiABluID did:diabluCache){
                
             
                if (did.getUUID().equalsIgnoreCase(cdd.getID().getUUID())){
                 
                 // found device, update it and quit the cycle
                 logger.finest("Found cached name:"+did.getFName());
                 cdd.getID().setFName(did.getFName());
                 return cdd;
                 
                }   
                
                
            }
            
        } else {
            
            logger.finest("Friendly name set, checking cache for updates...");
            // update or add it to the cache
            int cacheSize = diabluCache.size();
            boolean found = false;
            if (cacheSize>0){
                
                for (int i=0;i<cacheSize;i++){
                    
                  if (diabluCache.elementAt(i).getUUID().equalsIgnoreCase(cdd.getID().getUUID())){
                    
                    // found device
                    logger.finest("Device already in cache");
                    
                      // check for updates
                      if (!diabluCache.elementAt(i).getFName().equalsIgnoreCase(cdd.getID().getFName())){
                      
                        // update cache friendly name
                        diabluCache.elementAt(i).setFName(cdd.getID().getFName());
                        logger.finest("Friendly name changed.Updating cache...");
                        return cdd;
                                                            
                      } 
                    
                    return cdd;
                    
                  }
                    
                }
                
            }                
            
            // not found
            // add it to cache
            logger.finest("Adding ID to cache...");
            diabluCache.add(cdd.getID());
                            
        }
                
        return cdd;
    }
    
    /**
     *  This method receives a list of DiABluDevices and
     *  remove's Devices without the friendly name set.     *
     *
     */
    private Vector <DiABluDevice> filterFNames(Vector <DiABluDevice> rawList){
        
        Vector <DiABluDevice> filteredList = new Vector <DiABluDevice> ();
        
        if (rawList == null){
            
            logger.warning("Null argument");
            logger.finer("Returning empty result");
            
            
        } else {
            
            logger.finest("Checking "+rawList.size()+" friendly names");
            String fnameT = "";
            for (DiABluDevice dd:rawList){
                
                fnameT = dd.getID().getFName();
                if (!(fnameT.equalsIgnoreCase("")||fnameT.equalsIgnoreCase("[none yet]"))){
                    
                    filteredList.add(dd);
                    
                }
                
                
            }
            
            
        }
        
        logger.finest("Returning "+filteredList+ " devices list");
        return filteredList;
        
    }
    
    // Settings getters
    public String getLanguage(){
        
        return this.language;
    }
    
    public String getLocation(){
        
        return this.country;
    }
    
    public String getViewString(){
        
        return this.preferredView;
        
    }
    
    public boolean isFilterFriendlyName(){
        
        return this.filterFriendlyNames;
    
    }
    
    public Vector <String> getBlackList(){
        
        return this.blackList;
    
    }
    
    public boolean isAutomaticSimulator(){
        
        return this.isSimulatorAuto;
        
    }

    public boolean isAutoStartDiscovery() {
        
        return this.autoStartDiscovery;
        
    }  
    
   
    public boolean isAutoStartService() {
     
        return this.autoStartService;
        
    }

    public int getBtVCyclesIN() {
        return this.btVCyclesIN;
    }

    public int getBtVCyclesOUT() {
        return this.btVCyclesOUT;
    }

    public boolean isFastMode() {
        return this.fastMode;
    }       

    public String getServiceName() {
        return this.serviceName;
    }

    public String getServiceDescription() {
        return this.serviceDescription;
    }
        

    public String getProtocol() {
        return protocol;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public String getTargetPort() {
        return targetPort;
    }

    public int getLogDetail() {
        return logDetail;
    }        

    public int getBtDelay() {
        return btDelay;
    }
    
    private Vector <DiABluDevice> processVCyclesIN(Vector <DiABluDevice> inList){
        
        Vector <DiABluDevice> processedList = new Vector <DiABluDevice> ();
        Vector <DiABluDevice> tempDDInList = new Vector <DiABluDevice> ();  // Temporary IN verify cycles list      
        
        // checks        
        if (inList != null && btVCyclesIN > 0){
            
            
            DiABluDevice tempDD = new DiABluDevice ();
            int vCounter = 0;
            boolean newVerifyDevice = true;
            
            logger.finest("Processing list with "+inList.size()+" elements");
            
            for (DiABluDevice dd:inList){
                
                newVerifyDevice = true;
                for (DiABluDevice vdl:verifyDiABluDevicesIN) {
                    
                    if (dd.compareTo(vdl)==0){
                        
                        // found a match
                        newVerifyDevice = false;
                        
                        // check the counter
                        vCounter = vdl.getDetectionCounter();
                        if (vCounter>btVCyclesIN) {
                            
                            // expired the counter
                            
                            // add it to the result list
                            processedList.add(dd);                                                       
                            
                        } else {
                            
                            // update the counter
                            tempDD = dd;
                            tempDD.incrementDetectionCounter();                            
                            tempDDInList.add(tempDD);
                            
                        }
                        
                      // nothing more to do with this device
                      break;
                      
                    }
                    
                }
                
                // check for new elements
                if (newVerifyDevice) {
                    
                    tempDD = dd;
                    tempDD.incrementDetectionCounter();
                    tempDDInList.add(tempDD);
                    
                    
                }
                   
                
            }           
            
        } else {
            
            logger.warning("Null argument");
            
        }
        
        // update the verify list
        verifyDiABluDevicesIN = tempDDInList;
        
        return processedList;
    }

    public void newProtocol(String prot){
        
        // no changes
        if (this.protocol.equalsIgnoreCase(prot)){
            return;
        }
        
        int port; // convert targetPort

        try {
            
            port = Integer.parseInt(this.targetPort);
            
        } catch (Exception e){
        
            logger.warning("Error converting target port.Using default value instead");
            port = Integer.parseInt(OUT_DEFAULT_TARGET_PORT); // this should be safe
            
        }
        
        if (prot.equalsIgnoreCase(PROTOCOL_OSC)){
            
            oscListener = new DiABluServerOSC(this.targetAddress,port);
            //TODO:update method
            
        } else if (prot.equalsIgnoreCase(PROTOCOL_FLOSC)){
            
            oscListener = new DiABluServerFlosc(this.targetAddress,port);
            
        }
        
        // TODO:implement multiple protocol
        this.protocol = prot;
        
    }
    
    public void refreshDetectedView(){
        
        if (SERVER_VIEW_LISTENER_READY){
            
            serverView.resetDeviceList(currentDiABluDevices);
        }
    }

}
