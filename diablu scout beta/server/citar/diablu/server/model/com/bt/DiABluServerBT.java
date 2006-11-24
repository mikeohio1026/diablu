/*
 * DiABluServerBT.java
 *
 * Created on 29 de Agosto de 2006, 2:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.com.bt;

// j2se 1.5 - DiABlu System Constants
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*;

// controller (listening model)
import citar.diablu.server.controller.in.devices.bt.DiABluServerBTControllerListener;

// model
import citar.diablu.server.controller.out.bt.DiABluServerBTModelListener;

// bluetooth
import citar.diablu.server.model.com.bt.DiABluServerBTServiceServer;    // service server
import citar.diablu.server.model.com.bt.DiABluServerBTDeviceDiscovery;  // device discovery

// jsr82
import javax.bluetooth.*;

// logger
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import citar.diablu.server.model.log.diABluLogHandler;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerBT implements DiABluServerBTModelListener {
    
    private DiABluServerBTControllerListener controller;        // the listening model
    private int btDelay;                                        // personalized delay between bluetooth device discoverys
    private String serviceName;                                 // personalized DiABlu Server Service Name
    private String serviceDescription;                          // personalized DiABlu Server Service Description
    
    private DiABluServerBTDeviceDiscovery diABluDiscovery;      // Bluetooth device discovery class
    private DiABluServerBTServiceServer diABluServiceServer;          // Bluetooth service server class
    private static Logger logger = Logger.getLogger(LOG_MAIN_NAME); // Log API
    
    /**
     * Creates a new instance of DiABluServerBT
     */
    public DiABluServerBT(DiABluServerBTControllerListener controller) {
        
        this.controller = controller;

    }

    /**
     * This method starts the DiABlu Bluetooth System
     * First we start the Service Server and them the Discovery Agent
     * since registering the service will take much less time to complete
     * and they won't be in need of hardware conflit, which shouldn't happen but...
     *
     * Anyway if you feel the need to change the order (or etc) of 
     * the Bluetooth System, this is the right place :)
     *
     */
    public void startSystem(){
        
        // Start DiABlu Bluetooth functions
        
        // First the server
        try {
        
            diABluServiceServer = new DiABluServerBTServiceServer(controller,this);
            diABluServiceServer.start();
            logger.finest(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DSBT|startSystem()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Started_device_discovery"));
            
            
        } catch (Exception e){
            
            logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DSBT|startSystem()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Error_trying_to_register_service"));
            logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DSBT|startSystem()]_")+ e.getLocalizedMessage());
            e.printStackTrace();
            
        }
        /*
        try {
           
            // now the discovery
            // diABluDiscovery = new DiABluServerBTDeviceDiscovery(controller,this);
            // diABluDiscovery.run();            
            logger.finest(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DSBT|startSystem()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Started_device_discovery"));
                
        } catch (BluetoothStateException bte1) {
            
            logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DSBT|startSystem()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Unable_to_get_hardware_response"));
            logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DSBT|startSystem()]_")+ bte1.getLocalizedMessage());
            bte1.printStackTrace();
            
        } catch (Exception e2){
            
            logger.warning(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DSBT|startSystem()]_")+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Error_trying_to_start_device_discovery"));
            logger.config(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("[DSBT|startSystem()]_")+ e2.getLocalizedMessage());
            e2.printStackTrace();
            
            
        }
        */
        System.out.println("BT started 100%");
    }
    
    public void startService(){
        
        diABluServiceServer.setDone(false);
        
    }
    
    public void stopService(){
        
        diABluServiceServer.setDone(true);
        
    }
    // bluetooth parameters set & get
    public void setDelay(int newBTdelay){
        
        this.btDelay = newBTdelay;
        
    }
    
    public int getDelay(){
        
        return this.btDelay;
        
    }
    
    public void setServiceName(String sName){
        
        this.serviceName = sName;
        
    }
    
    public String getServiceName(){
        
        return this.serviceName;
        
    }
    
    public void setServiceDescription(String sDesc){
        
        this.serviceDescription = sDesc;
        
    }
    
    public String getServiceDescription(){
        
        return this.serviceDescription;
    }
    
    /**
     * @Deprecated
    private void log(int priority,String logMsg){
        
        this.controller.log(priority,logMsg);
        
    
     *
     */
}
