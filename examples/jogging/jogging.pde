import processing.serial.*;

import gcode.*;
import controlP5.*;

Machine m = new Machine();  
ControlP5 cp5;

float jogSpeed = 1; 

public void distance(int v) { 
  jogSpeed = ((float)v)/10.0;
}


public void home(int v) { 
  if (m.isBusy()) return;  
  println("home");
  m.home();  
}

void xmin(int v) { 
  if (m.isBusy()) return;  
  m.schedule(new String[]{"G91","G21","G0 X-"+jogSpeed} );  
}
void xplus(int v) { 
  if (m.isBusy()) return;  

  m.schedule(new String[]{"G91","G21","G0 X"+jogSpeed} );  
}

void ymin(int v) { 
  if (m.isBusy()) return;  

  m.schedule(new String[]{"G91","G21","G0 Y-"+jogSpeed} );  
}
void yplus(int v) { 
  if (m.isBusy()) return;  

  m.schedule(new String[]{"G91","G21","G0 Y"+jogSpeed} );  
}

void zmin(int v) { 
  if (m.isBusy()) return;  
  m.schedule(new String[]{"G91","G21","G0 Z-"+jogSpeed} );  
}
void zplus(int v) { 
  if (m.isBusy()) return;  
m.schedule(new String[]{"G91","G21","G0 Z"+jogSpeed} );  
}

void setupJogControls() {
    
    int x = 500;
    int y = 200;
    int d = 50;
    
    cp5.addButton("home")
     .setPosition(x,y)
     .setSize(40,20)
     ;

    cp5.addButton("xmin")
     .setPosition(x-d,y)
     .setSize(40,20)
     .setLabel("-x")
     ;
    cp5.addButton("xplus")
     .setPosition(x+d,y)
     .setSize(40,20)
     .setLabel("+x")
     ;

  cp5.addButton("yplus")
     .setPosition(x,y-d)
     .setSize(40,20)
     .setLabel("+y")
     ;
   cp5.addButton("ymin")
     .setPosition(x,y+d)
     .setSize(40,20)
     .setLabel("-y")
     ;

   cp5.addButton("zplus")
     .setPosition(x+d*1.5,y-d)
     .setSize(40,20)
     .setLabel("+z")
     ;
   cp5.addButton("zmin")
     .setPosition(x+d*1.5,y+d)
     .setSize(40,20)
     .setLabel("-z")
     ;
    cp5.addSlider("distance")
    .setPosition(x-d,y+2*d)
     .setSize(80,20)
    ;
}


void keyPressed() {
  if (m.isBusy()) return;  
  if (! keyPressed) return;

    if (key == 'w') {
       m.schedule(new String[]{"G91","G21","G0 Y5"} );  
    } else if (key == 's') {
       m.schedule(new String[]{"G91","G21","G0 Y-5"} );  
    } else if (key == 'a') {
       m.schedule(new String[]{"G91","G21","G0 X-5"} );  
    } else if (key == 'd') {
       m.schedule(new String[]{"G91","G21","G0 X5"} );  
    } else if (key == 'e') {
       m.schedule(new String[]{"G91","G21","G0 Z5"} );  
    } else if (key == 'c') {
       m.schedule(new String[]{"G91","G21","G0 Z-5"} );  
    } else if (key == '1') {
       m.schedule(new String[]{"M114"} );  
    }
    
}




void setup() {
  size(640,480);
  
  cp5 = new ControlP5(this);
  setupJogControls();
  
  m.setBaudRate(250000);  
  m.connect(this);
}

void draw() {
   background(128);

if (m.isBusy()) {
   print(".");
}

  if (m.hasNewReplies() ) {
    StringList replies = m.getReplies();
    for(int i=0; i < replies.size(); i++) {
      println(replies.get(i) );
    }
  }
}
