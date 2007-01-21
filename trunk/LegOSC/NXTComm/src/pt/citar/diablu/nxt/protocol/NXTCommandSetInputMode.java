/*
 * NXTCommandSetInputMode.java
 *
 * Created on 20 de Janeiro de 2007, 16:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pt.citar.diablu.nxt.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * The <code>NXTCommandSetInputMode</code> allows you to set the input mode for the sensors.
 *
 * @author Jorge Cardoso
 */
public class NXTCommandSetInputMode extends NXTCommand {
    
    
    /**
     * Constructs a new <code>NXTCommandSetInputMode</code> object with input port = 0, no sensor type, raw sensor mode and 
     * with no response requirement.
     */
    public NXTCommandSetInputMode() {
        this((byte)0, NXTResponseInputValues.NO_SENSOR_TYPE, NXTResponseInputValues.RAW_MODE);
    }
    
    /**
     * Constructs a new <code>NXTCommandSetInputMode</code> object with the input port, sensor type, sensor mode and 
     * with no response requirement.
     */
    public  NXTCommandSetInputMode(byte inputPort, byte sensorType, byte sensorMode) {
        this(inputPort, sensorType, sensorMode, false);
    }
    
    /**
     * Constructs a new <code>NXTCommandSetInputMode</code> object with the input port, sensor type, sensor mode and 
     * response requirement specified.
     */
    public NXTCommandSetInputMode(byte inputPort, byte sensorType, byte sensorMode, boolean responseRequired) {
        this.responseRequired = responseRequired;
        buffer = new byte[] {responseRequired ? DIRECT_COMMAND_RESPONSE_REQUIRED : DIRECT_COMMAND_NO_RESPONSE, 0x05, inputPort, sensorType, sensorMode};
    }
    
    public NXTResponseStatus sendCommand(InputStream is, OutputStream os) throws IOException {
      
        if (responseRequired) {
            buffer[COMMAND_TYPE_INDEX] = DIRECT_COMMAND_RESPONSE_REQUIRED;
            
            /* send command */
            os.write(buffer);
            
            os.flush();
            
            /* receive response */
            NXTResponseStatus response = new NXTResponseStatus();
            response.receiveResponse(is);
            return response;
            
        } else {
            buffer[COMMAND_TYPE_INDEX] = DIRECT_COMMAND_NO_RESPONSE;
            /* send command */
            os.write(buffer);
            os.flush();
            
            /* don't need to wait for a response*/
            return null;
        }        
        
    }
    
    /**
     * TODO: getter/setter for input port, sensor type and sensor mode
     */
}
