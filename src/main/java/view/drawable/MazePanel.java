package view.drawable;

import controller.MazeController;
import controller.listeners.MazeWaypointClickListener;
import model.Cell;
import model.Maze;
import model.MazeState;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel of the maze. This panel is where the maze is drawn during maze generation and solving, and where the user
 * can pick the start and end points.
 */
public class MazePanel extends JPanel {
	/**
	 * The state of setting waypoints. The two waypoints that are set are the start and end points.
	 */
    private enum WaypointState {
        START, END
    }

	private static final Color BACKGROUND = new Color(55, 50, 55);
    private Maze maze;
    private final MazeController mazeController;
    private final MazeDrawable mazeDrawable;
    private WaypointState waypointState;
    private int yOffset;
    private Dimension mazeDimension;

    public MazePanel(Maze maze, MazeController mazeController) {
    	this.maze = maze;
    	this.mazeController = mazeController;
        this.mazeDrawable = new MazeDrawable();
        this.waypointState = WaypointState.START;
        this.yOffset = 0;
        this.mazeDimension = new Dimension();

        initMazePanel();
    }

	/**
	 * @param panelHeight
	 */
    public void setYOffset(int panelHeight) {
    	int heightDifference = (int)(panelHeight - mazeDimension.getHeight());

    	if (heightDifference > 0) {
    		yOffset = heightDifference / 2;
		} else {
			yOffset = 0;
		}
	}

    private void initMazePanel() {
        int mazeWidth = maze.numCols() * CellDrawableConstants.CELL_SIZE + CellDrawableConstants.MARGIN * 2;
        int mazeHeight = maze.numRows() * CellDrawableConstants.CELL_SIZE + CellDrawableConstants.MARGIN * 2;

        mazeDimension = new Dimension(mazeWidth, mazeHeight);
        setMinimumSize(mazeDimension);
        setPreferredSize(mazeDimension);
        setBackground(BACKGROUND);

        addMouseListener(new MazeWaypointClickListener(this, mazeController));

        repaint();
    }

    public void repaintMaze(Maze maze) {
        this.maze = maze;
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        MazeState mazeState = mazeController.getState();
        mazeDrawable.drawMaze(maze, graphics, mazeState, yOffset);
    }

	/**
	 * Resizes the maze panel view based on the number of columns and the number of rows. This method is called on each
	 * iteration of generating the maze, and enables the user to be able to customize the dimensions of the maze.
	 */
    public void resize() {
        int mazeWidth = maze.numCols() * CellDrawableConstants.CELL_SIZE + CellDrawableConstants.MARGIN * 2;
        int mazeHeight = maze.numRows() * CellDrawableConstants.CELL_SIZE + CellDrawableConstants.MARGIN * 2;

		mazeDimension = new Dimension(mazeWidth, mazeHeight);
        setMinimumSize(mazeDimension);
        setPreferredSize(mazeDimension);
    }

	/**
	 * Sets a waypoint in the maze (based on the current state of the waypoint setting), and repaints the maze to show
	 * the new waypoint. As setting one waypoint enables the setting of another, the user can change their choice for
	 * the start and end points before solving the maze.
	 *
	 * @param mouseClickX The x coordinate of a mouse click
	 * @param mouseClickY The y coordinate of a mouse click
	 */
    public void setWaypoint(int mouseClickX, int mouseClickY) {
        for (int r = 0; r < maze.numRows(); r++) {
            for (int c = 0; c < maze.numCols(); c++) {
                Cell cell = maze.mazeCell(r,c);
                if (cell.pointInside(mouseClickX, mouseClickY, CellDrawableConstants.CELL_SIZE,
						CellDrawableConstants.MARGIN)) { // Check if the click is in the specific cell
                    if (waypointState == WaypointState.START) {
                        maze.setStartingCell(cell);
                        waypointState = WaypointState.END; // Switch to setting end point
                        repaint();
					// Ensure that the start and endpoint can't be the same cells
                    } else if (waypointState == WaypointState.END && !cell.getStart()) {
                        maze.setEndingCell(cell);
                        waypointState = WaypointState.START; // Switch to setting start point
                        repaint();
                    }
                }
            }
        }
    }

	/**
	 * Resets the waypoint state to the initial start state.
	 */
    public void resetWaypointSetterState() {
        waypointState = WaypointState.START;
    }
}
