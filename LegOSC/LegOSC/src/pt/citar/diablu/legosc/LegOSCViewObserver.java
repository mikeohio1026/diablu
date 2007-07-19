/*
 * LegOSCViewObserver.java
 *
 * Created on 19 de Julho de 2007, 10:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pt.citar.diablu.legosc;

/**
 *
 * @author Jorge Cardoso
 */
public interface LegOSCViewObserver {
    public void configChanged(String legOSCPort, String appHostname, String appPort, String brickCOM, boolean autoSensor);
    public void sensorMapChanged(String []sensorType);
    public void startStop();
}
