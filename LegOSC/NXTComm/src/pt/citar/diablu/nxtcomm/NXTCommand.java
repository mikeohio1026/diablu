/*
 * NXTCommand.java
 *
 * Created on 19 de Janeiro de 2007, 21:20
 *
 */
package pt.citar.diablu.nxtcomm;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


/**
 * Represents a Command that can be sent to the NXTBrick. This class cannot be instantiated. 
 * Only subclasses which represent concrete Commands should be used.
 *
 * @author Jorge Cardoso
 */
public abstract class NXTCommand {
    
    /** 
     * Direct Command Response Required.
     */
    protected static final byte DIRECT_COMMAND_RESPONSE_REQUIRED = 0x00;
    
    /** 
     * Direct Command Response Required.
     */
    protected static final byte DIRECT_COMMAND_NO_RESPONSE = (byte)0x80;    
    
    /**
     * Command type position in message frame.
     */
    protected static final int COMMAND_TYPE_INDEX = 0;
    
    /**
     * Indicates if a response to this command is required.
     **/
    protected boolean responseRequired;
    
    /**
     * The command packet.
     */
    protected byte []buffer;
    
    /** Creates a new instance of NXTCommand */
    public NXTCommand() {
        
        /* By default we should not require a response since it's faster this way*/
        this(false);
    }
    
    public NXTCommand(boolean responseRequired) {
            
        this.responseRequired = responseRequired;
    }
    /**
     * This must be implemented by each concrete Command. Only a concrete command knows
     * how to read a response.
     */
    public abstract NXTResponse sendCommand(InputStream is, OutputStream os) throws IOException;

    
    public boolean isResponseRequired() {
        return responseRequired;
    }

    public void setResponseRequired(boolean responseRequired) {
        this.responseRequired = responseRequired;
    }
    
    public String toString() {
        return null;
        
    }
    

}
