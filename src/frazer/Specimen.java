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
public class Specimen implements Comparable<Specimen> {

    Genotype genes;

    final static byte BITGENOTYPE = 1;
    final static byte FLOATGENOTYPE = 0;
    final static byte PARETOSCORE = 1;

    float fitnessScore;
    float[] fitnessParetoScores = null;

    /**
     *
     * @param geneCount
     * @param geneType
     */
    public Specimen(int geneCount, byte geneType) {
        if (geneType == BITGENOTYPE) {
            genes = new BitGenotype(geneCount);
        } else {
            genes = new FloatGenotype(geneCount);
        }
        fitnessScore = 0;
    }

    /**
     *
     * @param geneCount
     * @param geneType
     * @param paretoScoreSize
     * @throws Exception
     */
    public Specimen(int geneCount, byte geneType, int paretoScoreSize) throws Exception
    {
        this(geneCount, geneType);
        if(paretoScoreSize > 1)
        {
            fitnessParetoScores = new float[paretoScoreSize];
        }
        else
            throw new Exception("Pareto score size must be greater than 1.");
    }
    
    /**
     * Compares specimens by thier <code>fitnessScore</code>.
     *
     * @param otherSpecimen
     * @return
     */
    @Override
    public int compareTo(Specimen otherSpecimen) {
        if (otherSpecimen == null)
            throw new NullPointerException("Can't compere Specimen to null.");

        if (this.equals(otherSpecimen))
            return 0;

        return Float.compare(this.fitnessScore, otherSpecimen.fitnessScore);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Specimen))
            return false;

        final Specimen s = (Specimen) obj;
        return this.fitnessScore == s.fitnessScore;
    }

}
