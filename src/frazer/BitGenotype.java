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

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class BitGenotype extends Genotype<Boolean> {

    boolean[] genes;

    public BitGenotype(int count) {
        super(count);
        genes = new boolean[count];
        this.randomInit();
    }

    /**
     *
     */
    @Override
    public void randomInit() {
        Random r = new Random();
        for (int i = 0; i < count; i++) {
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

    public boolean[] getGenes() {
        return Arrays.copyOf(genes, genes.length);
    }
    
    /**
     * Creates "00101011110" type text representation.
     * @return a String of <code>count</code> size 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(count + 2);
        for (int i = 0; i < count; i++) {
            if (genes[i])
                sb.append('1');
            else
                sb.append('0');
        }
        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
