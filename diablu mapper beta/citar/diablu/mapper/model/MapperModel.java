/*
 * MapperModel.java
 *
 * Created on 9 de Setembro de 2006, 12:36
 *
 * Copyright (C) 9 de Setembro de 2006 Nuno Rodrigues
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

package citar.diablu.mapper.model;

// j2se
// util
import java.util.Vector;
import java.util.Iterator;

// Logging API
import com.sun.corba.se.impl.orbutil.LogKeywords;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;

// I/O
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;

// XML classes.
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMImplementation;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//JAXP 1.1
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

// mapper
import citar.diablu.mapper.controller.MapperModelViewController;
import citar.diablu.mapper.controller.MapperModelBTController;
import citar.diablu.mapper.controller.MapperViewController;
import citar.diablu.mapper.controller.MapperBTController;
import citar.diablu.mapper.controller.MapperHelpController;

import citar.diablu.mapper.model.data.DiABluAnchorPoint;
import citar.diablu.mapper.model.data.DiABluLandmark;

import citar.diablu.mapper.view.MapperView;
import citar.diablu.mapper.view.help.MapperViewHelp;
import citar.diablu.mapper.view.log.MapperViewLog;

import citar.diablu.mapper.model.com.bt.DiABluMapperBTDeviceDiscovery;

// const
import static citar.diablu.mapper.model.settings.DiABluMapperCONSTANTS.*;

// jsr82
import javax.bluetooth.*;

/**
 *
 * @author Nuno Rodrigues
 */
public class MapperModel implements MapperModelViewController,MapperModelBTController {
    
    private boolean isAutomaticSearch = true;
    private boolean checkedCopyAll = true;
    private boolean checkedCopyFiltered = false;
    
    private boolean[] logOutputOptions = new boolean[7];
    private int logDetail;
    
    private MapperBTController btEngine;
    private int btDelay = 0;
    private boolean BT_ENGINE_READY = false;
    
    private MapperViewController view;
    private boolean VIEW_READY = false;
    
    private MapperHelpController help;
    private boolean HELP_READY = false;
    
    private Vector <DiABluLandmark> landmarkList = new Vector <DiABluLandmark> ();
    private Vector <String> filterDeviceList = new Vector <String> ();
    
    private static Logger logger = Logger.getLogger("mapper.log");
    
    /** Creates a new instance of MapperModel */
    public MapperModel(String[] args) {
        
        try {
            
            // Log file handler
            Handler fh = new FileHandler("MapperLogFile");
            fh.setLevel(Level.ALL);
            
            // Log console handler
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.ALL);
            
            // Add the handlers to our log
            logger.addHandler(fh);
            logger.addHandler(ch);
            
        } catch (Exception e){
            
            logger.warning("Couldn't open log file!");
            e.printStackTrace();
            
        }
        logger.setLevel(Level.ALL);
        logger.entering("MapperModel","Constructor");
        
        final MapperModelViewController model = this;
        
        loadSettings();
        
        logger.fine("Creating view");
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    view = new MapperView(model);
                    //view.setVisible(true);
                    VIEW_READY = true;
                    updateView();
                }
            });
            
        } catch (Exception e){
            
            logger.throwing("MapperModel","Starting View",e);
        }
        
        try {
            
            logger.finest("Calling BT Device Discovery Class");
            BT_ENGINE_READY = true;
            btEngine = new DiABluMapperBTDeviceDiscovery(this,isAutomaticSearch,btDelay);
            //btEngine.manualSearch();
        } catch (BluetoothStateException bte){
            
            // Log the action
            BT_ENGINE_READY = false;
            logger.throwing("MapperModel","Constructor calling BT Device Discovery",bte);
            
           
        }
        
        
        
    }
    
    public static void main(String[] args){
        
        System.out.println("Starting system");
        new MapperModel(args);
        
    }
    
    /**
     * This method reads the settings xml file and updates the model values
     * Since we reduced the view options there's no more the need for this method
     *
     */
    @Deprecated
    private void loadSettings(){
        VIEW_READY = true;
        
        
    }
    
    /**
     *  This method updates the view with all the model variables
     *
     *
     */
    private void updateView(){
        
        if (!VIEW_READY) {
            logger.warning("Couldn't update the view:View not ready");
            return;
        }
        
        
        // sets the list of filtered device classes
//        view.setFilteredDeviceClasses(filterDeviceList);
        
        // informs the view of the current landmark list
        view.setLandmarkList(landmarkList);
        
        // set & get automatic discovery
     //   view.setAutomaticDiscovery(isAutomaticSearch);
        
        // informs the view of the current discovery delay
        String delay = ""+btDelay;
     //   view.setDiscoveryDelay(delay);
        
        // checks/unchecks the copy all option in creating landmarks
        view.setCopyAllFlag(checkedCopyAll);
        
        // checks/unchecks the filtered copy option in creating landmarks
    //    view.setFilteredCopy(checkedCopyFiltered);
        
        
    }
    
    /*
     * Bluetooth Interface Methods
     *
     *
     *
     */
    public void newAnchorPointList( Vector <DiABluAnchorPoint> anchorList){
        
        if (VIEW_READY){
            
            view.setDetectedAnchorList(anchorList);
            
        }
   
    }
    
    
    
    
    /**
     *
     * View Interface Methods
     *
     *
     *
     *
     */
        
    // perform a manual discovery as soon as possible
    public void manualSearch(){
        
        if (BT_ENGINE_READY){
            
            btEngine.manualSearch();
            
        }
        
    }
    
    // sets/unsets discovery engine to/from automatic
    public void setAutomaticSearch(boolean state){
        
        isAutomaticSearch = state;
        logger.finest("Setting automatic search2:"+isAutomaticSearch);
        logger.finest("Is BT ready?:"+BT_ENGINE_READY);
        if (isAutomaticSearch){
            
            if (BT_ENGINE_READY) {
                
                logger.finest("AUTO MODE ON");
                btEngine.automatic(this.btDelay);
                
            }
            
        } else {
            
            if (BT_ENGINE_READY){
                
                logger.finest("AUTO MODE OFF");
                btEngine.stopSearch();
                
            }
        }
        
    }
    
    // sets the current bt delay (needed in automatic mode)
    public void setDiscoveryDelay(String btDelay){
        
        if (isAutomaticSearch){
            
            this.btDelay = Integer.parseInt(btDelay);
            
        }
        
    }
    
    // creates a landmark based on the list of anchor points
    public void createLandmark(String name,Vector <DiABluAnchorPoint> anchorList){
        
        logger.entering("MapperModel","createLandmark():"+name,anchorList);
        
        // DEBUG ONLY
        for (DiABluAnchorPoint dap:anchorList){
            
            logger.finest("Got Anchor Point:"+dap.getFname()+"@"+dap.getUUID());
            
        }
        
        
        // check to see if there is already a landmark with the same name &/or anchorList
        for (DiABluLandmark dl:landmarkList){
            
            if (dl.getName().equalsIgnoreCase(name)){
                
                logger.warning("Landmark "+name+" already exists!");
                return;
                
            }
            
            if (dl.getAnchorList().equals(anchorList)){
                
                logger.warning("Landmark:"+dl.toString()+"already has the same anchor point list");
                return;
                
            }
            
        }
        
        // create the landmark
        DiABluLandmark dl = new DiABluLandmark(name,anchorList);
        
        // add it to the model list
        landmarkList.addElement(dl);
        
        // refresh the view
        if (VIEW_READY) {
            
            view.setLandmarkList(landmarkList);
            
        }
        
    }
    
    // landmark creation settings
    public void setCopyAllFlag(boolean copyAll){
        
        // refresh the view
        if (VIEW_READY){
            
            view.setCopyAllFlag(copyAll);
            
        }
        
    }
    
    
    @Deprecated
    public void setFilteredCopy(boolean copyOnly){
        
        if (VIEW_READY) {
            
       //     view.setFilteredCopy(copyOnly);
            
        }
        
    }
    
    
    // sets the list of filtered device classes
    public void setFilteredDeviceClasses(Vector <String> filterList){
        
        this.filterDeviceList = filterList;
        
    }
    // reset the landmark info - clears all the anchor points
    public void resetLandmark(String name){
        
        // make sure we have work to do
        if (!landmarkList.isEmpty()){
            
            for (int i=0;i<landmarkList.size();i++){
                
                if (landmarkList.elementAt(i).getName().equalsIgnoreCase(name)){
                    
                    // found our landmark,let's empty the anchorlist
                    landmarkList.removeElementAt(i);
                    DiABluLandmark emptyL = new DiABluLandmark(name);
                    landmarkList.add(emptyL);
                    
                    // update the view
                    if (VIEW_READY){
                        
                        view.setLandmarkList(landmarkList);
                        
                    }
                    
                    //there's nothing more to do here
                    break;
                }
                
                
            }
        }
        
    }
    
    // deletes the landmark
    public void deleteLandmark(String name){
        
        // make sure we have work to do
        if (!landmarkList.isEmpty()){
            
            DiABluLandmark dL = new DiABluLandmark();
            for (Iterator <DiABluLandmark> landIterator=landmarkList.iterator();landIterator.hasNext();){
                
                dL = landIterator.next();
                
                if (dL.getName().equalsIgnoreCase(name)){
                    
                    // we've found our target'
                    landIterator.remove();
                    
                    // inform the view
                    if (VIEW_READY){
                        
                        view.setLandmarkList(landmarkList);
                    }
                    
                    // there's nothing more to do
                    break;
                    
                }
                
            }
            
            
        }
        
    }
    
    // adds the current selected anchor point to the selected landmark
    public void addToLandmark(String landmarkName, Vector <DiABluAnchorPoint> dapList){
        
        // paranoid check
        if (dapList == null){
            
            logger.warning("Null argument");
            return;
            
        }
        // make sure we've got work to do
        if (!landmarkList.isEmpty()){
            
            logger.finest("Looking for "+landmarkName+" in Landmark List("+landmarkList.size()+")");
            
            // first let's locate the landmark
            DiABluLandmark dl = new DiABluLandmark();
            for (Iterator <DiABluLandmark> landIterator = landmarkList.iterator();landIterator.hasNext();){
                
                dl = landIterator.next();
                if (dl.getName().equalsIgnoreCase(landmarkName)){
                    
                    logger.finest("#Found landmark:"+dl.getName()+" with "+dl.getAnchorList().size()+"Anchor Points -> adding "+dapList.size()+" Anchor Points");
                    // we've got a match add the anchor point
                    for (DiABluAnchorPoint dap:dapList){
                        
                        logger.finest("#Adding:"+dap.getFname()+"@"+dap.getUUID());
                        dl.addAnchorPoint(dap);
                        
                    }
                    logger.finest("#Landmark has now "+dl.getAnchorList().size()+" anchor points");
                    // remove the old landmark
                    landIterator.remove();
                    
                    // insert the new landmark
                    landmarkList.addElement(dl);
                    
                    // inform the view
                    if (VIEW_READY) {
                        view.setLandmarkList(landmarkList);
                    }
                    // nothing more to do
                    break;
                    
                }
                
            }
            
            
        } else {
            
            // Log the error
            logger.warning("Empty landmark list");
            
        }
        
    }
    
    // adds the current selected anchor point to the selected landmark
    public void removeFromLandmark(String landmarkName, Vector <DiABluAnchorPoint> dapList){
        
        // make sure we've got work to do
        if (!landmarkList.isEmpty()){
            
            
            // first let's locate the landmark
            DiABluLandmark dl = new DiABluLandmark();
            for (Iterator <DiABluLandmark> landIterator = landmarkList.iterator();landIterator.hasNext();){
                
                dl = landIterator.next();
                if (dl.getName().equalsIgnoreCase(landmarkName)){
                    
                    // found our landmark let's remove the anchors'
                    DiABluAnchorPoint dap = new DiABluAnchorPoint();
                    DiABluAnchorPoint dapR = new DiABluAnchorPoint();
                    for (Iterator <DiABluAnchorPoint> dapIterator = dl.getAnchorList().iterator();dapIterator.hasNext();){
                        
                        dap = dapIterator.next();
                        for (Iterator <DiABluAnchorPoint> dapRIterator = dapList.iterator();dapRIterator.hasNext();){
                            
                            dapR = dapRIterator.next();
                            if ( dap.compareTo(dapR)==0 ){
                                
                                // found a match, remove the elements so we don't need to compare again'
                                dapIterator.remove();
                                dapRIterator.remove();
                                
                                // nothing more to do here
                                break;
                                
                            }
                            
                        }
                        
                        
                    }
                    
                    // remove the old landmark
                    landIterator.remove();
                    // insert the new landmark
                    landmarkList.addElement(dl);
                    // inform the view
                    if (VIEW_READY) {
                        view.setLandmarkList(landmarkList);
                    }
                    // nothing more to do
                    break;
                    
                }
                
            }
            
            
        } else {
            
            logger.warning("MapperModel"+"removeFromLandmark()"+" Landmark List is empty");
            
        }
        
    }
    
    // informs the model of the current selected landmark
    public void selectedLandmark(String name){
        
        Vector <DiABluAnchorPoint> correspondingAnchorList = new Vector <DiABluAnchorPoint> ();
        
        if (!name.equalsIgnoreCase("")){
            
            
            for (DiABluLandmark dl:landmarkList){
                
                if (dl.getName().equalsIgnoreCase(name)){
                    
                    // found a match let's inform the view of the anchor list'
                    
                    
                    correspondingAnchorList = dl.getAnchorList();
                    // nothing more to do here
                    break;
                    
                    
                }
            }
            
        }
        
        
        
        if (VIEW_READY){
            
            view.setLandmarkAnchorList(correspondingAnchorList);
            
        }
        
    }
    
    // set the preferred language
    public void setLanguage(String langue){}
    
    
    // set/unsets the log output options
    public void setLogOutput(boolean[] logOut){
        
        // copy the values to internal data
        for (int i=0;i<LOG_OUT_OPTIONS;i++){
            this.logOutputOptions[i]=logOut[i];
        }
        
        // update the handlers
        updateLogout();
        
    }
    
    private void updateLogout(){
        
        if (logOutputOptions[0]) {
            // add console handler
            
            
        } else {
            // remove console handler
            
        }
        
        if (logOutputOptions[1]) {
            // add file handler
            
            
        } else {
            // remove file handler
            
            
        }
        
        if (logOutputOptions[2]) {
            // add window handler
            
            
        } else {
            // remove window handler
            
            
        }
        
    }
    
    
    
    
    
    /**
     * Updates the desired log detail
     *
     *
     */
    public void setLogDetail(int detailCode){
        
        Level LogLevel=Level.OFF;
        
        switch (detailCode){
            
            case LOG_DETAIL_SEVERE:{
                
                LogLevel = Level.SEVERE;
                break;
            }
            
            case LOG_DETAIL_WARNING : {
                
                LogLevel = Level.WARNING;
                break;
            }
            case LOG_DETAIL_CONFIG : {
                
                LogLevel = Level.CONFIG;
                break;
                
            }
            case LOG_DETAIL_INFO : {
                
                LogLevel = Level.INFO;
                break;
                
            }
            case LOG_DETAIL_FINER : {
                
                LogLevel = Level.FINER;
                break;
                
            }
            case LOG_DETAIL_FINEST : {
                
                LogLevel = Level.FINEST;
                break;
            }
            
        }
        
        Handler[] hands = logger.getHandlers();
        for (int i =0;i<hands.length;i++){
            
            hands[i].setLevel(LogLevel);
            
        }
        logger.setLevel(LogLevel);
        
    }
    // exit the application
    public void exit(boolean save){
        
        if (save) {
            
            saveSettings();
            
        }
        
        System.exit(0);
    }
    
    /*
     * show/hide the help window
     *
     */
    public void help(){
        
        if (HELP_READY){
            
            HELP_READY = false;
            
            
        } else {
            
            HELP_READY = true;
            
            
        }
        
        help.show(HELP_READY);
        
    }
    
    
    // save the model to a xml file
    public void save(File mapFile){
        
        if (mapFile == null){
         
            logger.warning("Null argument");
            return;
            
        }
        logger.entering("MapperModel","save()");
        
        BufferedWriter out;
        
        // Create Output File
        try {
            
            out = new BufferedWriter(new FileWriter(mapFile.getName()));
            
        } catch (IOException ioe){
            
            logger.logp(Level.WARNING,"MapperModel","save()","Error creating output file",ioe);
            return;
            
        }
        
        // Create XML DOM document (Memory consuming).
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
        Element e1,e2,e3,e4,e5,e6,e7 = null;
        Node n1,n2,n3,n4 = null;
        
        // Document.
        xmldoc = impl.createDocument(null, "MAP", null);
        
        // Root element.
        Element root = xmldoc.getDocumentElement();
        
        logger.fine("Processing landmark info");
        for (DiABluLandmark dl:landmarkList){
            
            e1 = xmldoc.createElementNS(null,"LANDMARK");
            
            e2 = xmldoc.createElement("NAME");
            n1 = xmldoc.createTextNode(dl.getName());
            logger.fine("Adding landmark:"+dl.getName());
            e2.appendChild(n1);
            e1.appendChild(e2);
            
            e3 = xmldoc.createElementNS(null,"ANCHOR_LIST");
            
            for (DiABluAnchorPoint dap:dl.getAnchorList()){
                
            
                logger.finer("Adding Anchor:"+dap.toString());
                e4 = xmldoc.createElement("ANCHOR");
                
                e5 = xmldoc.createElement("UUID");
                n2 = xmldoc.createTextNode(dap.getUUID());
                e5.appendChild(n2);
                e4.appendChild(e5);
                
                e6 = xmldoc.createElement("FRIENDLY_NAME");
                n3 = xmldoc.createTextNode(dap.getFname());
                e6.appendChild(n3);
                e4.appendChild(e6);                
            
                // append it to the anchor list
                e3.appendChild(e4);
            
            }
            
            e1.appendChild(e3);
            root.appendChild(e1);
            
        }
        
        // Serialisation through Tranform.
        DOMSource domSource = new DOMSource(xmldoc);
        StreamResult streamResult = new StreamResult(out);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer;
        try {
         serializer = tf.newTransformer();
         serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
         serializer.setOutputProperty(OutputKeys.INDENT,"yes");
         serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"DiABluMap.dtd");
         serializer.transform(domSource, streamResult);
         out.flush();
         out.close();
         
        } catch (Exception e){
            
            logger.warning("Error serializing xml map:"+e.getLocalizedMessage());
            
        }

    }
    
    public void saveSettings(){
    
        logger.warning("work in progress");
        
    }
    
    
    // load the model from xml file
    public void load(File scoutMap){
   
        String dlName,dapUuid,dapFname = "";
        
   // Get the data
        try {
            
            // open the file
            logger.finest("Trying to open map file:"+scoutMap.getName());
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(scoutMap);
            logger.finer("Settings File Open.Parsing contents...");
            
            // normalize text representation
            doc.getDocumentElement().normalize();
            logger.finest("Root_element_of_the_doc_is:" + doc.getDocumentElement().getNodeName());

            NodeList landmarkList = doc.getElementsByTagName("LANDMARK");
            
            int totalLandmarks = landmarkList.getLength();
                    
            
            DiABluAnchorPoint dap = new DiABluAnchorPoint();
            
            for (int i=0;i<totalLandmarks;i++){
            
                DiABluLandmark dl = new DiABluLandmark();
                Element landmarkElement = (Element) landmarkList.item(i);
                NodeList landmarkName = landmarkElement.getElementsByTagName("NAME");
                Element nameElement = (Element) landmarkName.item(0);
                NodeList nameElementList = nameElement.getChildNodes();
                dlName = nameElementList.item(0).getNodeValue().trim();
                logger.finest("Processing landmark:"+dlName);
                dl.setName(dlName);
                
                NodeList landmarkAnchorList = landmarkElement.getElementsByTagName("ANCHOR_LIST");
                Element anchorListElement = (Element) landmarkAnchorList.item(0);                                
                if (anchorListElement.hasChildNodes()){
                    
                    
                    NodeList landmarkAnchors = anchorListElement.getElementsByTagName("ANCHOR");
                    int totalAnchors = landmarkAnchors.getLength();                                        
                    for (int j=0;j<totalAnchors;j++){
                            
                        Element landmarkAnchorElement = (Element) landmarkAnchors.item(j);
                        NodeList anchorUUID = landmarkAnchorElement.getElementsByTagName("UUID");
                        Element uuidElement = (Element) anchorUUID.item(0);
                        NodeList uuidNodeList = uuidElement.getChildNodes();
                        String uuidT = uuidNodeList.item(0).getNodeValue().trim();
                        logger.finest("UUID:"+uuidT);
                        
                        NodeList anchorFNAME = landmarkAnchorElement.getElementsByTagName("FRIENDLY_NAME");
                        Element fnameElement = (Element) anchorFNAME.item(0);
                        NodeList fnameNodeList = fnameElement.getChildNodes();
                        String fnameT = "";
                        if (fnameNodeList.item(0)!=null){
                         fnameT = fnameNodeList.item(0).getNodeValue().trim();    
                        }
                        
                        logger.finest("FNAME:"+fnameT);
                        
                        dap = new DiABluAnchorPoint(uuidT,fnameT);
                        dl.addAnchorPoint(dap);    
                    }                                                                
                    
                }
                    
               
                this.landmarkList.add(dl);
                logger.finer("Added landmark:"+dl.getName());
            
            }
            
            
         
            
        } catch (SAXParseException err) {
            
            logger.warning("**_Parsing_error" + ",_line_" + err.getLineNumber() + ",_uri_" + err.getSystemId());
            logger.warning("_" + err.getMessage());
           // defaultSettings();
            
        } catch (SAXException e) {
            
            Exception x = e.getException();
            logger.warning("SAXException:"+e.getMessage());
            ((x == null) ? e : x).printStackTrace();
            ///defaultSettings();
            
        } catch (Throwable t) {
            
            logger.warning(t.getLocalizedMessage());
            t.printStackTrace();
            //defaultSettings();
            
        }                  
        
        updateView();

    }
    
    private boolean isSafeLandmark(DiABluLandmark dl){
        
        boolean safe = false;
        if (dl == null){
            
            logger.warning("Null argument");

            
        } else {
            
            // check if there's work to do'
            if (landmarkList.size()>0){
                
                for (DiABluLandmark dl2:landmarkList){
                    
                    
                    if (dl2.compareTo(dl)==0) return false;
                    
                }
                
            } else {
                
                // current landmark list is empty we can add this one
                safe = true;
                
            }
                                    
        }
        
        return safe;

    }
}
