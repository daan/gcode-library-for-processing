import processing.serial.*;

import gcode.*;

Machine m;

void setup() {

    size(400,150);

    // print available serial ports
    printArray(Machine.enumeratePorts() );

    // make a machine
    m = new Machine();
    
    // connect using the "last" serial port and 25k baudrate.
    m.connect(this); 
    
    // m.connect(this, "/dev/tty.usbmodem1421"); // use on macOS 
    // m.connect(this, "COM6");  // use on Windows
    println("done setup");
}

void draw() {
  background(0); 
  fill(255);
  text("press o to open a gcode file", 10, 20);

  text(str(m.numScheduledCommands()) + " commands left", 10, 40);

 if( m.hasNext() ) {
     m.playNextIfReady();
 }
   if (m.hasNewReplies() ) {
    StringList replies = m.getReplies();
    for(int i=0; i < replies.size(); i++) {
      print( replies.get(i) );
    }
  }
}

void keyPressed() {
   if (key== 'o') {
     File file = null; 
     selectInput("Select a file to process:", "fileSelected", file);
   }
}

void fileSelected(File selection) {
  if (selection == null) {
    println("no file selected.");
  } else {
    println("opening file " + selection.getAbsolutePath());
    String[] gcode = loadStrings(selection.getAbsolutePath());
    if (gcode == null) return;
    m.schedule(gcode);
  }
}
