/*
 * MapperHelpController.java
 *
 * Created on 12 de Setembro de 2006, 17:59
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

/**
 *
 * @author Nuno Rodrigues
 */
public interface MapperHelpController {
    
    // provides the topic for more detailed help
    public void helpTopic(int topic);
    
    // set's the help frame visibility'
    public void show(boolean isVisible);
    
}
