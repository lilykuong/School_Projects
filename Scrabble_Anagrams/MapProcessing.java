// Name: Lily Kuong
// USC NetID: lkuong
// CS 455 PA4
// Fall 2017


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class handles the sorting and printing of the map.
 * Sort orders the map in descending order based on its values
 */
public class MapProcessing {

    /**
     * Print map out in the form "Value: Key"
     *
     * @param map to print out
     */
    private static void printMap(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet())
            System.out.println(entry.getValue() + ": " + entry.getKey());
    }

    /**
     * Sort Map on highest to lowest word and prints out the new sorted map
     *
     * @param map to sort and print out
     */
    public static void sortAndPrintMap(Map<String, Integer> map) {
        //Print out words from highest score to lowest score
        // if values are the same, print in alphabetical order
        TreeMap<String, Integer> sortedMap = new TreeMap<>(new SortMapByValue(map));
        sortedMap.putAll(map);
        printMap(sortedMap);
    }

}

/**
 * Implements the Comparator for Map and puts map in descending order based on its value
 */
class SortMapByValue implements Comparator<String> {
    private Map<String, Integer> map;

    public SortMapByValue(Map<String, Integer> map) {
        this.map = map;
    }

    //compare values of e1 and e2
    // if e2 < e1, return neg value and ordered as e1, e2
    // if e2 == e1, compare the keys together and order from smallest key to largest key
    // if e2 > e1, return pos value and ordered as e2, e1
    public int compare(String e1, String e2) {

        if (map.get(e2).equals(map.get(e1)))
            return e1.compareTo(e2);

        return map.get(e2) - (map.get(e1));
    }
}