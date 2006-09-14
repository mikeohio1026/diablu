/*
 * DiABluID.java
 *
 * Created on 12 de Maio de 2006, 15:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.data;

import javax.bluetooth.*;

/**
 *
 * @author nrodrigues
 */
public class DiABluID implements Comparable {
    
    private String uuid;            // uuid
    private String fName;           // friendly name
    private int vCounter;           // Verify cycles counter
    
    /** Creates a new instance of DiABluID */
    public DiABluID() {
        
        uuid = "";
        fName = "";
        vCounter = 0;
        
    }

    
    public DiABluID(String newUUID, String newfName){
        
        this.uuid = newUUID;
        this.fName = newfName;
        this.vCounter = 0;
        
    }
    
    /**
     * Returns the Device's Friendly name if avaiable, otherwise returns it's UUID
     */
    public String toString() {
        
        if (this.fName.equalsIgnoreCase("")|this.fName.equalsIgnoreCase(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[none_yet]"))) {
            return this.uuid;
        } else {
            return this.fName;
        }
    }
    // getters & setters
    public void setUUID(String newUUID){
        
        this.uuid=newUUID;        
                
    }
    
    public String getUUID(){
        
        return this.uuid;
    }
    
    public void setFName(String newfName){
        
        this.fName = newfName;
    }
    
    public String getFName() {
        
        if (this.fName.equalsIgnoreCase("")){
            
            return java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[none_yet]");
        } else {
            
            return this.fName;
        }
    }
    
    public int getVCounter(){
        return this.vCounter;
    }
    public void setVCounter(int vC){
        this.vCounter = vC;
    }
    public void incrementVCounter(){
        this.vCounter++;
    }
    
    // NOTE:We don't use the full spec.Only need to know if equal.'
    public int compareTo(Object anotherId) throws ClassCastException {                  
    
        // first:make sure it's a DiABlu Device to compare with'
        if (!(anotherId instanceof DiABluID)) {
                    
             throw new ClassCastException("DiABlu ID Expected!Got:"+anotherId.toString());  
             
        } 
       
        // second:compare the uuid's
        if (this.uuid.equalsIgnoreCase((((DiABluID)anotherId).getUUID()))) {
            
            return 0;
            
        } else {
            
            return -1;
            
        } 
        
    }

        
    // DEPRECATED
    public DiABluID(RemoteDevice remoteDev){
        
        this.uuid = remoteDev.getBluetoothAddress();
        
        try {
            
            // TODO: WARNING sometimes this method blocks the DiscoveryAgent
            // It's always better to use the other constructor
            this.fName = remoteDev.getFriendlyName(true);
            
        } catch (Exception e){
            
            System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Exception_getting_friendly_name"));
            
        }
        
    }
}
