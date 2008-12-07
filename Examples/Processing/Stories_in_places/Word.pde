class Word
{
  String word;
  PFont font;
  int fontSize;
  color col;

  float x;
  float y;

  TextBox textBox;

  Word(TextBox tb, String word, PFont font, int fontSize)
  {
    this.textBox = tb;

    this.word = word;

    this.font = font;
    //font = loadFont("AgencyFB-Reg-32.vlw");
    //font = f;
    this.fontSize = fontSize;
    processSpam();
  }

  public void processSpam() {
    for (int j = 0; j < textBox.getSpam().length; j++) {
      String spam = textBox.getSpam()[j];
      if (this.word.equalsIgnoreCase(spam) ) {
        StringBuffer sb = new StringBuffer();
        sb.append(word.charAt(0));
        for (int i = 1 ; i < this.word.length()-1; i++) {
          sb.append("*");
        }
        sb.append(word.charAt(this.word.length()-1));
        this.word = sb.toString();
      }
    }    
  }
  
  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
  }
  public void setColor(color c) {
    this.col = c;
  }
}
