package pt.citar.diablu.sms2osc.osc;

import de.sciss.net.OSCClient;
import de.sciss.net.OSCMessage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import pt.citar.diablu.sms2osc.S2O;

public class S2OOscClient {

    private OSCClient oscClient;
    private S2O s2o;

    public S2OOscClient(S2O s2o) {
        try {
            this.s2o = s2o;
            if (s2o.getProperties().useLoopback()) {
                oscClient = OSCClient.newUsing(OSCClient.UDP, 12345, true);
            } else {
                oscClient = OSCClient.newUsing(OSCClient.UDP);
            }
            s2o.getLogger().log(Level.INFO, "OSC Client Ready");
        } catch (IOException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        }
    }

    public void send(OSCMessage oscMessage) {
        String message = oscMessage.getName();
        for (int i = 0; i < oscMessage.getArgCount(); i++) {
            message += " " + oscMessage.getArg(i);
        }

        try {
            InetSocketAddress isa = new InetSocketAddress(s2o.getGui().getHostname(), Integer.parseInt(s2o.getGui().getClientPort()));
            oscClient.setTarget(isa);
            oscClient.start();
            oscClient.send(oscMessage);
            oscClient.stop();
            s2o.getLogger().log(Level.INFO, "OSC Message Sent: " + message);

        } catch (IOException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        }
    }
}
