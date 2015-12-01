final int WIDTH = 1000;
final int HEIGHT = 600;

boolean clicked = false;

void setup(){
  size(1000, 600);
  fill(0);
}

void draw(){
  
  if (clicked){
    ellipse(mouseX, mouseY, 2, 2);
  } else {
    background(255);
  }
}

void mousePressed() {
  clicked = true;
}

void mouseReleased() {
  clicked = false;
}