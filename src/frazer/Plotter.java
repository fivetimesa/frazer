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

import processing.core.PApplet;

/**
 *
 */
public class Plotter {
    private final PlotterPApplet plotterPApplet;
    private final History evolutionHistory;

    public Plotter(History evolutionHistory) {
        String[] args = {"--location=0,0", "EvolutionHistory", "500", "250"};
        plotterPApplet = new PlotterPApplet();
        PApplet.runSketch(args, plotterPApplet);
        
        this.evolutionHistory = evolutionHistory;
    }
    
    private class PlotterPApplet extends PApplet {
        //dimensions
        private int plotX;
        private int plotY;
        private int plotWidth;
        private int plotHeight;
        private int marginLeft;
        private int marginTop;
        private int textFieldWidth;
        private int textFieldHeight;
        private int titleHeight;
        private int plotLabelWidth;
        private int plotLabelHeight;
        
        //color scheme
        private int colorBackground;
        private int colorGridPrimary;
        private int colorGridSecondary;
        private int colorPrimary;
        private int colorSecondary;
        
        //plot data
        private int numberOfGenerations;
        private float maxValue;
        private float minValue;
        
        
        @Override
        public void settings() {
            int w = 400;
            int h = 300;
            if (args != null && args.length >= 2) {
                w = parseInt(args[0]);
                h = parseInt(args[1]);
            }
            size(w, h);
            setColorScheme();
            setDimensions();
        }
        
        private void setDimensions() {
            marginLeft = 20;
            marginTop = 20;
            
            plotWidth = width - 2 * marginLeft;
            plotHeight = height - 2 * marginTop;
            
            plotX = marginLeft;
            plotY = marginTop;
        }
        
        private void setColorScheme() {
            colorBackground = color(25, 40, 85);
            colorGridPrimary = color(143, 130, 182);
            colorGridSecondary = color(55, 28, 131);
            colorPrimary = color(131, 77, 28);
            colorSecondary = color(93, 77, 52);
        }
        
        
        @Override
        public void draw() {
            background(colorBackground);
            drawPlotGrid();
        }
        
        
        private void drawPlotGrid() {
            //update plot data
            numberOfGenerations = evolutionHistory.getHistoryLength();
            maxValue = evolutionHistory.getMaxFitnessScore();
            minValue = evolutionHistory.getMinFitnessScore();
            
            pushMatrix();
            translate(plotX, plotY);
            
            //primary lines
            strokeWeight(1);
            stroke(colorGridPrimary);
            
            line(0, 0, 0, plotHeight);
            line(0, plotHeight, plotWidth, plotHeight);
            //primary lines
            
            //plot
            for(int i = 0; i < numberOfGenerations; i++) {
                float x = mapItoX(i);
                float maxV = mapVtoY(evolutionHistory.getMaxScore(i));
                float minV = mapVtoY(evolutionHistory.getMinScore(i));
                
                stroke(colorSecondary);
                line(x, maxV, x, minV);
            }
            for(int i = 1; i < numberOfGenerations; i++) {
                float x1 = mapItoX(i - 1);
                float x2 = mapItoX(i);
                float v1 = mapVtoY(evolutionHistory.getAverage(i - 1));
                float v2 = mapVtoY(evolutionHistory.getAverage(i));
                stroke(colorPrimary);
                
                line(x1, v1, x2, v2);
            }
            //plot
            
            popMatrix();
        }
        
        private float mapItoX(int i) { 
            int generationsWidth = 250;
            if(numberOfGenerations > 250) generationsWidth = numberOfGenerations;
            return map(i, 0, generationsWidth, 0, plotWidth);
        }
        
        private float mapVtoY(float v) {
            return map(v, minValue, maxValue, plotHeight, 0);
        }
    }
}
