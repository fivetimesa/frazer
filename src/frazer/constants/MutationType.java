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
package frazer.constants;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public enum MutationType {
<<<<<<< .merge_file_LmF5ma
    CONSTANTVALUE, RANGEVALUE, PERCENTAGEVALUE, BIT,
    INDIVIDUALRANGEVALUE, PERCENTAGERANGEVALUE
=======
   /**
    * The simpleset mutation - 0 to 1 or 1 to 0.
    */
   BIT,

   /**
    * Mutation with constant value [-mutationStrength, mutationStrength]
    * the same for all genes.
    */
    CONSTANT, 

   /**
    * Mutation with constant value [-mutationStrength_i, mutationStrength_i]
    * individual for spcific gene.
    */
   INDIVIDUALCONSTANT,

   /**
    * Mutation by random value in range [-mutationStrength, ..., mutationStrength]
    * the same for all genes.
    */
   RANGE,

   /**
    * Mutation by random value in range [-mutationStrength, ..., mutationStrength]
    * individual for spcific gene.
    */
   INDIVIDUALRANGE
>>>>>>> .merge_file_6r7P9N
}
