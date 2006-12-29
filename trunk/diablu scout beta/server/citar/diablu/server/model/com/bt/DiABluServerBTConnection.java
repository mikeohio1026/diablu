/*
 * DiABluServerBTConnection.java
 *
 * Created on 14 juin 2006, 12:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.com.bt;

// j2se
import java.io.*;

// j2se 5 - DiABlu System Constants
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*;

// jsr 82
import javax.microedition.io.*;
import javax.bluetooth.*;

// model objects
import citar.diablu.server.model.data.DiABluMsg;
import citar.diablu.server.model.data.DiABluKey;
import citar.diablu.server.model.data.DiABluDevice;
import citar.diablu.server.model.data.DiABluID;

// logger
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import citar.diablu.server.model.log.diABluLogHandler;

// controller
import citar.diablu.server.controller.in.devices.DiABluServerDevicesListener;

/**
 *
 * @author nrodrigues
 */
public class DiABluServerBTConnection extends Thread {

    private static Logger logger = Logger.getLogger(LOG_MAIN_NAME); // Log API
    private DiABluServerDevicesListener controller;  
    private DiABluID clientID;
    private String message = "";            
    private StreamConnection clientConnection = null;
    private RemoteDevice clientDevice = null; 

    
    /**
     * Creates a new instance of DiABluServerBTConnection
     */
    public DiABluServerBTConnection (DiABluServerDevicesListener controller, StreamConnection conn){
      
        this.controller = controller;
        this.clientConnection = conn;        
        RemoteDevice remoteDevice;
        String clientUUID=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("not_avaiable");   
        
        logger.fine(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerBTConnection]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Starting_BT_connection_thread"));
        
        try {
                remoteDevice = RemoteDevice.getRemoteDevice(conn);
                clientUUID = remoteDevice.getBluetoothAddress();
                
        } catch ( Exception e ) {
            
            logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerBTConnection]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Error_getting_device_address"));
            logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerBTConnection]_")+e.getLocalizedMessage());
            e.printStackTrace();
            
        }
        
        this.clientID = new DiABluID(clientUUID,"");       
                  
    } 
    
    /**
     * @Deprecated
    // Simple log method
    private void log(int p, String s){
        
        controller.log(p,s);
        
    }
  */
    public void run() 
    {
          
        try {  
          
              // get the i/o streams
              logger.fine(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_") + java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Opening_data_IO_streams_for") +  clientID.toString() );
              DataInputStream in = clientConnection.openDataInputStream();
              DataOutputStream out = clientConnection.openDataOutputStream();                                                    
              
              // cycle until clients sends OVER or -1 detected
              while ( !message.equalsIgnoreCase(BT_OVER) ) {
                                   
               // read the message
               try {    
                        logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Reading_data"));
                        // get the string message
                        message = in.readUTF();
                        logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Message") + message);
                        processMsg(message,clientID);
                                                    
                 } catch (IOException e) {
                     
                        logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("IO_Exception_while_reading_from_Bluetooth_connection"));
                        logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+e.getLocalizedMessage());
                        e.printStackTrace();
                        break;
                 }
               
                // Them send the "ROGER" to the client so he knows
                try {
                    
                         logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Writing_ROGER"));
                         out.writeUTF(BT_ROGER);                                    
                            
                } catch (IOException ioE) {
                           
                          logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Error_writing_ROGER"));
                          logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+ioE.getLocalizedMessage());
                          ioE.printStackTrace();
                          break;
                                    
                 }
               
               }
                    
               
               // let's clean the streams'              
               logger.fine(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Closing_IO_Streams_for")+clientID.toString());

               in.close();
               out.close();

            } catch (IOException e) {
                
               logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("General_IO_Exception"));
               logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+e.getLocalizedMessage());
               e.printStackTrace();
                
            } finally {
                
                // close the BT connection
                if ( clientConnection != null ) {
                    
                    try {
                        
                        logger.fine(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Closing_BT_connection"));
                        clientConnection.close();
                        
                    } catch (IOException e) {
                        
                        logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Error_closing_BT_connection"));
                        logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+e.getLocalizedMessage());
                        e.printStackTrace();
                        
                    }
                    
                } else {
                    
                    logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DiABluServerConnection-run()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Trying_to_close_null_connection"));
                    
                }
            }
    }   
    

    private void processMsg(String msg,DiABluID dID){
              
        if (msg.startsWith(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("K"))) {
                          
            // Parse the keys
            String[] keys = msg.substring(1).split("\\|",2);
            
            logger.fine(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Keypressed")+keys[0]);
            logger.fine(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Game_Action")+keys[1]);
            
            // Create the DiABluKey
            DiABluKey dbK = new DiABluKey(dID,keys[0],keys[1]);
            
            // Send the key
            controller.newKey(dbK);
            
        } else {
            
            // Message without the M            
            DiABluMsg dM = new DiABluMsg(dID,msg);
            controller.newMsg(dM);
        
        }
        
    }

    
}   
