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

package mailman.util.datastructures;


public class MailManDeviceClass {
    
    private int major;
    private int minor;
    private String description;
    
    
    public MailManDeviceClass(int major, String description)
    {
        
        this.major = major;
        this.description = description;
    }
    
    public MailManDeviceClass(int major, int minor, String description)
    {
        
        this.major = major;
        this.minor = minor;
        this.description = description;
    }
    

    public String getDescription() {
        return description;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }
    

}
