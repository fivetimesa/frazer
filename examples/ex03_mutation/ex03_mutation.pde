import frazer.*;
import frazer.algorithms.*;

Frazer frazer;

int geneCount = 20;
int populationCount = 100;
String goal = "It seems to be working!";

FixCountMutation mutation;

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