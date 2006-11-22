/*
 * DiABluControllerListener.java
 *
 * Created on 12 de Agosto de 2006, 3:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.in.view;

// log data
import java.util.logging.Level;

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
    public void removeFromBlackList(String did);
    
    public void addToBlackList(String did);
    
    public void clearBlackList();
    
    // Simulator
    public void startStopSimulator();    
    
    public void autoSimulator(boolean as);
    
    // Input - Bluetooth
    // TODO: implement for any protocol
    public void newServiceName(String newServiceName);
    
    public void newServiceDescription(String newServiceDescription);
    
    public void newBluetoothDelay(int btDelay);
    
    public void newVerifyCyclesIN(int vCin);

    public void newVerifyCyclesOUT(int cCout);
    
    public void setFilterFriendlyNames(boolean filter);
    
    public void setFastMode(boolean fastMode);
    
    public void startStopDiscovery();
    
    public void startStopService();
    
    public void setAutoStartDiscovery(boolean autoDiscovery);
    
    public void setAutoStartService(boolean autoService);
    
    // Output - Open Sound Control
    // TODO: implement for any protocol
    public void newProtocol(String newProtocol);
    
    public void newPort(String newPort);
    
    public void newTargetAddress(String newTargetAddress);
    
    // Settings File
    public void applySettings();
    
    public void loadSettings();
    
    public void saveSettings();
      
    // Log
    public void newLogLevel(String newLogLevel);
    
    public void clearLog();
    
    public void saveLog();
    
    // Menu
    
    // View
    public void setView(String view);
    
    // Exit the application
    public void exit();
    
    public void showCredits();
    
}
