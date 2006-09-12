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


/**
 *
 * @author Nuno Rodrigues
 */
public class dbBTclient {
    
    // Keeps the connection string in case we want multiple connections
    private String serverConnectionString = "";
    
    // Our connection objects
    StreamConnection con = null;
    DataOutputStream out_server=null;
    DataInputStream in_server=null;
            
    // Simple connection status flag
    private boolean isServerOpen = false;
    private boolean isStreamOpen = false;
    
    // Out output GUI
    private msgMain parent = null;
    
    /** Creates a new instance of dbBTclient */
    public dbBTclient(String serverURL,msgMain dtParent) {
        
        this.serverConnectionString = serverURL;
        this.parent = dtParent;
        isServerOpen = false;
    
    }
    
    public msgMain getParent() {
        
        return this.parent;
        
    }
    
    public void log(int priority,String mlog){
        
        parent.log(priority,"[btc]"+mlog);
        
    }
      
    
    public void sendMsg(String msg) {
        

      String tMsg="M"+msg;
      String ROGER = "ROGER";
      String OVER = "OVER";
      String serverACK = "";
    
               
      byte[] inBuffer = new byte[256];
      int length=0;
               
      int checkServer = openServer();  
       
      if ( isServerOpen && checkServer == 0 ){
        try {
                int checkStreams = openStreams();
                if (isStreamOpen && checkStreams == 0) {
                               
                    out_server.writeUTF(tMsg);
                    log(4,"W OK");
                    
                    out_server.flush();
                    log(4,"F OK");
                    
                /* let's see what the server has for us'
                String serverACK = "";           
                length=in_server.read(inBuffer);           
                if (length!=-1) {serverACK=new String (inBuffer,0,length);}
                 */
           
                    while (!serverACK.equalsIgnoreCase(ROGER)){
                          
                        try {
                            log(4,"RServer");
                            serverACK = in_server.readUTF();
                                        
                        } catch (IOException ioE) {
               
                            log(2,"Err-reading server ACK");
                            break;
               
                        }
                    }
                    
                   } else {
                   
                    // Something went wrong
                    // We couldn't open the streams'                   
                    log(4,"Nothing done");  
                    
                   }
                   
                   closeStreams();
                                                                                                
           
           } catch (IOException e3){
               
                log(2,"Error3:"+e3.toString());
           
            }
           
            closeServer();        
      
        } else {
              
               log(2,"Connection Down.No data socket");
               
        }
                
    } 
    
      public int openServer() {
        
        try {
           
               /*
                * Open the connection to the server
                */
            
                log(0,"Server Conn");
                String tempURL=new String(serverConnectionString);
                
                if (!isServerOpen) {
                  
                    log(0,"URL:"+serverConnectionString);                
                    con =(StreamConnection) Connector.open(tempURL);
                    isServerOpen = true;
                    
                } else {
                    
                    log(2,"Trying to open an already open connection");
                    
                }
                
  
        } catch (IllegalArgumentException ie){
            
                log(3,"Error:IllegalArgument:"+ie.getMessage());
                return -1;
                
        } catch (ConnectionNotFoundException ce){
                
                log(2,"ConnectionNotFound:"+ce.getMessage());
                return -1;
                
        } catch (SecurityException se){
                
                log(5,"SecurityException:"+se.getMessage());
                return -1;
                
        } catch (IOException e2) {
                    
                log(3,"IOError:"+e2.getMessage());
                log(0,"Proceeding anyway...");
                // TODO:Check this condition as some authors accept to continue
        }
        
        return 0;
        
    }
    
    public int openStreams() {
        
        if ( isServerOpen ) {
         
   
                    // everything ok
                    // let's open our data streams'
                    log(4,"I/o streams");  
                    if ( !isStreamOpen ) {                    
                         
                        try {
                        
                            out_server = con.openDataOutputStream();
                            log(4,"Open DOS OK");
                    
                            in_server = con.openDataInputStream();
                            log(4,"Open DIS OK");
                    
                            isStreamOpen = true;                    
                            
                        } catch (IOException ioE){
                            
                            log(2,"Error Opening Streams");
                            return -1;
                        }
                
                    } else {
                    
                    log(2,"Stream Already OPEN");
                    return -1;
                }
        
            
        } else {
            
            // server not open(?)
            // let's try to open the server'
            int i = openServer();
            
            if ( i == 0 ) {
                
                // recall the method
                log(4,"RecallIOStreams");
                i = openStreams();
                // TODO:Check if this is the second time
                
            } else {
                
                // Error ocurred opening server.No server open!So no streams:(
                log(2,"NoOpenServer!");
                return -1;
                
            }
        }
        
        log(4,"Streams Open");
        return 0;
    }
    
    public void closeStreams() {
        
        boolean inputStreamClosed = false;
        boolean outputStreamClosed = false;
        
        if (isStreamOpen) {
           
            try {
                
                    log(4,"Closing data i/o");
                    in_server.close();
                    log(4,"InputStream Closed OK");
                    inputStreamClosed = true;
                    
            } catch (IOException ioE) {
                   
                     log(2,"Error closing IS:"+ioE.getMessage());
                     
            }
            
            try {
                    
                    out_server.close();
                    log(4,"OutputStream Closed OK");
                    outputStreamClosed = true;
                
            } catch (IOException ioE) {
                
                    log(2,"Error closing OS:"+ioE.getMessage());
                    
            }
            
            // check if both connections were properly closed
            if (inputStreamClosed && outputStreamClosed) {        
                
                isStreamOpen = false;
                
            }       
                    
                    
        } else {
            
            log(2,"Streams not open");
            
        }
    }
    
    public void closeServer() {
        
        if ( !isServerOpen ) {
            
            log(2,"SRV not Open!");
            return;
            
        } else {
            
           /*
            * Close all resources
            */
            try {
                              
                 con.close();
                 log(4,"[CL SRV]");  
                 isServerOpen = false;
           
            } catch (IOException e4){
          
                 log(2,"Error[CL]SRV"+e4.toString());
               
            }                                                       
           
        }
        
       log(0,"Data sended OK");
       return;
       
    }           
    
    public int checkComm() {
        
        // TODO: A method that checks the current state of connections
        return -1;
        
    }
            
    public int sendKeys (String KeysMsg) {                       
         
         try {
         
             out_server.writeUTF(KeysMsg);
             log(4,"W OK");
                    
             out_server.flush();
             log(4,"F OK");
        
         } catch (IOException ioE) {
             
             log(2,"Error sendind:"+ioE.getMessage());
             return -1;
             
         }
       
       
         return 0;  
        /* TODO:Comm checker or put another method with this checking
        // check for server and stream state
        if ( isServerOpen ) {
            
            if ( isStreamOpen ) {
                
                // everything ok let's send the data
                
            } else {
            
                
            
            }*/
        
        }
        
    
}

