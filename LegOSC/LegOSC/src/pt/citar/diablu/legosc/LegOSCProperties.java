/*
 * LegOSCProperties.java
 *
 * Created on 19 de Julho de 2007, 13:28
 *
 *  NXTComm: A java library to control the NXT Brick.
 *  This is part a of the DiABlu Project (http://diablu.jorgecardoso.org)
 *
 *  Copyright (C) 2007  Jorge Cardoso
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  You can reach me by:
 *  email: jorgecardoso <> ieee org
 *  web: http://jorgecardoso.org
 */

package pt.citar.diablu.legosc;

import java.io.*;
import java.util.*;

/**
 *
 * @author Jorge Cardoso
 */
public class LegOSCProperties {
    
    
    private static final String comments = "LegOSC Properties File\n\n" + 
            "# <legoscport> is the port on which you want LegOSC to listen for OSC messages from \n" +
            "# your application.\n\n"  +
            "# <apphostname> is the hostname or IP address of the computer on which your application\n" +
            "# will be running. \n\n" +
            "# <appport> is the port number on which your application listens for OSC input.\n\n" +
            "# <brickcomport> is the name of the (virtual bluetooth) COM port on which you have connected\n" +
            "# the NXT.\n\n" +
            "# <autosensor> (true or false) if true, LegOSC will periodically send to your application\n" +
            "# OSC messagens with the sensor readings.\n\n" +
            "# <portN> is the type of sensor connected to port number N. This is used with autosensor. ";
    Properties properties;
    
    /** Creates a new instance of LegOSCProperties */
    public LegOSCProperties() {
        properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("legosc.ini");
            properties.load(fis);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public String getLegOSCPort() {
        return properties.getProperty("legoscport", "20000");
    }
    
    public void setLegOSCPort(String port) {
        properties.setProperty("legoscport", port);
    }
    
    public String getAppHostname() {
        return properties.getProperty("apphostname", "localhost");
    }
    
    public void setAppHostname(String hostname) {
        properties.setProperty("apphostname", hostname);
    }
    
    public String getAppPort() {
        return properties.getProperty("appport", "20000");
    }
    
    public void setAppPort(String port) {
        properties.setProperty("appport", port);
    }
    
    public String getBrickCOMPort() {
        return properties.getProperty("brickcomport", "COM1");
    }
    
    public void setBrickCOMPort(String port) {
        properties.setProperty("brickcomport", port);
    }
    
    public boolean getAutoSensor() {
        return properties.getProperty("autosensor", "false").equalsIgnoreCase("true");
    }
    
    public void setAutoSensor(boolean autoSensor) {
        properties.setProperty("autosensor", autoSensor? "true" : "false");
    }
    
    public void setSensorMap(int port, LegOSC.SensorType type) {
        properties.setProperty("port"+port, type.toString());
    }
    
    public LegOSC.SensorType getSensorMap(int port) {
        
        if (properties.getProperty("port"+port, "None").equalsIgnoreCase("none")) {
            return LegOSC.SensorType.NONE;
        } else if (properties.getProperty("port"+port, "None").equalsIgnoreCase("light")) {
            return LegOSC.SensorType.LIGHT;
        } else if (properties.getProperty("port"+port, "None").equalsIgnoreCase("sound")) {
            return LegOSC.SensorType.SOUND;
        } else if (properties.getProperty("port"+port, "None").equalsIgnoreCase("ultrasonic")) {
            return LegOSC.SensorType.ULTRASONIC;
        } else if (properties.getProperty("port"+port, "None").equalsIgnoreCase("pressure")) {
            return LegOSC.SensorType.PRESSURE;
        } else {
            return LegOSC.SensorType.NONE;
        }
    }
    
    public void save() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("legosc.ini");
            properties.store(fos, comments);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }
}
