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
import frazer.Frazer.Algorithms;
import frazer.constants.*;
import frazer.interfaces.*;
import java.util.Arrays;
import frazer.genotypes.GenotypeDescription;

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
    private GenotypeDescription gD;
    /**
     * Population of specimens.
     */
    private Specimen[] specimens;

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     *
     * @param populationCount
     * @param gD
     */
    public Population(int populationCount, GenotypeDescription gD) {
        this.gD = gD;
        this.count = populationCount;
        specimens = new Specimen[populationCount];
        for (int i = 0; i < populationCount; i++) {
            specimens[i] = new Specimen(gD.geneCount, gD.getGenotypeType());
        }
        //System.out.print("New population created with " + count + " random specimen. \n");
    }

    /**
     *
     * @param specimens
     */
    public Population(Specimen[] specimens) {//throws Exception{
        //System.out.print("Creating new population.\n");
        this.specimens = specimens;
        this.count = specimens.length;
        //System.out.print("Array of " + count + " specimen received. \n");
        
//        if(count == 0) {
//            //System.out.print("Specimens array cannot be empty. \n");
//            throw new IllegalArgumentException("Specimens array cannot be empty. \n");
//        }
        //System.out.print("New population created. \n");
    }

// </editor-fold>

    /**
     *
     * @param fitness
     */
    
    public void evaluate(Fitness fitness) {
        scoreSum = 0;
        maxScore = Float.MIN_VALUE;
        minScore = Float.MAX_VALUE;
        if(!evaluated) {
            for (int i = 0; i < count; i++) {
                //System.out.print("Evaluating specimen #" + i + "\n");
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
    
     private void evaluate(Specimen[] specimens, Fitness fitness) {
        for (int i = 0; i < count; i++) {
            scoreSum -= specimens[i].getFitnessScore();
            //System.out.print("Evaluating specimen #" + i + "\n");
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
    
    /**
     *
     * @param algorithms
     * @return
     * @throws Exception
     */
    public Population nextGeneration(Algorithms algorithms) throws Exception {
        Specimen[] newSpecimens = new Specimen[count];
        
        //System.out.print("Evaluating… \n");
        evaluate(algorithms.fitness);
        //System.out.print("Specimen evaluation done. \n");
        
        if(algorithms.needSorting()) {
            Arrays.sort(specimens);
            //System.out.print("Specimen array sorted \n");
        }
        
        Specimen[] elite = algorithms.preselection.selectElite(this);
        int newSpecimensCount = 0;
        for(int i = 0; i < elite.length; i++) {
            newSpecimens[i] = elite[i];
            newSpecimensCount++;
        }
        
        specimens = algorithms.preselection.discardWorst(this);
        //System.out.print("Preselection done. \n");
        
        //System.out.print("Mating & breeding… \n");
        algorithms.mating.update(this);
        //System.out.print("Mating initialized \n");
        
        for(int i = newSpecimensCount; i < newSpecimens.length; i++) {
            Specimen[] parents = algorithms.mating.selectParents(this);
            //System.out.print("Parents ready. \n");
            Specimen[] children = algorithms.breeding.breed(parents);
            //System.out.print("Children ready. \n");
            for(int j = 0; j < children.length; j++) {
                if(newSpecimensCount >= count) break;
                newSpecimens[newSpecimensCount] = children[j];
                newSpecimensCount++;
            }
            //System.out.print("Children added to new generation. \n");
        }
        
        Population nextPopulation = new Population(newSpecimens);
        //System.out.print("Created " + newSpecimens.length + " new specimens. \n");
        //System.out.print("Mutating… \n");
        nextPopulation.mutate(algorithms.mutantSelection, algorithms.mutation, algorithms.fitness);
        //System.out.print("New generation ready. \n");
        return nextPopulation;
    }
    
    private void mutate(MutantSelection mutantSelection, Mutation mutation, Fitness fitness) {
        
        //System.out.print("Mutating new population with " + mutantSelection.getClass().getName() 
        //                + " and " + mutation.getClass().getName() +  "\n");
        
        if(mutantSelection.needsFitness())
            evaluate(fitness);
        
        //System.out.print("Selecting mutants… ");
        Specimen[] mutants = mutantSelection.selectMutants(this);
        //System.out.print(mutants.length + " specimens selected. \n");
        
        //System.out.print("Mutating… ");
        for(Specimen mutant : mutants)
            mutant.mutate(mutation);
        //System.out.print("done. \n");
        
        //System.out.print("Evaluating… \n");
        if(mutantSelection.needsFitness())
            evaluate(mutants, fitness);
        else 
            evaluate(fitness);
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

   public GenotypeDescription getGenotypeDescription() {
      return gD;
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
