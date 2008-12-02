package pt.citar.diablu.sms2osc.parser;

import de.sciss.net.OSCMessage;
import java.util.HashMap;
import java.util.StringTokenizer;
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
            System.out.println(s2o.getProperties().getCommandString(command));
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
                } else {
                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/commandError1", new Object[]{msg}));
                }
            } else {
                for (int i = 0; i < value.length(); i++) {
                    System.out.println("lsls");
                    if (st.hasMoreTokens()) {
                        c = value.charAt(i);
                        switch (c) {
                            case 'i': {
                                try {
                                    arguments[i] = Integer.parseInt(st.nextToken());
                                    System.out.println(arguments[i]);
                                    break;
                                } catch (NumberFormatException ex) {
                                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/commandError2", new Object[]{msg}));
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
                                        System.out.println(arguments[i]);
                                        break;
                                    }
                                } catch (NumberFormatException ex) {
                                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/commandError3", new Object[]{msg}));
                                    return;
                                }
                            }
                            case 'w': {
                                if (st.hasMoreTokens()) {
                                    arguments[i] = st.nextToken();
                                    System.out.println(arguments[i]);
                                    break;
                                } else {
                                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/commandError4", new Object[]{msg}));
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
                                System.out.println(arguments[i]);
                                break;
                            }
                            case 'c': {
                                if (st.hasMoreElements()) {
                                    s = st.nextToken();
                                    if (s.length() == 1) {
                                        String l = "" + s.charAt(0);
                                        arguments[i] = l;
                                        System.out.println(arguments[i]);
                                        break;
                                    } else {
                                        s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/commandError5", new Object[]{msg}));
                                        return;
                                    }
                                } else {
                                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/commandError", new Object[]{msg}));
                                    return;
                                }
                            }
                        }
                    }
                }
                s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/" + command, arguments));
            }
        } else {
            System.out.println("Command does not exist: " + command);
            s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/commandNotFounf", new Object[] {command, msg}));
        }
    }
}
