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

import frazer.Population;
import frazer.Specimen;
import frazer.interfaces.Mating;
import java.util.Random;

/**
 *
 */
public class TournamentMating implements Mating {

    private boolean minimise;
    private int parentsCount;
    private int candidateCount;

    public TournamentMating(boolean minimise) {
        this.candidateCount = 3;
        this.parentsCount = 2;
        this.minimise = minimise;
    }

    public TournamentMating(boolean minimise, int parentsCount, int candidateCount) {
        this.candidateCount = 3;
        this.parentsCount = 2;
        this.minimise = minimise;
        this.parentsCount = parentsCount;
        this.candidateCount = candidateCount;
    }

    public int getParentsCount() {
        return parentsCount;
    }

    public int getCandidateCount() {
        return candidateCount;
    }

    public void setCandidateCount(int candidateCount) {
        this.candidateCount = candidateCount;
    }

    public void setParentsCount(int parentsCount) {
        this.parentsCount = parentsCount;
    }

    @Override
    public Specimen[] selectParents(Population population) {
        Specimen[] specimens = population.getSpecimens();
        Specimen[] parents = new Specimen[parentsCount];
        Random random = new Random();
        for (int i = 0; i < parentsCount; i++) {
            Specimen candidate = specimens[random.nextInt(specimens.length)];
            float candidateScore = candidate.getFitnessScore();
            for (int j = 1; j < candidateCount; j++) {
                Specimen newCandidate = specimens[random.nextInt(specimens.length)];
                float newCandidateScore = candidate.getFitnessScore();
                if ((minimise && newCandidateScore < candidateScore) || (!minimise && newCandidateScore < candidateScore)) {
                    candidate = newCandidate;
                    candidateScore = newCandidateScore;
                }
            }
            parents[i] = candidate;
        }
        return parents;
    }
    
}
