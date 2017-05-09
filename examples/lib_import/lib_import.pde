import frazer.*;

Frazer frazer;

Population pop;

Genotype myGenome;
int geneCount = 20;

void setup()
{
  frazer = new Frazer(this);
  myGenome = new BitGenotype(geneCount);
  
  println("GENOME:");
  for(int i = 0; i < geneCount; i++)
    print((boolean)myGenome.getGene(i) ? '0' : '1');
}
