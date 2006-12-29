/*
 * DiABluServerCONSTANTS.java
 *
 * Created on 30 de Agosto de 2006, 16:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.settings;

import java.util.logging.Level;

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
    public final static String DEFAULT_SETTINGS_FILE        ="build/classes/citar/diABluServerSettings.xml";
    public final static String DEFAULT_LOG_FILE             ="DiABluLogFile";
    
    // I18N & L9N
    public final static String DEFAULT_COUNTRY              = "Portugal";    // Sempre contigo ;P
    public final static String DEFAULT_LANGUAGE             = "English";
    public final static String DEFAULT_BUNDLE_PATH          = "citar/diablu/server/model/i18n/diABluServerDefaultBundle";
        
    // VIEW
    public final static String VIEW_COMPACT                 = "Compact";
    public final static String VIEW_CLASSICAL               = "Classical";
    public final static String VIEW_NO_GRAPHICS             = "none";
    public final static String VIEW_DEFAULT                 = VIEW_CLASSICAL;
    public final static int VIEW_LOG_LINE_MAX               = 1000;
    
    // FILTER
    public final static boolean FILTER_DEFAULT_FNAMES = false;      // if true only device's with setted friendly names will go to the target application    
    
    /*
     * DiABlu Device TYPE IDENTIFIER
     */
    public final static int DEVICE_SIMULATED = 0;
    public final static int DEVICE_BLUETOOTH = 1;
    
    // DEVICE STATUS
    public final static int DEVICE_STATUS_SIMULATED = 0;
    public final static int DEVICE_STATUS_BT = 1;
    public final static int DEVICE_STATUS_BLACKLISTED = 2;
    public final static int DEVICE_STATUS_IGNORED = 3;
    public final static int DEVICE_STATUS_RECOVERED = 4;
    
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
    public final static int LOG_SIMPLE                      = 0;                         // Simple
    public final static int LOG_DETAILED                    = 1;                         // Detailed
    public final static int LOG_DEBUG                       = 2;                         // Debug
    public final static int LOG_INPUT_ERROR                 = 3;                         // Input Validation Error
    public final static int LOG_COM_ERROR                   = 4;                         // Communications Error
    public final static int LOG_SECURITY_ERROR              = 5;                         // Security Error
    public final static int LOG_UNEXPECTED_ERROR            = 6;                         // Unexpected Error    
    public final static String LOG_DEFAULT_DETAIL           = Level.FINE.toString();     // Simple log
    public final static String LOG_MAIN_NAME                = "diABluLog";               // Name of the log called by each class
    
    /* OSC */
    /**
     *  OSC COMMANDS
     *  These constants represent the Open Sound Control command names
     *  outputed by the DiABlu System
     *
     * !WARNING!:CHANGING THESE VALUES WILL AFECT THE TARGET APPLICATION!!!
     *
     */
    public final static String OSC_DEVICE_IN="/DeviceIn";                    // New Device entered
    public final static String OSC_DEVICES_IN="/DeviceListIn";               // Newly entered devices //ss ss ss
    public final static String OSC_DEVICE_OUT="/DeviceOut";                  // Single Device exited
    public final static String OSC_DEVICES_OUT="/DeviceListOut";             // Multiple Devices out //ss ss ss
    public final static String OSC_MESSAGE_IN="/MessageIn";                  // Text Message sended
    public final static String OSC_KEY_IN="/KeyIn";                          // Key pressed from device
    public final static String OSC_DEVICE_COUNT="/DeviceCount";              // Number of devices present
    public final static String OSC_DEVICE_LIST="/DeviceList";                // Actual list of devices present //ss ss ss
    public final static String OSC_NAME_CHANGED="/NameChanged";        

    public final static String OUT_DEFAULT_PROTOCOL             = "OSC";
    public final static String OUT_DEFAULT_TARGET_PORT          = "10000";
    public final static String LOCALHOST                        = "127.0.0.1"; // Localhost address
    public final static String OUT_DEFAULT_TARGET_ADDRESS       = LOCALHOST;
    
    // OUTPUT PROTOCOLS
    public final static String PROTOCOL_OSC = "Osc";
    public final static String PROTOCOL_FLOSC = "Flosc";
    public final static String PROTOCOL_XML = "Xml";
    public final static String PROTOCOL_DEFAULT = PROTOCOL_OSC;
    
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
    public final static int BT_DEFAULT_DELAY = 0;                               // Default delay in ms between bluetooth device discoverys
    public final static int BT_DEFAULT_VCYCLES_IN = 0;                      
    public final static int BT_DEFAULT_VCYCLES_OUT = 0;
    public final static boolean BT_DEFAULT_FAST_MODE = false;
    public final static boolean BT_DEFAULT_FILTER_FNAME = false;
    public final static boolean BT_DEFAULT_START_DISCOVERY = true;
    public final static boolean BT_DEFAULT_START_SERVICE = true;
    public final static String BT_DEFAULT_SERVICE_NAME = "DiABlu Service";
    public final static String BT_DEFAULT_SERVICE_DESCRIPTION = "This service allows you to interact with Digital Art works(in progress?:)";
    public final static boolean BT_DEFAULT_SIMULATOR = false;
    
    
    /** Creates a new instance of DiABluServerCONSTANTS */
    public DiABluServerCONSTANTS() {
        
    }
    
}
