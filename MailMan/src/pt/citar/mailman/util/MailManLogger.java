/*
 * DiABlu Mailman
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

package pt.citar.mailman.util;

import pt.citar.mailman.MailMan;
import pt.citar.mailman.util.datastructures.MailManLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.citar.mailman.util.datastructures.MailManLogFIFO;


public class MailManLogger {
    public static int BT_FILE_TRANSFER = 1;
    public static int OSC_MESSAGE = 2;
    public static int DEVICE_DETECTED = 4;
    public static int OTHER = 8;
    
    private static int ALL = 15;
    private static int NONE = 0;
    
    
    
    private MailMan mailman;
    private Vector<MailManLog> log = new Vector<MailManLog>();
    private MailManLogFIFO logs = new MailManLogFIFO(100);
    private String filename;
    
    public MailManLogFIFO getLogs() {
        return logs;
    }
    private int level;
    
    public MailManLogger(MailMan mailman, String filename) {
        try {
            this.mailman = mailman;
            this.level = ALL;
            this.filename = filename;
            File f = new File(filename);
            f.createNewFile();
            readFile();
        } catch (IOException ex) {
            Logger.getLogger(MailManLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void log(int level, String msg) {
        
        
        Calendar calendar = Calendar.getInstance();
        
        String date = String.format("%02d/%02d/%d",calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));
        
        String time = String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        
        String timedMsg = "["+date + "@" + time + "] " + msg;
        
        log.add(new MailManLog(level, timedMsg));
        
        logs.add(new MailManLog(level, timedMsg));
        mailman.getGui().updateLogTextPane();
        
        System.out.println(timedMsg);
        FileWriter fileWriter = null;
        
        try {
            fileWriter = new FileWriter(filename, true);
            fileWriter.write(level + " " + timedMsg + "\n");
        } catch (IOException ex) {
            Logger.getLogger(MailManLogger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(MailManLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    
    public Vector<MailManLog> getLog() {
        return log;
    }
    
    public boolean checkLevel(MailManLog log) {
        switch(log.getLevel()) {
            case 8: if((level & 0x08) > 0)
                return true;
            return false;
            case 4: if((level & 0x04) > 0)
                return true;
            return false;
            case 2: if((level & 0x02) > 0)
                return true;
            return false;
            case 1: if((level & 0x01) > 0)
                return true;
            default: return false;
            
        }
        
    }
    
    public void setLevel(int level) {
        this.level = level;
        mailman.getGui().updateLogTextPane();
    }
    
    private synchronized void readFile() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(MailMan.LOG_FILE);
            BufferedReader buffRead = new BufferedReader(fileReader);
            String line;
            StringTokenizer st;
            while((line = buffRead.readLine()) != null) {
                try {
                    st = new StringTokenizer(line);
                    
                    int logLevel = Integer.parseInt(st.nextToken());
                    String msg = "";
                    while(st.hasMoreTokens()) {
                        msg += st.nextToken() + " ";
                    }
                    
                    logs.add(new MailManLog(logLevel, msg));
                } catch (Exception e) { 
                    continue;  // make this resilient to bad logs...
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MailManLogger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MailManLogger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileReader.close();
            } catch (IOException ex) {
                Logger.getLogger(MailManLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    
    
}
