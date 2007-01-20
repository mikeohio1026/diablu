/*
 * NXTResponseStatus.java
 *
 * Created on 19 de Janeiro de 2007, 22:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pt.citar.diablu.nxtcomm;

import java.io.InputStream;
import java.io.IOException;

/**
 *
 * @author Jorge Cardoso
 */
public class NXTResponseStatus extends NXTResponse{
    
    /** Creates a new instance of NXTResponseStatus */
    public NXTResponseStatus() {
    }
    
    public void receiveResponse(InputStream is) throws IOException {
        buffer  = new byte[3];
        is.read(buffer);
    }
}
