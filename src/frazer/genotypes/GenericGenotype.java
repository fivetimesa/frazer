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

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 *
 *
 * @author Teodor Michalski, Paweł Sikorski
 * @param <T> object contains gene information
 */
public class GenericGenotype<T> extends Genotype<T> {

   T[] genes;

   /**
    * Used only for copy method.
    */
   private GenericGenotype() {
      //private constructor
   }

   public GenericGenotype(Class<T> c, int count) {
      @SuppressWarnings("unchecked")
      final T[] genericGenes = (T[]) Array.newInstance(c, count);
      this.genes = genericGenes;
   }

   @Override
   protected Object getArrayInstance() {
      return genes;
   }

   /**
    * Copies genotype.
    *
    * @return copy of GenericGenotype<T> instance
    */
   @Override
   public Genotype copy() {
      Class c;
      c = genes.getClass().getComponentType();
      GenericGenotype<T> copy = new GenericGenotype<>(c, genes.length);

      for (int i = 0; i < genes.length; i++) {
         copy.setGene(i, getGene(i));
      }
      return copy;
   }

   /**
    * Overrides method to set random initial values.
    */
   @Override
   public void randomInit() {
      //empty
   }

   /**
    * Returns genotype length. Should be override for best performance.
    *
    * @return array length
    */
   @Override
   public int getGeneCount() {
      return genes.length;
   }

   /**
    * Returns ith gene from genotype.
    *
    * @param i gene index
    * @return
    */
   @Override
   public T getGene(int i) {
      rangeCheck(i);
      return genes[i];
   }

   /**
    * Sets gene value with use of <T>.
    *
    * @param i gene index
    * @param value new gene value
    */
   @Override
   public void setGene(int i, T value) {
      rangeCheck(i);
      genes[i] = value;
   }

   @Override
   public Iterator<T> iterator() {
      return new GenericGenotypeIterator<>();
   }

    @Override
    public void applyLimits(GenotypeDescription gD) {
    }

   private class GenericGenotypeIterator<T> implements Iterator<T> {

      int index;

      public GenericGenotypeIterator() {
         this.index = 0;
      }

      @Override
      public boolean hasNext() {
         return (index < genes.length);
      }

      @Override
      public T next() {
         return (T) genes[index++];
      }
   }
}
