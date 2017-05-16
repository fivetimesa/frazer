import frazer.*;

Frazer frazer;
Specimen best;

String goal = "It seems to be working!";
boolean finished = false;

void setup()
{
  size(500, 200);
  textAlign(CENTER);
  
  int populationCount = 100;
  int geneCount = goal.length();
  frazer = new Frazer(this, populationCount, geneCount, GenotypeType.FLOAT, new MyFitness());
}

void draw() {
  if(!finished) best = frazer.evolve(1);
  if(best.getFitnessScore() == 0) finished = true;
  
  background(0);
  text(Utility.floatGeontypeToString(best.getGenes()), width/2, height/2);
}

class MyFitness implements Fitness {
  float calculateFitness(Genotype genes) {
    float difference = 0;
    String phenotype = Utility.floatGeontypeToString(genes);
    
    for(int i = 0; i < phenotype.length(); i++) {
      char currentChar = phenotype.charAt(i);
      char goalChar = goal.charAt(i);
      difference += abs(int(goalChar) - int(currentChar));
    }
    return difference;
  }
}