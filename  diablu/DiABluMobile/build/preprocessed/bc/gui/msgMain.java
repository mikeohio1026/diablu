package bc.gui;
/*
 * HelloMidlet.java
 *
 * Created on 10 juin 2006, 12:03
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.io.*;
import java.util.*;
import javax.bluetooth.*;
import bc.pressedKeys;
import com.bt.dbBTdiscovery;
import com.bt.DiABluService;
import org.netbeans.microedition.lcdui.WaitScreen;
import com.bt.dbBTclient;
//import com.bt.dbBTKeysClient;
import bc.pressedKeys;

/**
 *
 * @author Nuno Rodrigues
 */
public class msgMain extends MIDlet implements CommandListener {
    
    /** Vector of DiABlu Servers found */
    public static Vector servicesList = new Vector();
    
    /** Alert message to be displayed by the ALERT Screen */
    public String ALERT_MESSAGE = "Nothing to report so far;)";
    
    /* This DiABlu Mobile Text Version - used to title screens of the midlet */
    public String DIABLU_TITLE = "DiABlu Mobile 1";
    
    /** Memorizes BACK command pointer */
    public int BACK_COMMAND = 0;
    public int BACK_COMMAND_MSG = 1;
    public int BACK_COMMAND_SEARCH = 2;
    public int BACK_COMMAND_SENDKEYS = 3;
   
    
    /** Creates a new instance of HelloMidlet */
    public msgMain() {
    }
    
    private Command exitCommand;//GEN-BEGIN:MVDFields
    private Form msg;
    private Alert alertDiABlu;
    private Form help;
    private Command sendKeys;
    private Command exitDiABlu;
    private Command exitCommand2;
    private ChoiceGroup choiceDiABlu;
    private Command backCommand1;
    private Command helpKeys;
    private Command backCommand2;
    private Command exitHelp;
    private Command helpMain;
    private Command itemCommand1;
    private Command helpMain2;
    private Spacer spacer1;
    private TextField diabluMsg;
    private Command okCommand1;
    private Command sendMsg;
    private Spacer spacer2;
    private Spacer spacer3;
    private Spacer spacer4;
    private Command backException;
    private Command exceptionFound;
    private Ticker searchDBTicker;
    private Command backAlert;
    private Command exitCommand1;
    private Ticker helpTicker;
    private TextBox logTB;
    private Command exitCommand4;
    private Command backCommand4;
    private Command logCommand;
    private Command searchDBCommand;
    private Command searchLogCommand;
    private Command logKeys;
    private Command logCommand1;
    private Image logoDB;
    private Image alertDB;
    private Command backKeys;
    private bc.pressedKeys keyboardSensor;
    private Command backCommand3;
    private Command helpCommand1;
    private Form diabluSearchForm;
    private Command okCommand2;
    private Command reSearchDS;
    private ImageItem logoInit;
    private Image logoDiABlu;
    private Command okCommand3;
    private Command exitCommand3;//GEN-END:MVDFields
    private Gauge superGauge;
    
//GEN-LINE:MVDMethods

    /** This method initializes UI of the application.//GEN-BEGIN:MVDInitBegin
     */
    private void initialize() {//GEN-END:MVDInitBegin
        // Insert pre-init code here
        getDisplay().setCurrent(get_diabluSearchForm());//GEN-LINE:MVDInitInit
        
        if ( startSearch() ) {
            getDisplay().setCurrent(get_msg());
        } else {
            getDisplay().setCurrent(get_alertDiABlu());
        }
        
       
    }//GEN-LINE:MVDInitEnd
    
    public boolean startSearch() {
     
    BACK_COMMAND = BACK_COMMAND_SEARCH;
    
    log("Creating discovery class");    
    dbBTdiscovery diabluSearch = null;
    
    try {
          diabluSearch = new dbBTdiscovery(get_logTB(),get_superGauge());
                    
    } catch (BluetoothStateException btE) {
        
          ALERT_MESSAGE="Unable to start Bluetooth.Please reboot system";
          BACK_COMMAND = BACK_COMMAND_SEARCH;
          log(ALERT_MESSAGE);
          log("BSE:"+btE.getMessage());
          // TODO: redirect to ALERT FORM
          return false;
                
    }
          
    DiABluService dbService = new DiABluService();
    
    ServiceRecord[] dbServiceRecord;
    String remoteServiceName = "";
    RemoteDevice remoteDevice = null;
    String targetURL ="";
             
    log("[M]:Waiting BT");                 
    Vector sList1 = diabluSearch.findDiablus();
    log("[M]:BT ended");
            
    BACK_COMMAND = BACK_COMMAND_MSG;
                                                  
    if (sList1 == null || sList1.size()==0){
                
       ALERT_MESSAGE = "No DiABlu Servers found";
       log("ALERT!No DiABlu Servers");
       return false;
       // TODO: force to call the Alert
       //getDisplay().setCurrent(get_alertDiABlu());
                
    } else {
                log("Populating:"+sList1.size());
                populateDBList(sList1);                            
    }
  
         
        return true;
        
    }
    
    /** Called by the system to indicate that a command has been invoked on a particular displayable.//GEN-BEGIN:MVDCABegin
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:MVDCABegin
        // Insert global pre-action code here
               
        if (displayable == msg) {//GEN-BEGIN:MVDCABody
            if (command == sendKeys) {//GEN-END:MVDCABody
                // Insert pre-action code here
                getDisplay().setCurrent(get_keyboardSensor());//GEN-LINE:MVDCAAction19
                log("helloooo");
               // getDisplay().setCurrent(get_keyCanvas());
                
                BACK_COMMAND = BACK_COMMAND_MSG;
                log("[keys|");
                String sU = getServerConnectionString();
                log("URL|");
                dbBTclient dS = new dbBTclient(sU,this);
                log("Client|");
                
                //pressedKeys pK = new pressedKeys();
               // getDisplay().setCurrent(keySensor);
                get_keyboardSensor().setClientAndStart(dS);
                log("Display]");
                getDisplay().setCurrent(get_keyboardSensor());
                
                //get_keySensor();
         
                
            } else if (command == exitDiABlu) {//GEN-LINE:MVDCACase19
                // Insert pre-action code here
                exitMIDlet();//GEN-LINE:MVDCAAction28
                // Insert post-action code here
            } else if (command == helpMain) {//GEN-LINE:MVDCACase28
                // Insert pre-action code here
                getDisplay().setCurrent(get_help());//GEN-LINE:MVDCAAction46
                // Insert post-action code here
            } else if (command == sendMsg) {//GEN-LINE:MVDCACase46
                // Insert pre-action code here
                // Do nothing//GEN-LINE:MVDCAAction54
                // Insert post-action code here
                log("MSG:"+diabluMsg.getString());
                sendMsg(diabluMsg.getString());
                log("Msg method called");
                
                       
            } else if (command == logCommand) {//GEN-LINE:MVDCACase54
                // Insert pre-action code here
                getDisplay().setCurrent(get_logTB());//GEN-LINE:MVDCAAction85
                // Insert post-action code here
            } else if (command == searchDBCommand) {//GEN-LINE:MVDCACase85
                // Insert pre-action code here
                getDisplay().setCurrent(get_diabluSearchForm());//GEN-LINE:MVDCAAction87
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase87
        } else if (displayable == help) {
            if (command == backCommand2) {//GEN-END:MVDCACase87
                // Insert pre-action code here
                // Do nothing//GEN-LINE:MVDCAAction36
                // Insert post-action code here
                log("Calling command:"+BACK_COMMAND);
                switch (BACK_COMMAND){
            
                        case 1 : {                            
                                    getDisplay().setCurrent(get_msg());              
                                    break;
                        }
                        case 2 : {
                                    startSearch();
                                    getDisplay().setCurrent(get_diabluSearchForm());
                                    break;
                        }
                        case 3 : {
                                    getDisplay().setCurrent(get_keyboardSensor());
                                    break;
                        }
                }
            }//GEN-BEGIN:MVDCACase36
        } else if (displayable == alertDiABlu) {
            if (command == exitCommand1) {//GEN-END:MVDCACase36
                // Insert pre-action code here
                exitMIDlet();//GEN-LINE:MVDCAAction70
                // Insert post-action code here
            } else if (command == backAlert) {//GEN-LINE:MVDCACase70
                // Insert pre-action code here
                // Do nothing//GEN-LINE:MVDCAAction68
                // Insert post-action code here
                   switch (BACK_COMMAND){
            
                        case 1 : {                            
                                    getDisplay().setCurrent(get_msg());              
                                    break;
                        }
                        case 2 : {
                                    
                                    getDisplay().setCurrent(get_diabluSearchForm());
                                    break;
                        }
                        case 3 : {
                                    getDisplay().setCurrent(get_keyboardSensor());
                                    break;
                        }
                }
            }//GEN-BEGIN:MVDCACase68
        } else if (displayable == logTB) {
            if (command == exitCommand4) {//GEN-END:MVDCACase68
                // Insert pre-action code here
                exitMIDlet();//GEN-LINE:MVDCAAction81
                // Insert post-action code here
            } else if (command == backCommand4) {//GEN-LINE:MVDCACase81
                // Insert pre-action code here
                // Do nothing//GEN-LINE:MVDCAAction83
                // Insert post-action code here
              
                switch (BACK_COMMAND){
            
                        case 1 : {                    
                                    
                                    getDisplay().setCurrent(get_msg());              
                                    break;
                        }
                        case 2 : {
                                    getDisplay().setCurrent(get_diabluSearchForm());
                                    break;
                                    
                        }
                        case 3 : {
                                    getDisplay().setCurrent(get_keyboardSensor());
                                    break;
                                    
                        }
                }
            }//GEN-BEGIN:MVDCACase83
        } else if (displayable == keyboardSensor) {
            if (command == helpCommand1) {//GEN-END:MVDCACase83
             
                // no matter what command, we have to close open conns
                keyboardSensor.cleanUP();
                BACK_COMMAND = BACK_COMMAND_SENDKEYS;
                
                getDisplay().setCurrent(get_help());//GEN-LINE:MVDCAAction133
                // Insert post-action code here
            } else if (command == backCommand3) {//GEN-LINE:MVDCACase133
                // Insert pre-action code here
                keyboardSensor.cleanUP();
                getDisplay().setCurrent(get_msg());//GEN-LINE:MVDCAAction131
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase131
        } else if (displayable == diabluSearchForm) {
            if (command == exitCommand3) {//GEN-END:MVDCACase131
                // Insert pre-action code here
                exitMIDlet();//GEN-LINE:MVDCAAction152
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase152
        }//GEN-END:MVDCACase152
        // Insert global post-action code here
}//GEN-LINE:MVDCAEnd
    
    /**
     * This method should return an instance of the display.
     */
    public Display getDisplay() {//GEN-FIRST:MVDGetDisplay
        return Display.getDisplay(this);
    }//GEN-LAST:MVDGetDisplay
    
    /**
     * This method should exit the midlet.
     */
    public void exitMIDlet() {//GEN-FIRST:MVDExitMidlet
        getDisplay().setCurrent(null);
        destroyApp(true);
        notifyDestroyed();
    }//GEN-LAST:MVDExitMidlet
    
    /** This method returns instance for exitCommand component and should be called instead of accessing exitCommand field directly.//GEN-BEGIN:MVDGetBegin5
     * @return Instance for exitCommand component
     */
    public Command get_exitCommand() {
        if (exitCommand == null) {//GEN-END:MVDGetBegin5
            // Insert pre-init code here
            exitCommand = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit5
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd5
        return exitCommand;
    }//GEN-END:MVDGetEnd5

        
    /** This method returns instance for msg component and should be called instead of accessing msg field directly.//GEN-BEGIN:MVDGetBegin14
     * @return Instance for msg component
     */
    public Form get_msg() {
        if (msg == null) {//GEN-END:MVDGetBegin14

          
            msg = new Form("DiABlu Mobile", new Item[] {//GEN-BEGIN:MVDGetInit14
                get_spacer2(),
                get_choiceDiABlu(),
                get_spacer1(),
                get_spacer3(),
                get_spacer4(),
                get_diabluMsg()
            });
            msg.addCommand(get_sendKeys());
            msg.addCommand(get_exitDiABlu());
            msg.addCommand(get_helpMain());
            msg.addCommand(get_sendMsg());
            msg.addCommand(get_logCommand());
            msg.addCommand(get_searchDBCommand());
            msg.setCommandListener(this);//GEN-END:MVDGetInit14
 
            
        }//GEN-BEGIN:MVDGetEnd14
        return msg;
    }//GEN-END:MVDGetEnd14
       

    /** This method returns instance for alertDiABlu component and should be called instead of accessing alertDiABlu field directly.//GEN-BEGIN:MVDGetBegin16
     * @return Instance for alertDiABlu component
     */
    public Alert get_alertDiABlu() {
        if (alertDiABlu == null) {//GEN-END:MVDGetBegin16
     
            alertDiABlu = new Alert("DiAblu ALERT!", ALERT_MESSAGE, get_alertDB(), AlertType.WARNING);//GEN-BEGIN:MVDGetInit16
            alertDiABlu.addCommand(get_backAlert());
            alertDiABlu.addCommand(get_exitCommand1());
            alertDiABlu.setCommandListener(this);
            alertDiABlu.setTimeout(10);//GEN-END:MVDGetInit16

            
            
        }//GEN-BEGIN:MVDGetEnd16
        return alertDiABlu;
    }//GEN-END:MVDGetEnd16

    /** This method returns instance for help component and should be called instead of accessing help field directly.//GEN-BEGIN:MVDGetBegin17
     * @return Instance for help component
     */
    public Form get_help() {
        if (help == null) {//GEN-END:MVDGetBegin17
    
            help = new Form("DiABlu Mobile Help", new Item[0]);//GEN-BEGIN:MVDGetInit17
            help.addCommand(get_backCommand2());
            help.setCommandListener(this);
            help.setTicker(get_helpTicker());//GEN-END:MVDGetInit17
            
     

            
        }//GEN-BEGIN:MVDGetEnd17
        return help;
    }//GEN-END:MVDGetEnd17

    /** This method returns instance for sendKeys component and should be called instead of accessing sendKeys field directly.//GEN-BEGIN:MVDGetBegin18
     * @return Instance for sendKeys component
     */
    public Command get_sendKeys() {
        if (sendKeys == null) {//GEN-END:MVDGetBegin18
            

            sendKeys = new Command("Send Keys", "Send Keys", Command.SCREEN, 1);//GEN-LINE:MVDGetInit18
            


            
        }//GEN-BEGIN:MVDGetEnd18
        return sendKeys;
    }//GEN-END:MVDGetEnd18

    /** This method returns instance for exitDiABlu component and should be called instead of accessing exitDiABlu field directly.//GEN-BEGIN:MVDGetBegin25
     * @return Instance for exitDiABlu component
     */
    public Command get_exitDiABlu() {
        if (exitDiABlu == null) {//GEN-END:MVDGetBegin25
           
            exitDiABlu = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit25
    
        }//GEN-BEGIN:MVDGetEnd25
        return exitDiABlu;
    }//GEN-END:MVDGetEnd25

    /** This method returns instance for exitCommand2 component and should be called instead of accessing exitCommand2 field directly.//GEN-BEGIN:MVDGetBegin27
     * @return Instance for exitCommand2 component
     */
    public Command get_exitCommand2() {
        if (exitCommand2 == null) {//GEN-END:MVDGetBegin27
        
            
            exitCommand2 = new Command("Exit", "Exits the DiABlu Mobile", Command.EXIT, 1);//GEN-LINE:MVDGetInit27
            
            
        }//GEN-BEGIN:MVDGetEnd27
        return exitCommand2;
    }//GEN-END:MVDGetEnd27

    /** This method returns instance for choiceDiABlu component and should be called instead of accessing choiceDiABlu field directly.//GEN-BEGIN:MVDGetBegin29
     * @return Instance for choiceDiABlu component
     */
    public ChoiceGroup get_choiceDiABlu() {
        if (choiceDiABlu == null) {//GEN-END:MVDGetBegin29
            // Insert pre-init code here
            choiceDiABlu = new ChoiceGroup("Choose DiABlu Server", Choice.POPUP, new String[0], new Image[0]);//GEN-BEGIN:MVDGetInit29
            choiceDiABlu.setSelectedFlags(new boolean[0]);//GEN-END:MVDGetInit29

        }//GEN-BEGIN:MVDGetEnd29
        return choiceDiABlu;
    }//GEN-END:MVDGetEnd29

    

    /** This method returns instance for backCommand1 component and should be called instead of accessing backCommand1 field directly.//GEN-BEGIN:MVDGetBegin31
     * @return Instance for backCommand1 component
     */
    public Command get_backCommand1() {
        if (backCommand1 == null) {//GEN-END:MVDGetBegin31
            
            backCommand1 = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit31
            
        }//GEN-BEGIN:MVDGetEnd31
        return backCommand1;
    }//GEN-END:MVDGetEnd31

    /** This method returns instance for helpKeys component and should be called instead of accessing helpKeys field directly.//GEN-BEGIN:MVDGetBegin33
     * @return Instance for helpKeys component
     */
    public Command get_helpKeys() {
        if (helpKeys == null) {//GEN-END:MVDGetBegin33
    
            helpKeys = new Command("Help", "Do you need some assistance ?", Command.HELP, 1);//GEN-LINE:MVDGetInit33
        
        }//GEN-BEGIN:MVDGetEnd33
        return helpKeys;
    }//GEN-END:MVDGetEnd33

    /** This method returns instance for backCommand2 component and should be called instead of accessing backCommand2 field directly.//GEN-BEGIN:MVDGetBegin35
     * @return Instance for backCommand2 component
     */
    public Command get_backCommand2() {
        if (backCommand2 == null) {//GEN-END:MVDGetBegin35
            // Insert pre-init code here
            backCommand2 = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit35
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd35
        return backCommand2;
    }//GEN-END:MVDGetEnd35

    /** This method returns instance for exitHelp component and should be called instead of accessing exitHelp field directly.//GEN-BEGIN:MVDGetBegin38
     * @return Instance for exitHelp component
     */
    public Command get_exitHelp() {
        if (exitHelp == null) {//GEN-END:MVDGetBegin38
            // Insert pre-init code here
            exitHelp = new Command(" Exit DiABlu", Command.EXIT, 1);//GEN-LINE:MVDGetInit38
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd38
        return exitHelp;
    }//GEN-END:MVDGetEnd38

    /** This method returns instance for helpMain component and should be called instead of accessing helpMain field directly.//GEN-BEGIN:MVDGetBegin40
     * @return Instance for helpMain component
     */
    public Command get_helpMain() {
        if (helpMain == null) {//GEN-END:MVDGetBegin40
            // Insert pre-init code here
            helpMain = new Command("Help", Command.HELP, 1);//GEN-LINE:MVDGetInit40
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd40
        return helpMain;
    }//GEN-END:MVDGetEnd40

    /** This method returns instance for itemCommand1 component and should be called instead of accessing itemCommand1 field directly.//GEN-BEGIN:MVDGetBegin42
     * @return Instance for itemCommand1 component
     */
    public Command get_itemCommand1() {
        if (itemCommand1 == null) {//GEN-END:MVDGetBegin42
            // Insert pre-init code here
            itemCommand1 = new Command("Item", Command.ITEM, 1);//GEN-LINE:MVDGetInit42
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd42
        return itemCommand1;
    }//GEN-END:MVDGetEnd42

    /** This method returns instance for helpMain2 component and should be called instead of accessing helpMain2 field directly.//GEN-BEGIN:MVDGetBegin45
     * @return Instance for helpMain2 component
     */
    public Command get_helpMain2() {
        if (helpMain2 == null) {//GEN-END:MVDGetBegin45
            // Insert pre-init code here
            helpMain2 = new Command("Help", Command.HELP, 1);//GEN-LINE:MVDGetInit45
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd45
        return helpMain2;
    }//GEN-END:MVDGetEnd45

    /** This method returns instance for spacer1 component and should be called instead of accessing spacer1 field directly.//GEN-BEGIN:MVDGetBegin47
     * @return Instance for spacer1 component
     */
    public Spacer get_spacer1() {
        if (spacer1 == null) {//GEN-END:MVDGetBegin47
            // Insert pre-init code here
            spacer1 = new Spacer(1000, 1);//GEN-LINE:MVDGetInit47
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd47
        return spacer1;
    }//GEN-END:MVDGetEnd47

    /** This method returns instance for diabluMsg component and should be called instead of accessing diabluMsg field directly.//GEN-BEGIN:MVDGetBegin48
     * @return Instance for diabluMsg component
     */
    public TextField get_diabluMsg() {
        if (diabluMsg == null) {//GEN-END:MVDGetBegin48
            // Insert pre-init code here
            diabluMsg = new TextField("Type your message", null, 120, TextField.ANY);//GEN-LINE:MVDGetInit48
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd48
        return diabluMsg;
    }//GEN-END:MVDGetEnd48
 

    /** This method returns instance for okCommand1 component and should be called instead of accessing okCommand1 field directly.//GEN-BEGIN:MVDGetBegin51
     * @return Instance for okCommand1 component
     */
    public Command get_okCommand1() {
        if (okCommand1 == null) {//GEN-END:MVDGetBegin51
            // Insert pre-init code here
            okCommand1 = new Command("Command", Command.OK, 1);//GEN-LINE:MVDGetInit51
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd51
        return okCommand1;
    }//GEN-END:MVDGetEnd51

    /** This method returns instance for sendMsg component and should be called instead of accessing sendMsg field directly.//GEN-BEGIN:MVDGetBegin53
     * @return Instance for sendMsg component
     */
    public Command get_sendMsg() {
        if (sendMsg == null) {//GEN-END:MVDGetBegin53
            // Insert pre-init code here
            sendMsg = new Command("Message", "Send Message", Command.SCREEN, 1);//GEN-LINE:MVDGetInit53
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd53
        return sendMsg;
    }//GEN-END:MVDGetEnd53
 
    /** This method returns instance for spacer2 component and should be called instead of accessing spacer2 field directly.//GEN-BEGIN:MVDGetBegin58
     * @return Instance for spacer2 component
     */
    public Spacer get_spacer2() {
        if (spacer2 == null) {//GEN-END:MVDGetBegin58
            // Insert pre-init code here
            spacer2 = new Spacer(1000, 1);//GEN-LINE:MVDGetInit58
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd58
        return spacer2;
    }//GEN-END:MVDGetEnd58

    /** This method returns instance for spacer3 component and should be called instead of accessing spacer3 field directly.//GEN-BEGIN:MVDGetBegin59
     * @return Instance for spacer3 component
     */
    public Spacer get_spacer3() {
        if (spacer3 == null) {//GEN-END:MVDGetBegin59
            // Insert pre-init code here
            spacer3 = new Spacer(1000, 1);//GEN-LINE:MVDGetInit59
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd59
        return spacer3;
    }//GEN-END:MVDGetEnd59

    /** This method returns instance for spacer4 component and should be called instead of accessing spacer4 field directly.//GEN-BEGIN:MVDGetBegin60
     * @return Instance for spacer4 component
     */
    public Spacer get_spacer4() {
        if (spacer4 == null) {//GEN-END:MVDGetBegin60
            // Insert pre-init code here
            spacer4 = new Spacer(1000, 1);//GEN-LINE:MVDGetInit60
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd60
        return spacer4;
    }//GEN-END:MVDGetEnd60
    /** This method returns instance for backException component and should be called instead of accessing backException field directly.//GEN-BEGIN:MVDGetBegin62
     * @return Instance for backException component
     */
    public Command get_backException() {
        if (backException == null) {//GEN-END:MVDGetBegin62
            // Insert pre-init code here
            backException = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit62
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd62
        return backException;
    }//GEN-END:MVDGetEnd62

    /** This method returns instance for exceptionFound component and should be called instead of accessing exceptionFound field directly.//GEN-BEGIN:MVDGetBegin64
     * @return Instance for exceptionFound component
     */
    public Command get_exceptionFound() {
        if (exceptionFound == null) {//GEN-END:MVDGetBegin64
            // Insert pre-init code here
            exceptionFound = new Command("Screen", Command.SCREEN, 1);//GEN-LINE:MVDGetInit64
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd64
        return exceptionFound;
    }//GEN-END:MVDGetEnd64

    /** This method returns instance for searchDBTicker component and should be called instead of accessing searchDBTicker field directly.//GEN-BEGIN:MVDGetBegin66
     * @return Instance for searchDBTicker component
     */
    public Ticker get_searchDBTicker() {
        if (searchDBTicker == null) {//GEN-END:MVDGetBegin66
            // Insert pre-init code here
            searchDBTicker = new Ticker("Searching DiABlu Servers...");//GEN-LINE:MVDGetInit66
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd66
        return searchDBTicker;
    }//GEN-END:MVDGetEnd66

    /** This method returns instance for backAlert component and should be called instead of accessing backAlert field directly.//GEN-BEGIN:MVDGetBegin67
     * @return Instance for backAlert component
     */
    public Command get_backAlert() {
        if (backAlert == null) {//GEN-END:MVDGetBegin67
            // Insert pre-init code here
            backAlert = new Command("Back", "", Command.BACK, 1);//GEN-LINE:MVDGetInit67
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd67
        return backAlert;
    }//GEN-END:MVDGetEnd67

    /** This method returns instance for exitCommand1 component and should be called instead of accessing exitCommand1 field directly.//GEN-BEGIN:MVDGetBegin69
     * @return Instance for exitCommand1 component
     */
    public Command get_exitCommand1() {
        if (exitCommand1 == null) {//GEN-END:MVDGetBegin69
            // Insert pre-init code here
            exitCommand1 = new Command("Exit DiABlu", Command.EXIT, 1);//GEN-LINE:MVDGetInit69
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd69
        return exitCommand1;
    }//GEN-END:MVDGetEnd69

    /** This method returns instance for helpTicker component and should be called instead of accessing helpTicker field directly.//GEN-BEGIN:MVDGetBegin71
     * @return Instance for helpTicker component
     */
    public Ticker get_helpTicker() {
        if (helpTicker == null) {//GEN-END:MVDGetBegin71
            // Insert pre-init code here
            helpTicker = new Ticker("For more information, please visit http://soundserver.porto.ucp.pt/diablu");//GEN-LINE:MVDGetInit71
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd71
        return helpTicker;
    }//GEN-END:MVDGetEnd71
 
  
    /** This method returns instance for logTB component and should be called instead of accessing logTB field directly.//GEN-BEGIN:MVDGetBegin79
     * @return Instance for logTB component
     */
    public TextBox get_logTB() {
        if (logTB == null) {//GEN-END:MVDGetBegin79
            // Insert pre-init code here
            logTB = new TextBox("DiABlu Log", "", 600, TextField.ANY);//GEN-BEGIN:MVDGetInit79
            logTB.addCommand(get_exitCommand4());
            logTB.addCommand(get_backCommand4());
            logTB.setCommandListener(this);//GEN-END:MVDGetInit79
            // Insert post-init code here
            int maxSize = logTB.getMaxSize();
            logTB.setMaxSize(maxSize);
        }//GEN-BEGIN:MVDGetEnd79
        return logTB;
    }//GEN-END:MVDGetEnd79

   
    
    /** This method returns instance for exitCommand4 component and should be called instead of accessing exitCommand4 field directly.//GEN-BEGIN:MVDGetBegin80
     * @return Instance for exitCommand4 component
     */
    public Command get_exitCommand4() {
        if (exitCommand4 == null) {//GEN-END:MVDGetBegin80
            // Insert pre-init code here
            exitCommand4 = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit80
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd80
        return exitCommand4;
    }//GEN-END:MVDGetEnd80

    /** This method returns instance for backCommand4 component and should be called instead of accessing backCommand4 field directly.//GEN-BEGIN:MVDGetBegin82
     * @return Instance for backCommand4 component
     */
    public Command get_backCommand4() {
        if (backCommand4 == null) {//GEN-END:MVDGetBegin82
            // Insert pre-init code here
            backCommand4 = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit82
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd82
        return backCommand4;
    }//GEN-END:MVDGetEnd82

    /** This method returns instance for logCommand component and should be called instead of accessing logCommand field directly.//GEN-BEGIN:MVDGetBegin84
     * @return Instance for logCommand component
     */
    public Command get_logCommand() {
        if (logCommand == null) {//GEN-END:MVDGetBegin84
            // Insert pre-init code here
            logCommand = new Command("Log", Command.SCREEN, 1);//GEN-LINE:MVDGetInit84
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd84
        return logCommand;
    }//GEN-END:MVDGetEnd84

    /** This method returns instance for searchDBCommand component and should be called instead of accessing searchDBCommand field directly.//GEN-BEGIN:MVDGetBegin86
     * @return Instance for searchDBCommand component
     */
    public Command get_searchDBCommand() {
        if (searchDBCommand == null) {//GEN-END:MVDGetBegin86
            // Insert pre-init code here
            searchDBCommand = new Command("Search Again", Command.SCREEN, 1);//GEN-LINE:MVDGetInit86
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd86
        return searchDBCommand;
    }//GEN-END:MVDGetEnd86
  
    /** This method returns instance for searchLogCommand component and should be called instead of accessing searchLogCommand field directly.//GEN-BEGIN:MVDGetBegin92
     * @return Instance for searchLogCommand component
     */
    public Command get_searchLogCommand() {
        if (searchLogCommand == null) {//GEN-END:MVDGetBegin92
          // Insert pre-init code here
            searchLogCommand = new Command("Log", Command.SCREEN, 1);//GEN-LINE:MVDGetInit92
          // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd92
        return searchLogCommand;
    }//GEN-END:MVDGetEnd92
    /** This method returns instance for logKeys component and should be called instead of accessing logKeys field directly.//GEN-BEGIN:MVDGetBegin97
     * @return Instance for logKeys component
     */
    public Command get_logKeys() {
        if (logKeys == null) {//GEN-END:MVDGetBegin97
          // Insert pre-init code here
            logKeys = new Command("Log", Command.SCREEN, 1);//GEN-LINE:MVDGetInit97
          // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd97
        return logKeys;
    }//GEN-END:MVDGetEnd97
 
    /** This method returns instance for logCommand1 component and should be called instead of accessing logCommand1 field directly.//GEN-BEGIN:MVDGetBegin108
     * @return Instance for logCommand1 component
     */
    public Command get_logCommand1() {
        if (logCommand1 == null) {//GEN-END:MVDGetBegin108
          // Insert pre-init code here
            logCommand1 = new Command("Screen", Command.SCREEN, 1);//GEN-LINE:MVDGetInit108
          // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd108
        return logCommand1;
    }//GEN-END:MVDGetEnd108

    /** This method returns instance for logoDB component and should be called instead of accessing logoDB field directly.//GEN-BEGIN:MVDGetBegin111
     * @return Instance for logoDB component
     */
    public Image get_logoDB() {
        if (logoDB == null) {//GEN-END:MVDGetBegin111
            // Insert pre-init code here
            try {//GEN-BEGIN:MVDGetInit111
                logoDB = Image.createImage("/res/diablumobileWAIT00.png");
            } catch (java.io.IOException exception) {
            }//GEN-END:MVDGetInit111
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd111
        return logoDB;
    }//GEN-END:MVDGetEnd111

    /** This method returns instance for alertDB component and should be called instead of accessing alertDB field directly.//GEN-BEGIN:MVDGetBegin113
     * @return Instance for alertDB component
     */
    public Image get_alertDB() {
        if (alertDB == null) {//GEN-END:MVDGetBegin113
            // Insert pre-init code here
            try {//GEN-BEGIN:MVDGetInit113
                alertDB = Image.createImage("/res/dmALERT.png");
            } catch (java.io.IOException exception) {
            }//GEN-END:MVDGetInit113
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd113
        return alertDB;
    }//GEN-END:MVDGetEnd113
    /** This method returns instance for backKeys component and should be called instead of accessing backKeys field directly.//GEN-BEGIN:MVDGetBegin127
     * @return Instance for backKeys component
     */
    public Command get_backKeys() {
        if (backKeys == null) {//GEN-END:MVDGetBegin127
            // Insert pre-init code here
            backKeys = new Command("BACK", "Return to previous screen", Command.BACK, 1);//GEN-LINE:MVDGetInit127
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd127
        return backKeys;
    }//GEN-END:MVDGetEnd127

    /** This method returns instance for keyboardSensor component and should be called instead of accessing keyboardSensor field directly.//GEN-BEGIN:MVDGetBegin129
     * @return Instance for keyboardSensor component
     */
    public bc.pressedKeys get_keyboardSensor() {
        if (keyboardSensor == null) {//GEN-END:MVDGetBegin129
             // Insert pre-init code here
            keyboardSensor = new bc.pressedKeys();//GEN-BEGIN:MVDGetInit129
            keyboardSensor.addCommand(get_backCommand3());
            keyboardSensor.addCommand(get_helpCommand1());
            keyboardSensor.setCommandListener(this);
            keyboardSensor.setTitle("DiABlu Mobile Key Sensor");//GEN-END:MVDGetInit129
             // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd129
        return keyboardSensor;
    }//GEN-END:MVDGetEnd129

    /** This method returns instance for backCommand3 component and should be called instead of accessing backCommand3 field directly.//GEN-BEGIN:MVDGetBegin130
     * @return Instance for backCommand3 component
     */
    public Command get_backCommand3() {
        if (backCommand3 == null) {//GEN-END:MVDGetBegin130
             // Insert pre-init code here
            backCommand3 = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit130
             // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd130
        return backCommand3;
    }//GEN-END:MVDGetEnd130

    /** This method returns instance for helpCommand1 component and should be called instead of accessing helpCommand1 field directly.//GEN-BEGIN:MVDGetBegin132
     * @return Instance for helpCommand1 component
     */
    public Command get_helpCommand1() {
        if (helpCommand1 == null) {//GEN-END:MVDGetBegin132
             // Insert pre-init code here
            helpCommand1 = new Command("Help", Command.HELP, 1);//GEN-LINE:MVDGetInit132
             // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd132
        return helpCommand1;
    }//GEN-END:MVDGetEnd132
 
    /** This method returns instance for diabluSearchForm component and should be called instead of accessing diabluSearchForm field directly.//GEN-BEGIN:MVDGetBegin137
     * @return Instance for diabluSearchForm component
     */
    public Form get_diabluSearchForm() {
        if (diabluSearchForm == null) {//GEN-END:MVDGetBegin137
            // Insert pre-init code here
            diabluSearchForm = new Form(DIABLU_TITLE, new Item[] {get_logoInit()});//GEN-BEGIN:MVDGetInit137
            diabluSearchForm.addCommand(get_exitCommand3());
            diabluSearchForm.setCommandListener(this);//GEN-END:MVDGetInit137
            
            // Insert post-init code here
            diabluSearchForm.append(get_superGauge());
        }//GEN-BEGIN:MVDGetEnd137
        return diabluSearchForm;
    }//GEN-END:MVDGetEnd137

    /** This method returns instance for okCommand2 component and should be called instead of accessing okCommand2 field directly.//GEN-BEGIN:MVDGetBegin138
     * @return Instance for okCommand2 component
     */
    public Command get_okCommand2() {
        if (okCommand2 == null) {//GEN-END:MVDGetBegin138
            // Insert pre-init code here
            okCommand2 = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit138
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd138
        return okCommand2;
    }//GEN-END:MVDGetEnd138

    /** This method returns instance for reSearchDS component and should be called instead of accessing reSearchDS field directly.//GEN-BEGIN:MVDGetBegin140
     * @return Instance for reSearchDS component
     */
    public Command get_reSearchDS() {
        if (reSearchDS == null) {//GEN-END:MVDGetBegin140
            // Insert pre-init code here
            reSearchDS = new Command("ReSearch", "Search Again", Command.ITEM, 1);//GEN-LINE:MVDGetInit140
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd140
        return reSearchDS;
    }//GEN-END:MVDGetEnd140
 
    public Gauge get_superGauge() {
      
        if (superGauge == null) {
            superGauge = new Gauge ("Searching DiABlu Servers",false,Gauge.INDEFINITE,Gauge.CONTINUOUS_RUNNING);
            superGauge.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_BOTTOM | Item.LAYOUT_NEWLINE_BEFORE | Item.LAYOUT_SHRINK | Item.LAYOUT_VSHRINK | Item.LAYOUT_EXPAND | Item.LAYOUT_VEXPAND);
        }
        
        return superGauge;
    }
    /** This method returns instance for logoInit component and should be called instead of accessing logoInit field directly.//GEN-BEGIN:MVDGetBegin144
     * @return Instance for logoInit component
     */
    public ImageItem get_logoInit() {
        if (logoInit == null) {//GEN-END:MVDGetBegin144
            // Insert pre-init code here
            logoInit = new ImageItem("", get_logoDiABlu(), Item.LAYOUT_CENTER | Item.LAYOUT_VCENTER | Item.LAYOUT_SHRINK | Item.LAYOUT_VSHRINK | Item.LAYOUT_EXPAND | Item.LAYOUT_VEXPAND, "");//GEN-LINE:MVDGetInit144
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd144
        return logoInit;
    }//GEN-END:MVDGetEnd144

    /** This method returns instance for logoDiABlu component and should be called instead of accessing logoDiABlu field directly.//GEN-BEGIN:MVDGetBegin148
     * @return Instance for logoDiABlu component
     */
    public Image get_logoDiABlu() {
        if (logoDiABlu == null) {//GEN-END:MVDGetBegin148
            // Insert pre-init code here
            try {//GEN-BEGIN:MVDGetInit148
                logoDiABlu = Image.createImage("/res/diablumobile.png");
            } catch (java.io.IOException exception) {
            }//GEN-END:MVDGetInit148
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd148
        return logoDiABlu;
    }//GEN-END:MVDGetEnd148

    /** This method returns instance for okCommand3 component and should be called instead of accessing okCommand3 field directly.//GEN-BEGIN:MVDGetBegin149
     * @return Instance for okCommand3 component
     */
    public Command get_okCommand3() {
        if (okCommand3 == null) {//GEN-END:MVDGetBegin149
            // Insert pre-init code here
            okCommand3 = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit149
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd149
        return okCommand3;
    }//GEN-END:MVDGetEnd149

    /** This method returns instance for exitCommand3 component and should be called instead of accessing exitCommand3 field directly.//GEN-BEGIN:MVDGetBegin151
     * @return Instance for exitCommand3 component
     */
    public Command get_exitCommand3() {
        if (exitCommand3 == null) {//GEN-END:MVDGetBegin151
            // Insert pre-init code here
            exitCommand3 = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit151
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd151
        return exitCommand3;
    }//GEN-END:MVDGetEnd151
                
    
    
    public void initKeySensor() {
        
               // Get the server url
                // Start a client to handle connection
                // Start a keyboard sensor to send the data to the client                                                                                
                log("Sensor]");
                
    }
    
    
    
    
    private void sendMsg(String msg){
        log("sendMsg()method called.Message:"+msg);
        String tURL = getServerConnectionString();
        sendMessage(msg,tURL);
    }
    
    public void startApp() {
        initialize();
        log("Starting DiABlu Mobile 1");
    }
    
    public void pauseApp() {
        log("User call");
    }
    
    public void destroyApp(boolean unconditional) {
        
    }
    
    public void logDBMethod(){
        if (getDisplay().getCurrent()!=logTB){
            getDisplay().setCurrent(get_diabluSearchForm());
        }
    }
    
   public void populateDBList(Vector sList){
       String sName = "";
       log("Populating["+sList.size()+"]");
       if (sList!=null && sList.size()!=0){
            for (int i=0;i<sList.size();i++){
                    // populate the service list
                    this.servicesList.addElement(sList.elementAt(i));
                    //
                    sName = sList.elementAt(i).toString();
                    //log("Service:"+sName);
                    // populate the choice list
                   // choiceDiABlu.append(sName,null);
            }    
       } else {
           ALERT_MESSAGE = "Error:Pop EMPTY";
           log(ALERT_MESSAGE);
       
       }
       refreshMain();   
   }
  
      
    public void refreshMain(){
        log("Refreshing...");
        String sName = "Sorry";
          // if we have diablu services around let's add it to the choice list 
            if (servicesList!=null && !servicesList.isEmpty()) {
                
                for (int i=0;i<servicesList.size();i++){
                    
                    // Cast the service record
                    log("Getting the SR");
                    ServiceRecord tempService = (ServiceRecord)servicesList.elementAt(i);
                    // Get the services friendly name
                    log("DataElement");                    
                    DataElement de = tempService.getAttributeValue(0x100);
                    log("Checking");
                    if (de==null) { 
                        sName = "tempService"; 
                        log("null name");
                    }
                    boolean typeOK = false;
                    try {
                            typeOK = (de.getDataType()==DataElement.STRING);
                            log("DataElement:"+de.getDataType());
                    } catch (Exception e){
                        typeOK = false;
                        log("Error cast."+e.getMessage());
                    }
                    
                   
                    if (de!=null && typeOK ){
                        
                        log("DE ok.Getting name");
                        sName = (String)de.getValue();
                    } else {
                        //log("DataElement:"+de.getDataType());
                        RemoteDevice rD = tempService.getHostDevice();
                        try {
                            log("Fname");
                             sName = rD.getFriendlyName(true);
                             
                        } catch (IOException ioe){
                            ALERT_MESSAGE = "IOError trying to get FName";
                            log(ALERT_MESSAGE);
                            sName = "Service"+i;
                        }
                    }
                   
                    // Append it to the list
                    get_choiceDiABlu().append(sName,null);
                    
                }
                
            } else {
            
                if (servicesList==null) {
                      ALERT_MESSAGE = "Null ServiceList";
                      log(ALERT_MESSAGE);
                    
            
                    
                } else {
                    ALERT_MESSAGE = "Empty ServiceList";
                    log(ALERT_MESSAGE);
                    
                    
                }
                
                get_choiceDiABlu().append("No DiABlu Services found",null);
            }
        
    }
      
   public void sendMessage(String msgT,String tgt){
                
                // get the connection URL     
                String targetURL = tgt;
                log("MT:"+targetURL);
                log("MC:"+msgT);
                
                // create a new client
                
                dbBTclient dbClient = new dbBTclient(targetURL,this);
                
                // deliver the msg to the client                
                dbClient.sendMsg(msgT);
       }
    
       public String getServerConnectionString() {
        
        // get the selected index
        int choosenIndex = get_choiceDiABlu().getSelectedIndex();
        log("index:"+choosenIndex);
        
        ServiceRecord dbS = (ServiceRecord) servicesList.elementAt(choosenIndex);
        String tempURL = dbS.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        log("ServerConn:"+tempURL);
        // return the connection String
        return tempURL;
        
    }

   
   /**
     * This method returns the string entered in the msgTF textfield(used for msg).
     */
    public String getSendMsg() {
        
        return get_diabluMsg().getString();
        
    }

      /** This method returns an updated instance of the main screen */
  public Form getMsg(){
      
      
      // First get the current one
      Form tempMsg = get_msg();
      refreshMain();
   
      return get_msg();
  }  
    
    
    public void log(int priority,String log){
      // TODO:Priority log  
        log(log);
    }
    
    
    
    public void log(String Slog){

        TextBox tb = get_logTB(); 
        int pos = tb.getMaxSize();
        if (tb.size()+Slog.length()>=pos){get_logTB().setString("");}
        tb.insert(Slog, get_logTB().size());
        
    }
}
