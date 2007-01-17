/*
 * Main.java
 *
 * Created on 3 de Dezembro de 2006, 13:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.citar.diablu.nxtcomm;

import gnu.io.*;
import java.util.Enumeration;
import java.io.*;
/**
 *
 * @author jorge
 */
public class Main {
    
    private static final int BAUD = 9600;
    
    /* Our serial port used to communicate*/
    private SerialPort port = null;
    
    /* Stop processing  */
    private boolean stop = false;
    
    private Thread main;
    
    /** Creates a new instance of Main */
    public Main() {
          CommPortIdentifier portID;
//        Enumeration commPorts = CommPortIdentifier.getPortIdentifiers();

//        
//        main = Thread.currentThread();
//        
////        Runtime.getRuntime().addShutdownHook(new Thread() {
////            public void run() {
////                
////                System.out.print("\nExiting application: cleaning up... ");
////                stop = true;
////                try {
////                    
////                    main.join();
////                } catch (InterruptedException ex) {
////                    ex.printStackTrace();
////                }
////
////            }
////        });
//        
//        while (commPorts.hasMoreElements()) {
//            portID = (CommPortIdentifier) (commPorts.nextElement());
//            
//          //  if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//                System.out.println("Serial Port: " + portID.getName());
//                System.out.println("    CurrentOwner: " + portID.getCurrentOwner());
//                System.out.println("    Port Type: " + portID.getPortType());
//            //}
//        }
        
        portID = null;
        try {
            portID = CommPortIdentifier.getPortIdentifier("COM11");
        } catch (NoSuchPortException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        
        /* try to open the port with a timeout of 15 seconds */
        if (portID != null) {
            try {
                port = (SerialPort) portID.open("SerialSim", 15*1000);
            } catch (PortInUseException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (ClassCastException cce) {
                cce.printStackTrace();
                System.exit(1);
            }
        }
        /* we have a port, lets open it  */
        if (port != null) {
            try {
                port.setSerialPortParams(BAUD, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            } catch (UnsupportedCommOperationException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            InputStream is = null;
            OutputStream os = null;
            
            /* get the input and output streams     */
            try {
                is = new DataInputStream(port.getInputStream());
                
            } catch (IOException e) {
                System.err.println("Can't open input stream: write-only");
                is = null;
            }
            try {
                os = new PrintStream(port.getOutputStream(), true);
                byte[] b = {0x02, 0x00, (byte)0x00, 0x0b};
                os.write(b);
                os.close();
            } catch (IOException e) {
                System.err.println("Can't open output stream: read-only");
                is = null;
            }
           
            
            
            /* read messages */
            while (!stop) {
                int i;
                try {
                    i = is.read();
                    if (i != -1) {
                        System.out.print(i);
                       // os.write(i);
                    }
                }catch (IOException ioe) {
                    ioe.printStackTrace();
                    
                }
            }
            port.close();
            System.out.println("Done!");
            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Main();
    }
    
}
