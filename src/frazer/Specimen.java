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

import frazer.genotypes.BitGenotype;
import frazer.genotypes.Genotype;
import frazer.genotypes.FloatGenotype;
import frazer.constants.GenotypeType;
import frazer.interfaces.Fitness;
import frazer.interfaces.Mutation;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class Specimen implements Comparable<Specimen> {

    private Genotype genes;

    final static byte BITGENOTYPE = 1;
    final static byte FLOATGENOTYPE = 0;
    final static byte PARETOSCORE = 1;

    private float fitnessScore;
    private float[] fitnessParetoScores = null;

// <editor-fold defaultstate="collapsed" desc="Constructors">
    
    public Specimen(Genotype genes) {
        this.genes = genes;
        fitnessScore = 0;
    }
    
    public Specimen(Genotype genes, float fitnessScore) {
        this.genes = genes;
        this.fitnessScore = fitnessScore;
    }
    /**
     *
     * @param geneCount
     * @param geneType
     */
    public Specimen(int geneCount, GenotypeType geneType) {
        if (geneType == GenotypeType.BIT) {
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
    public Specimen(int geneCount, GenotypeType geneType, int paretoScoreSize) throws Exception {
        this(geneCount, geneType);
        if (paretoScoreSize > 1) {
            fitnessParetoScores = new float[paretoScoreSize];
        } else {
            throw new Exception("Pareto score size must be greater than 1.");
        }
    }
    
    public Specimen copy() {
        Specimen copy = new Specimen(genes.copy(), fitnessScore);
        return copy;
    } 
    
    public float evaluateFitness(Fitness fitness) {
        fitnessScore = fitness.calculateFitness(genes);
        return fitnessScore;
    }
    
    public void mutate(Mutation mutation) {
        genes = mutation.mutate(genes);
    }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Comparable interface methods">
    /**
     * Compares specimens by thier <code>fitnessScore</code>.
     *
     * @param otherSpecimen
     * @return
     */
    @Override
    public int compareTo(Specimen otherSpecimen) {
        if (otherSpecimen == null) {
            throw new NullPointerException("Can't compere Specimen to null.");
        }
        
        if (this.equals(otherSpecimen)) {
            return 0;
        }
        
        return Float.compare(this.getFitnessScore(), otherSpecimen.getFitnessScore());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (!(obj instanceof Specimen)) {
            return false;
        }
        
        final Specimen s = (Specimen) obj;
        return this.getFitnessScore() == s.getFitnessScore();
    }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Getters & Setters">
    /**
     * @return the genes
     */
    public Genotype getGenes() {
        return genes;
    }

    /**
     * @param genes the genes to set
     */
    public void setGenes(Genotype genes) {
        this.genes = genes;
    }

    /**
     * @return the fitnessScore
     */
    public float getFitnessScore() {
        return fitnessScore;
    }

    /**
     * @param fitnessScore the fitnessScore to set
     */
    public void setFitnessScore(float fitnessScore) {
        this.fitnessScore = fitnessScore;
    }

    /**
     * @return the fitnessParetoScores
     */
    public float[] getFitnessParetoScores() {
        return fitnessParetoScores;
    }

    /**
     * @param fitnessParetoScores the fitnessParetoScores to set
     */
    public void setFitnessParetoScores(float[] fitnessParetoScores) {
        this.fitnessParetoScores = fitnessParetoScores;
    }

// </editor-fold>

}
