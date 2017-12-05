/**
 * Name: Lily Kuong
 * USC loginid: lkuong
 * CSCI 455 PA3
 * Fall 2017
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * MazeViewer class
 *
 * Program to read in and display a maze and a path through the maze. At user
 * command displays a path through the maze if there is one.
 *
 * How to call it from the command line:
 *
 *      java MazeViewer mazeFile
 *
 * where mazeFile is a text file of the maze. The format is the number of rows
 * and number of columns, followed by one line per row, followed by the start location, 
 * and ending with the exit location. Each maze location is
 * either a wall (1) or free (0). Here is an example of contents of a file for
 * a 3x4 maze, with start location as the top left, and exit location as the bottom right
 * (we count locations from 0, similar to Java arrays):
 *
 * 3 4 
 * 0111
 * 0000
 * 1110
 * 0 0
 * 2 3
 *
 */

public class MazeViewer {

    private static final char WALL_CHAR = '1';
    private static final char FREE_CHAR = '0';


    public static void main(String[] args) {

        String fileName = "";

        try {

            if (args.length < 1) {
                System.out.println("ERROR: missing file name command line argument");
            } else {
                fileName = args[0];

                JFrame frame = readMazeFile(fileName);

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.setVisible(true);
            }

        } catch (FileNotFoundException exc) {
            System.out.println("ERROR: File not found: " + fileName);
        } catch (IOException exc) {
            exc.printStackTrace();
        } catch (NoSuchElementException exc) {
            System.out.println("ERROR: Wrong file format");
        }
    }

    /**
     readMazeFile reads in maze from the file whose name is given and
     returns a MazeFrame created from it.

     @param fileName the name of a file to read from (file format shown in class comments, above)
     @returns a MazeFrame containing the data from the file.

     @throws FileNotFoundException if there's no such file (subclass of IOException)
     @throws IOException (hook given in case you want to do more error-checking --
     that would also involve changing main to catch other exceptions)
     @throws NoSuchElementException if file format is incorrect
     */
    private static MazeFrame readMazeFile(String fileName) throws IOException {
        File file = new File(fileName);
        Scanner scan = new Scanner(file);

        //Size of Maze
        int mazeSizeRow = scan.nextInt();
        int mazeSizeCol = scan.nextInt();

        boolean[][] mazeData = new boolean[mazeSizeRow][mazeSizeCol];
        int row = 0;
        int column = 0;
        String value;

        while (scan.hasNext() && row < mazeSizeRow) {
            //read the next value. Since array of maze is considered to be one value, we need to
            //read each individual character
            value = scan.next();
            while (column < mazeSizeCol) {
                mazeData[row][column] = (value.charAt(column) == (WALL_CHAR));
                column++;
            }
            //move on to the next row and start back at Column 0
            row++;
            column = 0;
        }

        //save your start and exit locations
        MazeCoord startLoc = new MazeCoord(scan.nextInt(), scan.nextInt());
        MazeCoord endLoc = new MazeCoord(scan.nextInt(), scan.nextInt());

        return new MazeFrame(mazeData, startLoc, endLoc);

    }

}
