Textarea gMonitorTextArea;
String gMonitorText = new String();
Textfield gMonitorInputText;
Button gMonitorButton;

float jogSpeed = 1; 

public void distance(int v) { 
  jogSpeed = ((float)v)/10.0;
}


public void xmin(int v) { 
  if (m.isBusy()) return;  
  m.schedule(new String[]{"G91","G21","G0 X-"+jogSpeed} );  
}
public void xplus(int v) { 
  if (m.isBusy()) return;  

  m.schedule(new String[]{"G91","G21","G0 X"+jogSpeed} );  
}

public void ymin(int v) { 
  if (m.isBusy()) return;  

  m.schedule(new String[]{"G91","G21","G0 Y-"+jogSpeed} );  
}
public void yplus(int v) { 
  if (m.isBusy()) return;  

  m.schedule(new String[]{"G91","G21","G0 Y"+jogSpeed} );  
}

public void zmin(int v) { 
  if (m.isBusy()) return;  
  m.schedule(new String[]{"G91","G21","G0 Z-"+jogSpeed} );  
}
public void zplus(int v) { 
  if (m.isBusy()) return;  
m.schedule(new String[]{"G91","G21","G0 Z"+jogSpeed} );  
}


void updateMonitorControls() {
  if( m.hasNewReplies() ) {
    int last = m.numNewReplies();
    StringList l = m.getReplies();
    for(int i= last; i < l.size(); i++) {
      gMonitorText = gMonitorText + "\n" + l.get(i);
    }    
    gMonitorTextArea.setText(gMonitorText);
    gMonitorTextArea.scroll(1);
  }
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
   cp5.addButton("zplus")
     .setPosition(x+d*1.5,y+d)
     .setSize(40,20)
     .setLabel("-z")
     ;
    cp5.addSlider("distance")
    .setPosition(x-d,y+2*d)
     .setSize(80,20)
    ;
}


void setupMonitorControls() {
  gMonitorTextArea = cp5.addTextarea("txt")
    .setPosition(10,40)
    .setSize(400,300)
    .setLineHeight(14)
    .setColor(color(255))
    .setColorBackground(color(255,100))
    .setColorForeground(color(255,100));
    ;
  
  gMonitorInputText = cp5.addTextfield("inputText")
    .setPosition(10,350)
    .setSize(348,20)
    .setAutoClear(false)
    ;
  gMonitorInputText.getCaptionLabel().setVisible(false);
  
  gMonitorButton = cp5.addButton("send")
    .setPosition(360,350)
    .setSize(50,20)
    .align(ControlP5.CENTER, CENTER, CENTER, CENTER)
    ;    

  
}

public void inputText(String t) {
   m.schedule(t);
   println("send command" + t);
}

public void send(int v) {
   String t = gMonitorInputText.getText();
   if( t.length() != 0) {
     m.schedule(t);
   }
}
