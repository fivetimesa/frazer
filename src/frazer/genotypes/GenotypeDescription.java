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
   public final int geneCount;
   
   //MUTATION CONSTANTS
   private GeneMutationType geneMutationType;
   private GenotypeMutationType genotypeMutationType;
   private ValueType mutationValueType;
   int mutationCount;
   float mutationChance;
   float mutationStrength;
   float[] individualMutationStrength;
   
   //GENE LIMIT
   private Limit limit;
   float max, min;
   float[] maxs, mins;


   public GenotypeDescription(int geneCount, GenotypeType geneType) {
      this.genotypeType = geneType;
      this.geneCount = geneCount;

      this.genotypeMutationType = GenotypeMutationType.ONE;
      this.mutationValueType = mutationValueType.PERCENTAGE;
      this.mutationChance = 0.05f;
      
      if (geneType == geneType.BIT) {
         geneMutationType = GeneMutationType.BIT;
      }
      else {
         geneMutationType = GeneMutationType.CONSTANT;
         mutationStrength = 0.01f;
      }

      limit = Limit.NOLIMIT;
   }

   public GenotypeDescription(int geneCount, GenotypeType geneType, float min, float max) {
      this(geneCount, geneType);
      this.min = min;
      this.max = max;
      limit = Limit.FORALL;
   }

   public GenotypeDescription(int geneCount, GenotypeType geneType, int min, int max) {
      this(geneCount, geneType, (float) min, (float) max);
   }
   
   public GenotypeDescription(int geneCount, GenotypeType geneType, float[] minGeneLimits, float[] maxGeneLimits) {
      this(geneCount, geneType);
      this.mins = minGeneLimits;
      this.maxs = maxGeneLimits;
      limit = Limit.INDIVIDUAL;
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

   public Limit getLimit() {
      return limit;
   }

   public GeneMutationType getGeneMutationType() {
      return geneMutationType;
   }

   public GenotypeMutationType getGenotypeMutationType() {
      return genotypeMutationType;
   }
   
   public ValueType getMutationValueType() {
      return mutationValueType;
   }

   public float getMutationChance() {
      return mutationChance;
   }

   public int getMutationCount() {
      return mutationCount;
   }
   
   public void setMutationScale(float mutationStrength) {
      this.mutationStrength = mutationStrength;
   }

   public void setGeneMutationType(GeneMutationType geneMutationType) {
      this.geneMutationType = geneMutationType;
   }

   public void setMutationValueType(ValueType mutationValueType) {
      this.mutationValueType = mutationValueType;
   }

   public void setMutationChance(float mutationChance) {
      this.mutationChance = mutationChance;
   }

   public void setMutationCount(int mutationCount) {
      this.mutationCount = mutationCount;
   }

   public void setMutationStrength(float mutationStrength) {
      this.mutationStrength = mutationStrength;
   }
   
   public void setMutationStrength(float[] individualMutationStrength) {
      geneMutationType = GeneMutationType.INDIVIDUALRANGE;
      this.individualMutationStrength = individualMutationStrength;
   }

   public void setGenotypeMutationType(GenotypeMutationType genotypeMutationType) {
      this.genotypeMutationType = genotypeMutationType;
   }
   
   public float getMutationStrength() {
      return mutationStrength;
   }

   public float getMutationStrength(int i) {
      return mutationStrength;
   }

   private void rangeCheck(int i) {
      if (i < 0 || i > geneCount - 1)
         throw new IndexOutOfBoundsException(
                 "GenotypeDescription index: " + i + ", Size: " + geneCount);
   }
}
