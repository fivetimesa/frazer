import frazer.*;

Frazer frazer;
GoalPoints points;
int numberOfGenes = 8;
int numberOfPoints = 5;

void setup()
{
  frazer = new Frazer(this, 100, numberOfGenes);
  frazer.launchPlotter();
  size(800, 600);
  points = new GoalPoints(numberOfPoints);
  
  frazer.setMutation(new RangeValueMutation(0.05, 0.5));
  //frazer.setMating(Frazer.MatingType.ROULETTEWHEEL);
  frameRate(30);
}

void draw() {
  background(0);
  Specimen best = frazer.evolve(1);
  stroke(255); strokeWeight(2); noFill();
  display(((FloatGenotype)best.getGenes()).getGenes());
  println(best.getFitnessScore());
  
  stroke(255, 32); strokeWeight(1);
  for(Specimen s: frazer.getCurrentPopulation().getSpecimens()) {
    display(((FloatGenotype)s.getGenes()).getGenes());
  }
  points.display();
}

void keyPressed() {
  frazer.restart();
}

void mousePressed() {
  points.startMovingPoint();
}

void mouseReleased() {
  points.finishMovingPoint();
}

void display(float[] genes) {
  bezier(genes[0] * width, genes[1] * height, genes[2] * width, genes[3] * height, 
         genes[4] * width, genes[5] * height, genes[6] * width, genes[7] * height);
}

float fitness(float[] genes) {
  int samples = 50;
  float minDist[] = new float[numberOfPoints];
  for(int j = 0; j < numberOfPoints; j++) 
    minDist[j] = Float.MAX_VALUE;
    
  for(int i = 0; i < samples; i ++) {
    PVector pointOnCurve = new PVector(
      bezierPoint(genes[0] * width, genes[2] * width, 
                  genes[4] * width, genes[6] * width, 
                  float(i) / float(samples)),
      bezierPoint(genes[1] * height, genes[3] * height, 
                  genes[5] * height, genes[7] * height, 
                  float(i) / float(samples))
                  );
      
    for(int j = 0; j < numberOfPoints; j++) {
      float dist = PVector.dist(pointOnCurve, points.getPoint(j));
      if(dist < minDist[j]) minDist[j] = dist;
    }
  }
  float sum = 0;
  for(int j = 0; j < numberOfPoints; j++) 
    sum += minDist[j];
  return sum;
}

class GoalPoints {
  ArrayList<PVector> points;
  int count;
  int movingPoint;
  
  GoalPoints(int n) {
    count = n;
    points = new ArrayList();
    for(int i = 0; i < count; i++) {
      points.add(new PVector(random(50, width - 50), random(50, height - 50)));
    }
    movingPoint = -1;
  }
  
  PVector getPoint(int i) {
    return points.get(i);
  }
  
  boolean mouseOver(int i) {
    PVector mouse = new PVector(mouseX, mouseY);
    float threshold = 10;
    if(PVector.dist(mouse, points.get(i)) < threshold) 
      return true;
    else return false;
  }
  
  void startMovingPoint() {
    for(int i = 0; i < count; i++) {
      if(mouseOver(i)) {
        movingPoint = i;
        return;
      }
    }
  }
  
  void finishMovingPoint() {
    if(movingPoint == -1) return;
    PVector point = points.get(movingPoint);
    point.set(new PVector(mouseX, mouseY));
    movingPoint = -1;
  }
  
  void display() {
    for(int i = 0; i < count; i++) {
      PVector point = points.get(i);
      pushStyle();
      fill(128, 196, 128);
      stroke(255);
      if(movingPoint == i) {
        point = new PVector(mouseX, mouseY);
        fill(196, 64, 64); 
      }
      else if(mouseOver(i))
        fill(128, 128, 64);
      ellipse(point.x, point.y, 10, 10);
      popStyle();
    }
  }
}