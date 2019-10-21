import processing.serial.*;
import gcode.*;

Machine m;

void setup() {
    size(600,300);

    // print available serial ports
    // printArray(Machine.enumeratePorts() );
    
    // make a machine
    m = new Machine();
    

    // connect using the "last" serial port and 112k500 baudrate.
    m.connect(this);     
    
    // m.connect(this, "/dev/tty.usbmodem1421"); // use on macOS 
    // m.connect(this, "COM6");  // use on Windows
    
    GCodeGraphics ggraphics = new GCodeGraphics();
    
    // set the pen up pen down
    // https://github.com/DWiskow/grbl1-1g-Servo
    ggraphics.setPenUpDown(new StringList( "M3 S0"), new StringList( "M3 S255"));
    
    
    beginRecord(ggraphics); 
    //rect(50,50,10,10);
    //rect(25,25,10,10);
    
    
    ellipse(50,50,10,10);
    ellipse(25,25,10,10);
    
    endRecord();
    
    print(ggraphics);

    //m.home();
    m.schedule("G1 F200"); // feedrate 200
    m.schedule(ggraphics);
}

void draw() {
  
  // print the replies of the machine in the terminal.
  if (m.hasNewReplies() ) {
    StringList replies = m.getReplies();
    for(int i=0; i < replies.size(); i++) {
      println(replies.get(i) );
    }
  }
}
