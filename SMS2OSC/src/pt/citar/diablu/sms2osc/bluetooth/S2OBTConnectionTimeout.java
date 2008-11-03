
package pt.citar.diablu.sms2osc.bluetooth;

import pt.citar.diablu.sms2osc.S2O;

public class S2OBTConnectionTimeout implements Runnable{
    
    int timeout;
    boolean interrupted;
    Thread btConnect;
    S2O s2o;
    
    public S2OBTConnectionTimeout(S2O s2o, Thread btConnect, int timeout)
    {
        this.timeout = timeout;
        this.btConnect = btConnect;
        this.s2o = s2o;
    }

    public void run() {
        
        System.out.println("timeout");
        try {
            Thread.sleep(timeout * 1000);
            if(!interrupted)
            {
                btConnect.interrupt();
                
                
                
            }
        } catch (InterruptedException ex) {
            //Logger.getLogger(S2OBTConnectionTimeout.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("timeout stopped");
        }
    }
    
    
    

}
