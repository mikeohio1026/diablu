/*
 * DiABluServerOSCModelListener.java
 *
 * Created on 29 de Agosto de 2006, 17:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.out.osc;

// j2se
import java.util.Vector;

// data
import citar.diablu.server.model.data.DiABluDevice;

// parent controller interface
import citar.diablu.server.controller.out.DiABluServerModelListener;

/**
 * This interface contains specific methods to OSC output
 * @author Nuno Rodrigues
 */
public interface DiABluServerOSCModelListener extends DiABluServerModelListener {
    
    public void newDeviceList (Vector <DiABluDevice> updatedDiABlusList);
    
    public void newDeviceCount(int newDiABlusCount);
    
}
