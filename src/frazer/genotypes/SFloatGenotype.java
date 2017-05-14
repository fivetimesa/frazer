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
package frazer.genotypes;

import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class SFloatGenotype extends FloatGenotype {

    private float[] steps;

    public SFloatGenotype(int count) {
        super(count);
        this.steps = new float[count];
        this.randomInit();
    }

    @Override
    public Genotype copy() {
        SFloatGenotype copy = new SFloatGenotype(genes.length);
        for (int i = 0; i < genes.length; i++) {
            copy.setGene(i, getGene(i));
            copy.setStep(i, getStep(i));
        }
        return copy;
    }

    @Override
    public void randomInit() {
        Random generator = new Random();
        for (int i = 0; i < genes.length; i++) {
            genes[i] = generator.nextFloat();
            steps[i] = generator.nextFloat();
        }
    }

    public void randomInit(float min, float max) {
        Random generator = new Random();
        for (int i = 0; i < genes.length; i++) {
            genes[i] = min + generator.nextFloat() * (max - min);
            steps[i] = generator.nextFloat();
        }
    }

    public Float getStep(int i) {
        return steps[i]; //To change body of generated methods, choose Tools | Templates.
    }

    public void setStep(int i, float step) {
        this.steps[i] = step;
    }

    /**
     * Creates "[ [x_1,s_1], [x_2,s_2] ... [x_n,s_n] ]" type text
     * representation.
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        int n = genes.length;
        for (int i = 0; i < n; i++) {
            sb.append("[");
            sb.append(genes[i]);
            sb.append(",");
            sb.append(genes[i]);
            sb.append("]");
            if (i < n - 1)
                sb.append(", ");
        }
        sb.append(" ]");
        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
