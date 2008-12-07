import processing.core.*; import netP5.*; import oscP5.*; import java.net.*; import java.io.*; import java.applet.*; import java.awt.*; import java.awt.image.*; import java.awt.event.*; import java.io.*; import java.net.*; import java.text.*; import java.util.*; import java.util.zip.*; public class FlickrBrowser extends PApplet {





OscP5 osc;
NetAddress myRemoteLocation;

OscProperties oscp;
OscP5 oscClient;
OscP5 oscServer;


int lastInteraction = millis();
boolean retry = false;

int MAX_WORDS = 100;
Vector words;

public void setup ()
{

  frameRate(1);

  oscClient = new OscP5(this, "127.0.0.1", 12000, OscP5.UDP);
  oscServer = new OscP5(this, 12001, OscP5.UDP);

  myRemoteLocation = new NetAddress("127.0.0.1", 12000);

  words = new Vector();
}


public void draw()
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


public void oscEvent(OscMessage theOscMessage)
{

  if(theOscMessage.addrPattern().equals("/Diablu/Mailman/ReceivePath"))
  {
    recievePath(theOscMessage);
  } 
  else {
    println(theOscMessage);
  }

}

public void recievePath(OscMessage theOscMessage)
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

public void showNotSupported() {
  link(sketchPath("notSupported.html"));
  
}
public void showNetError() {
  link(sketchPath("neterror.html"));
}

public String parseTxt(String filename)
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

public String parseVnt(String filename)
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

public void mousePressed() {
  println(ping());
  
}

public boolean ping() {
  
  try {
    Socket s = new Socket("jorgecardoso.eu", 80);
  } catch(Exception e) {
    return false;
  }
  return true;
  
}








	/**
	 * Encodes an already formed URL using the <CODE>application/x-www-form-urlencoded</CODE>
	 * MIME format.
	 *
	 * @param s The String (URL) to be converted.
	 *
	 * @return The converted String (URL).
	 */
	 public String URLEncode(String s) {
		int questionMark = s.indexOf('?');

		if (questionMark == -1) { // no parameters
			return s;
		} else {
			return s.substring(0, questionMark+1) + encodeURL(s.substring(questionMark+1));
		}
	}

	/**
	 * Base on code in http://forums.java.sun.com/thread.jspa?threadID=409913&messageID=1806947
	 * Converts a String to the <CODE>application/x-www-form-urlencoded</CODE> MIME
  	 * format.
  	 *
  	 * @param s The String to be converted.
  	 *
  	 * @return The converted String.
	 */
	 public String encode(String s) {
		StringBuffer out = new StringBuffer();
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();

		DataOutputStream dOut = new DataOutputStream(bOut);

		try {
			dOut.writeUTF(s);
		} catch (IOException ioe) {
			return null;
		}

		ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());

		/* UTF strings have 2 bytes to indicate the size, so skip these	*/
		bIn.read();
		bIn.read();

		int c = bIn.read();
		while (c >= 0) {
			if ((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| c == '.' || c == '-' || c == '*' || c == '_') {
				out.append((char) c);
			} else if (c == ' ') {
				out.append('+');
			} else {
				if (c < 128) {
					appendHex(c,out);
				} else if (c < 224) {
					appendHex(c,out);
					appendHex(bIn.read(),out);
				} else if (c < 240) {
					appendHex(c,out);
					appendHex(bIn.read(),out);
					appendHex(bIn.read(),out);
				}

			}
			c = bIn.read();
		}
		return out.toString();
	}

	/**
	 * Base on code in http://forums.java.sun.com/thread.jspa?threadID=409913&messageID=1806947
	 *
	 * Similar to encode but we don't encode the '=' and '&' characters. This
	 * way we can use this to encode an already formed URL (or better, the parameter
	 * part of an already formed URL).
	 */
	 public String encodeURL(String s) {
		StringBuffer out = new StringBuffer();
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();

		DataOutputStream dOut = new DataOutputStream(bOut);

		try {
			dOut.writeUTF(s);
		} catch (IOException ioe) {
			return null;
		}

		ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());

		/* UTF strings have 2 bytes to indicate the size, so skip these	*/
		bIn.read();
		bIn.read();

		int c = bIn.read();
		while (c >= 0) {
			if ((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| c == '.' || c == '-'
					|| c == '*' || c == '_'
					|| c == '=' || c == '&') {
				out.append((char) c);
			} else if (c == ' ') {
				out.append('+');
			} else {
				if (c < 128) {
					appendHex(c,out);
				} else if (c < 224) {
					appendHex(c,out);
					appendHex(bIn.read(),out);
				} else if (c < 240) {
					appendHex(c,out);
					appendHex(bIn.read(),out);
					appendHex(bIn.read(),out);
				}

			}
			c = bIn.read();
		}
		return out.toString();
	}

	  public void appendHex(int arg0, StringBuffer buff){
		buff.append('%');
		if (arg0<16) {
			buff.append('0');
		}
		buff.append(Integer.toHexString(arg0));
	}


  static public void main(String args[]) {     PApplet.main(new String[] { "FlickrBrowser" });  }}