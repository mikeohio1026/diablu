/*
 * NXTCommandPlayTone.java
 *
 * Created on 19 de Janeiro de 2007, 21:53
 *
 * Represents the playtone command.
 */

package pt.citar.diablu.nxtcomm;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Jorge Cardoso
 */
public class NXTCommandPlayTone extends NXTCommand {
    /**
     * Frequency LSB index on buffer;
     */
    private final static int FREQUENCY_LSB_INDEX = 2;
    
    /**
     * Frequency MSB index on buffer;
     */
    private final static int FREQUENCY_MSB__INDEX = 3;
    
    /**
     * Duration LSB index on buffer;
     */
    private final static int DURATION_LSB_INDEX = 4;

     /**
     * Duration MSB index on buffer;
     */
    private final static int DURATION_MSB_INDEX = 5;
    
    protected byte  []buffer = new byte[] {(byte)0x80, 0x03, 0x00, 0x00, 0x00, 0x00};
    
    private int frequency;
    private int duration;
    
    public NXTCommandPlayTone() {
        super();
    }
    
    public NXTCommandPlayTone(int frequency, int duration) {
        /* No response required*/
        super(false);
        
        this.setFrequency(frequency);
        this.setDuration(duration);
    }
    
    public NXTResponseStatus sendCommand(InputStream is, OutputStream os) throws IOException{
        /* send Length, LSB */
        os.write((byte)buffer.length);
        /* send Length, MSB */
        os.write(0);   
       
        if (responseRequired) {
            buffer[COMMAND_TYPE_INDEX] = DIRECT_COMMAND_RESPONSE_REQUIRED;
            
            /* send command */
            os.write(buffer);
            
            /* receive response */
            NXTResponseStatus response = new NXTResponseStatus();
            response.receiveResponse(is);
            return response;
            
        } else {
             buffer[COMMAND_TYPE_INDEX] = DIRECT_COMMAND_NO_RESPONSE;
            /* send command */
            os.write(buffer);
            
            return null;
        }
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
        
        buffer[FREQUENCY_LSB_INDEX] = (byte) frequency;
        
        buffer[FREQUENCY_MSB__INDEX] = (byte) (frequency >> 8);
    }

    public int getDuration() {
        return duration;
        
   
    }

    public void setDuration(int duration) {
        this.duration = duration;
        
        buffer[DURATION_LSB_INDEX] = (byte) duration;
        
        buffer[DURATION_MSB_INDEX] = (byte) (duration >> 8);            
    }
    
    
    
}
