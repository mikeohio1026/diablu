/*
 * DiABluServerSettings.java
 *
 * Created on 24 de Agosto de 2006, 16:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.settings;

// IO
import java.io.File;

// I18N & L9N
import java.util.Locale;
import java.util.ResourceBundle;

// XML
import org.w3c.dom.Document;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

// DEFAULT SETTINGS
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*; 
        
// MODEL
import citar.diablu.server.model.DiABluServerModel;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerSettings {
    
    // DiABlu Model
    //private DiABluServerViewControllerListener model;
    
    // Settings variables
    private String serviceName=DEFAULT_SERVICE_NAME;
    private String serviceDescription=DEFAULT_SERVICE_DESCRIPTION;
    private int btDelay=DEFAULT_BT_DELAY;
    private String targetAddress=DEFAULT_TARGET_ADDRESS;
    private String targetPort=DEFAULT_TARGET_PORT;
    private String country=DEFAULT_COUNTRY;
    private String language=DEFAULT_LANGUAGE;
    private Locale location;
    private ResourceBundle diabluBundle;
    private String bundlePath=DEFAULT_BUNDLE_PATH;
    private boolean isSimulatorAuto=DEFAULT_SIMULATOR;
    private int logDetail=DEFAULT_LOG_DETAIL;
    private String commandParameters[];
    
    // Contants
    
    
    
    // Parameters
    private String COMMAND_SERVICE_NAME=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Name");
    private String COMMAND_SERVICE_DESCRIPTION=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Description");
    private String COMMAND_BT_DELAY=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("BTDelay");
    private String COMMAND_TARGET_ADDRESS=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Address");
    private String COMMAND_TARGET_PORT=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Port");
    private String COMMAND_COUNTRY=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Country");
    private String COMMAND_LANGUAGE=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Language");
    private String COMMAND_RESOURCE_BUNDLE=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Bundle");
    private String COMMAND_LOG_DETAIL=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Log");
    private String COMMAND_HELP=java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Help");
    
    // Log
    private String PARAMETER_SIMPLE_LOG = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Simple");
    private String PARAMETER_DETAILED_LOG = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Detailed");
    private String PARAMETER_DEBUG_LOG = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Debug");
    
    /**
     * Creates a new instance of DiABluServerSettings
     */
    public DiABluServerSettings(String args[]) {
                
        System.out.println("Loadind default settings");
        defaultSettings();
        System.out.println("Loading file settings");
        loadSettings();
        
        if (args.length > 1) {
            
            System.out.println("Parsing command line parameters...");
            parseArgs(args);
            
        }
          
    } 

    private void checkArgs(String arg[]){
        
    }
    
    private void log(int priority,String msg){
    
        System.out.println("DiABluSettings:"+priority+"Logging:"+msg);
        //model.log(priority,msg);
        
    }

    /**
     * This method loads the data from the XML settings file diabluSettings.xml
     *
     */
    public void loadSettings(){
        
      // Get the data
      try {
            
            // open the file
            log(4,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Opening_settings_file"));
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("src/citar/diablu/server/model/settings/diABluServerViewSettings.xml"));
         
            // normalize text representation
            doc.getDocumentElement ().normalize ();
            log(6,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Root_element_of_the_doc_is_") + doc.getDocumentElement().getNodeName());

            /** Global
             *  
             *  Extract the global settings  
             *
             *  XML - example code
             *  <global>
             *           <location>Portugal</location>                                            // Location
             *           <language>English</language>                                             // Prefered language
             *           <bundle_path>citar/diablu/server/model/i18n/defaultBundle</bundle_path>  // Path to ResourceBundle
             *
             *  
             *
             */
            NodeList globalList = doc.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("global"));
            Node globalNode = globalList.item(0);            
            Element globalElement = (Element) globalList.item(0);
            
            
            //Location
            NodeList locationGlobalList = globalElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("location"));
            Element locationElement = (Element) locationGlobalList.item(0);
            NodeList locationElementList = locationElement.getChildNodes();
            this.country = locationElementList.item(0).getNodeValue().trim();
            
            //Language
            NodeList languageGlobalList = globalElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("language"));
            Element languageElement = (Element) languageGlobalList.item(0);
            NodeList languageElementList = languageElement.getChildNodes();
            this.language = languageElementList.item(0).getNodeValue().trim();           
            
            //Bundle Path
            NodeList bundleGlobalList = globalElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("bundle_path"));
            Element bundleElement = (Element) bundleGlobalList.item(0);
            NodeList bundleElementList = bundleElement.getChildNodes();
            this.bundlePath = languageElementList.item(0).getNodeValue().trim();           
            
            /** Input
             *  
             *  Extract the input settings  
             *
             *  XML - example code
             *  
             *   <input>
             *      <protocol>Bluetooth</protocol>
             *       <delay>10000</delay>
             *       <service_name>DiABlu Service 1</service_name>
             *       <service_description>This service allows you to send keys and messages to this installation</service_description>
             *   </input>
             *
             */
                        
            NodeList inputList = doc.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("input"));
            Node inputNode = inputList.item(0);            
            Element inputElement = (Element) inputList.item(0);
            
            
            // protocol
            // TODO:in next version.for now we don't need this
            
            // delay
            NodeList delayInputList = inputElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("delay"));
            Element delayElement = (Element) delayInputList.item(0);
            NodeList delayElementList = delayElement.getChildNodes();
            String delay = delayElementList.item(0).getNodeValue().trim();
            this.btDelay = Integer.parseInt(delay);
           
            
            // service name
            NodeList service_nameInputList = inputElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("service_name"));
            Element service_nameElement = (Element) service_nameInputList.item(0);
            NodeList service_nameElementList = service_nameElement.getChildNodes();
            this.serviceName = service_nameElementList.item(0).getNodeValue().trim();  
            
            // service description
            NodeList service_descriptionInputList = inputElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("service_description"));
            Element service_descriptionElement = (Element) service_descriptionInputList.item(0);
            NodeList service_descriptionElementList = service_descriptionElement.getChildNodes();
            this.serviceDescription = service_descriptionElementList.item(0).getNodeValue().trim();       
                                    
            
            /** Output
             *  
             *  Extract the output settings  
             *
             *  XML - example code
             *  
             *       <output>
             *           <protocol>OSC</protocol>
             *           <target_address>127.0.0.1</target_address>
             *           <port>10000</port>
             *       </output>
             *
             */                      
            NodeList outputList = doc.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("output"));            
            Element outputElement = (Element) outputList.item(0);
            
            // protocol
            // TODO:in next version.For now we don't need this
            
            // target address
            NodeList target_addressOutputList = outputElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("target_address"));
            Element target_addressElement = (Element) target_addressOutputList.item(0);
            NodeList target_addressElementList = target_addressElement.getChildNodes();
            this.targetAddress = target_addressElementList.item(0).getNodeValue().trim();
           
            
            // port
            NodeList portOutputList = outputElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("port"));
            Element portElement = (Element) portOutputList.item(0);
            NodeList portElementList = portElement.getChildNodes();
            this.targetPort = portElementList.item(0).getNodeValue().trim();
            
            /**
             *  Simulator
             *
             *  XML example code
             *
             *  <simulator>
             *      <automatic>true</automatic>            
             *   </simulator>
             *
             */            
            NodeList simulatorList = doc.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("simulator"));
            Element simulatorElement = (Element) simulatorList.item(0);
            NodeList automaticList = simulatorElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("automatic"));
            Element automaticElement = (Element) automaticList.item(0);
            NodeList automaticElementList = automaticElement.getChildNodes();
            String isAutomatic = automaticElementList.item(0).getNodeValue().trim();
            this.isSimulatorAuto = new Boolean(isAutomatic);
            
            /**
             *  Log
             *
             *  XML example code
             *
             *   <log>
             *       <detail>Simple</detail>
             *       <file>citar/diablu/server/model/log/diABluLog</file>
             *   </log>
             *
             */
            NodeList logList = doc.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("log"));            
            Element logElement = (Element) logList.item(0);
                       
            // detail
            NodeList detailLogList = logElement.getElementsByTagName(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("detail"));
            Element detailElement = (Element) detailLogList.item(0);
            NodeList detailElementList = detailElement.getChildNodes();
            String detail = detailElementList.item(0).getNodeValue().trim();
            this.logDetail = Integer.parseInt(detail);
           
            // file
            // TODO:in next version                                

        } catch (SAXParseException err) {
        
            log (3,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("**_Parsing_error") + java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString(",_line_") + err.getLineNumber () + java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString(",_uri_") + err.getSystemId ());
            log(3,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("_") + err.getMessage ());
            defaultSettings();

        } catch (SAXException e) {
        
            Exception x = e.getException ();
            log(3,java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("SAXException:")+e.getMessage());
            ((x == null) ? e : x).printStackTrace ();
            defaultSettings();

        } catch (Throwable t) {
        
            log(3,t.getMessage());
            t.printStackTrace ();
            defaultSettings();
            
        }

    }
    
    /** This method uses the default settings present in the code
      *
      *
      */
    public void defaultSettings(){
            
       // initialize variables with default values
       this.targetPort = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("10000");
       this.targetAddress = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("127.0.0.1");
       this.serviceName = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("DiABlu_Service_Beta_1");
       this.serviceDescription = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("This_is_a_simple_way_to_interact_with_this_art_installation");
       this.btDelay = 10000;
       this.isSimulatorAuto = true;
       this.country = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("PT");
       this.language = java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("pt");
       this.logDetail = 0;
    
    }    
    
    /**
     * This method updates our model with our current settings values
     *
     */
    public void updateModel(DiABluServerModel model) {
        
        model.btDelay = this.btDelay;
        model.logDetail = this.logDetail;
        model.targetPort = this.targetPort;
        model.serviceDescription = this.serviceDescription;
        model.serviceName = this.serviceName;
        model.targetAddress = this.targetAddress;        
        model.language = this.language;
        model.country = this.country;                
        
    }
    /**
     * This method parse,validates & sets the command line arguments from the original args[]
     *
     */
    public void parseArgs(String args[]){
        
        String[] commandLineParameter; // Temporary parsing buffer
              
        //Process the args
        if ( args.length > 0 ){
           
            for (String s:args){
                
                // Parse the command line Parameter
                commandLineParameter = s.split(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString(":"),3);
                
                // Validate & set the results
                if (commandLineParameter.length!=2){
                
                    if (commandLineParameter[0].equalsIgnoreCase(COMMAND_TARGET_ADDRESS)){
                        
                    // set address
                    this.targetAddress = commandLineParameter[1];
                        
                    } else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_TARGET_PORT)){
                            
                           // TODO:validate the parameter 
                           // set port
                           this.targetPort = commandLineParameter[1];
                        
                      }else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_SERVICE_NAME)){
                            
                            // TODO:validate the parameter
                            // set service name
                            this.serviceName=commandLineParameter[1];
                        
                       } else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_SERVICE_DESCRIPTION)){
                        
                              // TODO:validate the parameter
                              // set service description                       
                              this.serviceDescription=commandLineParameter[1];  
                         
                         } else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_LANGUAGE)){
                                
                                // TODO:validate the parameter
                                // set language
                                this.language=commandLineParameter[1];
                            
                        
                           } else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_COUNTRY)){
                        
                                  // set country
                                  this.country=commandLineParameter[1];
                        
                             } else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_LOG_DETAIL)){
                        
                                  // parse & set log detail
                                  int tempLogDetail = this.logDetail; // default value
                                  
                                  if (commandLineParameter[1].equalsIgnoreCase(PARAMETER_SIMPLE_LOG)) { 
                                      
                                      tempLogDetail = 0; 
                                      
                                  } else if (commandLineParameter[1].equalsIgnoreCase(PARAMETER_DETAILED_LOG)) { 
                                      
                                      tempLogDetail = 1; 
                                  
                                  } else if (commandLineParameter[1].equalsIgnoreCase(PARAMETER_DEBUG_LOG)) { 
                                      
                                      tempLogDetail = 2; 
                                      
                                  } else {
                                      
                                      System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Log_detail_not_correct!Unknown_format:")+commandLineParameter[1]);
                                      
                                      System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Avaiable_formats_-_Log:")+PARAMETER_SIMPLE_LOG+
                                                                           java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("|Log:")+PARAMETER_DETAILED_LOG+
                                                                           java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("|Log:")+PARAMETER_DEBUG_LOG);
                                      
                                      System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Using_default[")+this.logDetail+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("]_instead"));
                                      
                                  }                                 
                                  this.logDetail=tempLogDetail;
                        
                               } else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_BT_DELAY)){
                        
                                     int tempBTdelay = this.btDelay;
                                     
                                     try {
                                     
                                         tempBTdelay = Integer.parseInt(commandLineParameter[1]);
                                         
                                     } catch (NumberFormatException e) {
                                         
                                         System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Wrong_number_format:")+commandLineParameter[1]);
                                         System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Using:")+this.btDelay+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("_instead"));
                                         
                                     }
                                     
                                     this.btDelay=tempBTdelay;
                        
                                 } else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_HELP)){
                        
                                        System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("DiABlu_Server_Command_Line_Help"));
                                        System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Avaiable_Command_Line_Parameters"));
                                        System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("--------------------------------"));
                                        System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Address:xxx.xxx.xxx.xxx[Valid_Target_IP_Address_-_") +
                                                            java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Example:_Address:127.0.0.1_for_localhost]"));
                                        System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Port:x[Valid_UDP_Target_Port_-_Example:_Port:10000"));
                                        
                                        // TODO:Finish the help screen
                                        System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Work_in_progress...:)"));
                                        
                                        // Exit the app
                                        System.exit(0);
                        
                                 }
                                /* 
                                 * ADD HERE MORE PARAMETERS CHECK & SET 
                                 * DON'T FORGET TO UPDATE THE ENTIRE SYSTEM IF NEEDED
                                 *
                                 */
                    
                } else {
                    
                    System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("WRONG_Command_supplied:")+s+java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("_using_registered_values_instead."));
                    System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Type_help_parameter_for_more_info"));
                    System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("Checking_for_next_parameter..."));
                    
                }
               
            }
            
            System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("No_more_parameters_to_check.Processed:")+args.length);
        
        } else {
            
            System.out.println(java.util.ResourceBundle.getBundle("citar/diablu/server/model/i18n/diABluServerDefaultBundle").getString("No_command_line_parameters_provided."));
        }
        
    }
    
    public void saveSettings(){
        
        //TODO: read the model
        
        
        //TODO: Save the data
       
        
        
        
        
    }

    
    
    
    
}