/*
 * DiABluKey.java
 *
 * Created on 12 de Maio de 2006, 15:34
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
public class DiABluKey {
    
    DiABluID id;
    String keyPressed;             
    String gAction;
    
    /** Creates a new instance of DiABluKey */
    public DiABluKey() {
        
        id = null;
        keyPressed = "";
        gAction = "";
    }
    
    public DiABluKey(DiABluID newDBid, String kp, String ga){
        
        id = newDBid;
        keyPressed = kp;
        gAction = ga;
        
    }
    // getters and setters
    
    // Key Pressed
    public String getKeyPressed () {
        
        return this.keyPressed;
    }
    
    public void setKeyPressed( String newKeyPressed) {
        
        this.keyPressed = newKeyPressed;
    }
    
    // Game Action
    public String getGAction() {
        
        return this.gAction;
    }
    
    public void setGAction(String newGAction){
        
        this.gAction = newGAction;
    }
    
    // ID
    public void setID (DiABluID newID){
        
        this.id = newID;
    }
    
    public DiABluID getID(){
        
        return this.id;
    }
    
    public String toString() {
        
        return "K:"+keyPressed+"|G:"+gAction;

    }
    
    
    
    
}
