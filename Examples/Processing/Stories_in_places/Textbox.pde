

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

  PFont font;
  int fontSize;
  
   private String spam[] = {"merda"};
  
  TextBox(int x, int w, int textBoxWidth, int textBoxHeight)
  {
    this.x = x;
    this.y = w;
    this.textBoxWidth = textBoxWidth;
    this.textBoxHeight = textBoxHeight;
    story = new Vector();
    
  }
  public void clearColor() {
        for (int i = 0; i < story.size(); i++) {
          Word w = (Word)story.get(i);
      w.col = #ffffff;
    }
  }
  
  public String[] getSpam() {
    return spam;
  }
  
  public void addSpam(String s) {
      spam = expand(spam, spam.length+1);
      spam[spam.length-1] = s;
      
   for (int i = 0; i < story.size(); i++) {
      Word w = (Word)story.get(i);
      w.processSpam();
    }      
  }
  
  public void setFont(PFont font) {
    this.font = font;
  }
  
  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
    updateWordFontSize();
  }
  
  public int getFontSize() {
    return this.fontSize;
  }
  
    public void increaseFont() {
    this.fontSize+=2;
    updateWordFontSize();
  }
  public void decreaseFont() {
    this.fontSize-=2;
    updateWordFontSize();
  }
  
  private void updateWordFontSize() {
    for (int i = 0; i < story.size(); i++) {
      Word w = (Word)story.get(i);
      w.setFontSize(this.fontSize);
    }
  }
  
  public void addContent(String content)
  {
    println("addContent " + content);

    for (int i = 0; i < story.size(); i++) {
      Word w = (Word)story.get(i);
      w.col = #ffffff;
    }

    StringTokenizer st = new StringTokenizer(content);
    {
      while(st.hasMoreTokens())
      {
        Word w = new Word(this, st.nextToken(), this.font, this.fontSize);
        w.setColor(#ff0000);
        story.add(w );
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
    
    lines = new Word[40][40];
    for(int i = 0; i < story.size(); i++)
    {
      Word w = (Word) story.get(i);
      textFont(w.font, w.fontSize);
      wordWidth = textWidth(w.word + " ");
      if(lineWidth + wordWidth > textBoxWidth)
      {

        lineNum++;
        if (lines.length  == lineNum) {
          lines = (Word[][])expand(lines);
        }
        if (lineNum >= wordsPerLine.length-1) {
          expandWordsPerLine();
        }
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
      lastLineHeight = this.y + FONT_SIZE*(lineNum+1)+5;
      ((Word) story.get(i)).y = lastLineHeight;
      //println(lineNum + " " + linePos + " " + lines.length + " " + lines[lineNum] + " " );
      if (lines[lineNum] == null) {
        lines[lineNum] = new Word[10];
      }
      if (linePos == lines[lineNum].length) {
        lines[lineNum] = (Word[])expand(lines[lineNum]);
      }
      lines[lineNum][linePos] = w;
      linePos++;
      

      wordsPerLine[lineNum] = linePos;

    }

  }

  private void expandWordsPerLine() {
    wordsPerLine = expand(wordsPerLine);
  }

  public void draw()
  {
    float lastY = lines[totalLines][0].y;

    float dec =0;
    if (lastY > (this.y +this.textBoxHeight)) {
      dec = lastY - (this.y +this.textBoxHeight);
    } 

    for(int i = 0; i <=totalLines; i++)
      //for(int i = totalLines-1; i >= 0; i--)
    {
      for(int j = 0; j < (int) wordsPerLine[i]; j++)
      {
        textFont(lines[i][j].font, lines[i][j].fontSize);
        fill(lines[i][j].col);
        //ellipse(lines[i][j].x, lines[i][j].y, 5, 5);
        text(lines[i][j].word, lines[i][j].x, lines[i][j].y-dec-15); //-15 para dar espaco abaixo da baseline
      }
    }

    
    fill(0);
    stroke(0);
    strokeWeight(1);
     rect(0, 0, width, this.y);
     rect(0, 0, borderWidth-1, height);
     rect(width-borderWidth, 0, borderWidth, height);
     rect(0, height-borderHeight, width, borderHeight);
    //fill(255);
    //println(lastLineHeight);
    //ellipse(lastLineWidth, lastLineHeight, 5, 5);
    
    noFill();
    stroke(255);
    strokeWeight(1);
    rect(this.x, this.y, this.textBoxWidth, this.textBoxHeight);
    
     line(10, this.y, 10, this.y+FONT_SIZE);
  }

  public void drawCursor()
  {
    

    float lastY = lines[totalLines][0].y;

    float dec =0;
    if (lastY > (this.y +this.textBoxHeight)) {
      dec = lastY - (this.y +this.textBoxHeight);
    } 
    strokeWeight(1);
    fill(255);
    stroke(255);

    rect(lastLineWidth, lastLineHeight-5-dec, 3, -FONT_SIZE);
    //image(cursorImg, lastLineWidth + 3, lastLineHeight +3-dec-30-15, 20, 35);
  }
  
  public String getStory() {
     String storyString = "";
     for (int i = 0; i < story.size(); i++) {
      Word w = (Word)story.get(i);
      storyString += w.word +" ";
    }
    return storyString;
  }

}





