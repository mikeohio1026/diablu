/*
 * DiABluBTDeviceDiscovery.java
 *
 * Created on 9 de Maio de 2006, 11:08
 *
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

package citar.diablu.com.bt;

import java.lang.*;
import java.io.*;
import java.util.Vector;
//import javax.microedition.io.*;
import javax.bluetooth.*;
import citar.diablu.com.interfaces.INWatcher;
import citar.diablu.classes.DiABluDevice;
import citar.diablu.classes.DiABluID;
import citar.diablu.bc.DiABluBC;

/**
 *
 * @author nrodrigues
 */
public class DiABluBTDeviceDiscovery implements DiscoveryListener {
    
    private DiscoveryAgent agent;               // Our Discovery Agent
    private static Vector deviceList;           // List of Discovered Devices
    private long time_initial;                  // TIME COUNTER - initial time
    private long time_end;                      // TIME COUNTER - ended time
    private long time_elapsed;                  // TIME COUNTER - elapsed time
    private INWatcher dbBC;
    private LocalDevice localDev;
    private int delayBetweenInquirys = 0;
    private int BLUETOOTH_STATUS_CODE=1;
    
   // private INWatcher outputBT;               // Output interface to the DiABlu Business Core
    
    /**
     * Creates a new instance of DiABluBTDeviceDiscovery
     */
    public DiABluBTDeviceDiscovery(INWatcher classBC) throws BluetoothStateException {
        
        this.dbBC = classBC;
        this.delayBetweenInquirys = dbBC.getBTdelay();
        localDev = LocalDevice.getLocalDevice();
        agent = localDev.getDiscoveryAgent();
        deviceList = new Vector();        
       
        // Start the INQUIRY
        searchDevices();
        
    }    
    
    /**
     * This methods print the log messages and outputs them
     * to the DiABlu Watcher Interface
     */    
    public void log(String logMsg) {
        
        // simple info
        dbBC.newLog(4,"[BT-discovery]:"+logMsg);
      
    }
    
    public void log(int priority, String logMsg) {
        
        // specified priority
        dbBC.newLog(priority, "[BT-discovery]:"+logMsg);
 
    }
    
    public void deviceDiscovered (RemoteDevice btDevice, DeviceClass cod){
        
        String uuidT="";
        String fnameT="";
        int minor = 0;
        int major = 0;
        
        // get the device's uuid 
        uuidT = btDevice.getBluetoothAddress();      
        log(4,"[FOUND DEVICE]@"+uuidT);
        
        
        // make sure it's not a duplicate
        int total = deviceList.size();
        for (int i = 0;i<total;i++){
            
            if (((DiABluDevice)deviceList.elementAt(i)).getID().getUUID().equalsIgnoreCase(uuidT)) { 
                dbBC.newLog(4,"[DUPLICATE DEVICE FOUND @"+uuidT+"]");
                return; 
            }
        }
        
        // get the device's friendly name
        try {
            fnameT = btDevice.getFriendlyName(true);            
            minor = cod.getMinorDeviceClass();
            major = cod.getMajorDeviceClass();
            
            log(4,"[DEVICE FRIENDLY NAME]:"+fnameT);
            log(4,"[MAJOR DEVICE CLASS CODE]:"+major);
            log(4,"[MINOR DEVICE CLASS CODE]:"+minor);
            log(4,"[/END OF DEVICE INFO]");
        } catch (Exception e){
            log(3,"[BT DEVICE EXCEPTION]:"+e);
        }        
        
        // create a new DiABlu ID
        DiABluID idT = new DiABluID(uuidT,fnameT);
        
        // create a new DiABlu Device
        DiABluDevice ddT = new DiABluDevice(idT,major,minor,BLUETOOTH_STATUS_CODE);
        
        // add the device to the device list
        deviceList.addElement(ddT);                      
        
    }

    public void inquiryCompleted(int discType){
        
        log(4,"[INQUIRY COMPLETED]:"+discType);
         
        // report the list of discovered devices

        dbBC.newDeviceList(deviceList,BLUETOOTH_STATUS_CODE);
                        
        // empty the list
        deviceList.removeAllElements();
        
       
        synchronized (this) {
            try {
                  log(4,"Sincronizing...");
                  this.notifyAll();
            }catch (Exception e)
            {
                log(4,"[INQUIRY EXCEPTION]:"+e);
            }            
        }
        log(4,"Sinc completed.Restarting search...");
        
        //STOPED for test purposes
        //restartSearch();
        
    }
    
    
    // DEPRECATED - see searchDevices()
    public void restartSearch(){
        
        /**
         * Use to make infinite cicle so you can test the discovery service
         * IS THIS CORRECT ? SHOULD I DO IT IN A DIFFERENT THREAD ?
         *
         */
        
        log(4,"Restarting search...");
        try 
        {

        
            agent.startInquiry(DiscoveryAgent.GIAC, this);
        } catch (BluetoothStateException e) {
            
            log(4,"[UNABLE TO FIND DEVICES!RESTART ERROR]" + e.getMessage() );
            e.printStackTrace();
        }
        
        
        
    }
    
    
    public void searchDevices() {
         
        while (delayBetweenInquirys!=-1) {
            
            try { 
                                        
                agent.startInquiry(DiscoveryAgent.GIAC, this);
                
                // wait until inquiry is done
                synchronized (this) {
                    
                    try {
                        
                        this.wait();
                        
                    } catch (Exception e) {
                        
                        log(2,"[ERROR WAITING BT INQUIRY]"+e.getLocalizedMessage());
                        e.printStackTrace();
                        
                    }
                }
                                    
            } catch (BluetoothStateException e) {
            
                log(2,"[UNABLE TO FIND DEVICES!]" + e.getLocalizedMessage() );
                e.printStackTrace();
              
            }
            
            // Give the corresponding pause
            try {
                
                Thread.sleep(this.delayBetweenInquirys);
                
            } catch (Exception e) {
                
                log(3,"[ERROR SLEEPING BT INQUIRY]"+e.getLocalizedMessage());
                e.printStackTrace();
                
            }                
       
        }
    }            
    
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
   
    }
    
    public void serviceSearchCompleted(int transID, int respCode) {
     
    }

    
    
    
}
