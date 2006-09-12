/*
 * PressedKeys.java
 *
 * Created on 13 juin 2006, 09:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bc;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.*;
import java.io.*;
import javax.bluetooth.*;
import java.lang.*;
import com.bt.dbBTclient;

/**
 *
 * @author Nuno Rodrigues
 */
public class pressedKeys extends Canvas {
    
    	/* As dimensões do canvas	*/
	int altura, largura;

	/* A imagem */
	Image imgFundo;
        
        /* GUI */
        public dbBTclient parent;
         
         /* Check the engine state */
         private boolean isSensorReady = false;
              
    
    /** Creates a new instance of PressedKeys */
    public pressedKeys()  {                
                        
                altura = getHeight();
		largura = getWidth();                
                parent = null;
                
                // Everything ok we may proceed
                try {
			imgFundo = Image.createImage("/res/diablumobile.png");
                        
		} catch (IOException ioe) {
                    
			log(3,"ImageError:"+ioe.getMessage());
                        
		}
      
                /* determinar se o dispositivo gera "repeat events"	*/
		if (hasRepeatEvents()) {
			log(0,"Canvas has repeat events.");
		} else {
			log(0,"Canvas doesn't have repeat events.");
		}                

    }
    
    public void setClientAndStart(dbBTclient parentClient) {
        
        this.parent = parentClient;        
                
        log(0,"Key sensor target activated");
                
        // Let's try to open the server connection'
        int ds = parent.openServer();
                
        // Check the server and open the streams
        if (ds == -1 ){ 
                    
            // Open Server error
            log(2,"Error open SERVER");
            return; 
                    
         } else {
                    
            // let's open the streams'
            ds = parent.openStreams();
                    
            if ( ds == -1 ){
            // error opening streams
                log(2,"Error open ST");
                 return;
             } 
          }
        
         isSensorReady = true;
        
        
    }
    
    public dbBTclient getBTClient(){
        
        return parent;
    
    }
        
    
    public void log(int priority, String m){
        
        if ( parent!=null ) {
            parent.log(priority, "[PK]:"+m);
        }
        
    }
    
    
    public void paint(Graphics g) {
		/* limpar o ecrã		*/
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, largura, altura);

		/* desenhar a imagem na posição determinada por imgX e imgY	*/
		g.drawImage(imgFundo, largura/2, altura/2, Graphics.HCENTER|Graphics.VCENTER);

	}
    
   /**
    * Invocado quando o utilizador pressiona uma tecla.
    */
   public void keyPressed(int keyCode) {

	log(0,"Key:"+keyCode);   
        log(0,"Game Action:" + getGameAction(keyCode));
        String keyMsg="K"+keyCode+"|"+getGameAction(keyCode);
        if (isSensorReady) parent.sendKeys(keyMsg);
		
   }
        
        
   /**
    * Invocado quando o utilizador larga uma tecla.
    */
   public void keyReleased(int keyCode) {

		/* Escrever na consola o nome da tecla		*/
		log(0,"Released: " + getKeyName(keyCode));
               
  }
        
        
	/**
	 * Invocado quando o utilizador pressiona continuamente uma tecla
	 * se o dispositivo suportar este tipo de eventos.
	 */
   public void keyRepeated(int keyCode) {

		/* fazer o mesmo que no keyPressed			*/
	     log(0,"Rep key code:"+keyCode);
            // parent.sendKeys(keyCode);
   }
      
   public void cleanUP() {
       
       parent.closeStreams();
       parent.closeServer();
       
   }
        

}
