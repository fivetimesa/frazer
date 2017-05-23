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
import frazer.genotypes.GenotypeDescription;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class ConstantValueMutation extends AbstractMutation implements Mutation {

   float mutationStrength;
   float[] individualMutationStrength;
   boolean isIndividual;

   public ConstantValueMutation(float mutationStrength) {
      super();
      this.mutationStrength = mutationStrength;
      this.isIndividual = false;
   }

   public ConstantValueMutation(float[] individualMutationStrength) {
      super();
      this.individualMutationStrength = individualMutationStrength;
      this.isIndividual = true;
   }

   public ConstantValueMutation(int mutationCount, boolean setUnique, float mutationStrength) {
      super(mutationCount, setUnique);
      this.mutationStrength = mutationStrength;
      this.isIndividual = false;
   }

   public ConstantValueMutation(int mutationCount, boolean setUnique, float[] individualMutationStrength) {
      super(mutationCount, setUnique);
      this.individualMutationStrength = individualMutationStrength;
      this.isIndividual = true;
   }

   public ConstantValueMutation(float mutationChance, float mutationStrength) {
      super(mutationChance);
      this.mutationStrength = mutationStrength;
      this.isIndividual = false;
   }

   public ConstantValueMutation(float mutationChance, float[] individualMutationStrength) {
      super(mutationChance);
      this.individualMutationStrength = individualMutationStrength;
      this.isIndividual = true;
   }

   @Override
   public void mutateGene(int i, Genotype genes) {
      geneMutation.mutate(i, genes);
   }

   @Override
   public Genotype mutate(Genotype genes) {
      mutateGenotypeByMutationType(genes);
      return genes;
   }

   @Override
   public void initialize(GenotypeDescription gD) {
      super.initialize(gD);
      setGeneMutation();
   }

   protected void setGeneMutation() {
      switch (gD.getGenotypeType()) {
         case BIT:
            throw new RuntimeException("Can't use ConstantValueMutation on BitGenotype");
         case FLOAT:
         case SFLOAT:
            if (isIndividual)
               geneMutation = new GeneMutation() {
                  @Override
                  void mutate(int i, Genotype genes) {
                     float value = genes.getFloat(i);
                     float newValue = newGeneValue(value, individualMutationStrength[i]);
                     genes.setFloat(i, gD.limitGene(i, newValue));
                  }
               };
            else
               geneMutation = new GeneMutation() {
                  @Override
                  void mutate(int i, Genotype genes) {
                     float value = genes.getFloat(i);
                     float newValue = newGeneValue(value, mutationStrength);
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
                     float newValue = newGeneValue(value, individualMutationStrength[i]);
                     genes.setInt(i, (int) gD.limitGene(i, newValue));
                  }
               };
            else
               geneMutation = new GeneMutation() {
                  @Override
                  void mutate(int i, Genotype genes) {
                     float value = (float) genes.getInt(i);
                     float newValue = newGeneValue(value, mutationStrength);
                     genes.setInt(i, (int) gD.limitGene(i, newValue));
                  }
               };
            break;
      }
   }
}
