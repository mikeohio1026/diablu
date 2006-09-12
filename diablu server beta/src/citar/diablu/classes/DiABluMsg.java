/*
 * DiABluMsg.java
 *
 * Created on 11 de Maio de 2006, 14:37
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

package citar.diablu.classes;


/**
 *
 * @author nrodrigues
 */
public class DiABluMsg {
    
    DiABluID id;            // ID of the Sender
    String text;            // Text message
    
    /**
     * Creates a new instance of DiABluMsg
     */
    public DiABluMsg() {

        id = null;
        text = "";
       
    }   
    
    public DiABluMsg(DiABluID newID, String newMsg){
        
        id = newID;
        text = newMsg;
        
    }
    
    
    // Getters & Setters
    
    // Text
    public void setText(String newText){
        
        this.text = newText;
    }
   
    // DEPRECATED see toString()
    public String getText(){
        
        return this.text;
    }

    // returns a text representation of this class
    public String toString() {
        
        return this.text;
    }
    
    // ID
    public void setID (DiABluID newID){
        
        this.id = newID;
    }
    
    public DiABluID getID(){
        
        return this.id;
    }
    
   
}
