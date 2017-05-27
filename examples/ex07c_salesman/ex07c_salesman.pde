import frazer.*;
import frazer.constants.*;

Frazer frazer;
Cities cities;
int numberOfCities = 10;

void setup()
{
  
  size(800, 600);
  
  frazer = new Frazer(this, 1000, numberOfCities * 2);
  float[] minLimits = new float[numberOfCities * 2];
  float[] maxLimits = new float[numberOfCities * 2];
  for(int i = 1; i < numberOfCities * 2; i += 2) {
    minLimits[i-1] = 0;
    minLimits[i] = 0;
    maxLimits[i-1] = width - 1;
    maxLimits[i] = height - 1;
  }
  
  frazer.setGeneLimits(minLimits, maxLimits);
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
  //*/

  /*
  int[] randomGenes = new int[numberOfCities * 2];
  for(int i = 1; i < numberOfCities * 2; i += 2) {
    randomGenes[i - 1] = (int) random(width);
    randomGenes[i] = (int) random(height);
  }
  displayPath(randomGenes);
  //*/
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
  for(int i = 1; i < genes.length; i += 2) {
    path.add(cities.closestCity(genes[i-1], genes[i]));
  }
  path.add(0);
  return path;
}

class Cities {
  ArrayList<PVector> positions;
  int count;
  float[][] distances;
  int[][] closest;
  
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
    
    closest = new int[width][height];
    for(int i = 0; i < width; i++)
      for(int j = 0; j < height; j++) {
        int closestCity = -1;
        float closestDist = Float.MAX_VALUE;
        for(int k = 0; k < numberOfCities; k++) {
          float d = PVector.dist(positions.get(k), new PVector(i, j));
          if(d < closestDist) {
            closestCity = k;
            closestDist = d;
          }
          closest[i][j] = closestCity;
        }
          
      }
  }
  
  int closestCity(int x, int y) {
    return closest[x][y];
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