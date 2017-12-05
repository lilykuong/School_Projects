/**
 Name: Lily Kuong
 CS455, PA1
 Fall 2017
*/

import java.awt.*;
import javax.swing.*;
import java.util.Scanner;

/**
 Contains the main method. Prompts for the number of trials. Can only handle integer inputs and will produce an error if input is less than 1 and ask for another input.
 
 It creates the JFrame containing the CoinSimComponent and only depends on the CoinSimComponent.
 
 */

public class CoinSimViewer{
    
    private int WINDOW_WIDTH = 800;
    private int WINDOW_HEIGHT = 500;
    
    public static void main (String[] args){
        
        Scanner userInput = new Scanner(System.in);
        String input;

        //read input from user that is greater than 0
        //Assumed that input will be numeric
        int trial = 0;
        while (trial < 1){
            System.out.print("Enter number of trials: ");
            input = userInput.next();
            trial = Integer.parseInt(input);
            if (trial < 1)
                System.out.println("ERROR: Number entered must be greater than 0.");
        }
        
        //create the window
        CoinSimComponent panel = new CoinSimComponent();
        panel.trialValues(trial); //run simulation
        
        JFrame window = new JFrame("CoinSim"); // CoinSim is the title of the window
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); // makes the window size 800 by 500
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // program will close when window is closed
        window.getContentPane().add(panel); //adds the panel to the window
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}
