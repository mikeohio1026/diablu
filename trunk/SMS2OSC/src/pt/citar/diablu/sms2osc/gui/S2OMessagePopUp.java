package pt.citar.diablu.sms2osc.gui;

import javax.swing.JFrame;

public class S2OMessagePopUp extends javax.swing.JFrame {

    public S2OMessagePopUp() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        numberLabel = new javax.swing.JLabel();
        number = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        messageScrollPanel = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        closeButton = new javax.swing.JButton();
        directionLabel = new javax.swing.JLabel();
        direction = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        numberLabel.setText("Number :");

        number.setText("jLabel2");

        messageLabel.setText("Message:");

        messageTextArea.setColumns(20);
        messageTextArea.setEditable(false);
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(5);
        messageScrollPanel.setViewportView(messageTextArea);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        directionLabel.setText("Direction:");

        direction.setText("jLabel5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(numberLabel)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(direction)
                                .addComponent(number, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE))
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(directionLabel)
                            .addContainerGap(286, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(286, 286, 286))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(messageScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                            .addContainerGap()))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(closeButton)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberLabel)
                    .addComponent(number))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(directionLabel)
                    .addComponent(direction))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
}//GEN-LAST:event_closeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel direction;
    private javax.swing.JLabel directionLabel;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JScrollPane messageScrollPanel;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel number;
    private javax.swing.JLabel numberLabel;
    // End of variables declaration//GEN-END:variables

    public void setDirection(String direction) {
        if (direction == null) {
            this.direction.setText("Unknown");
        } else {
            this.direction.setText(direction);
        }
    }

    public void setNumber(String number) {
        this.number.setText(number);
    }

    public void setMessage(String message) {
        messageTextArea.setText(message);
    }
}

