

package pt.citar.diablu.sms2osc.osc;


import de.sciss.net.OSCServer;
import java.io.IOException;
import java.util.logging.Level;
import pt.citar.diablu.sms2osc.S2O;


public class S2OOscServer {

    private S2O s2o;
    private S2OOscListener oscListener;
    private OSCServer oscServer;
    private boolean connected;

    public S2OOscServer(S2O s2o) {
        this.s2o = s2o;
        connected = false;
        this.oscListener = new S2OOscListener(s2o);

    }

    public void start() {

        try {
            oscServer = OSCServer.newUsing(OSCServer.UDP, Integer.parseInt(s2o.getGui().getServerPort()), true);
            oscServer.addOSCListener(oscListener);
            oscServer.start();
            connected = true;
            s2o.getGui().serverButtonStop(connected);
            s2o.getProperties().setProperty("OutgoingPort", s2o.getGui().getServerPort());
            s2o.getLogger().log(Level.INFO, "OSC Server started on port " + s2o.getGui().getServerPort());
        } catch (IOException ex) {
            oscServer.dispose();
            connected = false;
            s2o.getGui().serverButtonStop(!connected);
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        }
    }

    public void stop() {
       
        oscServer.dispose();
        while (!oscServer.isActive())
        {
            connected = false;
            s2o.getGui().serverButtonStop(connected);
            break;
        }
        s2o.getLogger().log(Level.INFO, "OSC Server stopped");
    }

    public boolean isConnected() {
        return connected;
    }
    
    
}
