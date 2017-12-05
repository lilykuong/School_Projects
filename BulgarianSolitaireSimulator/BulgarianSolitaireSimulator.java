// Name: Lily Kuong
// USC NetID: 8277174492
// CSCI455 PA2
// Fall 2017

import java.util.ArrayList;
import java.util.Scanner;

/**
 Create a simulation game of Bulgarian Solitaire.
 
 The game starts with 45 cards. The cards are either randomly divided into
 some number of piles or selected specically by the player. In each round,
 you take one card from each pile, forming a new pile with these cards.
 For example, the starting configuration is 20, 5, 1, 9, and 10. The first
 round will result into 19, 4, 8, 9, and 5. The game is over when the cards
 consists of size 1, 2, 3 ..., NUM_FINAL_PILES in some order.
 
 
 In the BulgarianSolitarieSimulator, the program interacts with the user and
 requests for inputs if the user selects to do so. Since there is I/O, error
 checking ensures that the user did not input any characters besides integers
 greater than zero. The simulation will begin when a valid input is provided
 
 Example of an error produced is:
 Please enter a space-separated list of positive integers followed by newline
 44 s z 1
 ERROR: Each pile must have at least one card and the total number of cards must be 45
 Please enter a space-separated list of positive integers followed by newline
 22 1 0 -4
 ERROR: Each pile must have at least one card and the total number of cards must be 45
 Please enter a space-separated list of positive integers followed by newline
 
 
 
 The program can be ran in four ways:
 
 1) generates a random configuration of a valid board and automatically simulates ]
 the whole game
     TO RUN: java -ea BulgarianSolitaireSimulator.java
 
 2) the user inputs what configuration they want and automatically
     TO RUN: java -ea BulgarianSolitaireSimulator.java -u
         -u prompts for the initial configuration from the user, instead of
         generating a random configuration.
 
 3) the user can step through each round of the game and the board is randomly generated
     TO RUN: java -ea BulgarianSolitaireSimulator.java -s
         -s causes the game to stop between every round of the game. The game only
         continues when the user hits enter
 
 4) the user can step through each round of the game and the user inputs the
 configuration of the board
     TO RUN: java -ea BulgarianSolitaireSimulator.java -u -s
         -u -s prompts for the inital configuration and stops between every
         round of the game.
 
 
 Example output when no tag is provided:
 Initial config: 31 13 1
 [1] Current configuration: 30 12 3
 [2] Current configuration: 29 11 2 3
 ...
 [40] Current configuration: 2 3 4 5 6 7 8 10
 [41] Current configuration: 1 2 3 4 5 6 7 9 8
 Done!
 
 Example output when -u is provided:
 Please enter a space-separated list of positive integers followed by newline
 45
 Initial config: 45
 [1] Current configuration: 44 1
 [2] Current configuration: 43 2
 ...
 [35] Current configuration: 10 2 3 4 5 6 7 8
 [36] Current configuration: 9 1 2 3 4 5 6 7 8
 Done!
 
 Example output when -s is provided:
 Initial config: 35 10
 [1] Current configuration: 34 9 2
 <Type return to continue>
 [2] Current configuration: 33 8 1 3
 <Type return to continue>
 ...
 [45] Current configuration: 1 2 3 4 5 6 7 9 8
 <Type return to continue>
 Done!
 
 Example output when -u -s is provided:
 Number of total cards is 45
 You will be entering the initial configuration of the cards (i.e., how many in each pile).
 Please enter a space-separated list of positive integers followed by newline
 45
 Initial config: 45
 [1] Current configuration: 44 1
 <Type return to continue>
 [2] Current configuration: 43 2
 <Type return to continue>
 ...
 [36] Current configuration: 9 1 2 3 4 5 6 7 8
 <Type return to continue>
 Done!
 
 */

public class BulgarianSolitaireSimulator {
    
    public static void main(String[] args) {
        
        // singleStep = check if user wants to stop between each round of the game
        // userConfig = check is user wants to input board configuration
        boolean singleStep = false;
        boolean userConfig = false;
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-u")) {
                userConfig = true;
            }
            else if (args[i].equals("-s")) {
                singleStep = true;
            }
        }
        /**
        Scanner declared in main because used when asking for the user
         configuration and when the simulation stops in between rounds
         and waits for user to move to the next step
         */
        Scanner scan = new Scanner (System.in);
        SolitaireBoard bulgarian;
        
        // create a valid SolitaireBoard based on the tags the user provided
        if (userConfig)
            bulgarian = userInput(scan);
        
        else
            bulgarian = new SolitaireBoard();
        
        //begin simulation
        playBulgarian(bulgarian, singleStep, scan);
        
    }
    
    /**
     Prints out the current configuration of the board
     */
    private static void play (SolitaireBoard bulgarian, int round){
        bulgarian.playRound();
        System.out.print("[" + round + "]");
        System.out.print(" Current configuration: ");
        System.out.println(bulgarian.configString());
    }
    
    /**
     Prints out error message and reads in the next board config input
     
     Returns the new board config input
     
     @ param Scanner scan : object created in main for system input commands
     */
    private static String printErrorInput(Scanner scan){
        System.out.println("ERROR: Each pile must have at least one card and the " +
                           "total number of cards must be " + SolitaireBoard.CARD_TOTAL);
        System.out.println("Please enter a space-separated list of positive integers " +
                           "followed by newline:");
        
        //get new line and remove any whitespace in the beginning and the end of input
        return scan.nextLine().trim();
    }
    
    /**
     Checks the user input for any characters that are not numerics
     
     Returns a valid user input that contains only numerics
     
     @ param String line represents the user input
     @ param Scanner scan represents the obeject created in main for system input commands
     */
    private static String checkForNonNumerics(String line, Scanner scan){
        
        //replace all characters that are not numerics with a space
        String numericInput = line.replaceAll("[^0-9]+", " ");
        
        //if the original and modified input are not the same, then it contains a non numeric
        //print error and get another input until valid if false
        while (!line.equals(numericInput)){
            line = printErrorInput(scan);
            numericInput = line.replaceAll("[^0-9]+", " ");
        }
        return line;
    }
    
    /**
     Check if the total amount of cards provided by the user is equal to CARD_TOTAL
     specified in SolitaireBoard.java
     ArrayList used because it is uncertain how many piles user wants to have
     
     Returns an Integer ArrayList with the number of cards in each pile specified by the user
     
     @ param String line represents the initial user input that has been checked if
         it only contains integer
     @ param Scanner scan represents the scanner declare in main for system input commands
     */
    
    private static ArrayList<Integer> checkCardTotal(String line, Scanner scan){
        
        ArrayList<Integer> intArray = new ArrayList <Integer>();
        
        String[] ints;
        int cardSum = 0;
        boolean hasZeros = false;
        
        while (cardSum != SolitaireBoard.CARD_TOTAL){
            
            // split the input into each individual number
            ints = line.split (" ");
            
            // add all the numbers to the integer Array, count the cards, and check if it has zeros if the string array isn't empty
            if (!(ints.length == 0)){
                for (int i = 0; i < ints.length; i++){
                    if (!ints[i].equals("")){
                        intArray.add(Integer.parseInt(ints[i]));
                        cardSum += intArray.get(i);
                        if (intArray.get(i) == 0)
                            hasZeros = true;
                    }
                }
                
                //if wrong number of cards entered or a pile contains zeros
                if ((cardSum != SolitaireBoard.CARD_TOTAL) || (hasZeros)){
                    
                    //Display error
                    line = printErrorInput(scan);
                    
                    //Check the new line for any characters besides numerics
                    line = checkForNonNumerics(line, scan);
                    
                    //set variables back to original variables and clear the integer Array
                    cardSum = 0;
                    hasZeros = false;
                    intArray.clear();
                }
            }
        }
        return intArray;
    }
    
    /**
     Prints out information if user wants to input board Configuration
     
     Returns the SolitaireBoard created
     
     @param Scanner scan represents the scanner declare in main for system input commands
     */
    private static SolitaireBoard userInput(Scanner scan){

        System.out.println("Number of total cards is " + SolitaireBoard.CARD_TOTAL);
        System.out.println("You will be entering the initial configuration of the cards (i.e., how many in each pile).");
        System.out.println("Please enter a space-separated list of positive integers followed by newline:");
        
        //remove whitespace from the beginning
        String line = scan.nextLine().trim();
        line = checkForNonNumerics(line, scan);
        
        return new SolitaireBoard(checkCardTotal(line, scan));
    }
    
    
    /**
     Simulates the whole game of Bulgarian solitaire
     
     @param SolitaireBoard bulgarian represents a valid configuration of the
         board based on program representation invariants
     @param boolean singleStep represents if the user want to stop between each round
     @param Scanner scan represents the scanner declare in main for system input commands
     
     */
    private static void playBulgarian(SolitaireBoard bulgarian, boolean singleStep, Scanner scan){
        
        //print out the initial configuration of the board
        System.out.println("Initial configuration: " + bulgarian.configString());
        
        int round = 1;
        
        //run until piles are ordered consecutively
        while (!bulgarian.isDone()){
            
            //runs one round of bulgarian solitaire
            play(bulgarian,round);
            
            //increase number of rounds
            round ++;
            
            //stop if user wants to stop every round
            singleStepRound(singleStep,scan);
        }
        
        // print when game is over
        System.out.println ("Done!");
    }
    
    /**
     Stops every round if user specified when running the program. Program will continue when user hits return.
     
     @param boolean singleStep represents if the user want to stop between each round
     @param Scanner scan represents the scanner declare in main for system input commands
     */
    private static void singleStepRound(boolean singleStep, Scanner scan){
        if (singleStep){
            System.out.print("<Type return to continue>");
            scan.nextLine();
        }
    }
}
