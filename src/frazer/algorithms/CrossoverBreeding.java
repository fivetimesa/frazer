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

    @Override
    @SuppressWarnings(value = "unchecked")
    public Specimen[] breed(Specimen[] parent) {
        Specimen[] children;
        if (parent.length == 0)
            throw new IllegalArgumentException("No parents received");
        children = new Specimen[parent.length];
        if (parent.length == 1) {
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
        for (int i = 0; i < children.length; i++) {
            Genotype genes = parent[i].getGenes().copy();
            int parentId = i;
            for (int g = 0; g < geneCount; g++) {
                if (g < crossOverPoints.length) {
                    if (g == crossOverPoints[parentId]) {
                        parentId++;
                    }
                }
                if (parentId >= parent.length)
                    parentId -= parent.length;
                if (parentId != i)
                    genes.setGene(g, parent[parentId].getGenes().getGene(g));
            }
            children[i] = new Specimen(genes);
        }
        return children;
    }
    
}
