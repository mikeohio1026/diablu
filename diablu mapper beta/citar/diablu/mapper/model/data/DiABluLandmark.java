/*
 * DiABluLandmark.java
 *
 * Created on 9 de Setembro de 2006, 17:17
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

package citar.diablu.mapper.model.data;

import java.util.Vector;
import citar.diablu.mapper.model.data.DiABluAnchorPoint;

/**
 * This class represents the area where the listed anchor points are all discoverable 
 * at the same time
 * 
 * @author Nuno Rodrigues
 */
public class DiABluLandmark implements Comparable {
    
    private String name;
    private Vector <DiABluAnchorPoint> anchorList;
    
    /** Creates a new instance of DiABluLandmark */
    public DiABluLandmark() {
        
        name = "[none yet]";
        anchorList = new Vector <DiABluAnchorPoint> ();
        
    }
    
    public DiABluLandmark(String name){
        
        this.name = name;
        anchorList = new Vector <DiABluAnchorPoint> ();
        
    }
    
    public DiABluLandmark(String name, Vector <DiABluAnchorPoint> anchorList){
        
        this.name = name;
        this.anchorList = anchorList;
        
    }
    
    public void setName(String name){
        
        this.name = name;
    }
    
    public String getName(){
        
        return this.name;
        
    }
    
    public void setAnchorList(Vector <DiABluAnchorPoint> anchorList){
        
        this.anchorList = anchorList;
        
    }
    
    public Vector <DiABluAnchorPoint> getAnchorList(){
        
        return this.anchorList;        
        
    }
    
    public void addAnchorPoint(DiABluAnchorPoint dap){
       
        // make sure the anchor isn't already present'
        if (isAnchorPresent(dap)) return;
        
        anchorList.addElement(dap);
    
    }
    
    public boolean removeAnchorPoint(DiABluAnchorPoint dap){
            
        return anchorList.removeElement(dap);
           
    }

    /**
     *  This method checks if a given Anchor Point is already
     *  present in the anchor list.
     *  The landmark doesn't supports multiple entrys of the same anchor point
     *
     */
    private boolean isAnchorPresent(DiABluAnchorPoint dap){
        
        for (DiABluAnchorPoint dap2:this.anchorList){
            
            if (dap2.compareTo(dap)==0){
                
                return true;
            
            }
            
            
        }
        return false;
        
    } 
    
    /**
     * This method implements the Comparable interface and compares 
     * this Landmark anchor point list with the given one. Landmarks match when have the same anchor lists.
     *
     * NOTE: That this does not respect the Comparable interface except 
     * in the equal value. 1 should mean greater and -1 smaller.
     *
     * Output:
     * -1 - It's a diferent device (don't match)
     * 0  - It's equal (match)

     *
     */
    public int compareTo(Object anotherDiABluLandmark) throws ClassCastException {                  
    
        // first:make sure it's a DiABlu Device to compare with'
        if (!(anotherDiABluLandmark instanceof DiABluLandmark)) {
                    
             throw new ClassCastException("DiABlu Landmark expected");  
             
        } 
       
        DiABluLandmark adl = (DiABluLandmark) anotherDiABluLandmark;
        boolean foundCorresponding = false;
        
        // second:compare the anchor lists
        
        // basics: lists size
        if (this.anchorList.size()!=adl.getAnchorList().size()){
            
            // diferent size list make diferent landmarks
            return 0;
            
        }
        
        // same size lists,let's check the elements'
        for (DiABluAnchorPoint da:this.anchorList){
            
            
            
            for (DiABluAnchorPoint da2:adl.getAnchorList()){
              
                if (da.compareTo(da2)==0){
                    
                    // found a match
                    foundCorresponding = true;
                    break;
                    
                }
                
            }
            
            if (!foundCorresponding){
                
                // these landmarks have at least a diferent anchor point
                return -1;
                
            } else {
                
                foundCorresponding = false;
                
            }
            
                        
        }
        
        // if we've make it all this far they must be equal'
        return 0;
    }
}
