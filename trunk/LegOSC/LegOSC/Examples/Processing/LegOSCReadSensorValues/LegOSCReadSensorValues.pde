import oscP5.*;
import netP5.*;

OscP5 oscP5;
NetAddress myRemoteLocation;
PFont font;

/* the current sensor values */
int lightLevel;
int soundLevel;
int buttonState;
int proximityLevel;


void setup() {
  size(400,400);
  frameRate(15);

  oscP5 = new OscP5(this, 20000);

  /* the address of LegOSC. Change this to match your computer name  */
  myRemoteLocation = new NetAddress("crpfa117l.ucpcrp.pt", 10000);

  font = loadFont("ArialNarrow-24.vlw");
  textFont(font);
}


void draw() {
  background(0);  

  textAlign(CENTER);
  textMode(SCREEN);



  // draw light level;
  fill(255);
  rect(10, height, 60, -lightLevel*2);
  text("Light: " + lightLevel, 40, height-lightLevel*2);

  // draw sound level;
  fill(255, 0, 0);
  rect( 90, height, 60, -soundLevel*2);
  text("Sound: " + soundLevel, 120, height-soundLevel*2);

  // draw proximity
  fill(0, 255, 0);
  rect(170, height, 60, -proximityLevel*2);
  text("Proximity: " + proximityLevel, 200, height-proximityLevel*2);

  // draw button state
  fill(0, 0, 255);
  rect(250, height, 60, -buttonState*200);
  text("Button: " + buttonState, 280, height-buttonState*200);
}

/* Pressing the mouse makes it ask LegOSC for current sensor values */
void mousePressed() {
  OscMessage msg;

  msg = new OscMessage("/getLightLevel");
  msg.add(0); // get light sensor on port 1
  oscP5.send(msg, myRemoteLocation);


  msg = new OscMessage("/getSoundLevel");
  msg.add(1);  // get sound sensor on port 2
  oscP5.send(msg, myRemoteLocation);


  msg = new OscMessage("/getProximityLevel");
  msg.add(2); // get proximity sensor on port 3
  oscP5.send(msg, myRemoteLocation); 


  msg = new OscMessage("/getButtonState");
  msg.add(3); // get button sensor on port 4
  oscP5.send(msg, myRemoteLocation);   
}

/* here we receive the response from LegOSC */
void oscEvent(OscMessage msg) {
  if (msg.checkAddrPattern("/lightLevel")) {
    lightLevel = msg.get(1).intValue();
    println("Light Level: " + lightLevel);

  } 
  else if (msg.checkAddrPattern("/soundLevel")) {
    soundLevel = msg.get(1).intValue();
    println("Sound Level: " + soundLevel);
  }  
  else if (msg.checkAddrPattern("/proximityLevel")) {
    proximityLevel = msg.get(1).intValue();
    println("Proximity Level: " + proximityLevel);
  }  
  else if (msg.checkAddrPattern("/buttonState")) {
    buttonState = msg.get(1).intValue();
    println("Button State: " + buttonState);
  }
}
