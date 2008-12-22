/*
 * DiABlu SMS2OSC
 * Copyright (C) 2008-2009, CITAR (Research Centre for Science and Technology in Art)
 *
 * This is part of the DiABlu Project, created by Jorge Cardoso - http://diablu.jorgecardoso.eu
 *
 *
 * Contributors:
 * - Pedro Santos <psantos@porto.ucp.pt>
 * - Jorge Cardoso <jccardoso@porto.ucp.pt>
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */

package pt.citar.diablu.sms2osc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import pt.citar.diablu.sms2osc.S2O;

public class S2OProperties {

    S2O s2o;
    Properties properties;

    public S2OProperties(S2O s2o) {
        this.s2o = s2o;
        this.properties = new Properties();
        this.loadProperties();
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
                properties.setProperty("BaudRate", "57600");
                properties.setProperty("Level", "SEVERE");
                properties.setProperty("TableSize", "20");
                properties.setProperty("LogSize", "20");
                properties.store(fos, "SMS2OSC Configuration");

            } catch (IOException ex) {
                s2o.getLogger().log(Level.SEVERE, ex.getMessage());
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    s2o.getLogger().log(Level.SEVERE, ex.getMessage());
                }
            }
        } else {
            try {
                fis = new FileInputStream(file);
                properties.load(fis);
            } catch (FileNotFoundException ex) {
                s2o.getLogger().log(Level.SEVERE, ex.getMessage());
            } catch (IOException ex) {
                s2o.getLogger().log(Level.SEVERE, ex.getMessage());
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    s2o.getLogger().log(Level.SEVERE, ex.getMessage());
                }
            }
        }

    }

    public String getRemoteIP() {
        return properties.getProperty("RemoteIP", "127.0.0.1");
    }

    public String getIncomingPort() {
        return properties.getProperty("IncomingPort", "12000");
    }

    public String getOutgoingPort() {
        return properties.getProperty("OutgoingPort", "12001");
    }

    public String getGateway() {
        return properties.getProperty("Gateway", "sms2osc");
    }

    public String getComPort() {
        return properties.getProperty("ComPort", s2o.getCommPortList().getFirst());
    }

    public boolean useLoopback() {
        if (properties.getProperty("UseLoopback", "false").compareToIgnoreCase("true") == 1) {
            return true;
        }
        return false;
    }

    public boolean useParser() {
        if (properties.getProperty("UseParser", "false").compareToIgnoreCase("true") == 0) {
            return true;
        }
        return false;
    }

    public String getCommands() {
        return properties.getProperty("Commands", "");
    }

    public String getCommandString(String command) {
        return properties.getProperty(command, "");
    }

    public int getTableSize() {
        return Integer.parseInt(properties.getProperty("TableSize", "20"));
    }

    public int getLogSize() {
        return Integer.parseInt(properties.getProperty("LogSize", "20"));
    }

    public Level getLevel() {
        return Level.parse(properties.getProperty("Level", "SEVERE"));
    }

    public void setProperty(String key, String value) {

        File file = new File("config.ini");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            properties.setProperty(key, value);
            properties.store(fos, "SMS2OSC Configuration");

        } catch (IOException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                s2o.getLogger().log(Level.SEVERE, ex.getMessage());
            }
        }

    }

    public String getBaudRate() {
        return properties.getProperty("BaudRate", "57600");
    }
}


