/*
 * LegoNXT.java
 *
 * Created on 22 de Janeiro de 2007, 0:21
 *
 *  LegoNXT: A Processing library to control the NXT Brick.
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
 *  You can reach me by
 *  email: jorgecardoso <> ieee org
 *  web: http://jorgecardoso.org
 */

package pt.citar.diablu.processing.nxt;

import processing.core.*;
import pt.citar.diablu.nxt.brick.*;

/**
 * TODO: CLose port on program stop.
 *
 * @author Jorge Cardoso
 */
public class LegoNXT {
    
    /**
     *  TODO: Don't know if this will be necessary yet...
     * The parent PApplet. 
     */
    private PApplet parent;
    
    /**
     * The NXTBrick object.
     */
    private NXTBrick brick;
    
    private NXTSpeaker speaker;
    
    /** Creates a new instance of LegoNXT */
    public LegoNXT(PApplet parent) {
        this.parent = parent;
    }
    
    /**
     * Tells the system to use the specified comm port to communicate with the NXT Brick.
     *
     * @param commPort The comm port name (e.g., COM3 on windows).
     * 
     * @return True if the port was opened successulfully; False otherwise.
     */
    public boolean useCommPort(String commPort) {
        brick = new NXTBrick();
        String result = brick.openChannel(commPort);
        if (result != null) {
            System.err.println(result);
            return false;
        }     
        speaker = new NXTSpeaker(brick);
        return true;
    }
    
    
    public boolean playTone(int frequency, int duration) {
        return speaker.playTone(frequency, duration);
    }
}
