import frazer.*;
import frazer.algorithms.mutation.*;
import frazer.algorithms.mutantselection.*;

Frazer frazer;

void setup()
{
  size(500, 200);
  textAlign(CENTER);
  
  frazer = new Frazer(this, 100, 10);
  frazer.setMutantSelection(new FixedCountMutantSelection());
  frazer.setMutation(new NoMutation());
}

void draw() {
  background(0);
  text("best score: " + String.format("%.3f", frazer.evolve(1).getFitnessScore()), width/2, height/2);
  
}

float fitness(float[] genes) {
  float sum = 0;
  for(float f: genes)
    sum += abs(0.5 - f);
  return sum;
}