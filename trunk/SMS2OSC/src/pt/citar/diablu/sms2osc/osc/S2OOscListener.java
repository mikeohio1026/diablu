package pt.citar.diablu.sms2osc.osc;


import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import java.net.SocketAddress;
import pt.citar.diablu.sms2osc.S2O;

/**
 *
 * @author Raspa
 */
public class S2OOscListener implements OSCListener {

    S2O s2o;

    public S2OOscListener(S2O s2o) {
        this.s2o = s2o;
    }

    public void messageReceived(OSCMessage msg, SocketAddress sender, long time) {
        
        String msgReceived = msg.getName();
        
        for(int i = 0; i <msg.getArgCount(); i++)
        {
            msgReceived += " " + msg.getArg(i);
        }
        System.out.println(msgReceived);
        
    }
    
    
}




