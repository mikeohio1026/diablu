/*
 * MapperModelViewController.java
 *
 * Created on 9 de Setembro de 2006, 15:04
 *
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

package citar.diablu.mapper.controller;

import java.util.Vector;
import java.io.File;
import citar.diablu.mapper.model.data.DiABluAnchorPoint;

/**
 * This interface documents all of the model methods called in by the view
 * it acts as controller
 *
 * @author Nuno Rodrigues
 */
public interface MapperModelViewController {
    
    // save the map model to a xml file
    public void save(File file);
    
    // load the map model from xml file
    public void load(File file);
    
    // perform a manual discovery as soon as possible
    public void manualSearch();
    
    // sets/unsets discovery engine to/from automatic
    public void setAutomaticSearch(boolean state);
    
    // sets the current bt delay (needed in automatic mode)
    public void setDiscoveryDelay(String btDelay);
    
    // creates a landmark based on the list of anchor points
    public void createLandmark(String name,Vector <DiABluAnchorPoint> anchorList);
    
    // landmark creation settings
    public void setCopyAllFlag(boolean copyAll);
    
    public void setFilteredCopy(boolean copyOnly);
    
    // sets the list of filtered device classes
    public void setFilteredDeviceClasses(Vector <String> filterList);
    
    // reset the landmark info - clears all the anchor points
    public void resetLandmark(String name);
    
    // deletes the landmark
    public void deleteLandmark(String name);
    
    // adds the current selected anchor points to the selected landmark
    public void addToLandmark(String landmarkName, Vector <DiABluAnchorPoint> dapList);
    
    // removes the current selected anchor points to the selected landmark
    public void removeFromLandmark(String landmarkName, Vector <DiABluAnchorPoint> dapList);
    
    // informs the model of the current selected landmark
    public void selectedLandmark(String name);
    
    // set the preferred language
    public void setLanguage(String langue);
        
    // informs of the desired log detail
    public void setLogDetail(int detailCode);
    
    // informs of the current log output options
    public void setLogOutput(boolean[] logOut);
    
    // exit the application
    public void exit(boolean save);
    
    // show the help window
    public void help();
    
    
}
