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
import frazer.constants.*;
import frazer.genotypes.*;
import java.util.Random;

/**
 * Describes Mutation for population of specimen.
 * 
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public interface Mutation {

   /**
    * This method should select specimen from population and run on it
    * {@link frazer.interfaces.Mutation#mutate(frazer.genotypes.Genotype)}
    * 
    * @param population
    * @return
    */
   default Population mutate(Population population){
      return population;
   }
   
   /**
    * This method should change genotype in some way.
    * 
    * @param genes
    * @return
    */
   Genotype mutate(Genotype genes);

}
