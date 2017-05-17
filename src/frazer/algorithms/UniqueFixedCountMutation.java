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
import frazer.Population;
import frazer.Specimen;
import frazer.constants.*;
import frazer.genotypes.Genotype;
import frazer.genotypes.GenotypeDescription;
import frazer.interfaces.Mutation;
import java.util.Random;
/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class UniqueFixedCountMutation extends AbstractMutation implements Mutation{
   ValueType mutationCountType;
   int mutatedSpecimenCount;
   float populationPrecentage;
   
   public UniqueFixedCountMutation() {
      this.mutatedSpecimenCount = 1;
      this.mutationCountType = ValueType.ABSOLUTE;
   }

   public UniqueFixedCountMutation(int mutationCount) {
      this.mutatedSpecimenCount = mutationCount;
      this.mutationCountType = ValueType.ABSOLUTE;
   }

   public UniqueFixedCountMutation(float precentage, Population population) {
      this.mutationCountType = ValueType.PERCENTAGE;
      this.populationPrecentage = precentage;
      this.mutatedSpecimenCount = (int)(population.getCount()*populationPrecentage);
      
      if(mutatedSpecimenCount < 1)
         mutatedSpecimenCount = 1;
   }

   @Override
   public Population mutate(Population population) {
      GenotypeDescription gD = population.getGenotypeDescription();
      Specimen[] specimens = population.getSpecimens();
      int populationCount = population.getCount();
      throw new UnsupportedOperationException("Not supported yet.");
      /*
      Random r = new Random();
      for (int i = 0; i < mutatedSpecimenCount; i++) {
         int randomIndex = r.nextInt(populationCount);
         mutateGenotypeByMutationType(specimens[randomIndex].getGenes(), gD);
      }

      return population;
      */
   }
   
   
   @Override
   public Genotype mutate(Genotype genes) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
   
}
