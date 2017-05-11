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
import processing.core.*;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class Frazer {
    private PApplet parent;
    private ArrayList<Population> populationList;
    private Population currentPopulation;
    
    /**
     *
     * @param parent reference to Processing sketch. Usually use "this".
     */
    public Frazer(PApplet parent)
    {
        this.parent = parent;
        populationList = new ArrayList<>();
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

        @Override
        public boolean needsSorting() {
            return false;
        }

        @Override
        public Specimen[] selectParents(Specimen[] specimens) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    /**
     * 
     */
    static private class CrossoverBreeding implements Breeding {

        @Override
        public Specimen[] breed(Specimen[] parent) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
