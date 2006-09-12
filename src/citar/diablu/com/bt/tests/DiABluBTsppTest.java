/*
 * DiABluBTsppTest.java
 *
 * Created on 6 juin 2006, 15:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.bt.tests;

import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;
import java.lang.*;

/**
 *
 * @author nrodrigues
 */
public class DiABluBTsppTest {
    
    
    StreamConnectionNotifier notifier = null;
    StreamConnection con = null;
    LocalDevice localdevice = null;
    ServiceRecord servicerecord = null;
    
    /** Creates a new instance of DiABluBTsppTest */
    public DiABluBTsppTest() {
        
        
        System.out.println("Registering the service...");
        String url = "btspp://localhost:10112233445566778899AABBCCDDEEFF;name=DiABluSerial";
        try {
            
        notifier = (StreamConnectionNotifier) Connector.open(url);                
        localdevice = LocalDevice.getLocalDevice();
        servicerecord = localdevice.getRecord(notifier);
        localdevice.setDiscoverable(DiscoveryAgent.GIAC); 
        
        //servicerecord.setAttributeValue( 0x0008, new DataElement( DataElement.U_INT_1, 0xFF ) );
        
        con = notifier.acceptAndOpen();
        
        notifier.close();
        
        } catch (Exception e){
            
            System.out.println(e);
        }
        
        System.out.println("Exiting...");
        
        
    }
    
     public static void main(String args[]) {
        // TODO: Parse the ARGS
        // TODO: Put a help situation /?        
         
             new DiABluBTsppTest();
            
    }
}
    
    
