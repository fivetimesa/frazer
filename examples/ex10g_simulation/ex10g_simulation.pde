import frazer.*;

Frazer frazer;
Creature[] population;
Creature sampleCreature;

int counter = 0;
int simulationSteps = 2500;
int groundLevel = 350;
int w = 5;
int h = 5;
int size = 50;
boolean display = true;

void setup() {
  size(800, 400);
  sampleCreature = new Creature();
  //c.moveTo(new PVector(width/2, height/2));
  
  frazer = new Frazer(this, 500, w * h * 2, GenotypeType.FLOAT, sampleCreature);
  frazer.setGeneLimits(0, 1);
  //frazer.setGoal(Goal.MAXIMISE);
  frazer.setMutantSelection(new ChanceMutantSelection(0.33f));
  frazer.setMutation(new RangeValueMutation(0.1f, 0.15f));
  frazer.setBreeding(new CrossoverBreeding(7));
  frazer.setMating(new TournamentMating(Goal.MINIMISE));
  //frazer.launchPlotter();
  population = castArray(frazer.getCurrentPopulation().getSpecimens());
  for(int i = 0; i < population.length; i++)
    population[i].init(w, h, size);
  sampleCreature = (Creature) frazer.getCurrentPopulation().getSpecimens()[0]
                              .makeChild(frazer.getCurrentPopulation().getSpecimens()[0].getGenes());
  sampleCreature.setGroundLevel(groundLevel);
}

void draw() {
  background(255);
  for(int i = 0; i < (display ? 1 : 50); i++) {
    for(Creature c: population)
      c.update();
    counter++;
  }
  if(display) {
    //sampleCreature.update();
    sampleCreature.display(g);
  }
  
  fill(0);
  stroke(0);
  line(0, groundLevel, width, groundLevel);
  textAlign(LEFT);
  text(counter, 10, 20);
  fill(255, 0, 0);
  text((1000 - sampleCreature.calculateFitness()), 10, 35);
  if(counter >= simulationSteps) {
    counter = 0;
    frazer.evaluateCurrent();
    //sampleCreature = (Creature) frazer.getCurrentPopulation().getMinSpecimen();
    //sampleCreature.moveTo(new PVector(width/2, sampleCreature.getCenter().y));
    println("best score: " + (1000 - frazer.getCurrentPopulation().getMinScore()));
    println("worst score: " + (1000 - frazer.getCurrentPopulation().getMaxScore()));
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

void keyPressed() {
  if(key == 's') 
    saveStrings("best_" + frazer.getHistory().getHistoryLength() + ".txt", 
                new String[]{sampleCreature.getGenes().toString()});
  if(key == '[') 
    simulationSteps -= 500;
  if(key == ']') 
    simulationSteps += 500;
  if(key == 'd') 
    display = !display;;
    
  if(simulationSteps <= 0) simulationSteps = 500;
  println("simulationSteps = " + simulationSteps);
}

void mouseDragged() {
  PVector mouse = new PVector(mouseX, mouseY);
  PVector pmouse = new PVector(pmouseX, pmouseY);
  sampleCreature.pullNode(pmouse, mouse);
}