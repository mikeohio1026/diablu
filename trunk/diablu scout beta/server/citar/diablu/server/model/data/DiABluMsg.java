/*
 * DiABluMsg.java
 *
 * Created on 11 de Maio de 2006, 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.data;


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
        text = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("");
       
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
