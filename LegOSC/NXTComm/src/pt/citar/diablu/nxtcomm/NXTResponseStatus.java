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
    
    
    /**
     * Receives the status response from the NXTBrick.
     * The status code can be obtained by invoking the <code>getStatus()</code> from the <code>NXTResponse</code> class.
     *
     * @param is The <code>InputStream</code> from which the response is read.
     */
    public void receiveResponse(InputStream is) throws IOException {
        buffer  = new byte[3];
        is.read(buffer);
    }
}
