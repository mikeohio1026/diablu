/*
 * DiABluBTspp.java
 *
 * Created on 6 juin 2006, 12:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.bt.tests;

import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;

/**
 *
 * @author nrodrigues
 */
public class DiABluBTspp implements Runnable {
    
  // Bluetooth singleton object
  LocalDevice device;
  DiscoveryAgent agent;

  // SPP_Server specific service UUID
  // note: this UUID must be a string of 32 char
  // do not use the 0x???? constructor because it won't
  // work. not sure if it is a N6600 bug or not
  public final static UUID uuid = new UUID("102030405060708090A0B0C0D0E0F010", false);
  
  // major service class as SERVICE_TELEPHONY
  private final static int SERVICE_TELEPHONY = 0x400000;
  
  
  // control flag for run loop
  // set true to exit loop
  public boolean done = false;

  // our BT server connection
  public StreamConnectionNotifier server;
  
  
  /**
    public void run_server()
  {
    try
    {
      //
      // initialize the JABWT stack
      device = LocalDevice.getLocalDevice(); // obtain reference to singleton
      device.setDiscoverable(DiscoveryAgent.GIAC); // set Discover mode to LIAC

      // start a thread to serve the server connection.
      // for simplicity of this demo, we only start one server thread
      // see run() for the task of this thread
      //Thread t = new Thread( this );
      //t.start();

    } catch ( BluetoothStateException e )
    {
      e.printStackTrace();
    }

  }
  */
  
   public void run()
  {
   

    try
    {
      //
      // initialize the JABWT stack
      device = LocalDevice.getLocalDevice(); // obtain reference to singleton
      device.setDiscoverable(DiscoveryAgent.GIAC); // set Discover mode to LIAC

      // start a thread to serve the server connection.
      // for simplicity of this demo, we only start one server thread
      // see run() for the task of this thread
      //Thread t = new Thread( this );
      //t.start();

    } catch ( BluetoothStateException e )
    {
      e.printStackTrace();
    }       


// human friendly name of this service
    String appName = "DiABluServerTester";

    
    // connection to remote device
    StreamConnection c = null;
    
    log("DiABlu SPP Service Server Starting...");
    try
    {
      String url = "btspp://localhost:" + uuid.toString() +";name="+ appName;
      log("server url: " + url );

      // Create a server connection object, using a
      // Serial Port Profile URL syntax and our specific UUID
      // and set the service name to BlueChatApp
      server = (StreamConnectionNotifier) Connector.open( url );

      // Retrieve the service record template
      ServiceRecord rec = device.getRecord(server);

      // set ServiceRecrod ServiceAvailability (0x0008) attribute to indicate our service is available
      // 0xFF indicate fully available status
      // This operation is optional
      rec.setAttributeValue( 0x0008, new DataElement( DataElement.U_INT_1, 0xFF ) );

      // Print the service record, which already contains
      // some default values
      //Util.printServiceRecord( rec );

      // Set the Major Service Classes flag in Bluetooth stack.
      // We choose Telephony Service
      rec.setDeviceServiceClasses( SERVICE_TELEPHONY  );



    } catch (Exception e)
    {
      e.printStackTrace();
      log(e.getClass().getName()+" "+e.getMessage());
    }

    while( !done)
    {
      try {
        ///////////////////////////////
        log("local service waiting for client connection...");

        //
        // start accepting client connection.
        // This method will block until a client
        // connected
        c = server.acceptAndOpen();

        log("accepted a client connection. reading it...");
        //
        // retrieve the remote device object
        RemoteDevice rdev = RemoteDevice.getRemoteDevice( c );

        // obtain an input stream to the remote service
        DataInputStream in = c.openDataInputStream();

        // read in a string from the string
        String s = in.readUTF();
        log("read in data '"+s+"' from client");

        DataOutputStream out = c.openDataOutputStream();
        out.writeUTF( s );
        out.flush();

        log("echo '"+s+"' back to client");

        // close current connection, wait for the next one
        c.close();


      } catch (Exception e)
      {
        e.printStackTrace();
        //SPP_MIDlet.alert(e, SPP_MIDlet.instance.spp_screen );
      }

    } // while

  }

    public void log(String texto){
        System.out.println(texto);
    }
    
    /** Creates a new instance of DiABluBTspp */
    public DiABluBTspp() {
    }
    
}
