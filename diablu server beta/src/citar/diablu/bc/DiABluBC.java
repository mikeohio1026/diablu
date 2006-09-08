/*
 * DiABluBC.java
 *
 * Created on 24 de Abril de 2006, 16:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
import citar.diablu.com.bt.tests.DiABluBTrfcommJSR82b;

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
        
        DBui.newLog(priority,log);
        
    }

    public void newLog(String log) {
    
        DBui.newLog(log);
    }
     
    // New Key Pressed
    public void newKey(DiABluKey newKey) {
        
        // log the action
        DBui.newLog(0,"[DiABluBC-newKey()]New Key arrived from "+newKey.getID().toString()+
                       "("+newKey.getID().getUUID()+")KeyPressed:"+newKey.getKeyPressed()+
                        " Game Action:"+newKey.getGAction());
        
        // get the target address
        InetSocketAddress addr = DBui.getSocketAddress();
        
        // forward the key
        sendKeys(newKey,addr);   
        
    }
    
    // New Message Arrived
    public void newMsg(DiABluMsg newMsg){
        
        // log the action
        DBui.newLog(0,"[DiABluBC-newMsg()]New Message arrived from "+newMsg.getID().getFName()+
                       "("+newMsg.getID().getUUID()+")Text:"+newMsg.getText());
        
        // Parse the msg
        
        // get the target address
        InetSocketAddress addr = DBui.getSocketAddress();
        
        // forward the msg
        sendMsg(newMsg,addr);
        
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
    
    public void newDeviceList(Vector newDeviceList, int type){
                       
        Vector finalDeviceList = new Vector();   // The final list with all the devices
        Vector namesChangedList = new Vector();  // The list with the devices that have changed their friendly name
        Vector devicesOutList = new Vector();    // The list with the exited devices       
        Vector devicesInList = new Vector();     // The list with the newly discovered devices
        Vector oldDeviceList = new Vector();     // Our last device list of the argument type
        Vector entireOldDeviceList = new Vector();  // Our entire last device list present
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
        int oldCount = DBui.getDiABluList().size();  // pseudo-flag that check's if there's the need to update the counter
        boolean updateList = false;   // flag that check's if there's the need to update the list                
        
        // if we don't have any mobile devices around...
        if (newDeviceList.size()==0||newDeviceList==null) {
            newLog(4,"[DiABluBC-newDeviceList()]Empty or null list received!!");
            
            // get the entire old DeviceList & counter
            entireOldDeviceList = DBui.getDiABluList();
            //oldCount = entireOldDeviceList.size();
            
            // crop the old list 
            oldDeviceList = cropList(type,entireOldDeviceList);
            newLog(4,"[DiABluBC-newDeviceList()]*Old device list with "+oldDeviceList.size()+" elements");
            
             // all other type elements remain in the final device list
            finalDeviceList = stripList(type,entireOldDeviceList); 
            newLog(4,"[DiABluBC-newDeviceList()] Final device list with "+finalDeviceList.size()+" elements");
            
        } else {
            
            newLog(0,"[DiABluBC-newDeviceList()]New Device List Found("
                      +newDeviceList.size()+"elements)");            
                
            // get the entire old DeviceList
            entireOldDeviceList = DBui.getDiABluList();        
            newLog(4,"[DiABluBC-newDeviceList()]Got the entire old device list with "+entireOldDeviceList.size()+" elements");
        
            // chop our specific old device list
            oldDeviceList = cropList(type,entireOldDeviceList);
            newLog(4,"[DiABluBC-newDeviceList()]Last list contained "+oldDeviceList.size()+" elements");
                        
            // all other type elements remain in the final device list
            finalDeviceList = stripList(type,entireOldDeviceList);   
        
            // check if the list contains any elements, otherwise we only need to add the new list
            if (oldDeviceList.size()==0){
                newLog(4,"[DiABluBC-newDeviceList()]Adding the entire new list...");
            
                for (int j=0;j<newDeviceList.size();j++)
                {
                    // add the device to the devicesin list
                    devicesInList.addElement(newDeviceList.elementAt(j));
                
                    // add the device to the final list
                    finalDeviceList.addElement(newDeviceList.elementAt(j));
                }
            
            }
            else
            {                              
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
                        newLog(4,"[DiABluBC-newDeviceList()]Device "+
                                dbDevNew.getID().getUUID()+"has changed his name from "+
                                dbDevOld.getID().getFName()+" to "+
                                dbDevNew.getID().getFName());                      
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
                    
                    newLog(4,"[DiABluBC-newDeviceList()]New device found:"
                             +dbDevNew.getID().getFName()+"@"+dbDevNew.getID().getUUID());
                    
                    // add it to the devicesin list
                    devicesInList.addElement(dbDevNew);
                    
                    // add it to the final list
                    finalDeviceList.addElement(dbDevNew);
                }
            
                newLog(4,"[DiABluBC-newDeviceList()]Finished searching for "+
                        dbDevNew.getID().getFName()+"@"+dbDevNew.getID().getUUID());
            }
        
        }
        newLog(0,"[DiABluBC-newDeviceList()]Finished Checking Arrived List.Processing results.Final list size:"+finalDeviceList.size());
        // Finished the searching
        // It's time to colect and send the data
    }   
       // get the target address
       InetSocketAddress addr = DBui.getSocketAddress();
       
        // /device(s)out
        if ( oldDeviceList.size() != 0 ) {
                
                newLog(4,"[DiABluBC-newDeviceList()]Removing "+oldDeviceList.size()+" elements");
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
    public Vector stripList (int stripStatus, Vector oldList){
                
        DiABluDevice dbTemp = null;         // temporary DiABlu Device
        Vector stripedList = new Vector();  // our return list
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
    
    public Vector cropList (int cropStatus, Vector oldList){
        
        int tempSize = oldList.size();          // length of the argument list
        DiABluDevice dbTemp = null;             // temporary DD
        Vector croppedList = new Vector();      // result list returned
          
        // for the paranoid.if it receives a null list will bounce back
        if ( oldList == null && oldList.size() != 0 ){
            
            newLog(1,"[DiABluBC-cropList()]Null or empty list received.Returning...");
            return oldList;
        }        
        
        newLog(4,"[DiABluBC-cropList()]Crop Method entered.List size:"+oldList.size()+"Crop Factor:"+cropStatus);
        
      
        for (int i=0;i<tempSize;i++){
            
            //newLog(0,"[DiABluBC-cropList()]Checking element:"+i+"of a total of "+tempSize);
            dbTemp = (DiABluDevice) oldList.elementAt(i);
            newLog(4,"[DiABluBC-cropList()]FName:"+dbTemp.getID().getFName()+"UUID:"+dbTemp.getID().getUUID()+"Status:"+dbTemp.getStatus());
            if (dbTemp.getStatus() == cropStatus) croppedList.addElement(dbTemp);
            
        }
        newLog(4,"[DiABluBC-cropList()]Exiting & returning list with "+croppedList.size()+" elements");
        return croppedList;
    }
    
    /**
     *  OUTWatcher Interface Methods
     *
     */
        
    public void sendAddDevices(Vector aDevices, InetSocketAddress addr){
        
        if (aDevices.size()==0){
            newLog(3,"[DiABluBC-sendAddDevices()] in DiABluBC has received an empty vector");
            return;
        }
        newLog(0,"[DiABluBC-sendAddDevices()]Outputing a list of "+aDevices.size()+" DiAblu Devices.");
        DBui.addDevices(aDevices);
        DBosc.sendAddDevices(aDevices,addr);
        
    }
    
    public void sendRemoveDevices(Vector rDevices, InetSocketAddress addr){
        
        if (rDevices.size()==0){
            newLog(3,"[DiABluBC-sendRemoveDevices()]This method has received an empty vector");
            return;
        }
        newLog(0,"[DiABluBC-sendRemoveDevices()]Removing "+rDevices.size()+" devices...");
        DBui.removeDevices(rDevices);
        DBosc.sendRemoveDevices(rDevices,addr);
        
    }
    
    public void sendDeviceList(Vector lDevices, InetSocketAddress addr){
                
        if (lDevices.size()==0){
            newLog(3,"[DiABluBC-sendDeviceList()] in DiABluBC has received an empty vector");
            return;
        }
        newLog(0,"[DiABluBC-sendDeviceList()]Sending Device List("+lDevices.size()+")");
        //DBui.newDeviceList(lDevices);
        DBosc.sendDeviceList(lDevices,addr);
    }
        
    public void sendMsg(DiABluMsg newDMsg, InetSocketAddress addr) {
        
       newLog(4,"[DiABluBC-sendMsg()]Sending msg from:"+newDMsg.getID().toString());
       newLog(4,"[DiABluBC-sendMsg()]Message Contents:"+newDMsg.getText());
        
       DBui.updateLastMsg(newDMsg);//TODO:update UI table's info on Last Message'
       DBosc.sendMsg(newDMsg, addr);
    }
    
    public void sendKeys(DiABluKey newDKey, InetSocketAddress addr){
        
        newLog(4,"[DiABluBC-sendKeys()]Sending Keys from:"+newDKey.getID().getUUID()+"("+newDKey.getID().getFName()+")");
        newLog(4,"[DiABluBC-sendKeys()]Key Pressed:"+newDKey.getKeyPressed()+"|Game Action:"+newDKey.getGAction());
        DBui.updateLastKey(newDKey); //TODO:update UI table's info on Last Key'
        DBosc.sendKeys(newDKey,addr);
    }
    
    public void sendDeviceCount(int dCount, InetSocketAddress addr){
        
        newLog(4,"[DiABluBC-sendDeviceCount()]Sending new device count:"+dCount);
        DBosc.sendDeviceCount(dCount,addr);
        
    }
    
    public void sendNamesChanged(Vector nDevices, InetSocketAddress addr){
        
        // paranoid check :P
        if (nDevices==null || nDevices.size()==0){
            newLog(3,"[DiABluBC-sendNamesChanged()] in DiABluBC has received an empty vector");
            return;
        }
        newLog(4,"[DiABluBC-sendNamesChanged()]List size:"+nDevices.size());
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
 
    public void reSearch(){      
      
      System.out.println("[DiABluBC]REStarting BT device discovery service...");                  
      
    }
    
    /**
     * Creates a new instance of DiABluBC
     */
    public DiABluBC() {
    
      // Get our ui
      DBui = new DiABluUI(this);
      // Get our osc
      DBosc = new DiABluOSC(); 
      
      
      System.out.println("Calling Server Service Class");
     
      String sName = DBui.getServiceName();             

      System.out.println("[DiABluBC]Calling service provider class...");
      DiABluBTServer dBTs = new DiABluBTServer(this,sName);
      dBTs.start();
      //DiABluBTrfcommJSR82b DBspp = new DiABluBTrfcommJSR82b();
      //DBspp.start();
      System.out.println("[DiABluBC]Called service registration...");
      
      
      
      
      
      
      System.out.println("[DiABluBC]Starting BT device discovery service...");
      
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
    
   /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // TODO: Parse the ARGS
        // ASK JC THE ARGS
        
        // TODO: Put a help situation /?      
         
            DiABluBC mainDiABlu = new DiABluBC();                        
                          
       
    }
    
    
}
