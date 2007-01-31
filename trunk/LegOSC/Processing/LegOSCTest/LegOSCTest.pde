/**
 * oscP5message by andreas schlegel
 * example shows how to create osc messages.
 * oscP5 website at http://www.sojamo.de/oscP5
 */

import oscP5.*;
import netP5.*;

OscP5 oscP5;
NetAddress myRemoteLocation;

void setup() {
  size(400,400);
  frameRate(25);
  /* start oscP5, listening for incoming messages at port 12000 */
  oscP5 = new OscP5(this, 20000);

  /* myRemoteLocation is a NetAddress. a NetAddress takes 2 parameters,
   * an ip address and a port number. myRemoteLocation is used as parameter in
   * oscP5.send() when sending osc packets to another computer, device, 
   * application. usage see below. for testing purposes the listening port
   * and the port of the remote location address are the same, hence you will
   * send messages back to this sketch.
   */
  myRemoteLocation = new NetAddress("crpfa117a", 10000);
}


void draw() {
  background(0);  
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


/* incoming osc message are forwarded to the oscEvent method. */
void oscEvent(OscMessage theOscMessage) {
  /* print the address pattern and the typetag of the received OscMessage */
  print("### received an osc message.");
  print(" addrpattern: "+theOscMessage.addrPattern());
  println(" typetag: "+theOscMessage.typetag());
}
