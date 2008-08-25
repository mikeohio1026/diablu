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

package pt.citar.mailman.osc.commands;

import pt.citar.mailman.MailMan;
import pt.citar.mailman.util.MailManGroupGetter;
import pt.citar.mailman.util.datastructures.MailManDevice;
import pt.citar.mailman.util.datastructures.MailManRemoteDevice;
import pt.citar.mailman.util.MailManLogger;
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
public class MailManBroadcast implements Runnable{

    private MailMan mailman;
    private OSCMessage msg;
    private Vector<String> responses = new Vector<String>();
    private String file;
    private int totalTime;
    private int intervalTime;
    private Vector<String> devicesToSend = new Vector<String>();
    private Object lock = new Object();
    private boolean stop;
    private boolean hasMimetype;
    private String mimetype;

    public MailManBroadcast(MailMan mailman, OSCMessage msg) {
        this.mailman = mailman;
        this.msg = msg;
        this.stop = false;
        this.hasMimetype = false;
    }
    
    public MailManBroadcast(MailMan mailman, OSCMessage msg, String mimetype) {
        this.mailman = mailman;
        this.msg = msg;
        this.stop = false;
        this.mimetype = mimetype;
        this.hasMimetype = true;
    }

    private int parseMessage() {
        if (hasMimetype) {
            if (msg.getArgCount() != 7) {
                mailman.getOscClient().send(new OSCMessage("/WrongArguments"));
                return -1;
            } else {
                file = (String) msg.getArg(0);
                totalTime = Integer.parseInt((String) msg.getArg(5));
                intervalTime = Integer.parseInt((String) msg.getArg(6));
            }
        }
        
        else {
            if (msg.getArgCount() != 6) {
                mailman.getOscClient().send(new OSCMessage("/WrongArguments"));
                return -1;
            } else {
                file = (String) msg.getArg(0);
                totalTime = Integer.parseInt((String) msg.getArg(4));
                intervalTime = Integer.parseInt((String) msg.getArg(5));
            }
        }
        return 0;
    }


    

    private void discovery() {
        
        MailManGroupGetter groupGetter = new MailManGroupGetter(mailman);
        mailman.getDiscovery().startDeviceInquiry(groupGetter);
        
        if(!hasMimetype)
            groupGetter.filterDevices(msg.getArg(1), msg.getArg(2), msg.getArg(3));
        else
            groupGetter.filterDevices(msg.getArg(2), msg.getArg(3), msg.getArg(4));
            

        for (MailManDevice d : groupGetter.getFiltered()) {
            if (!responses.contains(d.getUuid())) {
                addDevice(d.getUuid());
                responses.add(d.getUuid());
                devicesToSend.add(d.getUuid());
            }

        }
        
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

    public void run() {
        
        
        String newDevices = "";
        if(parseMessage() < 0)
            return;
        
        Calendar cal = Calendar.getInstance();

        long initialTime = cal.getTimeInMillis() / 1000;
        long finalTime = initialTime + totalTime;
        long intervalStart = initialTime;
        long currentTime = initialTime;

        if (totalTime == 0) {
            mailman.getLogger().log(MailManLogger.OTHER, "Searching for devices");
            discovery();
            
            newDevices = new String("");
            if (devicesToSend.size() > 0) {
                for (String d : devicesToSend) {
                    newDevices += " " + d;
                }
                mailman.getLogger().log(MailManLogger.DEVICE_DETECTED, "Devices Found:" + newDevices);
            } else {
                mailman.getLogger().log(MailManLogger.DEVICE_DETECTED, "No devices were found");
            }

            for (String deviceUUID : devicesToSend) {
                mailman.getLogger().log(MailManLogger.OTHER, "Broadcasting: " + file);
                if (hasMimetype)
                    mailman.getFileSender().sendWithMime(deviceUUID, file, mimetype);
                else
                    mailman.getFileSender().send(deviceUUID, file);
                
            }
            
            devicesToSend.clear();
            
        } 
        else {
            while (currentTime <= finalTime && !stop) {
                cal = Calendar.getInstance();
                intervalStart = cal.getTimeInMillis() / 1000;
                mailman.getLogger().log(MailManLogger.OTHER, "Searching for devices");
                discovery();
                newDevices = new String("");
                         
                if(devicesToSend.size() > 0)
                {
                    for(String d: devicesToSend)
                        newDevices += " " + d; 
                        mailman.getLogger().log(MailManLogger.DEVICE_DETECTED, "Devices Found:" + newDevices);
                }
                else
                {
                    mailman.getLogger().log(MailManLogger.DEVICE_DETECTED, "No new devices were found");
                }
                
                
                
               

                for (String deviceUUID : devicesToSend) {
                    addDevice(deviceUUID);
                    mailman.getLogger().log(MailManLogger.OTHER, "Broadcasting: " + file + " to " + deviceUUID);

                    if (hasMimetype) {
                        mailman.getFileSender().sendWithMime(deviceUUID, file, mimetype);
                    } else {
                        mailman.getFileSender().send(deviceUUID, file);
                    }
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
            }
        }
    }
}
