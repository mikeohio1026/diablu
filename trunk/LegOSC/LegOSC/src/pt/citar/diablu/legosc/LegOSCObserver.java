/*
 * LegOSCObserver.java
 *
 * Created on 30 de Janeiro de 2007, 12:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pt.citar.diablu.legosc;

/**
 *
 * @author jccardoso
 */
public interface LegOSCObserver {
    
    public void error(String error);
    public void message(String message);
    public void verbose(String message);
}
