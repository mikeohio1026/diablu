
package pt.citar.diablu.sms2osc.bluetooth;

import pt.citar.diablu.sms2osc.S2O;

public class S2OBTConnectionTimeout implements Runnable{
    
    int timeout;
    boolean interrupted;
    boolean connected;
    Thread btConnect;
    S2O s2o;
    
    public S2OBTConnectionTimeout(S2O s2o, int timeout)
    {
        this.timeout = timeout;
        this.s2o = s2o;
        this.connected = false;
    }

    public void run() {
        this.connected = false;
        System.out.println("timeout");
        try {
            Thread.sleep(timeout * 1000);
            if(!interrupted)
            {
                s2o.getBtConnectionThread().interrupt();
             }
             s2o.getConnectionPopUpGui().connectionFailed();
        } catch (InterruptedException ex) {
            //Logger.getLogger(S2OBTConnectionTimeout.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("timeout stopped");
            if(connected)
                s2o.getConnectionPopUpGui().connected();
            else
                s2o.getConnectionPopUpGui().connectionFailed();

            
            
        }

    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }


    
    
    

}
