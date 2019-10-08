import processing.serial.*;
import gcode.*;

Machine m;

void setup() {
    size(400,150);

    // print available serial ports
    printArray(Machine.enumeratePorts() );
    // make a machine
    m = new Machine();
    // connect using the "last" serial port and 112k500 baudrate.
    m.connect(this); 
    
    // m.connect(this, "/dev/tty.usbmodem1421"); // use on macOS 
    // m.connect(this, "COM6");  // use on Windows
}

int timer = millis() + 3000;
boolean home = false;

void draw() {
  if ((millis() > timer) && (home == false)) {
    home = true;
    m.send("?"); // grbl status    
    m.send("G0 X100");
    
  }
  if (m.hasNewReplies() ) {
    StringList replies = m.getReplies();
    for(int i=0; i < replies.size(); i++) {
      print( replies.get(i) );
    }
  }
}
