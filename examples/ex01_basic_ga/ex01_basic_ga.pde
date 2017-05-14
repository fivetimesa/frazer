import frazer.*;

Frazer frazer;

int geneCount = 20;
int populationCount = 100;

void setup()
{
  frazer = new Frazer(this, populationCount, geneCount, GenotypeType.FLOAT, new MyFitness());
  
}

void draw() {
  
}


class MyFitness implements Fitness {
  float calculateFitness(Genotype genes) {
    return 0;
  }
}