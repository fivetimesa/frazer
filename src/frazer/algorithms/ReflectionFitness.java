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

import frazer.Utility;
import frazer.constants.GenotypeType;
import frazer.genotypes.*;
import frazer.interfaces.Fitness;
import java.lang.reflect.*;
import processing.core.PApplet;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class ReflectionFitness implements Fitness {

    private Method userFitness;
    private GenotypeType genotypeType;
    private ArgumentType argumentType;
    private PApplet parent;
    
    public boolean findUserFitness(PApplet parent) {
        this.parent = parent;
        try {
            Method[] methods = parent.getClass().getDeclaredMethods();
            for(Method m: methods) {
                if(!m.getName().toLowerCase().contains("fitness")) continue;
                if(m.getReturnType() != float.class) continue;
                if(m.getParameterCount() != 1) continue;
                Class parameterType = m.getParameterTypes()[0];
                if(parameterType == BitGenotype.class) {
                    genotypeType = GenotypeType.BIT;
                    argumentType = ArgumentType.BITGENOTYPE;
                    userFitness = m;
                }
                if(parameterType == boolean[].class) {
                    genotypeType = GenotypeType.BIT;
                    argumentType = ArgumentType.BITARRAY;
                    userFitness = m;
                }
                if(parameterType == FloatGenotype.class) {
                    genotypeType = GenotypeType.FLOAT;
                    argumentType = ArgumentType.FLOATGENOTYPE;
                    userFitness = m;
                }
                if(parameterType == float[].class) {
                    genotypeType = GenotypeType.FLOAT;
                    argumentType = ArgumentType.FLOATARRAY;
                    userFitness = m;
                }
                if(parameterType == SFloatGenotype.class) {
                    genotypeType = GenotypeType.SFLOAT;
                    argumentType = ArgumentType.SFLOATGENOTYPE;
                    userFitness = m;
                }
                if(parameterType == IntegerGenotype.class) {
                    genotypeType = GenotypeType.INTEGER;
                    argumentType = ArgumentType.INTEGERGENOTYPE;
                    userFitness = m;
                }
                if(parameterType == int[].class) {
                    genotypeType = GenotypeType.INTEGER;
                    argumentType = ArgumentType.INTARRAY;
                    userFitness = m;
                }
                if(parameterType == char[].class) {
                    genotypeType = GenotypeType.INTEGER;
                    argumentType = ArgumentType.CHARARRAY;
                    userFitness = m;
                }
                if(parameterType == String.class) {
                    genotypeType = GenotypeType.FLOAT;
                    argumentType = ArgumentType.STRING;
                    userFitness = m;
                }
            }
            if(userFitness != null) {
                System.out.println("Yay! Found fitness function in Processing sketch!");
                System.out.println("Genotype type detected: " + genotypeType);
                System.out.println("Argument type detected: " + argumentType);
                return true;
            }
            else {
                System.out.println("Fitness function not found in Processing sketch.");
                return false;
            }
        }
        catch (Exception e) {
            System.err.println("Error when finding function in Processing sketch.");
        }
        return false;
    }
    
    public GenotypeType getGeontypeType() {
        if(userFitness == null) return null;
        else return genotypeType;
    }
    
    @Override
    public float calculateFitness(Genotype genotype) {
        //System.out.println("calling fitness function");
        float fitness = 0;
        if(userFitness != null)
            try {
                switch(argumentType) {
                    case BITGENOTYPE:
                    case FLOATGENOTYPE:
                    case SFLOATGENOTYPE:
                    case INTEGERGENOTYPE:
                        fitness = (float) userFitness.invoke(parent, new Object[] {genotype});
                        break;
                    case BITARRAY:
                        //boolean[] arguments = ((BitGenotype)genotype).getGenes();
                        //fitness = (float) userFitness.invoke(parent, new Object[] {arguments});
                        System.err.println("Not supported yet");
                        break;
                    case INTARRAY:
                        //int[] arguments = ((IntegerGenotype)genotype).getGenes();
                        //fitness = (float) userFitness.invoke(parent, new Object[] {arguments});
                        System.err.println("Not supported yet");
                        break;
                    case FLOATARRAY:
                        float[] arguments = ((FloatGenotype)genotype).getFloatGenes();
                        fitness = (float) userFitness.invoke(parent, new Object[] {arguments});
                        //System.out.println("Fitness function returned: " + fitness);
                        break;
                    case CHARARRAY:
                        //char[] arguments = ((FloatGenotype)genotype).getGenes();
                        //fitness = (float) userFitness.invoke(parent, new Object[] {arguments});
                        System.err.println("Not supported yet");
                        break;
                    case STRING:
                        String argument = Utility.floatGeontypeToString(genotype);
                        fitness = (float) userFitness.invoke(parent, new Object[] {argument});
                        break;
                }
            }
            catch (Exception e) {
                System.err.println("Error on calling fitness");
            }
        return fitness;
    }

    private static enum ArgumentType {
        BITGENOTYPE("BitGenotype"), 
        INTEGERGENOTYPE("IntegerGenotype"), 
        FLOATGENOTYPE("FloatGenotype"), 
        SFLOATGENOTYPE("FloatsGenotype"), 
        BITARRAY("boolean[]"), 
        INTARRAY("int[]"), 
        FLOATARRAY("float[]"), 
        CHARARRAY("char[]"), 
        STRING("String");
        
        private final String argumentName;
 
	ArgumentType(String argumentName) {
		this.argumentName = argumentName;
	}
 
	@Override
	public String toString() {
		return this.argumentName;
	}
    }
    
}
//</editor-fold>
