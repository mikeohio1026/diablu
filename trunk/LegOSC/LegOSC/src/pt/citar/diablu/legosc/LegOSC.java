/*
 * Main.java
 *
 * Created on 23 de Janeiro de 2007, 0:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
 * @author Jorge Cardoso
 */
public class LegOSC implements OSCListener, Runnable, LegOSCViewObserver {
    
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
    
    
    LegOSCObserver observer;
    
    boolean legOSCStarted = false;
    
    
    int legOSCPort;
    String appHostname;
    int appPort;
    String brickCOM;
    
    /**
     * Determines if LegOSC will poll the NXT for sensor readings periodically.
     */
    private boolean poll = false;
    
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
        System.out.println("Starting server at port " + this.legOSCPort);
        
        /* open bt channel */
        try {
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
            (new Thread(this)).start();
        }
        
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
        } else if(msg.getName().equals( "/motorHandBrake" )) {
            int motorNumber;
            motorNumber = ((Number) msg.getArg(0)).intValue();
            motor[motorNumber%3].handBrake();
            
        } else if(msg.getName().equals( "/getButtonState" )) {
            int portNumber;
            portNumber = ((Number) msg.getArg(0)).intValue();
            if (buttonSensor == null) {
                buttonSensor = new NXTButtonSensor(brick, (byte)portNumber);
            }
            int value = buttonSensor.getValue();
            try {
                
                oscServer.send(new OSCMessage("/buttonState", new Object[] {value}), remoteSocketAddress);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //c.send( new OSCMessage( "/done", new Object[] { m.getName() }), addr );
            notifyMessage("Sensor: " + value);
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
                Thread.sleep(250);
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
    
    public void configChanged(String legOSCPort, String appHostname, String appPort, String brickCOM, boolean autoSensor) {
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
        setPoll(autoSensor);
    }
    
    public void sensorMapChanged(String[] sensorType) {
        for (int i = 0; i < 4; i++) {

            if (sensorType[i].equalsIgnoreCase("none")) {
                this.mapSensor(i, LegOSC.SensorType.NONE);
            } else if (sensorType[i].equalsIgnoreCase("sound sensor")) {
                this.mapSensor(i, LegOSC.SensorType.SOUND);
            } else if (sensorType[i].equalsIgnoreCase("light sensor")) {
                this.mapSensor(i, LegOSC.SensorType.LIGHT);
            } else if (sensorType[i].equalsIgnoreCase("ultrasonic")) {
                this.mapSensor(i, LegOSC.SensorType.ULTRASONIC);
            } else if (sensorType[i].equalsIgnoreCase("button")) {
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
