/*
 * DiABlu SMS2OSC
 * Copyright (C) 2008-2009, CITAR (Research Centre for Science and Technology in Art)
 *
 * This is part of the DiABlu Project, created by Jorge Cardoso - http://diablu.jorgecardoso.eu
 *
 *
 * Contributors:
 * - Pedro Santos <psantos@porto.ucp.pt>
 * - Jorge Cardoso <jccardoso@porto.ucp.pt>
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */


package pt.citar.diablu.sms2osc.bluetooth;

import de.sciss.net.OSCMessage;
import pt.citar.diablu.sms2osc.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
    SerialModemGateway gateway;

    public S2OBTConnection(S2O s2o, String id, String port, int baud, String manufacturer, String model) {
        this.s2o = s2o;
        this.id = id;
        this.port = port;
        this.baud = baud;
        this.manufacturer = manufacturer;
        this.model = model;
        this.connected = false;

    }

    public void startConnection() {
        try {


            List<InboundMessage> msgList;

            InboundNotification inboundNotification = new InboundNotification();
            OutboundNotification outboundNotification = new OutboundNotification();


            srv = new Service();
            gateway = new SerialModemGateway(id, port, baud, manufacturer, model);
            gateway.setInbound(true);
            gateway.setOutbound(true);
            srv.setOutboundNotification(outboundNotification);
            srv.setInboundNotification(inboundNotification);
            srv.addGateway(gateway);
            srv.startService();


            /*System.out.println("  Modem Information: build2");
            System.out.println("  Manufacturer: " + gateway.getManufacturer());
            System.out.println("  Model: " + gateway.getModel());
            System.out.println("  Serial No: " + gateway.getSerialNo());
            System.out.println("  SIM IMSI: " + gateway.getImsi());
            System.out.println("  Signal Level: " + gateway.getSignalLevel() + "%");
            System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");*/

            msgList = new ArrayList<InboundMessage>();
            srv.readMessages(msgList, MessageClasses.ALL);
            for (InboundMessage msg : msgList) {
                System.out.println(msg);
            }
            this.connected = true;
            s2o.getConnectionPopUpGui().connected();

        } catch (TimeoutException ex) {
            s2o.getLogger().log(Level.WARNING, ex.getMessage());
            this.stopConnection();
            s2o.getConnectionPopUpGui().connectionFailed();
        } catch (GatewayException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
            this.stopConnection();
            
            s2o.getConnectionPopUpGui().connectionFailed();

        } catch (SMSLibException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
            
            s2o.getConnectionPopUpGui().connectionFailed();

        } catch (IOException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
            
            s2o.getConnectionPopUpGui().connectionFailed();

        } catch (InterruptedException ex) {
            s2o.getLogger().log(Level.WARNING, ex.getMessage());
            this.stopConnection();
            s2o.getConnectionPopUpGui().connectionStopped();

        }
    }

    public void sendMessage(String smsRecipient, String smsBody) {
        try {
            s2o.getGui().addMessageRow(smsRecipient, "Outbound", smsBody);
            OutboundMessage msg;
            msg = new OutboundMessage(smsRecipient, smsBody);
            srv.sendMessage(msg);

        } catch (TimeoutException ex) {
            s2o.getLogger().log(Level.WARNING, ex.getMessage());
        } catch (GatewayException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        } catch (InterruptedException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        }
    }

    public void stopConnection() {
        try {
            this.connected = false;
            srv.stopService();
            gateway.stopGateway();
        } catch (TimeoutException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        } catch (GatewayException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
        } catch (InterruptedException ex) {
            s2o.getLogger().log(Level.SEVERE, ex.getMessage());
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
                if (s2o.getSmsParser().isActive()) {
                    s2o.getSmsParser().parse(msg.getText());
                } else {
                    s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/sms", new Object[]{msg.getOriginator(), msg.getText()}));
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

    public void run() {
        S2OBTConnectionTimeout timeout = new S2OBTConnectionTimeout(s2o, 20000);
        Thread to = new Thread(timeout);
        to.start();
        this.startConnection();
        to.interrupt();
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getPort() {
        return port;
    }
}
