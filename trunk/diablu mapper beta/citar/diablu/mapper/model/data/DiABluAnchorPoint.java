/*
 * DiABluAnchorPoint.java
 *
 * Created on 9 de Setembro de 2006, 17:16
 *
 * Copyright (C) 9 de Setembro de 2006 Nuno Rodrigues
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

package citar.diablu.mapper.model.data;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluAnchorPoint implements Comparable {
    
    private String uuid;
    private String fname;
    private String deviceClass;
    
    /** Creates a new instance of DiABluAnchorPoint */
    public DiABluAnchorPoint() {
        
        uuid="[none yet]";
        fname="[none yet]";
        deviceClass="[none yet]";
        
    }
    
    public DiABluAnchorPoint(String uuid,String fname){
        
        this.uuid = uuid;
        this.fname = fname;
        this.deviceClass = "Mapped Device";
        
    }
    
    public DiABluAnchorPoint(String uuid,String fname,String deviceClass){
        
        this.uuid = uuid;
        this.fname = fname;
        this.deviceClass = deviceClass;        
        
    }
    
    
    public void setUUID(String uuid){
        
        this.uuid = uuid;
    
    }
    
    public String getUUID(){
        
        return this.uuid;
    
    }
    
    public void setFname(String fname){
        
        this.fname = fname;
        
    }
    
    public String getFname(){
        
        return this.fname;
        
    }
    
    public String getDeviceClass(){
        
        return this.deviceClass;
        
    }
    
    public void setDeviceClass(String deviceClass){
        
        this.deviceClass = deviceClass;
        
    }
    
    /**
     * This method implements the Comparable interface and compares 
     * this DiABlu Anchor Point with another one supplied as a parameter. 
     * It uses the UUID as comparable parameter
     *
     */
    public int compareTo(Object anotherAnchorPoint) throws ClassCastException {                  
    
        // first:make sure it's a DiABlu Device to compare with'
        if (!(anotherAnchorPoint instanceof DiABluAnchorPoint)) {
                    
             ClassCastException castError = new ClassCastException("Anchor Point expected");              
             throw castError;
        } 
        DiABluAnchorPoint other = (DiABluAnchorPoint) anotherAnchorPoint;
        
        if (this.getUUID().equalsIgnoreCase(other.getUUID())) { 
            return 0; 
        } else return -1;
        
    }

}
