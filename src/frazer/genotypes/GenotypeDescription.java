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

import frazer.constants.*;

/**
 *
 * @author Teodor Michalski, Paweł Sikorski
 */
public class GenotypeDescription {

   private GenotypeType genotypeType;
   public final int geneCount;
   
   //GENE LIMIT
   private GeneLimit geneLimit;
   private LimitType limit;
   private float max, min;
   private float[] maxs, mins;


   public GenotypeDescription(int geneCount, GenotypeType geneType) {
      this.genotypeType = geneType;
      this.geneCount = geneCount;
      
      limit = LimitType.NOLIMIT;
      setGeneLimit();
   }

   public GenotypeDescription(int geneCount, GenotypeType geneType, float min, float max) {
      this(geneCount, geneType);
      setGeneLimits(min, max);
      setGeneLimit();
   }
   
   public GenotypeDescription(int geneCount, GenotypeType geneType, float[] minGeneLimits, float[] maxGeneLimits) {
      this(geneCount, geneType);
      setGeneLimits(minGeneLimits, maxGeneLimits);
      setGeneLimit();
   }
   
   public final void setGeneLimits(float min, float max) {
      this.min = min;
      this.max = max;
      limit = LimitType.FORALL;
      setGeneLimit();
   }
   
   public final void setGeneLimits(float[] minGeneLimits, float[] maxGeneLimits) {
      if(minGeneLimits.length <= 0 || maxGeneLimits.length <= 0) return;
      if(this.mins == null)
          this.mins = new float[geneCount];
      if(this.maxs == null)
          this.maxs = new float[geneCount];
      for(int i = 0; i < geneCount; i++) {
          if(i < minGeneLimits.length)
            this.mins[i] = minGeneLimits[i];
          else
            this.mins[i] = minGeneLimits[minGeneLimits.length - 1];
          if(i < maxGeneLimits.length)
            this.maxs[i] = maxGeneLimits[i];
          else
            this.maxs[i] = maxGeneLimits[maxGeneLimits.length - 1];
      }
      limit = LimitType.INDIVIDUAL;
      setGeneLimit();
   }

   public GenotypeType getGenotypeType() {
      return genotypeType;
   }

   public int getGeneCount() {
      return geneCount;
   }

   public float getMax() {
      return max;
   }

   public float getMax(int i) {
      return maxs[i];
   }

   public float getMin() {
      return min;
   }

   public float getMin(int i) {
      return mins[i];
   }

   public LimitType getLimit() {
      return limit;
   }

   private void rangeCheck(int i) {
      if (i < 0 || i > geneCount - 1)
         throw new IndexOutOfBoundsException(
                 "GenotypeDescription index: " + i + ", Size: " + geneCount);
   }
   
   public float limitGene(int i, float value) {
       return geneLimit.limit(i, value);
   }

   private void setGeneLimit() {
      switch (limit) {
         case NOLIMIT:
            geneLimit = new GeneLimit() {
               @Override
               protected float limit(int i, float value) {
                  return value;
               }
            };
            break;
         case FORALL:
            geneLimit = new GeneLimit() {
               @Override
               protected float limit(int i, float value) {
                  if (value < min)
                     return min;
                  if (value > max)
                     return max;
                  return value;
               }
            };
            break;
         case INDIVIDUAL:
            geneLimit = new GeneLimit() {
               @Override
               protected float limit(int i, float value) {
                  if (value < mins[i])
                     return mins[i];
                  if (value > maxs[i])
                     return maxs[i];
                  return value;
               }
            };
            break;
         case NORMALIZE:
            geneLimit = new GeneLimit() {
               @Override
               protected float limit(int i, float value) {
                  if (value < 0)
                     return 0;
                  if (value > 1)
                     return 1;
                  return value;
               }
            };
      }
   }
   
   abstract private class GeneLimit {

      abstract protected float limit(int i, float value);
   }
}
