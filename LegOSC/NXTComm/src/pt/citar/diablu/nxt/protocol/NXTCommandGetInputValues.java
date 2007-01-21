/*
 * NXTCommandGetInputValues.java
 *
 * Created on 19 de Janeiro de 2007, 21:53
 *
 */

package pt.citar.diablu.nxt.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


/**
 * Represents the GETINPUTVALUES Command.
 * The response to this command (if required) is a <code>NXTResponseInputValues</code>.
 * 
 * @author Jorge Cardoso
 * @see NXTResponseResponseInputValues
 */
public class NXTCommandGetInputValues extends NXTCommand {
    /**
     * Output port index on buffer;
     */
    private final static int INPUT_PORT_INDEX = 2;
    
    
    /**
     * The input port to read values from.
     */
    private byte inputPort;
    
    /** 
     * Constructs a new <code>NXTCommandGetInputValues</code> object which will read values from input port 0.
     *
     */  
    public NXTCommandGetInputValues() {
        // just to override the default constructor from the base class.
        this((byte)0);
    }
    
    
    /**
     * 
     * Constructs a new <code>NXTCommandGetInputValues</code> object with a given input port.
     * 
     * 
     * @param inputPort The input port to read values from. Range: (0 - 3).
     */
    public NXTCommandGetInputValues(byte inputPort) {
        super(true);
        this.setInputPort(inputPort);
        buffer = new byte[] {DIRECT_COMMAND_RESPONSE_REQUIRED, 0x07, inputPort};
    }
    
    /**
     * Constructs a new command object with a response requirement. This was made private since it 
     * doesn't make sense in this command to set the response requirements to false.
     *
     * @param responseRequired The response requirement. <code>false</code>, no response required. <code>true</code>, response
     * required.
     */
    private NXTCommandGetInputValues(boolean responseRequired) {
            
    }
    
    
    /**
     * Sends this command to the NXT Brick.
     *
     * @param is The <code>InputStream</code> used to receive the response, if required. 
     * Can be null if no response is required.
     * @param os The <code>OutputStream</code> used to send the command.
     *
     * @return  A <code>NXTResponseInputValues</code> object .
     *
     * @see NXTCommand#sendCommand(InputStream is, OutputStream os)
     * @see NXTResponseInputValues
     */
    public NXTResponseInputValues sendCommand(InputStream is, OutputStream os) throws IOException{
        /* send Length, LSB */
        os.write((byte)buffer.length);
        /* send Length, MSB */
        os.write(0);   
       
        /* send command */
        os.write(buffer);
        
        os.flush();
        
        /* receive response */
        NXTResponseInputValues response = new NXTResponseInputValues();
        response.receiveResponse(is);
        return response;
            
 
    }

    public byte getInputPort() {
        return inputPort;
    }

    public void setInputPort(byte inputPort) {
        this.inputPort = inputPort;
    }

   

    
    
    
}
