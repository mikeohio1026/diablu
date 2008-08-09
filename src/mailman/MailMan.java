package mailman;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import mailman.bluetooth.*;
import mailman.bluetooth.discovery.*;
import mailman.gui.MailManGUI;
import mailman.gui.MailManGUI;
import mailman.osc.*;
import mailman.util.*;
import mailman.util.datastructures.*;


public class MailMan {

    // Constants
    public static String LOG_FILE = "log.txt";
    private Object discoveryLock = new Object();
    private Object sendLock = new Object();
    
    // state
    
    private boolean hasOUI = false;
    private boolean hasMimetypes = false;

   
    // Data Structures
     
    private MailManKnownDevices knownDevices;
    private Hashtable<String, String> manufacturerOUI;
    private Hashtable<String, String> mimetypes;
    
    private Vector<MailManDeviceClass> majorDeviceClasses;
    private Vector<MailManDeviceClass> minorDeviceClasses;
    
    // Class instances
    
    private MailManGUI gui;
    private MailManGUI g;
    private MailManDiscovery discovery;
    private Thread discoveryThread;
    
    private MailManBTFileSender fileSender;
    private MailManBTFileReceiver receiveFile;
    private Thread receiveThread;
    
    private MailManOscServer osc;
    private MailManOscClient oscClient;
    private Thread oscClientThread;
    
    private MailManFileReader fileReader;
    private MailManLogger logger;
    
    private Properties properties;
    
    public MailMan() {
        
       
        logger = new MailManLogger(this, LOG_FILE);
        discovery = new MailManDiscovery(this);
                
        fileSender = new MailManBTFileSender(this);
        receiveFile = new MailManBTFileReceiver(this);
        receiveThread = new Thread(receiveFile);
        
        osc = new MailManOscServer(this);
        oscClient = new MailManOscClient(this);
        
        fileReader = new MailManFileReader(this);
      
        
        knownDevices = new MailManKnownDevices(this);
        //Hashtable<String, MailManDevice>();
        manufacturerOUI = new Hashtable<String, String>();
        mimetypes = new Hashtable<String, String>();
        
        majorDeviceClasses = MailManMajorDeviceClass.getVector();
        minorDeviceClasses = MailManMinorDeviceClass.getVector();

        properties = new Properties();
        
    }
    
    // Application main code
    
    public static void main(String[] args) {
  
            
            
            MailMan mailman = new MailMan();
            mailman.g = new MailManGUI(mailman);
            mailman.fileReader.read();
            mailman.loadProperties();
            
            
            if(args.length != 0)
            {
                if(args[0].compareToIgnoreCase("-hideGUI") != 0)
                {
                 mailman.g.setVisible(true);
                }
            }
            else
            {
                 mailman.g.setVisible(true);
            }
            mailman.receiveThread.start();
            mailman.osc.start();
            
       
        
    }

    public Properties getProperties() {
        return properties;
    }

    
    // Setters
    public void setOscClientThread(Thread oscClientThread) {
        this.oscClientThread = oscClientThread;
    }

    public void setOUI(boolean hasOUI) {
        this.hasOUI = hasOUI;
    }
    
    
    public void setMimetypes(boolean hasMimetypes) {
        this.hasMimetypes = hasMimetypes;
    }
    //Getter
    
    
    public boolean hasOUI() {
        return hasOUI;
    }
    
    public boolean hasMimetypes() {
        return hasMimetypes;
    }
    

    
    // Class instance getters
    
    public MailManGUI getGui() {
        return g;
    }
    
    public MailManDiscovery getDiscovery() {
        return discovery;
    }
    
    public Thread getDiscoveryThread() {
        return discoveryThread;
    }
    
    public MailManBTFileSender getFileSender() {
        return fileSender;
    }
    
      
    public MailManOscServer getOsc() {
        return osc;
    }
    
    public MailManOscClient getOscClient() {
        return oscClient;
    }

    public Thread getOscClientThread() {
        return oscClientThread;
    }

    public MailManLogger getLogger() {
        return logger;
    }

    public Object getDiscoveryLock() {
        return discoveryLock;
    }

    public Object getSendLock() {
        return sendLock;
    }
    
    

    // Data structures setters
    
    public MailManKnownDevices getKnownDevices() {
        return knownDevices;
    }

    public Hashtable<String, String> getManufacturerOUI() {
        return manufacturerOUI;
    }
    
    public Hashtable<String, String> getMimetypes()
    {
        return mimetypes;
    }

    public Vector<MailManDeviceClass> getMajorDeviceClasses() {
        return majorDeviceClasses;
    }

    public Vector<MailManDeviceClass> getMinorDeviceClasses() {
        return minorDeviceClasses;
    }

    public void loadProperties()
    {
        File file = new File("config.ini");
        FileInputStream fis = null;
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                properties.setProperty("RemoteIP", "127.0.0.1");
                properties.setProperty("IncomingPort", "12000");
                properties.setProperty("OutgoingPort", "12001");
                properties.setProperty("Directory", new File(".").getCanonicalPath().toString());
                properties.store(fos, "Mailman Configuration");
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MailMan.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MailMan.class.getName()).log(Level.SEVERE, null, ex);
            
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(MailMan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            fis = new FileInputStream(file);
            properties.load(fis);
        } catch (FileNotFoundException ex) {
            getLogger().log(MailManLogger.OTHER, "File \"config.ini\" not found. Using default configuration. ");
        } catch (IOException ex) {
            getLogger().log(MailManLogger.OTHER, "Problem occured loading configuration from file. Using default configuration");
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
            }
        }
    }
    
    public void updateDeviceFiles()
    {
       Enumeration<MailManDevice> enumeration = knownDevices.getDevices().elements();
      
       {
            FileWriter fw = null;
            try {
                fw = new FileWriter("devices.txt", false);
                
            } catch (IOException ex) {
                Logger.getLogger(MailMan.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(MailMan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            while(enumeration.hasMoreElements())
                    enumeration.nextElement().toFile("devices.txt");
       }
    }
    
}
