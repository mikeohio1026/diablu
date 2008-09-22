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

package pt.citar.diablu.mailman.util;

import java.util.Vector;
import pt.citar.diablu.mailman.util.datastructures.MailManDeviceClass;


public class MailManMinorDeviceClass {

    public static Vector<MailManDeviceClass> getVector() {
        Vector<MailManDeviceClass> minorDevices = new Vector<MailManDeviceClass>();
        minorDevices.add(new MailManDeviceClass(256, 0, "Unassigned, misc"));
        minorDevices.add(new MailManDeviceClass(256, 4, "Desktop"));
        minorDevices.add(new MailManDeviceClass(256, 8, "Server"));
        minorDevices.add(new MailManDeviceClass(256, 12, "Laptop"));
        minorDevices.add(new MailManDeviceClass(256, 16, "Sub-laptop"));
        minorDevices.add(new MailManDeviceClass(256, 20, "PDA"));
        minorDevices.add(new MailManDeviceClass(256, 24, "watch size"));
        minorDevices.add(new MailManDeviceClass(512, 0, "Unassigned, misc"));
        minorDevices.add(new MailManDeviceClass(512, 4, "Cellular"));
        minorDevices.add(new MailManDeviceClass(512, 8, "Household cordless"));
        minorDevices.add(new MailManDeviceClass(512, 12, "Smartphone"));
        minorDevices.add(new MailManDeviceClass(768, 0, "Fully available"));
        minorDevices.add(new MailManDeviceClass(768, 32, "1-17% utilized"));
        minorDevices.add(new MailManDeviceClass(768, 64, "17-33% utilized"));
        minorDevices.add(new MailManDeviceClass(768, 96, "33-50% utilized"));
        minorDevices.add(new MailManDeviceClass(768, 128, "50-76% utilized"));
        minorDevices.add(new MailManDeviceClass(768, 160, "76-83% utilized"));
        minorDevices.add(new MailManDeviceClass(768, 192, "83-99% utilized"));
        minorDevices.add(new MailManDeviceClass(768, 224, "100% utilized, no service available"));
        minorDevices.add(new MailManDeviceClass(1024, 0, "Unassigned, misc"));
        minorDevices.add(new MailManDeviceClass(1024, 4, "Headset"));
        minorDevices.add(new MailManDeviceClass(1024, 8, "Hands-free device"));
        minorDevices.add(new MailManDeviceClass(1024, 16, "Microphone"));
        minorDevices.add(new MailManDeviceClass(1024, 44, "VCR"));
        minorDevices.add(new MailManDeviceClass(1024, 72, "Video game system"));
        minorDevices.add(new MailManDeviceClass(1280, 64, "Keyboard"));
        minorDevices.add(new MailManDeviceClass(1280, 128, "Mouse, trackball, etc"));
        minorDevices.add(new MailManDeviceClass(1280, 12, "Remote Control"));
        minorDevices.add(new MailManDeviceClass(1536, 16, "Display device"));
        minorDevices.add(new MailManDeviceClass(1536, 32, "Camera"));
        minorDevices.add(new MailManDeviceClass(1536, 64, "Scanner"));
        minorDevices.add(new MailManDeviceClass(1536, 128, "Printer"));
        return minorDevices;
    }
}
