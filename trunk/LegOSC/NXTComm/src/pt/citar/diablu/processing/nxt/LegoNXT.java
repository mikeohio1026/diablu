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
import pt.citar.diablu.nxt.protocol.NXTCommBluetoothSerialChannel;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;

/**
 * TODO: CLose port on program stop.
 *
 * @author Jorge Cardoso
 */
public class LegoNXT {
    public static final int MOTOR_A = 0;
    public static final int MOTOR_B = 1;
    public static final int MOTOR_C = 2;
    
    /**
     *  TODO: Don't know if this will be necessary yet...
     * The parent PApplet. 
     */
    private PApplet parent;
    
    /**
     * The bluetooth nxt channel.
     */
    NXTCommBluetoothSerialChannel btChannel;
    
    /**
     * The NXTBrick object.
     */
    private NXTBrick brick;
    
    /**
     * The NXT Speaker.
     */
    private NXTSpeaker speaker;
    
    /**
     * The NXT motors.
     */
    private NXTMotor motor[];
    
    /**
     * The button sensor.
     */
    NXTButtonSensor buttonSensor;    
    
    Object sensorPorts[];
    
    /** Creates a new instance of LegoNXT */
    public LegoNXT(PApplet parent, String commPort) {
        this.parent = parent;
        
        /* register calls */
        parent.registerDispose(this);
        /* open bt channel */
        try {
            btChannel = new NXTCommBluetoothSerialChannel(commPort);
        } catch (IOException ex) {
            ex.printStackTrace();
  
        } catch (PortInUseException ex) {
            ex.printStackTrace();

        } catch (NoSuchPortException ex) {
            ex.printStackTrace();

        } catch (UnsupportedCommOperationException ex) {
            ex.printStackTrace();
        }
        
         // make the brick
        brick = new NXTBrick(btChannel);
                 
        // make the motors
        motor = new NXTMotor[3];
        motor[0] = new NXTMotor(brick, (byte)0);
        motor[1] = new NXTMotor(brick, (byte)1);
        motor[2] = new NXTMotor(brick, (byte)2);
        
        // make the sensors
        sensorPorts = new Object[4];
        
    }
   
        
    
    public boolean playTone(int frequency, int duration) {
        return speaker.playTone(frequency, duration);
    }
    
    
    public boolean motorForward(int motorNumber, int power) {
        if (motorNumber > 2) {
            System.err.println("Motor number must be 0, 1 or 2.");
            return false;
        }
        if (power > 100) {
            power = 100;
        } else if (power < -100) {
            power = -100;
        }
        System.out.println("starting motor");
        return motor[motorNumber].forward((byte)power);
    }
    
    public boolean motorForwardLimit(int motorNumber, int power, int tachoLimit) {
        if (motorNumber > 2) {
            System.err.println("Motor number must be 0, 1 or 2.");
            return false;
        }
        if (power > 100) {
            power = 100;
        } else if (power < -100) {
            power = -100;
        }
        System.out.println("starting motor");
        return motor[motorNumber].forwardLimit((byte)power, tachoLimit);        
    }
    
    public boolean motorHandBrake(int motorNumber) {
        if (motorNumber > 2) {
            System.err.println("Motor number must be 0, 1 or 2.");
            return false;
        }
        return motor[motorNumber].handBrake();        
    }
    
    public boolean motorStop(int motorNumber) {
        if (motorNumber > 2) {
            System.err.println("Motor number must be 0, 1 or 2.");
            return false;
        }
        return motor[motorNumber].slowStop();
    }
    
    public boolean getButtonState(int portNumber) {
        
        if (portNumber > sensorPorts.length-1) {
            System.err.println("Port number too large!");
            return false;
        }
        if(sensorPorts[portNumber] == null || !(sensorPorts[portNumber] instanceof NXTButtonSensor)) {
            sensorPorts[portNumber] = new NXTButtonSensor(brick, (byte)portNumber);
        }
        return ((NXTButtonSensor)sensorPorts[portNumber]).isButtonPressed();        
    }
            
    
    public int getDB(int portNumber) {
        if (portNumber > sensorPorts.length-1) {
            System.err.println("Port number too large!");
            return -1;
        }
        if(sensorPorts[portNumber] == null || !(sensorPorts[portNumber] instanceof NXTSoundSensor)) {
            sensorPorts[portNumber] = new NXTSoundSensor(brick, (byte)portNumber);
        }
        return ((NXTSoundSensor)sensorPorts[portNumber]).getDB();              
    }
    
    public void dispose() {
        try {            
            btChannel.closeChannel();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
