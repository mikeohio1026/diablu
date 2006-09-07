/*
 * DiABluKey.java
 *
 * Created on 12 de Maio de 2006, 15:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
