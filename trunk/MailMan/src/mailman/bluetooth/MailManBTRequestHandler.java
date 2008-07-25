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

package mailman.bluetooth;

import mailman.*;
import mailman.util.MailManLogger;
import de.sciss.net.OSCMessage;
import java.io.*;
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

            File file = getFinalFilename(mailman.getGui().getDirectory() + "\\" + originalFilename);

            String filename = writeFile(file, op);

            if (!hasAddress) {
                waitingForAddress = true;
                synchronized (addressLock) {
                    addressLock.wait();
                }
            }

                

            if (fileSize == bytesRead) {
                mailman.getKnownDevices().addRecievedFiles(address, filename);
                mailman.getLogger().log(MailManLogger.BT_FILE_TRANSFER, "Received file \"" + filename + "\" from " + address);
                mailman.getOscClient().send(new OSCMessage("/Diablu/Mailman/ReceivePath", new Object[]{address, originalFilename, mimetype, file.getAbsolutePath()}));
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

    private File getFinalFilename(String filename) {
        File file = new File(filename);
        int i = 0;
        while (file.exists()) {
            i++;
            String extention;
            String path;
            extention = filename.substring(filename.lastIndexOf('.'), filename.length());
            path = filename.substring(0, filename.lastIndexOf('.'));
            path = path.concat("" + i).concat(extention);
            file = new File(path);
        }
        return file;
    }

    public String writeFile(File file, Operation op) throws IOException {

        InputStream is = op.openInputStream();

        String filename = file.getName();

        FileOutputStream os = new FileOutputStream(file);
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
}


