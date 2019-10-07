import gcode.*;


void setup() {
  size(600,600);
}

void draw() {  
  background(255);
  stroke(0);
  noFill();

   for (int i = 0; i < 200; i += 20) {
    bezier(mouseX-(i/2.0), 40+i, 410, 20, 440, 300, 240-(i/16.0), 300+(i/8.0));
  }

  GCodeGraphics ggraphics = new GCodeGraphics();

  beginRecord(ggraphics);
  pushMatrix();
  for (int i = 0; i < 200; i += 20) {
    bezier(mouseX-(i/2.0), 40+i, 410, 20, 440, 300, 240-(i/16.0), 300+(i/8.0));
  }
  popMatrix();
  endRecord();

  GCode.visualize(this.g, ggraphics.getStringList());


}
