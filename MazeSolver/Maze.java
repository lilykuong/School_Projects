/**
 * Name: Lily Kuong
 * USC loginid: lkuong
 * CSCI 455 PA3
 * Fall 2017
 */

import java.util.LinkedList;
import java.util.Arrays;

/**
 * Maze class
 * <p>
 * Stores information about a maze and can find a path through the maze
 * (if there is one).
 * <p>
 * Assumptions about structure of the maze, as given in mazeData, startLoc, and endLoc
 * (parameters to constructor), and the path:
 * -- no outer walls given in mazeData -- search assumes there is a virtual
 * border around the maze (i.e., the maze path can't go outside of the maze
 * boundaries)
 * -- start location for a path is maze coordinate startLoc
 * -- exit location is maze coordinate exitLoc
 * -- mazeData input is a 2D array of booleans, where true means there is a wall
 * at that location, and false means there isn't (see public FREE / WALL
 * constants below)
 * -- in mazeData the first index indicates the row. e.g., mazeData[row][col]
 * -- only travel in 4 compass directions (no diagonal paths)
 * -- can't travel through walls
 * <p>
 * Does not perform any I/O
 *

 */

public class Maze {

    /**
     *  Representation invariant:
     *  - mazeData is a boolean array provided by MazeViewer.
     *  - mazeData size is at least 1 by 1
     *  - (0,0) <= startLoc <= (maxCol, maxRow)
     *  - (0,0) <= exitLoc <= ((maxCol, maxRow)
     *  - if startLoc and exitLoc are outside maze parameters, will return no path found
     *  - if startLoc and exitLoc are walls, will return no path found
     *  - path contains the starting location, coordinates to get to exit location, and exit location
     *  - if no path is found, path will be an empty LinkedList
     */

    public static final boolean FREE = false;
    public static final boolean WALL = true;
    private static boolean VISITED = true;

    private boolean[][] mazeData;
    private MazeCoord startLoc;
    private MazeCoord exitLoc;

    private LinkedList<MazeCoord> path = new LinkedList<>();

    //used in search function
    private boolean exitFound = false;
    private boolean startFound = false;

    /**
     * Constructs a maze.
     *
     * @param mazeData the maze to search.  See general Maze comments above for what
     *                 goes in this array.
     * @param startLoc the location in maze to start the search (not necessarily on an edge)
     * @param exitLoc  the "exit" location of the maze (not necessarily on an edge)
     *                 PRE: 0 <= startLoc.getRow() < mazeData.length and 0 <= startLoc.getCol() < mazeData[0].length
     *                 and 0 <= endLoc.getRow() < mazeData.length and 0 <= endLoc.getCol() < mazeData[0].length
     */
    public Maze(boolean[][] mazeData, MazeCoord startLoc, MazeCoord exitLoc) {
        for (int i = 0; i < mazeData.length; i++){
            this.mazeData[i] = Arrays.copyOf(mazeData[i], mazeData[i].length);
        }
        this.startLoc = startLoc;
        this.exitLoc = exitLoc;
    }


    /**
     * Returns the number of rows in the maze
     *
     * @return number of rows
     */
    public int numRows() {
        return mazeData.length;
    }


    /**
     * Returns the number of columns in the maze
     *
     * @return number of columns
     */
    public int numCols() {
        return mazeData[0].length;
    }


    /**
     * Returns true iff there is a wall at this location
     *
     * @param loc the location in maze coordinates
     * @return whether there is a wall here
     * PRE: 0 <= loc.getRow() < numRows() and 0 <= loc.getCol() < numCols()
     */
    public boolean hasWallAt(MazeCoord loc) {
        int row = loc.getRow();
        int column = loc.getCol();
        return (mazeData[row][column]);
    }


    /**
     * Returns the entry location of this maze.
     */
    public MazeCoord getEntryLoc() {
        return startLoc;
    }


    /**
     * Returns the exit location of this maze.
     */
    public MazeCoord getExitLoc() {
        return exitLoc;
    }


    /**
     * Returns the path through the maze. First element is start location, and
     * last element is exit location.  If there was not path, or if this is called
     * before a call to search, returns empty list.
     *
     * @return the maze path
     */
    public LinkedList<MazeCoord> getPath() {
        return new LinkedList<> (path);
    }


    /**
     * Find a path from start location to the exit location (see Maze
     * constructor parameters, startLoc and exitLoc) if there is one.
     * Client can access the path found via getPath method.
     *
     * @return whether a path was found.
     */
    public boolean search() {
        //only search if a path has not been found
        if (!exitFound) {
            //Start Coordinates
            int startLocRow = startLoc.getRow();
            int startLocCol = startLoc.getCol();

            //Exit Coordinates
            int exitLocRow = exitLoc.getRow();
            int exitLocCol = exitLoc.getCol();

            //Check if start and exit locations are within parameters
            if     (startLocRow < 0 || startLocRow > numRows() - 1 ||
                    startLocCol < 0 || startLocCol > numCols() - 1 ||
                    exitLocRow < 0  || exitLocRow > numRows() - 1  ||
                    exitLocCol < 0  || exitLocCol > numCols() - 1)
                return exitFound;

            //Check if the start and exit are not walls before trying to find a path
            if ((mazeData[startLocRow][startLocCol] == WALL
                    || mazeData[exitLocRow][exitLocCol] == WALL))
                return exitFound;

            // copy of mazeData to keep track of locations visited.
            boolean[][] locVisited = new boolean[numRows()][];
            for (int i = 0; i < numRows(); i++)
                locVisited[i] = mazeData[i].clone();

            //search for path
            findPath(locVisited, startLocRow, startLocCol);
        }

        return exitFound;
    }

    /**
     * Helper Recursive function for search. Checks neighboring coordinates and
     * adds coordinates to the front of a LinkedList after the exit has been found
     *
     * @param locVisited Location visited array
     * @param row y coordinate in 2D array
     * @param col x coordinate in 2D array
     */
    private void findPath(boolean[][] locVisited, int row, int col) {

        exitFound = CheckCoordForFinalPos(row, col);
        //Go Up
        if (row - 1 >= 0)
            goToCoord(row - 1, col, locVisited);

        //Go Left
        if (col - 1 >= 0)
            goToCoord(row, col - 1, locVisited);

        //Go Down
        if (row + 1 < numRows())
            goToCoord(row + 1, col, locVisited);

        //Go Right
        if (col + 1 < numCols())
            goToCoord(row, col + 1, locVisited);

        //Add elements to path if the exitLoc was Found and stop when it gets to startLoc
        if (exitFound) {
            if (row == startLoc.getRow() && col == startLoc.getCol() && !startFound) {
                startFound = VISITED;
                path.addFirst(new MazeCoord(row, col));
            } else if (!startFound)
                path.addFirst(new MazeCoord(row, col));
        }

    }

    /**
     * Check if the coordinates is the Exit Location
     *
     * @param row y coordinate in 2D array
     * @param col x coordinate in 2D array
     * @return true if the coordinate is the exit location
     */

    private boolean CheckCoordForFinalPos(int row, int col) {
        //Coordinate is final position
        return (row == exitLoc.getRow() && col == exitLoc.getCol());
    }

    /**
     * Check if the coordinate was not visited and if the Exit wasn't found.
     * Mark coordinate was visited and then go to that coordinate
     *
     * @param row the y coordinate in 2D array
     * @param col the x coordinate in 2D array
     * @param locVisited 2D array that contains visited coordinates
     */
    private void goToCoord(int row, int col, boolean[][] locVisited) {
        if (!locVisited[row][col] && !exitFound) {
            locVisited[row][col] = VISITED;
            findPath(locVisited, row, col);
        }
    }
}
