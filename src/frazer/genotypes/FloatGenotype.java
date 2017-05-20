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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class FloatGenotype extends Genotype<Float> {

   protected float[] genes;

   /**
    * Used only for copy method.
    */
   protected FloatGenotype() {
      //protected constructor
   }

   /**
    * Creates genotype with decimal values. Genes are stored in array. One gene
    * is represented by primitive type float. Genes has random value.
    *
    * @param count genotype's size
    */
   public FloatGenotype(int count) {
      genes = new float[count];
      this.randomInit();
   }

   /**
    * Creates genotype with decimal values. Genes are stored in array. One gene
    * is represented by primitive type float.
    *
    * @param count genotype's size
    * @param randomInitialize if true, fires randomInit
    */
   public FloatGenotype(int count, boolean randomInitialize) {
      genes = new float[count];
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
    * @return copy of FloatGenotype instance
    */
   @Override
   public Genotype copy() {
      FloatGenotype copy = new FloatGenotype();
      copy.setGenes(Arrays.copyOf(genes, genes.length));
      return copy;
   }

   /**
    * Ranodomizes genes' values in range 0 to 1.
    */
   @Override
   public void randomInit() {
      Random generator = new Random();
      for (int i = 0; i < genes.length; i++) {
         genes[i] = generator.nextFloat();
      }
   }

   /**
    * Ranodomizes genes' values in range <code>min</code> to <code>max</code>.
    *
    * @param min
    * @param max
    */
   @Override
   public void randomInit(float min, float max) {
      if (max < min)
         throw new ArithmeticException("min > max in FloatGenotype.randomInit(float min, float max)");
      Random generator = new Random();
      for (int i = 0; i < genes.length; i++) {
         genes[i] = min + generator.nextFloat() * (max - min);
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
    * Returns ith gene from genotype. Uses Float object.
    *
    * @param i gene index
    * @return warped float
    */
   @Override
   public Float getGene(int i) {
      rangeCheck(i);
      return genes[i];
   }

   /**
    * 
    * 
    * @param i gene index
    * @param value
    */
   @Override
   public void setGene(int i, Float value) {
      rangeCheck(i);
      genes[i] = value;
   }

   /**
    * Returns all genes in float array.
    *
    * @return
    */
   public float[] getGenes() {
      return Arrays.copyOf(genes, genes.length);
   }

   /**
    * Uses in copy method.
    * 
    * @param thatGenes
    */
   protected void setGenes(float[] thatGenes) {
      this.genes = thatGenes;
   }

   /**
    * Sets new value for ith gene.
    *
    * @param i gene index
    * @param value new value to set
    */
   @Override
   public void setFloat(int i, float value) {
      genes[i] = value;
   }

   /**
    * Gets ith gene value.
    *
    * @param i gene index
    * @return
    */
   @Override
   public float getFloat(int i) {
      return genes[i];
   }

   /**
    *
    * @return
    */
   @Override
   public String toString() {
      return Arrays.toString(genes);
   }

   @Override
   public FloatGenotypeIterator iterator() {
      return new FloatGenotypeIterator();
   }

   public static interface PrimitiveIteratorOfFloat extends PrimitiveIterator<Float, FloatConsumer> {

      public float nextFloat();

      @Override
      public default Float next() {
         return nextFloat();
      }

      @Override
      public default void forEachRemaining(FloatConsumer action) {
         Objects.requireNonNull(action);
         while (hasNext()) {
            action.accept(nextFloat());
         }
      }
   }

   public class FloatGenotypeIterator implements PrimitiveIteratorOfFloat {

      int index;

      private FloatGenotypeIterator() {
         this.index = 0;
      }

      @Override
      public boolean hasNext() {
         return index < genes.length;
      }

      @Override
      public float nextFloat() {
         if (hasNext()) {
            return genes[index++]; //post-increment
         } else
            throw new NoSuchElementException();
      }
   }

   @FunctionalInterface
   private interface FloatConsumer {

      void accept(float value);

      default FloatConsumer andThen(FloatConsumer after) {
         return (float t) -> {
            accept(t);
            after.accept(t);
         };
      }
   }
}
