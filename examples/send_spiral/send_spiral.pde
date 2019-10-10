import processing.serial.*;
import gcode.*;

Machine m;

void setup() {
    size(400,150);

    // print available serial ports
    // printArray(Machine.enumeratePorts() );
    
    // make a machine
    m = new Machine();

    // connect using the "last" serial port and 112k500 baudrate.
    m.connect(this);     
    
    // m.connect(this, "/dev/tty.usbmodem1421"); // use on macOS 
    // m.connect(this, "COM6");  // use on Windows

    GCodeList gcodes = new GCodeList();

  float cx = 50;
  float cy = 50;
  float r = 20;
  float z = 0;
  for(int turns=0; turns < 10; turns++) {
    for(int a=0; a < 360; a++) {
      float x = cx + r * (float)Math.cos(a/360.0 * TWO_PI);
      float y = cy + r * (float)Math.sin(a/360.0 * TWO_PI);
      if ( (a==0) && (turns==0) ) {
        gcodes.append( GCode.G0(x,y,z) ); 
      } else {
        gcodes.append( GCode.G1(x,y,z) );
      }        
      z+= 0.05;
    }
  }
    
    m.schedule("G90"); // absolute
    m.schedule("G21"); // mm
    
    m.schedule("G1 F200"); // feedrate 200
    m.schedule(gcodes.toStringArray() );
}

void draw() {
  if (m.hasNewReplies() ) {
    StringList replies = m.getReplies();
    for(int i=0; i < replies.size(); i++) {
      println(replies.get(i) );
    }
  }
}
