import processing.serial.*;

import gcode.*;
Machine m = new Machine();  


void setup() {
  size(800,800);
  
  background(128);
   stroke(255);
  m.setBaudRate(250000);  // marlin 
  m.connect(this);
  
  while (m.isBusy() ) {
    delay(100);
    print(".");
  }
  delay(100);
  println("done");
  m.home();
  m.schedule( "G90" );  // absolute mode 
  m.schedule("G0 F8000"); // go fast
  m.schedule("G! F8000"); // go fast

}

boolean penUp = true;


void draw() {
  if (mousePressed) {
    if (penUp) {
      if (! m.isBusy()) {
        m.schedule("G0 Z0");
        println("pen down");
    }
      penUp = false;
    }
  } else {
    if (penUp == false) {
      if (! m.isBusy()) {
        m.schedule("G0 Z2");
        println("pen up");
      }
      penUp = true;
    }
  }
  if (mousePressed == true) {
    line(mouseX, mouseY, pmouseX, pmouseY);
  }
  if (!m.isBusy()) {
    if ((mouseX != pmouseX) && (mouseY != pmouseY) ) {
      m.schedule( GCode.G0(mouseX/10, mouseY/10) );
    }
    println("go to " + mouseX + " " + mouseY);
  }
}
