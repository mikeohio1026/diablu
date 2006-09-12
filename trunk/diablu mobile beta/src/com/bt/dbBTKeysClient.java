/*
 * dbBTclient.java
 *
 * Created on 29 mai 2006, 14:44
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
    
   