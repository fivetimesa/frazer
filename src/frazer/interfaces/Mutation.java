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
package frazer.interfaces;

import frazer.constants.*;
import frazer.genotypes.*;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public interface Mutation {

   Genotype mutate(Genotype genes);

   /**
    * Mutate 
    * 
    * @param i
    * @param genes
    * @param gD
    */
   public default void mutateGene(int i, Genotype genes, GenotypeDescription gD) {
      MutationType mT = gD.getMutationType();
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

            if (mT == MutationType.INDIVIDUALRANGE || mT == MutationType.INDIVIDUALCONSTANT)
               mutationStrength = gD.getMutationStrength(i);

            if (mT == MutationType.RANGE || mT == MutationType.CONSTANT)
               mutationStrength = gD.getMutationStrength();

            if (mT == MutationType.INDIVIDUALRANGE || mT == MutationType.RANGE)
               mutationStrength = random.nextFloat() * mutationStrength * 2 - mutationStrength;

            if (mT == MutationType.CONSTANT || mT == MutationType.INDIVIDUALCONSTANT) {
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

   public default void mutateOneRandomGene(Genotype genes, GenotypeDescription gD)
   {
      Random r = new Random();
      mutateGene(r.nextInt(gD.geneCount), genes, gD);
   }
   
   public default void mutateNRandomGenes(int n, Genotype genes, GenotypeDescription gD)
   {
      Random r = new Random();
      for (int i = 0; i < n; i++) {
         mutateGene(r.nextInt(gD.geneCount), genes, gD);
      }
   }
   public default void mutateNRandomUniqueGenes(int n, Genotype genes, GenotypeDescription gD)
   {
      if(n > gD.geneCount)
         throw new IndexOutOfBoundsException("There are " + gD.geneCount
                 + "genes. Not more.");
      Random r = new Random();
      int[] swaped = new int[n];
      //swap gene and remember swaped
      for(int i = 0; i < n; i++) {
         int iRandom = r.nextInt(gD.geneCount - i);
         swaped[i] = iRandom;
         mutateGene(iRandom, genes, gD);
         
         Object temp = genes.getGene(gD.geneCount - i);
         genes.setGene(gD.geneCount - i, genes.getGene(iRandom));
         genes.setGene(iRandom, temp);
      }
      
      //unswap
      for(int i = n-1; i >= 0; i--) {
         Object temp = genes.getGene(gD.geneCount - i);
         genes.setGene(gD.geneCount - i, genes.getGene(swaped[i]));
         genes.setGene(swaped[i], temp);
      }
      
   }
   
   public default float limitValue(int i, float value, GenotypeDescription gD) {
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
