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
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class GenotypeDescription {

   private GenotypeType genotypeType;
   private MutationType mutationType;
   private MutationValue mutationValue;
   float mutationScale;
   float mutationScales[];

   int geneCount;
   float max, min;
   float[] maxs, mins;

   public GenotypeDescription(int geneCount, GenotypeType geneType) {
      this.genotypeType = geneType;
      this.geneCount = geneCount;
      this.mutationValue = MutationValue.PERCENTAGE;

      if (geneType == GenotypeType.BIT) {
         mutationType = MutationType.BIT;
      }
      if (geneType == GenotypeType.BIT) {
         mutationType = MutationType.CONSTANTVALUE;
         mutationScale = 0.01f;
      }
   }

   public GenotypeDescription(int geneCount, GenotypeType geneType, int min, int max) {
      this(geneCount, geneType);
      this.min = min;
      this.max = max;
   }

   public GenotypeDescription(int geneCount, GenotypeType geneType, float[] minGeneLimits, float[] maxGeneLimits) {
      this(geneCount, geneType);
      this.mins = minGeneLimits;
      this.maxs = maxGeneLimits;
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

   public MutationType getMutationType() {
      return mutationType;
   }

   public MutationValue getMutationValue() {
      return mutationValue;
   }

   public void setMutationScale(float mutationScale) {
      this.mutationScale = mutationScale;
   }

   public void setMutationType(MutationType mutationType) {
      this.mutationType = mutationType;
   }

   public void setMutationValue(MutationValue mutationValue) {
      this.mutationValue = mutationValue;
   }

   public void setMutationScales(float[] mutationScales) {
      mutationType = MutationType.INDIVIDUALRANGEVALUE;
      this.mutationScales = mutationScales;
   }

   public float getMutationScale() {
      return mutationScale;
   }

   public float getMutationScale(int i) {
      return mutationScale;
   }

   private void rangeCheck(int i) {
      if (i < 0 || i > geneCount - 1)
         throw new IndexOutOfBoundsException(
                 "GenotypeDescription index: " + i + ", Size: " + geneCount);
   }
}
