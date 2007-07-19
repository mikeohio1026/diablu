import oscP5.*;
import netP5.*;

OscP5 oscP5;
NetAddress myRemoteLocation;
PFont font;

int lightLevel;
int soundLevel;
int buttonState;
int proximityLevel;


void setup() {
  size(400,400);
  frameRate(15);

  oscP5 = new OscP5(this, 20000);

  myRemoteLocation = new NetAddress("localhost", 10000);
  
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

void mousePressed() {
  OscMessage myMessage;
  if (mouseButton == LEFT) {
    /* in the following different ways of creating osc messages are shown by example */
    myMessage = new OscMessage("/motorForward");

    myMessage.add((int)random(0, 3)); /* add an int to the osc message */
    myMessage.add((int)random(0, 100)); /* add a float to the osc message */


    /* send the message */

  } 
  else {
    myMessage = new OscMessage("/motorSlowStop");

    myMessage.add((int)random(0, 3)); /* add an int to the osc message */

  }
  oscP5.send(myMessage, myRemoteLocation); 
  println("sending message");
}

void keyPressed() {
  if (key == 'b') {
    OscMessage myMessage;

    myMessage = new OscMessage("/getButtonState");

    myMessage.add(0); /* add an int to the osc message */

    oscP5.send(myMessage, myRemoteLocation);     
  }
}



void oscEvent(OscMessage msg) {
  if (msg.checkAddrPattern("/lightLevel")) {
    lightLevel = msg.get(1).intValue();
     println("Light Level: " + lightLevel);
    
  } else if (msg.checkAddrPattern("/soundLevel")) {
    soundLevel = msg.get(1).intValue();
     println("Sound Level: " + soundLevel);
  }  else if (msg.checkAddrPattern("/proximityLevel")) {
    proximityLevel = msg.get(1).intValue();
     println("Proximity Level: " + proximityLevel);
  }  else if (msg.checkAddrPattern("/buttonState")) {
    buttonState = msg.get(1).intValue();
     println("Button State: " + buttonState);
  }
}
