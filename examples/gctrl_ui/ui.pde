
public void endstops(int v) { 
  m.schedule("?");
}

public void servo(int v) {
  m.schedule("M3 S255");
}

public void home(int v) {
  m.home();
}

public void go1(int v) {
   m.schedule("G1 X100 Y100 F2000"); // gcode_move(100,150,2000) ); 
}
public void go2(int v) {
   m.schedule("G1 X50 Y150 F2000"); // gcode_move(100,150,2000) ); 
}

void setupExampleControls() {
    
  cp5.addButton("endstops")
     .setPosition(10,410)
     .setSize(80,20)
     ;

  cp5.addButton("homeXY")
     .setPosition(110,410)
     .setSize(80,20)
     ;

  cp5.addButton("go1")
     .setPosition(210,410)
     .setSize(40,20)
     ;


  cp5.addButton("go2")
     .setPosition(360,410)
     .setSize(40,20)
     ;
}
