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

import frazer.Population;
import frazer.Specimen;
import frazer.constants.Goal;
import frazer.interfaces.Mating;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class RouletteWheelMating implements Mating {

    double[] roulette;
    private Goal goal;
    private int parentsCount;

    public RouletteWheelMating(Goal goal) {
        this.parentsCount = 2;
        this.goal = goal;
    }

    @Override
    public boolean needsSorting() {
        return true;
    }

    @Override
    public void initialize(Population population) {
        int n = population.getCount();
        Specimen[] specimens = population.getSpecimens();
        roulette = new double[n];

        if (goal == Goal.MAXIMISE) {
            double len = 0;
            for (int i = 0; i < n; i++) {
                len += specimens[i].getFitnessScore();
                roulette[i] = len;
            }
        }

        if (goal == Goal.MINIMISE) {
            double len = 0;
            double rangeValue
                    = specimens[0].getFitnessScore() + specimens[n - 1].getFitnessScore();
            for (int i = 0; i < n; i++) {
                len += (rangeValue - specimens[i].getFitnessScore());
                roulette[i] = len;
            }
        }
    }

    @Override
    public Specimen[] selectParents(Population population) {
        Specimen[] specimens = population.getSpecimens();
        Specimen[] parents = new Specimen[parentsCount];
        Random random = new Random();
        for (int i = 0; i < parentsCount; i++) {
            double r = random.nextDouble();
            int index = Arrays.binarySearch(roulette, r);
            int insertionIndex;
            if (index < 0)
                insertionIndex = ((-index) - 1);
            else
                insertionIndex = index;
            if(index == specimens.length)
                index--;
            parents[i] = specimens[i];
        }

        return parents;
    }

    public int getParentsCount() {
        return parentsCount;
    }

    public void setParentsCount(int parentsCount) {
        this.parentsCount = parentsCount;
    }

}
