import processing.opengl.*;
import netP5.*;
import oscP5.*;

OscP5 osc;
NetAddress myRemoteLocation;

OscProperties oscp;
OscP5 oscClient;
OscP5 oscServer;

PImage img;

String start = "Era uma vez";

String s = start;

PFont f;
int fontSize = 32;
String vlw = ".vlw";

int frameWidth = 800;
int frameHeight = 600;
int borderWidth = int(frameWidth * 0.05);
int borderHeight = int(frameHeight * 0.05);

boolean drawCursor = true;

TextBox tb;


void setup ()
{
  
  
  size(frameWidth, frameHeight);
  background(0);
  oscClient = new OscP5(this, "127.0.0.1", 12000, OscP5.UDP);
  oscServer = new OscP5(this, 12001, OscP5.UDP);

  myRemoteLocation = new NetAddress("127.0.0.1", 12000);

  frame.setUndecorated(true);
  frameRate(10);
  f = loadFont("AgencyFB-Reg-40.vlw");
  
  tb = new TextBox(borderWidth, borderHeight+ 60,frameWidth - 2*borderWidth, frameHeight - 2*borderHeight);
  tb.addContent(start);
  


}


void draw()
{
  background(0);
  fill(128);
  tb.draw();
  if((frameCount % 8) == 0)
    drawCursor = !drawCursor;
  if(drawCursor)
    tb.drawCursor();
}


void oscEvent(OscMessage theOscMessage)
{

  if(theOscMessage.addrPattern().equals("/Diablu/Mailman/ReceivePath"));
  {
    recievePath(theOscMessage);
  }

}

void recievePath(OscMessage theOscMessage)
{
  String filename = (String) theOscMessage.arguments()[3];
  String mimetype = (String) theOscMessage.arguments()[2];
  
  int[] n = new int[2];
  
  String content = s;
  
  String ext = filename;
  ext = ext.substring(ext.lastIndexOf('.') +1);

  
  if(ext.compareToIgnoreCase("txt") == 0)
      parseTxt(filename);
  if(ext.compareToIgnoreCase("vnt") == 0)
      parseVnt(filename);
}

void parseTxt(String filename)
{
  byte bytes[] = loadBytes(filename);

  String content;
  try{
    if(bytes[0] == -1 && bytes[1] == -2)
    {
      content = new String(bytes, 0, bytes.length , "UTF-16");
      }
    else{
      content = new String(bytes, 0, bytes.length , "UTF-8"); 
     }
    tb.addContent(content);
     
    } catch (UnsupportedEncodingException ex) {}
    
}  

void parseVnt(String filename)
{
  String strings[] = loadStrings(filename);

  String content;
  
  for(int i = 0; i < strings.length; i++)
  {
    if(strings[i].startsWith("BODY:"))
    {
      content = strings[i].substring(strings[i].indexOf(':')+1);
      tb.addContent(content);  
   }
  }

}  



class TextBox
{
  int x;
  int y;
  int textBoxWidth;
  int textBoxHeight;
  Vector story;
  Word[][] lines;
  int totalLines;
  float lastLineWidth;
  float lastLineHeight;
  float[] wordsPerLine = new float[30];
  
  TextBox(int x, int w, int textBoxWidth, int textBoxHeight)
  {
    this.x = x;
    this.y = w;
    this.textBoxWidth = textBoxWidth;
    this.textBoxHeight = textBoxHeight;
    story = new Vector();
    lines = new Word[30][30];
  }
  
  public void addContent(String content)
  {
     println("addContent");
    StringTokenizer st = new StringTokenizer(content);
    {
      while(st.hasMoreTokens())
      {
        story.add(new Word(st.nextToken()));
      }
    }
    distribute();
  }
  
  public void distribute()
  {
    
    float lineWidth = 0;
    float wordWidth = 0;
    int lineNum = 0;
    int linePos = 0;
    for(int i = 0; i < story.size(); i++)
    {
      Word w = (Word) story.get(i);
      textFont(w.font, w.fontSize);
      wordWidth = textWidth(w.word + " ");
      if(lineWidth + wordWidth > textBoxWidth)
      {
        
        lineNum++;
        totalLines = lineNum;
        linePos = 0;
        ((Word) story.get(i)).x = this.x;
        lineWidth = wordWidth;
      }
      else
      {
        ((Word) story.get(i)).x = lineWidth + this.x;
        
        lineWidth += wordWidth;
      }
      
      lastLineWidth = lineWidth + this.x;
      lastLineHeight = this.y + 40*(lineNum);
      ((Word) story.get(i)).y = lastLineHeight;
      
      lines[lineNum][linePos] = w;
      linePos++;
      wordsPerLine[lineNum] = linePos;
      
    }
    
  }
  
  public void draw()
  {
    for(int i = 0; i <=totalLines; i++)
    {
      for(int j = 0; j < (int) wordsPerLine[i]; j++)
      {
        textFont(lines[i][j].font, lines[i][j].fontSize);
        text(lines[i][j].word, lines[i][j].x, lines[i][j].y);
      }
    }
  }
  
  public void drawCursor()
  {
    stroke(128);
    line(lastLineWidth + 3, lastLineHeight +3, lastLineWidth + 3, lastLineHeight - 22);
  }
}

class Word
{
  String word;
  PFont font;
  int fontSize;
  
  float x;
  float y;
  
  Word(String word)
  {
    this.word = word;
    font = loadFont("AgencyFB-Reg-32.vlw");
    fontSize = 32;
  }
}
   
  
  

