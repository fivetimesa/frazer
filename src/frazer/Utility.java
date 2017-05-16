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

import frazer.genotypes.*;

/**
 * Utility class is a collection of static methods usefull when coding 
 * fitness functions. It features various mappings and conversions from 
 * supported gene types to several types of data.
 * @author Teodor Michalski
 */
public class Utility {
    
    private Utility() {}
    /**
     * Utility method for remapping unbound float gene to a given range using 
     * a saw-like function.
     * @param gene value to remap
     * @param min lower bound
     * @param max upper bound
     * @return
     */
    public static float sawLimit(float gene, float min, float max) {
        int div = (int) Math.floor(gene / (max - min));
        float mod = gene % (max - min);
        float val = min;
        if(div % 2 == 1) 
            val += mod;
        else
            val += (max - min) - mod;
        return val;
    }
    
    /**
     * Utility method for remapping unbound float gene to a given range using 
     * a sine function.
     * @param gene value to remap
     * @param min lower bound
     * @param max upper bound
     * @return
     */
    public static float sinLimit(float gene, float min, float max) {
        float sin = (float) Math.sin(gene * 2d * Math.PI);
        return (min + sin * (max - min));
    }

    /**
     * Utility method for converting an unbound float gene to single char.
     * @param gene value to convert
     * @return ASCII char
     */
    public static char floatGeneToChar(float gene) {
        float map = sinLimit(gene, 0f, 255f);
        return (char) (int) map;
    }

    /**
     * Utility method for converting unbound whole genotype of unbound floats 
     * to a single String. 
     * @param genes genotype to convert
     * @return genes converted to String 
     */
    public static String floatGeontypeToString(Genotype genes) {
        if(!genes.getClass().equals(FloatGenotype.class))
            System.err.print("Genotype type mismatch. Expected float genotype");
        StringBuilder sb = new StringBuilder();
        for (Object g : genes) {
            char c = floatGeneToChar((float) g);
            sb.append(c);
        }
        return sb.toString();
    }
}
