
package pt.citar.diablu.sms2osc.gui;

import java.util.logging.Level;
import javax.swing.JFrame;
import pt.citar.diablu.sms2osc.S2O;


public class S2OConnectingPopUp extends javax.swing.JFrame {

    S2O s2o;
    String commPort;

    public S2OConnectingPopUp(S2O s2o, String commPort) {
        this.s2o = s2o;
        this.commPort = commPort;
        initComponents();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        messageLabel.setText("Establishing Connection on port " + commPort);
        okButton.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cancelButton = new javax.swing.JButton();
        messageLabel = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cancelButton.setLabel("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageLabel.setText("jLabel1");

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton))
                    .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(messageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        s2o.getBtConnectionThread().interrupt();
        s2o.getBtConnection().stopConnection();
        s2o.getGui().setVisible(true);
        this.setVisible(false);
        s2o.getGui().setGatewayConnectionState(false);

    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        s2o.getGui().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    public void connected() {
        cancelButton.setVisible(false);
        okButton.setVisible(true);
        messageLabel.setText("Connection established on port " + commPort);
        s2o.getGui().setGatewayConnectionState(true);
        s2o.getLogger().log(Level.WARNING, "Connection established on port " +  commPort);


    }

    public void connectionFailed() {
        cancelButton.setVisible(false);
        okButton.setVisible(true);
        messageLabel.setText("Connection failed on port " + commPort);
        s2o.getGui().setGatewayConnectionState(false);
        s2o.getLogger().log(Level.WARNING, "Connection failed on port " +  commPort);

    }

    public void connectionStopped() {
        this.setVisible(true);
        cancelButton.setVisible(false);
        okButton.setVisible(true);
        messageLabel.setText("Connection stopped port " + s2o.getBtConnection().getPort());
        s2o.getGui().setGatewayConnectionState(false);
        s2o.getLogger().log(Level.WARNING, "Connection stopped on port " +  commPort);

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}
