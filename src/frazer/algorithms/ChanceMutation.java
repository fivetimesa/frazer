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
import frazer.genotypes.Genotype;
import frazer.interfaces.Mutation;
/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class ChanceMutation extends AbstractMutation implements Mutation{

   @Override
   public Population mutate(Population population) {
      return Mutation.super.mutate(population); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public Genotype mutate(Genotype genes) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
   
}
