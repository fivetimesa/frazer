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
import frazer.algorithms.mutation.NoMutation;
import frazer.constants.*;
import frazer.algorithms.*;
import frazer.algorithms.mutantselection.*;
import frazer.algorithms.mutation.*;
import frazer.genotypes.*;
import frazer.interfaces.*;
import processing.core.*;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class Frazer {

    
    private PApplet parent;
    private History history;
    private Population currentPopulation;
    
    private GenotypeDescription gD;

    /** StopCondition options. */
    private StopCondition stopCondition;
    
    /** Generation counter. Keeps track of the number of evaluated generations. */
    private int generationCount;
    
    /** The goal of the optimisation process (either to minimise of to maximise). */
    private Goal goal;
    
    /** Algorithm for preselection. */
    private Preselection preselection;
    /** Algorithm for mating. */
    private Mating mating;
    /** Algorithm for breeding. */
    private Breeding breeding;
    /** Algorithm for fitness, to be specified by the user. */
    private Fitness fitness;
    /** Algorithm for mutant selection. */
    private MutantSelection mutantSelection;
    /** Algorithm for mutation. */
    private Mutation mutation;
    
    
    /**
     *
     * @param parent reference to Processing sketch. Usually use "this".
     */
    public Frazer(PApplet parent)
    {
        this.parent = parent;
        history = new History();
        setDefaults();
    }
    
    
    public Frazer(PApplet parent, int populationCount, int geneCount)
    {
        this.parent = parent;
        history = new History();
        
        if(findFitnessFunction()) {
            GenotypeType genotypeType = ((ReflectionFitness) fitness).getGeontypeType();
            this.gD = new GenotypeDescription(geneCount, genotypeType);
            currentPopulation = new Population(populationCount, gD);
            
            currentPopulation.evaluate(fitness);
            history.recordPopulation(currentPopulation);
        }
        setDefaults();
    }
    
    /**
     *
     * @param parent
     * @param populationCount
     * @param geneCount
     * @param genotypeType
     * @param fitness
     */
    public Frazer(PApplet parent, int populationCount, int geneCount, GenotypeType genotypeType, Fitness fitness)
    {
        this.parent = parent;
        history = new History();
        
        this.gD = new GenotypeDescription(geneCount, genotypeType);
        currentPopulation = new Population(populationCount, gD);
        this.fitness = fitness;
        
        currentPopulation.evaluate(fitness);
        history.recordPopulation(currentPopulation);
        
        setDefaults();
    }
    
    
    private void setDefaults() {
        if(goal == null) 
            goal = Goal.MINIMISE;
        if(preselection == null) 
            setPreselection(new NoPreselection());
        if(mating == null) 
            setMating(new TournamentMating(goal));
        if(breeding == null) 
            setBreeding(new CrossoverBreeding());
        if(getMutantSelection() == null)
            setMutantSelection(new ChanceMutantSelection(0.10f));
        if(mutation == null) {
            switch(gD.getGenotypeType()) {
                case BIT:
                    mutation = new BitMutation();
                    break;
                case INTEGER:
                    mutation = new ConstantValueMutation(1f);
                    break;
                case SFLOAT:
                case FLOAT:
                    mutation = new RangeValueMutation(0.5f);
                    break;
                default:
                    mutation = new NoMutation();
                    break;
            }
            
        }
        if(getStopCondition() == null)
            setStopCondition(new StopCondition());
        
        for(AlgorithmsInterface algorithm: new AlgorithmsInterface[]{preselection, fitness, mating, breeding, mutantSelection, mutation})
            algorithm.initialize(gD);
    }
    
    /**
     * Try to find a fitness method inside the Processing sketch. If 
     * a function with name "fitness", that returns a float, is found it will
     * be stored in a {@linkplain frazer.algorithms.ReflectionFitness} object.
     * It will be then invoked by the {@linkplain #evolve(int)} method in order
     * to evaluate specimen during evolution.
     * 
     * @return true if a proper fitness function is found, false otherwise.
     */
    public final boolean findFitnessFunction() {
        ReflectionFitness reflectionFitness = new ReflectionFitness();
        if(reflectionFitness.findUserFitness(parent)) {
            fitness = reflectionFitness;
            return true;
        }
        else return false;
    }
    
    /**
     * Primary evolution method. Creates and evaluates a number of generations specified by 
     * maxGenerations parameter. Generation stops if one of the stop conditions is met.
     * @param maxGenerations Number of generations to iterate over.
     * @return Best Specimen of the last generation.
     * @see StopCondition
     */
    public Specimen evolve(int maxGenerations) {
        if(fitness == null) {
            System.err.print("ERROR! No Fitness funtion specified.\n Aborting evolution.\n");
            return null;
        }
        for (int i = 0; i < maxGenerations; i++) {
            try {
                
                System.out.print("Evolving… \n");
                Population nextPopulation = currentPopulation.nextGeneration(getPreselection(), getFitness(), getMating(), getBreeding(), getMutantSelection(), getMutation());
                generationCount++;
                System.out.print("Generation " + generationCount + "\n");
                currentPopulation = nextPopulation;
                getHistory().recordPopulation(currentPopulation);
            }
                catch (Exception e) {
                    System.out.println();
                    e.printStackTrace(System.err);
                    System.out.print("Something went wrong. Evolution stopped at generation " + generationCount + "\n");
            }
            if(stopCondition.check()) break;
        }
        return currentPopulation.getBestSpecimen(goal);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Getters & Setters">
    /**
     * @return the parent
     */
    public PApplet getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(PApplet parent) {
        this.parent = parent;
    }
    
    /**
     * @return the stopCondition
     */
    public StopCondition getStopCondition() {
        return stopCondition;
    }

    /**
     * @param stopCondition the stopCondition to set
     */
    public void setStopCondition(StopCondition stopCondition) {
        this.stopCondition = stopCondition;
    }

    /**
     *
     * @return
     */
    public Population getCurrentPopulation() {
        return currentPopulation;
    }

    /**
     * @return the preselection
     */
    public Preselection getPreselection() {
        return preselection;
    }

    /**
     * @param preselection the preselection to set
     */
    public void setPreselection(Preselection preselection) {
        this.preselection = preselection;
    }

    /**
     * @return the mating
     */
    public Mating getMating() {
        return mating;
    }

    /**
     * @param mating the mating to set
     */
    public void setMating(Mating mating) {
        this.mating = mating;
    }

    /**
     * @return the breeding
     */
    public Breeding getBreeding() {
        return breeding;
    }

    /**
     * @param breeding the breeding to set
     */
    public void setBreeding(Breeding breeding) {
        this.breeding = breeding;
    }

    /**
     * @return the fitness
     */
    public Fitness getFitness() {
        return fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(Fitness fitness) {
        this.fitness = fitness;
    }

    /**
     * @return the mutantSelection
     */
    public MutantSelection getMutantSelection() {
        return mutantSelection;
    }

    /**
     * @param mutantSelection the mutantSelection to set
     */
    public void setMutantSelection(MutantSelection mutantSelection) {
        this.mutantSelection = mutantSelection;
    }
    
    /**
     * @return the mutation
     */
    public Mutation getMutation() {
        return mutation;
    }

    /**
     * @param mutation the mutation to set
     */
    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }

    /**
     * @return the history
     */
    public History getHistory() {
        return history;
    }
// </editor-fold>
    
    /**
     * Inner class for specifing stop conditions.
     * Three conditions implemented: generation limit, fitness convergence, fitness goal.
     * Each of these can be turned off or on, and each has a separate threshold to specify.
     */
    public class StopCondition {
        private boolean disabled;
        private boolean stopOnGeneration;
        private boolean stopOnConvergence;
        private boolean stopOnFitnessScore;
        private int generationLimit;
        private float convergenceThreshold;
        private float fitnessThreshold;

        /**
         * Constructor setting the default setup: <br>
         * {@linkplain #stopOnGeneration} - false <br>
         * {@linkplain #stopOnConvergence} - true with {@linkplain #convergenceThreshold} = 0.0f <br>
         * {@linkplain #stopOnFitnessScore} - true if goal is set to MINIMISE <br>
         * 
         * @see frazer.constants.Goal
         */
        public StopCondition() {
            stopOnConvergence = true;
            stopOnGeneration = false;
            
            if(goal == Goal.MINIMISE) {
                stopOnFitnessScore = true;
                fitnessThreshold = 0.0f;
            }
            else stopOnFitnessScore = false;
            
            convergenceThreshold = 0.0f;
            generationLimit = Integer.MAX_VALUE;
        }
        
        /**
         * Checks if the specified conditions are met. 
         * This method is invoked upon calling {@linkplain Frazer#evolve(int)}.
         * @return True when any of the conditions are met, false otherwise.
         */
        public boolean check() {
            if(stopOnConvergence)
                if(fitnessConvergenceCheck()) return true;
            if(stopOnGeneration)
                if(generationLimitCheck()) return true;
            if(stopOnFitnessScore)
                if(fitnessThresholdCheck()) return true;
            return false;
        }
        
        /**
         * Checks if the range fitness scores of current population is below 
         * convergence threshold.
         * @return 
         * @see #convergenceThreshold
         * @see Population#minScore
         * @see Population#maxScore
         */
        public boolean fitnessConvergenceCheck() {
            return Math.abs(currentPopulation.getMaxScore() 
                    - currentPopulation.getMinScore()) <= convergenceThreshold;
        }
        
        /**
         * Checks if the best fitness score of current population is below 
         * or above fitness threshold (depending on the goal).
         * @return 
         * @see #fitnessThreshold
         * @see Population#minScore
         * @see Population#maxScore
         */
        public boolean fitnessThresholdCheck() {
            if(goal == Goal.MINIMISE && currentPopulation.getMinScore() <= fitnessThreshold)
                return true;
            if(goal == Goal.MAXIMISE && currentPopulation.getMaxScore() >= fitnessThreshold)
                return true;
            return false;
        }
        
        /**
         * Checks if the number of generations is above generation limit.
         * @return 
         * @see #generationLimit
         * @see Frazer#generationCount
         */
        public boolean generationLimitCheck() {
            return generationCount >= generationLimit;
        }
        
// <editor-fold defaultstate="collapsed" desc="Getters & Setters">

        /**
         * @param stopOnGeneration the stopOnGeneration to set
         */
        public void setStopOnGeneration(boolean stopOnGeneration) {
            this.stopOnGeneration = stopOnGeneration;
            if (stopOnGeneration) {
                disabled = false;
            }
        }

        /**
         * @param stopOnConvergence the stopOnConvergence to set
         */
        public void setStopOnConvergence(boolean stopOnConvergence) {
            this.stopOnConvergence = stopOnConvergence;
            if (stopOnConvergence) {
                disabled = false;
            }
        }

        /**
         * @param stopOnFitnessScore the stopOnFitnessScore to set
         */
        public void setStopOnFitnessScore(boolean stopOnFitnessScore) {
            this.stopOnFitnessScore = stopOnFitnessScore;
            if (stopOnFitnessScore) {
                disabled = false;
            }
        }

        /**
         * @return the generationLimit
         */
        public int getGenerationLimit() {
            if (stopOnGeneration) {
                return generationLimit;
            } else {
                return 0;
            }
        }

        /**
         * Set and enable generation limit stop condition.
         *
         * @param generationLimit the generationLimit to set
         */
        public void setGenerationLimit(int generationLimit) {
            this.generationLimit = generationLimit;
            stopOnGeneration = true;
            disabled = false;
        }

        /**
         * @return the convergenceThreshold
         */
        public float getConvergenceThreshold() {
            if (stopOnConvergence) {
                return convergenceThreshold;
            } else {
                return 0.0f;
            }
        }

        /**
         * Set and enable the convergence threshold stop condition.
         *
         * @param convergenceThreshold the convergenceThreshold to set
         */
        public void setConvergenceThreshold(float convergenceThreshold) {
            this.convergenceThreshold = convergenceThreshold;
            stopOnConvergence = true;
            disabled = false;
        }

        /**
         * @return the fitnessThreshold
         */
        public float getFitnessThreshold() {
            if (stopOnFitnessScore) {
                return fitnessThreshold;
            } else {
                return 0.0f;
            }
        }

        /**
         * Set and enable the fitness threshold stop condition.
         *
         * @param fitnessThreshold the fitnessThreshold to set
         */
        public void setFitnessThreshold(float fitnessThreshold) {
            this.fitnessThreshold = fitnessThreshold;
            stopOnFitnessScore = true;
            disabled = false;
        }

// </editor-fold>

        /**
         * 
         * @return true if general switch for checking conditions is on.
         */
        public boolean isEnabled() {
            return !disabled;
        }

        /**
         * Disable all stop conditions. Does not change any specific
         * conditions properties. 
         * @see #enable() 
         */
        public void disable() {
            this.disabled = true;
        }
        /**
         * Enable checking stop conditions. Does not enable any specific 
         * conditions, it is a general switch.
         * @see #disable() 
         */
        public void enable() {
            this.disabled = false;
        }
        
        
    }
    
    protected final class Algorithms {
    
    }

}
