
package pt.citar.diablu.sms2osc.gui;

import java.util.logging.Level;
import javax.swing.JTextArea;
import pt.citar.diablu.sms2osc.*;


public class S2OGUI extends javax.swing.JFrame {

    S2O s2o;
    int rowNumber = 0;
    
    public S2OGUI(S2O s2o) {
        this.s2o = s2o;
        initComponents();
        setCommPorts();
    }
    
    private void setCommPorts()
    {
        for(String port: s2o.getCommPortList().getCommPorts())
            CommPorts.addItem(port);
        CommPorts.setSelectedItem(s2o.getProperties().getComPort());
            
                
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        S2OTabbedPane = new javax.swing.JTabbedPane();
        ConnectionPanel = new javax.swing.JPanel();
        OSCPanel = new javax.swing.JPanel();
        YourAppPane = new javax.swing.JPanel();
        YAHostnameTextField = new javax.swing.JTextField();
        YAHostnameLabel = new javax.swing.JLabel();
        YAPortjLabel = new javax.swing.JLabel();
        YAPortTextField = new javax.swing.JTextField();
        OSCConnectButton = new javax.swing.JButton();
        SMS2OSCPanel = new javax.swing.JPanel();
        S2OPortLabel = new javax.swing.JLabel();
        S2OPortTextField = new javax.swing.JTextField();
        GatewayPanel = new javax.swing.JPanel();
        CommPorts = new javax.swing.JComboBox();
        Connect = new javax.swing.JButton();
        CommPortLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        jComboBox1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
                .addContainerGap(17, Short.MAX_VALUE))
        );

        OSCConnectButton.setText("Start Server");
        OSCConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OSCConnectButtonActionPerformed(evt);
            }
        });

        SMS2OSCPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("SMS2OSC"));

        S2OPortLabel.setText("Port:");

        S2OPortTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        S2OPortTextField.setText(s2o.getProperties().getOutgoingPort() );

        javax.swing.GroupLayout SMS2OSCPanelLayout = new javax.swing.GroupLayout(SMS2OSCPanel);
        SMS2OSCPanel.setLayout(SMS2OSCPanelLayout);
        SMS2OSCPanelLayout.setHorizontalGroup(
            SMS2OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SMS2OSCPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(S2OPortLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(S2OPortTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addContainerGap())
        );
        SMS2OSCPanelLayout.setVerticalGroup(
            SMS2OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SMS2OSCPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SMS2OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(S2OPortLabel)
                    .addComponent(S2OPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout OSCPanelLayout = new javax.swing.GroupLayout(OSCPanel);
        OSCPanel.setLayout(OSCPanelLayout);
        OSCPanelLayout.setHorizontalGroup(
            OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OSCPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(YourAppPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SMS2OSCPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OSCPanelLayout.createSequentialGroup()
                .addContainerGap(327, Short.MAX_VALUE)
                .addComponent(OSCConnectButton)
                .addContainerGap())
        );

        OSCPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {SMS2OSCPanel, YourAppPane});

        OSCPanelLayout.setVerticalGroup(
            OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OSCPanelLayout.createSequentialGroup()
                .addGroup(OSCPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(YourAppPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SMS2OSCPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addComponent(OSCConnectButton)
                .addContainerGap())
        );

        OSCConnectButton.getAccessibleContext().setAccessibleName("");

        GatewayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Gateway"));

        Connect.setText("Connect");
        Connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectActionPerformed(evt);
            }
        });

        CommPortLabel.setText("Port:");

        javax.swing.GroupLayout GatewayPanelLayout = new javax.swing.GroupLayout(GatewayPanel);
        GatewayPanel.setLayout(GatewayPanelLayout);
        GatewayPanelLayout.setHorizontalGroup(
            GatewayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GatewayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CommPortLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CommPorts, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GatewayPanelLayout.createSequentialGroup()
                .addContainerGap(81, Short.MAX_VALUE)
                .addComponent(Connect)
                .addContainerGap())
        );
        GatewayPanelLayout.setVerticalGroup(
            GatewayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GatewayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GatewayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CommPortLabel)
                    .addComponent(CommPorts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addComponent(Connect)
                .addContainerGap())
        );

        javax.swing.GroupLayout ConnectionPanelLayout = new javax.swing.GroupLayout(ConnectionPanel);
        ConnectionPanel.setLayout(ConnectionPanelLayout);
        ConnectionPanelLayout.setHorizontalGroup(
            ConnectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConnectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(OSCPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(GatewayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ConnectionPanelLayout.setVerticalGroup(
            ConnectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConnectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ConnectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(OSCPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(GatewayPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        S2OTabbedPane.addTab("Connection", ConnectionPanel);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Number", "Direction", "Title 3"
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
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setResizable(false);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(1).setResizable(false);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(2).setResizable(false);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(600);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addContainerGap())
        );

        S2OTabbedPane.addTab("tab3", jPanel2);

        logTextArea.setColumns(20);
        logTextArea.setRows(5);
        jScrollPane1.setViewportView(logTextArea);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST"  }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        S2OTabbedPane.addTab("Log", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(S2OTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(S2OTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void ConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectActionPerformed
if(s2o.getBtConnection().isConnected())
    {
        s2o.getBtConnection().stopConnection();
    }
    s2o.getBtConnection().setPort(CommPorts.getSelectedItem().toString());
    
    Thread btT = new Thread(s2o.getBtConnection());
    btT.start();
}//GEN-LAST:event_ConnectActionPerformed

private void OSCConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OSCConnectButtonActionPerformed
if(!s2o.getOscServer().isConnected())
        s2o.getOscServer().start();
    else
        s2o.getOscServer().stop();
}//GEN-LAST:event_OSCConnectButtonActionPerformed

private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    s2o.getLogger().setLevel(Level.parse((String) jComboBox1.getSelectedItem()));
}//GEN-LAST:event_jComboBox1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CommPortLabel;
    private javax.swing.JComboBox CommPorts;
    private javax.swing.JButton Connect;
    private javax.swing.JPanel ConnectionPanel;
    private javax.swing.JPanel GatewayPanel;
    private javax.swing.JButton OSCConnectButton;
    private javax.swing.JPanel OSCPanel;
    private javax.swing.JLabel S2OPortLabel;
    private javax.swing.JTextField S2OPortTextField;
    private javax.swing.JTabbedPane S2OTabbedPane;
    private javax.swing.JPanel SMS2OSCPanel;
    private javax.swing.JLabel YAHostnameLabel;
    private javax.swing.JTextField YAHostnameTextField;
    private javax.swing.JTextField YAPortTextField;
    private javax.swing.JLabel YAPortjLabel;
    private javax.swing.JPanel YourAppPane;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea logTextArea;
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
    
    public void serverButtonStop(boolean b) {
        if(b)
            OSCConnectButton.setText("Stop Server");
        else
            OSCConnectButton.setText("Start Server");
    }

    public JTextArea getLogTextArea() {
        return logTextArea;
    }
    
    public void addMessageRow(String number, String direction, String message)
    {
        if(rowNumber < 20)
        {
            jTable1.getModel().setValueAt(number, rowNumber,0);
            jTable1.getModel().setValueAt(direction, rowNumber,1);
            jTable1.getModel().setValueAt(message, rowNumber,2);
            jTable1.changeSelection(rowNumber, 0, false, false);
            rowNumber++;
        }
        else
        {
            for(int i = 0; i < 19; i++)
            {
                jTable1.getModel().setValueAt(jTable1.getModel().getValueAt(i+1, 0), i,0);
                jTable1.getModel().setValueAt(jTable1.getModel().getValueAt(i+1, 1), i,1);
                jTable1.getModel().setValueAt(jTable1.getModel().getValueAt(i+1, 2), i,2);
            }
            jTable1.getModel().setValueAt(number, 19,0);
            jTable1.getModel().setValueAt(direction,19,1);
            jTable1.getModel().setValueAt(message, 19,2);
            jTable1.changeSelection(19, 0, false, false);
            rowNumber++;
        }
    }

    
}
