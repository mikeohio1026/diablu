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

package pt.citar.diablu.sms2osc.gui;

import de.sciss.net.OSCMessage;
import java.util.logging.Level;
import javax.swing.JTable;
import javax.swing.JTextArea;
import pt.citar.diablu.sms2osc.*;
import pt.citar.diablu.sms2osc.bluetooth.S2OBTConnection;
import pt.citar.diablu.sms2osc.util.S2OTableMouseAdapter;

public class S2OGUI extends javax.swing.JFrame {

    S2O s2o;
    int rowNumber = 0;
    boolean isGatewayConnected = false;
    private S2OTableMouseAdapter tableMouseAdapter;

    public S2OGUI(S2O s2o) {
        this.s2o = s2o;
        initComponents();
        setCommPorts();
        tableMouseAdapter = new S2OTableMouseAdapter(s2o);

        SMSTable.addMouseListener(tableMouseAdapter);

    }

    private void setCommPorts() {
        for (String port : s2o.getCommPortList().getCommPorts()) {
            CommPorts.addItem(port);
        }
        CommPorts.setSelectedItem(s2o.getProperties().getComPort());


    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        S2OTabbedPane = new javax.swing.JTabbedPane();
        ConnectionPanel = new javax.swing.JPanel();
        OSCPanel = new javax.swing.JPanel();
        YourAppPane = new javax.swing.JPanel();
        YAHostnameTextField = new javax.swing.JTextField();
        YAHostnameLabel = new javax.swing.JLabel();
        YAPortjLabel = new javax.swing.JLabel();
        YAPortTextField = new javax.swing.JTextField();
        SMS2OSCPanel = new javax.swing.JPanel();
        S2OPortLabel = new javax.swing.JLabel();
        S2OPortTextField = new javax.swing.JTextField();
        OSCConnectButton = new javax.swing.JButton();
        GatewayPanel = new javax.swing.JPanel();
        CommPorts = new javax.swing.JComboBox();
        Connect = new javax.swing.JButton();
        CommPortLabel = new javax.swing.JLabel();
        CommPortLabel1 = new javax.swing.JLabel();
        BaudRateFormatedTextField = new javax.swing.JFormattedTextField();
        SMSPannel = new javax.swing.JPanel();
        SMSTableScrollPane = new javax.swing.JScrollPane();
        SMSTable = new javax.swing.JTable();
        logPannel = new javax.swing.JPanel();
        logScrollPane = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        LevelComboBox = new javax.swing.JComboBox();
        TestPanel = new javax.swing.JPanel();
        messageScrollPane = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        messageLabel = new javax.swing.JLabel();
        outRadioButton = new javax.swing.JRadioButton();
        inRadioButton = new javax.swing.JRadioButton();
        sendButton = new javax.swing.JButton();
        numberLabel = new javax.swing.JLabel();
        numberTextField = new javax.swing.JTextField();
        AboutPanel = new javax.swing.JPanel();
        Imagem = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DiABlu SMS2OSC");

        OSCPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("OSC"));

        YourAppPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Your Application"));

        YAHostnameTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        YAHostnameTextField.setText(s2o.getProperties().getRemoteIP() );

        YAHostnameLabel.setText("Hostname:");

        YAPortjLabel.setText("Port:");

        YAPortTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        YAPortTextField.setText(s2o.getProperties().getIncomingPort() );

        javax.swing.GroupLayout YourAppPaneLayout = new javax.swing.GroupLayout(YourAppPane);
        YourAppPane.setLayout(YourAppPaneLayout);
        YourAppPaneLayout.setHorizontalGroup(
            YourAppPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(YourAppPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(YourAppPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(YourAppPaneLayout.createSequentialGroup()
                        .addComponent(YAHostnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(YAHostnameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                    .addGroup(YourAppPaneLayout.createSequentialGroup()
                        .addComponent(YAPortjLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(YAPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        YourAppPaneLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {YAHostnameLabel, YAPortjLabel});

        YourAppPaneLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {YAHostnameTextField, YAPortTextField});

        YourAppPaneLayout.setVerticalGroup(
            YourAppPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(YourAppPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(YourAppPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(YAHostnameLabel)
                    .addComponent(YAHostnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(YourAppPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(YAPortjLabel)
                    .addComponent(YAPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        SMS2OSCPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("SMS2OSC"));

        S2OPortLabel.setText("Port:");

        S2OPortTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        S2OPortTextField.setText(s2o.getProperties().getOutgoingPort() );

        OSCConnectButton.setText("Start Server");
        OSCConnectButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        OSCConnectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        OSCConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OSCConnectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SMS2OSCPanelLayout = new javax.swing.GroupLayout(SMS2OSCPanel);
        SMS2OSCPanel.setLayout(SMS2OSCPanelLayout);
        SMS2OSCPanelLayout.setHorizontalGroup(
            SMS2OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SMS2OSCPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SMS2OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SMS2OSCPanelLayout.createSequentialGroup()
                        .addComponent(S2OPortLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(S2OPortTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                    .addComponent(OSCConnectButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        SMS2OSCPanelLayout.setVerticalGroup(
            SMS2OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SMS2OSCPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SMS2OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(S2OPortLabel)
                    .addComponent(S2OPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(OSCConnectButton)
                .addContainerGap())
        );

        OSCConnectButton.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout OSCPanelLayout = new javax.swing.GroupLayout(OSCPanel);
        OSCPanel.setLayout(OSCPanelLayout);
        OSCPanelLayout.setHorizontalGroup(
            OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OSCPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(YourAppPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(SMS2OSCPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        OSCPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {SMS2OSCPanel, YourAppPane});

        OSCPanelLayout.setVerticalGroup(
            OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OSCPanelLayout.createSequentialGroup()
                .addGroup(OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SMS2OSCPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(YourAppPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        GatewayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Gateway"));

        Connect.setText("Connect");
        Connect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectActionPerformed(evt);
            }
        });

        CommPortLabel.setText("Port:");

        CommPortLabel1.setText("Baud Rate:");

        BaudRateFormatedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        javax.swing.GroupLayout GatewayPanelLayout = new javax.swing.GroupLayout(GatewayPanel);
        GatewayPanel.setLayout(GatewayPanelLayout);
        GatewayPanelLayout.setHorizontalGroup(
            GatewayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GatewayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GatewayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(GatewayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GatewayPanelLayout.createSequentialGroup()
                            .addComponent(CommPortLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(CommPorts, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GatewayPanelLayout.createSequentialGroup()
                            .addComponent(CommPortLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(BaudRateFormatedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(Connect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                .addContainerGap())
        );
        GatewayPanelLayout.setVerticalGroup(
            GatewayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GatewayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GatewayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CommPortLabel)
                    .addComponent(CommPorts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GatewayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CommPortLabel1)
                    .addComponent(BaudRateFormatedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(Connect)
                .addContainerGap())
        );

        BaudRateFormatedTextField.setText(s2o.getProperties().getBaudRate());

        javax.swing.GroupLayout ConnectionPanelLayout = new javax.swing.GroupLayout(ConnectionPanel);
        ConnectionPanel.setLayout(ConnectionPanelLayout);
        ConnectionPanelLayout.setHorizontalGroup(
            ConnectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConnectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(OSCPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(GatewayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ConnectionPanelLayout.setVerticalGroup(
            ConnectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConnectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ConnectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(OSCPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(GatewayPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        S2OTabbedPane.addTab("Connection", ConnectionPanel);

        Object[][] to = new Object[s2o.getProperties().getTableSize()][3];
        SMSTable.setModel(new javax.swing.table.DefaultTableModel(to,
            new String [] {
                "Number", "Direction", "Message"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        SMSTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        SMSTable.getTableHeader().setReorderingAllowed(false);
        SMSTableScrollPane.setViewportView(SMSTable);
        SMSTable.getColumnModel().getColumn(0).setResizable(false);
        SMSTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        SMSTable.getColumnModel().getColumn(1).setResizable(false);
        SMSTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        SMSTable.getColumnModel().getColumn(2).setResizable(false);
        SMSTable.getColumnModel().getColumn(2).setPreferredWidth(600);

        javax.swing.GroupLayout SMSPannelLayout = new javax.swing.GroupLayout(SMSPannel);
        SMSPannel.setLayout(SMSPannelLayout);
        SMSPannelLayout.setHorizontalGroup(
            SMSPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SMSPannelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SMSTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                .addContainerGap())
        );
        SMSPannelLayout.setVerticalGroup(
            SMSPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SMSPannelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SMSTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addContainerGap())
        );

        S2OTabbedPane.addTab("SMSs", SMSPannel);

        logTextArea.setColumns(20);
        logTextArea.setRows(5);
        logScrollPane.setViewportView(logTextArea);

        LevelComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST"  }));
        LevelComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LevelComboBoxActionPerformed(evt);
            }
        });
        LevelComboBox.setSelectedItem(s2o.getProperties().getLevel().getName());

        javax.swing.GroupLayout logPannelLayout = new javax.swing.GroupLayout(logPannel);
        logPannel.setLayout(logPannelLayout);
        logPannelLayout.setHorizontalGroup(
            logPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logPannelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addComponent(LevelComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        logPannelLayout.setVerticalGroup(
            logPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logPannelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LevelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        S2OTabbedPane.addTab("Log", logPannel);

        messageTextArea.setColumns(20);
        messageTextArea.setRows(5);
        messageScrollPane.setViewportView(messageTextArea);

        messageLabel.setText("Message:");

        buttonGroup1.add(outRadioButton);
        outRadioButton.setText("Send to Cell Phone");

        buttonGroup1.add(inRadioButton);
        inRadioButton.setSelected(true);
        inRadioButton.setText("Send to Application");
        inRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inRadioButtonActionPerformed(evt);
            }
        });

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        numberLabel.setText("Number:");

        javax.swing.GroupLayout TestPanelLayout = new javax.swing.GroupLayout(TestPanel);
        TestPanel.setLayout(TestPanelLayout);
        TestPanelLayout.setHorizontalGroup(
            TestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TestPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TestPanelLayout.createSequentialGroup()
                        .addComponent(numberLabel)
                        .addGap(18, 18, 18)
                        .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addGroup(TestPanelLayout.createSequentialGroup()
                        .addComponent(outRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 462, Short.MAX_VALUE)
                        .addComponent(sendButton))
                    .addComponent(inRadioButton)
                    .addGroup(TestPanelLayout.createSequentialGroup()
                        .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                        .addGap(69, 69, 69)))
                .addContainerGap())
        );
        TestPanelLayout.setVerticalGroup(
            TestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TestPanelLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(TestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberLabel)
                    .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(messageScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(inRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(TestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outRadioButton)
                    .addComponent(sendButton))
                .addGap(29, 29, 29))
        );

        S2OTabbedPane.addTab("Test", TestPanel);

        Imagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Imagem.setText("Imagem");

        javax.swing.GroupLayout AboutPanelLayout = new javax.swing.GroupLayout(AboutPanel);
        AboutPanel.setLayout(AboutPanelLayout);
        AboutPanelLayout.setHorizontalGroup(
            AboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AboutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Imagem, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                .addContainerGap())
        );
        AboutPanelLayout.setVerticalGroup(
            AboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AboutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Imagem, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addContainerGap())
        );

        Imagem.getAccessibleContext().setAccessibleName("Imagem");

        S2OTabbedPane.addTab("About", AboutPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(S2OTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(S2OTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void ConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectActionPerformed
    if (isGatewayConnected) {
        this.setVisible(false);
        s2o.getBtConnection().stopConnection();
        s2o.getConnectionPopUpGui().connectionStopped();
    } else {

        s2o.setBtConnection(new S2OBTConnection(s2o, "gateway", CommPorts.getSelectedItem().toString(), 57600, "", ""));
        s2o.setBtConnectionThread(new Thread(s2o.getBtConnection()));
        s2o.getBtConnection().setPort(CommPorts.getSelectedItem().toString());
        s2o.getProperties().setProperty("ComPort", CommPorts.getSelectedItem().toString());
        s2o.getProperties().setProperty("BaudRate", BaudRateFormatedTextField.getText());

        s2o.setConnectionPopUpGui(new S2OConnectingPopUp(s2o, CommPorts.getSelectedItem().toString()));
        s2o.getConnectionPopUpGui().setVisible(true);

        s2o.setBtConnectionThread(new Thread(s2o.getBtConnection()));
        s2o.getBtConnectionThread().start();
        this.setVisible(false);
    }
}//GEN-LAST:event_ConnectActionPerformed

private void OSCConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OSCConnectButtonActionPerformed
    if (!s2o.getOscServer().isConnected()) {
        s2o.getOscServer().start();
    } else {
        s2o.getOscServer().stop();
    }
}//GEN-LAST:event_OSCConnectButtonActionPerformed

private void LevelComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LevelComboBoxActionPerformed
    s2o.getLogger().setLevel(Level.parse((String) LevelComboBox.getSelectedItem()));
    s2o.getProperties().setProperty("Level", (String) LevelComboBox.getSelectedItem());
}//GEN-LAST:event_LevelComboBoxActionPerformed

private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
    if (inRadioButton.isSelected()) {
        s2o.getGui().addMessageRow(numberTextField.getText(), "Inbound", messageTextArea.getText());
        if (s2o.getSmsParser().isActive()) {
            s2o.getSmsParser().parse(messageTextArea.getText());
        } else {
            s2o.getOscClient().send(new OSCMessage("/diablu/sms2osc/sms", new Object[]{numberTextField.getText(), messageTextArea.getText()}));
            s2o.getLogger().log(Level.INFO, "OSC Message Sent: /diablu/sms2osc/sms " + numberTextField.getText() + " " + messageTextArea.getText());
        }
    } else if (outRadioButton.isSelected()) {
        if (s2o.getBtConnection().isConnected()) {
            s2o.getBtConnection().sendMessage(numberTextField.getText(), messageTextArea.getText());
        }
    }
}//GEN-LAST:event_sendButtonActionPerformed

private void inRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inRadioButtonActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_inRadioButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AboutPanel;
    private javax.swing.JFormattedTextField BaudRateFormatedTextField;
    private javax.swing.JLabel CommPortLabel;
    private javax.swing.JLabel CommPortLabel1;
    private javax.swing.JComboBox CommPorts;
    private javax.swing.JButton Connect;
    private javax.swing.JPanel ConnectionPanel;
    private javax.swing.JPanel GatewayPanel;
    private javax.swing.JLabel Imagem;
    private javax.swing.JComboBox LevelComboBox;
    private javax.swing.JButton OSCConnectButton;
    private javax.swing.JPanel OSCPanel;
    private javax.swing.JLabel S2OPortLabel;
    private javax.swing.JTextField S2OPortTextField;
    private javax.swing.JTabbedPane S2OTabbedPane;
    private javax.swing.JPanel SMS2OSCPanel;
    private javax.swing.JPanel SMSPannel;
    private javax.swing.JTable SMSTable;
    private javax.swing.JScrollPane SMSTableScrollPane;
    private javax.swing.JPanel TestPanel;
    private javax.swing.JLabel YAHostnameLabel;
    private javax.swing.JTextField YAHostnameTextField;
    private javax.swing.JTextField YAPortTextField;
    private javax.swing.JLabel YAPortjLabel;
    private javax.swing.JPanel YourAppPane;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton inRadioButton;
    private javax.swing.JPanel logPannel;
    private javax.swing.JScrollPane logScrollPane;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel numberLabel;
    private javax.swing.JTextField numberTextField;
    private javax.swing.JRadioButton outRadioButton;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables

    public String getHostname() {
        return YAHostnameTextField.getText();
    }

    public String getClientPort() {
        return YAPortTextField.getText();
    }

    public String getServerPort() {
        return S2OPortTextField.getText();
    }

    public void serverButtonStop(boolean bool) {
        if (bool) {
            OSCConnectButton.setText("Stop Server");
        } else {
            OSCConnectButton.setText("Start Server");
        }
    }

    public JTextArea getLogTextArea() {
        return logTextArea;
    }

    public void addMessageRow(String number, String direction, String message) {
        if (rowNumber < s2o.getProperties().getTableSize()) {
            SMSTable.getModel().setValueAt(number, rowNumber, 0);
            SMSTable.getModel().setValueAt(direction, rowNumber, 1);
            SMSTable.getModel().setValueAt(message, rowNumber, 2);
            SMSTable.changeSelection(rowNumber, 0, false, false);
            rowNumber++;
        } else {
            for (int i = 0; i < s2o.getProperties().getTableSize() - 1; i++) {
                SMSTable.getModel().setValueAt(SMSTable.getModel().getValueAt(i + 1, 0), i, 0);
                SMSTable.getModel().setValueAt(SMSTable.getModel().getValueAt(i + 1, 1), i, 1);
                SMSTable.getModel().setValueAt(SMSTable.getModel().getValueAt(i + 1, 2), i, 2);
            }
            SMSTable.getModel().setValueAt(number, s2o.getProperties().getTableSize(), 0);
            SMSTable.getModel().setValueAt(direction, s2o.getProperties().getTableSize(), 1);
            SMSTable.getModel().setValueAt(message, s2o.getProperties().getTableSize(), 2);
            SMSTable.changeSelection(s2o.getProperties().getTableSize(), 0, false, false);
            rowNumber++;
        }
    }

    public String getCommPort() {
        return CommPorts.getSelectedItem().toString();
    }

    public void setGatewayConnectionState(boolean state) {
        this.isGatewayConnected = state;
        if (state) {
            this.Connect.setText("Stop Connection");
        } else {
            this.Connect.setText("Connect");
        }
    }

    public JTable getSmsTable() {
        return SMSTable;
    }




}
