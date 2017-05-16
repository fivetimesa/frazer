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

<<<<<<< .merge_file_MpHxJV
import frazer.genotypes.Genotype;
import frazer.genotypes.GenotypeDescription;
import frazer.constants.*;
import frazer.genotypes.FloatGenotype;
import frazer.genotypes.BitGenotype;
=======
import frazer.constants.*;
import frazer.genotypes.*;
>>>>>>> .merge_file_ERxtg2
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public interface Mutation {

   Genotype mutate(Genotype genes);

   public default void mutateGene(int i, Genotype genes, GenotypeDescription gD) {
      MutationType mT = gD.getMutationType();
<<<<<<< .merge_file_MpHxJV
      MutationValue mV = gD.getMutationValue();

      switch (mT) {
         case BIT:
            BitGenotype bitGenes = (BitGenotype) genes;
            boolean booleanValue = bitGenes.getGene(i);
            bitGenes.setGene(i, booleanValue);
            break;
         case CONSTANTVALUE:
         case RANGEVALUE:
         case INDIVIDUALRANGEVALUE:
            //sprawdzać za pomocą instanceOf
            if (!genes.getClass().equals(FloatGenotype.class)) {
               System.err.print("Genotype type mismatch. Mutation expected: FloatGenotype");
            }
            FloatGenotype floatGenes = (FloatGenotype) genes;

            float newValue = 0;
            float mutationScale = 0;

            Random random = new Random();
            if (mT == MutationType.RANGEVALUE) {
               mutationScale = gD.getMutationScale();
               mutationScale = random.nextFloat() * mutationScale * 2 - mutationScale;
            }
            if (mT == MutationType.INDIVIDUALRANGEVALUE) {
               mutationScale = gD.getMutationScale(i);
               mutationScale = random.nextFloat() * mutationScale * 2 - mutationScale;
            }
            if (mT == MutationType.CONSTANTVALUE) {
               mutationScale = gD.getMutationScale();
               if (random.nextFloat() > 0.5)
                  mutationScale = -mutationScale;
            }

            float value = floatGenes.getGene(i);

            if (mV == MutationValue.PERCENTAGE) {
               newValue = value * (1 + mutationScale);
            }
            if (mV == MutationValue.VALUE) {
               newValue = value + mutationScale;
            }

            floatGenes.setGene(i, newValue);
=======
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

               integerGenes.setGene(i, (int) newValue);
            } else {
               FloatGenotype floatGenes = (FloatGenotype) genes;
               float value = floatGenes.getGene(i);

               if (mV == ValueType.PERCENTAGE) {
                  newValue = value * (1 + mutationStrength);
               }
               if (mV == ValueType.ABSOLUTE) {
                  newValue = value + mutationStrength;
               }

               floatGenes.setGene(i, newValue);
            }

>>>>>>> .merge_file_ERxtg2
            break;
      }
   }
}
