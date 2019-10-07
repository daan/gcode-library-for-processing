package gcode;

import processing.data.StringList;
import processing.core.PShape;
import processing.core.PGraphics;


public class GCode implements processing.core.PConstants {
  String mCode;
  java.util.Map<String,String> mArgs; // linkedhashmap preserves order of arguments
  
  public void arg(String arg, float value) {
    mArgs.put(arg, String.valueOf(value));
  }
  public void arg(String arg, int value) {
    mArgs.put(arg, String.valueOf(value));
  }
  public void arg(String arg, String value) {
    mArgs.put(arg, value);
  }

  public void arg(String arg) {
    mArgs.put(arg, "");    
  }

  public boolean  hasArg(String arg) {
    return mArgs.containsKey(arg);
  }

  public String getCode() {
    return mCode;
  }
  
  public String getArg(String a) {
    return mArgs.get(a) ;    
  }

  public String getArg(String a, String defaultValue) {
    return mArgs.getOrDefault(a, defaultValue);    
  }


  @Override
  public String toString() {
    String ret = mCode;
    for (java.util.Map.Entry<String, String> entry : mArgs.entrySet()) {
      ret = ret + " " + entry.getKey() + entry.getValue();
    }
    return ret;
  }

  /**
   *
   *
   */
  public GCode(String code) { 
    mArgs = new java.util.LinkedHashMap<String, String>();  
    mCode = code; 
  }
    
  public GCode x(float value) {
    arg("X", value);
    return this;
  }  

  public GCode y(float value) {
    arg("Y", value);
    return this;
  }  

  public GCode z(float value) {
    arg("Y", value);
    return this;
  }  

  public GCode feedrate(float aValue) {
    arg("F", aValue);
    return this;
  }  
  
  static public GCode G0(float x, float y) {
    GCode g = new GCode("G0"); 
    g.arg("X",x); g.arg("Y",y); return g; 
  }

  static public GCode G0(float x, float y, float z) {
    GCode g = new GCode("G0"); g.arg("X",x); g.arg("Y",y); g.arg("Z",z); return g; 
  }  

  static public GCode move(float x, float y) { return G0(x,y); }
  static public GCode move(float x, float y, float z) { return G0(x,y,z); }

  static public GCode G1(float x, float y) {
    GCode g = new GCode("G1"); 
    g.arg("X",x); g.arg("Y",y); return g; 
  }
  static public GCode G1(float x, float y, float z) {
    GCode g = new GCode("G1"); g.arg("X",x); g.arg("Y",y); g.arg("Z",z); return g; 
  }  





  static public GCode home() { return new GCode("G28"); }
  
  static public GCode homeXY() {
    GCode g = new GCode("G28"); g.arg("X"); g.arg("Y"); return g;
  }

  static public GCode servoPosition(int index, float pos) {
   GCode g = new GCode("M280"); g.arg("P",index); g.arg("S", pos); return g;
  }
  
  static public GCode millimeters() {
    return new GCode("G28"); 
  }

  static public GCode inches() {
    return new GCode("G21"); 
  }

  static public GCode absolutePositioning() {
    return new GCode("G90");
  }

  static public GCode relativePositioning() {
    return new GCode("G91");
  }
  static public GCode fromString(String str) {
    // first remove everything following a (
    int c = str.indexOf('(');
    if(c != -1) {
      str = str.substring(0,c-1);
    }    
    String[] parts = str.split(" ");
    GCode g = new GCode(parts[0]);
    // parse args
    for(int i=1; i < parts.length; i++) {
      String arg = parts[i].substring(0,1);
      if(parts[i].length() == 1) {
        g.arg(arg);
      } else {
        String value = parts[i].substring(1);
        g.arg(arg, value);        
      }
    }
    return g;
  }

static public void visualize(PGraphics g, StringList gcodes) {
  visualize(g, GCodeList.fromStrings(gcodes) );
}

static public void visualize(PGraphics g, GCodeList gcodes) {

    // draw lines between (x,y,z) and (x2,y2,z2)
    float x = 0;
    float y = 0;
    float z = 0;
    
    float x2 = x;
    float y2 = y;
    float z2 = z;

    g.noFill();
    g.beginShape(LINES); 

    for (int i = 0; i < gcodes.size(); i++) {
      GCode gcode = gcodes.get(i);
      if (gcode.getCode().equals("G0") || gcode.getCode().equals("G1")) { 
        if (gcode.hasArg("X")) { x2 = Float.parseFloat(gcode.getArg("X")); }
        if (gcode.hasArg("Y")) { y2 = Float.parseFloat(gcode.getArg("Y")); }
        if (gcode.hasArg("Z")) { z2 = Float.parseFloat(gcode.getArg("Z")); }        
        if (gcode.getCode().equals("G1")) {
          g.stroke(0,128,255);
        } else {
          g.stroke(255,128,0);
        }
        if (g.is2D()) { 
          g.vertex(x,y);
          g.vertex(x2,y2); 
        } else { 
          g.vertex(x,y,z);
          g.vertex(x2,y2,z2);
        } 
        x = x2;
        y = y2;
        z = z2;
      } else {
          System.out.println("not (yet) supported code  " +gcodes.get(i));
      }
    }
    g.endShape();
  }




} // end class
