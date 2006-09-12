/*
 * DiABluService.java
 *
 * Created on 29 mai 2006, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
