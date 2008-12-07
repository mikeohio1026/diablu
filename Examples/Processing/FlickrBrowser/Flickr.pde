import proxml.*;

String userName = "jorgecardoso";
String apiKey = "b70f502524ffce22674e108aaf642180";
String nsid = "";
String url = "http://api.flickr.com/services/rest/";
XMLInOut xml = null;

int num = -1;

boolean search(String _n) {
  num = -1;
  String rest = url+"?method=flickr.photos.search";
  rest += "&api_key=" + apiKey;
  rest += "&text=" + _n;
  xml.loadElement(rest);
  
  for (int i = 0; i < 20; i++) {
    if (num > 0) {
      return true;
    } else if (num == 0) {
      return false;
    }
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ie) {
      println(ie.getMessage());
    }
  }
  return false;
}

void findByUsername(String _n) {
  String rest = url+"?method=flickr.people.findByUsername";
  rest += "&api_key=" + apiKey;
  rest += "&username=" + _n;
  xml.loadElement(rest);
}

void getPublicPhotos(String _n) {
  String rest = url+"?method=flickr.people.getPublicPhotos";
  rest += "&api_key=" + apiKey;
  rest += "&user_id=" + nsid;
  xml.loadElement(rest);
}

void xmlEvent(proxml.XMLElement _x) {
  parseXML(_x);
}

void parseXML(proxml.XMLElement _x) {
  String stat = _x.getAttribute("stat");
  if (!stat.equals("ok")) {
    println("Error from Flickr");
    println(_x);
    return;
  }
  proxml.XMLElement node = _x.getChild(0);
  String type = node.getName();
  if (type.equals("user")) {
    nsid = node.getAttribute("nsid");
    //println(nsid);
    getPublicPhotos(nsid);
  }
  else if (type.equals("photos")) {
    num = node.countChildren();
    //println(num);
    //getPhotos(node);
  }
}

void getPhotos(proxml.XMLElement _n) {
  int cnt = _n.countChildren();
  cnt = min(cnt,64);
  for (int i=0;i<cnt;i++) {
    proxml.XMLElement ph = _n.getChild(i);
    String fm = ph.getAttribute("farm");
    String sv = ph.getAttribute("server");
    String id  = ph.getAttribute("id");
    String sc = ph.getAttribute("secret");
    String imgURL = "http://farm"+fm+".static.flickr.com/"+
      sv + "/" + id + "_" + sc + "_s.jpg";
    PImage img = loadImage(imgURL);
    int x = (i%8) * img.width;
    int y = (i/8) * img.height;
    image(img,x,y);
  }
}
