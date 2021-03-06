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
import frazer.Population;
import frazer.Specimen;
/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public interface MutantSelection extends AlgorithmsInterface{
   
      /**
    * This method should select specimen from population to mutate.
    * Mutation is provided by 
    * {@link frazer.interfaces.Mutation#mutate(frazer.genotypes.Genotype)}.
    * 
    * @param population
    * @return specimens to mutate
    */
   public Specimen[] selectMutants(Population population);
   
   public default boolean needFitness()
   {
      return false;
   }
}
