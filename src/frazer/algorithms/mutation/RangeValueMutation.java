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

import frazer.genotypes.Genotype;
import frazer.interfaces.Mutation;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class RangeValueMutation extends ConstantValueMutation implements Mutation {

   public RangeValueMutation(float mutationStrength) {
      super(mutationStrength);
   }

   public RangeValueMutation(float[] individualMutationStrength) {
      super(individualMutationStrength);
   }

   public RangeValueMutation(int mutationCount, boolean setUnique, float mutationStrength) {
      super(mutationCount, setUnique, mutationStrength);
   }

   public RangeValueMutation(int mutationCount, boolean setUnique, float[] individualMutationStrength) {
      super(mutationCount, setUnique, individualMutationStrength);
   }

   public RangeValueMutation(float mutationChance, float mutationStrength) {
      super(mutationChance, mutationStrength);
   }

   public RangeValueMutation(float mutationChance, float[] individualMutationStrength) {
      super(mutationChance, individualMutationStrength);
   }

   @Override
   protected void setGeneMutation() {
      switch (gD.getGenotypeType()) {
         case BIT:
            throw new RuntimeException("Can't use RangeValueMutation on BitGenotype");
         case FLOAT:
         case SFLOAT:
            if (isIndividual)
               geneMutation = new GeneMutation() {
                  @Override
                  void mutate(int i, Genotype genes) {
                     float value = genes.getFloat(i);
                     float newValue = newGeneRangeValue(value, individualMutationStrength[i]);
                     genes.setFloat(i, gD.limitGene(i, newValue));
                  }
               };
            else
               geneMutation = new GeneMutation() {
                  @Override
                  void mutate(int i, Genotype genes) {
                     float value = genes.getFloat(i);
                     float newValue = newGeneRangeValue(value, mutationStrength);
                     genes.setFloat(i, gD.limitGene(i, newValue));
                  }
               };
            break;
         case INTEGER:
            if (isIndividual)
               geneMutation = new GeneMutation() {
                  @Override
                  void mutate(int i, Genotype genes) {
                     float value = (float) genes.getInt(i);
                     float newValue = newGeneRangeValue(value, individualMutationStrength[i]);
                     genes.setInt(i, (int) gD.limitGene(i, newValue));
                  }
               };
            else
               geneMutation = new GeneMutation() {
                  @Override
                  void mutate(int i, Genotype genes) {
                     float value = (float) genes.getInt(i);
                     float newValue = newGeneRangeValue(value, mutationStrength);
                     genes.setInt(i, (int) gD.limitGene(i, newValue));
                  }
               };
            break;
      }
   }

}
