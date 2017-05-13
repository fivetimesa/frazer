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
    
    public void setDefaults() {
        mating = new TournamentMating(minimise == 1);
    }
    
    public void evolve(int maxGenerations) throws Exception {
        for (int i = 0; i < maxGenerations; i++) {
            currentPopulation = currentPopulation.nextGeneration(preselection, fitness, mating, breeding, mutation);
        }
    }
    
    //<editor-fold desc="Static private classes" defaultstate="collapsed">
    /* STATIC PRIVATE CLASSES */
    static private class RouletteWheelMating implements Mating {

        @Override
        public boolean needsSorting() {
            return true;
        }
        
        @Override
        public Specimen[] selectParents(Specimen[] specimens) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }

    /**
     * 
     */
    static private class TournamentMating implements Mating {
        
        boolean minimise;
        
        TournamentMating(boolean minimise) {
            this.minimise = minimise;
        }

        @Override
        public boolean needsSorting() {
            return false;
        }

        @Override
        public Specimen[] selectParents(Specimen[] specimens) {
            final int parentsCount = 2;
            final int candidateCount = 3;
            Specimen[] parents = new Specimen[parentsCount];
            Random random = new Random();
            
            for(int i = 0; i < parentsCount; i++) {
                Specimen candidate = specimens[random.nextInt(specimens.length)];
                float candidateScore = candidate.getFitnessScore();
                for(int j = 1; j < candidateCount; j++) {
                    Specimen newCandidate = specimens[random.nextInt(specimens.length)];
                    float newCandidateScore = candidate.getFitnessScore();
                    if((minimise && newCandidateScore < candidateScore) || 
                      (!minimise && newCandidateScore < candidateScore)) {
                        candidate = newCandidate;
                        candidateScore = newCandidateScore;
                    }
                }
                parents[i] = candidate;
            }
            return parents;
        }

    }

    /**
     * 
     */
    static private class CrossoverBreeding implements Breeding {

        @Override
        @SuppressWarnings("unchecked")
        public Specimen[] breed(Specimen[] parent) {
            Specimen[] children;
            
            if(parent.length == 0) throw new IllegalArgumentException("No parents received");
            children = new Specimen[parent.length];
            
            if(parent.length == 1) {
                children[0] = parent[0].copy();
                return children;
            }
            Random random = new Random();
            int geneCount = parent[0].getGenes().getGeneCount();
            int[] crossOverPoints = new int[parent.length - 1];
            for (int i = 0; i < crossOverPoints.length; i++) {
                crossOverPoints[i] = random.nextInt(geneCount);
            }
            Arrays.sort(crossOverPoints);
            
            for(int i = 0; i < children.length; i++) {
                Genotype genes = parent[i].getGenes().copy();
                int parentId = i;
                for(int g = 0; g < geneCount; g++) {
                    if(g < crossOverPoints.length) {
                        if (g == crossOverPoints[parentId]) {
                            parentId++;
                        }
                    }
                    if(parentId >= parent.length) parentId -= parent.length;
                    if(parentId != i) genes.setGene(g, parent[parentId].getGenes().getGene(g));
                }
                children[i] = new Specimen(genes);
            }
            return children;
        }

    }

    /**
     * 
     */
    static private class ExtrapolatedBreeding implements Breeding {

        @Override
        public Specimen[] breed(Specimen[] parent) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
    //</editor-fold>
    
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

// </editor-fold>
}
