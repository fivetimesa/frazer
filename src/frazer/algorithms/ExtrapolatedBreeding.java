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
package frazer.algorithms;

import frazer.Specimen;
import frazer.constants.GenotypeType;
import frazer.genotypes.Genotype;
import frazer.genotypes.GenotypeDescription;
import frazer.interfaces.Breeding;
import java.util.Random;

/**
 *
 */
public class ExtrapolatedBreeding implements Breeding {
    private ExtrapolationType type;
    private float range;

    public ExtrapolatedBreeding() {
        this.type = ExtrapolationType.COMPLEMENTARY;
        this.range = 1.0f;
    }
    
    public ExtrapolatedBreeding(float range) {
        this.type = ExtrapolationType.COMPLEMENTARY;
        this.range = range;
    }
    
    public ExtrapolatedBreeding(float range, ExtrapolationType type) {
        this.type = type;
        this.range = range;
    }

    @Override
    public void initialize(GenotypeDescription gD) {
        if(gD.getGenotypeType() == GenotypeType.BIT)
                System.err.print("Extrapolated breeding does not work with bit-type Genotype.");
    }
    

    @Override
    public Specimen[] breed(Specimen[] parent) {
        Specimen[] children;
        int number = parent.length;
        if (number == 0) 
            throw new IllegalArgumentException("No parents received");
        children = new Specimen[number];
        if (number == 1) {
            children[0] = parent[0].copy();
            return children;
        }
        
        Random random = new Random();
        int geneCount = parent[0].getGenes().getGeneCount();
        
        Genotype[] childrenGenes = new Genotype[number];
        for(int i = 0; i < number; i++) {
            childrenGenes[i] = parent[i].getGenes().copy();
        }
        
        float[][][] influence = new float[number][number - 1][geneCount];
        
        switch(type) {
            case INDIVIDUAL:
                //each gene has a separate infulence value from each parent
                for(int i = 0; i < number; i++) 
                    for(int j = 0; j < number; j++)
                        for(int g = 0; g < geneCount; g++)
                            influence[i][j][g] = random.nextFloat() * range;
            break;
            case UNIFORM:
                //all genes have the same influence from given parent
                for(int i = 0; i < number; i++) 
                    for(int j = 0; j < number - 1; j++)
                        for(int g = 0; g < geneCount; g++)
                            if(g == 0)
                                influence[i][j][g] = random.nextFloat() * range;
                            else
                                influence[i][j][g] = influence[i][j][0];
            break;
            case COMPLEMENTARY:
                //children have genes in complementary positions
                for(int i = 0; i < number; i++) 
                    for(int j = 0; j < number - 1; j++)
                        for(int g = 0; g < geneCount; g++)
                            if(g == 0 && i == 0)
                                influence[i][j][g] = random.nextFloat() * range;
                            else
                                influence[i][j][g] = influence[0][j][0];
            break;
        }
        
        for(int i = 0; i < children.length; i++) {
            for(int g = 0; g < geneCount; g++) {

                float value = childrenGenes[i].getFloat(g);
                for(int j = 1; j < parent.length; j++) {
                    value -= influence[i][j - 1][g]*(value - parent[(i + j) % number].getGenes().getFloat(g));
                }
                childrenGenes[i].setFloat(g, value);
            }
            children[i] = new Specimen(childrenGenes[i]);
        }
        return children;
    }
    

    /**
     * @return the type
     */
    public ExtrapolationType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ExtrapolationType type) {
        this.type = type;
    }

    /**
     * @return the range
     */
    public float getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(float range) {
        this.range = Math.abs(range);
        if(range > 2f)
            System.out.println("The extrapolation range should usually be close to 1.0");
    }
    
    public enum ExtrapolationType {
        INDIVIDUAL, UNIFORM, COMPLEMENTARY
    }
}
