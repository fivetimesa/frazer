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
public class ChanceMutantSelection implements MutantSelection {

   private float mutantSelectionChance;

   public ChanceMutantSelection() {
      this.mutantSelectionChance = 0.01f;
   }

   public ChanceMutantSelection(float mutantSelectionChance) {
      this.mutantSelectionChance = mutantSelectionChance;
   }

   @Override
   public Specimen[] selectMutants(Population population) {
      Random random = new Random();
      ArrayList<Specimen> selectedMutants = new ArrayList<>();
      Specimen[] specimens = population.getSpecimens();
        for(int i = 0; i < specimens.length; i++) {
            if(random.nextFloat() < mutantSelectionChance)
               selectedMutants.add(specimens[i]);
        }
        return (Specimen[]) selectedMutants.toArray();
   }

   public void setMutantSelectionChance(float mutantSelectionChance) {
      this.mutantSelectionChance = mutantSelectionChance;
   }

   public float getMutantSelectionChance() {
      return mutantSelectionChance;
   }
}
