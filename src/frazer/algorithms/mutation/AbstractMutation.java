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
package frazer.algorithms.mutation;

import frazer.constants.GenotypeMutationType;
import frazer.constants.Limit;
import frazer.genotypes.*;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
abstract public class AbstractMutation {

   GenotypeDescription gD;
   GenotypeMutationType genotypeMutationType;
   int mutationCount;
   float mutationChance;
   GeneMutation geneMutation;
   GeneLimit geneLimit;
   protected Random random;

   protected AbstractMutation() {
      this.random = new Random();
      this.genotypeMutationType = GenotypeMutationType.CHANCE;
      this.mutationChance = 0.01f;
   }

   protected AbstractMutation(int mutationCount, boolean setUnique) {
      this.random = new Random();
      this.mutationCount = mutationCount;

      if (mutationCount < 0)
         throw new RuntimeException("Mutation count can't be negative!");

      if (mutationCount == 0)
         this.genotypeMutationType = GenotypeMutationType.NOMUTATION;

      if (mutationCount == 1)
         this.genotypeMutationType = GenotypeMutationType.ONE;

      if (mutationCount > 1 && setUnique == false)
         this.genotypeMutationType = GenotypeMutationType.NRANDOM;

      if (mutationCount > 1 && setUnique == true)
         this.genotypeMutationType = GenotypeMutationType.NUNIQUERANDOM;
   }

   protected AbstractMutation(float mutationChance) {
      this.random = new Random();
      this.genotypeMutationType = GenotypeMutationType.CHANCE;
      this.mutationChance = mutationChance;
   }

   abstract public void mutateGene(int i, Genotype genes);

   public void mutateGenotypeByMutationType(Genotype genes) {
      switch (genotypeMutationType) {
         case NOMUTATION:
            break;
         case ONE:
            mutateOneRandomGene(genes);
            break;
         case CHANCE:
            mutateGeneWithChance(genes);
            break;
         case NRANDOM:
            mutateNRandomGenes(mutationCount, genes);
            break;
         case NUNIQUERANDOM:
            mutateNRandomUniqueGenes(mutationCount, genes);
            break;
      }
   }

   /**
    * Mutation of all genes with chance to occure.
    *
    * @param genes genotype
    */
   private void mutateGeneWithChance(Genotype genes) {
      for (int i = 0; i < gD.geneCount; i++) {
         if (random.nextFloat() > mutationChance)
            continue;
         mutateGene(i, genes);
      }
   }

   /**
    * Mutates one random gene in genotype.
    *
    * @param genes genotype
    */
   public void mutateOneRandomGene(Genotype genes) {
      mutateGene(random.nextInt(gD.geneCount), genes);
   }

   /**
    * Mutates <code>n</code> random genes in genotype. Gene can mutate more than
    * once.
    *
    * @param n number of genes to mutate
    * @param genes genotype
    */
   public void mutateNRandomGenes(int n, Genotype genes) {
      for (int i = 0; i < n; i++) {
         mutateGene(random.nextInt(gD.geneCount), genes);
      }
   }

   /**
    * Mutates <code>n</code> random unique genes in genotype. Gene mutates only
    * once.
    *
    * @param n number of genes to mutate (must be smaller than
    * <code>geneCount</code>)
    * @param genes
    */
   public void mutateNRandomUniqueGenes(int n, Genotype genes) {
      if (n > gD.geneCount)
         throw new IndexOutOfBoundsException("There are " + gD.geneCount
                 + "genes. Not more.");
      int[] swaped = new int[n];
      //swap gene and remember swaped
      for (int i = 0; i < n; i++) {
         int iRandom = random.nextInt(gD.geneCount - i);
         swaped[i] = iRandom;
         mutateGene(iRandom, genes);

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

   public void setGenotypeDescription(GenotypeDescription gD) {
      this.gD = gD;
      setGeneLimit();
   }

   private void setGeneLimit() {
      Limit limit = gD.getLimit();
      switch (limit) {
         case NOLIMIT:
            geneLimit = new GeneLimit() {
               @Override
               float limit(int i, float value) {
                  return value;
               }
            };
            break;
         case FORALL:
            geneLimit = new GeneLimit() {
               @Override
               float limit(int i, float value) {
                  float max;
                  float min;
                  max = gD.getMax();
                  min = gD.getMin();
                  if (value < min)
                     return min;
                  if (value > max)
                     return max;
                  return value;
               }
            };
            break;
         case INDIVIDUAL:
            geneLimit = new GeneLimit() {
               @Override
               float limit(int i, float value) {
                  float max;
                  float min;
                  max = gD.getMax(i);
                  min = gD.getMin(i);
                  if (value < min)
                     return min;
                  if (value > max)
                     return max;
                  return value;
               }
            };
            break;
         case NORMALIZE:
            geneLimit = new GeneLimit() {
               @Override
               float limit(int i, float value) {
                  if (value < 0)
                     return 0;
                  if (value > 1)
                     return 1;
                  return value;
               }
            };
      }
   }

   abstract class GeneMutation {

      abstract void mutate(int i, Genotype genes);

      float newGeneValue(float value, float strength) {
         if (random.nextFloat() < 0.5)
            return value + strength;
         else
            return value - strength;
      }

      float newGeneRangeValue(float value, float strength) {
         strength = random.nextFloat() * strength * 2 - strength;
         return value + strength;
      }
   }

   abstract class GeneLimit {

      abstract float limit(int i, float value);
   }
}
