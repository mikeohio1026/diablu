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
public class LegOSC implements OSCListener {
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
    
    LegOSCObserver observer;
    
    /** Creates a new instance of Main */
    public LegOSC() {
        btChannel = null;
    }
    
    public void registerObserver(LegOSCObserver ob) {
        this.observer = ob;
    }
    
    public boolean start(String legoCommPort, int localPort, String remoteHostname, int remotPort) {
        String result = null;
        System.out.println("Starting server at port " + localPort);
        
        /* open bt channel */
        try {
            btChannel = new NXTCommBluetoothSerialChannel(legoCommPort);
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
            oscServer = OSCServer.newUsing(OSCServer.UDP, localPort, false);
            oscServer.addOSCListener(this);
            oscServer.start();
        }catch (IOException ioe) {
            ioe.printStackTrace();
            notifyError("IOException occurred while initializing OSC server: " + ioe.getMessage());
            return false;
        }
        
        this.remoteSocketAddress = new InetSocketAddress(remoteHostname, remotPort);
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
    
    public void stop() {
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
    
}
