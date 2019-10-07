import processing.serial.*;

import gcode.*;
import controlP5.*;

Machine m;
ControlP5 cp5;

void setup() {
  size(640,480);
  cp5 = new ControlP5(this);
  setupMonitorControls();
  setupJogControls();
  setupConnectionControls();
  setupExampleControls();

  m = new Machine();  
// m.connect(this, "/dev/tty.usbmodem1451", 250000);
  
}

  


void draw() {
  background(0);
  updateConnectionStatus();
  updateMonitorControls();
  
}
