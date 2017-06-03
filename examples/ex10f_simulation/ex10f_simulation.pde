import frazer.*;

Frazer frazer;
Creature[] population;
Creature sampleCreature;

int counter = 0;
int simulationSteps = 1500;
int w = 5;
int h = 5;
int size = 50;

void setup() {
  size(800, 400);
  sampleCreature = new Creature();
  //c.moveTo(new PVector(width/2, height/2));
  
  frazer = new Frazer(this, 150, w * h, GenotypeType.FLOAT, sampleCreature);
  //frazer.setGoal(Goal.MAXIMISE);
  //frazer.launchPlotter();
  population = castArray(frazer.getCurrentPopulation().getSpecimens());
  for(int i = 0; i < population.length; i++)
    population[i].init(w, h, size);
  sampleCreature = population[0];
}

void draw() {
  background(255);
  for(Creature c: population)
    c.update();
  sampleCreature.display(g);
  
  counter++;
  if(counter >= simulationSteps) {
    counter = 0;
    frazer.evaluateCurrent();
    println("best score: " + frazer.getCurrentPopulation().getMinScore());
    population = castArray(frazer.evolve());
    sampleCreature = population[0];
  }
}

Creature[] castArray(Specimen[] specimens) {
  Creature[] creatures = new Creature[specimens.length];
  for(int i = 0; i < specimens.length; i++)
    creatures[i] = (Creature) specimens[i];
  return creatures;
}

void mouseDragged() {
  PVector mouse = new PVector(mouseX, mouseY);
  PVector pmouse = new PVector(pmouseX, pmouseY);
  sampleCreature.pullNode(pmouse, mouse);
}