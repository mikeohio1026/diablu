/*
 * DiABluServerBTControllerListener.java
 *
 * Created on 29 de Agosto de 2006, 11:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.in.devices.bt;

//j2se
import java.util.Vector;
//parent interface
import citar.diablu.server.controller.in.devices.DiABluServerDevicesListener;
//model data
import citar.diablu.server.model.data.DiABluDevice;


/**
 *
 * @author Nuno Rodrigues
 */
public interface DiABluServerBTControllerListener extends DiABluServerDevicesListener {
  
    public void newDeviceList (int deviceType, Vector <DiABluDevice> updatedDiABlusList);
    
}
