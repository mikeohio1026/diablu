/*
 * dbBTdiscovery2.java
 *
 * Created on 20 juillet 2006, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bt;

import java.lang.*;
import java.io.*;
import java.util.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.bluetooth.*;
import bc.gui.msgMain;
import org.netbeans.microedition.lcdui.WaitScreen;

/**
 * This class shows a simple client application that performs device
 * and service
 * discovery and communicates with a print server to show how the Java
 * API for Bluetooth wireless technology works.
 */
public class dbBTdiscovery  implements DiscoveryListener {

    
    /**
     * The DiscoveryAgent for the local Bluetooth device.
     */
    private DiscoveryAgent agent;

    /**
     * Keeps track of the devices found during an inquiry.
     */
    private Vector deviceList;
    //*************
    private Vector serviceList;
    
    // gui parent
    private TextBox parent;   
    private int progressStatus;
    private Gauge progressBar;
    /**
     * Creates a PrintClient object and prepares the object for device
     * discovery and service searching.
     *
     * @exception BluetoothStateException if the Bluetooth system could not be
     * initialized
     */
    public dbBTdiscovery(TextBox parent,Gauge progressBar) throws BluetoothStateException {
        
        this.parent = parent;            
        
        progressStatus = 0;
        
        /*
         * Retrieve the local Bluetooth device object.
         */
        LocalDevice local = LocalDevice.getLocalDevice();
        progress(1);
        
        /*
         * Retrieve the DiscoveryAgent object that allows us to perform device
         * and service discovery.
         */
        agent = local.getDiscoveryAgent();

        deviceList = new Vector();
        
        //*******************
        serviceList = new Vector();
        
    }
    
 public void progress(int ps){
     
       //TODO:use a incremental and precise updating 
      // progressBar.setValue(Gauge.CONTINUOUS_RUNNING);
      
    }
    
    public void log(String msg){
        
        msg = "[BT]"+msg;
        int posMAX = parent.getMaxSize();
        
        if (parent.size()+msg.length()>=posMAX) {
            parent.setString("");
        }
        
        parent.insert(msg, parent.size());
                
    }
    
    private void searchServices(RemoteDevice[] devList) {
        
        int[] attrs = { 0x100 };
        
        UUID[] searchList = new UUID[2];

        /*
         * Add the UUID for L2CAP to make sure that the service record
         * found will support L2CAP.  This value is defined in the
         * Bluetooth Assigned Numbers document.
         */
        searchList[0] = new UUID(0x0100);

        /*
         * Add the UUID for the printer service that we are going to use to
         * the list of UUIDs to search for. (a fictional printer service UUID)
         */
        searchList[1] = new UUID("F0E0D0C0B0A000908070605040302013", false);

        /*
         * Start a search on as many devices as the system can support.
         */
        log("Searching " + devList.length+" devices");
        for (int i = 0; i < devList.length; i++) {
                       
            try {
                    log("Service Search on " + devList[i].getBluetoothAddress());
                    progress(5);
                    agent.searchServices(attrs, searchList, devList[i],this);
                   
            } catch (BluetoothStateException e) {
                    
                    log("BluetoothStateException: " + e.getMessage());
              
            }

            /*
             * Determine if another search can be started.  If not, wait for
             * a service search to end.
             */
            synchronized (this) {

                log("[Waiting|");
                try {
                        this.wait();
                
                } 
                catch (Exception e) {
                    
                    log("Error waiting:"+e.getMessage());                    
                }
                
                log("| Done Waiting ]");
            }
        }

    }

    public Vector findDiablus() {
    
        RemoteDevice[] devList = null;
   
        /*
         * Did not find a printer service in the list of pre-known or cached
         * devices.  So start an inquiry to find all devices that could be a
         * printer and do a search on those devices.
         */
        /* Start an inquiry to find a printer   */
        try {

            agent.startInquiry(DiscoveryAgent.GIAC, this);
            progress(2);
            /*
             * Wait until all the devices are found before trying to start the
             * service search.
             */
            synchronized (this) {
                try {
                    this.wait();
                } catch (Exception e) {
                }
            }
        
        } catch (BluetoothStateException e) {

            log("Unable to find devices to search");
        }

        if (deviceList.size() > 0) {
            devList = new RemoteDevice[deviceList.size()];
            deviceList.copyInto(devList);
            log("calling ss");
            searchServices(devList); 
            log("Search returned results");
            if (serviceList!=null && serviceList.size()!=0) {
                    //log("Casting SRecord");
                    //ServiceRecord tempService = (ServiceRecord)serviceList.elementAt(0);   
                    log("Returning service list");
                    return serviceList;
            }else {
                    log("error service list is empty");
                    return serviceList;
            }                                           
         } else {
            log("No Devices found!");
         }
        log("No services found:(");
        return serviceList;
     }
  


    /**
     * Called when a device was found during an inquiry.  An inquiry
     * searches for devices that are discoverable.  The same device may
     * be returned multiple times.
     *
     * @see DiscoveryAgent#startInquiry
     *
     * @param btDevice the device that was found during the inquiry
     *
     * @param cod the service classes, major device class, and minor
     * device class of the remote device being returned
     *
     */
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        
        progress(3);
        log("FoundDEV= " + btDevice.getBluetoothAddress());
        int minorDeviceClass;
        int majorDeviceClass;
        
        /** 
         * optimization:work only with possible DiABlu Servers
         * Major Device Class 00001 (256) Computers
         * Minor Device Class 0000010 (4) Desktop Workstation
         *  "   "     "   "   0000100 (8) Server Class Server
         *  "     "    "   "  0000110 (12) Laptop
         * see: Bluetooth Assigned Numbers Document
         */
        
         majorDeviceClass=cod.getMajorDeviceClass();
         minorDeviceClass=cod.getMinorDeviceClass();
         progress(4);
         
         if ( majorDeviceClass == 256 ) 
         {
             if ( minorDeviceClass == 4 || minorDeviceClass == 8 || minorDeviceClass == 12){
                              
                log("Adding dev("+minorDeviceClass+")");
                // TODO: get the service class
                // if ((cod.getServiceClasses() & 0x40000) != 0) 
                deviceList.addElement(btDevice);
                
             } else {
                 // not 100%
                 log("PDA:)");
             }
         } else {
             // not 100%
             log("Phones");
         }
          progress(5);
    }
    
    /**
     * The following method is called when a service search is completed or
     * was terminated because of an error.  Legal status values
     * include:
     * <code>SERVICE_SEARCH_COMPLETED</code>,
     * <code>SERVICE_SEARCH_TERMINATED</code>,
     * <code>SERVICE_SEARCH_ERROR</code>,
     * <code>SERVICE_SEARCH_DEVICE_NOT_REACHABLE</code>, and
     * <code>SERVICE_SEARCH_NO_RECORDS</code>.
     *
     * @param transID the transaction ID identifying the request which
     * initiated the service search
     *
     * @param respCode the response code which indicates the
     * status of the transaction; guaranteed to be one of the
     * aforementioned only
     *
     */
    public void serviceSearchCompleted(int transID, int respCode) {
       
       String logMsg="SS100%("+transID+",";
       
       switch (respCode) {
           case SERVICE_SEARCH_COMPLETED: {
               logMsg += "COMPLETED";
               break;
           }
           case SERVICE_SEARCH_TERMINATED: {
               logMsg += "TERMINATED";
               break;
           }
           case SERVICE_SEARCH_ERROR: {
               logMsg +="ERROR";
               break;
           }
           case SERVICE_SEARCH_DEVICE_NOT_REACHABLE: {
               logMsg +="NOT REACHABLE";
               break;
           }
           case SERVICE_SEARCH_NO_RECORDS: {
               logMsg +="NO RECORDS";
               break;
           }
           default: {
               logMsg +="UNKNOW ERROR!!!";
           }
       }
       
       logMsg +=")";
       log(logMsg);

       // inform the threads
       synchronized (this) {
            this.notifyAll();
       }
       
    }

    /**
     * Called when service(s) are found during a service search.
     * This method provides the array of services that have been found.
     *
     * @param transID the transaction ID of the service search that is
     * posting the result
     *
     * @param service a list of services found during the search request
     *
     * @see DiscoveryAgent#searchServices
     */
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
         
         progress(6);
         log("Service:" + transID);
         log("SLength= " + servRecord.length);
         if (servRecord[0] == null) {
             
            log("The service record is null");
            
         } else {
             
            log("Adding SRecord");
            serviceList.addElement(servRecord[0]);
            
         }
         progress(7);
    }

    /**
     * Called when a device discovery transaction is
     * completed. The <code>discType</code> will be
     * <code>INQUIRY_COMPLETED</code> if the device discovery
     * transactions ended normally,
     * <code>INQUIRY_ERROR</code> if the device
     * discovery transaction failed to complete normally,
     * <code>INQUIRY_TERMINATED</code> if the device
     * discovery transaction was canceled by calling
     * <code>DiscoveryAgent.cancelInquiry()</code>.
     *
     * @param discType the type of request that was completed; one of
     * <code>INQUIRY_COMPLETED</code>, <code>INQUIRY_ERROR</code>
     * or <code>INQUIRY_TERMINATED</code>
     */
    public void inquiryCompleted(int discType) {
       
       // Log the Action
       String outLog ="[Inquiry100%,";
       switch (discType) {
           case INQUIRY_COMPLETED: {
               outLog +="COMPLETED";
               break;
           }
           case INQUIRY_ERROR: {
               outLog+="ERROR";
               break;
           }
           case INQUIRY_TERMINATED: {
               outLog+="TERMINATED";
               break;
           }
           default: {
               outLog+="UNKNOWN!!";
           }
       }
       outLog +="]";
       log(outLog); 
       progress(4);
       // free the waits
       synchronized (this) {
            try {
                this.notifyAll();
            } catch (Exception e) {
            }
        }
    }
}
