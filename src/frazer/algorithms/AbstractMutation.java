/*
 * Copyright (C) 2017 Teodor Michalski, Maciek Bajor, Paweł Sikorski
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package frazer.algorithms;

import frazer.constants.GeneMutationType;
import frazer.constants.GenotypeMutationType;
import frazer.constants.ValueType;
import frazer.genotypes.*;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
abstract public class AbstractMutation {

   /**
    * Mutates ith gene in genotype.
    *
    * @param i gene index
    * @param genes genotype
    * @param gD genotype description
    */
   public void mutateGene(int i, Genotype genes, GenotypeDescription gD) {
      GeneMutationType mT = gD.getGeneMutationType();
      ValueType mV = gD.getMutationValueType();

      switch (mT) {
         case BIT:
            if (!genes.getClass().equals(BitGenotype.class)) {
               System.err.print("Genotype type mismatch. Mutation expected: BitGenotype");
            }
            BitGenotype bitGenes = (BitGenotype) genes;
            bitGenes.setGene(i, !bitGenes.getGene(i));
            break;
         case CONSTANT:
         case INDIVIDUALCONSTANT:
         case RANGE:
         case INDIVIDUALRANGE:
            if ((genes.getClass().equals(BitGenotype.class))) {
               System.err.print("Genotype type mismatch. "
                       + "Mutation expected: FloatGenotype, SFloatGenotype, IntegerGenotype");
            }

            float newValue = 0;
            float mutationStrength = 0;

            Random random = new Random();

            if (mT == GeneMutationType.INDIVIDUALRANGE || mT == GeneMutationType.INDIVIDUALCONSTANT)
               mutationStrength = gD.getMutationStrength(i);

            if (mT == GeneMutationType.RANGE || mT == GeneMutationType.CONSTANT)
               mutationStrength = gD.getMutationStrength();

            if (mT == GeneMutationType.INDIVIDUALRANGE || mT == GeneMutationType.RANGE)
               mutationStrength = random.nextFloat() * mutationStrength * 2 - mutationStrength;

            if (mT == GeneMutationType.CONSTANT || mT == GeneMutationType.INDIVIDUALCONSTANT) {
               if (random.nextFloat() > 0.5)
                  mutationStrength = -mutationStrength;
            }

            if (genes.getClass().equals(IntegerGenotype.class)) {
               IntegerGenotype integerGenes = (IntegerGenotype) genes;
               float value = (float) integerGenes.getGene(i);

               if (mV == ValueType.PERCENTAGE) {
                  newValue = value * (1 + mutationStrength);
               }
               if (mV == ValueType.ABSOLUTE) {
                  newValue = value + mutationStrength;
               }
               integerGenes.setGene(i, (int) limitValue(i, newValue, gD));

            } else {
               FloatGenotype floatGenes = (FloatGenotype) genes;
               float value = floatGenes.getGene(i);

               if (mV == ValueType.PERCENTAGE) {
                  newValue = value * (1 + mutationStrength);
               }
               if (mV == ValueType.ABSOLUTE) {
                  newValue = value + mutationStrength;
               }
               floatGenes.setGene(i, limitValue(i, newValue, gD));
            }
            break;
      }
   }

   public void mutateGenotypeByMutationType(Genotype genes, GenotypeDescription gD) {
      GenotypeMutationType genotypeMT = gD.getGenotypeMutationType();
      switch (genotypeMT) {
         case ONE:
            mutateOneRandomGene(genes, gD);
            break;
         case CHANCE:
            mutateGeneWithChance(genes, gD);
            break;
         case NRANDOM:
            mutateNRandomGenes(gD.getMutationCount(), genes, gD);
            break;
         case NUNIQUERANDOM:
            mutateNRandomUniqueGenes(gD.getMutationCount(), genes, gD);
            break;
      }
   }

   /**
    * Mutation of all genes with chance to occure.
    *
    * @param genes genotype
    * @param gD genotype description
    */
   private void mutateGeneWithChance(Genotype genes, GenotypeDescription gD) {
      float mutatinoChance = gD.getMutationChance();
      Random random = new Random();
        for(int i = 0; i < gD.geneCount; i++) {
            if(random.nextFloat() > mutatinoChance) continue;
            mutateGene(i, genes, gD);
        }
   }

   /**
    * Mutates one random gene in genotype.
    *
    * @param genes genotype
    * @param gD genotype description
    */
   public void mutateOneRandomGene(Genotype genes, GenotypeDescription gD) {
      Random r = new Random();
      mutateGene(r.nextInt(gD.geneCount), genes, gD);
   }

   /**
    * Mutates <code>n</code> random genes in genotype. Gene can mutate more than
    * once.
    *
    * @param n number of genes to mutate
    * @param genes genotype
    * @param gD genotype description
    */
   public void mutateNRandomGenes(int n, Genotype genes, GenotypeDescription gD) {
      Random r = new Random();
      for (int i = 0; i < n; i++) {
         mutateGene(r.nextInt(gD.geneCount), genes, gD);
      }
   }

   /**
    * Mutates <code>n</code> random unique genes in genotype. Gene mutates only
    * once.
    *
    * @param n number of genes to mutate (must be smaller than
    * <code>geneCount</code>)
    * @param genes
    * @param gD
    */
   public void mutateNRandomUniqueGenes(int n, Genotype genes, GenotypeDescription gD) {
      if (n > gD.geneCount)
         throw new IndexOutOfBoundsException("There are " + gD.geneCount
                 + "genes. Not more.");
      Random r = new Random();
      int[] swaped = new int[n];
      //swap gene and remember swaped
      for (int i = 0; i < n; i++) {
         int iRandom = r.nextInt(gD.geneCount - i);
         swaped[i] = iRandom;
         mutateGene(iRandom, genes, gD);

         Object temp = genes.getGene(gD.geneCount - i);
         genes.setGene(gD.geneCount - i, genes.getGene(iRandom));
         genes.setGene(iRandom, temp);
      }

      //unswap
      for (int i = n - 1; i >= 0; i--) {
         Object temp = genes.getGene(gD.geneCount - i);
         genes.setGene(gD.geneCount - i, genes.getGene(swaped[i]));
         genes.setGene(swaped[i], temp);
      }

   }

   protected float limitValue(int i, float value, GenotypeDescription gD) {
      switch (gD.getLimit()) {
         case NOLIMIT:
            return value;
         case NORMALIZE:
            if (value < 0)
               return 0;
            if (value > 1)
               return 1;
            break;
         case FORALL:
            float max;
            float min;
            max = gD.getMax();
            min = gD.getMin();
            if (value < min)
               return min;
            if (value > max)
               return max;
            break;
         case INDIVIDUAL:
            max = gD.getMax(i);
            min = gD.getMin(i);
            if (value < min)
               return min;
            if (value > max)
               return max;
            break;
      }
      return value;
   }

}
