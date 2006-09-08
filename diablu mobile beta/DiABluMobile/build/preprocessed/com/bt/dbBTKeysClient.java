/*
 * dbBTclient.java
 *
 * Created on 29 mai 2006, 14:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bt;

import java.io.*;
import java.lang.*;
import javax.microedition.io.*;
import bc.gui.msgMain;
import javax.bluetooth.*;
import bc.pressedKeys;

/**
 *
 * @author Nuno Rodrigues
 */
public class dbBTKeysClient {
    
    // Keeps the connection string in case we want multiple connections
    private String serverConnectionString = "";   
    private OutputStream os;
    private StreamConnection con;
    private msgMain parent;
    private pressedKeys keyListen;
    
    /** Creates a new instance of dbBTclient 
    public dbBTKeysClient(msgMain dtParent,String serverURL) {
        
        this.serverConnectionString = serverURL;
        this.parent = dtParent;
     
          try {
            /*
             * Open the connection to the server
             
            
           log("Opening Server Connection");
           String tempURL=new String(serverConnectionString);
           log("URL:"+tempURL);
           con =(StreamConnection)Connector.open(tempURL);
  
            } catch (IOException e2) {
            log("ERROR2!"+e2.getMessage());
            // ignore
            return;
            //return ("IOException: " + e2.getMessage());
        }
        // Call the key sensor
        keyListen = new pressedKeys(parent);
    
    }
    
    */
    public void log(String slog){
        parent.log("[btc]"+slog);
    }
    
    public void sendKeys(int uniC){
        
        // Exit code
         if (uniC == 256){
             
             closeConn();
             return;
         }
         try {
                    /*
            * Sends data to remote device
            */
          log("Getting output stream");
          String buffer = (new Integer (uniC)).toString();
          os = con.openOutputStream();
           log("writing");
           os.write(buffer.getBytes());
           os.flush();
           }catch (IOException e3){
                   log("Error3:"+e3.toString());
           }
          
    }
    
    public void closeConn(){
          try{
               
           
           Thread.sleep(50000);
           }
           catch (Exception e){
               log("Thread ERROR:"+e.getMessage());
           }
          try {
           os.close();}
          catch (IOException ioE){
              log("IOERROR:"+ioE.getMessage());
          }
    }
}
    
   