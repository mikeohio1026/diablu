
import oscP5.*;
import netP5.*;



OscP5 oscP5;
int receiveAtPort;
int sendToPort;
String host;
String oscP5event;

void initOsc() {
  receiveAtPort = 10000; // DiABlu default port
  sendToPort = 57120; // not needed in this example
  host = "127.0.0.1";
  oscP5event = "oscEvent";
  oscP5 = new OscP5(this, host, sendToPort, receiveAtPort, oscP5event);
}


void oscEvent(OscIn oscIn) {
  if(oscIn.checkAddrPattern("/DeviceCount")) {
        
      int deviceCount = oscIn.getInt(0);
      println("/DeviceCount " + deviceCount);
    
  } 
  else if(oscIn.checkAddrPattern("/DeviceIn")) {
        
      String uuid = oscIn.getString(0);
      String friendlyName = oscIn.getString(1);
      println("/DeviceIn " + uuid + " " + friendlyName + " " + oscIn.getString(2) + " " + oscIn.getString(3) + " " + oscIn.getString(4));
    
  }   
  else if(oscIn.checkAddrPattern("/DeviceListIn")) {
    println("/DeviceListIn");
    Object[] o = oscIn.getData();
    for(int i=0; i < o.length; i +=5) {
      String uuid = (String)o[i];
      String friendlyName = (String)o[i+1];
      println("\t" + uuid + " " + friendlyName + " " + (String)o[i+2] + " " + (String)o[i+3] + " " + (String)o[i+4]);    
    }
  } 
  else if(oscIn.checkAddrPattern("/DeviceOut")) {
   // if(oscIn.checkTypetag("ss")) {         
      String uuid = oscIn.getString(0);
      String friendlyName = oscIn.getString(1);
      println("/DeviceOut " + uuid + " " + friendlyName + " " + oscIn.getString(2) + " " + oscIn.getString(3) + " " + oscIn.getString(4));
   // }
  }   
  else if(oscIn.checkAddrPattern("/DeviceListOut")) {
    println("/DeviceListOut");
    Object[] o = oscIn.getData();
    for(int i=0; i < o.length; i +=5) {
      String uuid = (String)o[i];
      String friendlyName = (String)o[i+1];
      println("\t" + uuid + " " + friendlyName + " " + (String)o[i+2] + " " + (String)o[i+3] + " " + (String)o[i+4]);    
    }
  }
  else if(oscIn.checkAddrPattern("/DeviceList")) {
    println("/DeviceList");
    Object[] o = oscIn.getData();
    for(int i=0; i < o.length; i +=5) {
      String uuid = (String)o[i];
      String friendlyName = (String)o[i+1];
      println("\t" + uuid + " " + friendlyName + " " + (String)o[i+2] + " " + (String)o[i+3] +" " + (String)o[i+4]);    
    }
  }
  else if(oscIn.checkAddrPattern("/NameChanged")) {
    //if(oscIn.checkTypetag("ss")) {         
      String uuid = oscIn.getString(0);
      String friendlyName = oscIn.getString(1);
      println("/NameChanged " + uuid + " " + friendlyName + " " + oscIn.getString(2) + " " + oscIn.getString(3) + " " + oscIn.getString(4));
   // }
  }
  else if(oscIn.checkAddrPattern("/KeyIn")) {
    if(oscIn.checkTypetag("ssss")) {         
      String uuid = oscIn.getString(0);
      String friendlyName = oscIn.getString(1);
      String keyPressed = oscIn.getString(2);
      String gameAction = oscIn.getString(3);
      println("/KeyIn " + uuid + " " + friendlyName + " " + keyPressed + " " + gameAction);
    }
  }   
  else if(oscIn.checkAddrPattern("/MessageIn")) {
    if(oscIn.checkTypetag("sss")) {         
      String uuid = oscIn.getString(0);
      String friendlyName = oscIn.getString(1);
      String message = oscIn.getString(2);

      println("/MessageIn " + uuid + " " + friendlyName + " " + message);
    }
  }
  /* else {
   println("you have received an osc message "+
   oscIn.getAddrPattern()+"   "+oscIn.getTypetag()
   );
   Object[] o = oscIn.getData();
   for(int i=0;i<o.length;i++) {
   println(i+"  "+o[i]);
   }
   }*/
}

void setup(){
  initOsc();
}


void draw() {
}


