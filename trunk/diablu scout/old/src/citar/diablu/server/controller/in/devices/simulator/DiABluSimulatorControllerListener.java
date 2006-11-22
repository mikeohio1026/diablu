/*
 * DiABluSimulatorControllerListener.java
 *
 * Created on 29 de Agosto de 2006, 11:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.in.devices.simulator;

//j2se
import java.util.Vector;

//model data
import citar.diablu.server.model.data.DiABluDevice;

//parent controller interface
import citar.diablu.server.controller.in.devices.DiABluServerDevicesListener;

/**
 *
 * @author Nuno Rodrigues
 */
public interface DiABluSimulatorControllerListener extends DiABluServerDevicesListener {
   
    public void newSimDiABluDevice (DiABluDevice addDiABlu);
    
    public void editSimDiABluDevice (DiABluDevice editDiABlu);
    
    public void removeSimDiABluDevice (DiABluDevice removeDiABlu);
    
}
