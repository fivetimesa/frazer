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
package frazer.algorithms.mutantselection;

import frazer.Population;
import frazer.Specimen;
import frazer.interfaces.MutantSelection;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class FixedCountMutantSelection implements MutantSelection {

   int mutantSelectionCount;
   int populationCount;
   float populationPrecentage;

   boolean setPopulationCount;
   boolean setMutantSelectionCount;

   public FixedCountMutantSelection() {
      this.mutantSelectionCount = 1;
      this.setMutantSelectionCount = true;
   }

   public FixedCountMutantSelection(int mutationCount) {
      this.mutantSelectionCount = mutationCount;
      this.setMutantSelectionCount = true;
   }

   public FixedCountMutantSelection(float precentage) {
      this.populationPrecentage = precentage;
   }

   @Override
   public Specimen[] selectMutants(Population population) {
      if (setPopulationCount == false) {
         populationCount = population.getCount();
         setPopulationCount = true;
      }

      if (setMutantSelectionCount == false) {
         this.mutantSelectionCount = (int) (populationCount * populationPrecentage);

         if (mutantSelectionCount < 1)
            mutantSelectionCount = 1;

         setMutantSelectionCount = true;
      }

      Random random = new Random();
      ArrayList<Specimen> selectedMutants = new ArrayList<>();
      Specimen[] specimens = population.getSpecimens();
      for (int i = 0; i < mutantSelectionCount; i++) {
         selectedMutants.add(specimens[random.nextInt(populationCount)]);
      }
      return (Specimen[]) selectedMutants.toArray();
   }

   public int getMutantSelectionCount() {
      return mutantSelectionCount;
   }

   public float getPopulationPrecentage() {
      return populationPrecentage;
   }

   public void setMutantSelectionCount(int mutantSelectionCount) {
      this.mutantSelectionCount = mutantSelectionCount;
      this.setMutantSelectionCount = true;
      
   }

   final public void setPopulationPrecentage(float populationPrecentage) {
      this.populationPrecentage = populationPrecentage;
      this.setMutantSelectionCount = false;
   }
}
