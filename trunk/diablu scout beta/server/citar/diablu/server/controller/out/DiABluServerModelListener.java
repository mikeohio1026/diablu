/*
 * DiABluModelListener.java
 *
 * Created on 10 août 2006, 12:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.out;

// j2se
import java.util.Vector;

// log
import java.util.logging.Level;

// i18n & l9n
import java.util.ResourceBundle;

// data
import citar.diablu.server.model.data.DiABluDevice;
import citar.diablu.server.model.data.DiABluKey;
import citar.diablu.server.model.data.DiABluMsg;

/**
 * This interface implements the view listener methods
 *
 * @author nrodrigues
 */
public interface DiABluServerModelListener {
    
    /* 
     * DiABlu Device's
     *
     */   
    public void newDiABluDevices (Vector <DiABluDevice> addDiABlus);
    
    public void editDiABluDevices (Vector <DiABluDevice> editDiABlus);
    
    public void removeDiABluDevices (Vector <DiABluDevice> removeDiABlus);
    
    public void newMsg (DiABluMsg newMsg);
    
    public void newKey (DiABluKey newKey);
    
    /* 
     * Settings
     */
    
    // Global    
    public void setResourceBundle(ResourceBundle rb);
    
    // Log
    /**
     *  Set's the log level
     *
     */
    public void setLogLevel(Level newLevel);
    
    
    
    // Input
    // TODO:Multiple Protocol
   
    // Output
    // Protocol OpenSoundControl
    public void setTargetAddress(String targetURL);
    
    public void setTargetPort(String targetPort);

    
}
