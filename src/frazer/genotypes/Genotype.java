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

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public abstract class Genotype<T> implements Iterable<T> {

   /**
    * Returns object reference to array with genes.
    *
    * @return array as object
    */
   protected abstract Object getArrayInstance();

   abstract public Genotype copy();

   abstract public void randomInit();

   public void randomInit(float min, float max) {
      throw new UnsupportedOperationException("Method "
              + "randomInit(float min, float max) "
              + "is not supported by this type of genotype.");
   }

   /**
    * Returns genotype length. Should be override for best performance.
    *
    * @return array length
    */
   public int getGeneCount() {
      return Array.getLength(getArrayInstance());
   }

   abstract public T getGene(int i);

   abstract public void setGene(int i, T value);

   protected void rangeCheck(int i) {
      if (i < 0 || i > getGeneCount() - 1)
         throw new IndexOutOfBoundsException(
                 "Genes index: " + i + ", Size: " + getGeneCount());
   }

   public int getInteger(int i) {
      throw new UnsupportedOperationException("Method getInteger() is not supported by this type of genotype.");
   }

   public boolean getBoolean(int i) {
      throw new UnsupportedOperationException("Method getBoolean() is not supported by this type of genotype.");
   }

   public float getFloat(int i) {
      throw new UnsupportedOperationException("Method getFloat() is not supported by this type of genotype.");
   }

   public float getStep(int i) {
      throw new UnsupportedOperationException("Method getStep() is not supported by this type of genotype.");
   }

   public void setInteger(int i, int value) {
      throw new UnsupportedOperationException("Method getInteger() is not supported by this type of genotype.");
   }

   public void setBoolean(int i, boolean value) {
      throw new UnsupportedOperationException("Method getBoolean() is not supported by this type of genotype.");
   }

   public void setFloat(int i, float value) {
      throw new UnsupportedOperationException("Method getFloat() is not supported by this type of genotype.");
   }

   public void setStep(int i, float value) {
      throw new UnsupportedOperationException("Method getStep() is not supported by this type of genotype.");
   }

   /*   @Override
   public Iterator<T> iterator() {
      return new GeneIterator<>();
   }

   private class GeneIterator<T> implements Iterator<T> {
      int cursor;
      int length = getGeneCount();
      Object objectArray = getArrayInstance();
      Class arrayType = objectArray.getClass().getComponentType();
      
      int[] intArray;
      float[] floatArray;
      boolean[] bitArray;
      
      public GeneIterator() {
         cursor = 0;
      }

      @Override
      public boolean hasNext() {
         if (cursor < length)
            return true;
         else
            return false;
      }

      @Override
      public T next() {
         cursor++;
         return (T) genes[cursor - 1];
      }
   }*/
}
