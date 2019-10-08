package gcode;

import java.io.*;

import processing.core.PConstants;
import processing.data.StringList;
import processing.core.PVector;
import processing.core.PMatrix3D;

import processing.core.PGraphics;
import static java.lang.System.*;


/**
 * This is a library for writing Gcodes files using beginRecord()/endRecord()
 * Inspired by the svg/obj/pdf export libraries and https://github.com/ciaron/HPGLGraphics/
 */

// https://github.com/processing/processing/blob/master/core%2Fsrc%2Fprocessing%2Fcore%2FPGraphics.java
// https://github.com/processing/processing/blob/master/core/src/processing/core/PConstants.java

public class GCodeGraphics extends PGraphics {

public static final String GGRAPHICS = "gcode.GCodeGraphics";
  
File file;
PrintWriter writer;  
  
StringList gcodes = new StringList();
float feedRate = 10.0f;

// for closing shapes and quads
PVector first;
boolean firstIs2D;

boolean penMode = false;
StringList penUpCommands;
StringList penDownCommands;


private PMatrix3D transformMatrix;
private boolean matricesAllocated = false;
private int MATRIX_STACK_DEPTH = 32;
private int transformCount = 0;
private PMatrix3D transformStack[] = new PMatrix3D[MATRIX_STACK_DEPTH];


//////////////////////////////////////////////////////////////

public GCodeGraphics(){
     super();

    if (!matricesAllocated) {   
      // Init default matrix
      this.transformMatrix = new PMatrix3D();
      matricesAllocated = true;
     }
}


//////////////////////////////////////////////////////////////

  // few common helper functions to shorten code and improve readability

void appendG0(float x, float y) { 
  gcodes.append("G0 X"+x+ " Y"+y);
}

void appendG0(float x, float y, float z) { 
  gcodes.append("G0 X"+x+ " Y"+y+ " Z"+z);
}

void appendG0(PVector v) { 
  gcodes.append("G0 X"+v.x+ " Y"+v.y+ " Z"+v.z);
}


void appendG1(float x, float y) { 
  gcodes.append("G1 X"+x+ " Y"+y);
}


void appendG1(float x, float y, float z) { 
  gcodes.append("G1 X"+x+ " Y"+y + " Z"+z);
}

void appendG1(PVector v) { 
  gcodes.append("G1 X"+v.x+ " Y"+v.y+ " Z"+v.z);
}

void appendClose() {
  if (firstIs2D) {
      appendG1( first.x, first.y);
    } else {
      appendG1( first.x, first.y, first.z);
    }      
}


void appendPenUp() {
  gcodes.append(penUpCommands);
}

void appendPenDown() {
  gcodes.append(penDownCommands);
}



//////////////////////////////////////////////////////////////

/**
 *
 *
 */

public void setPenUpDown(StringList penUp, StringList penDown) {
  penMode = true;
  penUpCommands = penUp;
  penDownCommands = penDown;  
}

public void setPenUpDown(GCodeList penUp, GCodeList penDown) {
  setPenUpDown(penUp.toStringList(), penDown.toStringList());
}

//////////////////////////////////////////////////////////////

  /**
   * This method sets the path and filename for the gcode output.
   * Must be called from the Processing sketch
   * 
   * @example demo
   * @param path String: name of file.
   */
   
  public void setPath(String path) {
    
    this.path = parent.sketchPath(path);
     if (path != null) {
       file = new File(this.path);
     }
     if (file == null) {
        throw new RuntimeException("Error trying to create file "+this.path);
     }
  }


//////////////////////////////////////////////////////////////

  public void pushMatrix() {

    if (transformCount == transformStack.length) {   
      throw new RuntimeException("pushMatrix() overflow");
    }
      
    transformStack[transformCount] = this.transformMatrix.get();
    transformCount++;
  }

  public void popMatrix() {
    if (transformCount == 0) {   
      throw new RuntimeException("gcode: matrix stack underrun");
    }
    transformCount--;
    transformMatrix = new PMatrix3D();
    for (int i = 0; i <= transformCount; i++) {   
      transformMatrix.apply(transformStack[i]);
    }
  }
  
  public void resetMatrix(){ }

//////////////////////////////////////////////////////////////

  public void translate(float x, float y) {
    transformMatrix.translate(x, y);
  }
  

  public void scale(float s) {
    transformMatrix.scale(s,s);
  }
  
  public void scale(float x, float y) {
    transformMatrix.scale(x,y);    
  }
  
  public void rotate(float angle) { 
    transformMatrix.rotate(angle);
  }

//////////////////////////////////////////////////////////////

@Override
public void beginShape(int kind) {
    first = null;
    shape = kind;
    if(penMode == true) {
      appendPenDown();
    }
}

@Override 
  public void endShape(int mode) {

  if (mode == CLOSE) {
    appendClose();     
  } else {
  }
  if (shape == QUADS) {
    appendClose(); 
  }
  
  if(penMode == true) {
    appendPenUp();
  } 
}

private PVector transformPoint(float x, float y, float z) {
  // apply matrix
  float v[]  = { x,y,z };
  float r[] = {0,0,0};
  r = transformMatrix.mult(v, r); 
  return new PVector(r[0], r[1], r[2]);
}


@Override
public void vertex(float x, float y, float z) {
  
  /*
  // apply matrix
  float v[]  = { x,y,z };
  float r[] = {0,0,0};
  r = transformMatrix.mult(v, r); 
  x = r[0];
  y = r[1];
  z = r[2];
  */

  PVector pt = transformPoint(x, y, z);

  curveVertexCount = 0;
  float vertex[] = vertices[vertexCount];
  vertex[X] = pt.x;  
  vertex[Y] = pt.y;
  vertex[Z] = pt.z;
  vertexCount++;
  
  if ( first == null ) { 
    firstIs2D = false;
    first = pt;
    appendG0(pt.x,pt.y,pt.z);
  } else {
    appendG1(pt.x,pt.y,pt.z);
  }
}

@Override
public void vertex(float x, float y) {
  vertex(x,y,0);
}


/**
 *  we implement arcs, ellipses and circles with the G2 and G3 commands
 *  see: https://reprap.org/wiki/G-code
 *

 *  void addArc(...
 *  void addEllipse(...
 *   https://github.com/processing/processing/blob/master/core/src/processing/opengl/PGraphicsOpenGL.java  
 *
 */


// FIXME: in case of w==h we should use G2 (arc).

protected void arcImpl(float x, float y, float w, float h, float start, float stop, int mode) {

  beginShape(LINE_STRIP);

  float step = (float)(10.0/360.0) * (float)(TWO_PI); // FIXME: use the same parameter as bezier (in pgraphics).
  float da = (stop - start) / step;

  long steps = Math.abs(Math.round(da));
 
  for(long i=0; i<= steps; i++) {
    float px = x + w*(float)(0.5*Math.cos(start+i * step)); // 0.5 because of radius.
    float py = y + h*(float)(0.5*Math.sin(start+i * step));

    if (i==0) {
      if (mode == PIE) {
        vertex(x, y);
        vertex(px, py);
      } else {
        vertex(px, py);
      }
    } else {
      vertex(px, py);
    }  
  }
  if (mode == PIE) {
    vertex(x, y);
  }
  if (mode == CHORD) { // return to first point.
    vertex(x + w*(float)(0.5*Math.cos(start)), y + h*(float)(0.5*Math.sin(start)) );
  }

  endShape(OPEN);
}


protected void ellipseImpl(float x, float y, float w, float h) {
  arcImpl(x,y,w,h, 0, TWO_PI, OPEN);
}


public StringList getStringList() {
  return gcodes;
}

@Override
public String toString() {
  String s = "";
  for (int i = 0; i < gcodes.size(); i++) {
    s += gcodes.get(i) + "\n";
  }
  return s;
}

} // class
