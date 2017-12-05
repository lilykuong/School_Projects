// Name: Lily Kuong
// USC NetID: lkuong
// CS 455 PA4
// Fall 2017

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;


/**
 * This program will display all such words, with the corresponding Scrabble score for each word, in decreasing
 * order by score. For words with the same scrabble score, the words appear in alphabetical order. The program will
 * loop until the user specify to exit by typing "." in the prompt.
 *
 * The program is able to take in any character but will disregard those that are not alpha letters. The program works
 * on lower and upper case versions of dictionaries, but all processing will be case-sensitive. Ex: if the dictionary
 * only has upper case, a rack of "MALL" would work but "mall" will not be able to find any words.
 *
 * There are two ways to compile this code:
 * 1) java WordFinder
 *      This will automatically use the sowpods dictionary.
 * 2) java WordFinder [dictionaryFile]
 *      This will use the dictionary file specified to find words.
 * If the file specified (either explicitly or default one does not exist, the program will print an error message
 * that consists of the file name and exit the program.
 *
 * Example output using sowpods dictionary for input "cmal":
 * Type . to quit.
 * Rack? cmal
 * We can make 11 words from "aclm"
 * All of the words with their scores (sorted by score):
 * 8: calm
 * 8: clam
 * 7: cam
 * 7: mac
 * 5: lac
 * 5: lam
 * 5: mal
 * 4: am
 * 4: ma
 * 2: al
 * 2: la
 * Rack?
 *
 */

public class WordFinder {

    /**
     *
     * This contains the main method and is responsible for processing the command-line argument and handling
     * any errors related to the above tasks. It will run in a loop asking for a new rack and display all
     * words that can be made with each rack. All other functionality will be delegated to other objects created in
     * man and their methods.
     *
     * @param args Specified dictionary. If no argument provided, defaults to sowpods.txt
     */

    public static void main(String[] args) {

        //set default dictionary to sowpods.txt
        String fileName = "sowpods.txt";

        //process command line argument;
        if (args.length == 1)
            fileName = args[0];

        //create anagram dictionary
        AnagramDictionary dict = null;
        try {
            dict = new AnagramDictionary(fileName);
        } catch (FileNotFoundException exc) {
            System.out.println("ERROR: " + exc);
            return;
        }

        //run program
        System.out.println("Type . to quit.");

        String userInput;
        Scanner scan = new Scanner(System.in);
        ScoreTable score = new ScoreTable();

        //Continuously run until . appears
        while (true) {
            System.out.print("Rack? ");

            //any non whitespace will be considered a rack
            userInput = scan.next();

            //exit program if the input is .
            if (userInput.equals("."))
                return;

            // Find all the words for rack specified
            Map<String, Integer> wordMap = Rack.findAllWords(userInput, dict, score);

            //Print out how many words can be made with userInput arranged in alphabetical order by characters
            System.out.println("We can make " + wordMap.size() + " words from " +
                    "\"" + dict.sortStringLexOrder(userInput) + "\"");

            //Sort map by decreasing order in score and words in alphabetical order
            if (wordMap.size() != 0) {
                System.out.println("All of the words with their scores (sorted by score):");
                MapProcessing.sortAndPrintMap(wordMap);
            }
        }
    }
}
