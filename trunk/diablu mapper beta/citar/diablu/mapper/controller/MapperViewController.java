/*
 * MapperViewController.java
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
import citar.diablu.mapper.model.data.DiABluAnchorPoint;
import citar.diablu.mapper.model.data.DiABluLandmark;

/**
 *
 * @author Nuno Rodrigues
 */
public interface MapperViewController {
    
    // informs the view of the current table of detected devices
    public void setDetectedAnchorList(Vector <DiABluAnchorPoint> dapList);
    
    // sets the list of filtered device classes
   // public void setFilteredDeviceClasses(Vector <String> filterList);
    
    // informs the view of the current landmark list
    public void setLandmarkList(Vector <DiABluLandmark> landList);
    
    // informs the view of the selected landmarks anchor points
    public void setLandmarkAnchorList(Vector <DiABluAnchorPoint> dapList);
    
    // set & get automatic discovery
   // public void setAutomaticDiscovery(boolean isAutomatic);
    
    // informs the view of the current discovery delay
   //public void setDiscoveryDelay(String btDelay);
    
    // checks/unchecks the copy all option in creating landmarks
    public void setCopyAllFlag(boolean copyAll);
    
    // checks/unchecks the filtered copy option in creating landmarks
    //public void setFilteredCopy(boolean filteredCopy);
    
    // informs the view of the current log options
    public void setLogOutput(boolean[] logOut);
    
    
}
