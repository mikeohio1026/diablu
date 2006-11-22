/*
 * DiABluServerCONSTANTS.java
 *
 * Created on 30 de Agosto de 2006, 16:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.settings;

/**
 * DiABlu System Constants
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerCONSTANTS {
    
    /**
     *   DEFAULT SETTINGS
     *   This is the first layer of settings and these values
     *   will be replaced by file or command line parameters
     *
     */
    public final static String DEFAULT_SERVICE_NAME         = "DiABlu Service";
    public final static String DEFAULT_SERVICE_DESCRIPTION  = "This service allows you to interact with Digital Art works(in progress?:)";
    public final static String DEFAULT_TARGET_ADDRESS       = "127.0.0.1";   // Localhost
    public final static String DEFAULT_TARGET_PORT          = "10000";
    public final static String DEFAULT_COUNTRY              = "Portugal";    // Sempre contigo ;P 
    public final static String DEFAULT_LANGUAGE             = "English";
    public final static String DEFAULT_BUNDLE_PATH          = "citar/diablu/server/model/i18n/diABluServerDefaultBundle";
    public final static boolean DEFAULT_SIMULATOR           = true;
    public final static int DEFAULT_BT_DELAY                = 10000;
    public final static int DEFAULT_LOG_DETAIL              = 0;             // Simple log
      
    /*
     * DiABlu Device TYPE IDENTIFIER
     */
    public final static int DEVICE_SIMULATED = 0;
    public final static int DEVICE_BLUETOOTH = 1;
    
    /** LOG Detail & Priority
     *    
     * The first three constantes represent Detail as well as Priority
     * It means a DETAILED will have both SIMPLE as DETAILED log messages...
     * ...and DEBUG will have all(SIMPLE,DETAILED,DEBUG)
     * The remaining represent ERRORS and will always be showed
     * 
     * If you whish to change the log heuristics please goto DiABluServerModel class
     * and check out the log method
     *
     */
    public final static int LOG_SIMPLE                      = 0;   // Simple 
    public final static int LOG_DETAILED                    = 1;   // Detailed 
    public final static int LOG_DEBUG                       = 2;   // Debug
    public final static int LOG_INPUT_ERROR                 = 3;   // Input Validation Error
    public final static int LOG_COM_ERROR                   = 4;   // Communications Error
    public final static int LOG_SECURITY_ERROR              = 5;   // Security Error
    public final static int LOG_UNEXPECTED_ERROR            = 6;   // Unexpected Error
    
    /* OSC */
    /** 
     *  OSC COMMANDS 
     *  These constants represent the Open Sound Control command names
     *  outputed by the DiABlu System
     *  
     * !WARNING!:CHANGING THESE VALUES WILL AFECT THE TARGET APPLICATION!!!
     *  
     */
    public final static String OSC_DEVICE_IN=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("/DeviceIn");                    // New Device entered 
    public final static String OSC_DEVICES_IN=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("/DeviceListIn");               // Newly entered devices //ss ss ss
    public final static String OSC_DEVICE_OUT=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("/DeviceOut");                  // Single Device exited
    public final static String OSC_DEVICES_OUT=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("/DeviceListOut");             // Multiple Devices out //ss ss ss
    public final static String OSC_MESSAGE_IN=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("/MessageIn");                  // Text Message sended
    public final static String OSC_KEY_IN=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("/KeyIn");                          // Key pressed from device
    public final static String OSC_DEVICE_COUNT=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("/DeviceCount");              // Number of devices present
    public final static String OSC_DEVICE_LIST=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("/DeviceList");                // Actual list of devices present //ss ss ss
    public final static String OSC_NAME_CHANGED=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("/NameChanged"); 
    
    /** BT */
    /** 
     *  BLUETOOTH CONNECTION PARAMETERS
     *  
     *  !WARNING!: MODIFYING THESE VALUES WILL AFECT DIABLU CLIENT!!!
     *
     */    
    public final static String BT_ROGER="ROGER";                                    // Acknowledge communication code
    public final static String BT_OVER="OVER";                                      // End communication code
    public final static String BT_SERVICE_UUID="F0E0D0C0B0A000908070605040302013";  // DiABlu Bluetooth Service UUID
    public final static int BT_INPUT_BUFFER_SIZE = 256;                             // Incoming data connection buffer size
    public final static int BT_OUTPUT_BUFFER_SIZE = 256;                            // Outgoing data connection buffer size
    public final static int BT_DEFAULT_DELAY = 10000;                               // Default delay in ms between bluetooth device discoverys
    
    
    
    
    /** Creates a new instance of DiABluServerCONSTANTS */
    public DiABluServerCONSTANTS() {
        
    }
    
}
