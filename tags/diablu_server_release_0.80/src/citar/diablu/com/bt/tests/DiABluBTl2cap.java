/*
 * DiABluBTl2cap.java
 *
 * Created on 6 juin 2006, 14:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.com.bt.tests;


import javax.bluetooth.*;
import javax.microedition.io.*;
import java.lang.*;

/**
 *
 * @author nrodrigues
 */
public class DiABluBTl2cap implements Runnable {
        
  // Bluetooth singleton object
  LocalDevice device;
  DiscoveryAgent agent;

  // SPP_Server specific service UUID
  // note: this UUID must be a string of 32 char
  // do not use the 0x???? constructor because it won't
  // work. not sure if it is a N6600 bug or not
  public final static UUID uuid = new UUID("102030405060708090A0B0C0D0E0F011", false);

  //
  // major service class as SERVICE_TELEPHONY
  private final static int SERVICE_TELEPHONY = 0x400000;


  // control flag for run loop
  // set true to exit loop
  public boolean done = false;

  // our BT server connection
  // public L2CAPConnectionNotifier server;



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
      Thread t = new Thread( this );
      t.start();

    } catch ( BluetoothStateException e )
    {
      e.printStackTrace();
    }

  }

  public void run()
  {
      /**
    // human friendly name of this service
    String appName = "DiABlu Server L2CAP Test";

    log("DiABlu L2CAP Service Server Starting...");
    // connection to remote device
    L2CAPConnection c = null;
    try
    {
      String url = "btl2cap://localhost:" + uuid.toString() +";name="+ appName+";ReceiveMTU=512;TransmitMTU=512";
      log("server url: " + url );

      // Create a server connection object, using a
      // Serial Port Profile URL syntax and our specific UUID
      // and set the service name to BlueChatApp
      server =  (L2CAPConnectionNotifier)Connector.open( url );

      // Retrieve the service record template
      ServiceRecord rec = device.getRecord( server );

      // set ServiceRecrod ServiceAvailability (0x0008) attribute to indicate our service is available
      // 0xFF indicate fully available status
      // This operation is optional
      rec.setAttributeValue( 0x0008, new DataElement( DataElement.U_INT_1, 0xFF ) );

      // Print the service record, which already contains
      // some default values
      //Util.printServiceRecord( rec );

      // Set the Major Service Classes flag in Bluetooth stack.
      // We choose Object Transfer Service
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

        log("accepted a client connection, reading data..");

        //
        // retrieve the remote device object
        RemoteDevice rdev = RemoteDevice.getRemoteDevice( c );

        int size = c.getReceiveMTU();
        log("ReceiveMTU size "+size);
        // we read only as much as ReceiveMTU limited us
        byte[] data = new byte[size];

        // obtain an input stream to the remote service
        int read = c.receive( data );

        log("read in data "+read+" bytes");

        c.send( data );
        log("echo "+read+" bytes back to client");

        // close current connection, wait for the next one
        c.close();


      } catch (Exception e)
      {
        e.printStackTrace();
        //L2CAP_MIDlet.alert(e, L2CAP_MIDlet.instance.l2cap_screen );
        return;

      }

    } // while

  }

  public void log( String s )
  {
    System.out.println(s);
  }
    
    
    */
    
  } 
    
    /** Creates a new instance of DiABluBTl2cap */
    public DiABluBTl2cap() {
    }
    
}
