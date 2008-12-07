
import netP5.*;
import oscP5.*;
import java.net.*;


OscP5 osc;
NetAddress myRemoteLocation;

OscProperties oscp;
OscP5 oscClient;
OscP5 oscServer;


int lastInteraction = millis();
boolean retry = false;

int MAX_WORDS = 100;
Vector words;

void setup ()
{

  frameRate(1);

  oscClient = new OscP5(this, "127.0.0.1", 12000, OscP5.UDP);
  oscServer = new OscP5(this, 12001, OscP5.UDP);

  myRemoteLocation = new NetAddress("127.0.0.1", 12000);

  words = new Vector();
}


void draw()
{
  if (retry) {
    if (millis() > lastInteraction+10000) {
      String c = (String)words.get(words.size()-1);
      //println(year() + "-" + month() + "-" + day() + "@" + hour() + ":" +minute()+":" +second() + " ---- Using friendly name "+c);
      lastInteraction=millis();
      if (ping()) {
        link(URLEncode("http://www.flickr.com/search/show/?q=\""+c+"\"")); 
        retry = false;
      } else {
         showNetError();
      }
      
    }
  }
  if (millis() > lastInteraction+1000*60*30) {
    if (words.size() > 0) {
      String c = (String)words.get((int)random(words.size()));
      println(year() + "-" + month() + "-" + day() + "@" + hour() + ":" +minute()+":" +second() + " ---- Reusing old tag "+c);
      
      if (ping()) {
        lastInteraction=millis();
        link(URLEncode("http://www.flickr.com/search/show/?q=\""+c+"\"")); 
      } else {
         showNetError();
        lastInteraction=lastInteraction+10*1000; // retry 10 sec later
      }
    }
  }
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
  retry = false;
  String uuid = (String) theOscMessage.arguments()[0];
  String fname = (String) theOscMessage.arguments()[1];
  String originalFilename = (String) theOscMessage.arguments()[2];
  String filename = (String) theOscMessage.arguments()[4];
  String mimetype = (String) theOscMessage.arguments()[3];

  // println(uuid + " " + fname + " " +filename + " "+ mimetype);

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
    lastInteraction=millis();
    words.add(fname);
    if (words.size() > MAX_WORDS) {
      words.remove(0);
    }
     println(year() + "-" + month() + "-" + day() + "@" + hour() + ":" +minute()+":" +second() + " fname " + uuid + " "+ fname + " " + originalFilename + " " + filename + " " + mimetype + " " + fname);
 
    
    showNotSupported();
    retry = true; // show error, then retry last word
    return;
  }

  if (content.length() > 0) {
    lastInteraction=millis();
    words.add(content);
    if (words.size() > MAX_WORDS) {
      words.remove(0);
    }
    println(year() + "-" + month() + "-" + day() + "@" + hour() + ":" +minute()+":" +second() + " content " +  uuid + " "+ fname + " " + originalFilename + " " + filename + " " + mimetype + " " + content);
    if (ping()) {
      link(URLEncode("http://www.flickr.com/search/show/?q=\""+content+"\"")); 
    } else {
      showNetError();
      retry = true;
    }

  }
}

void showNotSupported() {
  link(sketchPath("notSupported.html"));
  
}
void showNetError() {
  link(sketchPath("neterror.html"));
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

void mousePressed() {
  println(ping());
  
}

boolean ping() {
  
  try {
    Socket s = new Socket("jorgecardoso.eu", 80);
  } catch(Exception e) {
    return false;
  }
  return true;
  
}



