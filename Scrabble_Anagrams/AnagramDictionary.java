// Name: Lily Kuong
// USC NetID: lkuong
// CS 455 PA4
// Fall 2017

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * A dictionary of all anagram sets.
 *
 * The dictionary can have a mix of lower and upper case words. The processing is case sensitive. All anagrams of
 * one String is found time linear in the size of the output set. If there are duplicates in the dictionary it
 * will not add the duplicate to the anagram set.
 */

public class AnagramDictionary {

    /**
     * Representation invariants:
     * anagramMap contains all the anagrams from the dictionary and stores each string in lexicographic order
     * there should be no duplicates or null in the map or in the linkedlist
     */

    private Map<String, ArrayList<String>> anagramMap = new HashMap<>();

    /**
     * Create an anagram dictionary from the list of words given in the file
     * indicated by fileName.
     * PRE: The strings in the file are unique.
     *
     * @param fileName the name of the file to read from
     * @throws FileNotFoundException if the file is not found
     */
    public AnagramDictionary(String fileName) throws FileNotFoundException {
        File dict = new File(fileName);
        Scanner scan = new Scanner(dict);

        String current;
        String sorted;
        ArrayList<String> newAnagram;

        while (scan.hasNext()) {
            //get current word
            current = scan.nextLine();

            //sort current word in lexicographic order
            sorted = sortStringLexOrder(current);

            //check if key is already in there
            if (anagramMap.containsKey(sorted)) {

                //add the word to that anagram
                anagramMap.get(sorted).add(current);
            }

            //make an entry for the new anagram list
            else {
                newAnagram = new ArrayList<>();
                newAnagram.add(current);
                anagramMap.put(sorted, newAnagram);
            }
        }
    }


    /**
     * Return all anagrams with the same characters as that string. This method is case-sensitive.
     * E.g. "CARE" and "race" would not be recognized as anagrams.
     *
     * @param s string to process
     * @return a list of the anagrams of s
     */
    public ArrayList<String> getAnagramsOf(String s) {
        //return map.get(sortStringLexOrder(s));
        ArrayList<String> anagrams = anagramMap.get(sortStringLexOrder(s));
        ArrayList<String> temp = new ArrayList<String>();
        if (anagrams != null) {
            temp.addAll(anagrams);
        }
        return temp;
    }

    /**
     * Arranges the String to be in lexicographic order
     *
     * @param str   word/string
     * @return      string in lexicographic order
     */
    public String sortStringLexOrder(String str) {
        char[] temp = str.toCharArray();
        Arrays.sort(temp);
        String sorted = new String(temp);
        return sorted;
    }

}
