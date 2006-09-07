/*
 * INWatcher.java
 *
 * Created on 11 de Maio de 2006, 14:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.interfaces;

import citar.diablu.classes.DiABluMsg;
import citar.diablu.classes.DiABluKey;
import java.util.Vector;

/**
 *
 * @author nrodrigues
 */
public interface INWatcher {
    
    public void newDeviceList (Vector nDeviceList, int type);
    
    public void newMsg (DiABluMsg newMsg);
    
    public void newKey (DiABluKey newKey);
    
    public void newLog (int priority,String log);
    
    public void newLog (String log);
    
    public int getBTdelay();


    
}
