/*
 * DiABluControllerListener.java
 *
 * Created on 12 de Agosto de 2006, 3:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.in.view;

// parent interface
import citar.diablu.server.controller.in.DiABluServerModelControllerListener;

// model data
import citar.diablu.server.model.data.DiABluDevice;
import citar.diablu.server.model.data.DiABluID;

/**
 *
 * @author Nuno Rodrigues
 */
public interface DiABluServerViewControllerListener extends DiABluServerModelControllerListener {
    
    /**
     *   Methods for the View Server class
     *
     */
    
    // Detect table
    public void newSelectedDevice(DiABluID selectDD);
    
    // Global  
    public void newLanguage(String newLanguage);
    
    public void newCountry(String newCountry);
    
    // Black List
    public void removeFromBlackList(DiABluID did);
    
    public void addToBlackList(DiABluID did);
    
    public void clearBlackList();
    
    // Simulator
    public void startSimulator();
    
    public void stopSimulator();
    
    public void autoSimulator(boolean as);
    
    // Input - Bluetooth
    // TODO: implement for any protocol
    public void newServiceName(String newServiceName);
    
    public void newServiceDescription(String newServiceDescription);
    
    public void newBluetoothDelay(int btDelay);
    
    public void newVerifyCycles(int vC);
   
    // Output - Open Sound Control
    // TODO: implement for any protocol
    public void newPort(String newPort);
    
    public void newTargetAddress(String newTargetAddress);
    
    // Settings File
    public void applySettings();
    
    public void loadSettings();
    
    public void saveSettings();
      
    // Log
    public void newLogDetail(int newLogDetail);
    
    public void clearLog();
    
    public void saveLog();
    
    // Menu
    
    // Exit the application
    public void exit();
    
    public void showCredits();
    
}
