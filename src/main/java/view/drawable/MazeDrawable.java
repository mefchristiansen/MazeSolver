package view.drawable;

import model.Maze;
import model.Cell;
import model.MazeState;

import java.awt.*;

/**
 * Class responsible for the drawing of the maze. The maze is drawn by iterating through each cell, and drawing each
 * cell.
 */
class MazeDrawable {
	/**
	 * @param maze
	 * @param graphics
	 * @param mazeState
	 * @param yOffSet
	 */
    public void drawMaze(Maze maze, Graphics graphics, MazeState mazeState, int yOffSet) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        for (int r = 0; r < maze.numRows(); r++) {
            for (int c = 0; c < maze.numCols(); c++) {
                Cell cell = maze.mazeCell(r,c);
                CellDrawable.drawCell(cell, graphics2D, mazeState, yOffSet);
            }
        }
    }
}
