/*
 * DiABluServerViewModelListener.java
 *
 * Created on 29 de Agosto de 2006, 17:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.out.view;

import java.util.Vector;

import citar.diablu.server.controller.out.DiABluServerModelListener;
import citar.diablu.server.model.data.DiABluDevice;

/**
 * This interface contains the View's specific methods
 * @author Nuno Rodrigues
 */
public interface DiABluServerViewModelListener extends DiABluServerModelListener {
    
    // Protocol Bluetooth
    public void setFilterFNames(boolean filterFN);
    
    public void setServiceName(String newServiceName);
    
    public void setServiceDescription(String newServiceDesc);
    
    public void setBluetoothDelay(int delay);
    
    public void setAutoDiscovery(boolean autoDisc);
    
    public void setAutoService(boolean autoService);
    
    public void setFastMode(boolean fastmode);     
    
    public void setDiscoveryStatus(boolean started);
    
    public void setServiceStatus(boolean started);
    
    public void setSimulatorStatus(boolean started);
    
    public void setVCyclesIN(int vcIN);
    
    public void setVCyclesOUT(int vcOUT);
    
    public void setProtocol(String prot);
    
    // blacklist
    /**
     * This method tells the view to reset it's 
     * detected devices table and fill up with
     * new values
     * @param Vector <DiABluDevice> newList
     *
     */
    public void resetDeviceList(Vector <DiABluDevice> ddList);
    
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
    public void setVisibleView(boolean visible);
    
}
