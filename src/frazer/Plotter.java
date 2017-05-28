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
        String[] args = {"--location=0,0", "EvolutionHistory", "700", "350"};
        plotterPApplet = new PlotterPApplet();
        PApplet.runSketch(args, plotterPApplet);
        plotterPApplet.redraw();
        
        this.evolutionHistory = evolutionHistory;
    }
    
    private class PlotterPApplet extends PApplet {
        //dimensions
        private int plotX;
        private int plotY;
        private int plotWidth;
        private int plotHeight;
        private int marginHorizontal;
        private int marginVertical;
        private int textFieldWidth;
        private int textFieldHeight;
        private int textFieldX;
        private int textFieldY;
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
            marginHorizontal = 50;
            marginVertical = 50;
            
            textFieldWidth = 150;
            textFieldHeight = height - 2 * marginVertical;
            
            plotWidth = width - 3 * marginHorizontal - textFieldWidth;
            plotHeight = height - 2 * marginVertical;
            
            plotX = marginHorizontal;
            plotY = marginVertical;
            
            textFieldX = 2 * marginHorizontal + plotWidth;
            textFieldY = 2 * marginVertical + titleHeight;
            
            plotLabelWidth = 30;
            plotLabelHeight = 15;
        }
        
        private void setColorScheme() {
            colorBackground = color(0);
            colorGridPrimary = color(126, 138, 162);
            colorGridSecondary = color(38, 50, 72);
            colorPrimary = color(255);
            colorSecondary = color(255, 192, 0);
        }
        
        
        @Override
        public void draw() {
            background(colorBackground);
            drawPlot();
            drawText();
        }
        
        
        private void drawPlot() {
            //update plot data
            numberOfGenerations = evolutionHistory.getHistoryLength();
            if(numberOfGenerations < 50)
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
            /*
            for(int i = 1; i < numberOfGenerations; i++) {
                float x = mapItoX(i);
                float maxV = mapVtoY(evolutionHistory.getMaxScore(i));
                float minV = mapVtoY(evolutionHistory.getMinScore(i));
                
                stroke(colorSecondary);
                line(x, maxV, x, minV);
            
            for(int i = 2; i < numberOfGenerations; i++) {
                float x1 = mapItoX(i - 1);
                float x2 = mapItoX(i);
                float v1 = mapVtoY(evolutionHistory.getAverage(i - 1));
                float v2 = mapVtoY(evolutionHistory.getAverage(i));
                stroke(colorPrimary);
                
                line(x1, v1, x2, v2);
            }
            }*/
            
            float nextMaxValue = 0;
            pushMatrix();
            stroke(colorPrimary);
            fill(colorSecondary);
            int offset = 0;
            if(numberOfGenerations > 250) offset = numberOfGenerations - 250;
            beginShape();
            for(int i = 5 + offset; i < numberOfGenerations - 5; i++) {
                float sumMaxV = 0;
                for(int k = i - 5; k < i + 5; k++) 
                    sumMaxV += evolutionHistory.getMaxScore(k);
                sumMaxV /= 10;
                if(sumMaxV > nextMaxValue) 
                    nextMaxValue = sumMaxV;
                float x = mapItoX(i, offset);
                point(x, mapVtoY(evolutionHistory.getAverage(i)));
                vertex(x, mapVtoY(sumMaxV));
                //vertex(x, mapVtoY(v));
            }
            
            for(int i = numberOfGenerations - 6; i > 5 + offset; i--) {
                float sumMinV = 0;
                //for(int k = i - 5; k < i + 5; k++) 
                //    sumMinV += mapVtoY(evolutionHistory.getMinScore(k));
                //sumMinV /= 10;
                float v = evolutionHistory.getMinScore(i);
                float x = mapItoX(i, offset);
                vertex(x, mapVtoY(v));
            }
            endShape();
            popMatrix();
            //plot
            
            //secondary grid + labels
            pushMatrix();
            int plotGridValue = (int)(maxValue / 100) * 10;
            //while(plotGridValue < maxValue / 15)
            //    plotGridValue *= 10;
            float plotGridY = mapVtoY(plotGridValue);
            fill(colorGridPrimary);
            stroke(colorGridSecondary);
            for(int v = 0; v < maxValue - plotGridValue; v += plotGridValue) {
                text(v, 3, mapVtoY(v) - 3);
                if(v != 0)
                    line(-5, mapVtoY(v), plotWidth, mapVtoY(v));
            }
            
            translate(0, plotHeight);
            for(int i = offset + (50 - offset % 50); i < numberOfGenerations; i += 50) {
                text(i, mapItoX(i, offset) + 3, plotLabelHeight - 3);
                if(i != offset)
                    line(mapItoX(i, offset), 5, mapItoX(i, offset), -plotHeight);
            }
            
            popMatrix();
            
            popMatrix();
            
            //update max value
            maxValue = (maxValue * 0.9f + (nextMaxValue + plotGridValue * 2) * 0.1f);
        }
        
        void drawText() {
            pushMatrix();
            int textLineHeight = 20;
            translate(textFieldX, textFieldY);
            fill(colorPrimary);
            text("Generation: " + numberOfGenerations, 0, 0);
            translate(0, textLineHeight);
            translate(0, textLineHeight);
            text("Population size: " + evolutionHistory.getCount(numberOfGenerations - 1), 0, 0);
            translate(0, textLineHeight);
            text("Best fitness: " + evolutionHistory.getMinScore(numberOfGenerations - 1), 0, 0);
            translate(0, textLineHeight);
            text("Average fitness: " + evolutionHistory.getAverage(numberOfGenerations - 1), 0, 0);
            popMatrix();
        }
        
        private float mapItoX(int i, int offset) { 
            int generationsWidth = 250;
            if(numberOfGenerations > 250) generationsWidth = numberOfGenerations;
            return map(i, offset, generationsWidth, plotLabelWidth, plotWidth);
        }
        
        private float mapVtoY(float v) {
            return map(v, 0, maxValue, plotHeight, 0);
        }
    }
}
