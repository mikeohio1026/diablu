/*
 * OUTWatcher.java
 *
 * Created on 11 de Maio de 2006, 17:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.interfaces;

import java.util.Vector;
import citar.diablu.classes.DiABluKey;
import citar.diablu.classes.DiABluMsg;
import java.net.InetSocketAddress;

/**
 *
 * @author nrodrigues
 */
public interface OUTWatcher {
    
    public void sendAddDevices(Vector aDevices, InetSocketAddress addr);
    
    public void sendRemoveDevices(Vector rDevices, InetSocketAddress addr);
    
    public void sendDeviceList(Vector lDevices, InetSocketAddress addr);
        
    public void sendMsg(DiABluMsg newDMsg, InetSocketAddress addr);
    
    public void sendKeys(DiABluKey newDKey, InetSocketAddress addr);
    
    public void sendDeviceCount(int dCount, InetSocketAddress addr);
    
    public void sendNamesChanged(Vector nDevices, InetSocketAddress addr);
    
}
