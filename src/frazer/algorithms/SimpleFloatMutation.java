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

import frazer.genotypes.FloatGenotype;
import frazer.genotypes.Genotype;
import frazer.interfaces.Mutation;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class SimpleFloatMutation implements Mutation {
    private float mutationChance;
    private float mutationScale;
    
    public SimpleFloatMutation() {    
        mutationChance = 0.01f;
        mutationScale = 0.01f;
    }
    
    public SimpleFloatMutation(float mutationChance, float mutationScale) {    
        this.mutationChance = mutationChance;
        this.mutationScale = mutationScale;
    }
    
    @Override
    public Genotype mutate(Genotype genes) {
        if(!genes.getClass().equals(FloatGenotype.class)) {
            System.err.print("Genotype type mismatch. Mutation expected: FloatGenotype");
            return genes;
        }
        FloatGenotype floatGenes = (FloatGenotype) genes;
     
        Random random = new Random();
        for(int i = 0; i < genes.getGeneCount(); i++) {
            if(random.nextFloat() > getMutationChance()) continue;
            float value = floatGenes.getGene(i);
            float change = random.nextFloat() * getMutationScale() * 2 - getMutationScale();
            floatGenes.setGene(i, value + change);
            
        }
        return floatGenes;
    }

    /**
     * @return the mutationChance
     */
    public float getMutationChance() {
        return mutationChance;
    }

    /**
     * @param mutationChance the mutationChance to set
     */
    public void setMutationChance(float mutationChance) {
        this.mutationChance = mutationChance;
    }

    /**
     * @return the mutationScale
     */
    public float getMutationScale() {
        return mutationScale;
    }

    /**
     * @param mutationScale the mutationScale to set
     */
    public void setMutationScale(float mutationScale) {
        this.mutationScale = mutationScale;
    }
    
}
//</editor-fold>
