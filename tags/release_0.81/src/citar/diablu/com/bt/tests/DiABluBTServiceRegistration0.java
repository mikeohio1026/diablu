/*
 * DiABluServiceRegistration.java
 *
 * Created on 4 juin 2006, 11:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.bt.tests;

import java.lang.*;
import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluBTServiceRegistration0 {
    
    /** Creates a new instance of DiABluServiceRegistration */
    public DiABluBTServiceRegistration0() {
    }
    

    /**
     * This is the main method of the RFCOMM Print Server application.  It will
     * accept connections and print the data received to standard out.
     *
     * @param args the arguments provided to the application on the command
     * line
     */
    public static void main(String[] args) {
        
        StreamConnectionNotifier server = null;
        ServiceRecord record;
        LocalDevice local;
        String message = "";
        byte[] data = new byte[20];
        int length;
        System.out.println("Registering DiABlu Service...");
        try {
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);
        } catch (BluetoothStateException e) {
            System.err.println("Failed to start service");
            System.err.println("BluetoothStateException: " + e.getMessage());
            return;
        }

        try {
            server = (StreamConnectionNotifier)Connector.open("btspp://localhost:F0E0D0C0B0A000908070605040302013;name=666Service");         
            
        } catch (IOException e) {
            System.err.println("Failed to start service");
            System.err.println("IOException: " + e.getMessage());
            return;
        }


          while (!(message.equals("Stop Server"))) {

            message = "";
            StreamConnection conn = null;

            try {
                try {
                    System.out.println("Service registered.Waiting connections...");
                    conn = server.acceptAndOpen();
                    System.out.println("Client connected!!");
                } catch (IOException e) {
                    System.err.println("IOException1: " + e.getMessage());
                    return;
                }

                InputStream in = conn.openInputStream();

                length = in.read(data);
                while (length != -1) {
                    message += new String(data, 0, length);
                System.out.println("Message = " + message);
                 System.out.println("Length = " + length);

                    try {
                        length = in.read(data);
                    } catch (IOException e) {
                        System.out.println("Error reading data!");
                        e.printStackTrace();
                        break;
                    }
               
                }

               // System.out.println(message);

                in.close();

            } catch (IOException e) {
                System.out.println("IOException2: " + e.getMessage());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (IOException e) {
                        System.out.println("Error closing connection!");
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            server.close();
        } catch (IOException e) {
            System.out.println("Error closing server..");
            e.printStackTrace();

        }
}

}
  
