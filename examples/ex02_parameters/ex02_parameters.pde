import frazer.*;

Frazer frazer;
Specimen best;

int geneCount = 20;
int populationCount = 1000;
String goal = "It seems to be working!";

void setup()
{
  size(500, 200);
  textAlign(CENTER);
  geneCount = goal.length();
  frazer = new Frazer(this, populationCount, geneCount, GenotypeType.FLOAT, new MyFitness());
  
  ((TournamentMating) frazer.getMating()).setParentsCount(3);
  ((CrossoverBreeding) frazer.getBreeding()).setCrossoverPointsCount(5);
}

void draw() {
  background(0);
  Specimen best = frazer.evolve(1);
  text(Utility.floatGeontypeToString(best.getGenes()), width/2, height/2);
}


class MyFitness implements Fitness {
  float calculateFitness(Genotype genes) {
    float difference = 0;
    
    for(int i = 0; i < geneCount; i++) {
      char currentChar = Utility.floatGeneToChar((float)genes.getGene(i));
      char goalChar = goal.charAt(i);
      difference += abs(int(goalChar) - int(currentChar));
    }
    return difference;
  }
}