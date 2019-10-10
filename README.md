A library for dealing with gcodes in Processing Java. 

With this library you can:
* render PShapes to gcodes, very similar to the PDF, SVG DXF writers
* parse and generate gcodes with a convienient class GCode
* communicate with a CNC device over serial.

Working with fabrication machines can be dangerous. Use this library at your own risk. 

Features:
* Derived from PGraphics, all processing drawing should be supported.
* Custom "pen up" and "pen down" gcodes for pen-plotters.

Limitations:
* beziers don't work with transformation matrices.
* SVG shape is not supported yet.
* This library doesn't do slicing
* This library ignores any fills, just outlines. 

## install ##

Download the zip or clone the the archive into your processing library folder. Rename it to "gcode".




## Examples ##

```
import gcode.*;

GCodeGraphics ggraphics = new GCodeGraphics();

setup() {
  beginRecord(ggraphics);
  ellipse(0,0,300,200);
  endRecord();
  print(ggraphics);
}

draw() {
  background(255);
  noFill();
  GCode.visualize(this.g, ggraphics.getStringList());
}
````

```
import processing.serial.*;
import gcode.*;

Machine m = new Machine();
GCodeGraphics ggraphics = new GCodeGraphics();

setup() {
  m.connect(this);
  
  GCodeGraphics ggraphics = new GCodeGraphics();
  ggraphics.setPenUpDown(new StringList( "M3 S0"), new StringList( "M3 S255"));
  
  beginRecord(ggraphics); 
    ellipse(50,50,10,10);
    ellipse(25,25,10,10);
  endRecord();
  
  m.home();
  m.schedule("G1 F200"); // feedrate 200
  m.schedule(ggraphics);
}

draw() {
if (m.hasNewReplies() ) {
    StringList replies = m.getReplies();
    for(int i=0; i < replies.size(); i++) {
      println(replies.get(i) );
    }
  }
}
````






