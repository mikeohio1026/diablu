/*
 * LegOSC.java
 *
 * Created on 23 de Janeiro de 2007, 0:13
 *
 *  LegOSC: An OSC gateway to control the NXT Brick.
 *  This is part of the DiABlu Project (http://diablu.jorgecardoso.org)
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

import de.sciss.net.*;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;


import pt.citar.diablu.nxt.brick.*;
import pt.citar.diablu.nxt.protocol.*;

/**
 *
 * @todo Implement OSC messages to configure polling.
 * @author Jorge Cardoso
 */
public class LegOSC implements OSCListener, Runnable, LegOSCWindowObserver {
    
    /**
     * The sensor types used for the mapping between port and sensor type.
     */
    public enum SensorType {NONE, SOUND, LIGHT, ULTRASONIC, PRESSURE};
    
    
    /**
     * The OSC server.
     */
    OSCServer oscServer;
    
    InetSocketAddress remoteSocketAddress;
    
    /**
     * The bluetooth nxt channel.
     */
    NXTCommBluetoothSerialChannel btChannel;
    
    /**
     * The lego brick we're controlling.
     */
    NXTBrick brick;
    
    /**
     * The motor A
     */
    NXTMotor motor[];
    
    /**
     * The button sensor.
     */
    NXTButtonSensor buttonSensor;
    
    /**
     * The Light sensor.
     */
    NXTLightSensor lightSensor;
    
    /**
     * The proximity sensor.
     */
    NXTProximitySensor proximitySensor;
    
    /**
     * The Sound sensor.
     */
    NXTSoundSensor soundSensor;
    
    /* Our observer (the View) */
    LegOSCObserver observer;
    
    boolean legOSCStarted = false;
    
    
    /**
     * The port on which LegOSC listens. Apps should direct OSC traffic to this port.
     */
    int legOSCPort;
    
    /**
     * The hostname of the machine where the user application is running.
     */
    String appHostname;
    
    /**
     * The port on which the user application is listening.
     */
    int appPort;
    
    /**
     * The COM port which the brick uses to connect to the machine. This is a
     * virtual bluetooth COM port.
     */
    String brickCOM;
    
    /**
     * Determines if LegOSC will poll the NXT for sensor readings periodically.
     */
    private boolean poll = false;
    
    /**
     * The interval for polling the sensor state and sending to the user application.
     */
    private int pollInterval = 100;
    
    /**
     * Flag to stop the thread.
     */
    private boolean running = false;
    
    /**
     * The mapping port->sensor type. Used to determine the type of sensor when
     * automatically polling sensor readings.
     */
    private SensorType[] sensorMap = {SensorType.NONE, SensorType.NONE,SensorType.NONE,SensorType.NONE};
    
    /** Creates a new instance of Main */
    public LegOSC() {
        btChannel = null;
        
    }
    
    public void registerObserver(LegOSCObserver ob) {
        this.observer = ob;
    }
    
    public boolean start() {
        String result = null;
        notifyMessage("Starting server...");
        
        /* open bt channel */
        try {
            notifyMessage("Opening " + this.brickCOM + " port.");
            btChannel = new NXTCommBluetoothSerialChannel(this.brickCOM);
        } catch (IOException ex) {
            ex.printStackTrace();
            notifyError("IOException occurred while initializing BT Comm port: " + ex.getMessage());
            return false;
        } catch (PortInUseException ex) {
            ex.printStackTrace();
            notifyError("PortInUseException occurred while initializing BT Comm port: " + ex.getMessage());
            return false;
        } catch (NoSuchPortException ex) {
            ex.printStackTrace();
            notifyError("NoSuchPortException occurred while initializing BT Comm port: " + ex.getMessage());
            return false;
        } catch (UnsupportedCommOperationException ex) {
            ex.printStackTrace();
            notifyError("UnsupportedCommOperationException occurred while initializing BT Comm port: " + ex.getMessage());
            return false;
        }
        
        /* start the brick */
        brick = new NXTBrick(btChannel);
        /* add motor A  */
        motor = new NXTMotor[3];
        motor[0] = new NXTMotor(brick, (byte)0);
        motor[1] = new NXTMotor(brick, (byte)1);
        motor[2] = new NXTMotor(brick, (byte)2);
        
        
        /* start the OSC server */
        try {
            oscServer = OSCServer.newUsing(OSCServer.UDP, this.legOSCPort, false);
            oscServer.addOSCListener(this);
            oscServer.start();
        }catch (IOException ioe) {
            ioe.printStackTrace();
            notifyError("IOException occurred while initializing OSC server: " + ioe.getMessage());
            return false;
        }
        
        this.remoteSocketAddress = new InetSocketAddress(this.appHostname, this.appPort);
        
        // See if we need to poll for sensor readings
        if (poll) {
            running = true;
            /* print mapping */
            for (byte i = 0; i < 4; i++) {
                switch(sensorMap[i]) {
                    case NONE:
                        notifyMessage("Port " + (i+1) +": NONE");
                        break;
                    case SOUND:
                        notifyMessage("Port " + (i+1) +": SOUND");
                        break;
                    case LIGHT:
                        notifyMessage("Port " + (i+1) +": LIGHT");
                        break;
                    case ULTRASONIC:
                        notifyMessage("Port " + (i+1) +": ULTRASONIC");
                        break;
                    case PRESSURE:
                        notifyMessage("Port " + (i+1) +": PRESSURE");
                        break;
                }
            }
            (new Thread(this)).start();
        }
        notifyMessage("OSC Server running and listening on  " + oscServer.getLocalAddress().getHostName()+ ":" + this.legOSCPort +
                "\nSend OSC messages to this port. ");
        notifyMessage("LegOSC will try to send messages to " + this.appHostname + ":" + this.appPort +
                "\nMake sure your application is listening.");
        return true;
    }
    
    private void notifyError(String error) {
        if (observer != null) {
            observer.error(error);
        }
    }
    
    private void notifyMessage(String msg) {
        if (observer != null) {
            observer.message(msg);
        }
    }
    private void notifyVerbose(String msg) {
        if (observer != null) {
            observer.verbose(msg);
        }
    }
    
    public void stop() {
        running = false;
        notifyMessage("Stopping server...");
        if (btChannel != null) {
            try {
                btChannel.closeChannel();
                
            } catch (IOException ex) {
                ex.printStackTrace();
                notifyError("IOException occurred while initializing stopping BT Comm port: " + ex.getMessage());
            }
        }
        try {
            oscServer.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
            notifyError("IOException occurred while initializing stopping BT Comm port: " + ex.getMessage());
        }
        oscServer.dispose();
        notifyMessage("Stopped");
    }
    
    public void messageReceived(OSCMessage msg, SocketAddress sender, long time) {
        
        notifyMessage("Received OSC message: " + messageToString(msg) );
        
        if( msg.getName().equals( "/motorForward" )) {
            int motorNumber;
            int power;
            
            motorNumber = ((Number) msg.getArg(0)).intValue();
            power = ((Number) msg.getArg(1)).intValue();
            
            motor[motorNumber%3].forward((byte)power);
        } else if(msg.getName().equals( "/motorSlowStop" )) {
            int motorNumber;
            motorNumber = ((Number) msg.getArg(0)).intValue();
            motor[motorNumber%3].slowStop();
        } else if(msg.getName().equals( "/motorHandBrake" ) || msg.getName().equals( "/motorBrake" ) ) {
            int motorNumber;
            motorNumber = ((Number) msg.getArg(0)).intValue();
            motor[motorNumber%3].handBrake();
        } else if (msg.getName().equals("/resetMotor")) {
            int motorNumber = ((Number) msg.getArg(0)).intValue();
            motor[motorNumber%3].resetMotorPosition(false);
        } else if (msg.getName().equals("/getMotorTachoCount")) {
            int motorNumber = ((Number) msg.getArg(0)).intValue();
            long count = motor[motorNumber%3].getTachoCount();
            try {
                
                oscServer.send(new OSCMessage("/motorTachoCount", new Object[] {motorNumber, count}), remoteSocketAddress);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            notifyMessage("Motor Tacho Count : " + count);
        } else if(msg.getName().equals( "/getButtonState" )) {
            int portNumber;
            portNumber = ((Number) msg.getArg(0)).intValue();
            if (buttonSensor == null) {
                buttonSensor = new NXTButtonSensor(brick, (byte)portNumber);
            }
            int value = buttonSensor.getValue();
            try {
                
                oscServer.send(new OSCMessage("/buttonState", new Object[] {portNumber, value}), remoteSocketAddress);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //c.send( new OSCMessage( "/done", new Object[] { m.getName() }), addr );
            notifyMessage("Button Sensor: " + value);
        } else if(msg.getName().equals( "/getLightLevel" )) {
            int portNumber;
            portNumber = ((Number) msg.getArg(0)).intValue();
            if (lightSensor == null) {
                lightSensor = new NXTLightSensor(brick, (byte)portNumber);
            }
            int value = lightSensor.getValue();
            try {
                
                oscServer.send(new OSCMessage("/lightLevel", new Object[] {portNumber, value}), remoteSocketAddress);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //c.send( new OSCMessage( "/done", new Object[] { m.getName() }), addr );
            notifyMessage("Light Sensor: " + value);
        } else if(msg.getName().equals( "/getSoundLevel" )) {
            int portNumber;
            portNumber = ((Number) msg.getArg(0)).intValue();
            if (soundSensor == null) {
                soundSensor = new NXTSoundSensor(brick, (byte)portNumber);
            }
            int value = soundSensor.getDB();
            try {
                
                oscServer.send(new OSCMessage("/soundLevel", new Object[] {portNumber, value}), remoteSocketAddress);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //c.send( new OSCMessage( "/done", new Object[] { m.getName() }), addr );
            notifyMessage("Sound Sensor: " + value);
        } else if(msg.getName().equals( "/getProximityLevel" )) {
            int portNumber;
            portNumber = ((Number) msg.getArg(0)).intValue();
            if (proximitySensor == null) {
                proximitySensor = new NXTProximitySensor(brick, (byte)portNumber);
            }
            int value = proximitySensor.getDistance();
            try {
                
                oscServer.send(new OSCMessage("/proximityLevel", new Object[] {portNumber, value}), remoteSocketAddress);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //c.send( new OSCMessage( "/done", new Object[] { m.getName() }), addr );
            notifyMessage("Proximity Sensor: " + value);
        } else if (msg.getName().equals("/getBatteryLevel")) {
            int batLevel = brick.getBatteryLevel();
            try {
                
                oscServer.send(new OSCMessage("/batteryLevel", new Object[] {batLevel}), remoteSocketAddress);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    private String messageToString(OSCMessage msg) {
        StringBuffer  sb = new StringBuffer();
        sb.append(msg.getName()).append(" ");
        for (int i = 0; i < msg.getArgCount(); i++) {
            sb.append((msg.getArg(i))).append(" ");
        }
        return sb.toString();
    }
    
    
    
    public void run() {
        /* create the sensors based on the mapping */
        Object port[] = new Object[4];
        
        for (byte i = 0; i < 4; i++) {
            switch(sensorMap[i]) {
                case NONE:
                    port[i] = null;
                    break;
                case SOUND:
                    port[i] = new NXTSoundSensor(brick, i);
                    break;
                case LIGHT:
                    port[i] = new NXTLightSensor(brick, i);
                    break;
                case ULTRASONIC:
                    port[i] = new NXTProximitySensor(brick, i);
                    break;
                case PRESSURE:
                    port[i] = new NXTButtonSensor(brick, i);
                    break;
            }
        }
        
        while(running) {
            int reading;
            OSCMessage msg;
            for (byte i = 0; i < 4; i++) {
                switch(sensorMap[i]) {
                    case NONE:
                        break;
                    case SOUND:
                        reading = ((NXTSoundSensor)port[i]).getDB();
                        
                        try {
                            msg  = new OSCMessage("/soundLevel", new Object[] {(int)i, reading});
                            oscServer.send(msg, remoteSocketAddress);
                            //notifyVerbose(messageToString(msg));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case LIGHT:
                        reading = ((NXTLightSensor)port[i]).getValue();
                        
                        try {
                            msg  = new OSCMessage("/lightLevel", new Object[] {(int)i, reading});
                            oscServer.send(msg, remoteSocketAddress);
                            //notifyVerbose(messageToString(msg));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        
                        break;
                    case ULTRASONIC:
                        reading = ((NXTProximitySensor)port[i]).getDistance();
                        
                        try {
                            msg  = new OSCMessage("/proximityLevel", new Object[] {(int)i, reading});
                            oscServer.send(msg, remoteSocketAddress);
                            //notifyVerbose(messageToString(msg));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        
                        break;
                    case PRESSURE:
                        reading = ((NXTButtonSensor)port[i]).getValue();
                        
                        try {
                            msg  = new OSCMessage("/buttonState", new Object[] {(int)i, reading});
                            oscServer.send(msg, remoteSocketAddress);
                            //notifyVerbose(messageToString(msg));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        
                        break;
                }
            }
            
            try {
                Thread.sleep(this.pollInterval);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void setPoll(boolean poll) {
        this.poll = poll;
    }
    
    
    public void mapSensor(int port, SensorType type) {
        if (port < 0 || port > 3) {
            throw new IllegalArgumentException("Port number must be between 0 and 3.");
        }
        
        sensorMap[port] = type;
    }
    
    public void configChanged(String legOSCPort, String appHostname, String appPort, String brickCOM, boolean autoSensor, int autoSensorInterval) {
        this.legOSCPort = 10000;
        try {
            this.legOSCPort = Integer.parseInt(legOSCPort);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        
        this.appHostname = appHostname;
        this.appPort = 20000;
        
        try {
            this.appPort = Integer.parseInt(appPort);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        this.brickCOM = brickCOM;
        this.pollInterval = autoSensorInterval;
        setPoll(autoSensor);
    }
    
    public void sensorMapChanged(String[] sensorType) {
        for (int i = 0; i < 4; i++) {
            
            if (sensorType[i].equalsIgnoreCase(SensorType.NONE.toString())) {
                this.mapSensor(i, LegOSC.SensorType.NONE);
            } else if (sensorType[i].equalsIgnoreCase(SensorType.SOUND.toString())) {
                this.mapSensor(i, LegOSC.SensorType.SOUND);
            } else if (sensorType[i].equalsIgnoreCase(SensorType.LIGHT.toString())) {
                this.mapSensor(i, LegOSC.SensorType.LIGHT);
            } else if (sensorType[i].equalsIgnoreCase(SensorType.ULTRASONIC.toString())) {
                this.mapSensor(i, LegOSC.SensorType.ULTRASONIC);
            } else if (sensorType[i].equalsIgnoreCase(SensorType.PRESSURE.toString())) {
                this.mapSensor(i, LegOSC.SensorType.PRESSURE);
            }
        }
    }
    
    public void startStop() {
        if (legOSCStarted) {
            this.stop();
            legOSCStarted = false;
            
            
        } else {
            if (start()) {
                legOSCStarted = true;
            }
        }
        observer.start(legOSCStarted);
    }
}
