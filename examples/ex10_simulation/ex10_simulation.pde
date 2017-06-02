
Creature c;

void setup() {
  size(800, 400);
  c = new Creature();
  c.init(3, 3, 40);
  c.moveTo(new PVector(width/2, height/2));
}

void draw() {
  background(255);
  c.update();
  c.display(g);
}

void mouseDragged() {
  PVector mouse = new PVector(mouseX, mouseY);
  PVector pmouse = new PVector(pmouseX, pmouseY);
  c.pullNode(pmouse, mouse);
}