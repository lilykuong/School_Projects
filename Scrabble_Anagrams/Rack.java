// Name: Lily Kuong
// USC NetID: lkuong
// CS 455 PA4
// Fall 2017

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a rack of scrabble tiles and finds all subsets of the string
 * based on its unique characters and frequency of each character.
 *
 * For example: find all subset for the rack "abbd"
 * All the unique characters will be abd
 * The frequency will be {1, 2, 1} respectively to each unique character.
 * The following subset is ab, ad, bd, abb, abd, bbd, abbd
 */

public class Rack {

    /**
     * Finds all subsets of the multiset starting at position k in unique and mult.
     * unique and mult describe a multiset such that mult[i] is the multiplicity of the char
     * unique.charAt(i).
     * PRE: mult.length must be at least as big as unique.length()
     * 0 <= k <= unique.length()
     *
     * @param unique a string of unique letters
     * @param mult   the multiplicity of each letter from unique.
     * @param k      the smallest index of unique and mult to consider.
     * @return all subsets of the indicated multiset
     * @author Claire Bono
     */
    private static ArrayList<String> allSubsets(String unique, int[] mult, int k) {
        ArrayList<String> allCombos = new ArrayList<>();

        if (k == unique.length()) {  // multiset is empty
            allCombos.add("");
            return allCombos;
        }

        // get all subsets of the multiset without the first unique char
        ArrayList<String> restCombos = allSubsets(unique, mult, k + 1);

        // prepend all possible numbers of the first char (i.e., the one at position k)
        // to the front of each string in restCombos.  Suppose that char is 'a'...

        String firstPart = "";          // in outer loop firstPart takes on the values: "", "a", "aa", ...
        for (int n = 0; n <= mult[k]; n++) {
            for (int i = 0; i < restCombos.size(); i++) {  // for each of the subsets
                // we found in the recursive call
                // create and add a new string with n 'a's in front of that subset
                allCombos.add(firstPart + restCombos.get(i));
            }
            firstPart += unique.charAt(k);  // append another instance of 'a' to the first part
        }

        return allCombos;
    }

    /**
     * Returns the SubSet array list
     *
     * PRE: mult.length must be at least as big as unique.length()
     * 0 <= k <= unique.length()
     *
     * @param unique a string of unique letters
     * @param mult   the multiplicity of each letter from unique.
     * @param k      the smallest index of unique and mult to consider.
     * @return all subsets of the indicated multiset
     */
    public static ArrayList<String> returnSubSet(String unique, int[] mult, int k) {
        return allSubsets(unique, mult, k);
    }


    /**
     * Returns the unique characters and the frequency it occurs within the string in alphabetical order.
     *
     * @param str string to be processed
     * @return Map of the keys, each being a unique character in alphabetical order connected to the frequency of
     * occurrence
     */
    private static Map<Character, Integer> findUniqueChar(String str) {

        //Map contains the letter and the value of occurrence for each letter
        Map<Character, Integer> uniqueChar = new TreeMap<>();

        for (int i = 0; i < str.length(); i++) {
            char key = str.charAt(i);

            //already part of the map
            if (uniqueChar.containsKey(key)) {
                uniqueChar.put(key, uniqueChar.get(key) + 1);
            }

            //not part of the map.
            else {
                uniqueChar.put(key, 1);
            }

        }

        return uniqueChar;

    }

    /**
     * Find all the words and their score based on the rack and the dictionary provided
     * @param userInput Rack provided by the user
     * @param dict      Anagram Dictionary
     * @param score     ScoreTable for each character
     * @return the map that contains each anagram with its correlated score
     */
    public static Map <String, Integer> findAllWords (String userInput, AnagramDictionary dict, ScoreTable score) {

        Map<Character, Integer> uniqueChar = findUniqueChar(userInput);

        // Extract map value so that the uniqueString in String format and the multiSet in an array format
        int[] multi = new int[uniqueChar.size()];
        String uniqueStr = "";
        int i = 0;
        for (Map.Entry<Character, Integer> entry : uniqueChar.entrySet()) {
            uniqueStr = uniqueStr + entry.getKey();
            multi[i] = entry.getValue();
            i++;
        }

        //get all the words in the subset
        ArrayList<String> subSet = returnSubSet(uniqueStr, multi, 0);

        //Find all the words that can make that word and associated score
        Map<String, Integer> wordMap = new HashMap<>();
        ArrayList<String> temp;
        for (String a : subSet) {
            temp = dict.getAnagramsOf(a);
            if (temp != null) {
                for (String word : temp)
                    wordMap.put(word, score.value(word));
            }

        }
        return new HashMap<>(wordMap);
    }

}
