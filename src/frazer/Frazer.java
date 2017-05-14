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
import frazer.algorithms.NoPreselection;
import frazer.algorithms.NoMutation;
import frazer.algorithms.CrossoverBreeding;
import frazer.algorithms.TournamentMating;
import frazer.genotypes.Genotype;
import frazer.interfaces.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import processing.core.*;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class Frazer {
    private PApplet parent;
    private ArrayList<Population> populationList;
    private Population currentPopulation;
    
    private int generationCount;
    
    private byte genotypeType;
    private byte minimise = 1;
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
    
    public Frazer(PApplet parent, int populationCount, int genotypeCount, byte genotypeType, Fitness fitness)
    {
        this.parent = parent;
        populationList = new ArrayList<>();
        
        currentPopulation = new Population(populationCount, genotypeCount, genotypeType);
        populationList.add(currentPopulation);
        this.fitness = fitness;
        
        setDefaults();
    }
    
    private void setDefaults() {
        preselection = new NoPreselection();
        mating = new TournamentMating(minimise == 1);
        breeding = new CrossoverBreeding();
        mutation = new NoMutation();
    }
    
    public void evolve(int maxGenerations) throws Exception {
        for (int i = 0; i < maxGenerations; i++) {
            currentPopulation = currentPopulation.nextGeneration(preselection, fitness, mating, breeding, mutation);
        }
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
