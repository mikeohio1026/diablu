/*
 * DiABluBC.java
 *
 * Created on 24 de Abril de 2006, 16:19
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

package citar.diablu.bc;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.table.*;
import citar.diablu.classes.*;
import citar.diablu.com.interfaces.INWatcher;
import citar.diablu.com.interfaces.OUTWatcher;
import citar.diablu.gui.DiABluUI;
import citar.diablu.com.osc.DiABluOSC;
import citar.diablu.com.bt.DiABluBTDeviceDiscovery;
import citar.diablu.com.bt.*;

/**
 *
 * @author nrodrigues
 */
public class DiABluBC implements INWatcher {
    
    /* DiABlu OSC Class */
    static DiABluOSC DBosc = null;
    
    /* DiABlu Graphical User Interface Class */
    static DiABluUI DBui = null;
    
    /* DiABlu BT class */
    static DiABluBTDeviceDiscovery DBbt = null;
    
    /* Delay between inquirys */
    int DELAY_INQUIRY = 0;
    
    private Vector<DiABluDevice> deviceList;
    
    
    /**
     *    INWatcher Interface Methods
     *
     */
    /**
     *    newLog() Method
     *
     *    Priority assigns a diferent final output
     *    0 - Simple Information(Default)
     *    1 - Alert.Input Error
     *    2 - Communications Error
     *    3 - Serious Error.Exception found.
     *    4 - Detailed info (debug purposes)
     *    5 - Security Error
     */
    public void newLog(int priority, String log){
        
        // Command line log
        if (priority==0) {
            
            // get the Time
            Calendar timeStamp = Calendar.getInstance();
            String dayLog = new Integer(timeStamp.get(Calendar.DAY_OF_MONTH)).toString();
            String monthLog = new Integer(timeStamp.get(Calendar.MONTH)+1).toString();
            String yearLog = new Integer(timeStamp.get(Calendar.YEAR)).toString();
            String hourLog = new Integer(timeStamp.get(Calendar.HOUR_OF_DAY)).toString();
            String minuteLog = new Integer(timeStamp.get(Calendar.MINUTE)).toString();
            String secondLog = new Integer(timeStamp.get(Calendar.SECOND)).toString();
            String timeLog = "["+dayLog+"/"+monthLog+"/"+yearLog+"@"+hourLog+":"+minuteLog+":"+secondLog+"]";
            System.out.println(timeLog+log);
            
            
            
            
        }
        
        
        
        DBui.newLog(priority,log);
        
    }
    
    
    /**
     * New Device List Arrived
     * Now we compare with the old one and notice the changes if any
     * We got to keep an eye for :
     * exited devices
     * found devices
     * devices with changed friendly names
     * and fire these events accordally as well as a new device count
     *
     * TODO:further improve this method, it should be possible to reduce
     * the target list while searching and finding
     */
    public void newDeviceList(Vector <DiABluDevice>newDeviceList, int type){
        
        Vector <DiABluDevice>finalDeviceList = new Vector<DiABluDevice>();   // The final list with all the devices
        Vector <DiABluDevice>namesChangedList = new Vector<DiABluDevice>();  // The list with the devices that have changed their friendly name
        Vector <DiABluDevice>devicesOutList = new Vector<DiABluDevice>();    // The list with the exited devices
        Vector <DiABluDevice>devicesInList = new Vector<DiABluDevice>();     // The list with the newly discovered devices
        Vector <DiABluDevice>oldDeviceList = new Vector<DiABluDevice>();     // Our last device list of the argument type
        Vector <DiABluDevice>entireOldDeviceList = new Vector<DiABluDevice>();  // Our entire last device list present
        //Vector stripedDeviceList = new Vector(); // Temporary list without any simulated devices
        
        DiABluDevice dbDevNew = null;
        DiABluDevice dbDevOld = null;
        int newSize = 0;
        int oldSize = 0;
        boolean newDevice;                      // represents a new entered device(true)
        boolean someDeviceMatch;                // represents some type of match
        int compareFactor = 0;
        int stripedSize=0;
        String uuidT="";
        String fnameT="";
        DiABluDevice ddTemp=null;
        int oldCount = deviceList.size(); //DBui.getDiABluList().size();  // pseudo-flag that check's if there's the need to update the counter
        boolean updateList = false;   // flag that check's if there's the need to update the list
        
        
        
        // if we don't have any mobile devices around...
        if (newDeviceList.size()==0||newDeviceList==null) {
            newLog(0,"No devices found.");
            
            // get the entire old DeviceList & counter
            entireOldDeviceList = deviceList;//DBui.getDiABluList();
            
            //oldCount = entireOldDeviceList.size();
            
            // crop the old list
            oldDeviceList = cropList(type,entireOldDeviceList);
            newLog(4,"[DiABluBC-newDeviceList()]*Old device list with "+oldDeviceList.size()+" elements");
            
            // all other type elements remain in the final device list
            finalDeviceList = stripList(type,entireOldDeviceList);
            newLog(4,"[DiABluBC-newDeviceList()] Final device list with "+finalDeviceList.size()+" elements");
            
        } else {
            
            newLog(4,"Found "+newDeviceList.size()+" devices.");
            // get the entire old DeviceList
            entireOldDeviceList = deviceList; //DBui.getDiABluList();
            newLog(4,"[DiABluBC-newDeviceList()]Got the entire old device list with "+entireOldDeviceList.size()+" elements");
            
            // chop our specific old device list
            oldDeviceList = cropList(type,entireOldDeviceList);
            newLog(4,"[DiABluBC-newDeviceList()]Last list contained "+oldDeviceList.size()+" elements");
            
            // all other type elements remain in the final device list
            finalDeviceList = stripList(type,entireOldDeviceList);
            
            // check if the list contains any elements, otherwise we only need to add the new list
            if (oldDeviceList.size()==0){
                newLog(4,"[DiABluBC-newDeviceList()]Adding the entire new list...");
                
                for (int j=0;j<newDeviceList.size();j++) {
                    // add the device to the devicesin list
                    devicesInList.addElement(newDeviceList.elementAt(j));
                    
                    // add the device to the final list
                    finalDeviceList.addElement(newDeviceList.elementAt(j));
                }
                
            } else {
                // get the list's size's
                newSize = newDeviceList.size();
                
                // do a compare for each of the newly discovered devices
                for (int i=0;i<newSize;i++){
                    
                    // reset the flags
                    newDevice = true;
                    someDeviceMatch = false;
                    
                    // get the first comparable element
                    dbDevNew = (DiABluDevice) newDeviceList.elementAt(i);
                    newLog(4,"[DiABluBC-newDeviceList()]Checking element:"+i+" of a total of "+newSize);
                    newLog(4,"[DiABluBC-newDeviceList()]FName:"+dbDevNew.getID().getFName()+"@"+dbDevNew.getID().getUUID());
                    
                    // get the old list actual size
                    oldSize = oldDeviceList.size();
                    
                    for (int j=0;j<oldSize;j++){
                        
                        // get the second comparable element
                        dbDevOld = (DiABluDevice) oldDeviceList.elementAt(j);
                        
                        // compare the two
                        compareFactor = dbDevNew.compareDB(dbDevOld);
                        
                        if ( compareFactor==1 ) {
                            
                            // The device is still in da house :P
                            newLog(4,"[DiABluBC-newDeviceList()]Device "+
                                    dbDevNew.getID().getUUID()+" is in dah house:)");
                            newDevice = false;
                            
                            // add it to the final list
                            finalDeviceList.addElement(dbDevNew);
                            
                            // remove it from the old list
                            oldDeviceList.removeElementAt(j);
                            
                            // since we've found a match we don't need to do more search
                            break;
                            
                        } else if ( compareFactor==2 ) {
                            
                            // The device is present but has changed the name
                        /* newLog(0,"NameChanged:["+
                                dbDevNew.getID().getUUID()+"]["+
                                dbDevOld.getID().getFName()+"] TO ["+
                                dbDevNew.getID().getFName()+"]");
                         */
                            newDevice = false;
                            
                            // Add it to changed names list
                            namesChangedList.addElement(dbDevNew);
                            
                            // Add it to the final list
                            finalDeviceList.addElement(dbDevNew);
                            
                            // Remove it from the old list
                            oldDeviceList.removeElementAt(j);
                            
                            // since we've found a match we don't need to do more search
                            break;
                            
                        } else {
                            newLog(4,"[DiABluBC-newDeviceList()]No match found.Continue searching...");
                        }
                    }
                    
                    // Check if it's a new device
                    if (newDevice == true) {
                    /*
                    newLog(0,"DeviceIn:["
                             +dbDevNew.getID().getUUID()+"]["+dbDevNew.getID().getFName()+"]");
                     */
                        // add it to the devicesin list
                        devicesInList.addElement(dbDevNew);
                        
                        // add it to the final list
                        finalDeviceList.addElement(dbDevNew);
                    }
                    
                    newLog(4,"[DiABluBC-newDeviceList()]Finished searching for "+
                            dbDevNew.getID().getFName()+"@"+dbDevNew.getID().getUUID());
                }
                
            }
            newLog(4,"[DiABluBC-newDeviceList()]Finished Checking Arrived List.Processing results.Final list size:"+finalDeviceList.size());
            // Finished the searching
            // It's time to colect and send the data
        }
        // get the target address
        InetSocketAddress addr = DBui.getSocketAddress();
        
        deviceList =finalDeviceList;
        // /device(s)out
        if ( oldDeviceList.size() != 0 ) {
            int totalRemoved = oldDeviceList.size();
            newLog(4,"[DiABluBC-newDeviceList()]Removing "+totalRemoved+" elements");
            
                /*
                for (int i=0;i<totalRemoved;i++){
                 
                    DiABluDevice removedDevice = (DiABluDevice) oldDeviceList.elementAt(i);
                    newLog(0,"DeviceOut:["+removedDevice.getID().getUUID()+"]["+removedDevice.getID().getFName()+"]");
                 
                }
                 */
            sendRemoveDevices(oldDeviceList, addr);
            updateList = true;
        }
        
        // /device(s)in
        if ( devicesInList.size() != 0 ) {
            
            newLog(4,"[DiABluBC-newDeviceList()]Adding "+devicesInList.size()+" elements");
            sendAddDevices(devicesInList,addr);
            updateList = true;
        }
        
        // /nameschanged
        if ( namesChangedList.size() != 0 ){
            
            newLog(4,"[DiABluBC-newDeviceList()]"+namesChangedList.size()+" elements have changed their names");
            sendNamesChanged(namesChangedList,addr);
            updateList = true;
        }
        
        // devicelist
        if ( finalDeviceList.size() != 0 ) {
            
            newLog(4,"[DiABluBC-newDeviceList()]Sending device list information");
            
            if (updateList && finalDeviceList.size()>0) sendDeviceList(finalDeviceList, addr);
        }
        
        // devicecount
        if (oldCount!=finalDeviceList.size()) {
            newLog(4,"[DiABluBC-newDeviceList()]Old Count:"+oldCount+"New Count:"+finalDeviceList.size()+"Updating OSC Listener");
            sendDeviceCount(finalDeviceList.size(),addr);
            
        }
        
    }
    
    /**
     * THis method strips a DiABlu Devices Vector from a given status
     * E.g. stripList(0) will return a DiABlu Devices Vector without any
     * device with the zero status, this is useful in order to eliminate
     * the simulated ones
     * TODO:improve the method in order to accept a collection of status
     * DEPRECATED ? - shouldn't the crop method be sufficient enough in a
     * simpler newDeviceList()
     */
    public Vector<DiABluDevice> stripList(int stripStatus, Vector oldList){
        
        DiABluDevice dbTemp = null;         // temporary DiABlu Device
        Vector <DiABluDevice>stripedList = new Vector<DiABluDevice>();  // our return list
        int tempSize;                       // size of the list supplied as argument
        
        // for the paranoid.if it receives a null list will bounce back
        if (oldList==null) {
            newLog(3,"[DiABluBC-stripList()]Received a null list!");
            return null;
        }
        // get the list size
        tempSize = oldList.size();
        newLog(4,"[DiABluBC-stripList()]Stripping list with "+tempSize+" elements");
        
        // cicle the list and add diferent status elements to the result
        for (int i=0;i<tempSize;i++){
            
            dbTemp = (DiABluDevice) oldList.elementAt(i);
            newLog(4,"[DiABluBC-stripList()]Element:"+dbTemp.getID().toString()+"Status:"+dbTemp.getStatus());
            if (dbTemp.getStatus() != stripStatus) stripedList.addElement(dbTemp);
            
        }
        newLog(4,"[DiABluBC-stripList()]Returning stripped list with "+stripedList.size()+" elements");
        return stripedList;
    }
    
    /**
     * THis method crops from DiABlu Devices Vector all elements
     * with a given status
     * E.g. cropList(0) will return a DiABlu Devices Vector only with
     * devices with the zero status, this is useful in order to get
     * only the simulated ones
     */
    
    public Vector<DiABluDevice> cropList(int cropStatus, Vector <DiABluDevice>oldList){
        
        int tempSize = oldList.size();          // length of the argument list
        DiABluDevice dbTemp = null;             // temporary DD
        Vector <DiABluDevice>croppedList = new Vector<DiABluDevice>();      // result list returned
        
        // for the paranoid.if it receives a null list will bounce back
        if ( oldList == null && oldList.size() != 0 ){
            
            newLog(1,"[DiABluBC-cropList()]Null or empty list received.Returning...");
            return oldList;
        }
        
        newLog(4,"[DiABluBC-cropList()]Crop Method entered.List size:"+oldList.size()+"Crop Factor:"+cropStatus);
        
        
        for (int i=0;i<tempSize;i++){
            
            //newLog(0,"[DiABluBC-cropList()]Checking element:"+i+"of a total of "+tempSize);
            dbTemp =  oldList.elementAt(i);
            newLog(4,"[DiABluBC-cropList()]FName:"+dbTemp.getID().getFName()+"UUID:"+dbTemp.getID().getUUID()+"Major:" +dbTemp.getMajorDeviceClassString()+ "Status:"+dbTemp.getStatus());
            if (dbTemp.getStatus() == cropStatus) croppedList.addElement(dbTemp);
            
        }
        newLog(4,"[DiABluBC-cropList()]Exiting & returning list with "+croppedList.size()+" elements");
        return croppedList;
    }
    
    /**
     *  OUTWatcher Interface Methods
     *
     */
    
    public void sendAddDevices(Vector <DiABluDevice>aDevices, InetSocketAddress addr){
        
        if (aDevices==null||aDevices.size()==0){
            newLog(3,"[DiABluBC-sendAddDevices()] in DiABluBC has received an empty vector");
            return;
        }
        
        newLog(4,"[DiABluBC-sendAddDevices()]Outputing a list of "+aDevices.size()+" DiAblu Devices.");
        
        int totalAdded = aDevices.size();
        for (int i=0;i<totalAdded;i++){
            
            DiABluDevice addedDevice = (DiABluDevice) aDevices.elementAt(i);
            newLog(0,"DeviceIn:["+addedDevice.getID().getUUID()+"]["+addedDevice.getID().getFName()+"]");
            
        }
        
        DBui.addDevices(aDevices);
        DBosc.sendAddDevices(aDevices,addr);
        
    }
    
    /**
     * @todo Should build the list of removed devices based on devices in the device list to ensure proper minor, major..
     */
    public void sendRemoveDevices(Vector <DiABluDevice>rDevices, InetSocketAddress addr){
        
        if (rDevices == null || rDevices.size()==0){
            newLog(3,"[DiABluBC-sendRemoveDevices()]This method has received an empty vector");
            return;
        }
        newLog(4,"[DiABluBC-sendRemoveDevices()]Removing "+rDevices.size()+" devices...");
        
        
        int totalRemoved = rDevices.size();
        for (int i=0;i<totalRemoved;i++){
            
            DiABluDevice removedDevice = (DiABluDevice) rDevices.elementAt(i);
            newLog(0,"DeviceOut:["+removedDevice.getID().getUUID()+"]["+removedDevice.getID().getFName()+"]");
            /* we need to remove it from the device list */
            if (deviceList.contains(removedDevice)) {
                deviceList.remove(removedDevice);
                
            }
        }
        
        DBui.removeDevices(rDevices);
        DBosc.sendRemoveDevices(rDevices,addr);
        
    }
    
    public void sendDeviceList(Vector <DiABluDevice>lDevices, InetSocketAddress addr){
        
        if (lDevices.size()==0){
            newLog(3,"[DiABluBC-sendDeviceList()] in DiABluBC has received an empty vector");
            return;
        }
        newLog(4,"[DiABluBC-sendDeviceList()]Sending Device List("+lDevices.size()+")");
        //DBui.newDeviceList(lDevices);
        DBosc.sendDeviceList(lDevices,addr);
    }
        /*
    public void sendMsg(DiABluMsg newDMsg, InetSocketAddress addr) {
         
       newLog(0,"MessageIn:["+newDMsg.getID().getUUID()+"]["+newDMsg.getID().getFName()+"]["
               +newDMsg.getText()+"]");
       newLog(4,"[DiABluBC-sendMsg()]Message Contents:"+newDMsg.getText());
         
       DBui.updateLastMsg(newDMsg);//TODO:update UI table's info on Last Message'
       DBosc.sendMsg(newDMsg, addr);
    }*/
    /*
    public void sendKeys(DiABluKey newDKey, InetSocketAddress addr){
     
        newLog(0,"KeyIn:["+newDKey.getID().getUUID()+"]["+newDKey.getID().getFName()+"]["
                  +newDKey.getKeyPressed()+"]["+newDKey.getGAction()+"]");
     
        DBui.updateLastKey(newDKey); //TODO:update UI table's info on Last Key'
        DBosc.sendKeys(newDKey,addr);
    }*/
    
    public void sendDeviceCount(int dCount, InetSocketAddress addr){
        
        newLog(4,"DeviceCount:["+dCount+"]");
        DBosc.sendDeviceCount(dCount,addr);
        
    }
    
    public void sendNamesChanged(Vector nDevices, InetSocketAddress addr){
        
        // paranoid check :P
        if (nDevices==null || nDevices.size()==0){
            newLog(3,"[DiABluBC-sendNamesChanged()] in DiABluBC has received an empty vector");
            return;
        }
        
        // log
        int totalChanged = nDevices.size();
        for (int i=0;i<totalChanged;i++){
            
            DiABluDevice changedDevice = (DiABluDevice) nDevices.elementAt(i);
            newLog(0,"NameChanged:["+changedDevice.getID().getUUID()+"]["+changedDevice.getID().getFName()+"]");
            
        }
        
        DBui.editDevices(nDevices);
        DBosc.sendNamesChanged(nDevices,addr);
    }
    
    /**
     * Prints found exceptions
     */
    private static void printException( Exception e ) {
        
        System.err.println( e.getClass().getName() + " : " + e.getLocalizedMessage() );
    }
    
    /**
     * This method receives a simple string and sends it to /messagein
     */
    public String sendText(String text, InetSocketAddress addr) {
        
        String logMessage="[SEND TEXT][OSC /messagein]:"+text;
        //sendMessage(MESSAGEIN,text,addr);
        return logMessage;
        
    }
    
    public void searchDevices() {
        
        DBbt = null;
        
        /* DiABlu Bluetooth Class */
        try {
            DBbt = new DiABluBTDeviceDiscovery(this);
        } catch (Exception e){
            System.out.println(e);
        };
        
    }
    
    public int getBTdelay(){
        
        return DBui.getBTdelay();
        
    }
 /*DEPRECATED
    public void reSearch(){
  
      System.out.println("[DiABluBC]REStarting BT device discovery service...");
  
    }
  */
    /**
     * Creates a new instance of DiABluBC
     */
    public DiABluBC(String args[]) {
        
        // Get our ui
        DBui = new DiABluUI(this);
        //String sName = DBui.getServiceName();
        boolean showWindow = true;
        
        // Process the args if any
        int cla = args.length;
        if ( cla > 0 ){
            
            String commandLineParameter = "";
            String temp = "";
            for (int i=0;i<cla;i++){
                
                
                temp = args[i];
                if (temp.equalsIgnoreCase("/?")||temp.equalsIgnoreCase("/help")) {
                    
                    displayCommandLineHelp();
                    
                } else if (temp.equalsIgnoreCase("hide")){
                    
                    showWindow = false;
                    
                } else {
                    
                    String commandLineParameters[] = temp.split(":",3);
                    temp = commandLineParameters[0];
                    
                    // validate the command line parameter
                    if (commandLineParameters.length==2) {
                        
                        
                        if (temp.equalsIgnoreCase("servicename")) {
                            
                            // sName = commandLineParameters[1];
                            //DBui.setServiceName(sName);
                            
                            
                        } else if (temp.equalsIgnoreCase("port")){
                            
                            DBui.setPort(commandLineParameters[1]);
                            
                            
                        } else if (temp.equalsIgnoreCase("address")){
                            
                            DBui.setTargetAddress(commandLineParameters[1]);
                            
                            
                            
                        } else if (temp.equalsIgnoreCase("btdelay")){
                            
                            DBui.setBTdelay(commandLineParameters[1]);
                            
                        }
                        
                        
                    } else {
                        
                        System.out.println("Invalid command:"+temp);
                        System.out.println("Type /help or /? for more help");
                        
                    }
                }
                
            }
            
        }
        
        // check if the user want to see the window
        if (showWindow) { DBui.setVisible(true); }
        
        deviceList = new Vector<DiABluDevice>();
        // Get our osc
        DBosc = new DiABluOSC();
        
        
        //System.out.println("Calling Server Service Class");
        
        
        
        // System.out.println("[DiABluBC]Calling service provider class...");
        // DiABluBTServer dBTs = new DiABluBTServer(this,sName);
        //dBTs.start();
        //DiABluBTrfcommJSR82b DBspp = new DiABluBTrfcommJSR82b();
        //DBspp.start();
        //System.out.println("[DiABluBC]Called service registration...");
        
        // System.out.println("[DiABluBC]Starting BT device discovery service...");
        
        // display current values
        System.out.println("Target Address = "+DBui.getTargetAddress());
        System.out.println("Target Port = "+DBui.getPort());
        System.out.println("Delay between Bluetooth Discoverys = "+DBui.getBTdelay());
        //System.out.println("Service Name = "+DBui.getServiceName());
        
        /* DiABlu Bluetooth Class */
        try {
            DBbt = new DiABluBTDeviceDiscovery(this);
        } catch (Exception e){
            System.out.println(e);
        };
        
        //DiABluBTspp DBspp = new DiABluBTspp();
        //new Thread(DBspp).start();
        //DiABluBTl2cap DBl2cap = new DiABluBTl2cap();
        //new Thread(DBl2cap).start();
        
    }
    
    
    // This method prints on the console information on the command line parameters
    public void displayCommandLineHelp() {
        
        System.out.println("DiABlu Server 1");
        System.out.println("---------------");
        System.out.println("Command usage:");
        System.out.println("/help or /? for this screen");
        System.out.println("");
        System.out.println("hide - This option hides the Graphics User Interface from displaying");
        System.out.println("btdelay:xxxx - This specifies the Bluetooth Device Discovery delay in xxxx seconds");
        System.out.println("servicename:xxxxx - This specifies the Bluetooth Service Server Name to xxxxx");
        System.out.println("address:xxx.xxx.xxx.xxx - Specify the Target Machine Address to xxx.xxx.xxx.xxx");
        System.out.println("port:xxxx - Specify the Target Machine Listening Port to xxxx");
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // TODO: Parse the ARGS
        // ASK JC THE ARGS
        
        // TODO: Put a help situation /?
        
        
        DiABluBC mainDiABlu = new DiABluBC(args);
    }
    
    
    
    
}
