import processing.core.*;
import fullscreen.*;
import processing.opengl.*;
import netP5.*;
import oscP5.*;

int STORY_NUM = 1;


OscP5 osc;
NetAddress myRemoteLocation;

OscProperties oscp;
OscP5 oscClient;
OscP5 oscServer;

PImage cursorImg;
PImage titulo;

String start = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit.  Phasellus vel eros id tortor euismod mattis. Integer magna lectus, malesuada nec, lacinia eu, luctus eget, enim. Nulla quis erat. Fusce sapien. Mauris euismod scelerisque libero. Donec luctus aliquet metus. Duis sed est volutpat lacus varius dapibus. Quisque pellentesque magna ac dolor. Vivamus auctor. Vivamus lorem. Aenean massa ipsum, sollicitudin sed, euismod sed, tristique non, erat. Nulla facilisi. Nunc consequat velit sit amet neque. Maecenas bibendum sem id sem lobortis rutrum. Proin id velit. Proin ac dolor vel elit tempus luctus. Integer eu pede consequat leo porttitor tincidunt. Sed venenatis.";

String contributors[];
String serverData[] = {""};

String s = start;

PFont f;
PFont fontTextBox;
PFont fontArialBlack;
PFont fontNasa;

int saverPos = 0;

int FONT_SIZE = 52;
String vlw = ".vlw";

int frameWidth = 1280;
int frameHeight = 768;
int borderWidth = 50;
int borderHeight = 50;

boolean drawCursor = true;
boolean drawTitle = false;
TextBox tb;

String spam[];

Vector sending;

FullScreen fs;
void setup ()
{


  size(frameWidth, frameHeight);
  background(0);
  oscClient = new OscP5(this, "127.0.0.1", 12000, OscP5.UDP);
  oscServer = new OscP5(this, 12001, OscP5.UDP);

  myRemoteLocation = new NetAddress("127.0.0.1", 12000);

  //frame.setUndecorated(true);
  
  fs = new FullScreen(this);
  fs.enter();
  frameRate(1);
  f = loadFont("ArialMT-48.vlw");
  fontTextBox = loadFont("Courier10PitchBT-Bold-48.vlw");
  fontArialBlack = loadFont("Arial-Black-48.vlw");
  fontNasa = loadFont("Nasalization-48.vlw");

  tb = new TextBox(50, 150, frameWidth-50*2, frameHeight-150-50);
  tb.setFont(fontTextBox);
  tb.setFontSize(FONT_SIZE);
 // tb.addContent(start);
  loadStory();
  tb.clearColor();
  loadContributors();
  loadSpam();
  loadServer();
   // tb.addContent("Jorge Cardoso Stories in Place aha ha ah Stories in Place Jorge Cardoso Stories in Place aha ha ah Stories in Place");

  cursorImg = loadImage("bt.png");
  smooth();
  sending = new Vector();
  titulo = loadImage("st.png");
  noCursor();
}


void draw()
{
  background(0);
  fill(255);
  //if ( (frameCount%10) == 0) {
    tb.draw();
  
  
 
  textFont(f);
   // Titulo
    textSize(50);
    fill(50, 100, 200);
    String title = "Stories in Place";
    //text(title, width/2-textWidth(title)/2, 45); 
    image(titulo, width/2-titulo.width/2, 0);
    
    // Instru��es
    textFont(fontArialBlack);
    textSize(45);
    fill(255,0, 0);
    String inst = "!";
    text(inst, 50, 130);
    
   // textFont(f);
    textSize(24.6);
    fill(255);
    String inst1 = "Escreve um fich. de texto ou nota e envia para o dispositivo bluetooth 'StoriesInPlace'";
    text(inst1, 70, 130);
    
 drawContributors();
//}
 
  if((frameCount % 1) == 0)
    drawCursor = !drawCursor;
  if(drawCursor)
    tb.drawCursor();
    
 if ((frameCount %5) == 0) {
   sender();
   }
   
 saverPos++;
 saverPos %= width;
 stroke(0);
 line(saverPos, 0, saverPos, height);
}

void drawContributors() {
  
  textFont(fontArialBlack);
  textSize(23.5);
  int x = width-50;
  String sep = "|";
  float sepWidth = textWidth(sep);
  for (int i = contributors.length-1; i >= 0; i--) {
    float l = textWidth(contributors[i]);
    x -= l;
    if (i == contributors.length-1) {
      fill(255, 0, 0);
    } else {
      fill(255);
    }
    text(contributors[i], x, height-15);
    x -= sepWidth;
   fill(50, 100, 200);
  
    //rect(x, height-15, 50, 10);
    text(sep, x, height-15);
  }
  /*
  if (textWidth(contributors) > width-50) {
    text(contributors, -(textWidth(contributors)-width+50), height-15);
    fill(0);
    stroke(0);
    rect(0, height-50, 50, 50);
  } else {
    text(contributors, 10, height-15);
  }*/
  fill(0);
  stroke(0);
  rect(0, height-50, 50, 50);
}


void oscEvent(OscMessage theOscMessage)
{

  if(theOscMessage.addrPattern().equals("/Diablu/Mailman/ReceivePath"))
  {
    recievePath(theOscMessage);
  } 
  else {
    println(theOscMessage);
  }

}

void recievePath(OscMessage theOscMessage)
{
  String uuid = (String) theOscMessage.arguments()[0];
  String fname = (String) theOscMessage.arguments()[1];
  String originalFilename = (String) theOscMessage.arguments()[2];
  String filename = (String) theOscMessage.arguments()[4];
  String mimetype = (String) theOscMessage.arguments()[3];

  println(uuid + " " + fname + " " +filename + " "+ mimetype);

  int[] n = new int[2];

  //String content = s;

  String ext = filename;
  ext = ext.substring(ext.lastIndexOf('.') +1);

  String content = "";
  if(ext.compareToIgnoreCase("txt") == 0) {

    content = parseTxt(filename);

  } 
  else if(ext.compareToIgnoreCase("vnt") == 0) {

    content = parseVnt(filename);
  } 
  else { //not supported
    sending.add(new Sending(uuid, "notsupported.txt"));
    /*OscMessage message = new OscMessage("/Diablu/Mailman/SendPath",   new Object[] {
      sketchPath("data/notsupported.txt"), uuid     }
    );
    oscClient.send(message, myRemoteLocation);*/
    //println("sent Response");
    return;
  }

  if (content.length() > 0) {
    // check commands
    if (content.toLowerCase().indexOf("cmd:fontsize:-")>=0) {
      tb.decreaseFont();
      tb.distribute();
    }
    else if (content.toLowerCase().indexOf("cmd:fontsize:+")>=0) {
      tb.increaseFont();
      tb.distribute();
    } else if (content.toLowerCase().indexOf("cmd:spam:")>=0) {
      int s1 = content.indexOf("cmd:spam:");
      int s2 = content.indexOf(":", s1+10);
      
      String sWord = content.substring(s1+10, s2);

      tb.addSpam(sWord);
      saveSpam();
    } 
    else {
      contributors = expand(contributors, contributors.length+1);
      contributors[contributors.length-1] =  fname + " (" + hour() + ":" + minute() + ")";
      saveContributors();
      tb.addContent(content);  
      String server = "http://jorgecardoso.eu/DiABlu/StoriesInPlace/add.php?story_num="+ STORY_NUM + "&contribution="+content+"&friendly_name="+fname+"&uuid="+uuid;
      
      
      server = URLEncode(server);
      
      serverData = append(serverData, server);
      saveServer();
      println(server);
      try {
        loadStrings(server);
      } catch (Exception e) {
        println(e.getMessage());
      }
      saveStory();
      // send story back
      sending.add(new Sending(uuid, "StoriesInPlace.txt"));
    }
  }
}

String parseTxt(String filename)
{
  byte bytes[] = loadBytes(filename);
//println(bytes);
  String content;
  try{
    if(bytes[0] == -1 && bytes[1] == -2)
    {
      content = new String(bytes, 0, bytes.length , "UTF-16");
    }
    else{
      content = new String(bytes, 0, bytes.length , "ISO-8859-1"); 
    }
    //tb.addContent(content);
    return content;

  } 
  catch (UnsupportedEncodingException ex) {

  }
  return "";

}  

String parseVnt(String filename)
{
  byte bytes[] = loadBytes(filename);
  //println(bytes);
  String content="";
  String body  = "";
  try{
    if(bytes[0] == -1 && bytes[1] == -2)
    {
      content = new String(bytes, 0, bytes.length , "UTF-16");
    }
    else{
      content = new String(bytes, 0, bytes.length , "ISO-8859-1"); 
    }
    //tb.addContent(content);
  

  } 
  catch (UnsupportedEncodingException ex) {
    println(ex.getMessage());
  }

 
 int start = content.toUpperCase().indexOf("BODY");
 if(start < 0)
   return "";
 content = content.substring(start);
 int dd = content.toUpperCase().indexOf(":");
 if(dd < 0)
   return "";
 content = content.substring(dd+1);
 int nl = content.toUpperCase().indexOf("\n");
 if(nl < 0)
   return "";
 content = content.substring(0, nl);
 
 while(content.toUpperCase().indexOf("=0A") != -1)
 {
   body += content.substring(0, content.toUpperCase().indexOf("=0A")) + " ";
   content = content.substring(content.toUpperCase().indexOf("=0A")+3);
 }
 body += content;
 

  return  body;

}  


public void saveContributors() {
 saveStrings("contributors.txt",  contributors);
  
}
public void loadContributors() {
  String s[] = loadStrings("contributors.txt");
  contributors = s;
}
public void saveStory() {
  saveStrings("StoriesInPlace.txt",  new String[] {tb.getStory()});
  print ("Saved story");
}

public void loadStory() {
  String s[] = loadStrings("StoriesInPlace.txt");
  tb.addContent(s[0]);
}

public void saveSpam() {
  saveStrings("spam.txt", tb.getSpam());
}

public void loadSpam() {
  spam = loadStrings("spam.txt");
  
  if (spam !=null ) {
    for (int i = 0; i < spam.length; i++) {
      
      tb.addSpam(spam[i]);
    }
  }
//  println(tb.getSpam());
}

void saveServer() {
  saveStrings("server.txt", serverData);
}

void loadServer() {
  serverData = new String[] {""};
  try {
    serverData = loadStrings("server.txt");
  } catch (Exception e) {}
}

public void sender() {
  
  if (sending.size() > 0) {
   
  Sending s = (Sending) sending.elementAt(0);
  sending.remove(0);
   println("Sending: " + s.uuid + " "+ s.file);
  OscMessage message = new OscMessage("/Diablu/Mailman/SendPath",   new Object[] {
      sketchPath(s.file), s.uuid     }
    );
    oscClient.send(message, myRemoteLocation);
  }
}

