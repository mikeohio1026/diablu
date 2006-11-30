/*
 * DiABluServerXml.java
 *
 * Created on 11 de Setembro de 2006, 0:20
 *
 * Copyright (C) 11 de Setembro de 2006 Nuno Rodrigues
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */

package citar.diablu.server.model.com.xml;

/**
 * Interface for the DiABlu OSC Listener.
 * Since i pass only DiABlu Objects i can, 
 * easily use this one without the need 
 * of a custom interface.
 *
 */
import citar.diablu.server.controller.out.osc.DiABluServerOSCModelListener;

// j2se 
import java.util.Vector;

// i18n
import java.util.ResourceBundle;

// log
import java.util.logging.Level;

// DiABlu Objects
import citar.diablu.server.model.data.DiABluMsg;
import citar.diablu.server.model.data.DiABluKey;
import citar.diablu.server.model.data.DiABluDevice;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerXml implements DiABluServerOSCModelListener{
    
    /** Creates a new instance of DiABluServerXml */
    public DiABluServerXml() {
    }
    
     /* 
     * DiABlu Device's
     *
     */   
    public void newDiABluDevices (Vector <DiABluDevice> addDiABlus){}
    
    public void editDiABluDevices (Vector <DiABluDevice> editDiABlus){}
    
    public void removeDiABluDevices (Vector <DiABluDevice> removeDiABlus){}
    
    public void newMsg (DiABluMsg newMsg){}
    
    public void newKey (DiABluKey newKey){}
    
    /* 
     * Settings
     */
    
    // Global    
    public void setResourceBundle(ResourceBundle rb){}
    
   
    // Output
    // Protocol OpenSoundControl
    public void setTargetAddress(String targetURL){}
    
    public void setTargetPort(String targetPort){}
    
   public void newDeviceList (Vector <DiABluDevice> updatedDiABlusList){}
    
    public void newDeviceCount(int newDiABlusCount){}
    
    public void setLogLevel(Level newLevel){}
}
