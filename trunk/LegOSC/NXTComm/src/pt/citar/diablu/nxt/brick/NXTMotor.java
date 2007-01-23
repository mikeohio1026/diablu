/*
 * NXTMotor.java
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
import java.io.IOException;
import pt.citar.diablu.nxt.protocol.*;

/**
 *
 * @author Jorge Cardoso
 */
public class NXTMotor {
    
    /**
     * The value of tacho limit that means forever.
     */
    private static final int TACHO_LIMIT_FOREVER = 0;
    
    /**
     * The NXT Brick.
     */
    private NXTBrick brick;
    
    /**
     * The command used to control the motor.
     */
    private NXTCommandSetOutputState outputState;
    
    /**
     * The port to which this motor is connected. (0 - 2)
     */
    private byte portAttached;
    
    /** Creates a new instance of NXTMotor */
    public NXTMotor(NXTBrick brick, byte portAttached) {
        this.brick = brick;
        this.portAttached = portAttached;
        outputState = new NXTCommandSetOutputState(portAttached, (byte)30, 
                (byte)(NXTCommandSetOutputState.MODE_MOTOR_ON | NXTCommandSetOutputState.MODE_REGULATED),
                NXTCommandSetOutputState.REGULATION_MODE_MOTOR_SPEED, 
                (byte)0, NXTCommandSetOutputState.RUN_STATE_RUNNING, TACHO_LIMIT_FOREVER);
    }
    
    
    /**
     * Make the motor go forward, ramping up the speed.
     *
     * @param power The power of the motor.
     * @return True, if no error; False otherwise.
     */
    public boolean forward(byte power) {
        if (power > 100 || power < -100) {
            System.out.println("Power must be between -100 and 100.");
            return false;
        }
        try {
            outputState.setMode((byte)(NXTCommandSetOutputState.MODE_MOTOR_ON | 
                     NXTCommandSetOutputState.MODE_REGULATED));
            
            // make it ramp up
            outputState.setRunState(NXTCommandSetOutputState.RUN_STATE_RUNNING);
            
            // final speed
            outputState.setPowerSetPoint(power);
            
            // go forever
            outputState.setTachoLimit(TACHO_LIMIT_FOREVER);
            
            // send command
            System.out.println("Sending command");
            brick.getChannel().sendCommand(outputState);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * Stops the motor slowly.
     * 
     * @return True, if no error; False otherwise.
     */
    public boolean slowStop() {
         try {
             /* must ensure that brakes aren't on */
             outputState.setMode((byte)(NXTCommandSetOutputState.MODE_MOTOR_ON | 
                     NXTCommandSetOutputState.MODE_REGULATED));
             
             outputState.setRunState(NXTCommandSetOutputState.RUN_STATE_RAMP_DOWN);
             
             outputState.setPowerSetPoint((byte)0);
             /* TODO: See if we need to set a tacho limit.*/
             brick.getChannel().sendCommand(outputState);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;   
    }
    
    public boolean handBrake() {
          try {
             /* must ensure that brakes aren't on */
             outputState.setMode((byte)(NXTCommandSetOutputState.MODE_MOTOR_ON | NXTCommandSetOutputState.MODE_BRAKE | 
                     NXTCommandSetOutputState.MODE_REGULATED));
             
             outputState.setRunState(NXTCommandSetOutputState.RUN_STATE_RUNNING);
             
             outputState.setPowerSetPoint((byte)0);
             
             /* TODO: See if we need to set a tacho limit.*/
             brick.getChannel().sendCommand(outputState);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;      
    }
    
    public boolean fastStop() {
        return false;
    
    }
    
}
