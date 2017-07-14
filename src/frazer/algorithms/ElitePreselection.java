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
import frazer.interfaces.Preselection;


public class ElitePreselection implements Preselection {
    private Goal goal;
    private float eliteFraction;

    public ElitePreselection(Goal goal) {
        this.goal = goal;
        eliteFraction = 0.1f;
    }
    
    public ElitePreselection(Goal goal, float eliteFraction) {
        this.goal = goal;
        this.eliteFraction = eliteFraction;
        if(this.eliteFraction >= 1) this.eliteFraction = 0.99f;
    }

    @Override
    public boolean needsSorting() {
        return true;
    }
    
    @Override
    public Specimen[] selectElite(Population population) {
        Specimen[] specimens = population.getSpecimens();
        
        int eliteCount = (int) (specimens.length * eliteFraction);
        if(eliteCount < 1) 
            eliteCount = 1;
        
        Specimen[] elite = new Specimen[eliteCount];
        
        
        for(int i = 0; i < eliteCount; i++)
            if(goal == Goal.MINIMISE)
                elite[i] = specimens[i];
            else if(goal == Goal.MAXIMISE)
                elite[i] = specimens[specimens.length - 1 - i];
        
        return elite;
    }

    @Override
    public Specimen[] discardWorst(Population population) {
        return population.getSpecimens();
    }

    /**
     * @param goal the goal to set
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }
    
    /**
     *
     * @param eliteFraction
     */
    public void setEliteFraction(float eliteFraction) {
        this.eliteFraction = eliteFraction;
        if(this.eliteFraction >= 1) this.eliteFraction = 0.99f;
    }
}
