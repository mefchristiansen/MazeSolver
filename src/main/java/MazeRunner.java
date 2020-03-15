import controller.MazeController;
import model.*;

public class MazeRunner {
	private static final int NUM_ROWS = 25;
	private static final int NUM_COLS = 25;

	public static void main(String[] args) {
        // Initialize maze
        Maze maze = new Maze(NUM_ROWS, NUM_COLS);

        // Initialize controller and add maze model
        MazeController mazeController = new MazeController(maze);

        // Start building maze
        mazeController.launch();
	}
}