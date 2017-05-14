import frazer.*;

Frazer frazer;
Specimen best;

int geneCount = 20;
int populationCount = 100;
String goal = "It seems to be working!";

void setup()
{
  geneCount = goal.length();
  frazer = new Frazer(this, populationCount, geneCount, GenotypeType.FLOAT, new MyFitness());
}

void draw() {
  Specimen best = frazer.evolve(1);
  println(geontypeToString(best.getGenes()));
}


class MyFitness implements Fitness {
  float calculateFitness(Genotype genes) {
    float difference = random(1);
    /*
    for(int i = 0; i < geneCount; i++) {
      char currentChar = geneToChar((float)genes.getGene(i));
      char goalChar = goal.charAt(i);
      difference += abs(int(goalChar) - int(currentChar));
    }*/
    return difference;
  }
}



char geneToChar(float gene) {
  return char(int(map(sin(gene), -1, 1, 0, 255)));
}

String geontypeToString(Genotype genes) {
  StringBuilder sb = new StringBuilder();
  for(Object g: genes) {
    char c = geneToChar((float)g);
    sb.append(c);
  }
  return sb.toString();
}