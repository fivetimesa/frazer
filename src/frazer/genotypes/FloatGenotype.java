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
package frazer.genotypes;

import java.util.Random;
import java.util.Arrays;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class FloatGenotype extends Genotype<Float> {

   public FloatGenotype(int count) {
      super(Float.class, count);
      this.randomInit();
   }

   @Override
   public Genotype copy() {
      FloatGenotype copy = new FloatGenotype(genes.length);
      for (int i = 0; i < genes.length; i++) {
         copy.setGene(i, getGene(i));
      }
      return copy;
   }

   @Override
   public void randomInit() {
      Random generator = new Random();
      for (int i = 0; i < genes.length; i++) {
         genes[i] = generator.nextFloat();
      }
   }

   /**
    *
    * @param min
    * @param max
    */
   public void randomInit(float min, float max) {
      Random generator = new Random();
      for (int i = 0; i < genes.length; i++) {
         genes[i] = min + generator.nextFloat() * (max - min);
      }
   }

   @Override
   public Float getGene(int i) {
      rangeCheck(i);
      return genes[i];
   }

   public void setGene(int i, float v) {
      rangeCheck(i);
      genes[i] = v;
   }

   public Float[] getGenes() {
      return Arrays.copyOf(genes, genes.length);
   }

   @Override
   public String toString() {
      return Arrays.toString(genes);
   }
}
