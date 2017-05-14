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
import frazer.constants.*;
import frazer.genotypes.*;
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
    private float maxScore;
    private float minScore;
    private double scoreSum;
    private Specimen minSpecimen;
    private Specimen maxSpecimen;
    
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
    public Population(int populationCount, int geneCount, GenotypeType geneType) {
        this.count = populationCount;
        specimens = new Specimen[populationCount];
        for (int i = 0; i < populationCount; i++) {
            specimens[i] = new Specimen(geneCount, geneType);
        }
        System.out.print("New population created with " + count + " random specimen. \n");
    }

    /**
     *
     * @param specimens
     */
    public Population(Specimen[] specimens) throws Exception{
        this.specimens = specimens;
        this.count = specimens.length;
        
        if(count == 0) {
            System.out.print("Specimens array cannot be empty. \n");
            throw new IllegalArgumentException("Specimens array cannot be empty. \n");
        }
    }

// </editor-fold>
    
    public void evaluate(Fitness fitness) {
        scoreSum = 0;
        maxScore = Float.MIN_VALUE;
        minScore = Float.MAX_VALUE;
        if(!evaluated) {
            for (int i = 0; i < count; i++) {
                System.out.print("Evaluating specimen #" + i + "\n");
                float score = specimens[i].evaluateFitness(fitness);
                if(score < minScore) {
                    minScore = score;
                    minSpecimen = specimens[i];
                }
                if(score > maxScore) {
                    maxScore = score;
                    maxSpecimen = specimens[i];
                }
                scoreSum += score;
            }
            evaluated = true;
        }
    }
    
    public Population nextGeneration(Preselection preselection, Fitness fitness, Mating mating, Breeding breeding, Mutation mutation) throws Exception {
        ArrayList<Specimen> newSpecimens = new ArrayList<>();
        
        System.out.print("Evaluating… \n");
        evaluate(fitness);
        System.out.print("Specimen evaluation done. \n");
        
        if(mating.needsSorting() || preselection.needsSorting()) {
            Arrays.sort(specimens);
            System.out.print("Specimen array sorted \n");
        }
        
        Specimen[] elite = preselection.selectElite(specimens);
        newSpecimens.addAll(Arrays.asList(elite));
        
        specimens = preselection.discardWorst(specimens);
        System.out.print("Preselection done. \n");
        
        System.out.print("Mating & breeding… \n");
        while(newSpecimens.size() < count) {
            Specimen[] parents = mating.selectParents(this);
            System.out.print("Parents ready. \n");
            Specimen[] children = breeding.breed(parents);
            System.out.print("Children ready. \n");
            newSpecimens.addAll(Arrays.asList(children));
            System.out.print("Children added to new generation. \n");
        }
        
        System.out.print("Created " + newSpecimens.size() + " new specimens. \n");
        System.out.print("Mutating… \n");
        newSpecimens.forEach((newSpecimen) -> {
            newSpecimen.mutate(mutation);
        });
        
        System.out.print("New generation ready. \n");
        return new Population((Specimen[]) newSpecimens.toArray());
    }
    
    private void evaluateFitness() {
        
    }

//<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public Specimen[] getSpecimens() {
        return specimens;
    }
    
    public Specimen getMinSpecimen() {
        return minSpecimen;
    }
    
    public Specimen getMaxSpecimen() {
        return maxSpecimen;
    }
    
    public Specimen getBestSpecimen(Goal goal) {
        if(goal == Goal.MAXIMISE)
            return maxSpecimen;
        else return minSpecimen;
    }

    public int getCount() {
        return count;
    }
    
    public float getMaxScore() {
        return maxScore;
    }
    
    public float getMinScore() {
        return minScore;
    }
    
    public double getScoreSum() {
        return scoreSum;
    }
    
//</editor-fold>

    
}
