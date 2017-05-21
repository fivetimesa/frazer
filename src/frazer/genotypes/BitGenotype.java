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
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Paweł Sikorski
 */
public class BitGenotype extends Genotype<Boolean> {

   private boolean[] genes;

   /**
    * Used only for copy method.
    */
   private BitGenotype() {
      //private constructor
   }

   /**
    * Creates genotype with bit values. Genes are stored in array. One gene is
    * represented by primitive type boolean. Genes has random value.
    *
    * @param count genotype's size
    */
   public BitGenotype(int count) {
      genes = new boolean[count];
      this.randomInit();
   }

   /**
    * Creates genotype with bit values. Genes are stored in array. One gene is
    * represented by primitive type boolean. Genes has random value.
    *
    * @param count genotype's size
    * @param randomInitialize if true, fires randomInit
    */
   public BitGenotype(int count, boolean randomInitialize) {
      genes = new boolean[count];
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
    * @return copy of BitGenotype instance
    */
   @Override
   public Genotype copy() {
      BitGenotype copy = new BitGenotype();
      copy.setGenes(Arrays.copyOf(genes, genes.length));
      return copy;
   }

   /**
    * Ranodomizes genes' values sets one or zero. (true or flase)
    */
   @Override
   public void randomInit() {
      Random r = new Random();
      for (int i = 0; i < genes.length; i++) {
         genes[i] = r.nextBoolean();
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
    * Returns ith gene from genotype. Uses Boolean object.
    *
    * @param i gene index
    * @return warped boolean
    */
   @Override
   public Boolean getGene(int i) {
      rangeCheck(i);
      return genes[i];
   }

   /**
    * Sets gene value with use of Boolean.
    *
    * @param i gene index
    * @param b new gene value
    */
   public void setGene(int i, Boolean b) {
      rangeCheck(i);
      genes[i] = b;
   }

   /**
    * Returns all genes in boolean array.
    *
    * @return
    */
   public boolean[] getGenes() {
      return Arrays.copyOf(genes, genes.length);
   }

   /**
    * Uses in copy method.
    *
    * @param thatGenes
    */
   private void setGenes(boolean[] thatGenes) {
      this.genes = thatGenes;
   }

   /**
    * Sets new value for ith gene.
    *
    * @param i gene index
    * @param value new value to set
    */
   @Override
   public void setBoolean(int i, boolean value) {
      genes[i] = value;
   }

   /**
    * Gets ith gene value.
    *
    * @param i gene index
    * @return
    */
   @Override
   public boolean getBoolean(int i) {
      return genes[i];
   }

   /**
    * Creates "00101011110" type text representation.
    *
    * @return a String of genotype length
    */
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(genes.length + 2);
      for (int i = 0; i < genes.length; i++) {
         if (genes[i])
            sb.append('1');
         else
            sb.append('0');
      }
      return sb.toString(); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public BitGenotypeIterator iterator() {
      return new BitGenotypeIterator();
   }

   public static interface PrimitiveIteratorOfBoolean extends PrimitiveIterator<Boolean, BooleanConsumer> {

      public boolean nextBoolean();

      @Override
      public default Boolean next() {
         return nextBoolean();
      }

      @Override
      public default void forEachRemaining(BooleanConsumer action) {
         Objects.requireNonNull(action);
         while (hasNext()) {
            action.accept(nextBoolean());
         }
      }
   }

   public class BitGenotypeIterator implements PrimitiveIteratorOfBoolean {

      int index;

      private BitGenotypeIterator() {
         this.index = 0;
      }

      @Override
      public boolean hasNext() {
         return index < genes.length;
      }

      @Override
      public boolean nextBoolean() {
         if (hasNext()) {
            return genes[index++]; //post-increment
         } else
            throw new NoSuchElementException();
      }
   }

   @FunctionalInterface
   private interface BooleanConsumer {

      void accept(boolean value);

      default BooleanConsumer andThen(BooleanConsumer after) {
         return (boolean t) -> {
            accept(t);
            after.accept(t);
         };
      }
   }
}
