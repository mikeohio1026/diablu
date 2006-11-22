/*
 * DiABluServerFlosc.java
 *
 * Created on 11 de Setembro de 2006, 11:39
 *
 * Copyright (C) 11 de Setembro de 2006 Nuno Rodrigues
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

package citar.diablu.server.model.com.flosc;

import java.util.Vector;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.InetAddress;

// DiABlu System
import citar.diablu.server.model.data.*;
import citar.diablu.server.controller.out.osc.DiABluServerOSCModelListener;
import citar.diablu.server.controller.in.DiABluServerModelControllerListener;
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerFlosc implements DiABluServerOSCModelListener {
    
    private int targetPort;
    private String targetAddress;
    private static Logger logger = Logger.getLogger(LOG_MAIN_NAME);   
    private DiABluServerFloscTcpServer dsftp;
    
    /** Creates a new instance of DiABluServerFlosc */
    public DiABluServerFlosc(String targetAddress,int targetPort) {
        
        
        this.targetAddress = targetAddress;
        this.targetPort = targetPort;
        
        // Rise the TCP server
        dsftp = new DiABluServerFloscTcpServer(targetPort);
        dsftp.start();
        
    }
    
    public void newDeviceList (Vector <DiABluDevice> updatedDiABlusList){
    
        logger.finest("Sending new device list...");
        // paranoid check
        if (updatedDiABlusList==null){
            
            logger.warning("Null argument");
            return;
            
        }
    
        // make sure there's work to be done'
        if (updatedDiABlusList.isEmpty()){
            
            logger.warning("Empty list");
            return;
            
        }
        
        logger.finest("Creating message...");
        
        
        // OSC Message
        DiABluServerFloscOscMessage dsfom = new DiABluServerFloscOscMessage(OSC_DEVICE_LIST);
        
        // OSC Packet
        DiABluServerFloscOscPacket dsfop = new DiABluServerFloscOscPacket();
        
        logger.finest("Creating packet....");
        // create the osc packet that will hold all the info
        for (DiABluDevice dd:updatedDiABlusList){
            
                logger.finest("Adding Device:"+dd.getID().toString());
                dsfom.addArg((Character)"s".charAt(0),(Object)dd.getID().getUUID());
                dsfom.addArg((Character)"s".charAt(0),(Object)dd.getID().getFName());                       
            
        }
        
        logger.finest("Broadcasting...");
        dsfop.addMessage(dsfom);
        dsfop.setPort(targetPort);
        try {
        dsfop.setAddress(InetAddress.getByName(this.targetAddress));
        } catch (Exception e){
            
            logger.finest("Error with loc");
        }
        dsftp.broadcastMessage(dsfop.getXml());
    
    }
    
    public void newDeviceCount(int newDiABlusCount){
    
        // OSC Message
        DiABluServerFloscOscMessage dsfom = new DiABluServerFloscOscMessage(OSC_DEVICE_COUNT);
        // Format with content
        dsfom.addArg((Character)"i".charAt(0),(Object)Integer.valueOf(newDiABlusCount));
                
        // OSC Packet
        DiABluServerFloscOscPacket dsfop = new DiABluServerFloscOscPacket();
        // Add the created message
        dsfop.addMessage(dsfom);
        dsfop.setPort(targetPort);
        
        try {
            
            dsfop.setAddress(InetAddress.getByName(this.targetAddress));
            
        } catch (Exception e){
            
            logger.warning("Error with target address:"+targetAddress);
            
        }
    
        logger.finest("Broadcasting device count:"+newDiABlusCount);
        dsftp.broadcastMessage(dsfop.getXml());
    
    
    
    
    }
        
    /* 
     * DiABlu Device's
     *
     */   
    public void newDiABluDevices (Vector <DiABluDevice> addDiABlus){
    
        // paranoid check
        if (addDiABlus==null){
            
            logger.warning("Null argument");
            return;
            
        }
        
        // make sure we have something to do
        if (addDiABlus.isEmpty()){
            
            logger.warning("Empty list");
            return;
            
        }
            
        // OSC Message
        DiABluServerFloscOscMessage dsfom = new DiABluServerFloscOscMessage(OSC_DEVICE_IN);
        
        // Format with content
        for (DiABluDevice dd:addDiABlus){
            
            logger.finest("Flosc ["+OSC_DEVICE_IN+"]:"+dd.getID().toString());
            dsfom.addArg((Character)"s".charAt(0),(Object)dd.getID().getUUID());    
            dsfom.addArg((Character)"s".charAt(0),(Object)dd.getID().getFName());
            
        }
              
        // OSC Packet
        DiABluServerFloscOscPacket dsfop = new DiABluServerFloscOscPacket();
        // Add the created message
        dsfop.addMessage(dsfom);
        dsfop.setPort(targetPort);
            
        try {
            
            dsfop.setAddress(InetAddress.getByName(this.targetAddress));
            
        } catch (Exception e){
            
            logger.warning("Error with target address:"+targetAddress);
            
        }
    
        logger.finest("Broadcasting:"+addDiABlus.size()+" new devices");
        dsftp.broadcastMessage(dsfop.getXml());
    
    }
    
    public void editDiABluDevices (Vector <DiABluDevice> editDiABlus){
    
           // paranoid check
        if (editDiABlus==null){
            
            logger.warning("Null argument");
            return;
            
        }
        
        // make sure we have something to do
        if (editDiABlus.isEmpty()){
            
            logger.warning("Empty list");
            return;
            
        }
            
        // OSC Message
        DiABluServerFloscOscMessage dsfom = new DiABluServerFloscOscMessage(OSC_NAME_CHANGED);
        
        // Format with content
        for (DiABluDevice dd:editDiABlus){
            
            logger.finest("Flosc ["+OSC_NAME_CHANGED+"]:"+dd.getID().toString());
            dsfom.addArg((Character)"s".charAt(0),(Object)dd.getID().getUUID());    
            dsfom.addArg((Character)"s".charAt(0),(Object)dd.getID().getFName());
            
        }
              
        // OSC Packet
        DiABluServerFloscOscPacket dsfop = new DiABluServerFloscOscPacket();
        // Add the created message
        dsfop.addMessage(dsfom);
        dsfop.setPort(targetPort);
            
        try {
            
            dsfop.setAddress(InetAddress.getByName(this.targetAddress));
            
        } catch (Exception e){
            
            logger.warning("Error with target address:"+targetAddress);
            
        }
    
        logger.finest("Broadcasting:"+editDiABlus.size()+" changed devices");
        dsftp.broadcastMessage(dsfop.getXml());                    
    
    }
    
    public void removeDiABluDevices (Vector <DiABluDevice> removeDiABlus){
    
        // paranoid check
        if (removeDiABlus==null){
            
            logger.warning("Null argument");
            return;
            
        }
        
        // make sure we have something to do
        if (removeDiABlus.isEmpty()){
            
            logger.warning("Empty list");
            return;
            
        }
            
        // OSC Message
        DiABluServerFloscOscMessage dsfom = new DiABluServerFloscOscMessage(OSC_DEVICE_OUT);
        
        // Format with content
        for (DiABluDevice dd:removeDiABlus){
            
            logger.finest("Flosc ["+OSC_DEVICE_OUT+"]:"+dd.getID().toString());
            dsfom.addArg((Character)"s".charAt(0),(Object)dd.getID().getUUID());    
            dsfom.addArg((Character)"s".charAt(0),(Object)dd.getID().getFName());
            
        }
              
        // OSC Packet
        DiABluServerFloscOscPacket dsfop = new DiABluServerFloscOscPacket();
        // Add the created message
        dsfop.addMessage(dsfom);
        dsfop.setPort(targetPort);
            
        try {
            
            dsfop.setAddress(InetAddress.getByName(this.targetAddress));
            
        } catch (Exception e){
            
            logger.warning("Error with target address:"+targetAddress);
            
        }
    
        logger.finest("Broadcasting:"+removeDiABlus.size()+" removed devices");
        dsftp.broadcastMessage(dsfop.getXml());                    
    
    
    
    
    }
    
    public void newMsg (DiABluMsg newMsg){
    
        if (newMsg == null){
            
            logger.warning("Null argument");
            return;
            
        }
        
        // OSC Message
        DiABluServerFloscOscMessage dsfom = new DiABluServerFloscOscMessage(OSC_MESSAGE_IN);
        // Add the data
        dsfom.addArg((Character)"s".charAt(0),(Object)newMsg.getID().getUUID());    
        dsfom.addArg((Character)"s".charAt(0),(Object)newMsg.getID().getFName());
        dsfom.addArg((Character)"s".charAt(0),(Object)newMsg.getText());
    
        // OSC Packet
        DiABluServerFloscOscPacket dsfop = new DiABluServerFloscOscPacket();
        // Add the created message
        dsfop.addMessage(dsfom);
        dsfop.setPort(targetPort);
            
        try {
            
            dsfop.setAddress(InetAddress.getByName(this.targetAddress));
            
        } catch (Exception e){
            
            logger.warning("Error with target address:"+targetAddress);
            
        }
    
        logger.finest("Broadcasting message:"+newMsg.getText()+" from "+newMsg.getID().toString());
        dsftp.broadcastMessage(dsfop.getXml());  
        
    }
    
    public void newKey (DiABluKey newKey){
    
            if (newKey == null){
            
            logger.warning("Null argument");
            return;
            
        }
        
        // OSC Message
        DiABluServerFloscOscMessage dsfom = new DiABluServerFloscOscMessage(OSC_MESSAGE_IN);
        // Add the data
        dsfom.addArg((Character)"s".charAt(0),(Object)newKey.getID().getUUID());    
        dsfom.addArg((Character)"s".charAt(0),(Object)newKey.getID().getFName());
        dsfom.addArg((Character)"s".charAt(0),(Object)newKey.getKeyPressed());
        dsfom.addArg((Character)"s".charAt(0),(Object)newKey.getGAction());
    
        // OSC Packet
        DiABluServerFloscOscPacket dsfop = new DiABluServerFloscOscPacket();
        // Add the created message
        dsfop.addMessage(dsfom);
        dsfop.setPort(targetPort);
            
        try {
            
            dsfop.setAddress(InetAddress.getByName(this.targetAddress));
            
        } catch (Exception e){
            
            logger.warning("Error with target address:"+targetAddress);
            
        }
    
        logger.finest("Broadcasting Keys:"+newKey.getKeyPressed()+"|"+newKey.getGAction()+" from "+newKey.getID().toString());
        dsftp.broadcastMessage(dsfop.getXml());  
        
        
    }
    
    /* 
     * Settings
     */
    
    // Global    
    public void setResourceBundle(ResourceBundle rb){
    
    }
    
    // Input
    // TODO:Multiple Protocol
   
    // Output
    // Protocol OpenSoundControl
    public void setTargetAddress(String targetURL){
    
        this.targetAddress = targetURL;
        
    }
    
    public void setTargetPort(String targetPort){
    
        this.targetPort = Integer.parseInt(targetPort);
        
    }
    
    public void stopServer(){
        
        dsftp.killServer();
    
    }
}
