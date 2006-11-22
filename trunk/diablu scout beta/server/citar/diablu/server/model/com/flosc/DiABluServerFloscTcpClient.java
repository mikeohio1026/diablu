/*
 * DiABluServerFloscTcpClient.java
 *
 * This class is based on the TcpClient.java from Flosc (www.benchun.net/flosc)
 * written by Ben Chun. Since we don't need comunications in both directions i've stripped the class
 * so it better meets the DiABlu System needs. Thanks to Ben Chun and all other who have worked with Flosc
 *
 * Created on 11 de Setembro de 2006, 11:55
 *
 * Copyright (C) 11 de Setembro de 2006 Nuno Rodrigues
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */

package citar.diablu.server.model.com.flosc;

import java.io.*;
import java.net.*;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerFloscTcpClient extends Thread {
    
    private Thread thrThis;         // client thread
    private Socket socket;          // socket for connection
    private DiABluServerFloscTcpServer server;      // server to which the client is connected
    private String ip;              // the ip of this client
   // protected BufferedReader in;    // captures incoming messages
    protected PrintWriter out;      // sends outgoing messages
    
    
    /** Creates a new instance of DiABluServerFloscTcpClient 
     *
     * @param   server    The server to which this client is connected.
     * @param   socket    The socket through which this client has connected.   
     *
     */
    public DiABluServerFloscTcpClient(DiABluServerFloscTcpServer server,Socket socket) {
        this.server = server;
        this.socket = socket;
        this.ip = socket.getInetAddress().getHostAddress();
        char EOF = (char)0x00;

        // --- init the reader and writer
        try {
	   // in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.print("Hello from DiABlu"+EOF);
            
        } catch(IOException ioe) {
            System.out.println("Client IP: " + ip + " could not be " 
            + "initialized and has been disconnected.");
            killClient();
        }
    }
    
      /**
     * Constructor for the TcpClient.  Initializes the TcpClient properties.

 
    /**
     * Thread run method.  Monitors incoming messages.
    */	
    public void run() {
      while(true){}
    }

    /**
     * Gets the ip of this client.
     * @return   ip    this client's ip
    */
    public String getIP() {
        return ip;
    }
    
    /**
     * Sends a message to this client. Called by the server's broadcast method.
     * @param   message    The message to send.
    */
    public void send(String message) {
        // --- put the message into the buffer
        System.out.println("Client send message:"+message);
        out.print(message);
        //out.print(message);
        out.flush();
        // --- flush the buffer and check for errors
        // --- if error then kill this client
        if(out.checkError()) {
            System.out.println("Client IP: " + ip + " caused a write error "
            + "and has been disconnected.");
            killClient();
        }
    }
 
    /**
     * Kills this client. 
    */   
    private void killClient() {
        // --- tell the server to remove the client from the client list    
        server.removeClient(this);

        // --- close open connections and references
        try {
        //    in.close();
            out.close();
            socket.close();            
            thrThis = null;
        } catch (IOException ioe) {
            System.out.println("Client IP: " + ip + " caused an error "
            + "while disconnecting.");
        }       
    }
    
}
