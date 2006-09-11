/*
 * DiABluBTConnection.java
 *
 * Created on 14 juin 2006, 12:21
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

import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;
import citar.diablu.com.interfaces.INWatcher;
import citar.diablu.classes.DiABluMsg;
import citar.diablu.classes.DiABluID;
import citar.diablu.classes.DiABluKey;

import citar.diablu.com.bt.DiABluBTCONSTANTS.*;

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
    DiABluBTCONSTANTS mKeys;
    
    /** Creates a new instance of DiABluBTConnection */
    public DiABluBTConnection (StreamConnection conn,INWatcher out, String clientID){
      
        this.clientConnection = conn;        
        this.clientUUID = clientID;
        this.outInfo = out;
                  
    } 
    
    // DEPRECATED
    public void log(String s){
        
        outInfo.newLog(4,s);
        
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
            
            // process the keys
            String keyPressed = getKeyJavaString(keys[0]);
            String gACtion = getKeyJavaString(keys[1]);
            
            // Create the DiABluKey
            DiABluKey dbK = new DiABluKey(dID,keys[0],keys[1]);
            
            // Send the key
            outInfo.newKey(dbK);
            
        } else {
            
            // Message without the M            
            createMsg(msg.substring(1),id);
        
        }
        
    }
    /**
     * This method returns the corresponding String of a given String(int(keycode))
     * The constants values used by this method are defined in the DiABluBTCONSTANTS
     * TODO:use enum(?) instead of if-then-else
     *
     */
    private String getKeyJavaString(String keyCode){
        
        String javaString = "unknowned";
        // convert the string
      try {
            
        int kC = Integer.parseInt(keyCode);    
                
        if (kC==mKeys.DOWN){ javaString = mKeys.SDOWN; } else
            if (kC==mKeys.FIRE){ javaString = mKeys.SFIRE; } else
                if (kC==mKeys.GAME_A){ javaString = mKeys.SGAME_A; } else
                    if (kC==mKeys.GAME_B){ javaString = mKeys.SGAME_B; } else
                        if (kC==mKeys.GAME_C){ javaString = mKeys.SGAME_C; } else
                            if (kC==mKeys.GAME_D){ javaString = mKeys.SGAME_D; } else
                                if (kC==mKeys.KEY_NUM0){ javaString = mKeys.SKEY_NUM0; } else                                   
                                        if (kC==mKeys.KEY_NUM1){ javaString = mKeys.SKEY_NUM1; } else
                                            if (kC==mKeys.KEY_NUM2){ javaString = mKeys.SKEY_NUM2; } else
                                                if (kC==mKeys.KEY_NUM3){ javaString = mKeys.SKEY_NUM3; } else
                                                    if (kC==mKeys.KEY_NUM4){ javaString = mKeys.SKEY_NUM4; } else
                                                        if (kC==mKeys.KEY_NUM5){ javaString = mKeys.SKEY_NUM5; } else
                                                            if (kC==mKeys.KEY_NUM6){ javaString = mKeys.SKEY_NUM6; } else
                                                                if (kC==mKeys.KEY_NUM7){ javaString = mKeys.SKEY_NUM7; } else
                                                                    if (kC==mKeys.KEY_NUM8){ javaString = mKeys.SKEY_NUM8; } else
                                                                        if (kC==mKeys.KEY_NUM9){ javaString = mKeys.SKEY_NUM9; } else
                                                                            if (kC==mKeys.KEY_STAR){ javaString = mKeys.SKEY_STAR; } else
                                                                                if (kC==mKeys.KEY_POUND){ javaString = mKeys.SKEY_POUND; } 
                                                    
                                                                        
        } catch (Exception e){
            log(3,"BTConnection:Error parsing key:"+keyCode);
        }
        return javaString;
    }
    
    private void createMsg(String msg,String id){
        
        DiABluID dID = new DiABluID(id,"");
        DiABluMsg dM = new DiABluMsg(dID,msg);
        outInfo.newMsg(dM);
        
    }
    
}   