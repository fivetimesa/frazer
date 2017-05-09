import frazer.*;

Frazer frazer;

Population pop;

Genotype myGenom;
int geneCount = 20;

void setup()
{
  frazer = new Frazer(this);
  myGenom = new BitGenotype(geneCount);
  
  println("GENOM:");
  for(int i = 0; i < geneCount; i++)
    print((boolean)myGenom.getGene(i) ? '0' : '1');
}