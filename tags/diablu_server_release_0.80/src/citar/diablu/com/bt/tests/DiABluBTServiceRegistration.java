/*
 * DiABluBTServiceRegistration.java
 *
 * Created on 6 juin 2006, 10:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.bt.tests;

import javax.microedition.io.*;
//import com.atinav.standardedition.io;
//import javax.microedition.*;
import javax.bluetooth.*;
import java.io.*;
import java.util.*;


/**
 *
 * @author nrodrigues
 */
public class DiABluBTServiceRegistration {
    
    // variables
    StreamConnectionNotifier notifier = null;
    StreamConnection sconn = null;
    LocalDevice localdevice = null;
    ServiceRecord servicerecord = null;
    StreamConnection connection = null;
    
    /** Creates a new instance of DiABluBTServiceRegistration */
    public DiABluBTServiceRegistration() {
     try{
        
         localdevice = LocalDevice.getLocalDevice();                   
        
        /**
         * To create a new service record that represents the service, invoke 
         * Connector.open with a server connection URL argument, and cast the 
         * result to a StreamConnectionNotifier that represents the service
         */
        String url = "btspp://localhost:93007CA747114F42b1dcce878a65391f;name=diabluservice";
        notifier = (StreamConnectionNotifier) Connector.open(url);
        
        /**
         * Obtain the service record created by the server device
         */
        servicerecord = localdevice.getRecord(notifier);
        
    
        /**
         * Indicate that the service is ready to accept a client connection.
         * Note that acceptAndOpen blocks until a client connects
         */
        connection = (StreamConnection) notifier.acceptAndOpen();
       
        /**
         * When the server is ready to exit, close the connection 
         * and remove the service record
         */
        notifier.close();
        
        } catch (Exception e){
            
            System.out.println("Exception found:"+e);
        }
    }
    
}
