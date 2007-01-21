/*
 * NXTCommChannel.java
 *
 * Created on 21 de Janeiro de 2007, 12:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pt.citar.diablu.nxt.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Jorge Cardoso
 */
public interface NXTCommChannel {
    
    public NXTResponse sendCommand(NXTCommand command) throws IOException;
}
