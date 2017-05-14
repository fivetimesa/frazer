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
public class IntegerGenotype extends Genotype<Integer> {

    protected int[] genes;

    public IntegerGenotype(int count) {
        super(count);
        genes = new int[count];
    }

    public void randomInit(int min, int max) {
        Random generator = new Random();
        for (int i = 0; i < genes.length; i++) {
            genes[i] = min + generator.nextInt() * (max - min);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(this.genes);
    }

}
