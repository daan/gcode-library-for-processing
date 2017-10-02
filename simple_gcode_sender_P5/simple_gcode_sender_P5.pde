import processing.serial.*;
import controlP5.*;


Machine m;
ControlP5 cp5;


// this function gets called when a line is received on the serial
// we trigger the machine. Currently only one is supported..
void serialEvent(Serial myPort) {
    m.event(); 
}

void setup() {
  size(640,480);

  m = new Machine();
  // init the machine (without using the gui)
  //     m.setBaudRate(9600)  // default is 250000
  //     m.connect(this, "/dev/tty.usbmodem1451");

  cp5 = new ControlP5(this);  
  setupMonitor();
  setupConnectionUI();
  
  setupExampleButtonBar();
}

  
void draw() {
  background(0);
  updateConnectionStatus();
  updateMonitor(); 
}