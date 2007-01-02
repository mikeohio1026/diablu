/*
 * DiABluServerFloscOscPacket.java
 *
 * This class is based on the OscPacket.java from Flosc (www.benchun.net/flosc)
 * written by Ben Chun. Since we don't need comunications in both directions i've stripped the class
 * so it better meets the DiABlu System needs. Thanks to Ben Chun and all others who have worked with Flosc
 *
 * Created on 11 de Setembro de 2006, 14:49
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

import java.util.*;
import java.io.*;
import java.net.InetAddress;


/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerFloscOscPacket {
            
    private long time;
    private Vector <DiABluServerFloscOscMessage> messages;
    public InetAddress address;
    public int port;
    
    /** Creates a new instance of DiABluServerFloscOscPacket */
    public DiABluServerFloscOscPacket() {
                	
        time = 0;
	messages = new Vector <DiABluServerFloscOscMessage> ();
        
    }

    /**
     * Constructor for outgoing packets.
     *
     * @param  time    OSC time tag
     * @param  address destination host
     * @param  port    destination port
    */
    public DiABluServerFloscOscPacket(long time, InetAddress address, int port) {
	this.time = time;
	messages = new Vector <DiABluServerFloscOscMessage>();
	this.address = address;
	this.port = port;
    }


    /**
     * Sets the time.
     *
     * @param   time    the new time
     */
    public void setTime(long time) {
	this.time = time;
    }


    /**
     * Sets the destination address.
     *
     * @param   address  the new address
     */
    public void setAddress(InetAddress address) {
	this.address = address;
    }

    /**
     * Sets the destination port.
     *
     * @param   port    the new port
     */
    public void setPort(int port) {
	this.port = port;
    }


    /**
     * Adds a message to this packet.
     *
     * @param   message   the message to add
    */
    public void addMessage(DiABluServerFloscOscMessage message) {
	messages.addElement(message);
    }

    public InetAddress getAddress() {
	return address;
    }

    public int getPort() {
	return port;
    }


  /**
   * Returns an XML representation of this packet, suitable for
   * sending to Flash.  The return value should validate against
   * flosc.dtd
   *
   */
    public String getXml() {

	String xml = "";
	xml += "<OSCPACKET ADDRESS=\"" + address.getHostAddress() +
	    "\" PORT=\"" + port +
	    "\" TIME=\""+ time + "\">";

	Enumeration <DiABluServerFloscOscMessage> m = messages.elements();
	while (m.hasMoreElements()) {
	    DiABluServerFloscOscMessage mess = m.nextElement();
	    xml += mess.getXml();
	}

	xml += "</OSCPACKET>";
	return xml;
    }

    /**
     * Returns a byte array representation of this packet, suitable for
     * sending to OSC client applications.
     *
     */
    public byte[] getByteArray() throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream stream = new DataOutputStream(baos);

	// bundle
      	if (messages.size() > 1) {
	    baos.write( ("#bundle").getBytes() );
	    baos.write(0);
	    // bundles have a time tag
	    stream.writeLong(time);
	}

	// messages
	Enumeration <DiABluServerFloscOscMessage> m = messages.elements();
	while (m.hasMoreElements()) {
	    DiABluServerFloscOscMessage mess = m.nextElement();
	    byte[] byteArray = mess.getByteArray();
	    // bundles have message size tags
	    if (messages.size() > 1) {
		stream.writeInt(byteArray.length);
	    }
	    baos.write(byteArray);
	}
	return baos.toByteArray();
    }

    /**
     * Make the stream end on a 4-byte boundary by padding it with
     * null characters.
     *
     * @param stream The stream to align.
     */
    private void alignStream(ByteArrayOutputStream stream) throws IOException {
        int pad = 4 - ( stream.size() % 4 );
        for (int i = 0; i < pad; i++)
            stream.write(0);
    }

    /**
     * Prints out a byte array in 4-byte lines, useful for debugging.
     *
     * @param byteArray The byte array
     */
    public static void printBytes(byte[] byteArray) {
	for (int i=0; i<byteArray.length; i++) {
	    System.out.print(byteArray[i] + " (" + (char)byteArray[i] + ")  ");
	    if ((i+1)%4 == 0)
		System.out.print("\n");
	}
    }
}
