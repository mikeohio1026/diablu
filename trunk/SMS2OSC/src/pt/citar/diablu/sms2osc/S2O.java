package pt.citar.diablu.sms2osc;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import pt.citar.diablu.sms2osc.bluetooth.S2OBTConnection;
import pt.citar.diablu.sms2osc.gui.S2OConnectingPopUp;
import pt.citar.diablu.sms2osc.gui.S2OGUI;
import pt.citar.diablu.sms2osc.gui.S2OMessagePopUp;
import pt.citar.diablu.sms2osc.osc.S2OOscClient;
import pt.citar.diablu.sms2osc.osc.S2OOscServer;
import pt.citar.diablu.sms2osc.parser.S2OSMSParser;
import pt.citar.diablu.sms2osc.util.S2OCommPortList;
import pt.citar.diablu.sms2osc.util.S2OProperties;
import pt.citar.diablu.sms2osc.util.S2OTextAreaHandler;

public class S2O {

    private Logger logger;
    private S2OCommPortList commPortList;
    private S2OProperties properties;
    private S2OGUI gui;
    private S2OConnectingPopUp connectionPopUpGui;
    private S2OMessagePopUp messagePopUp;
    private S2OTextAreaHandler textAreaHandler;
    private FileHandler fileHandler;
    private S2OOscServer oscServer;
    private S2OOscClient oscClient;
    private S2OSMSParser smsParser;
    private S2OBTConnection btConnection;
    public Thread btConnectionThread;

    public S2O() {
        logger = Logger.getLogger("pt.citar.diablu.sms2osc");
        commPortList = new S2OCommPortList();
        properties = new S2OProperties(this);
        
        gui = new S2OGUI(this);
        setupLogger();

        messagePopUp = new S2OMessagePopUp();
        properties.loadProperties();
        oscServer = new S2OOscServer(this);
        oscClient = new S2OOscClient(this);

        smsParser = new S2OSMSParser(this, properties.useParser());
        if (smsParser.isActive()) {
            smsParser.getCommands(properties.getCommands());
        }
        btConnection = new S2OBTConnection(this, properties.getGateway(), this.getGui().getCommPort(), 57600, "", "");
    }

    public static void main(String[] args) {

        S2O s2o = new S2O();
        s2o.gui.setVisible(true);
    }

    private void setupLogger() {
        logger.setLevel(Level.ALL);

        textAreaHandler = new S2OTextAreaHandler(gui.getLogTextArea());
        try {
            fileHandler = new FileHandler("logs.txt");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        fileHandler.setFormatter(new SimpleFormatter());

        logger.addHandler(textAreaHandler);
        logger.addHandler(fileHandler);

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

    public S2OOscClient getOscClient() {
        return oscClient;
    }

    public S2OTextAreaHandler getTextAreaHandler() {
        return textAreaHandler;
    }

    public Logger getLogger() {
        return logger;
    }

    public S2OSMSParser getSmsParser() {
        return smsParser;
    }

    public void setBtConnectionThread(Thread btConnectionThread) {
        this.btConnectionThread = btConnectionThread;
    }

    public S2OConnectingPopUp getConnectionPopUpGui() {
        return connectionPopUpGui;
    }

    public void setConnectionPopUpGui(S2OConnectingPopUp connectionPopUpGui) {
        this.connectionPopUpGui = connectionPopUpGui;
    }

    public void setBtConnection(S2OBTConnection btConnection) {
        this.btConnection = btConnection;
    }

    public S2OMessagePopUp getMessagePopUp() {
        return messagePopUp;
    }
}
