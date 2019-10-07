import gcode.*;

GCodeGraphics ggraphics = new GCodeGraphics();

void setup() {
  size(600,600);
 
  beginRecord(ggraphics);
  
  background(255);
  stroke(0);
  noFill();

  int steps = 10;
  for(int i=0; i<steps; i++) {
    pushMatrix();
    translate(200,200);
    rotate((float)i/(float)steps * PI );
    println("rect");
    ellipse(0,0,300,200);
    popMatrix();
  }
  
  endRecord();
  
  print(ggraphics);
}

void draw() {  
  background(255);
  stroke(0);
  noFill();
  GCode.visualize(this.g, ggraphics.getStringList());
}
