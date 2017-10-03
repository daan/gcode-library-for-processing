import processing.serial.*;
import controlP5.*;

Machine m;
ControlP5 cp5;


// this function gets called when a line is received on the serial
// we trigger the machine.
void serialEvent(Serial myPort) {
    m.event(); 
}

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