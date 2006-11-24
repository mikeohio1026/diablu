/*
 * DiABluServerBTServiceServer.java
 *
 * Created on 25 juillet 2006, 11:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.com.bt;

// j2se
import java.lang.*;
import java.io.*;

// logger
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import citar.diablu.server.model.log.diABluLogHandler;

// j2se 1.5 - DiABlu System Constants
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*; 
        
// bluetooth
import javax.microedition.io.*;
import javax.bluetooth.*;

// controller
import citar.diablu.server.controller.in.devices.DiABluServerDevicesListener;

// model
import citar.diablu.server.controller.out.bt.DiABluServerBTModelListener;

/**
 *
 * @author nrodrigues
 */
public class DiABluServerBTServiceServer extends Thread {
    

    private static Logger logger = Logger.getLogger(LOG_MAIN_NAME); // Log API
    private DiABluServerDevicesListener controller;             // this class controller
    private DiABluServerBTModelListener model;                  // this class model
 
    private StreamConnectionNotifier server = null;             // the connection notifier
    private RemoteDevice clientDevice = null;                   // the newly connected client device
            
    private String message = "";                       
    private StreamConnection conn = null;
        
    private int length; 
    private boolean DONE = false;    
    
    // Communication buffers
    private byte[] dataIN = new byte[BT_INPUT_BUFFER_SIZE];
    private byte[] dataOUT = new byte[BT_OUTPUT_BUFFER_SIZE];
    
    /**
     * Creates a new instance of DiABluServerBTServiceServer
     */
    public DiABluServerBTServiceServer(DiABluServerDevicesListener controller, DiABluServerBTModelListener model) {
        
       this.controller = controller;
       this.model = model;   
       
    }
    
    public void run() {
        
        String serviceName = "";
        
        logger.fine(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerBTServiceServer]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Starting_DiABlu_Server_Service"));

        try {
            
            // get our bt device        
            LocalDevice local = LocalDevice.getLocalDevice();
            
            // set it discoverable (?) - It shouldn't be necessary, but some systems fail without it
            local.setDiscoverable(DiscoveryAgent.GIAC);
            
        } catch (BluetoothStateException e) {
            
            logger.warning("[DiABluServerBTServiceServer]_"+"Failed_to_start_bluetooth_system");
            logger.config("[DiABluServerBTServiceServer]_"+"BluetoothStateException_:"+e.getLocalizedMessage());
            e.printStackTrace();
            return;
            
        }

        // get the notifier
        try {
            
            // construct service url
            serviceName = model.getServiceName();
            String serviceURL = "btspp://localhost:"+BT_SERVICE_UUID+";name="+serviceName;
            
            // open a notifier 
            server = (StreamConnectionNotifier)Connector.open(serviceURL);
            
        } catch (IOException e) {
            
            logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerBTServiceServer]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Failed_to_get_bluetooth_notifier"));
            logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerBTServiceServer]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("I/O_Exception")+e.getLocalizedMessage());
            e.printStackTrace();           
            return;
            
        }                        
        
        while (!DONE){
            
        // register the service and wait for clients
        // once a client is connected delegate the connection to a thread and restart
        try {
            
                logger.fine(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerBTServiceServer]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("DiABlu_Service_registered_as_")+serviceName);
                conn = server.acceptAndOpen();
                logger.fine(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerBTServiceServer]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Client_connected"));
        
                
                DiABluServerBTConnection dBTC = new DiABluServerBTConnection(controller,conn);               
                dBTC.start();
                                
            } catch (IOException e) {
                  
                System.err.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("IOException_") + e.getMessage());       
                
             }
        }
    }
    
    
    public void setDone(boolean newDone){
        
        if (this.DONE!=newDone){
            
            // update value
            this.DONE = newDone;
            if (!newDone){
                
                // restart system
                closeServer();
                run();
                
            }
                    
        } 
        
        
    }
    
    /**
     * Simplify log method
     * @Deprecated
    public void log(int priority,String msgLog){
        
        controller.log(priority,msgLog);
        
    }
     */
    
    public void closeServer(){
        
        try {
        
            conn.close();
            
        } catch (IOException ioe){
            
            logger.warning("Error closing service server connection:"+ioe.getLocalizedMessage());
            
        }
        
    }
}
