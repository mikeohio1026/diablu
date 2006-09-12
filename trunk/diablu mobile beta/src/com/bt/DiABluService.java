/*
 * DiABluService.java
 *
 * Created on 29 mai 2006, 15:13
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

import javax.bluetooth.*;

/**
 *
 * @author Nuno Rodrigues
 */

public class DiABluService {
    
    private String stringConnectionURL="";
    private String serviceName="";
    private ServiceRecord[] brutus;
    
    /** Creates a new instance of DiABluService */
    public DiABluService(String URL,String Name) {
        
        this.stringConnectionURL = URL;
        this.serviceName = Name;
        this.brutus = new ServiceRecord[1];
        brutus[0]=null;
        
    }
    
    public DiABluService(){
        
        this.stringConnectionURL="";
        this.serviceName="";
    }
    public String getServiceName() {
        
        return this.serviceName;
    }
    
    public String getStringConnectionURL() {
        
        return this.stringConnectionURL;
    }

    public ServiceRecord[] getServiceRecord(){
        
        return this.brutus;
    
    }
    
    public void setServiceRecord(ServiceRecord[] sR){
        
        this.brutus = new ServiceRecord[sR.length];
        
        for (int i=0;i<sR.length;i++){
            this.brutus[i]=sR[i];
        }
    
    }
    
    public String toString(){
        return this.serviceName;
    }
    
    public void setServiceName(String sName){
        
        this.serviceName = sName;
    }
    
    public void setStringConnectionURL(String sURL){
        this.stringConnectionURL = sURL;
    }
    
}
