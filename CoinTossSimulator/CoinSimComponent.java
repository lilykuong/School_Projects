/**
 Name: Lily Kuong
 CS455 PA1
 Fall 2017
 */


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.*;
import javax.swing.*;

/**
 CoinSimComponent class is an extension of JComponent. It calls CoinTossSimulator and receive the outcome values. It overrides paintComponent to draw the bar graph using Bar objects for each bar in the graph. The parameters are defined here for each bar and passed to the Bar class. It is dependent on CoinTossSimulator and Bar class.
 
 */

public class CoinSimComponent extends JComponent {
    
    /**
     @param bottom      location of the bottom of the label
     @param left        location of the left side of the bar
     
     @param width       width of the bar (in pixels)
     @param Height      height of the bar in application units
     
     @param scale       how many pixels per application unit; how tall in pixels is one trial on the bar
     @param color       the color of the bar
     @param label       the label at the bottom of the bar
     
     @param heads       number of Two-Head outcomes
     @param headTails   number of One head and one tail outcomes
     @param tails       number of Two-Tail outcomes
     @param trials      number of trials
     @param percent     percentage of specific outcome occured out of the trials
     
     */
    
    private static int left;
    private static int bottom;
    private static int BAR_WIDTH = 75; //bar width fixed
    private static int height;
    
    private static int windowWidth;
    private static int windowHeight;
    
    private static double scale;
    private static Color color;
    private static String label;
    
    private int heads;
    private int headTails;
    private int tails;
    private int trials;
    private int percent;
    
    //get outcome values
    public void trialValues(int trial){
        CoinTossSimulator test = new CoinTossSimulator();
        test.run(trial);
        heads = test.getTwoHeads();
        headTails = test.getHeadTails();
        tails = test.getTwoTails();
        trials = test.getNumTrials();
    }



    //draw the bars
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //need to match the graphics in Bar
        Graphics2D g2 = (Graphics2D) g;
        
        //get size of window
        windowWidth = getWidth();
        windowHeight = getHeight();
        left = (int) ((windowWidth - BAR_WIDTH * 3)/4);
        bottom = windowHeight - 40;
        
        //scale for how tall in pixels is one trial on the bar
        scale = ((double)(bottom - 20)/(double)trials);
        
        //First bar
        color = Color.RED;
        percent = (int)Math.round(((double) heads/trials * 100));
        label = "Two Heads: " + heads + " (" + percent + "%)";
        height = heads;
        Bar bar1 = new Bar(bottom, left, BAR_WIDTH, height, scale, color, label);
        bar1.draw(g2);
        
        //Second bar
        color = Color.GREEN;
        percent = (int)Math.round(((double) headTails/trials * 100));
        label = "A Head and a Tail: " + headTails + " (" + percent + "%)";
        int left2 = left * 2 + BAR_WIDTH; //left coordinate for the second bar
        height = headTails;
        Bar bar2 = new Bar(bottom, left2, BAR_WIDTH, height, scale, color, label);
        bar2.draw(g2);
        
        //Third bar
        color = Color.BLUE;
        percent = (int)Math.round(((double) tails/trials * 100));
        label = "Two Tails: " + tails + " (" + percent+ "%)";
        int left3 = left + left2 + BAR_WIDTH; //left coordinate for the third bar
        height = tails;
        Bar bar3 = new Bar(bottom, left3, BAR_WIDTH, height, scale, color, label);
        bar3.draw(g2);
    }
}
