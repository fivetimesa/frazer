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

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class BitGenotype extends Genotype<Boolean> {

    public BitGenotype(int count) {
        super(Boolean.class, count);
        this.randomInit();
    }

    @Override
    public Genotype copy() {
        BitGenotype copy = new BitGenotype(genes.length);
        for (int i = 0; i < genes.length; i++) {
            copy.setGene(i, getGene(i));
        }
        return copy;
    }

    /**
     *
     */
    @Override
    public void randomInit() {
        Random r = new Random();
        for (int i = 0; i < genes.length; i++) {
            genes[i] = r.nextBoolean();
        }
    }

    public void setGene(int i, boolean b) {
        rangeCheck(i);
        genes[i] = b;
    }

    @Override
    public Boolean getGene(int i) {
        rangeCheck(i);
        return genes[i];
    }

    public Boolean[] getGenes() {
        return Arrays.copyOf(genes, genes.length);
    }

    /**
     * Creates "00101011110" type text representation.
     *
     * @return a String of genotype length
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(genes.length + 2);
        for (int i = 0; i < genes.length; i++) {
            if (genes[i])
                sb.append('1');
            else
                sb.append('0');
        }
        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
