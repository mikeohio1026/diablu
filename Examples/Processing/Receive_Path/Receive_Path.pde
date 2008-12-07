import netP5.*;
import oscP5.*;

OscP5 oscServer;

void setup() 
{
  
  // Open a port for incoming OSC messges
  oscServer = new OscP5(this, 12001, OscP5.UDP);
}

void draw() {}

void oscEvent(OscMessage theOscMessage)
{
  //listen for desired messages
  if(theOscMessage.addrPattern().compareTo("/Diablu/Mailman/ReceivePath") == 0)
  {
    String device = (String) theOscMessage.arguments()[0];
    String friendlyName = (String) theOscMessage.arguments()[1];
    String filename = (String) theOscMessage.arguments()[2];
    String mimetype = (String) theOscMessage.arguments()[3];
    String path = (String) theOscMessage.arguments()[4];
    
    println("Received      - " + theOscMessage.addrPattern());
    println("Device        - " + device);
    println("Friendly Name - " + friendlyName);
    println("Filename      - " + filename);
    println("Mimetype      - " + mimetype);
    println("Filepath      - " + path);
  }
}
