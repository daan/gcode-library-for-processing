package gcode;

import processing.core.PShape;
import java.util.*;
import processing.data.StringList;


public class GCodeList {
  LinkedList<GCode> mCommands;

  public GCodeList() {
    mCommands = new LinkedList<GCode>();
  }

  public void clear() {
    mCommands.clear();
  }
 
  public void append(GCode g) {
    mCommands.add(g);  
  }
  public void remove(int index) {
    mCommands.remove(index);
  }

  public int length() {
    return mCommands.size();
  }

  public void append(String s) {
    if( s.indexOf('\n') == -1) {
      mCommands.add( GCode.fromString(s) );
      return;    
    }   
    String[] lines = s.split("\n");
    for(int i=0;i<lines.length;i++) {
      mCommands.add( GCode.fromString(lines[i]) );
    }
  }

  public void append(String[] s) {
    for(int i=0;i<s.length;i++) {
      append(s); // 
    }
  }

  public void append(GCodeList s) {
    for(int i=0;i<s.size();i++) {
      append(s.get(i));  
    }
  }

  /*
  void append(PShape s) {

  }
  */

  public int size() { return mCommands.size(); }

  public GCode get(int index) {
    return mCommands.get(index);
  }

  public void set(int index, GCode g) {
    mCommands.set(index, g);
  }

  public String[] toStringArray() {
    String[] s = new String[mCommands.size()];
    for(int i=0; i < mCommands.size(); i++) {
      s[i] = mCommands.get(i).toString();
    }
    return s;
  }
  
  public StringList toStringList() {
    StringList s = new StringList();    
    for(int i=0; i < mCommands.size(); i++) {
      s.append(mCommands.get(i).toString());
    }
    return s;    
  }

  @Override
  public String toString() {
    String ret = "";
    for(int i=0; i < mCommands.size(); i++) {
      ret = ret + mCommands.get(i).toString() + "\n";
    }    
    return ret;
  }



  static public GCodeList fromShape(PShape p) {
    
    GCodeList ret = new GCodeList(); // code.toArray(new String[gcode.size()]);
    return ret;
  }

  /*
  static public PShape toShape(GCodeList g) {
     PShape p = new PShape();
    return p;  
  }
  */

static public GCodeList fromStrings(String[] lines) {
  GCodeList gl = new GCodeList();
  for (int i = 0 ; i < lines.length; i++) {
    // System.out.println("line" + i);
    GCode g = GCode.fromString(lines[i]);
    gl.append(g);
  }
  return gl;
} 


static public GCodeList fromStrings(StringList lines) {
  GCodeList gl = new GCodeList();
  for (int i = 0 ; i < lines.size(); i++) {
    GCode g = GCode.fromString(lines.get(i));
    gl.append(g);
  }
  return gl;
} 


}
