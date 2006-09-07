/*
 * DiABluBTServer.java
 *
 * Created on 25 juillet 2006, 11:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.bt;

import java.lang.*;
import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;
import citar.diablu.bc.DiABluBC;

/**
 *
 * @author nrodrigues
 */
public class DiABluBTServer extends Thread {
    
    DiABluBC parent;
    boolean DONE = false;
    
    /* Service Name & Description */
    String serviceName = "DiABluServer";
    String serviceDesc = "Simple messaging service that allows you to interact with this digital art installation";
    
    StreamConnectionNotifier server = null;     // the connection notifier
    RemoteDevice clientDevice = null;
    String message = "";                        
        String ROGER = "ROGER";
        String OVER = "OVER";
        StreamConnection conn = null;
        
           int length; 
        
        // Communication buffers
        byte[] dataIN = new byte[256];
        byte[] dataOUT = new byte[256];
    
    /** Creates a new instance of DiABluBTServer */
    public DiABluBTServer(DiABluBC classBC,String sName,String sDesc) {
        
        // initialize vars
        this.parent = classBC;                      // this class gui
        this.serviceName = sName;                   // service name
        this.serviceDesc = sDesc;                   // service description
     

        
       
    }
    
    public void run() {
        

        System.out.println("Starting DiABlu Server Service");
        // get our bt device
        // set it discoverable (?)
        try {
            
            LocalDevice local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);
            
        } catch (BluetoothStateException e) {
            System.err.println("Failed to start service");
            System.err.println("BluetoothStateException: " + e.getMessage());
            return;
        }

        // open a notifier 
        try {
            
            server = (StreamConnectionNotifier)Connector.open("btspp://localhost:F0E0D0C0B0A000908070605040302013;name=DiABluServer2");
            
        } catch (IOException e) {
            
            System.err.println("Failed to start service");
            System.err.println("IOException: " + e.getMessage());
            return;
        }                        
        
        while (!DONE){
        // register the service and wait for clients      
        try {
                System.out.println("Waiting client connections...");
                conn = server.acceptAndOpen();
                System.out.println("Client connected!Getting info");               
                clientDevice = RemoteDevice.getRemoteDevice(conn);
                String id = clientDevice.getBluetoothAddress();
                DiABluBTConnection dBTC = new DiABluBTConnection(conn,parent,id);               
                dBTC.start();
                                
            } catch (IOException e) {
                  
                System.err.println("IOException: " + e.getMessage());       
                
             }
        }
    }
    
    public void setDone(boolean newDone){
        DONE = newDone;
    }
    
    public void log(String msgLog){
        
        parent.newLog(msgLog);
        
    }
}
