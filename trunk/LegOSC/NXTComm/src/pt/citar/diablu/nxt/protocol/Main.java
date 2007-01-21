/*
 * Main.java
 *
 * Created on 3 de Dezembro de 2006, 13:00
 *
 *  LegOSC: and OSC gateway to control the Lego Minstorms NXT robots.
 *  This is part a of the DiABlu Project (http://diablu.jorgecardoso.org)
 *
 *  Copyright (C) 2007  Jorge Cardoso
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  You can reach me by email: jorgecardoso <> ieee org
 *
 *
 */

package pt.citar.diablu.nxt.protocol;


import java.util.Enumeration;
import java.io.*;

/**
 *
 * @author Jorge Cardoso
 */
public class Main {
    
   
    /* Stop processing  */
    private boolean stop = false;
    
    private Thread main;
    
    /** Creates a new instance of Main */
    public Main() {
         
        NXTCommBluetoothSerialChannel channel = new NXTCommBluetoothSerialChannel();
        
        try {
            

            channel.openPort("COM11");

            NXTCommandPlayTone playTone = new NXTCommandPlayTone(4000, 500);
            playTone.setResponseRequired(true);
            playTone.setFrequency(2000);
            System.out.println(playTone);
            NXTResponseStatus status = (NXTResponseStatus)channel.sendCommand(playTone);

            boolean c = true;
            NXTCommandSetInputMode sim = new NXTCommandSetInputMode((byte)0, NXTResponseInputValues.LIGHT_INACTIVE_TYPE, NXTResponseInputValues.RAW_MODE);
            channel.sendCommand(sim);
            NXTCommandGetInputValues iv = new NXTCommandGetInputValues((byte)0);
            NXTResponseInputValues riv;
            do {
                    
                riv = (NXTResponseInputValues)channel.sendCommand(iv);
                System.out.println("Valor: " + riv.getNormalizedValue() +  "Snsor type: " + riv.getSensorType());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {

                }
            } while (c);
            channel.closePort();    
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        } catch (gnu.io.PortInUseException piue) {
             System.err.println("PortInUseException: " + piue.getMessage());
        } catch (gnu.io.UnsupportedCommOperationException ucoe) {
             System.err.println("UnsupportedCommOperationException: " + ucoe.getMessage());
        } catch (gnu.io.NoSuchPortException nspe) {
             System.err.println("NoSuchPortException: " + nspe.getMessage());
        }

        
         System.out.println("Done!");
            
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Main();
    }
    
}
