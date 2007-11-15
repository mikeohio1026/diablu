/*
 * DiABluID.java
 *
 * Created on 12 de Maio de 2006, 15:38
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
