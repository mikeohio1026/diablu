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

package pt.citar.diablu.mailman.bluetooth;

import pt.citar.diablu.mailman.MailMan;
import pt.citar.diablu.mailman.util.MailManLogger;
import de.sciss.net.OSCMessage;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Vector;
import javax.obex.*;

public class MailManBTRequestHandler extends ServerRequestHandler {

    private MailMan mailman;
    private boolean hasAddress;
    private boolean waitingForAddress;
    private Object addressLock;
    private String address;
    private String originalFilename;
    private String mimetype;
    private int bytesRead = 0;

    @Override
    public int onPut(Operation op) {
        try {

            HeaderSet hs = op.getReceivedHeaders();
            originalFilename = (String) hs.getHeader(HeaderSet.NAME);
            int fileSize = Integer.parseInt((String) hs.getHeader(HeaderSet.LENGTH).toString());
            mimetype = (String) hs.getHeader(HeaderSet.TYPE);
            
            if(mimetype == null)
                mimetype = "";

            File directory = new File(mailman.getGui().getDirectory());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String legalFilename = removeIllegalCharacters(originalFilename);
            String finalFilename = mailman.getGui().getDirectory() + System.getProperty("file.separator") + getFinalFilename(legalFilename);
            String filename = writeFile(finalFilename, op);
            
            File file = new File(filename);
            

            if (!hasAddress) {
                waitingForAddress = true;
                synchronized (addressLock) {
                    addressLock.wait();
                }
            }

                

            if (fileSize == bytesRead) {
                mailman.getKnownDevices().addReceivedFiles(address, filename);
                mailman.getLogger().log(MailManLogger.BT_FILE_TRANSFER, "Received file \"" + filename + "\" from " + address);
                String friendlyName = mailman.getKnownDevices().get(address).getFriendlyName();
                mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/ReceivePath", new Object[]{address, friendlyName, originalFilename, mimetype, file.getAbsolutePath()}));
                mailman.updateDeviceFiles();
            } else {
                mailman.getLogger().log(MailManLogger.BT_FILE_TRANSFER, "Connection Interrupted while recieving \"" + filename + "\" from " + address);
                file.delete();
            }



            return ResponseCodes.OBEX_HTTP_OK;

        } catch (InterruptedException ex) {
            mailman.getLogger().log(MailManLogger.OTHER, "File reception failed");
            return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
        } catch (IOException ex) {
            mailman.getLogger().log(MailManLogger.OTHER, "File reception failed");
            return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
        }


    }

    public MailManBTRequestHandler(MailMan mailman) {
        this.mailman = mailman;
        this.hasAddress = false;
        this.addressLock = new Object();
        this.waitingForAddress = false;
    }

    public void setAddress(String address) {
        this.address = address;
        this.hasAddress = true;

        if (waitingForAddress) {
            synchronized (addressLock) {
                addressLock.notify();
            }
        }
        if(mailman.getKnownDevices().get(address) == null)
            mailman.getLogger().log(MailManLogger.DEVICE_DETECTED, "New bluetooth device detected: " + address);
                
        mailman.getKnownDevices().add(address);
    }

    private String getFinalFilename(String filename) {
        File file = new File(filename);
        String path;
        String extention = "";
        
        String []reserved = {"com1", "com2", "com3", "com4", "com5", "com6", "com7",
                             "com8", "com9", "lpt1", "lpt2", "lpt3", "lpt4", "lpt5", 
                             "lpt6", "lpt7", "lpt8", "lpt9", "con", "nul", "prn"};
        
        Vector r = new Vector(Arrays.asList(reserved));
        
        while(filename.indexOf(' ') == 0 || filename.indexOf('.') == 0)
            filename = filename.substring(1);
        
        while(filename.lastIndexOf(' ') == filename.length() - 1 || filename.lastIndexOf('.') == filename.length() - 1)
            filename = filename.substring(0, filename.length() - 1);
        
        
        
        if(filename.lastIndexOf('.') > -1)
            extention = filename.substring(filename.lastIndexOf('.'), filename.length());
        
        if(r.contains(filename))
        {
            Random rand = new Random();
            filename = Integer.toString(rand.nextInt(10000)) + extention;
        }
        
        int i = 0;
        while (file.exists()) {
            i++;
            if(filename.lastIndexOf('.') > -1)
                path = filename.substring(0, filename.lastIndexOf('.'));
            else
                path = filename;
            if(i > 1)
                path = path.substring(0, path.length() - Integer.toString(i-1).length()).concat("" + i).concat(extention);
            else
                path = path.concat("" + i).concat(extention);
            filename = path;
            file = new File(filename);
        }
        
        
        return filename;
    }

    public String writeFile(String filename, Operation op) throws IOException {

        InputStream is = op.openInputStream();
        FileOutputStream os; 

        os = new FileOutputStream(new File(filename));
            
        int data;

        while (((data) = is.read()) != -1) {
            os.write(data);
            bytesRead++;
        }

        is.close();
        os.close();
        op.close();

        return filename;

    }
    
    public String removeIllegalCharacters(String filename)
    {
        String charsToRemove = "/?<>\\:*|\"";
        
        for(int i = 0; i < charsToRemove.length(); i++)
        {
            filename = filename.replace(charsToRemove.charAt(i), '_');
        }
        return filename;
    }
}


