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
import frazer.interfaces.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class Population {

    private final int count;
    private boolean evaluated = false;
    /**
     * Population of specimens.
     */
    private Specimen[] specimens;

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     *
     * @param populationCount
     * @param geneCount
     * @param geneType
     */
    public Population(int populationCount, int geneCount, byte geneType) {
        this.count = populationCount;
        specimens = new Specimen[populationCount];
        for (int i = 0; i < populationCount; i++) {
            specimens[i] = new Specimen(geneCount, geneType);
        }
    }

    /**
     *
     * @param specimens
     */
    public Population(Specimen[] specimens) throws Exception{
        this.specimens = specimens;
        this.count = specimens.length;
        
        if(count == 0) throw new IllegalArgumentException("Specimens array cannot be empty.");
    }

// </editor-fold>
    
    public void evaluate(Fitness fitness) {
        if(!evaluated) {
            for (int i = 0; i < count; i++) {
                specimens[i].evaluateFitness(fitness);
            }
            evaluated = true;
        }
    }
    
    public Population nextGeneration(Preselection preselection, Fitness fitness, Mating mating, Breeding breeding, Mutation mutation) throws Exception {
        ArrayList<Specimen> newSpecimens = new ArrayList<>();
        
        if(!evaluated) {
            for (int i = 0; i < count; i++) {
                specimens[i].evaluateFitness(fitness);
            }
            evaluated = true;
        }
        
        if(mating.needsSorting() || preselection.needsSorting()) {
            Arrays.sort(specimens);
        }
        
        Specimen[] elite = preselection.selectElite(specimens);
        newSpecimens.addAll(Arrays.asList(elite));
        
        specimens = preselection.discardWorst(specimens);
        
        while(newSpecimens.size() < count) {
            Specimen[] parents = mating.selectParents(this);
            Specimen[] children = breeding.breed(parents);
            newSpecimens.addAll(Arrays.asList(children));
        }
        
        newSpecimens.forEach((newSpecimen) -> {
            newSpecimen.mutate(mutation);
        });
        
        return new Population((Specimen[]) newSpecimens.toArray());
    }

    public Specimen[] getSpecimens() {
        return specimens;
    }

}
