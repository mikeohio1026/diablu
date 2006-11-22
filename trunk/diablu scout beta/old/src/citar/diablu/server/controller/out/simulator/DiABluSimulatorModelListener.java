/*
 * DiABluSimulatorModelListener.java
 *
 * Created on 29 de Agosto de 2006, 10:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.controller.out.simulator;

import citar.diablu.server.model.data.DiABluDevice;


/**
 * This interface contains the simulator's specific controller methods 
 * @author Nuno Rodrigues
 */
public interface DiABluSimulatorModelListener {
    

   public void setSelectedDevice(DiABluDevice dd); 
    
    
}
