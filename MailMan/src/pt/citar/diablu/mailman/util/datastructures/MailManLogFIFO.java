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

package pt.citar.diablu.mailman.util.datastructures;

import java.util.LinkedList;


public class MailManLogFIFO {
    
    int size;
    LinkedList<MailManLog> list;
    
    public MailManLogFIFO(int size) {
        this.size = size;
        list = new LinkedList<MailManLog>();
    }
    
    public void add(MailManLog m)
    {
        if(list.size() < size)
            list.add(m);
        else
        {
            list.remove(0);
            list.add(m);
        }
    }

    public LinkedList<MailManLog> getList() {
        return list;
    }
    
    
    
    
    
    
    
    

}
