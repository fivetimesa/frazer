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

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Paweł Sikorski
 */
public class IntegerGenotype extends Genotype<Integer> {

   int[] genes;

   /**
    * Used only for copy method.
    */
   private IntegerGenotype() {
      //private constructor
   }

   /**
    * Creates genotype with integer values. Genes are stored in array. One gene
    * is represented by primitive type int.
    *
    * @param count genotype's size
    */
   public IntegerGenotype(int count) {
      genes = new int[count];
      this.randomInit();
   }

   /**
    * Creates genotype with integer values. Genes are stored in array. One gene
    * is represented by primitive type int.
    *
    * @param count genotype's size
    * @param randomInitialize if true, fires randomInit
    */
   public IntegerGenotype(int count, boolean randomInitialize) {
      genes = new int[count];
      if (randomInitialize)
         this.randomInit();
   }

   @Override
   protected Object getArrayInstance() {
      return genes;
   }

   /**
    * Copies genotype. Uses Arrays.copyOf() method.
    *
    * @return copy of IntegerGenotype instance
    */
   @Override
   public Genotype copy() {
      IntegerGenotype copy = new IntegerGenotype();
      copy.setGenes(Arrays.copyOf(genes, genes.length));
      return copy;
   }

   /**
    * Ranodomizes genes' values in range 0 to 100.
    */
   @Override
   public void randomInit() {
      randomInit(0, 100);
   }

   /**
    * Ranodomizes genes' values in range <code>min</code> to <code>max</code>.
    *
    * @param min
    * @param max
    */
   public void randomInit(int min, int max) {
      Random generator = new Random();
      for (int i = 0; i < genes.length; i++) {
         genes[i] = min + generator.nextInt() * (max - min);
      }
   }

   /**
    * Returns genotype length.
    *
    * @return array length
    */
   @Override
   public int getGeneCount() {
      return genes.length;
   }

   /**
    * Returns ith gene from genotype. Uses Integer object.
    *
    * @param i gene index
    * @return warped float
    */
   @Override
   public Integer getGene(int i) {
      return genes[i];
   }

   /**
    * Sets gene value with use of Integer.
    *
    * @param i gene index
    * @param value new gene value
    */
   @Override
   public void setGene(int i, Integer value) {
      genes[i] = value;
   }

   /**
    * Returns all genes in int array.
    *
    * @return
    */
   public int[] getGenes() {
      return Arrays.copyOf(genes, genes.length);
   }

   /**
    * Uses in copy method.
    *
    * @param thatGenes
    */
   private void setGenes(int[] thatGenes) {
      this.genes = thatGenes;
   }

      /**
    * Sets new value for ith gene. Uses primitive data type int.
    *
    * @param i gene index
    * @param value new value to set
    */
   @Override
   public void setInt(int i, int value) {
      genes[i] = value;
   }

   /**
    * Gets ith gene value. Uses primitive data type int.
    *
    * @param i gene index
    * @return
    */
   @Override
   public int getInt(int i) {
      return genes[i];
   }
   
   /**
    * Returns genotype as array's string.
    * 
    * @return
    */
   @Override
   public String toString() {
      return Arrays.toString(this.genes);
   }

   @Override
   public IntegerGenotypeIterator iterator() {
      return new IntegerGenotypeIterator();
   }

    @Override
    public void applyLimits(GenotypeDescription gD) {
        for (int i = 0; i < genes.length; i++) {
            genes[i] = (int) gD.limitGene(i, genes[i]);
        }
    }

   private class IntegerGenotypeIterator implements PrimitiveIterator.OfInt {

      int index;

      public IntegerGenotypeIterator() {
         this.index = 0;
      }

      @Override
      public int nextInt() {
         if (hasNext()) {
            return genes[index++]; //post-increment
         } else
            throw new NoSuchElementException();
      }

      @Override
      public boolean hasNext() {
         return index < genes.length;
      }
   }

}
