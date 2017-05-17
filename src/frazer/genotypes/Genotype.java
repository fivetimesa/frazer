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
import java.util.Iterator;

import java.lang.reflect.Array;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 * @param <T>
 */
public class Genotype<T> implements Iterable {

    T[] genes;

    public Genotype(Class<T> c, int count) {
        @SuppressWarnings("unchecked")
        final T[] genericGenes = (T[]) Array.newInstance(c, count);
        this.genes = genericGenes;
    }
    
    public Genotype copy() {
        Class c;
        c = genes.getClass().getComponentType();
        Genotype<T> copy = new Genotype<T>(c, genes.length);
        
        for(int i = 0; i < genes.length; i++)
        {
            copy.setGene(i, getGene(i));
        }    
        return copy;
//            copy.setGene(i, getGene(i));
        
//        Genotype<T> copy = new Genotype<>(genes.length);
//        for(int i = 0; i < genes.length; i++)
//            copy.setGene(i, getGene(i));
//        return copy;
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
        if (i < 0 || i > genes.length - 1)
            throw new IndexOutOfBoundsException(
                    "Genes index: " + i + ", Size: " + genes.length);
    }
    
    public int getGeneCount() {
        return genes.length;
    }

    @Override
    public Iterator iterator() {
        return new GeneIterator<>();
    }
    
    private class GeneIterator <T> implements Iterator<T> {
        int cursor;
        
        public GeneIterator() {
            cursor = 0;
        }
        @Override
        public boolean hasNext() {
            if(cursor < genes.length)
                return true;
            else return false;
        }

        @Override
        public T next() {
            cursor++;
            return (T) genes[cursor-1];
        }
    }
}
