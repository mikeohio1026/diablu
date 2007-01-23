/*
 * NXTBrick.java
 *
 * Created on 21 de Janeiro de 2007, 14:46
 *
 *  NXTComm: A java library to control the NXT Brick.
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

package pt.citar.diablu.nxt.brick;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.nio.channels.Channel;
import pt.citar.diablu.nxt.protocol.*;

/**
 *
 * @author Jorge Cardoso
 */
public class NXTBrick {
    
    private static NXTCommBluetoothSerialChannel channel;
    
    /** Creates a new instance of NXTBrick */
    public NXTBrick() {
    }
    
    
    public String openChannel(String commPort) {
        try {
            channel = new NXTCommBluetoothSerialChannel(commPort);
        } catch (IOException ex) {
            ex.printStackTrace();
            return "IOException: " + ex.getMessage();
        } catch (PortInUseException ex) {
             ex.printStackTrace();
            return "PortInUseException: " + ex.getMessage();
        } catch (NoSuchPortException ex) {
            ex.printStackTrace();
            return "NoSuchPortException: " + ex.getMessage();
        } catch (UnsupportedCommOperationException ex) {
            ex.printStackTrace();
            return "UnsupportedCommOperationException: " + ex.getMessage();
        }
        return null;
    }
    
    public NXTCommChannel getChannel() {
        return channel;
    }
    
    public void closeChannel() {
        try {
            channel.closeChannel();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
