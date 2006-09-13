/*
 * DiABluBTServer.java
 *
 * Created on 25 juillet 2006, 11:17
 *
 * Copyright (C) 2006 CITAR
 * This is part of the DiABlu Project
 * http://diablu.jorgecardoso.org
 * Created by Jorge Cardoso(jccardoso@porto.ucp.pt) & Nuno Rodrigues(nunoalexandre.rodrigues@gmail.com)
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
 *
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
    
    /* Service Name*/
    String serviceName = "DiABluServer";

    
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
    public DiABluBTServer(DiABluBC classBC,String sName) {
        
        // initialize vars
        this.parent = classBC;                      // this class gui
        this.serviceName = sName;                   // service name
  

        
       
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
    
    /* DEPRECATED
    public void log(String msgLog){
        
        parent.newLog(msgLog);
        
    }
     */
}
