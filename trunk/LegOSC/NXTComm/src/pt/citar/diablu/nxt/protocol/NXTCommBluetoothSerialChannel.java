/*
 * NXTCommBluetoothSerialChannel.java
 *
 * Created on 21 de Janeiro de 2007, 12:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pt.citar.diablu.nxt.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import gnu.io.*;

/**
 *
 * @author Jorge Cardoso
 */
public class NXTCommBluetoothSerialChannel implements NXTCommChannel {
    private static final int BAUD = 9600;
    private InputStream is;
    private OutputStream os;
    
    private SerialPort port;
    
    /** Creates a new instance of NXTCommBluetoothChannel */
    public NXTCommBluetoothSerialChannel() {
    }
    
    public NXTCommBluetoothSerialChannel(String commPort) throws Exception{
        openPort(commPort);
    }
   
    public NXTResponse sendCommand(NXTCommand command) throws IOException {
        
        /* send Length of packet, this is only necessary when sending via bluetooth */
        int length = command.getPacketLength();
        os.write((byte) length); // LSB
        os.write((byte) (0xff & (length >> 8)));   // MSB
        
        return command.sendCommand(is, os);
    }
    
    public void openPort(String commPort) throws IOException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        CommPortIdentifier portID = null;

       
        portID = CommPortIdentifier.getPortIdentifier(commPort);
        
        
        /* try to open the port with a timeout of 15 seconds */
        if (portID != null) {

           port = (SerialPort) portID.open("SerialSim", 15*1000);
           port.setSerialPortParams(BAUD, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

           is = port.getInputStream();
           os = port.getOutputStream();
        }
    }
    
    public void closePort() throws IOException {
        if (is != null) {
            is.close();
        }
        if (os != null) {
            os.close();
        }
        if (port != null) {
            port.close();
        }
    }
}
