/*
 * DiABluBTConnection.java
 *
 * Created on 14 juin 2006, 12:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.bt;

import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;
import citar.diablu.com.interfaces.INWatcher;
import citar.diablu.classes.DiABluMsg;
import citar.diablu.classes.DiABluID;
import citar.diablu.classes.DiABluKey;

/**
 *
 * @author nrodrigues
 */
public class DiABluBTConnection extends Thread {
    
    String ROGER = "ROGER";       // Simple serve acknweledge message
    String message = "";            
    String OVER = "OVER";
    StreamConnection clientConnection = null;
    String clientUUID = "";
    RemoteDevice clientDevice = null;
    INWatcher outInfo;   
    Thread btClient;
    
    
    /** Creates a new instance of DiABluBTConnection */
    public DiABluBTConnection (StreamConnection conn,INWatcher out, String clientID){
      
        this.clientConnection = conn;        
        this.clientUUID = clientID;
        this.outInfo = out;
                  
    } 
    
    // DEPRECATED
    public void log(String s){
        
        outInfo.newLog(0,s);
        
    }
    
    // Simple log method
    public void log(int p, String s){
        
        outInfo.newLog(p,s);
        
    }
  

    
    public void run() 
    {
          
        try {  
          
              // get the i/o streams
              DataInputStream in = clientConnection.openDataInputStream();
              DataOutputStream out = clientConnection.openDataOutputStream();                                                    
              
              // cicle until clients sends OVER or -1 detected
              while ( !message.equalsIgnoreCase(OVER) ) {
                                   
               // read the message
               try {    
                        log(4,"Reading String");
                        // get the string message
                        message = in.readUTF();
                        log(4,"Message received = " + message);
                        processMsg(message,clientUUID);
                                                    
                 } catch (IOException e) {
                     
                        log(2,"IOException launched read");                       
                        break;
                 }
               
                // Them send the "ROGER" to the client so he knows
                try {
                    
                         log(4,"Writing ROGER...");
                         out.writeUTF(ROGER);                                    
                            
                } catch (IOException ioE) {
                                
                          log(2,"ERROR Writing ROGER:"+ioE.getMessage());
                          ioE.printStackTrace();
                          break;
                                    
                 }
               
               }
                    
               
               // let's clean the streams'              
               log(4,"Closing i/o Streams");

               in.close();
               out.close();

            } catch (IOException e) {
                
                log(3,"General IOException: " + e.getMessage());
                e.printStackTrace();
                
            } finally {
                
                // close the BT connection
                if (clientConnection != null) {
                    try {
                        
                        log(4,"Closing bt conn");
                        clientConnection.close();
                        
                    } catch (IOException e) {
                        
                        log(2,"ERROR:closing conn:"+e.getMessage());
                        e.printStackTrace();
                        
                    }
                }
            }
    }   
    
   
    
    private void processMsg(String msg,String id){
        
        DiABluID dID = new DiABluID(id,"");
        
        if (msg.startsWith("K")) {
                          
            // Parse the keys
            String[] keys = msg.substring(1).split("\\|",2);
            
            log(4,"Keypressed:"+keys[0]);
            log(4,"Game Action:"+keys[1]);
            
            // Create the DiABluKey
            DiABluKey dbK = new DiABluKey(dID,keys[0],keys[1]);
            
            // Send the key
            outInfo.newKey(dbK);
            
        } else {
            
            // Message without the M            
            createMsg(msg.substring(1),id);
        
        }
        
    }
    
    private void createMsg(String msg,String id){
        
        DiABluID dID = new DiABluID(id,"");
        DiABluMsg dM = new DiABluMsg(dID,msg);
        outInfo.newMsg(dM);
        
    }
    
}   