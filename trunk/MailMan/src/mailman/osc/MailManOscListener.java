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

package mailman.osc;

import mailman.MailMan;
import mailman.osc.commands.MailManBroadcast;
import mailman.util.*;
import mailman.util.datastructures.MailManDevice;
import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import java.io.File;
import java.net.SocketAddress;
import java.util.Vector;

/**
 *
 * @author Raspa
 */
public class MailManOscListener implements OSCListener {

    MailMan mailman;

    public MailManOscListener(MailMan mailman) {
        this.mailman = mailman;
    }

    public void messageReceived(OSCMessage msg, SocketAddress sender, long time) {
        
        String msgRecieved = msg.getName();
        
        for(int i = 0; i <msg.getArgCount(); i++)
        {
            msgRecieved += " " + msg.getArg(i);
        }
        
        mailman.getLogger().log(MailManLogger.OSC_MESSAGE, "Recieved OSC Message: " + msgRecieved);

        if (msg.getName().compareTo("/Diablu/Mailman/SendPath") == 0) {
            if(!mailman.hasMimetypes())
                mailman.getLogger().log(MailManLogger.OTHER, "File not Found: \"mimetypes.txt\". No mimetipe will be used");
            sendPath(msg);
        }
        
        if (msg.getName().compareTo("/Diablu/Mailman/SendPathWithMime") == 0) {
            sendPathWithMime(msg);
        }

         if (msg.getName().compareTo("/Diablu/Mailman/SendPathToGroup") == 0) {
           if(!mailman.hasMimetypes())
                mailman.getLogger().log(MailManLogger.OTHER, "File not Found: \"mimetypes.txt\". No mimetipe will be used");
           sendPathToGroup(msg);
        }
        
        if (msg.getName().compareTo("/Diablu/Mailman/SendPathWithMimeToGroup") == 0) {
            sendPathWithMimeToGroup(msg);
        }
        
        if (msg.getName().compareTo("/Diablu/Mailman/GetGroup") == 0) {
            getGroup(msg);

        }

        if (msg.getName().compareTo("/Diablu/Mailman/GetSentFiles") == 0)
        {
            getSentFiles(msg);
        }
        
        if (msg.getName().compareTo("/Diablu/Mailman/GetReceivedFiles") == 0)
        {
            getRecievedFiles(msg);
        }
        
        if (msg.getName().compareTo("/Diablu/Mailman/Broadcast") == 0)
        {
            if(!mailman.hasMimetypes())
                mailman.getLogger().log(MailManLogger.OTHER, "File not Found: \"mimetypes.txt\". No mimetipe will be used");
            MailManBroadcast bc = new MailManBroadcast(mailman, msg);
            bc.start();
        }
    }
    
    public void sendPath(OSCMessage msg) {

        Vector<String> responses = new Vector<String>();
        String deviceAddress;
        
        if(msg.getArgCount() < 2)
        {
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/WrongArguments"));
            return;
        }
        
        File file = new File((String) msg.getArg(0));
        if(!file.exists())
        {
            mailman.getLogger().log(MailManLogger.OTHER, "File \"" + msg.getArg(0) + "\" does not exist");
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/FileNotFound", new Object[]{(String) msg.getArg(0)}));
            return;
        }
        
        for (int i = 1; i < msg.getArgCount(); i++) {
            deviceAddress = ((String) msg.getArg(i)).toUpperCase();
            
            if(MailManUtil.validDeviceAddress(deviceAddress))
            {
                int responseCode = mailman.getFileSender().send(deviceAddress, (String) msg.getArg(0));
                if (responseCode == -1) {
                    responses.add(deviceAddress);
                }
            }
            else
            {
                 responses.add(deviceAddress);
            }
            
        }

        if (responses.isEmpty()) {
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/OK", new Object[]{(String) msg.getArg(0)}));
        } else {
            Object[] msgArgs = new Object[responses.size() + 1];
            msgArgs[0] = (String) msg.getArg(0);
            for (int i = 0; i < responses.size(); i++) {
                msgArgs[i+1] = responses.elementAt(i);
            }
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/SendFailed", msgArgs));
        }
    }
    
    
    public void sendPathWithMime(OSCMessage msg) {

        Vector<String> responses = new Vector<String>();

        String deviceAddress;
        
        if(msg.getArgCount() < 3)
        {
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/WrongArguments"));
            return;
        }
        
        File file = new File((String) msg.getArg(0));
        if(!file.exists())
        {
            mailman.getLogger().log(MailManLogger.OTHER, "File \"" + msg.getArg(0) + "\" does not exist");
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/FileNotFound", new Object[]{(String) msg.getArg(0)}));
            return;
        }
        
        
        for (int i = 2; i < msg.getArgCount(); i++) {
            deviceAddress = ((String) msg.getArg(i)).toUpperCase();
            if(MailManUtil.validDeviceAddress(deviceAddress))
            {
                int responseCode = mailman.getFileSender().send(deviceAddress, (String) msg.getArg(0));
                if (responseCode == -1) {
                    responses.add(deviceAddress);
                }
            }
            else
            {
                 responses.add(deviceAddress);
            }
        }

        if (responses.isEmpty()) {
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/OK", new Object[]{(String) msg.getArg(0)}));
        } else {
            Object[] msgArgs = new Object[responses.size()];
            for (int i = 0; i < responses.size(); i++) {
                msgArgs[i] = responses.elementAt(i);
                mailman.getKnownDevices().getDevices().remove((String) msgArgs[i]);
            }
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/SendFailed", msgArgs));
        }
    }
    
    public void getGroup(OSCMessage msg) {

        Vector<String> responses = new Vector<String>();
        MailManGroupGetter groupGetter = new MailManGroupGetter(mailman);

        if (msg.getArgCount() != 3) {
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/WrongArguments"));
        }
        mailman.getDiscovery().startDeviceInquiry(groupGetter);

        groupGetter.filterDevices(msg.getArg(0), msg.getArg(1), msg.getArg(2));

        for (MailManDevice d : groupGetter.getFiltered()) {
            responses.add((String) d.getUuid());
        }

        Object[] msgArgs = new Object[responses.size()];
        for (int i = 0; i < responses.size(); i++) {
            msgArgs[i] = responses.elementAt(i);
            System.out.println((String) msgArgs[i]);


        }
        mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/GroupDefinition", msgArgs));

    }

    public void sendPathToGroup(OSCMessage msg) {

        Vector<String> responses = new Vector<String>();
        MailManGroupGetter groupGetter = new MailManGroupGetter(mailman);


        if (msg.getArgCount() != 4) {
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/WrongArguments"));
        }
        mailman.getDiscovery().startDeviceInquiry(groupGetter);

        groupGetter.filterDevices(msg.getArg(1), msg.getArg(2), msg.getArg(3));


        for (MailManDevice d : groupGetter.getFiltered()) {
            responses.add((String) d.getUuid());
        }

        Object[] msgArgs = new Object[responses.size()];
        for (int i = 0; i < responses.size(); i++) {
            msgArgs[i] = responses.elementAt(i);
            System.out.println((String) msgArgs[i]);


        }
       
        String deviceAddress;
        
        for (int i = 1; i < msg.getArgCount(); i++) {
            deviceAddress = (responses.elementAt(i)).toUpperCase();
            mailman.getKnownDevices().add(deviceAddress);
            int responseCode = mailman.getFileSender().send(deviceAddress, (String) msg.getArg(0));
            if (responseCode == -1) {
                responses.add((String) msg.getArg(i));
            }
        }
    }

    

    
    
    
    
    public void sendPathWithMimeToGroup(OSCMessage msg) {

        Vector<String> responses = new Vector<String>();
        Vector<String> failedDevices = new Vector<String>();
        MailManGroupGetter groupGetter = new MailManGroupGetter(mailman);


        if (msg.getArgCount() != 5) {
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/WrongArguments"));
        }
        mailman.getDiscovery().startDeviceInquiry(groupGetter);

        groupGetter.filterDevices(msg.getArg(2), msg.getArg(3), msg.getArg(4));


        for (MailManDevice d : groupGetter.getFiltered()) {
            responses.add((String) d.getUuid());
        }

        Object[] msgArgs = new Object[responses.size()];
        for (int i = 0; i < responses.size(); i++) {
            msgArgs[i] = responses.elementAt(i);
            System.out.println((String) msgArgs[i]);


        }
        mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/GroupDefinition", msgArgs));



       String deviceAddress;
        
        for (int i = 1; i < msg.getArgCount(); i++) {
            deviceAddress = (responses.elementAt(i)).toUpperCase();
            mailman.getKnownDevices().add(deviceAddress);
            int responseCode = mailman.getFileSender().send(deviceAddress, (String) msg.getArg(0));
            if (responseCode == -1) {
                responses.add((String) msg.getArg(i));
            }
        }

    }

    
    public void getSentFiles(OSCMessage msg) {
        
        String deviceAddress = ((String) msg.getArg(0)).toUpperCase();
        
        if (msg.getArgCount() != 1) {
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/WrongArguments"));
        }
        else if(mailman.getKnownDevices().get(deviceAddress) == null)
        {
             mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/UnknownDevice"));
        }
        else
        {
            for(String sentFile : mailman.getKnownDevices().get(deviceAddress).getSentFiles())
                System.out.println(sentFile);
            int argNum = mailman.getKnownDevices().get(deviceAddress).getSentFiles().size();
            Object[] msgArgs = new Object[argNum];
            if(argNum == 0)
                mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/NoFilesSent"));
            else {
                int i = 0;
                for(String sentFile : mailman.getKnownDevices().get(deviceAddress).getSentFiles()) {
                    msgArgs[i] = sentFile;
                    i++;
                }
                mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/FilesSent", msgArgs));
            }
        }
    }
    
    public void getRecievedFiles(OSCMessage msg) {
        
        if (msg.getArgCount() != 1) {
            mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/WrongArguments"));
        }
        else if(mailman.getKnownDevices().get((String) msg.getArg(0)) == null)
        {
             mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/UnknownDevice"));
        }
        else
        {
            int argNum = mailman.getKnownDevices().get((String) msg.getArg(0)).getRecievedFiles().size();
            Object[] msgArgs = new Object[argNum];
            if(argNum == 0)
                mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/NoFilesReceived"));
            else {
                int i = 0;
                for(String receivedFile : mailman.getKnownDevices().get((String) msg.getArg(0)).getRecievedFiles()) {
                    msgArgs[i] = receivedFile;
                    i++;
                }
                mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/FilesReceived", msgArgs));
            }
        }
    }
}



