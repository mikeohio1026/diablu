
package pt.citar.diablu.sms2osc.bluetooth;

import de.sciss.net.OSCMessage;
import pt.citar.diablu.sms2osc.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.smslib.GatewayException;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;


public class S2OBTConnection implements Runnable {

    String id;
    String port;
    int baud;
    String manufacturer;
    String model;
    Service srv;
    S2O s2o;
    boolean connected;
    boolean connecting;
    
    public S2OBTConnection(S2O s2o, String id, String port, int baud, String manufacturer, String model) {
        this.s2o = s2o;
        this.id = id;
        this.port = port;
        this.baud = baud;
        this.manufacturer = manufacturer;
        this.model = model;
        this.connected = false;
        this.connecting = false;
    }

    public void startConnection() {
        try {
            
            this.connecting = true;
            List<InboundMessage> msgList;
           
            InboundNotification inboundNotification = new InboundNotification();
            OutboundNotification outboundNotification = new OutboundNotification();
            

            srv = new Service();
            SerialModemGateway gateway = new SerialModemGateway(id, port, baud, manufacturer, model);
            gateway.setInbound(true);
            gateway.setOutbound(true);
            gateway.setOutboundNotification(outboundNotification);
            gateway.setInboundNotification(inboundNotification);
            srv.addGateway(gateway);
            srv.startService();
            System.out.println("Modem Information: build2");
            System.out.println("  Manufacturer: " + gateway.getManufacturer());
            System.out.println("  Model: " + gateway.getModel());
            System.out.println("  Serial No: " + gateway.getSerialNo());
            System.out.println("  SIM IMSI: " + gateway.getImsi());
            System.out.println("  Signal Level: " + gateway.getSignalLevel() + "%");
            System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");

            msgList = new ArrayList<InboundMessage>();
            srv.readMessages(msgList, MessageClasses.ALL);
            for (InboundMessage msg : msgList) {
                System.out.println(msg);
            }
            this.connected = true;
            
        } catch (TimeoutException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
            this.connecting = false;
        } catch (GatewayException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
            this.connecting = false;
        } catch (SMSLibException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
            this.connecting = false;
        } catch (IOException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
            this.connecting = false;
        } catch (InterruptedException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
            this.connecting = false;
        }
    }

    public void sendMessage(String smsRecipient, String smsBody) {
        try {
            OutboundMessage msg;
            msg = new OutboundMessage(smsRecipient, smsBody);
            srv.sendMessage(msg);

        } catch (TimeoutException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GatewayException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopConnection() {
        try {
            srv.stopService();
        } catch (TimeoutException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GatewayException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(S2OBTConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class OutboundNotification implements IOutboundMessageNotification {

        public void process(String gatewayId, OutboundMessage msg) {
            System.out.println("Sending message through: " + gatewayId);
            System.out.println("To: " + msg.getRecipient());
            System.out.println("Body:\n" + msg);
            
        }
    }

    public class InboundNotification implements IInboundMessageNotification {

        public void process(String gatewayId, MessageTypes msgType, InboundMessage msg) {

            if (msgType == MessageTypes.INBOUND) {
                System.out.println(">>> New Inbound message detected from Gateway: " + gatewayId);
                s2o.getGui().addMessageRow(msg.getOriginator(), "Inbound", msg.getText());
                if(s2o.getSmsParser().isActive())
                {
                    s2o.getSmsParser().parse(msg.getText());
                }
                else
                {
                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/sms", new Object[] { msg.getOriginator(), msg.getText()}));
                    System.out.println("Message: " + msg.getText());
                }
                try {
                    srv.deleteMessage(msg);
                } catch (Exception e) {
                    
                System.out.println("Oops!!! Something gone bad...");
                e.printStackTrace();
                }
            }
            
        }
    }
    
    public void connect()
    {
        Thread to = new Thread(new S2OBTConnectionTimeout(s2o, Thread.currentThread(), 15));
        to.start();
        this.startConnection();
        to.interrupt();
    }

    public void run() {
        connect();
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isConnected() {
        return connected;
    }
    
    
    
}
