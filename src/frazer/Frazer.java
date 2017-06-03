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
    private final History history;
    private Plotter plotter;
    private Population currentPopulation;
    
    private GenotypeDescription gD;
    private SpecimenFactory specimenFactory;

    /** StopCondition options. */
    private StopCondition stopCondition;
    
    /** Generation counter. Keeps track of the number of evaluated generations. */
    private int generationCount;
    
    /** The goal of the optimisation process (either to minimise of to maximise). */
    private Goal goal;
    
    private Algorithms algorithms;
    
    /**
     *
     * @param parent reference to Processing sketch. Usually use "this".
     */
    public Frazer(PApplet parent)
    {
        this.parent = parent;
        history = new History();
        algorithms = new Algorithms();
        algorithms.setDefaults();
        
        specimenFactory = new SpecimenFactory();
    }
    
    /**
     *
     * @param parent
     * @param populationCount
     * @param geneCount
     */
    public Frazer(PApplet parent, int populationCount, int geneCount)
    {
        this.parent = parent;
        history = new History();
        algorithms = new Algorithms();
        
        specimenFactory = new SpecimenFactory();
        
        if(findFitnessFunction()) {
            GenotypeType genotypeType = ((ReflectionFitness) algorithms.fitness).getGeontypeType();
            gD = new GenotypeDescription(geneCount, genotypeType);
            System.out.print(gD.getGenotypeType());
            currentPopulation = new Population(specimenFactory.createSpecimens(populationCount));
            
            currentPopulation.evaluate(algorithms.fitness);
            history.recordPopulation(currentPopulation);
        }
        algorithms.setDefaults();
    }
    
    
    public Frazer(PApplet parent, int populationCount, int geneCount, GenotypeType genotypeType, CustomSpecimen specimen)
    {
        
        
        
        this.parent = parent;
        history = new History();
        algorithms = new Algorithms();
        specimenFactory = new SpecimenFactory();
        specimenFactory.setSpecimenClass(specimen.getClass());
        
        this.gD = new GenotypeDescription(geneCount, genotypeType);
        currentPopulation = new Population(specimenFactory.createSpecimens(populationCount));
        algorithms.fitness = new SpecimenFitness();
        
        history.recordPopulation(currentPopulation);
        algorithms.setDefaults();
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
        algorithms = new Algorithms();
        specimenFactory = new SpecimenFactory();
        
        this.gD = new GenotypeDescription(geneCount, genotypeType);
        currentPopulation = new Population(specimenFactory.createSpecimens(populationCount));
        this.algorithms.fitness = fitness;
        
        currentPopulation.evaluate(fitness);
        history.recordPopulation(currentPopulation);
        
        algorithms.setDefaults();
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
            algorithms.fitness = reflectionFitness;
            return true;
        }
        else return false;
    }
    
    public final void restart() {
        int populationCount = currentPopulation.getCount();
        currentPopulation = new Population(specimenFactory.createSpecimens(populationCount));
    }
    
    /**
     * Primary evolution method. Creates and evaluates a number of generations specified by 
     * maxGenerations parameter. Generation stops if one of the stop conditions is met.
     * @param maxGenerations Number of generations to iterate over.
     * @return Best Specimen of the last generation.
     * @see StopCondition
     */
    public Specimen evolve(int maxGenerations) {
        if(algorithms.fitness == null) {
            System.err.print("ERROR! No Fitness funtion specified.\n Aborting evolution.\n");
            return null;
        }
        for (int i = 0; i < maxGenerations; i++) {
            if(stopCondition.check()) break;
            try {
                
                System.out.print("Evolving… \n");
                Population nextPopulation = currentPopulation.nextGeneration(algorithms);
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
        }
        return currentPopulation.getBestSpecimen(getGoal());
    }
    
    public void evaluateCurrent() {
        if(algorithms.fitness == null) {
            System.err.print("ERROR! No Fitness funtion specified.\n Aborting evolution.\n");
        } else {
            currentPopulation.evaluate(algorithms.fitness);
        }
    }
    
    public Specimen[] evolve() {
        if(algorithms.fitness == null) {
            System.err.print("ERROR! No Fitness funtion specified.\n Aborting evolution.\n");
            return null;
        }
        try {
            System.out.print("Evolving… \n");
            Population nextPopulation = currentPopulation.nextGeneration(algorithms);
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
        return currentPopulation.getSpecimens();
    }
    
    public void launchPlotter() {
        plotter = new Plotter(history);
    }
    
    /**
     * @return the goal
     */
    public Goal getGoal() {
        return goal;
    }

    /**
     * @param goal the goal to set
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Getters & Setters">
    /**
     * @return the parent
     */
    public PApplet getParent() {
        return parent;
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
    protected void setStopCondition(StopCondition stopCondition) {
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
        return algorithms.preselection;
    }

    /**
     * @param preselection the preselection to set
     */
    public void setPreselection(Preselection preselection) {
        this.algorithms.preselection = preselection;
        preselection.initialize(gD);
    }

    /**
     * @return the mating
     */
    public Mating getMating() {
        return algorithms.mating;
    }

    /**
     * @param mating the mating to set
     */
    public void setMating(Mating mating) {
        this.algorithms.mating = mating;
        mating.initialize(gD);
    }

    /**
     * @return the breeding
     */
    public Breeding getBreeding() {
        return algorithms.breeding;
    }

    /**
     * @param breeding the breeding to set
     */
    public void setBreeding(Breeding breeding) {
        this.algorithms.breeding = breeding;
        breeding.initialize(gD);
    }

    /**
     * @return the fitness
     */
    public Fitness getFitness() {
        return algorithms.fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(Fitness fitness) {
        this.algorithms.fitness = fitness;
        fitness.initialize(gD);
    }

    /**
     * @return the mutantSelection
     */
    public MutantSelection getMutantSelection() {
        return algorithms.mutantSelection;
    }

    /**
     * @param mutantSelection the mutantSelection to set
     */
    public void setMutantSelection(MutantSelection mutantSelection) {
        this.algorithms.mutantSelection = mutantSelection;
        mutantSelection.initialize(gD);
    }
    
    /**
     * @return the mutation
     */
    public Mutation getMutation() {
        return algorithms.mutation;
    }

    /**
     * @param mutation the mutation to set
     */
    public void setMutation(Mutation mutation) {
        this.algorithms.mutation = mutation;
        mutation.initialize(gD);
    }

    /**
     * @return the history
     */
    public History getHistory() {
        return history;
    }
    
    public void setGeneLimits(float min, float max) {
        gD.setGeneLimits(min, max);
        currentPopulation.applyGeneLimits(gD);
    }
    
    public void setGeneLimits(float[] min, float[] max) {
        gD.setGeneLimits(min, max);
        currentPopulation.applyGeneLimits(gD);
    }
    
    public void setMating(MatingType type) {
        switch(type) {
            case TOURNAMENT:
                setMating(new TournamentMating(getGoal()));
                break;
            case ROULETTEWHEEL:
                setMating(new RouletteWheelMating(getGoal()));
                break;
        }
    }
    
    public void setBreeding(BreedingType type) {
        switch(type) {
            case CROSSOVER:
                setBreeding(new CrossoverBreeding());
                break;
            case EXTRAPOLATED:
                setBreeding(new ExtrapolatedBreeding());
                break;
        }
    }
    
    public void setPreselection(PreselectionType type) {
        switch(type) {
            case NONE:
                setPreselection(new NoPreselection());
                break;
            case ELITISM:
                throw new UnsupportedOperationException("not supported yet");
        }
    }
    
    public void setMutantSelection(MutantSelectionType type) {
        switch(type) {
            case CHANCE:
                setMutantSelection(new ChanceMutantSelection());
                break;
            case FIXEDCOUNT:
                setMutantSelection(new FixedCountMutantSelection());
                break;
            case UNIQUEFIXEDCOUNT:
                setMutantSelection(new UniqueFixedCountMutantSelection());
                break;
            case WORSTFITNESS:
                setMutantSelection(new WorstFitnessMutantSelection());
                break;
        }
    }
    
    public void setMutation(MutationType type) {
        switch(type) {
            case NONE:
                setMutation(new NoMutation());
                break;
            case BIT:
                setMutation(new BitMutation());
                break;
            case CONSTANTVALUE:
                setMutation(new ConstantValueMutation(0.5f));
                break;
            case RANGE:
                setMutation(new RangeValueMutation(0.5f));
                break;
        }
    }
// </editor-fold>
    
    public static enum MatingType {
       TOURNAMENT, ROULETTEWHEEL, CUSTOM
    }
    
    public static enum BreedingType {
       CROSSOVER, EXTRAPOLATED, CUSTOM
    }
    
    public static enum PreselectionType {
        NONE, ELITISM, CUSTOM
    }
    
    public static enum MutantSelectionType {
        CHANCE, FIXEDCOUNT, UNIQUEFIXEDCOUNT, WORSTFITNESS, CUSTOM
    }
    
    public static enum MutationType {
        NONE, BIT, CONSTANTVALUE, RANGE, CUSTOM
    }

    
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
            
            if(getGoal() == Goal.MINIMISE) {
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
            if(getGoal() == Goal.MINIMISE && currentPopulation.getMinScore() <= fitnessThreshold)
                return true;
            if(getGoal() == Goal.MAXIMISE && currentPopulation.getMaxScore() >= fitnessThreshold)
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
    
    /**
     * Convenience inner class for storing all the algorithms.
     * Fields accessible trough getters and setters in Frazer.
     */
    protected final class Algorithms {
        
        /** Algorithm for preselection. */
        protected Preselection preselection;
        /** Algorithm for mating. */
        protected Mating mating;
        /** Algorithm for breeding. */
        protected Breeding breeding;
        /** Algorithm for fitness, to be specified by the user. */
        protected Fitness fitness;
        /** Algorithm for mutant selection. */
        protected MutantSelection mutantSelection;
        /** Algorithm for mutation. */
        protected Mutation mutation;
        
        protected AlgorithmsInterface[] getAlgorithms() {
            return new AlgorithmsInterface[]{preselection, fitness, mating, breeding, mutantSelection, mutation};
        }
        
        
        protected void setDefaults() {
            if(getGoal() == null) 
                setGoal(Goal.MINIMISE);
            if(preselection == null) 
                setPreselection(new NoPreselection());
            if(mating == null) 
                setMating(new TournamentMating(getGoal()));
            if(breeding == null) {
                switch(gD.getGenotypeType()) {
                    case INTEGER:
                    case SFLOAT:
                    case FLOAT:
                        setBreeding(new ExtrapolatedBreeding());
                        break;
                    case BIT:
                    default:
                        setBreeding(new CrossoverBreeding());
                        break;
                }
            }
                
            if(getMutantSelection() == null)
                setMutantSelection(new ChanceMutantSelection(0.05f));
            if(mutation == null) {
                switch(gD.getGenotypeType()) {
                    case BIT:
                        setMutation(new BitMutation());
                        break;
                    case INTEGER:
                        setMutation(new ConstantValueMutation(1f));
                        break;
                    case SFLOAT:
                    case FLOAT:
                        setMutation(new RangeValueMutation(0.5f));
                        break;
                    default:
                        setMutation(new NoMutation());
                        break;
                }

            }
            if(getStopCondition() == null)
                setStopCondition(new StopCondition());

        }
    
        protected void update(Population population) {
            for(AlgorithmsInterface algorithm: getAlgorithms())
                algorithm.update(population);
        }
        
        protected void initialize(GenotypeDescription gD) {
            for(AlgorithmsInterface algorithm: getAlgorithms())
                algorithm.initialize(gD);
        }
        
        protected boolean needSorting() {
            for(AlgorithmsInterface algorithm: getAlgorithms())
                if(algorithm.needsSorting()) return true;
            return false;
        }
    }

    private class SpecimenFactory {
        Class SpecimenClass = Specimen.class;
    
        public Specimen createSpecimen() {
            GenotypeType genotypeType = gD.getGenotypeType();
            int geneCount = gD.getGeneCount();
            
            Genotype genes = null;
            
            switch(genotypeType) {
                case BIT:
                    genes = new BitGenotype(geneCount);
                    break;
                case INTEGER:
                    genes = new IntegerGenotype(geneCount);
                    break;
                case FLOAT:
                    genes = new FloatGenotype(geneCount);
                    break;
                case SFLOAT:
                    genes = new SFloatGenotype(geneCount);
                    break;
                default:
                    System.err.println("ERROR! Genotype type not found.");
            }
            return createSpecimen(genes);
        }
        
        public Specimen createSpecimen(Genotype genes) {
            Specimen newSpecimen = null;
            try { 
                newSpecimen = (Specimen) SpecimenClass.newInstance();
                newSpecimen.setGenes(genes);
            }
            catch (Exception e) {
                System.err.println("Error on instantiating a specimen");
            }
            return newSpecimen;
        }
        
        public Specimen[] createSpecimens(int count) {
            Specimen[] specimens = new Specimen[count];
            for(int i = 0; i < count; i++) 
                specimens[i] = createSpecimen();
            return specimens;
        }
        
        public void setSpecimenClass(Class specimenClass) {
            this.SpecimenClass = specimenClass;
        }
        
        public boolean hasCustomSpecimen() {
            return !SpecimenClass.equals(Specimen.class);
        }
    }
}
