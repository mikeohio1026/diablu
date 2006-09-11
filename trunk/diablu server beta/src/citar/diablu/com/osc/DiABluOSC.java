
/*
 * DiABluOSC.java
 *
 * Created on 11 de Maio de 2006, 16:11
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
 *
 */

package citar.diablu.com.osc;

import java.util.Vector;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

import citar.diablu.com.interfaces.OUTWatcher;
import citar.diablu.classes.*;

import de.sciss.net.*;


/**
 *
 * @author nrodrigues
 */
public class DiABluOSC {
    
    /* OSC Messaging Variable List */
    static int DELAY = 500;                          // Delay between sendings
    
    /**
     * OSC Commands used
     */
    String DEVICEIN="/DeviceIn";                    // New Device entered
    String DEVICESIN="/DeviceListIn";               // Newly entered devices //ss ss ss
    String DEVICEOUT="/DeviceOut";                  // Single Device exited
    String DEVICESOUT="/DeviceListOut";             // Multiple Devices out //ss ss ss
    String MESSAGEIN="/MessageIn";                  // Text Message sended
    String KEYIN="/KeyIn";                          // Key pressed from device
    String DEVICECOUNT="/DeviceCount";              // Number of devices present
    String DEVICELIST="/DeviceList";                // Actual list of devices present //ss ss ss
    String NAMECHANGED="/NameChanged";              // Device's Friendly Name has changedOSC
    
    /** Creates a new instance of DiABluOSC */
    public DiABluOSC() {
        
    }
    
    
    /**
     * This method outputs the added devices present in the Vector
     * to 2 OSC commands /devicein & /devicesin
     * /devicein sends an OSC Bundle with all the devices
     * /devicesin sends a Single OSC Message with all the devices concatenated in
     *
     */
    public void sendAddDevices(Vector aDevices, InetSocketAddress addr){
        
        int totalDevicesAdded = 0;  // size of the list
        String[][] tempAddDevices1 = null;
        Object[] tempAddDevices2 = null;
        
        // paranoid check
        if (aDevices == null || aDevices.size() == 0){
            System.out.println("[DiABluOSC-sendAddDevices()]Error!Received an empty list!!");
            return;
        }
        
        // get the vector's size'
        totalDevicesAdded = aDevices.size();
        
        tempAddDevices1 = new String[totalDevicesAdded][totalDevicesAdded]; // devicein
        tempAddDevices2 = new Object[totalDevicesAdded];                    // devicesin
        
        // Convert the info
        tempAddDevices1 = vectorTOstring(aDevices);
        tempAddDevices2 = vectorTOobject(aDevices);
        
        // Send it...
        
        // OSC /devicein
        sendBundle("/devicein",tempAddDevices1,addr);
        
        // OSC /devicesin
        sendMessage("/devicesin",tempAddDevices2,addr);
        
    }
    
    /**
     * This method outputs the removed devices present in the Vector
     * to 2 OSC commands /deviceout & /devicesout
     * /deviceout sends an OSC Bundle with all the devices
     * /devicesout sends a Single OSC Message with all the devices concatenated in
     *
     */
    public void sendRemoveDevices(Vector rDevices, InetSocketAddress addr){
        
        int totalDevicesRemoved = rDevices.size();
        String[][] tempRemoveDevices1 = new String[totalDevicesRemoved][totalDevicesRemoved];
        Object[] tempRemoveDevices2 = new Object[totalDevicesRemoved];
        
        // Convert the info
        tempRemoveDevices1 = vectorTOstring(rDevices);
        tempRemoveDevices2 = vectorTOobject(rDevices);
        
        // Send it...
        
        // OSC /deviceout
        sendBundle(DEVICEOUT,tempRemoveDevices1,addr);
        
        // OSC /devicesout
        sendMessage(DEVICESOUT,tempRemoveDevices2,addr);
        
    }
    
    /**
     * This method sends the list of all devices detected by the DB System
     * to the OSC commands /devicelist
     *
     */
    public void sendDeviceList(Vector lDevices, InetSocketAddress addr){
        
        // first verify that we've got some info
        if (lDevices != null) {
            
            int totalDevicesFound = lDevices.size();
            //Object[] tempListDevices = new Object();
            
            // Convert the info
            Object[] tempListDevices = vectorTOobject(lDevices);
            
            // OSC /devicelist
            sendMessage(DEVICELIST,tempListDevices,addr);
        } else return;
        
    }
    
    public void sendMsg(DiABluMsg newDMsg, InetSocketAddress addr){
        
        // get the sender's related ID
        String uuidT = newDMsg.getID().getUUID();
        String fnameT = newDMsg.getID().getFName();
        
        // get the message body
        String textT = newDMsg.getText();
        
        // convert the info
        Object[] oscMsg = new Object[] { uuidT, fnameT, textT } ;
        
        // send the message
        sendMessage(MESSAGEIN,oscMsg,addr);
        
    }
    
    public void sendKeys(DiABluKey newDKey, InetSocketAddress addr){
        
        // get the sender's related id'
        String uuidT = newDKey.getID().getUUID();
        String fnameT = newDKey.getID().getFName();
        
        // get the keys
        String keypressedT = newDKey.getKeyPressed();
        String gactionT = newDKey.getGAction();
        
        // convert the info
        Object [] oscMsg = new Object[] { uuidT, fnameT, keypressedT, gactionT };
        
        // send the keys
        sendMessage(KEYIN,oscMsg,addr);
        
    }
    
    public void sendNamesChanged(Vector nDevices, InetSocketAddress addr){
        
        // paranoid :P
        if ( nDevices == null || nDevices.size() == 0 ) {
            
            System.out.println("[DiABluOSC-sendNamesChanged()]Names changed error!!!Null vector found!!");
            return;
        }
        
        int totalNamesChanged = nDevices.size();
        
        System.out.println("[DiABluOSC-sendNamesChanged()]Vector size:"+totalNamesChanged);
        
        DiABluDevice dbT = null;
        String uuidT = "";
        String fnameT = "";
        Object[] oscMsg = null;
        
        for (int i=0;i<totalNamesChanged;i++){
            
            // get the ID
            dbT = (DiABluDevice) nDevices.get(i);
            uuidT = dbT.getID().getUUID();
            fnameT = dbT.getID().getFName();
            
            // convert the info
            oscMsg = new Object[] { uuidT, fnameT };
            
            // send it...
            sendMessage(NAMECHANGED,oscMsg,addr);
        }
        
    }
    
    /**
     *   Send's the total of devices present in the DiABlu System
     */
    public void sendDeviceCount(int dCount, InetSocketAddress addr){
        
        sendMessage(DEVICECOUNT,dCount,addr);
        
    }
    
    /**
     *  This method converts a vector of DiABlu Devices into a String[][] array
     *  The returned array only contains the DiABlu's ID's (UUID & FName) ready to be sent
     *  by the sendMessage() method.
     *  TODO:Validation for an empty list
     */
    private String[][] vectorTOstring(Vector xDevices) {
        
        String uuidT = "";
        String fnameT = "";
        DiABluDevice ddT;
        int tempSize = xDevices.size();
        String[][] tempDBstring = new String[tempSize][2];
        
        for (int i=0; i<tempSize; i++){
            
            // get the device
            ddT = (DiABluDevice) xDevices.get(i);
            
            // get the device info
            uuidT = ddT.getID().getUUID();
            fnameT = ddT.getID().getFName();
            
            // copy the device info
            tempDBstring[i][0]=uuidT;
            tempDBstring[i][1]=fnameT;
            
        }
        
        return tempDBstring;
    }
    
    /**
     *  This method converts a vector of DiABlu Devices into a Object[] array
     *  The returned array only contains the DiABlu's ID's (UUID & FName) ready to be sent
     *  by the sendMessage() method.
     *  TODO:Further refine the checking of an empty or null vector
     */
    private Object[] vectorTOobject(Vector xDevices){
        
        String uuidT = "";                 // temporary uuid
        String fnameT = "";                // temporary friendly name
        DiABluDevice ddT;                  // temporary Diablu Device
        int tempSize = 0;                  // size of arg vector
        int tempCounter = 0;               // counter for the returned Object[]
        Object[] tempDBobject = null;      // returned array
        
        if ( xDevices == null || xDevices.size() == 0 ) {
            
            System.out.println("[DiABluOSC-vectorTOobject()]ERROR!!vectorTOobject Method has received an empty list!!");
            return null;
            
        }
        
        // get the size
        tempSize = xDevices.size();
        
        // initialize the returned array which has 2x nº elements of the incoming list
        tempDBobject = new Object[2*tempSize];
        
        // debug info
        System.out.println("[DiABluOSC-vectorTOobject()]List size:"+xDevices.size());
        
        for (int i=0; i<tempSize; i++){
            
            // get the device
            ddT = (DiABluDevice) xDevices.get(i);
            
            // get the device info
            uuidT = ddT.getID().getUUID();
            fnameT = ddT.getID().getFName();
            
            // copy the device info
            tempDBobject[tempCounter] = uuidT;
            tempDBobject[tempCounter+1] = fnameT;
            tempCounter+=2;
            
        }
        
        // debug info
        System.out.println("[DiABluOSC-vectorTOobject()]Returned list size:"+tempDBobject.length);
        
        return tempDBobject;
        
        
    }
    
    
    /**
     * This method sends a Bundle of several DiABlu Devices
     * Each device is sended on the form
     * [UUID][FName][Key Pressed(if any)][gAction(if any)][text(if any)]
     *
     */
    
    public void sendBundle( String oscCommand, String[][] brutus , InetSocketAddress addr ){
        
        final long serverLatency = 50;
        final long now = System.currentTimeMillis() + serverLatency;
        
        DatagramChannel dch = null;
        OSCBundle bundle;
        OSCTransmitter trns;
        
        
        try {
            dch = DatagramChannel.open();
            dch.configureBlocking(true);
            trns = new OSCTransmitter(dch, addr);
            bundle = new OSCBundle(now);
            
            for ( int i = 0; i < brutus.length ; i++ ){
                
                System.out.println("[DiABluOSC-sendBundle()]Processing packets...["+brutus[i][0]+"]["+brutus[i][1]+"]\n");
                Object[] objT = new Object[] { brutus[i][0] , brutus[i][1] };
                bundle.addPacket( new OSCMessage(oscCommand, objT));
                
            }
            
            /* Sending message */
            System.out.print("Sending [OSC Bundle:" + oscCommand + "] | [BRUTUS]\n");
            trns.send(bundle, addr);
        }
        
        catch( Exception e1 ) {
            // TODO: catch this exception
            // printException( e1 );
        } finally {
            if( dch != null ) {
                try {
                    dch.close();
                } catch( Exception e2 ) {
                    // TODO: catch this exception
                    // printException( e2 );
                };
            }
        }
    }
    
    /**
     * Sends a simple osc message
     */
    public static void sendMessage( String oscCommand, String message, InetSocketAddress addr ){
        
        DatagramChannel dch = null;
        OSCTransmitter trns;
        
        try {
            dch = DatagramChannel.open();
            trns = new OSCTransmitter(dch, addr);
            
            /* Sending message */
            System.out.print("Sending Message...OSC:" + oscCommand + " | Content:" + message + "\n");
            trns.send( new OSCMessage( oscCommand, new Object[] {
                message
            }
            ),addr);
        }
        
        catch( Exception e1 ) {
            
            // TODO: catch this exception
            // printException( e1 );
        } finally {
            if( dch != null ) {
                try {
                    dch.close();
                } catch( Exception e2 ) {
                    
                    // TODO: catch this exception
                    //printException( e2 );
                };
            }
        }
        
    }
    
    /**
     * Sends a composite Object[] message
     */
    public static void sendMessage( String oscCommand, Object[] brutus , InetSocketAddress addr ){
        
        
        DatagramChannel dch = null;
        OSCTransmitter trns;
        
        try {
            dch = DatagramChannel.open();
            trns = new OSCTransmitter(dch, addr);
            
            
            /* Sending message */
            System.out.print("Sending [OSC Command:" + oscCommand + "] | [List Size:"+brutus.length+"]\n");
            trns.send( new OSCMessage( oscCommand, brutus
                    ),addr);
        }
        
        catch( Exception e1 ) {
            //printException( e1 );
        } finally {
            if( dch != null ) {
                try {
                    dch.close();
                } catch( Exception e2 ) {
                    
                    //printException( e2 );
                };
            }
        }
    }
    
    /**
     * Sends simple OSC message with a [int]
     * Suited for /devicecount
     */
    public static void sendMessage( String oscCommand, int num , InetSocketAddress addr ){
        
        DatagramChannel dch = null;
        OSCTransmitter trns;
        
        try {
            dch = DatagramChannel.open();
            trns = new OSCTransmitter(dch, addr);
            
            
            /* Sending message */
            System.out.print("Sending [OSC Command:" + oscCommand + "] | [Count:"+num+"]\n");
            trns.send( new OSCMessage( oscCommand, new Object[] {
                new Integer(num)
            }
            ),addr);
        }
        
        catch( Exception e1 ) {
            //TODO:Handle this exception
            //printException( e1 );
        } finally {
            if( dch != null ) {
                try {
                    dch.close();
                } catch( Exception e2 ) {
                    //TODO:Handle this exception
                    //printException( e2 );
                };
            }
        }
    }
    
}
