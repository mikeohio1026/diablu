/*
 * DiABluServerBTModelListener.java
 *
 * Created on 29 de Agosto de 2006, 2:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.out.bt;

/**
 * This interface contains the BT specific methods
 * @author Nuno Rodrigues
 */
public interface DiABluServerBTModelListener {

    // starts the corresponding bluetooth sub-system 
    public void startSystem();
    
    // service server                
    public void setServiceName(String sName);
    
    public void setServiceDescription(String sDesc);
    
    public String getServiceName();
    
    public String getServiceDescription();
        
    // device discovery   
    public void setDelay(int newBTdelay);

    public int getDelay();
    
    
}
