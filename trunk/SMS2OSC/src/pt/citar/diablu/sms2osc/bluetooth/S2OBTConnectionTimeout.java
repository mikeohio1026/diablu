package pt.citar.diablu.sms2osc.bluetooth;

import pt.citar.diablu.sms2osc.S2O;

public class S2OBTConnectionTimeout implements Runnable {

    int timeout;
    boolean interrupted;
    Thread btConnect;
    S2O s2o;

    public S2OBTConnectionTimeout(S2O s2o, int timeout) {
        this.timeout = timeout;
        this.s2o = s2o;

    }

    public void run() {

        
        try {
            Thread.sleep(timeout * 1000);
            if (!interrupted) {
                s2o.getBtConnectionThread().interrupt();
            }
        } catch (InterruptedException ex) {
        }
    }
}

   
    
    


