/*
 * DiABluServerFloscOscMessage.java
 *
 * This class is based on the OscMessage.java from Flosc (www.benchun.net/flosc)
 * written by Ben Chun. Since we don't need comunications in both directions i've stripped the class
 * so it better meets the DiABlu System needs. Thanks to Ben Chun and all others who have worked with Flosc
 *
 * Created on 11 de Setembro de 2006, 12:14
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

// j2se
import java.util.*;
import java.io.*;

/**
 * 
 *
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerFloscOscMessage {
    
    private String name;
    private Vector <Character> types;
    private Vector <Object> arguments;
        
    /** Creates a new instance of DiABluServerFloscOscMessage 
     *
     *  @param name 
     */
    public DiABluServerFloscOscMessage(String name) {

        this.name = name;
	types = new Vector <Character> ();
	arguments = new Vector <Object> ();
        
    
    }
    /**
     * Adds a type/argument pair to the list of arguments
     *
     * @param   type           the argument data type
     * @param   argument       the argument value
    */
    public void addArg(Character type, Object argument) {
	types.addElement(type);
	arguments.addElement(argument);
    }

    /**
     * Directly sets the type and arg Vectors
     *
     * @param   types    a list of types
     * @param   args     a list of arguments matching the types
    */
    public void setTypesAndArgs(Vector <Character> types, Vector <Object> args) {
	this.types = types;
	this.arguments = args;
    }

    /**
     * Returns an XML representation of the message
     *
     */
    public String getXml() {
	if (types == null)
	    return "ERROR: Types not set";

	String xml = "";
	xml += "<MESSAGE NAME=\"" + name + "\">";

	Enumeration <Character> t = types.elements();
	Enumeration <Object> a = arguments.elements();

	while (t.hasMoreElements()) {
	    char type = ( t.nextElement() ).charValue();

	    if (type == '[')
		xml += "<ARRAY>";
	    else if (type == ']')
		xml += "</ARRAY>";
	    else {
		xml += "<ARGUMENT TYPE=\"" + type + "\" ";
		switch(type) {
		case 'i':
		    xml += "VALUE=\"" + (Integer)a.nextElement() + "\" />";
		    break;
		case 'f':
		    xml += "VALUE=\"" + (Float)a.nextElement() + "\" />";
		    break;
		case 'h':
		    xml += "VALUE=\"" + (Long)a.nextElement() + "\" />";
		    break;
		case 'd':
		    xml += "VALUE=\"" + (Double)a.nextElement() + "\" />";
		    break;
		case 's':
		    xml += "VALUE=\"" + (String)a.nextElement() + "\" />";
		    break;
		case 'T':
		case 'F':
		case 'N':
		case 'I':
		    xml += " />";
		    break;
		}
	    }

	}
	xml += "</MESSAGE>";
	return xml;
    }

    /**
     * Returns a byte array representation of this message.
     *
     */
    public byte[] getByteArray() throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream stream = new DataOutputStream(baos);
	
	// address (name)
	stream.writeBytes( name );
	alignStream( baos );

	// type tags
	stream.writeByte( ',' );  // comma indicates type tags
	Enumeration <Character> t = types.elements();
	while ( t.hasMoreElements() ) {
	    char type = ( t.nextElement() ).charValue();
	    stream.writeByte( type );
	}
	alignStream( baos );

	// values
	t = types.elements();
	Enumeration a = arguments.elements();
	while ( t.hasMoreElements() ) {
	    char type = ( (Character)t.nextElement() ).charValue();
	    switch(type) {
	    case 'i':
		stream.writeInt( ((Integer)a.nextElement()).intValue() );
		break;
	    case 'f':
		stream.writeFloat( ((Float)a.nextElement()).floatValue() );
		break;
	    case 'h':
		stream.writeLong( ((Long)a.nextElement()).longValue() );
		break;
	    case 'd':
		stream.writeDouble( ((Double)a.nextElement()).doubleValue() );
		break;
	    case 's':
		stream.writeBytes( ((String)a.nextElement()) );
		alignStream( baos );
		break;
	    }    
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
}

