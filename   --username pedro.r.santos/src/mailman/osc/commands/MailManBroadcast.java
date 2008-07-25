/*
 * DiABlu Mailman
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

package mailman.osc.commands;

import mailman.MailMan;
import mailman.util.datastructures.MailManDevice;
import mailman.util.datastructures.MailManRemoteDevice;
import mailman.util.MailManLogger;
import de.sciss.net.OSCMessage;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;

/**
 *
 * @author Raspa
 */
public class MailManBroadcast {

    private MailMan mailman;
    private OSCMessage msg;
    private Vector<String> responses = new Vector<String>();
    private String file;
    private int totalTime;
    private int intervalTime;
    private Vector<String> sentDevices = new Vector<String>();
    private Vector<String> devicesToSend = new Vector<String>();
    private Object lock = new Object();
    private boolean stop;

    public MailManBroadcast(MailMan mailman, OSCMessage msg) {
        this.mailman = mailman;
        this.msg = msg;
        this.stop = false;
    }

    private void parseMessage() {
        if (msg.getArgCount() != 6) {
            mailman.getOscClient().send(new OSCMessage("/WrongArguments"));
        }

        file = (String) msg.getArg(0);
        totalTime = Integer.parseInt((String) msg.getArg(4));
        intervalTime = Integer.parseInt((String) msg.getArg(5));
    }

    private void discovery() {
        mailman.getDiscovery().startDeviceInquiry();

        mailman.getGroupGetter().filterDevices(msg.getArg(1), msg.getArg(2), msg.getArg(3));

        for (MailManDevice d : mailman.getGroupGetter().getFiltered()) {
            System.out.println(d.getUuid());

            if (!responses.contains(d.getUuid())) {
                addDevice(d.getUuid());
                responses.add(d.getUuid());
                devicesToSend.add(d.getUuid());
            }

        }
        mailman.getGroupGetter().clear();
    }

    public void addDevice(String deviceUUID) {
        try
        {
        if (!mailman.getKnownDevices().getDevices().containsKey(deviceUUID)) {
            MailManDevice device = new MailManDevice((RemoteDevice) new MailManRemoteDevice(deviceUUID));
            mailman.getKnownDevices().getDevices().put(deviceUUID, device);
            mailman.getDiscovery().searchServices(device);


        }
        }catch(RuntimeException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        
    }

    public void start() {

        parseMessage();
        Calendar cal = Calendar.getInstance();

        long initialTime = cal.getTimeInMillis() / 1000;
        long finalTime = initialTime + totalTime;
        long intervalStart = initialTime;
        long currentTime = initialTime;

        if (totalTime == 0) {
            discovery();

            for (String deviceUUID : devicesToSend) {
                mailman.getFileSender().send((String) deviceUUID, file);
                mailman.getLogger().log(MailManLogger.OTHER, "Broadcasting: " + file);
            }
            
            devicesToSend.clear();
            
        } 
        else {
            while (currentTime <= finalTime && !stop) {
                cal = Calendar.getInstance();
                intervalStart = cal.getTimeInMillis() / 1000;
                discovery();
                
               

                for (String deviceUUID : devicesToSend) {
                    mailman.getKnownDevices().add(deviceUUID);
                    mailman.getFileSender().send((String) deviceUUID, file);
                    mailman.getLogger().log(MailManLogger.OTHER, "Broadcasting: " + file +" to " + deviceUUID);
                }
                devicesToSend.clear();

                cal = Calendar.getInstance();
                currentTime = cal.getTimeInMillis() / 1000;
                synchronized (lock) {
                    if (currentTime - intervalStart < intervalTime) {
                        try {
                            lock.wait((intervalTime - (currentTime - intervalStart)) * 1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MailManBroadcast.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                cal = Calendar.getInstance();
                currentTime = cal.getTimeInMillis() / 1000;
                System.out.println(currentTime + " " + finalTime);
            }
        }
    }
}
