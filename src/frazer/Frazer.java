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
import frazer.genotypes.*;
import frazer.interfaces.*;
import java.util.ArrayList;
import processing.core.*;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class Frazer {

// <editor-fold defaultstate="collapsed" desc="CONSTANTS">

//    final static byte BITGENOTYPE = 0;
//    final static byte FLOATGENOTYPE = 1;
//    final static byte SFLOATGENOTYPE = 2;
//    final static byte INTGENOTYPE = 3;
// </editor-fold>    
    
    private PApplet parent;
    private ArrayList<Population> populationList;
    private Population currentPopulation;
    private GenotypeDescription gD;
    
    private int generationCount;
    
    private Goal goal;
    private int populationCount;
    
    private Preselection preselection;
    private Mating mating;
    private Breeding breeding;
    private Fitness fitness;
    private Mutation mutation;
    
    
    /**
     *
     * @param parent reference to Processing sketch. Usually use "this".
     */
    public Frazer(PApplet parent)
    {
        this.parent = parent;
        populationList = new ArrayList<>();
        setDefaults();
    }
    
    public Frazer(PApplet parent, int populationCount, int geneCount, GenotypeType genotypeType, Fitness fitness)
    {
        this.parent = parent;
        populationList = new ArrayList<>();
        
        this.gD = new GenotypeDescription(geneCount, genotypeType);
        currentPopulation = new Population(populationCount, geneCount, genotypeType);
        populationList.add(currentPopulation);
        this.fitness = fitness;
        
        setDefaults();
    }
    
    private void setDefaults() {
        goal = Goal.MINIMISE;
        preselection = new NoPreselection();
        mating = new TournamentMating(goal);
        breeding = new CrossoverBreeding();
        if(gD.getGenotypeType() == GenotypeType.FLOAT)
            mutation = new SimpleFloatMutation();
        else mutation = new NoMutation();
    }
    
    public Specimen evolve(int maxGenerations) {
        for (int i = 0; i < maxGenerations; i++) {
            try {
                
                System.out.print("Evolving… \n");
                Population nextPopulation = currentPopulation.nextGeneration(preselection, fitness, mating, breeding, mutation);
                generationCount++;
                System.out.print("Generation " + generationCount + "\n");
                currentPopulation = nextPopulation;
            }
                catch (Exception e) {
                    System.out.print("Something went wrong. Evolution stopped at generation " + generationCount + "\n");
            }
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
    
    public Population getCurrentPopulation() {
        return currentPopulation;
    }

// </editor-fold>

}
