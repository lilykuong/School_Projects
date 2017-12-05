// Name: Lily Kuong
// USC NetID: lkuong
// CS 455 PA4
// Fall 2017

/**
 * This class provides the score for each string based on how much each scrabble letter is worth. Uppercase
 * and lowercase letters worth the same number of points.
 *
 * The following are the letter values used:
 * (1 point)-A, E, I, O, U, L, N, S, T, R
 * (2 points)-D, G
 * (3 points)-B, C, M, P
 * (4 points)-F, H, V, W, Y
 * (5 points)-K
 * (8 points)- J, X
 * (10 points)-Q, Z
 */
public class ScoreTable {
    /**
     * Representation Invariants:
     * TOTAL_LETTERS represent the total number of letters in the English language
     * letterValue is based on lower case letters and only use lower case letters to find the index
     * index for letterValue for a specific letter would be letterValue['b' -'a'] with b being the letter to be found
     * letterValue represents each letter and no value of of each character is 0
     */

    /**
     * Represents each letter's value
     */
    private int[] letterValue;

    /**
     * This represents the total number of letters in alphabet
     */
    private static final int TOTAL_LETTERS = 26;

    /**cmal
     * Creates an array of 26 (each element matches with the corresponding letter) with their
     * value in points in the array. Refer to the class description for the score breakdown.
     */
    public ScoreTable() {
        letterValue = new int[TOTAL_LETTERS];

        // (1 point)-A, E, I, O, U, L, N, S, T, R
        letterValue['a' - 'a'] = letterValue['e' - 'a'] = letterValue['i' - 'a'] = letterValue['o' - 'a'] =
                letterValue['u' - 'a'] = letterValue['l' - 'a'] = letterValue['n' - 'a'] = letterValue['s' - 'a'] =
                        letterValue['t' - 'a'] = letterValue['r' - 'a'] = 1;

        // (2 points)-D, G
        letterValue['d' - 'a'] = letterValue['g' - 'a'] = 2;

        // (3 points)-B, C, M, P
        letterValue['b' - 'a'] = letterValue['c' - 'a'] = letterValue['m' - 'a'] = letterValue['p' - 'a'] = 3;

        // (4 points)-F, H, V, W, Y
        letterValue['f' - 'a'] = letterValue['h' - 'a'] = letterValue['v' - 'a'] = letterValue['w' - 'a'] =
                letterValue['y' - 'a'] = 4;

        // (5 points)-K
        letterValue['k' - 'a'] = 5;

        // (8 points)- J, X
        letterValue['j' - 'a'] = letterValue['x' - 'a'] = 8;

        // (10 points)-Q, Z
        letterValue['q' - 'a'] = letterValue['z' - 'a'] = 10;
    }

    /**
     * Calculates the string value based on each letter value
     * @param str String used to calculate the score
     * @return the total amount of points for that string
     *
     * PRE: There are only characters in the alphabet in the string
     */
    public int value(String str) {
        String strLower = str.toLowerCase();
        int total = 0;
        for (int i = 0; i < strLower.length(); i++) {
            total = total + letterValue[strLower.charAt(i) - 'a'];
        }
        return total;
    }
}
