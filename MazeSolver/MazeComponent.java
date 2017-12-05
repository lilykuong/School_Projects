/**
 * Name: Lily Kuong
 * USC loginid: lkuong
 * CSCI 455 PA3
 * Fall 2017
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JComponent;

/**
 MazeComponent class

 A component that displays the maze and path through it if one has been found.
 */
public class MazeComponent extends JComponent {

    private static final int START_X = 10; // top left of corner of maze in frame
    private static final int START_Y = 10;
    private static final int BOX_WIDTH = 20;  // width and height of one maze "location"
    private static final int BOX_HEIGHT = 20;
    private static final int INSET = 2;
    // how much smaller on each side to make entry/exit inner box

    private int mazeSizeRow;
    private int mazeSizeCol;

    private Maze maze;


    /**
     * Constructs the component.
     *
     * @param maze the maze to display
     */
    public MazeComponent(Maze maze) {
        mazeSizeRow = maze.numRows();
        mazeSizeCol = maze.numCols();
        this.maze = maze;
    }


    /**
     * Draws the current state of maze including the path through it if one has
     * been found.
     *
     * @param g the graphics context
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        //draw the parameter of the maze
        drawMazeParam(g2);

        //draw all the Walls in the maze.
        drawWalls(g2);

        //draw start and end locations
        drawLoc(g2);

        //draw path to find the exit location
        drawPath(g2);

    }

    /**
     * Draws the parameter of the maze
     *
     * @param g2 the graphics 2D context
     */
    private void drawMazeParam(Graphics2D g2) {
        Rectangle mazeParam = new Rectangle(START_X, START_Y,
                        mazeSizeCol * BOX_WIDTH, mazeSizeRow * BOX_HEIGHT);
        g2.draw(mazeParam);
    }

    /**
     * Draws and fills the walls in the maze Black. Free paths don't need to be drawn
     * because background color is white
     *
     * @param g2 the graphics 2D context
     */
    private void drawWalls(Graphics2D g2) {
        Rectangle rect;
        int rowNum = 0;
        int colNum = 0;
        MazeCoord coord;

        while (rowNum < mazeSizeRow) {
            while (colNum < mazeSizeCol) {

                coord = new MazeCoord(rowNum, colNum);

                //Check if that coord has a wall.
                if (maze.hasWallAt(coord)) {

                    //fill the walls black
                    g2.setColor(Color.BLACK);
                    g2.fillRect((START_X + (colNum * BOX_WIDTH)),
                                (START_Y + (rowNum * BOX_HEIGHT)),
                                 BOX_WIDTH,
                                 BOX_HEIGHT);
                }
                colNum++;
            }
            rowNum++;
            colNum = 0;
        }
    }

    /**
     * Draws a yellow square for the startLoc and a green square for the exitLoc
     *
     * @param g2 the graphics 2D context
     */
    private void drawLoc(Graphics2D g2) {
        //Start location
        MazeCoord startLoc = maze.getEntryLoc();
        g2.setColor(Color.yellow);
        int startLocRow = startLoc.getRow();
        int startLocCol = startLoc.getCol();
        g2.fillRect(START_X + INSET + (startLocCol * BOX_WIDTH),
                    START_Y + INSET + (startLocRow * BOX_HEIGHT),
                BOX_WIDTH - INSET - 1,
                BOX_HEIGHT - INSET - 1);


        //End location
        MazeCoord endLoc = maze.getExitLoc();
        g2.setColor(Color.green);
        int endLocRow = endLoc.getRow();
        int endLocCol = endLoc.getCol();
        g2.fillRect(START_X + INSET + (endLocCol * BOX_WIDTH) ,
                START_Y + INSET + (endLocRow * BOX_HEIGHT) ,
                BOX_WIDTH - INSET - 1,
                BOX_HEIGHT - INSET - 1);
    }

    /**
     * Draws the path to solve the maze.
     * PRE: path size > 0
     *
     * @param g2 the graphics context
     */
    private void drawPath(Graphics2D g2) {
        g2.setColor(Color.blue);

        LinkedList<MazeCoord> path = maze.getPath();

        //no path drawn if the size of the path is 0. Prevents MazeFrame from drawing the path
        //before search occurs
        if (path.size() == 0) {return;}

        ListIterator<MazeCoord> iter = path.listIterator();

        MazeCoord prev = iter.next();
        MazeCoord next;

        int prevRow; int prevCol;

        int nextRow; int nextCol;

        //draw the path to each coordinate in path
        while (iter.hasNext()) {

            prevRow = ((prev.getRow() * BOX_HEIGHT) + BOX_HEIGHT/2 + START_Y);
            prevCol = ((prev.getCol() * BOX_WIDTH) + BOX_WIDTH/2 + START_X);

            next = iter.next();
            nextRow = ((next.getRow() * BOX_HEIGHT) + BOX_HEIGHT/2 +START_Y);
            nextCol = ((next.getCol() * BOX_WIDTH) + BOX_WIDTH/2 +START_X);

            g2.drawLine(prevCol, prevRow, nextCol, nextRow);

            prev = next;

        }

    }
}



