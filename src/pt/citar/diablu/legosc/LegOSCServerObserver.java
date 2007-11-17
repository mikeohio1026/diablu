/*
 * LegOSCServerObserver.java
 *
 * Created on 30 de Janeiro de 2007, 12:31
 *
 *  LegOSC: An OSC gateway to control the NXT Brick.
 *  This is part a of the DiABlu Project (http://diablu.jorgecardoso.org)
 *
 *  Copyright (C) 2007  Jorge Cardoso
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  You can reach me by:
 *  email: jorgecardoso <> ieee org
 *  web: http://jorgecardoso.org
 */

package pt.citar.diablu.legosc;

/**
 *
 * @author jccardoso
 */
public interface LegOSCServerObserver {
    
    /**
     * Indicates an error message produced by LegOSC.
     * @param error The String description of the error message.
     */
    public void legOSCError(String error);
    
    /**
     * Indicates a message produced by LegOSC.
     * @param error The String description of the message.
     */    
    public void legOSCMessage(String message);
    
    /**
     * Indicates a verbose message produced by LegOSC.
     * @param error The String description of the verbose message.
     */     
    public void legOSCVerbose(String message);
    
    /**
     * Indicates if LegOSC was really started or not (in response to the start command).
     * @param started true, if LegOSC started ok; false, otherwise.
     */
    public void legOSCStarted(boolean started);
}
