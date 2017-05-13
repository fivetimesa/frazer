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

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class Genotype<T> {

    protected final int count;
    T[] genes;

    public Genotype(int count) {
        this.count = count;
    }
    
    public Genotype copy() {
        Genotype<T> copy = new Genotype<>(count);
        for(int i = 0; i < genes.length; i++)
            copy.setGene(i, getGene(i));
        return copy;
    }

    public void randomInit() {
    }

    public T getGene(int i) {
        rangeCheck(i);
        return genes[i];
    }
    
    public void setGene(int i, T value) {
        rangeCheck(i);
        genes[i] = value;
    }

    protected void rangeCheck(int i) {
        if (i < 0 || i > count - 1)
            throw new IndexOutOfBoundsException(
                    "Genes index: " + i + ", Size: " + count);
    }
    
    public int getGeneCount() {
        return count;
    }
}
