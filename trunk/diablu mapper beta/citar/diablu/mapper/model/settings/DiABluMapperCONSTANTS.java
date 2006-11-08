/*
 * DiABluMapperCONSTANTS.java
 *
 * Created on 9 de Setembro de 2006, 12:39
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

package citar.diablu.mapper.model.settings;

/**
 *
 * @author Nuno Rodrigues
 */
public class DiABluMapperCONSTANTS {
    
    // Log sub-system
    public static final int LOG_DETAIL_ALL        = 0;
    public static final int LOG_DETAIL_SEVERE     = 1;    
    public static final int LOG_DETAIL_WARNING    = 2;
    public static final int LOG_DETAIL_CONFIG     = 3;
    public static final int LOG_DETAIL_INFO       = 4;
    public static final int LOG_DETAIL_FINER      = 5;
    public static final int LOG_DETAIL_FINEST     = 6;
    public static final int LOG_DETAIL_OFF        = 7;
    
    public static final int LOG_OUT_OPTIONS = 3;
    
    // File sub-system
    public static final String FILE_MAP_EXTENSION = "map";          // extension of DiABlu Maps
    public static final String FILE_MAP_DESCRIPTION = "DiABlu Map"; // small file desc.
            
    /** Creates a new instance of DiABluMapperCONSTANTS */
    public DiABluMapperCONSTANTS() {
    }
    
}
