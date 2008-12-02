package pt.citar.diablu.sms2osc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import pt.citar.diablu.sms2osc.S2O;

public class S2OProperties {

    S2O s2o;
    Properties properties;

    public S2OProperties(S2O s2o) {
        this.s2o = s2o;
        this.properties = new Properties();
    }

    public void loadProperties() {
        File file = new File("config.ini");
        FileInputStream fis = null;
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                properties.setProperty("RemoteIP", "127.0.0.1");
                properties.setProperty("IncomingPort", "12000");
                properties.setProperty("OutgoingPort", "12001");
                properties.setProperty("Gateway", "sms2osc");
                properties.setProperty("ComPort", s2o.getCommPortList().getFirst());
                properties.setProperty("UseLoopback", "true");
                properties.store(fos, "SMS2OSC Configuration");

            } catch (IOException ex) {
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                }
            }
        } else {
            try {
                fis = new FileInputStream(file);
                properties.load(fis);
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                }
            }
        }

    }
    
    public String getRemoteIP()
    {
        return properties.getProperty("RemoteIP", "127.0.0.1");
    }
    
    public String getIncomingPort()
    {
        return properties.getProperty("IncomingPort", "12000");
    }

    public String getOutgoingPort()
    {
        return properties.getProperty("OutgoingPort", "12001");
    }
    
    public String getGateway()
    {
        return properties.getProperty("Gateway", "sms2osc");
    }

    public String getComPort()
    {
        return properties.getProperty("ComPort", s2o.getCommPortList().getFirst());
    }
    
    public boolean useLoopback()
    {
        if(properties.getProperty("UseLoopback", "false").compareToIgnoreCase("true") == 1)
            return true;
        return false;
    }
    
    public boolean useParser()
    {
        if(properties.getProperty("UseParser", "false").compareToIgnoreCase("true") == 0)
            return true;
        return false;
    }
    
    public String getCommands()
    {
        return properties.getProperty("Commands", "");
    }
    
    public String getCommandString(String command)
    {
        return properties.getProperty(command, "");
    }
    
    
    
    
            
}


