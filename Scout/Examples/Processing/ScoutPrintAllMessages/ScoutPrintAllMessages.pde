import oscP5.*;
import netP5.*;

OscP5 oscP5;

void setup(){
  /* listen on port 10000 (scout sends osc to this port by default) */
  oscP5 = new OscP5(this, 10000);
}

void draw() {
}


void oscEvent(OscIn oscIn) {
  if(oscIn.checkAddrPattern("/DeviceCount")) {
    println("/DeviceCount " + oscIn.getInt(0));
  } 
  else if(oscIn.checkAddrPattern("/DeviceIn")) {
    println("/DeviceIn " + oscIn.getString(0) + " " + oscIn.getString(1) + " " +
      oscIn.getString(2) + " " + oscIn.getString(3) + " " + oscIn.getString(4));
  }   
  else if(oscIn.checkAddrPattern("/DeviceListIn")) {
    println("/DeviceListIn");
    Object[] o = oscIn.getData();
    for(int i=0; i < o.length; i +=5) {
      println("\t" + (String)o[i] + " " + (String)o[i+1] + " " + (String)o[i+2] + " " + 
        (String)o[i+3] + " " + (String)o[i+4]);    
    }
  } 
  else if(oscIn.checkAddrPattern("/DeviceOut")) {
    println("/DeviceOut " + oscIn.getString(0) + " " + oscIn.getString(1) + " " + 
      oscIn.getString(2) + " " + oscIn.getString(3) + " " + oscIn.getString(4));
  }   
  else if(oscIn.checkAddrPattern("/DeviceListOut")) {
    println("/DeviceListOut");
    Object[] o = oscIn.getData();
    for(int i=0; i < o.length; i +=5) {
      println("\t" + (String)o[i] + " " + (String)o[i+1] + " " + (String)o[i+2] + " " + 
        (String)o[i+3] + " " + (String)o[i+4]);    
    }
  }
  else if(oscIn.checkAddrPattern("/DeviceList")) {
    println("/DeviceList");
    Object[] o = oscIn.getData();
    for(int i=0; i < o.length; i +=5) {
      println("\t" + (String)o[i] + " " + (String)o[i+1] + " " + (String)o[i+2] + " " + 
        (String)o[i+3] +" " + (String)o[i+4]);    
    }
  }
  else if(oscIn.checkAddrPattern("/NameChanged")) {
    println("/NameChanged " + oscIn.getString(0) + " " + oscIn.getString(1) + " " + 
      oscIn.getString(2) + " " + oscIn.getString(3) + " " + oscIn.getString(4));
  }
}







