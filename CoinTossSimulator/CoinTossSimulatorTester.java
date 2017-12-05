/**
Name: Lily Kuong
CS455, PA1
Fall 2017
*/


/**
Contains the main method to test CoinTossSimulator independently of CoinSimViewer

The program provides the parameters (number of trials or reset) to CoinTossSimulator and prints out the results for the number of two-heads, two-tails, or one head and one tail toss and checs if the sum of the three is equal to the expected value for the number of trials done.
 
 If the number of trials is negative or a 0, the program will print an error and exit the program.
 
 If reset is called, it will set all the tosses back to zero and the number of trials back to zero. The number of trials will added up until reset is called for this specific main function.
 
 Ex:
 CoinTossSimulator test = new CoinTossSimulator(); // initializes the coin toss simulator
 test.run(1); // number of Trials = 1
 test.run(3); // number of Trials = 4
 test.reset(); // number of Trials = 0
 test.reset(2); //number of Trials = 2
 
 An example print outs would look like this for the above
 
 //For initializing the coin toss simulator
 Number of trials [exp 0]: 0
 Two-head tosses: 0
 Two-tail tosses: 0
 One-head one-tail tosses: 0
 Tosses added up correctly? true
 
 //after run X amount of times
 Number of trials [exp X] = X
 Two-head tosses: A                     //A is an arbitary number
 Two-tail tosses: B                     //B is an arbitary number
 One-head one-tail tosses: C            //C is an arbitary number
 Tosses added up correctly? true        //A+B+C == X
 
 reset print out will be the same as initializing the coin toss simulator
 
*/


public class CoinTossSimulatorTester {
    
    /**
     Print statement for the main functions
     */
    public static void printOut (CoinTossSimulator test, int expected){
        System.out.println("Number of trials [exp " + expected + "]: " + test.getNumTrials());
        System.out.println("Two-head tosses: " + test.getTwoHeads());
        System.out.println("Two-tail tosses: " + test.getTwoTails());
        System.out.println("One-head one-tail tosses: " + test.getHeadTails());
        System.out.println("Tosses add up correctly? " + (test.getNumTrials() == expected));
        System.out.println();
    }
    
    
    public static void main (String[] args){
        
        //initialize CoinTossSimulator
        int trial = 0;
        int expected = 0;
        System.out.println("After constructor:");
        CoinTossSimulator test = new CoinTossSimulator();
        printOut(test, expected);
        
        //run CoinToss once
        trial = 1;
        expected = expected + trial;
        System.out.println("After run (" + trial + "):");
        test.run(trial);
        printOut(test, expected);
        
        //run CoinToss 10 times
        trial = 10;
        expected = expected + trial;
        System.out.println("After run (" + trial + "):");
        test.run(trial);
        printOut(test, expected);
        
        //run CoinToss 100 times
        trial = 100;
        expected = expected + trial;
        System.out.println("After run (" + trial + "):");
        test.run(trial);
        printOut(test, expected);
        
        //reset CoinToss
        test.reset();
        trial = 0;
        expected = 0;
        System.out.println("After reset:");
        printOut(test, expected);
        
        //run CoinToss 1000 times
        trial = 1000;
        expected = expected + trial;
        System.out.println("After run (" + trial + "):");
        test.run(trial);
        printOut(test, expected);
        
        
        //reset CoinToss
        test.reset();
        trial = 0;
        expected = 0;
        System.out.println("After reset:");
        printOut(test, expected);
        
//        //run CoinToss -1 times
//        trial = -1;
//        expected = expected + trial;
//        System.out.println("After run (" + trial + "):");
//        test.run(trial);
//        printOut(test, expected);
        
        //run CoinToss 1000 times
        trial = 2147483647;
        expected = expected + trial;
        System.out.println("After run (" + trial + "):");
        test.run(trial);
        printOut(test, expected);
        
//        //run CoinToss 0 times
//        trial = 0;
//        expected = expected + trial;
//        System.out.println("After run (" + trial + "):");
//        test.run(trial);
//        printOut(test, expected);
        
        //run CoinToss 1000 times
        trial = 1;
        expected = expected + trial;
        System.out.println("After run (" + trial + "):");
        test.run(trial);
        printOut(test, expected);
        
    }
    
}
