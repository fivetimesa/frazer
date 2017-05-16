/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frazer;

import frazer.genotypes.*;

/**
 *
 * @author TM1
 */
public class Utility {
    
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
    
    public static float sinLimit(float gene, float min, float max) {
        float sin = (float) Math.sin(gene * 2d * Math.PI);
        return (min + sin * (max - min));
    }

    public static char floatGeneToChar(float gene) {
        float map = sinLimit(gene, 0f, 255f);
        return (char) (int) map;
    }

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
