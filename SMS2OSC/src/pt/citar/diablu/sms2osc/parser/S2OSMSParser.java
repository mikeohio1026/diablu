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

package pt.citar.diablu.sms2osc.parser;

import de.sciss.net.OSCMessage;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import pt.citar.diablu.sms2osc.S2O;

public class S2OSMSParser {

    S2O s2o;
    boolean active;
    HashMap<String, String> commands;

    public S2OSMSParser(S2O s2o, boolean active) {

        this.s2o = s2o;
        this.active = active;
        this.commands = new HashMap<String, String>();

    }

    public boolean isActive() {
        return active;
    }

    public void getCommands(String commandString) {
        StringTokenizer st = new StringTokenizer(commandString, ";");
        String command;
        while (st.hasMoreTokens()) {
            command = st.nextToken();
            commands.put(command, s2o.getProperties().getCommandString(command));
        }
    }

    public void parse(String msg) {
        StringTokenizer st = new StringTokenizer(msg, "\n ");
        String command = st.nextToken();

        if (commands.containsKey(command)) {
            String value = commands.get(command);
            Object[] arguments = new Object[value.length()];
            char c;
            float f;
            String s;
            boolean first;

            if (value.length() == 0) {
                if (!st.hasMoreTokens()) {
                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/" + command));
                    s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/" + command);
                } else {
                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/argumentsError", new Object[]{msg}));
                    s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/argumentsError" + msg);
                }
            } else {
                for (int i = 0; i < value.length(); i++) {
                    if (st.hasMoreTokens()) {
                        c = value.charAt(i);
                        switch (c) {
                            case 'i': {
                                try {
                                    arguments[i] = Integer.parseInt(st.nextToken());
                                    break;
                                } catch (NumberFormatException ex) {
                                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/argumentsError", new Object[]{msg}));
                                    s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/argumentsError" + msg);
                                    return;
                                }
                            }
                            case 'f': {
                                try {
                                    StringTokenizer floatSt = new StringTokenizer(st.nextToken(), ".,");
                                    if (floatSt.countTokens() == 2) {
                                        f = Integer.parseInt(floatSt.nextToken());
                                        String decimal = floatSt.nextToken();
                                        f += Integer.parseInt(decimal) / (Math.pow(10.0, decimal.length()));
                                        arguments[i] = f;
                                        break;
                                    }
                                } catch (NumberFormatException ex) {
                                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/argumentsError", new Object[]{msg}));
                                    s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/argumentsError" + msg);
                                    return;
                                }
                            }
                            case 'w': {
                                if (st.hasMoreTokens()) {
                                    arguments[i] = st.nextToken();
                                    break;
                                } else {
                                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/argumentsError", new Object[]{msg}));
                                    s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/argumentsError" + msg);
                                    return;
                                }
                            }
                            case 's': {
                                s = "";
                                first = true;
                                while (st.hasMoreElements()) {
                                    if (first) {
                                        s = st.nextToken();
                                        first = false;
                                    } else {
                                        s = s + " " + st.nextToken();
                                    }
                                }
                                arguments[i] = s;
                                break;
                            }
                            case 'c': {
                                if (st.hasMoreElements()) {
                                    s = st.nextToken();
                                    if (s.length() == 1) {
                                        String l = "" + s.charAt(0);
                                        arguments[i] = l;
                                        break;
                                    } else {
                                        s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/argumentsError", new Object[]{msg}));
                                        s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/argumentsError" + msg);
                                        return;
                                    }
                                } else {
                                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/argumentsError", new Object[]{msg}));
                                    s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/argumentsError" + msg);
                                    return;
                                }
                            }
                        }
                    }
                }
                s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/" + command, arguments));
                s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/" + command + " " + msg.substring(command.length() + 1, msg.length()));
            }
        } else {
            s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/commandNotFound", new Object[]{msg}));
            s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/commandNotFound" + command);
        }
    }
}
