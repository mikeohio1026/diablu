/*
 * diABluLogHandler.java
 *
 * Created on 13 de Setembro de 2006, 11:30
 *
 * Copyright (C) 13 de Setembro de 2006 Nuno Rodrigues
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

package citar.diablu.server.model.log;

import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.logging.Handler;
import java.util.logging.Level;

import java.text.DateFormat;
import java.util.Date;

import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*;

import javax.swing.JTextArea;


/**
 *
 * @author Nuno Rodrigues
 */
public class diABluLogHandler  extends Handler {
    
    private JTextArea jta;

    
    /** Creates a new instance of diABluLogHandler */
    public diABluLogHandler(JTextArea jta) {
    
        this.jta = jta;
  
        
    }
    
    public void publish(LogRecord logRecord){
   
        // get the date & time
        String message = "["+DateFormat.getTimeInstance().format(new Date())+"]"; 
        // add the text
        message+=logRecord.getMessage();
        
        // check the text area
        if ( jta.getLineCount() > VIEW_LOG_LINE_MAX ){
        
            jta.setText("");
            
        } 
    
        /**
        // check the level
        if (Logger.getLogger(LOG_MAIN_NAME).getLevel().equals(Level.ALL)){
            
            message += "[Source:"+logRecord.getSourceClassName()+"]"+"[Method:"+logRecord.getSourceMethodName()+"]";
            
        }
        */
        
        jta.append(message+"\n");        

        jta.setCaretPosition(jta.getText().length());
        
        
    }
    
    public void flush(){}
    
    public void close(){}
    
}
