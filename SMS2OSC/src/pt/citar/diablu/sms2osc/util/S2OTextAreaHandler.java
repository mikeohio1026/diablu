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

package pt.citar.diablu.sms2osc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JTextArea;
import pt.citar.diablu.sms2osc.S2O;

public class S2OTextAreaHandler extends Handler {

    private JTextArea textArea;
    private Level level;
    private Vector<LogRecord> logs;
    S2O s2o;

    public S2OTextAreaHandler(S2O s2o, JTextArea textArea) {
        this.s2o = s2o;
        this.textArea = textArea;
        logs = new Vector<LogRecord>();
        this.level = Level.ALL;
    }

    @Override
    public void publish(LogRecord record) {

        if (logs.size() == s2o.getProperties().getLogSize()) {
            logs.remove(0);
        }
        if (level.intValue() <= record.getLevel().intValue()) {
            textArea.setText("");
            logs.add(record);
            
            for (int i = 0; i < logs.size(); i++) {
                if (i != 0) {
                    textArea.append("\n");
                }
                LogRecord logRecord = logs.elementAt(i);

                textArea.append(getTime(logRecord) +  " ");
                textArea.append(logRecord.getSourceClassName() + " ");
                textArea.append(logRecord.getSourceMethodName() + "\n");
                textArea.append(logRecord.getLevel().getName() + ": ");
                textArea.append(logRecord.getMessage());
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

    private String getTime(LogRecord logRecord)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(logRecord.getMillis());
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        return df.format(cal.getTime());
    }


}
