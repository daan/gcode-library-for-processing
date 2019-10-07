import peasy.*;
import gcode.*;

PeasyCam cam;
PVector buildVolume = new PVector(300,300,300);

GCodeList gcodes = new GCodeList();

void setup() {
  size(600,600,P3D);  
  setupCamera();
  
  float cx = 150;
  float cy = 150;
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
  
  // print gcodes
  
  print(gcodes);
}

void setupCamera() {
  cam = new PeasyCam(this, 500);
  cam.setMinimumDistance(50);
  cam.setMaximumDistance(800);
  cam.rotateX(-HALF_PI/2.0);
  cam.lookAt(buildVolume.x/2.0, buildVolume.y/2.0, 0);
}

void drawOrigin() {
  stroke(255,0,0);
  line(0,0,buildVolume.x,0);
  stroke(0,255,0);
  line(0,0,0,buildVolume.y);

  stroke(230);
  line(buildVolume.x,0,buildVolume.x,buildVolume.y);
  line(0,buildVolume.y,buildVolume.x,buildVolume.y);

  pushMatrix();
    rotateY(-HALF_PI);
    stroke(0,0,255);
    line(0,0,buildVolume.z,0);
  popMatrix(); 
}



void draw() {
  background(255);
  drawOrigin();
  
  GCode.visualize(this.g, gcodes);
  
}
