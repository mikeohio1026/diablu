/*
 * DiABluBTrfcommJSR82b.java
 *
 * Created on 7 juin 2006, 11:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.bt.tests;


/**
 *
 * @author nrodrigues
 */
import java.lang.*;
import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

/**
 * This class will start an RFCOMM service that will accept data and print it
 * to standard out.
 */
public class DiABluBTrfcommJSR82b {

    /**
     * This is the main method of the RFCOMM Print Server application.  It will
     * accept connections and print the data received to standard out.
     *
     * @param args the arguments provided to the application on the command
     * line
     */
    private static String serviceName = "DiABluService";
    private StreamConnectionNotifier serverNotifier;
    private LocalDevice localDevice;
    private ServiceRecord record;
    private String message = "";
    private byte[] data = new byte[20];
    private int length; 
    
    public void DiABluBTrfcommJSR82b() {
       
    }
      public void start() {
        
    
        
        System.out.println("Starting DiABlu Service...");
        try {
            
            
            localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);
            
        } catch (BluetoothStateException e) {
            
            System.err.println("Failed to start service");
            System.err.println("BluetoothStateException: " + e.getMessage());
            return;
            
        }
        
        System.out.println("Registering DiABlu Service...");
        try {
            
            String connectionString ="btspp://localhost:11111111111111111111111111111111;name="+serviceName;
            // create the notifier
            serverNotifier = (StreamConnectionNotifier)Connector.open(connectionString);
            // remember the service record for late updates
            record = localDevice.getRecord(serverNotifier);
            
            /** create a special attribute
            DataElement base = new DataElement(DataElement.UUID);
            record.setAttributeValue(0x0003,base);
            */
            
        } catch (IOException e) {
            
            e.printStackTrace();
            return;
        }

        while (!(message.equals("Stop Server"))) {

            message = "";
            StreamConnection conn = null;

            try {
                try {
                    
                    System.out.println("DiABlu Service Registered...waiting clients to respond");
                    conn = serverNotifier.acceptAndOpen();
                    
                } catch (IOException e) {
                    System.err.println("IOException: " + e.getMessage());
                    return;
                }

                InputStream in = conn.openInputStream();

                length = in.read(data);
                while (length != -1) {
                    message += new String(data, 0, length);
                    System.out.println("Message = " + message);

                    try {
                        length = in.read(data);
                    } catch (IOException e) {
                        break;
                    }
                System.out.println("Length = " + length);
                }

                System.out.println(message);

                in.close();

            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        try {
            serverNotifier.close();
        } catch (IOException e) {

        }
    }

}
