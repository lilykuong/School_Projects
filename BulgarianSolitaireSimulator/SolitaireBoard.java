import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 Creates a Solitaire Board specified by the user or is randomly generated.
 
 Contains methods to play one round of bulgarian solitaire, determine if it is
 the end of the game, print current board configuration, and check if the board is valid.
 
 Does not contain any I/O
 
 */

public class SolitaireBoard {
    
    public static final int NUM_FINAL_PILES = 9;
    // number of piles in a final configuration
    // (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)
    
    public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
    // bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
    // see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
    // the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES
    
    
    /**
     Representation invariants:
     
     - piles[] is a partially filled array
     - 0 <= numPiles < piles.length
     - maximum number of piles is CARD_TOTAL
     - piles cannot contain 0
     - CARD_TOTAL does not change
     - NUM_FINAL_PILES does not change during program execution
     - numPiles is the number of piles in Bulgarian solitaire
     - if numPiles > 0, then the elements are in piles indices [0, piles.length)
     
     */
    
    /**
     piles : create an array with the size of CARD_TOTAL for the boundary case of
     all piles being 1s. Don't need to resize the array
     */
    private final int[] piles = new int[CARD_TOTAL];
    
    //numPiles represents the elements in the partially filled array
    private int numPiles = 0;
    
    /**
     Creates a solitaire board with the configuration specified in piles.
     piles has the number of cards in the first pile, then the number of cards in the second pile, etc.
     PRE: piles contains a sequence of positive numbers that sum to SolitaireBoard.CARD_TOTAL
     */
    
    public SolitaireBoard(ArrayList<Integer> piles) {
        
        // copy all values from the ArrayList to a partially filled array
        for (int i = 0; i < piles.size(); i++){
            this.piles[i] = piles.get(i);
        }
        // save the number of elements filled
        numPiles = piles.size();
        
        //check if all represntation invariants are met
        assert isValidSolitaireBoard();
    }
    
    
    /**
     Creates a solitaire board with a random initial configuration.
     */
    public SolitaireBoard() {
        Random rand = new Random();
        int sum = 0;
        
        // run while the sum of the random integers is smaller than the card total.
        // add each random value to the array and increase the number of
        // elements in the array
        while (sum < CARD_TOTAL){
            //checks if the current element in the array is not already filled
            while (piles[numPiles] == 0)
                piles[numPiles] = rand.nextInt(CARD_TOTAL);
            
            sum += piles[numPiles];
            numPiles++;
        }
        
        // When the next random number causes the sum to be greater than the card total
        if (sum > CARD_TOTAL){
            // the last element in the array will be changed so the card total
            // will be the specified CARD_TOTAL
            piles[numPiles - 1] -= (sum - CARD_TOTAL);
        }
        
        //check if the board is valid
        assert isValidSolitaireBoard();
        
    }
    
    /**
     Plays one round of Bulgarian solitaire.
     Updates the configuration according to the rules of Bulgarian solitaire:
     Takes one card from each pile, and puts them all together in a new pile.
     The old piles that are left will be in the same relative order as before,
     and the new pile will be added to the end.
     */
    public void playRound() {
        int count = 0;
        boolean hasZeros = false;
        
        //remove one card from every pile
        for (int i = 0; i < numPiles; i++){
            count++;
            piles[i] -= 1;
            
            //checks the array for any zeros beforehand
            if (piles[i] == 0 && !hasZeros){
                hasZeros = true;
            }
        }
        
        // shift piles over if there is a zero in the pile and decreases the
        // amount of elements in the array
        if (hasZeros){
            for (int i = 0; i < numPiles; i++){
                while (piles[i] == 0 && i < numPiles){
                    for (int a = i; a < numPiles - 1; a++){
                        piles[a] = piles[a+1];
                    }
                    numPiles --;
                }
            }
        }
        
        // add the number of cards removed into the last pile in the array and
        // increase the number of elements in the array
        piles[numPiles] = count;
        numPiles++;
        
        //check if board is valid
        assert isValidSolitaireBoard();
    }
    
    /**
     Returns true iff the current board is at the end of the game.  That is, there are NUM_FINAL_PILES
     piles that are of sizes 1, 2, 3, . . . , NUM_FINAL_PILES, in any order.
     */
    
    public boolean isDone() {
        //check if valid board
        assert isValidSolitaireBoard();
        if (numPiles == NUM_FINAL_PILES){
            //create array checkDonewith size NUM_FINAL_PILES
            int[] checkDone = new int[NUM_FINAL_PILES];
            
            /**
             the value in piles[i] needs to be unique, not greater than NUM_FINAL_PILES
                 and no duplicates of the number
             each value is used as an index to the new array and their value is
                 the checkDone's value. Eg. piles[2] = 4, checkDone[4] = 4
             if the checkDone[piles[i]] equals to piles[i], there is a duplicate.
             */
            for (int i = 0 ; i < numPiles; i++){
                if ((piles[i] <= NUM_FINAL_PILES) && (checkDone[piles[i] - 1] != piles[i])){
                        checkDone[piles[i] - 1] = piles[i];
                }
                else
                    return false;
            }
            return true;
        }
        else
            return false;
    }
    
    
    /**
     Returns current board configuration as a string with the format of
     a space-separated list of numbers with no leading or trailing spaces.
     The numbers represent the number of cards in each non-empty pile.
     */
    public String configString() {
        
        String boardConfig = "";
        // adds each element in the array to the string with a space in between
        // condition is numPiles - 1 so last element does not print with a space afterwards
        
        for (int i = 0; i < numPiles - 1; i++){
            boardConfig += piles[i] + " ";
        }
        boardConfig += piles[numPiles - 1] + " ";
        
        //check if valid board still
        assert isValidSolitaireBoard();
        
        //return the current board configuration string
        return boardConfig;
    }
    
    
    /**
     Returns true iff the solitaire board data is in a valid state
     See above for represntation invariants
     */
    
    private boolean isValidSolitaireBoard() {
        
        /**
         The number of piles should be greater than or equal to zero but smaller than piles.length
         should be true
         */
        boolean containsPiles = numPiles >= 0 && numPiles < piles.length;
        
        /**
         The number of piles should be equal to the CARD_TOTAL.
         should be true
         */
        boolean pilesSizeEqualCardTotal = (piles.length == CARD_TOTAL);
        
        /**
         None of the elements in the partially filled array is equal to zero
         should be true
         */
        boolean pilesDoesntContainZeros = true;
        
        /**
         The card sum is equal to the card total
         should be true
         */
        boolean cardSumEqualCardTotal = true;
        
        
        if (containsPiles && pilesSizeEqualCardTotal){
            int cardSum = 0;
            for (int i = 0; i < numPiles; i++){
                cardSum += piles[i];
                //checks if array has any zeros
                if (piles [i] == 0)
                    pilesDoesntContainZeros = false;
            }
            //check if the card sum is equal to the card total
            if (cardSum != CARD_TOTAL){
                cardSumEqualCardTotal = false;
            }
        }
        
        //returns true if all true
        return containsPiles && pilesSizeEqualCardTotal && cardSumEqualCardTotal &&
        pilesDoesntContainZeros;
    }
}
