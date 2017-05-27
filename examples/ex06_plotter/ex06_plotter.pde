import frazer.*;
import frazer.algorithms.mutation.*;

Frazer frazer;
PImage image;
PImage blurred;

void setup()
{
  
  size(946, 710);
  image = loadImage("image.jpg");
  blurred = image.copy();
  blurred.filter(BLUR, 3);
  
  frazer = new Frazer(this, 10000, 2);
  frazer.setGeneLimits(new float[]{3, 3}, new float[] {width - 4, height - 4});
  frazer.setMutation(new RangeValueMutation(100));
  frazer.launchPlotter();
  
}

void draw() {
  image(image, 0, 0);
  Specimen best = frazer.evolve(1);
  drawCross(best.getGenes().getInt(0), best.getGenes().getInt(1));
  println(best.getGenes().getInt(0) + ", " + best.getGenes().getInt(1) + " => " + best.getFitnessScore());
  println("mouse => " + brightness(blurred.get(mouseX, mouseY)));
  //drawCross(width/2, height/2);
}

void drawCross(float x, float y) {
  pushStyle();
  stroke(#ff0000);
  strokeWeight(2);
  line(x - 15, y, x + 15, y);
  line(x, y - 15, x, y + 15);
  popStyle();
}

float fitness(int[] genes) {
  return (float) brightness(blurred.get(genes[0], genes[1]));
}