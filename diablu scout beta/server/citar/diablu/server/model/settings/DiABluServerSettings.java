/*
 * DiABluServerSettings.java
 *
 * Created on 24 de Agosto de 2006, 16:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package citar.diablu.server.model.settings;
// J2SE
import java.util.Vector;

//Log
import java.util.logging.Logger;
import java.util.logging.Level;

// IO
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;

// I18N & L9N
import java.util.Locale;
import java.util.ResourceBundle;

// XML
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMImplementation;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;


// DEFAULT SETTINGS
import static citar.diablu.server.model.settings.DiABluServerCONSTANTS.*;

// MODEL
import citar.diablu.server.controller.in.view.DiABluServerViewControllerListener;
import citar.diablu.server.model.DiABluServerModel;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluServerSettings {
    
    // DiABlu Model
    private DiABluServerModel model;
    
    // Log
    private static Logger logger = Logger.getLogger(LOG_MAIN_NAME);
    
    // Settings variables
    
    //i18n
    private String country=DEFAULT_COUNTRY;
    private String language=DEFAULT_LANGUAGE;
    private Locale location;
    private ResourceBundle diabluBundle;
    private String bundlePath=DEFAULT_BUNDLE_PATH;
    // view
    private String view = VIEW_DEFAULT;
    // bluetooth
    // filter
    private boolean filterFNames = BT_DEFAULT_FILTER_FNAME;
    private Vector <String> blackList = new Vector <String> ();   
    private boolean isSimulatorAuto = BT_DEFAULT_SIMULATOR;
    private boolean isDiscoveryAuto = BT_DEFAULT_START_DISCOVERY;
    private boolean fastMode = BT_DEFAULT_FAST_MODE;
    private int vCyclesIN = BT_DEFAULT_VCYCLES_IN;
    private int vCyclesOUT = BT_DEFAULT_VCYCLES_OUT;
    private int btDelay = BT_DEFAULT_DELAY;
    private boolean isServiceAuto = BT_DEFAULT_START_SERVICE;
    private String serviceName = BT_DEFAULT_SERVICE_NAME;
    private String serviceDescription = BT_DEFAULT_SERVICE_DESCRIPTION;
    
    private String protocol = OUT_DEFAULT_PROTOCOL;
    private String targetAddress = OUT_DEFAULT_TARGET_ADDRESS;
    private String targetPort = OUT_DEFAULT_TARGET_PORT;
    private boolean triggerAll = OUT_DEFAULT_TRIGGER_ALL;
    
    private String logDetail=LOG_DEFAULT_DETAIL;
    private String commandParameters[];
    
    // Parameters
    private String COMMAND_SERVICE_NAME="Name";
    private String COMMAND_SERVICE_DESCRIPTION="Description";
    private String COMMAND_BT_DELAY="BTDelay";
    private String COMMAND_TARGET_ADDRESS="Address";
    private String COMMAND_TARGET_PORT="Port";
    private String COMMAND_COUNTRY="Country";
    private String COMMAND_LANGUAGE="Language";
    private String COMMAND_RESOURCE_BUNDLE="Bundle";
    private String COMMAND_LOG_DETAIL="Log";
    private String COMMAND_HELP="Help";
    
    // Log
    private String PARAMETER_SIMPLE_LOG = "Simple";
    private String PARAMETER_DETAILED_LOG = "Detailed";
    private String PARAMETER_DEBUG_LOG = "Debug";
    
    /**
     * Creates a new instance of DiABluServerSettings
     */
    public DiABluServerSettings(String args[],DiABluServerModel model) {
        
        this.model = model;
        logger.setLevel(Level.ALL);
        logger.fine("Loading default settings");
        defaultSettings();
        logger.fine("Loading file settings");
        loadSettings();
        
        if (args.length > 1) {
            
            logger.fine("Parsing command line parameters...");
            parseArgs(args);
            
        }
        
    }
    
    private void checkArgs(String arg[]){
        
    }
    
    private void log(int priority,String msg){
        
        logger.finest(msg);
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
            log(4,"Opening_settings_file");
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(DEFAULT_SETTINGS_FILE));
            logger.finer("Settings File Open.Parsing contents...");
            
            // normalize text representation
            doc.getDocumentElement().normalize();
            log(6,"Root_element_of_the_doc_is:" + doc.getDocumentElement().getNodeName());
            
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
            NodeList globalList = doc.getElementsByTagName("global");
            Node globalNode = globalList.item(0);
            Element globalElement = (Element) globalList.item(0);
            
            
            //Location
            NodeList locationGlobalList = globalElement.getElementsByTagName("location");
            Element locationElement = (Element) locationGlobalList.item(0);
            NodeList locationElementList = locationElement.getChildNodes();
            this.country = locationElementList.item(0).getNodeValue().trim();
            logger.finer("Country:"+country);
            
            //Language
            NodeList languageGlobalList = globalElement.getElementsByTagName("language");
            Element languageElement = (Element) languageGlobalList.item(0);
            NodeList languageElementList = languageElement.getChildNodes();
            this.language = languageElementList.item(0).getNodeValue().trim();
            logger.finer("Language:"+language);
            
            //Bundle Path
            NodeList bundleGlobalList = globalElement.getElementsByTagName("bundle_path");
            Element bundleElement = (Element) bundleGlobalList.item(0);
            NodeList bundleElementList = bundleElement.getChildNodes();
            this.bundlePath = bundleElementList.item(0).getNodeValue().trim();
            logger.finer("Bundle Path:"+bundlePath);
            
            /** View
             *
             *  XML - example code
             *
             *  <view>
             *      Classical
             *  </view>
             *
             */
            NodeList viewList = doc.getElementsByTagName("view");
            Element viewElement = (Element) viewList.item(0);
            NodeList viewElementList = viewElement.getChildNodes();
            this.view = viewElementList.item(0).getNodeValue().trim();
            logger.finer("View:"+view);
            
            /** Filter
             *
             *  Extract the filter & black list settings and values
             *
             *  XML - example code
             *
             *  <filter>
             *
             *      <friendly_name_filter>true</friendly_name_filter>
             *      <black_list>
             *               <uuid></uuid>
             *      </black_list>
             *
             *  </filter>
             *
             *
             */
            NodeList filterList = doc.getElementsByTagName("filter");
            Element filterElement = (Element) filterList.item(0);
            
            NodeList filterFNamesNL = filterElement.getElementsByTagName("friendly_name_filter");
            Element filterFNamesElement = (Element)filterFNamesNL.item(0);
            NodeList filterFNamesNode = filterFNamesElement.getChildNodes();
            String filterFNamesValue = filterFNamesNode.item(0).getNodeValue().trim();
            this.filterFNames = new Boolean(filterFNamesValue);          
            logger.finer("Filter devices without friendly name set?:"+this.filterFNames+" from "+filterFNamesValue);

            NodeList blackListNodeList = filterElement.getElementsByTagName("black_list");
                
            Element blackListElement = (Element) blackListNodeList.item(0);
            
            
            if (blackListElement.hasChildNodes())
            {
                
                
            
            logger.finer("Processing black list...");
            
            NodeList uuidNodeList = blackListElement.getElementsByTagName("uuid");
            Element uuidElement = (Element)uuidNodeList.item(0);
            
            if (uuidElement.hasChildNodes()){
              
                NodeList uuidElementNodeList = uuidElement.getChildNodes();
                int blackListSize = uuidElementNodeList.getLength();
                if (blackListSize > 0){
                                
                    logger.finer("Processing "+blackListSize+" elements");
                
                    for (int i=0;i<blackListSize;i++){
                                    
                        Element blackedListElement = (Element) uuidElementNodeList.item(i);                                    
                        blackList.add(blackedListElement.getNodeValue().trim());
                                
                    }
            
                }
                
            } 
            
            }
            else {
                
                logger.finer("Empty Blacklist proceeding...");
                
            }
            
            /** Input
             *
             *  Extract the input settings
             *
             *  XML - example code
             *
             * <bluetooth>
             *
             *  <simulator>
             *
             *      <automatic>true</automatic>
             *
             *  </simulator>
             *
             *  <discovery>
             *
             *
             * <automatic_start_discovery>true</automatic_start_discovery>
             *
             * <fast_mode>false</fast_mode>
             *
             * <verify_cycles>
             *
             * <in>0</in>
             *
             * <out>0</out>
             *
             * </verify_cycles>
             *
             * <delay>10000</delay>
             *
             *
             * </discovery>
             *
             * <service>
             *
             * <automatic_start_service>true</automatic_start_service>
             * <service_name>Super DiABlu Service Beta 2</service_name>
             * <service_description>This service allows you to send keys and messages to this installation</service_description>
             *
             * </service>
             *
             * </bluetooth>
             *
             */
            
            NodeList bluetoothList = doc.getElementsByTagName("bluetooth");
            Element bluetoothElement = (Element) bluetoothList.item(0);
            
            
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
            NodeList simulatorList = bluetoothElement.getElementsByTagName("simulator");
            Element simulatorElement = (Element) simulatorList.item(0);
            NodeList automaticList = simulatorElement.getElementsByTagName("automatic");
            Element automaticElement = (Element) automaticList.item(0);
            NodeList automaticElementList = automaticElement.getChildNodes();
            String isAutomatic = automaticElementList.item(0).getNodeValue().trim();
            this.isSimulatorAuto = new Boolean(isAutomatic);  
            logger.finer("Autostart Simulator?:"+this.isSimulatorAuto);
            
            /** 
             *  Discovery
             *
             *  This code portion handles the Discovery enviroment variables
             * 
             *
             */
            NodeList discoveryList = bluetoothElement.getElementsByTagName("discovery");
            Element discoveryElement = (Element) discoveryList.item(0);
            
            // automatic discovery start
            NodeList autoStartList = discoveryElement.getElementsByTagName("automatic_start_discovery");
            Element autoStartElement = (Element) autoStartList.item(0);
            NodeList autoStartElementList = autoStartElement.getChildNodes();
            String isDiscoveryA =  autoStartElementList.item(0).getNodeValue().trim();
            this.isDiscoveryAuto = new Boolean(isDiscoveryA);
            logger.finer("Auto start bluetooth discovery?:"+isDiscoveryAuto);
                    
            // fast mode      
            NodeList fastModeList = discoveryElement.getElementsByTagName("fast_mode");
            Element fastModeElement = (Element) fastModeList.item(0);
            NodeList fastModeElementList = fastModeElement.getChildNodes();
            String isFastMode = fastModeElementList.item(0).getNodeValue().trim();
            this.fastMode = new Boolean(isFastMode);
            logger.finer("Enable Fast Mode?:"+fastMode);
            
            // verify cycles
            NodeList vCyclesList = discoveryElement.getElementsByTagName("verify_cycles");
            Element vCyclesElement = (Element) vCyclesList.item(0);
            
            // vCycles in
            NodeList vCyclesInList = vCyclesElement.getElementsByTagName("in");
            Element vCyclesInElement = (Element) vCyclesInList.item(0);
            NodeList vCyclesInElementList = vCyclesInElement.getChildNodes();
            String vCyclesIn = vCyclesInElementList.item(0).getNodeValue().trim();
            this.vCyclesIN = Integer.parseInt(vCyclesIn);
            logger.finer("Verify Cycles IN:"+vCyclesIN);
            
            // vCycles out
            NodeList vCyclesOutList = vCyclesElement.getElementsByTagName("out");
            Element vCyclesOutElement = (Element) vCyclesOutList.item(0);
            NodeList vCyclesOutElementList = vCyclesOutElement.getChildNodes();
            String vCyclesOut = vCyclesOutElementList.item(0).getNodeValue().trim();
            this.vCyclesOUT = Integer.parseInt(vCyclesOut);
            logger.finer("Verify Cycles OUT:"+vCyclesOUT);
            
            
            // delay
            NodeList delayInputList = discoveryElement.getElementsByTagName("delay");
            Element delayElement = (Element) delayInputList.item(0);
            NodeList delayElementList = delayElement.getChildNodes();
            String delay = delayElementList.item(0).getNodeValue().trim();
            this.btDelay = Integer.parseInt(delay);
            logger.finer("Bluetooth Inquiry Delay:"+delay);
            
            /**
             *  Service Server
             *
             *
             */
            NodeList serviceList = bluetoothElement.getElementsByTagName("service");
            Element serviceElement = (Element) serviceList.item(0);
            
            // automatic service start
            NodeList autoServiceStartList = serviceElement.getElementsByTagName("automatic_start_service");
            Element autoServiceStartElement = (Element) autoServiceStartList.item(0);
            NodeList autoServiceStartNodeList = autoServiceStartElement.getChildNodes();
            String autoServiceStart = autoServiceStartNodeList.item(0).getNodeValue().trim();
            this.isServiceAuto = new Boolean(autoServiceStart);
            logger.finer("Auto Start DiABlu Service Server?:"+isServiceAuto);
            
            // service name
            NodeList service_nameInputList = serviceElement.getElementsByTagName("service_name");
            Element service_nameElement = (Element) service_nameInputList.item(0);
            NodeList service_nameElementList = service_nameElement.getChildNodes();
            this.serviceName = service_nameElementList.item(0).getNodeValue().trim();
            logger.finer("Service Name:"+serviceName);
            
            // service description
            NodeList service_descriptionInputList = serviceElement.getElementsByTagName("service_description");
            Element service_descriptionElement = (Element) service_descriptionInputList.item(0);
            NodeList service_descriptionElementList = service_descriptionElement.getChildNodes();
            this.serviceDescription = service_descriptionElementList.item(0).getNodeValue().trim();
            logger.finer("Service Description:"+serviceDescription);
            
            
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
             *           <trigger_all>true</trigger_all>
             *       </output>
             *
             */
            NodeList outputList = doc.getElementsByTagName("output");
            Element outputElement = (Element) outputList.item(0);
            
            NodeList target_protocol = outputElement.getElementsByTagName("protocol");
            Element target_protocolElement = (Element) target_protocol.item(0);
            NodeList target_protocolElementList = target_protocolElement.getChildNodes();
            this.protocol = target_protocolElementList.item(0).getNodeValue().trim();
            logger.finer("Output Protocol:"+protocol);
            
            // target address
            NodeList target_addressOutputList = outputElement.getElementsByTagName("target_address");
            Element target_addressElement = (Element) target_addressOutputList.item(0);
            NodeList target_addressElementList = target_addressElement.getChildNodes();
            this.targetAddress = target_addressElementList.item(0).getNodeValue().trim();
            logger.finer("Target Address:"+targetAddress);
            
            // port
            NodeList portOutputList = outputElement.getElementsByTagName("port");
            Element portElement = (Element) portOutputList.item(0);
            NodeList portElementList = portElement.getChildNodes();
            this.targetPort = portElementList.item(0).getNodeValue().trim();
            logger.finer("Port:"+targetPort);
            
            // trigger all
            NodeList triggerAllNL = outputElement.getElementsByTagName("trigger_all");
            Element triggerAllElement = (Element)triggerAllNL.item(0);
            NodeList triggerAllNode = triggerAllElement.getChildNodes();
            String triggerAllValue = triggerAllNode.item(0).getNodeValue().trim();
            this.triggerAll = new Boolean(triggerAllValue);          
            logger.finer("Trigger All Device List Inquiries ?:"+this.triggerAll);
                        
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
            NodeList logList = doc.getElementsByTagName("log");
            Element logElement = (Element) logList.item(0);
            
            // detail
            NodeList detailLogList = logElement.getElementsByTagName("detail");
            Element detailElement = (Element) detailLogList.item(0);
            NodeList detailElementList = detailElement.getChildNodes();
            String detail = detailElementList.item(0).getNodeValue().trim();
            this.logDetail = detail;
            logger.finer("Log detail:"+logDetail);
            
            // file
            // TODO:in next version
            
        } catch (SAXParseException err) {
            
            log(3,"**_Parsing_error" + ",_line_" + err.getLineNumber() + ",_uri_" + err.getSystemId());
            log(3,"_" + err.getMessage());
            defaultSettings();
            
        } catch (SAXException e) {
            
            Exception x = e.getException();
            log(3,"SAXException:"+e.getMessage());
            ((x == null) ? e : x).printStackTrace();
            defaultSettings();
            
        } catch (Throwable t) {
            
            log(3,t.getMessage());
            t.printStackTrace();
            defaultSettings();
            
        }
        
        updateModel();
    }
    
    /** This method uses the default settings present in the code
     *
     *
     */
    public void defaultSettings(){
        
        // initialize variables with default values
        logger.entering(this.toString(),"Calling default settings");
        
        // i18n
        this.country = DEFAULT_COUNTRY;
        this.language = DEFAULT_LANGUAGE;
        this.bundlePath=DEFAULT_BUNDLE_PATH;
       
        // view
        this.view = VIEW_DEFAULT;
        
        // filter
        this.filterFNames = FILTER_DEFAULT_FNAMES;
        
        // simulator
        this.isSimulatorAuto = BT_DEFAULT_SIMULATOR;
        
        // BT
        // discovery
        this.isDiscoveryAuto = BT_DEFAULT_START_DISCOVERY;
        this.fastMode = BT_DEFAULT_FAST_MODE;
        this.vCyclesIN = BT_DEFAULT_VCYCLES_IN;
        this.vCyclesOUT = BT_DEFAULT_VCYCLES_OUT;
        this.btDelay = BT_DEFAULT_DELAY;
        // service
        this.isServiceAuto = BT_DEFAULT_START_SERVICE;        
        this.serviceName = BT_DEFAULT_SERVICE_NAME;
        this.serviceDescription = BT_DEFAULT_SERVICE_DESCRIPTION;
        
        // OUTPUT
        this.protocol = OUT_DEFAULT_PROTOCOL;
        this.targetPort = OUT_DEFAULT_TARGET_PORT;
        this.targetAddress = OUT_DEFAULT_TARGET_ADDRESS;
        this.triggerAll = OUT_DEFAULT_TRIGGER_ALL;

        this.logDetail = LOG_DEFAULT_DETAIL;
        
    }
    
    /**
     * This method updates our model with our current settings values
     *
     */
    public void updateModel() {
        
        logger.finer("Updating model...");
        
        // i18n
        model.newLanguage(this.language);
        model.newCountry(this.country);
        model.setBundlePath(this.bundlePath);
        
        // view
        model.setView(this.view);
        
        // filter
        logger.finest("Setting filter1:"+this.filterFNames);
        // DEBUG!!!
        //  replaces true in the following line
        model.setFilterFriendlyNames(this.filterFNames);
        for (String blackUuid:blackList){
            
            model.addToBlackList(blackUuid);
            
        }
        
        // bluetooth
        logger.finest("Auto start discovery ?:"+this.isDiscoveryAuto);
        model.setAutoStartDiscovery(this.isDiscoveryAuto);
        model.newVerifyCyclesIN(this.vCyclesIN);
        model.newVerifyCyclesOUT(this.vCyclesOUT);
        model.newBluetoothDelay(this.btDelay);
        
        model.setAutoStartService(this.isServiceAuto);

        model.newServiceDescription(this.serviceDescription);
        model.newServiceName(this.serviceName);
        // simulator
        model.autoSimulator(this.isSimulatorAuto);
        // out
        model.setProtocol(this.protocol);
        model.newTargetAddress(this.targetAddress);
        model.newPort(this.targetPort);
        model.setTriggerAll(this.triggerAll);
        
        // log
        model.newLogLevel(this.logDetail);
        
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
                commandLineParameter = s.split(":",3);
                
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
                        String tempLogDetail = this.logDetail; // default value
                        
                        try {
                            
                            Level checkLogLevel = Level.parse(commandLineParameter[1]);                             
                            tempLogDetail = checkLogLevel.getLocalizedName();
                            
                        } catch(Exception e) {
                            
                            logger.warning("Wrong log detail:"+commandLineParameter[1]);
                            
                        }
                        this.logDetail=tempLogDetail;
                        
                    } else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_BT_DELAY)){
                        
                        int tempBTdelay = this.btDelay;
                        
                        try {
                            
                            tempBTdelay = Integer.parseInt(commandLineParameter[1]);
                            
                        } catch (NumberFormatException e) {
                            
                            System.out.println("Wrong_number_format:"+commandLineParameter[1]);
                            System.out.println("Using:"+this.btDelay+"_instead");
                            
                        }
                        
                        this.btDelay=tempBTdelay;
                        
                    } else if (commandLineParameter[0].equalsIgnoreCase(COMMAND_HELP)){
                        
                        System.out.println("DiABlu_Server_Command_Line_Help");
                        System.out.println("Avaiable_Command_Line_Parameters");
                        System.out.println("--------------------------------");
                        System.out.println("Address:xxx.xxx.xxx.xxx[Valid_Target_IP_Address_-_" +
                                "Example:_Address:127.0.0.1_for_localhost]");
                        System.out.println("Port:x[Valid_UDP_Target_Port_-_Example:_Port:10000");
                        
                        // TODO:Finish the help screen
                        System.out.println("Work_in_progress...:)");
                        
                        // Exit the app
                        System.exit(0);
                        
                    }
                                /*
                                 * ADD HERE MORE PARAMETERS CHECK & SET
                                 * DON'T FORGET TO UPDATE THE ENTIRE SYSTEM IF NEEDED
                                 *
                                 */
                    
                } else {
                    
                    System.out.println("WRONG_Command_supplied:"+s+"_using_registered_values_instead.");
                    System.out.println("Type_help_parameter_for_more_info");
                    System.out.println("Checking_for_next_parameter...");
                    
                }
                
            }
            
            System.out.println("No_more_parameters_to_check.Processed:"+args.length);
            
        } else {
            
            System.out.println("No_command_line_parameters_provided.");
        }
        
    }
    
    public void saveSettings(){
        
        logger.fine("Saving settings...");
        BufferedWriter out;
        
        try {
            
            out = new BufferedWriter(new FileWriter(DEFAULT_SETTINGS_FILE));
            logger.finest("Opened settings file");
            
        } catch (IOException ioe){
            
            
            logger.logp(Level.WARNING,"DiABlu Settings","Save settings","Error creating settings file",ioe);
            return;
            
        }
        
        logger.finest("Creating xml document");
        // Create XML DOM Document         
        Document xmldoc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;        
        try {
            
            builder = factory.newDocumentBuilder();
            
        } catch (Exception e){
            
            logger.log(Level.WARNING,"Error parsing xml file",e);
            return;
            
        }        
        DOMImplementation impl = builder.getDOMImplementation();
        Element e_global,e_location,e_language,e_bundle,e_view,e_filter,e_ffname,e_blist,e_uuid,e_bluetooth,e_simulator = null;
        Element e_discovery,e_autoDisc,e_fastM,e_vCycles,e_vIn,e_vOut,e_delay,e_service,e_autoServ,e_sName,e_sDesc,e_protocol = null;
        Element e_simAuto, e_output,e_address,e_port,e_triggerAll,e_log,e_logDetail = null;
        
        Node n_location,n_language,n_bundle,n_view, n_ffname, n_blist, n_uuid, n_simAuto, n_autoDisc, n_fastM = null;
        Node n_vIn,n_vOut,n_delay, n_sName, n_sDesc, n_autoServ, n_protocol, n_address, n_port,n_triggerAll, n_logDetail = null;
        
        // Document.
        xmldoc = impl.createDocument(null, "diabluServer", null);
        
        // Root element.
        Element root = xmldoc.getDocumentElement();
        
        // Create the elements
        // Global
        e_global = xmldoc.createElementNS(null,"global");
        
        e_location = xmldoc.createElementNS(null,"location");
        n_location = xmldoc.createTextNode(model.getLocation());
        e_location.appendChild(n_location);
        logger.fine("Saving location:"+model.getLocation());
        
        
        e_language = xmldoc.createElementNS(null,"language");
        n_language = xmldoc.createTextNode(model.getLanguage());
        e_language.appendChild(n_language);
        logger.fine("Saving language:"+model.getLanguage());
        
        e_bundle = xmldoc.createElementNS(null,"bundle_path");                
        n_bundle = xmldoc.createTextNode(this.bundlePath);
        e_bundle.appendChild(n_bundle);
        
        e_global.appendChild(e_location);
        e_global.appendChild(e_language);
        e_global.appendChild(e_bundle);                
        
        // View
        e_view = xmldoc.createElementNS(null,"view");
        n_view = xmldoc.createTextNode(model.getViewString());
        e_view.appendChild(n_view);
        logger.fine("Saving view:"+model.getViewString());
        
        // Filter
        e_filter = xmldoc.createElementNS(null,"filter");
        
        e_ffname = xmldoc.createElementNS(null,"friendly_name_filter");       
        n_ffname = xmldoc.createTextNode(Boolean.toString(model.isFilterFriendlyName()));        
        e_ffname.appendChild(n_ffname);
        logger.fine("Friendly name filter:"+Boolean.toString(model.isFilterFriendlyName()));
        
        // Filter - Black List
        e_blist = xmldoc.createElementNS(null,"black_list");
        Vector <String> bList = new Vector <String> ();
        bList = model.getBlackList();
        
        for (String uuidT : bList){
            
            e_uuid = xmldoc.createElementNS(null,"uuid");
            n_uuid = xmldoc.createTextNode(uuidT);
            e_uuid.appendChild(n_uuid);
            e_blist.appendChild(e_uuid);
            logger.fine("Saved Blacklisted:"+uuidT);
                        
        }
        
        e_filter.appendChild(e_ffname);
        e_filter.appendChild(e_blist);
        
        
        // Bluetooth
        e_bluetooth = xmldoc.createElementNS(null,"bluetooth");
        
        
        // Bluetooth Simulator
        e_simulator = xmldoc.createElementNS(null,"simulator");
        e_simAuto = xmldoc.createElementNS(null,"automatic");
        n_simAuto = xmldoc.createTextNode(Boolean.toString(model.isAutomaticSimulator()));
        logger.fine("Is simulator automatic ?"+Boolean.toString(model.isAutomaticSimulator()));
        
        e_simAuto.appendChild(n_simAuto);
        e_simulator.appendChild(e_simAuto);
        
        // Bluetooth Discovery
        e_discovery = xmldoc.createElementNS(null,"discovery");
        e_autoDisc = xmldoc.createElementNS(null,"automatic_start_discovery");
        n_autoDisc = xmldoc.createTextNode(Boolean.toString(model.isAutoStartDiscovery()));
        e_autoDisc.appendChild(n_autoDisc);        
        logger.fine("Automatic start discovery ?"+Boolean.toString(model.isAutoStartDiscovery()));
        
        e_fastM = xmldoc.createElementNS(null,"fast_mode");
        n_fastM = xmldoc.createTextNode(Boolean.toString(model.isFastMode()));
        e_fastM.appendChild(n_fastM);
        
        
        e_vCycles = xmldoc.createElementNS(null,"verify_cycles");
        
        e_vIn = xmldoc.createElementNS(null,"in");
        n_vIn = xmldoc.createTextNode(Integer.toString(model.getBtVCyclesIN()));
        e_vIn.appendChild(n_vIn);
        
        e_vOut = xmldoc.createElementNS(null,"out");
        n_vOut = xmldoc.createTextNode(Integer.toString(model.getBtVCyclesOUT()));
        e_vOut.appendChild(n_vOut);
        
        e_vCycles.appendChild(e_vIn);
        e_vCycles.appendChild(e_vOut);
        
        e_delay = xmldoc.createElementNS(null,"delay");
        n_delay = xmldoc.createTextNode(Integer.toString(model.getBtDelay()));
        e_delay.appendChild(n_delay);        
        
        e_discovery.appendChild(e_autoDisc);
        e_discovery.appendChild(e_fastM);
        e_discovery.appendChild(e_vCycles);
        e_discovery.appendChild(e_delay);
        
        
        
        // Bluetooth Service
        e_service = xmldoc.createElementNS(null,"service");
        
        e_autoServ = xmldoc.createElementNS(null,"automatic_start_service");
        n_autoServ = xmldoc.createTextNode(Boolean.toString(model.isAutoStartService()));
        e_autoServ.appendChild(n_autoServ);
        
        e_sName = xmldoc.createElementNS(null,"service_name");
        n_sName = xmldoc.createTextNode(model.getServiceName());
        e_sName.appendChild(n_sName);
        
        e_sDesc = xmldoc.createElementNS(null,"service_description");
        n_sDesc = xmldoc.createTextNode(model.getServiceDescription());
        e_sDesc.appendChild(n_sDesc);
        
        e_service.appendChild(e_autoServ);
        e_service.appendChild(e_sName);
        e_service.appendChild(e_sDesc);
        
        e_bluetooth.appendChild(e_simulator);
        e_bluetooth.appendChild(e_discovery);
        e_bluetooth.appendChild(e_service);
        
        
        // Output
        e_output = xmldoc.createElementNS(null,"output");
        
        e_protocol = xmldoc.createElementNS(null,"protocol");
        n_protocol = xmldoc.createTextNode(model.getProtocol());
        e_protocol.appendChild(n_protocol);
        
        e_address = xmldoc.createElementNS(null,"target_address");
        n_address = xmldoc.createTextNode(model.getTargetAddress());
        e_address.appendChild(n_address);
        
        e_port = xmldoc.createElementNS(null,"port");
        n_port = xmldoc.createTextNode(model.getTargetPort());
        e_port.appendChild(n_port);
        
        e_triggerAll = xmldoc.createElementNS(null,"trigger_all");
        n_triggerAll = xmldoc.createTextNode(Boolean.toString(model.isTriggerAll()));
        e_triggerAll.appendChild(n_triggerAll);
        
        e_output.appendChild(e_protocol);
        e_output.appendChild(e_address);
        e_output.appendChild(e_port);
        e_output.appendChild(e_triggerAll);
        
        // log
        e_log = xmldoc.createElementNS(null,"log");
        
        e_logDetail = xmldoc.createElementNS(null,"detail");
        n_logDetail = xmldoc.createTextNode(Integer.toString(model.getLogDetail()));
        e_logDetail.appendChild(n_logDetail);
        
        e_log.appendChild(e_logDetail);
        
        
        // Append all to root
        root.appendChild(e_global);
        root.appendChild(e_view);
        root.appendChild(e_filter);
        root.appendChild(e_bluetooth);
        root.appendChild(e_output);
        root.appendChild(e_log);        
        
        // Save the data
        logger.finest("XML serialization...");
        // Serialisation through Tranform.
        DOMSource domSource = new DOMSource(xmldoc);
        StreamResult streamResult = new StreamResult(out);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer;
        try {
         serializer = tf.newTransformer();
         serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
         serializer.setOutputProperty(OutputKeys.INDENT,"yes");
         //serializer.
         //serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"src/citar/diablu/server/model/settings/diABluServerSettings.dtd");
         serializer.transform(domSource, streamResult);
         out.flush();
         out.close();
         logger.finest("Done with xml saving");
         
        } catch (Exception e){
            
            logger.warning("Error serializing xml map:"+e.getLocalizedMessage());
            
        }
        
        logger.finest("Settings saved.");
        
    }
    
    
    
    
    
}
