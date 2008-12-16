package pt.citar.diablu.sms2osc.util;

import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JTextArea;

public class S2OTextAreaHandler extends Handler {

    private static int MAXLOGS = 20;
    private JTextArea textArea;
    private Level level;
    private Vector<LogRecord> logs;

    public S2OTextAreaHandler(JTextArea textArea) {
        this.textArea = textArea;
        logs = new Vector<LogRecord>();
        this.level = Level.ALL;
    }

    @Override
    public void publish(LogRecord record) {

        if (logs.size() == MAXLOGS) {
            logs.remove(0);
        }
        if (level.intValue() <= record.getLevel().intValue()) {
            textArea.setText("");
            logs.add(record);
            
            for (int i = 0; i < logs.size(); i++) {
                if (i != 0) {
                    textArea.append("\n");
                }
                textArea.append(logs.elementAt(i).getMessage());
                textArea.setCaretPosition(textArea.getText().length());
            }
        }
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws SecurityException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLevel(Level level) {
        this.level = level;
    }
}
