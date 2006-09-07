/*
 * DiABluBTT.java
 *
 * Created on 10 de Maio de 2006, 14:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author nrodrigues
 */

package citar.diablu.com.bt.tests;

import java.lang.*;
import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

public class DiABluBTT implements DiscoveryListener {
    
    private DiscoveryAgent agent;               // Our Discovery Agent
    private java.util.Vector deviceList;        // List of Discovered Devices
    private long time_initial;                  // TIME COUNTER - initial time
    private long time_end;                      // TIME COUNTER - ended time
    private long time_elapsed;                  // TIME COUNTER - elapsed time
    private long counter;                       // Total of discovery services performed
    
    /** Creates a new instance of DiabluBT */
    public DiABluBTT() throws BluetoothStateException {
        
        
        LocalDevice localDev = LocalDevice.getLocalDevice();
        agent = localDev.getDiscoveryAgent();
        deviceList = new java.util.Vector();
        
        try { 
            
            // Start the first discovery service and timer
            counter = 1;
            time_initial = System.currentTimeMillis();
            agent.startInquiry(DiscoveryAgent.GIAC, this);
            synchronized (this) {
                try {
                    this.wait();                    
                    }
                catch (Exception e) {
                    System.out.println("[BT EX]:"+e);
                    }
                }
            
                        
        } catch (BluetoothStateException e) {
            
            System.out.println("[UNABLE TO FIND DEVICES!!]:" + e );
                       
        }
        
        
        
    }
    
    public void deviceDiscovered (RemoteDevice btDevice, DeviceClass cod){
        
        System.out.println("[FOUND DEVICE]@"+btDevice.getBluetoothAddress());
        try {
            System.out.println("[DEVICE FRIENDLY NAME]:"+btDevice.getFriendlyName(true));
        } catch (Exception e){
            System.out.println("[BT DEVICE EXCEPTION]:"+e);
        }
        
    }
    
    public void inquiryCompleted(int discType){
        
        // Extract the time
        time_end = System.currentTimeMillis();
        time_elapsed = time_end - time_initial;
        
        // Print the info
        System.out.println("[INQUIRY Nº"+counter+" COMPLETED]" +
                           "[END CODE:"+discType +
                           "][TIME ELAPSED:"+time_elapsed+"ms]");
        
        // Finalize the method and check for exceptions
        synchronized (this) {
            try {
                
                  this.notifyAll();
            }
            catch (Exception e){
                
                System.out.println("[INQUIRY Nº"+counter+" FOUND EXCEPTION:"+e+"]");
            }            
        }
        
        /**
         * Used to make infinite cicle so we can test the discovery service
         * Since this method is always called upon finished inquiry we simply 
         * start another
         */
        
        // increment the counter
        counter++;
        
        try {
            
            // Update the initial time
            time_initial = System.currentTimeMillis();
            
            agent.startInquiry(DiscoveryAgent.GIAC, this);
        } 
        catch (BluetoothStateException e) {           
            System.out.println("[UNABLE TO FIND DEVICES!!][EXCEPTION:"+e+"]");                       
        }
        
        
    }
    
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
   
    }
    
    public void serviceSearchCompleted(int transID, int respCode) {
     
    }
    /**
      public static void main(String[] args) {

         DiABluBTT client = null;
         
         try {
            client = new DiABluBTT();
            } 
         catch (BluetoothStateException e) {
            System.out.println("Failed to start Bluetooth System");
            System.out.println("\tBluetoothStateException: " +
                                                     e.getMessage());
        }
         
     }
    */
    
    
    
    
    
}

