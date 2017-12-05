// Name: Lily Kuong
// CS 455 PA1
// Fall 2017

/**
 * class CoinTossSimulator
 * 
 * Simulates trials of tossing two coins and allows the user to access the
 * cumulative results.
 * 
 * NOTE: we have provided the public interface for this class.  Do not change
 * the public interface.  You can add private instance variables, constants, 
 * and private methods to the class.  You will also be completing the 
 * implementation of the methods given. 
 * 
 * Invariant: getNumTrials() = getTwoHeads() + getTwoTails() + getHeadTails()
 * 
 */

import java.util.Random;

public class CoinTossSimulator {
    private int twoHeads;
    private int twoTails;
    private int HeadTails;

    private Random rand = new Random();
    
    private int TWO_HEADS = 0;
    private int TWO_TAILS = 0;
    
   /**
      Creates a coin toss simulator with no trials done yet.
   */
   public CoinTossSimulator() {
       twoHeads = 0;
       twoTails = 0;
       HeadTails = 0;
   }


   /**
      Runs the simulation for numTrials more trials. Multiple calls to this method
      without a reset() between them *add* these trials to the current simulation.
      
      @param numTrials  number of trials to for simulation; must be >= 1
    */
   public void run(int numTrials) {
       if (numTrials < 1){
           System.out.println("Number of trials for simulation must be > 0");
           System.exit(0);
       }
       
       for (int i = 0; i < numTrials; i++){
           //give random number from 0 to 3
           //if random number equal to 0, received 2 Heads
           //if random number equal to 1 or 2, received Head Tail
           //if random number equal to 3, received 2 Tails
           int outcome = rand.nextInt(4);
           
           if (outcome == TWO_HEADS)
               twoHeads ++;
           else if (outcome == TWO_TAILS)
               twoTails ++;
           else
               HeadTails ++;
       }
   }


   /**
      Get number of trials performed since last reset.
   */
   public int getNumTrials() {
       return getTwoHeads() + getTwoTails() + getHeadTails();
   }


   /**
      Get number of trials that came up two heads since last reset.
   */
   public int getTwoHeads() {
       return twoHeads;
   }


   /**
     Get number of trials that came up two tails since last reset.
   */  
   public int getTwoTails() {
       return twoTails;
   }


   /**
     Get number of trials that came up one head and one tail since last reset.
   */
   public int getHeadTails() {
       return HeadTails;
   }


   /**
      Resets the simulation, so that subsequent runs start from 0 trials done.
    */
   public void reset() {
       twoHeads = 0;
       twoTails = 0;
       HeadTails = 0;
   }

}
