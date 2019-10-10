import processing.serial.*;

import gcode.*;
import controlP5.*;

Machine m = new Machine();  
ControlP5 cp5;

void setup() {
  size(640,480);
  cp5 = new ControlP5(this);
  setupMonitorControls();
  setupJogControls();
  setupConnectionControls();
  setupExampleControls();

  // m.connect(this, "/dev/tty.usbmodem1451", 250000);
  
}

  


void draw() {
  if (m.isConnected()) {  
    m.poll();
  }
  background(0);
  updateConnectionStatus();
  updateMonitorControls();
  
}
