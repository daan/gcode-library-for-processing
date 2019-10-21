/**
 * Letters. 
 * 
 * Draws letters to the screen. This requires loading a font, 
 * setting the font, and then drawing the letters.
 */

PFont f;

void setup() {
  size(640, 360);
  background(0);

  // Create the font
  printArray(PFont.list());
  f = createFont("SourceCodePro-Regular.ttf", 24);
  textFont(f);
  textAlign(CENTER, CENTER);
} 

void draw() {
  background(0);

  textMode(SHAPE);
  text("100",100,100);

}
