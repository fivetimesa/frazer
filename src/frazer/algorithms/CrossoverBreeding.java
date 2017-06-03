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
import frazer.genotypes.Genotype;
import frazer.interfaces.Breeding;
import java.util.Arrays;
import java.util.Random;

/**
 *
 */
public class CrossoverBreeding implements Breeding {
    
    private int crossoverPointsCount;
    
    public CrossoverBreeding() {
        this.crossoverPointsCount = -1;
    }

    public CrossoverBreeding(int crossoverPointsCount) {
        this.crossoverPointsCount = crossoverPointsCount;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Specimen[] breed(Specimen[] parent) {
        
        //System.out.print("Breeding children… \n");
        //System.out.print("Received " + parent.length + " parents. \n");
        Specimen[] children;
        if (parent.length == 0) 
            throw new IllegalArgumentException("No parents received");
        children = new Specimen[parent.length];
        if (parent.length == 1) {
            children[0] = parent[0].copy();
            return children;
        }
        //System.out.print("Choosing crossover points… \n");
        Random random = new Random();
        //if(parent[0] != null) //System.out.print("Parent is not null. \n");
        //if(parent[0].getGenes() != null) //System.out.print("Genes are not null. \n");
        //System.out.print("Genotype length = " + parent[0].getGenes().getGeneCount());
        int geneCount = parent[0].getGenes().getGeneCount();
        //System.out.print("Gene count = " + geneCount + " \n");
        if(crossoverPointsCount == -1) crossoverPointsCount = parent.length - 1;
        int[] crossOverPoints = new int[crossoverPointsCount];
        for (int i = 0; i < crossOverPoints.length; i++) {
            crossOverPoints[i] = random.nextInt(geneCount);
            //System.out.print("Crossover point #" + i + " = " + crossOverPoints[i] + " \n");
        }
        //System.out.print("Sorting crossover points… \n");
        Arrays.sort(crossOverPoints);
        //System.out.print("Crossover points chosen. \n");
        for (int i = 0; i < children.length; i++) {
            //System.out.print("Making child #" + i + "… \n");
            Genotype genes = parent[i].getGenes().copy();
            int parentId = i;
            int crossOver = 0;
            for (int g = 0; g < geneCount; g++) {
                if (crossOver < crossOverPoints.length) {
                    if (g == crossOverPoints[crossOver]) {
                        //System.out.print("c");
                        parentId++;
                        crossOver++;
                    }
                }
                
                if (parentId >= parent.length)
                    parentId -= parent.length;
                if (parentId != i)
                    genes.setGene(g, parent[parentId].getGenes().getGene(g));
                //System.out.print(parentId + "|");
            }
            children[i] = parent[0].makeChild(genes);
            //System.out.print("\nChild #" + i + " ready. \n");
        }
        return children;
    }

    /**
     * @return the crossoverPointsCount
     */
    public int getCrossoverPointsCount() {
        return crossoverPointsCount;
    }

    /**
     * @param crossoverPointsCount the crossoverPointsCount to set
     */
    public void setCrossoverPointsCount(int crossoverPointsCount) {
        this.crossoverPointsCount = crossoverPointsCount;
    }
    
}
