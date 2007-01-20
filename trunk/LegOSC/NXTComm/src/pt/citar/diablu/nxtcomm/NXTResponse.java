/*
 * NXTResponse.java
 *
 * Created on 19 de Janeiro de 2007, 21:39
 *
 * 
 */
package pt.citar.diablu.nxtcomm;

import java.io.InputStream;
import java.io.IOException;


/**
 *
 * @author Jorge Cardoso
 */
public abstract class NXTResponse {
    /* The position of the status byte in the response packet */
    protected static final int STATUS_BYTE_INDEX = 2;

    
    /**
     * Success status code.
     */
    public static final byte SUCCESS = 0x00;
    /**
     * PENDING_COMMUNICATION_TRANSACTION_IN_PROGRESS status code.
     */
    public static final byte PENDING_COMMUNICATION_TRANSACTION_IN_PROGRESS = 0x20;
    public static final byte SPECIFIED_MAILBOX_QUEUE_IS_EMPTY = 0x40;
    public static final byte REQUEST_FAILED = (byte)0xBD;
    public static final byte UNKNOWN_COMMAND_OPCODE = (byte)0xBE;
    public static final byte INSANE_PACKET = (byte)0xBF;
    public static final byte DATA_CONTAINS_OUT_OF_RANGE_VALUES = (byte)0xC0;
    public static final byte COMMUNICATION_BUS_ERROR = (byte)0xDD;
    public static final byte NO_FREE_MEMORY_IN_COMMUNICATION_BUFFER = (byte)0xDE;
    public static final byte SPECIFIED_CHANNEL_CONNECTION_IS_NOT_VALID = (byte)0xDF;
    public static final byte SPECIFIED_CHANNEL_CONNECTION_NOT_CONFIGURED_OR_BUSY = (byte)0xE0;
    public static final byte NO_ACTIVE_PROGRAM = (byte)0xEC;
    public static final byte ILLEGAL_SIZE_SPECIFIED = (byte)0xED;
    public static final byte ILLEGAL_MAILBOX_QUEUE_ID_SPECIFIED = (byte)0xEE;
    public static final byte ATTEMPTED_TO_ACCESS_INVALID_FIELD_OF_A_STRUCTURE = (byte)0xEF;
    public static final byte BAD_INPUT_OR_OUTPUT_SPECIFIED = (byte)0xF0;
    public static final byte INSUFFICIENT_MEMORY_AVAILABLE = (byte)0xFB;
    public static final byte BAD_ARGUMENTS = (byte)0xFF;
        
    /**
     * The response packet. 
     */
    protected byte []buffer;
    
    /** Creates a new instance of NXTResponse */
    public NXTResponse() {
    }
    
    public abstract void receiveResponse(InputStream is) throws IOException;
    
    public int getStatus() {
        return buffer[STATUS_BYTE_INDEX];
    }
    
    public String toString() {
        switch (this.getStatus()) {
            case SUCCESS: return "Success"; 
            case PENDING_COMMUNICATION_TRANSACTION_IN_PROGRESS: return "Pending communication transaction in progress"; 
            case SPECIFIED_MAILBOX_QUEUE_IS_EMPTY: return "Specified mailbox queue is empty"; 
            case REQUEST_FAILED: return "Request failed (i.e. specified file not found)";
            case UNKNOWN_COMMAND_OPCODE: return "Unknown command opcode";
            case INSANE_PACKET: return "Insane packet";
            case DATA_CONTAINS_OUT_OF_RANGE_VALUES: return "Data contains out-of-range values";
            case COMMUNICATION_BUS_ERROR: return "Communication bus error";
            case NO_FREE_MEMORY_IN_COMMUNICATION_BUFFER: return "No free memory in communication buffer";
            case SPECIFIED_CHANNEL_CONNECTION_IS_NOT_VALID: return "Specified channel/connection is not valid";
            case SPECIFIED_CHANNEL_CONNECTION_NOT_CONFIGURED_OR_BUSY: return "Specified channel/connection not configured or busy";
            case NO_ACTIVE_PROGRAM: return "No active program";
            case ILLEGAL_SIZE_SPECIFIED: return "Illegal size specified";
            case ILLEGAL_MAILBOX_QUEUE_ID_SPECIFIED: return "Illegal mailbox queue ID specified";
            case ATTEMPTED_TO_ACCESS_INVALID_FIELD_OF_A_STRUCTURE: return "Attempted to access invalid field of a structure";
            case BAD_INPUT_OR_OUTPUT_SPECIFIED: return "Bad input or output specified";
            case INSUFFICIENT_MEMORY_AVAILABLE: return "Insufficient memory available";
            case BAD_ARGUMENTS: return "Bad arguments";
            default: return "Panic!: Unkown Status Response!";
        }
    }
}
