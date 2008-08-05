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
package mailman.util;

import java.util.StringTokenizer;

public class MailManUtil {
   

    
public static boolean validDeviceAddress(String deviceAddress)
{
    
    if(deviceAddress.length() != 12)
        return false;
    for(int i = 0; i < deviceAddress.length(); i++)
    {
        if(!isHexDigit(deviceAddress.charAt(i)))

            return false;
     
            
    }
    return true;
}

private static boolean isHexDigit(char c)
{
    if((c >= 48 && c <= 57) || (c >=65 && c <= 70) || (c >= 97 && c <= 102))
        return true;
    return false;
}

public static boolean useServiceDeviceClass() {
    String os = System.getProperty("os.name");
    String version = System.getProperty("os.version");
    
    if(os.compareToIgnoreCase("Mac OS X") == 0)
    {
        StringTokenizer st = new StringTokenizer(version, ".", false);
        if(Integer.parseInt(st.nextToken()) >= 10)
            if(Integer.parseInt(st.nextToken()) > 4 )
                return true;
        return false;
    }
    
    return true;
}

}


