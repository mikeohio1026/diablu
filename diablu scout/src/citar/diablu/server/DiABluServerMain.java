/*
 * DiABluServerMain.java
 *
 * Created on 13 de Agosto de 2006, 19:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server;

// j2se
import java.util.ResourceBundle;
import java.util.Locale;

// model
import citar.diablu.server.model.DiABluServerModel;

// model settings
import citar.diablu.server.model.settings.DiABluServerSettings;

// view
import citar.diablu.server.view.main.DiABluServerView;

// controller
import citar.diablu.server.controller.out.view.DiABluServerViewModelListener;


/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerMain {
    
    DiABluServerModel model;
    DiABluServerView  view;
    DiABluServerSettings settings;
    String commandParameteres[];
           
    /** Creates a new instance of DiABluServerMain */
    public DiABluServerMain(String arg[]) {                        
       
        startDiABluServer(arg);
       
    }
    
   /**
    * @param args the command line arguments
    *
    *  p - port
    *  a - address
    *  sn - service name
    *  sd - service description
    *  sim - simulator
    *  sf - settings file
    *
    */
    
    public static void main(String args[]) {           
                 
        new DiABluServerMain(args);
            
    }
    
    public void startDiABluServer(String args[]){
        
   
        // Create the model
        model = new DiABluServerModel(args);
        
        System.out.println("Loading settings...");
        
        // Initialize model settings
        
        //settings = new DiABluServerSettings();
        //System.out.println("Settings loaded...");
        
        //settings.updateModel(model);
        
        // Process the command line arguments if any
        if ( args.length > 0 ) { 
            
            System.out.println("Processing args...");
            //settings.parseArgs(args); 
        }
        
        System.out.println("Starting DiABlu System...");
        model.startDiABluSystem();
                
    }

}
