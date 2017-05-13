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
package frazer;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class GenotypeDescription {
    final static byte BITGENOTYPE = 1;
    final static byte FLOATGENOTYPE = 0;
    final static byte SFLOATGENOTYPE = 2;

    byte geneType;
    int geneCount;
    float max, min;
    float[] maxs, mins;
    
    GenotypeDescription(int geneCount, byte geneType) 
    {
        this.geneCount = geneCount;
        this.geneType = geneType;
    }
    
    GenotypeDescription(int geneCount, byte geneType, int min, int max)
    {
        this(geneCount, geneType);
        this.min = min;
        this.max = max;
    }
    
    GenotypeDescription(int geneCount, byte geneType, float[] minGeneLimits, float[] maxGeneLimits)
    {
        this(geneCount, geneType);
        this.mins = minGeneLimits;
        this.maxs = maxGeneLimits;
    }

    public byte getGeneType() {
        return geneType;
    }

    public int getGeneCount() {
        return geneCount;
    }
    
    public float getMax()
    {
        return max;
    }
            
    public float getMax(int i)
    {
        return maxs[i];
    }
    public float getMin()
    {
        return min;
    }
            
    public float getMin(int i)
    {
        return mins[i];
    }
    
    private void rangeCheck(int i) {
        if (i < 0 || i > geneCount - 1)
            throw new IndexOutOfBoundsException(
                    "GenotypeDescription index: " + i + ", Size: " + geneCount);
    }
}
