/*
 * INWatcher.java
 *
 * Created on 11 de Maio de 2006, 14:19
 * 
 * Copyright (C) 2006 CITAR
 * This is part of the DiABlu Project
 * http://diablu.jorgecardoso.org
 * Created by Jorge Cardoso(jccardoso@porto.ucp.pt) & Nuno Rodrigues(nunoalexandre.rodrigues@gmail.com)
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

package citar.diablu.com.interfaces;

import citar.diablu.classes.DiABluMsg;
import citar.diablu.classes.DiABluKey;
import java.util.Vector;

/**
 *
 * @author nrodrigues
 */
public interface INWatcher {
    
    public void newDeviceList (Vector nDeviceList, int type);
    
    public void newMsg (DiABluMsg newMsg);
    
    public void newKey (DiABluKey newKey);
    
    public void newLog (int priority,String log);
    
   // DEPRECATED public void newLog (String log);
    
    public int getBTdelay();


    
}
