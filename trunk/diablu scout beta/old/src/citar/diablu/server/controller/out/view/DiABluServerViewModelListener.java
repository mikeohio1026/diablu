/*
 * DiABluServerViewModelListener.java
 *
 * Created on 29 de Agosto de 2006, 17:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.out.view;

import citar.diablu.server.controller.out.DiABluServerModelListener;

/**
 * This interface contains the View's specific methods
 * @author Nuno Rodrigues
 */
public interface DiABluServerViewModelListener extends DiABluServerModelListener {
    
    // Protocol Bluetooth
    public void setServiceName(String newServiceName);
    
    public void setServiceDescription(String newServiceDesc);
    
    public void setBluetoothDelay(int delay);
    
    // Global
    public void setCountry(String country);
    
    public void setLanguage(String language);
    
    // Log     
    public void log(int priority, String log);
    
    public void setLogDetail(int priority);
    
    public void clearLog();
    
    // Simulator    
    public void setSimulatorAuto(boolean auto);
    
    public void simulatorIsRunning(boolean status);
    
    // Hide/unhide
    public void setVisible(boolean visible);
    
}
