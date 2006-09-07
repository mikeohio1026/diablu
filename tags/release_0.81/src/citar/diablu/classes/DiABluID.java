/*
 * DiABluID.java
 *
 * Created on 12 de Maio de 2006, 15:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.classes;

import javax.bluetooth.*;

/**
 *
 * @author nrodrigues
 */
public class DiABluID {
    
    String UUID;
    String FName;
    
    /** Creates a new instance of DiABluID */
    public DiABluID() {
        
        UUID = "";
        FName = "";
        
    }
    
    public DiABluID(RemoteDevice remoteDev){
        
        this.UUID = remoteDev.getBluetoothAddress();
        
        try {
            
            // TODO: WARNING sometimes this method blocks the DiscoveryAgent
            this.FName = remoteDev.getFriendlyName(true);
            
        } catch (Exception e){
            
            System.out.println("Exception getting friendly name");
            
        }
        
    }
    public DiABluID(String newUUID, String newFName){
        
        UUID = newUUID;
        FName = newFName;
    }
    
    /**
     * Returns the Device's Friendly name if avaiable, otherwise returns it's UUID
     */
    public String toString() {
        
        if (this.FName.equalsIgnoreCase("")|this.FName.equalsIgnoreCase("[none yet]")) {
            return this.UUID;
        } else {
            return this.FName;
        }
    }
    // getters & setters
    public void setUUID(String newUUID){
        
        this.UUID=newUUID;        
                
    }
    
    public String getUUID(){
        
        return this.UUID;
    }
    
    public void setFName(String newFName){
        
        this.FName = newFName;
    }
    
    public String getFName() {
        
        if (this.FName.equalsIgnoreCase("")){
            
            return "[none yet]";
        } else {
            
            return this.FName;
        }
    }

}
