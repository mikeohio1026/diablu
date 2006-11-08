/*
 * DiABluMapperFileFilter.java
 *
 * Created on 10 de Setembro de 2006, 14:41
 *
 * Copyright (C) 10 de Setembro de 2006 Nuno Rodrigues
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

package citar.diablu.mapper.model.settings;

import static citar.diablu.mapper.model.settings.DiABluMapperCONSTANTS.*;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluMapperFileFilter extends FileFilter {
    
    private String filename = "."+FILE_MAP_EXTENSION;
    private int filenameSize = filename.length();
    
    /** Creates a new instance of DiABluMapperFileFilter */
    public DiABluMapperFileFilter()  {
        
    }
        public String getDescription(){
            
            return "DiABlu Mapper Files";
            
        }
        
        public boolean accept(File file){
            
            filename = file.getName();
            filenameSize = filename.length();
            
            if (filename.substring(filenameSize-3,filenameSize).equalsIgnoreCase(FILE_MAP_EXTENSION)) return true;
            
            return false;
            
        }
        
        
        
        
        
        
  
    
}
