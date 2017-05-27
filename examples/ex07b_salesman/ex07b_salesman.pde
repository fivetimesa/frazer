import frazer.*;
import frazer.constants.*;

Frazer frazer;
Cities cities;
int numberOfCities = 10;

void setup()
{
  
  size(800, 600);
  
  frazer = new Frazer(this, 5000, numberOfCities);
  frazer.setGeneLimits(0, numberOfCities - 1);
  frazer.setMutation(new RangeValueMutation(0.05, 0.8));
  frazer.setMating(Frazer.MatingType.ROULETTEWHEEL);
  frazer.launchPlotter();
  
  cities = new Cities(numberOfCities);
}

void draw() {
  background(255);
  cities.display();
  Specimen best = frazer.evolve(1);
  displayPath(((IntegerGenotype)best.getGenes()).getGenes());
  println(best.getFitnessScore());

  /*
  float[] randomGenes = new float[numberOfCities - 1];
  for(int i = 0; i < numberOfCities - 1; i++) {
    randomGenes[i] = random(1);
  }
  displayPath(randomGenes);
  */
}

void keyPressed() {
  cities = new Cities(10);
}

void mousePressed() {
  frazer.restart();
}

void displayPath(int[] genes) {
  ArrayList<Integer> path = genesToCities(genes);
  
  for(int i = 1; i < path.size(); i++) {
    cities.drawConnection(path.get(i-1), path.get(i));
  }
  
}

float fitness(int[] genes) {
  ArrayList<Integer> path = genesToCities(genes);
  float dist = 0.0f;
  
  for(int i = 1; i < path.size(); i++) {
    dist += cities.getDistance(path.get(i-1), path.get(i));
  }
  
  for(int i = 0; i < numberOfCities; i++) {
    if(!path.contains(i)) dist += 10000;
  }
    
  return dist;
}

ArrayList genesToCities(int[] genes) {
  ArrayList<Integer> path = new ArrayList();
  
  path.add(0);
  for(int i = 0; i < genes.length; i++) {
    path.add(genes[i]);
  }
  path.add(0);
  return path;
}

class Cities {
  ArrayList<PVector> positions;
  int count;
  float[][] distances;
  
  Cities(int count) {
    this.count = count;
    positions = new ArrayList();
    distances = new float[count][count];
    
    for(int i = 0; i < count; i++) {
      positions.add(new PVector(random(25, width - 25), random(25, height - 25)));
      
      for(int j = 0; j < i; j++) {
        float dist = PVector.dist(positions.get(i), positions.get(j));
        distances[i][j] = dist;
        distances[j][i] = dist;
      }
      distances[i][i] = 0;
    }
  }
  
  void display() {
    pushStyle();
    strokeWeight(5);
    stroke(0, 0, 20);
    for(int i = 0; i < count; i++)
      point(positions.get(i).x, positions.get(i).y);
    popStyle();
  }
  
  void drawConnection(int a, int b) {
    line(positions.get(a).x, positions.get(a).y,
         positions.get(b).x, positions.get(b).y);
  }
  
  float getDistance(int a, int b) {
    return distances[a][b];
  }
}