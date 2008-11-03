package pt.citar.diablu.sms2osc;

import pt.citar.diablu.sms2osc.bluetooth.S2OBTConnection;
import pt.citar.diablu.sms2osc.gui.S2OGUI;
import pt.citar.diablu.sms2osc.osc.S2OOscServer;
import pt.citar.diablu.sms2osc.util.S2OCommPortList;
import pt.citar.diablu.sms2osc.util.S2OProperties;


public class S2O {

    private S2OCommPortList commPortList;
    private S2OProperties properties;
    
    private S2OGUI gui;
    private S2OOscServer oscServer;
    
    
    private S2OBTConnection btConnection;
    public Thread btConnectionThread;
    
    public S2O()
    {
        commPortList = new S2OCommPortList();
        properties = new S2OProperties(this);
        properties.loadProperties();
        oscServer = new S2OOscServer(this);
        gui = new S2OGUI(this);
        btConnection = new S2OBTConnection(this, properties.getGateway(), "COM6", 57600, "Siemens", "S65");
    
        
    }
    
    public static void main(String[] args) {
    
        S2O s2o = new S2O();
        s2o.gui.setVisible(true);
        
    }

    public S2OGUI getGui() {
        return gui;
    }

    public S2OBTConnection getBtConnection() {
        return btConnection;
    }

    public Thread getBtConnectionThread() {
        return btConnectionThread;
    }

    
    public S2OProperties getProperties() {
        return properties;
    }

    public S2OCommPortList getCommPortList() {
        return commPortList;
    }

    public S2OOscServer getOscServer() {
        return oscServer;
    }
    
    
    
    
    
    
    
    
    

}
