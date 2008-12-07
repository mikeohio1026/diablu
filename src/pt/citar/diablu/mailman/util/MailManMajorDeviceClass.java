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

public class MailManMajorDeviceClass {
    
    public static Vector<MailManDeviceClass> getVector()
    {
        Vector<MailManDeviceClass> majorDevices = new Vector<MailManDeviceClass>();
        majorDevices.add(new MailManDeviceClass(0, "Misc. Major device"));
        majorDevices.add(new MailManDeviceClass(256, "Computer"));
        majorDevices.add(new MailManDeviceClass(512, "Phone"));
        majorDevices.add(new MailManDeviceClass(512, "Phone modem"));
        majorDevices.add(new MailManDeviceClass(768, "LAN/network access point"));
        majorDevices.add(new MailManDeviceClass(1024, "Audio/video device"));
        majorDevices.add(new MailManDeviceClass(1280, "Computer periferical"));
        majorDevices.add(new MailManDeviceClass(1536, "Imaging device"));
        majorDevices.add(new MailManDeviceClass(7936, "Unclassifieed major device"));
        return majorDevices;
    }

}
