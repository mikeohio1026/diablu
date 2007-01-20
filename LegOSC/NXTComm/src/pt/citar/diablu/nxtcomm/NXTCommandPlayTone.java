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
 * Represents the PLAYTONE Command.
 * The response to this command (if required) is a <code>NXTResponseStatus</code>.
 * 
 * <br>Example 1:
 * <pre>
 *  // plays a tone of 4KHz during half a second. No response.
 *  NXTCommandPlayTone playTone = new NXTCommandPlayTone(4000, 500);
 *  playTone.sendCommand(is, os);
 * </pre>
 * Example 2:
 * <pre>
 * // plays a tone of 2KHz during two seconds. Requires a response.
 * playTone.setResponseRequired(true);
 * playTone.setFrequency(2000);
 * NXTResponseStatus status = playTone.sendCommand(is, os);
 * System.out.println(status);
 * </pre>
 * @author Jorge Cardoso
 * @see NXTResponseStatus
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
    
    
    
    /**
     * The frequency of the tone to play.
     */
    private int frequency;
    
    /**
     * The duration of the tone.
     */
    private int duration;
    
    
    public NXTCommandPlayTone() {
        this(0, 0);
        
    }
    /** 
     * Constructs a new <code>NXTCommandPlayTone</code> object with a given frequency and duration and
     * with no response requirement. 
     *
     * @param frequency The frequency of the tone. Only the two least significant bytes are considered.
     * @param duration The duration of the tone. Only the two least significatn bytes are considered.
     */
    public NXTCommandPlayTone(int frequency, int duration) {
        
        this(frequency, duration, false);
    }
    
    /** 
     * Constructs a new <code>NXTCommandPlayTone</code> object with a given frequency, duration and
     * response requirement. 
     *
     * @param frequency The frequency of the tone. Only the two least significant bytes are considered.
     * @param duration The duration of the tone. Only the two least significatn bytes are considered.
     * @param responseRequired Indicates if a response to this command is required. 
     */
    public NXTCommandPlayTone(int frequency, int duration, boolean responseRequired) {
        /* Set the response required parameter using the base class */
        super(responseRequired);
        
        buffer = new byte[] {(byte)0x80, 0x03, 0x00, 0x00, 0x00, 0x00};
        
        this.setFrequency(frequency);
        this.setDuration(duration);        
    }
    /**
     * Sends this command to the NXT Brick.
     *
     * @param is The <code>InputStream</code> used to receive the response, if required. 
     * Can be null if no response is required.
     * @param os The <code>OutputStream</code> used to send the command.
     *
     * @return Null if no response is required, <code>NXTResponseStatus</code> otherwise.
     *
     * @see NXTCommand#sendCommand(InputStream is, OutputStream os)
     * @see NXTResponseStatus
     */
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
            
            /* don't need to wait for a response*/
            return null;
        }
    }

    /**
     * Returns the current frequency set to be played when the command is
     * sent.
     *
     * @return The frequency of the tone.
     */
    public int getFrequency() {
        return frequency;
    }
    
    /**
     * Sets the frequency of the tone to play.
     * @param frequency The frequency to play. On the two least significant bytes are considered.
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
        
        buffer[FREQUENCY_LSB_INDEX] = (byte) frequency;
        
        buffer[FREQUENCY_MSB__INDEX] = (byte) (frequency >> 8);
    }

    /**
     * Returns the current duration set to be played when the command is
     * sent.
     *
     * @return The duration of the tone.
     */
    public int getDuration() {
        return duration;
    }
    
    /**
     * Sets the duration of the tone to play.
     * @param duration The frequency to play. On the two least significant bytes are considered.
     */
    public void setDuration(int duration) {
        this.duration = duration;
        
        buffer[DURATION_LSB_INDEX] = (byte) duration;
        
        buffer[DURATION_MSB_INDEX] = (byte) (duration >> 8);            
    }
    
    
    
}
