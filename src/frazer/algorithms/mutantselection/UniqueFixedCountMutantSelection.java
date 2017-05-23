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
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class UniqueFixedCountMutantSelection extends FixedCountMutantSelection implements MutantSelection {

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

      if (mutantSelectionCount > populationCount)
         mutantSelectionCount = populationCount;

      Random random = new Random();
      ArrayList<Specimen> selectedMutants = new ArrayList<>();
      Specimen[] specimens = population.getSpecimens();
      int[] swaped = new int[mutantSelectionCount];
      for (int i = 0; i < mutantSelectionCount; i++) {
         int iRandom = random.nextInt(populationCount - i);
         selectedMutants.add(specimens[iRandom]);

         //swap
         swaped[i] = iRandom;
         Specimen temp = specimens[populationCount - i];
         specimens[populationCount - i] = specimens[iRandom];
         specimens[iRandom] = temp;
      }

      //unswap 
      for (int i = populationCount - 1; i >= 0; i--) {
         Specimen temp = specimens[populationCount - i];
         specimens[populationCount - i] = specimens[swaped[i]];
         specimens[swaped[i]] = temp;
      }

      return selectedMutants.toArray(new Specimen[0]);
   }
}
