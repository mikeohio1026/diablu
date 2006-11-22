/*
 * DiABluServerDevicesListener.java
 *
 * Created on 29 de Agosto de 2006, 11:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.in.devices;

// parent interface
import citar.diablu.server.controller.in.DiABluServerModelControllerListener;

// model data
import citar.diablu.server.model.data.DiABluKey;
import citar.diablu.server.model.data.DiABluMsg;

/**
 *
 * @author Nuno Rodrigues
 */
public interface DiABluServerDevicesListener extends DiABluServerModelControllerListener {    
     
    public void newMsg (DiABluMsg newMsg);
    
    public void newKey (DiABluKey newKey);
    
}
