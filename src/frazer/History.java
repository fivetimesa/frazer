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

import java.util.ArrayList;

/**
 *
 * @author Teodor Michalski, Maciek Bajor, Paweł Sikorski
 */
public class History {
    ArrayList<HistoryRecord> record;
    
    private float maxFitnessScore;
    private float minFitnessScore;

    public History() {
        record = new ArrayList<>();
        maxFitnessScore = 0;
        minFitnessScore = Float.MAX_VALUE;
    }
    
    public void recordPopulation(Population population) {
        HistoryRecord newRecord = new HistoryRecord(population);
        record.add(newRecord);
        if(newRecord.getMaxScore() > maxFitnessScore)
            maxFitnessScore = newRecord.getMaxScore();
        if(newRecord.getMinScore() < minFitnessScore)
            minFitnessScore = newRecord.getMinScore();
    }
    
    public int getHistoryLength() {
        return record.size();
    }
    
    /**
     * @param i generation
     * @return the count
     */
    public int getCount(int i) {
        return record.get(i).getCount();
    }

    /**
     * @param i generation
     * @return the maxScore
     */
    public float getMaxScore(int i) {
        return record.get(i).getMaxScore();
    }

    /**
     * @param i generation
     * @return the minScore
     */
    public float getMinScore(int i) {
        return record.get(i).getMinScore();
    }

    /**
     * @param i generation
     * @return the scoreSum
     */
    public double getScoreSum(int i) {
        return record.get(i).getScoreSum();
    }
    
    /**
     * @param i generation
     * @return the average score
     */
    public float getAverage(int i) {
        return record.get(i).getAverage();
    }
    
    /**
     * @return the maxFitnessScore
     */
    public float getMaxFitnessScore() {
        return maxFitnessScore;
    }

    /**
     * @return the minFitnessScore
     */
    public float getMinFitnessScore() {
        return minFitnessScore;
    }
    
    private class HistoryRecord {
        private final int count;
        private final float maxScore;
        private final float minScore;
        private final double scoreSum;
        private final float average;

        public HistoryRecord(Population population) {
            this.count = population.getCount();
            this.maxScore = population.getMaxScore();
            this.minScore = population.getMinScore();
            this.scoreSum = population.getScoreSum();
            this.average = (float) scoreSum / count;
        }

        /**
         * @return the count
         */
        public int getCount() {
            return count;
        }

        /**
         * @return the maxScore
         */
        public float getMaxScore() {
            return maxScore;
        }

        /**
         * @return the minScore
         */
        public float getMinScore() {
            return minScore;
        }

        /**
         * @return the scoreSum
         */
        public double getScoreSum() {
            return scoreSum;
        }

        /**
         * @return the average
         */
        public float getAverage() {
            return average;
        }
        
        
    }

}
