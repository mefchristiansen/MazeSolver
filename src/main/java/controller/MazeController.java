package controller;

import model.Maze;
import model.MazeGenerator;
import model.MazeGeneratorFactory;
import model.MazeSolver;
import model.MazeSolverFactory;
import view.MazeView;
import view.drawable.MazeSolverSelectionFrame;
import controller.listeners.AlgorithmSelectListener;
import controller.listeners.MazeClickListener;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

public class MazeController implements java.awt.event.ActionListener {
    private static final int CELL_SIZE = 25;
    private static final int MARGIN = 10;
    private static final long GENERATION_SLEEP_TIME = 1L;
    private static final long SOLVE_SLEEP_TIME = 25L;
    private static final long SOLUTION_SLEEP_TIME = 15L;

    // Model
    private Maze maze;
    private MazeGenerator generator;
    private MazeSolver solver;

    // View
    private MazeView view;

    // Listeners
    private MazeClickListener mazeClickListener;
    private AlgorithmSelectListener algorithmSelectListener;

    public MazeController(Maze maze) {
        this.maze = maze;

        // Create view
        MazeView mazeView = new MazeView(maze, CELL_SIZE, MARGIN,
        GENERATION_SLEEP_TIME, SOLVE_SLEEP_TIME, SOLUTION_SLEEP_TIME);
        this.view = mazeView;
    }

    public void launch() {
        generateMaze();
        setSolverMethod();
        setEndpoints();
        solveMaze();
    }

    private void generateMaze() {
        setMazeGenerator();
        view.setDisplayState("generate");
        generator.generateMaze();
        maze.resetMaze();
    }

    private void solveMaze() {
        view.setDisplayState("solve");
        solver.solve(maze.startingCell.row(), maze.startingCell.col(), maze.endingCell.row(), maze.endingCell.col());
        solver.walkSolutionPath();
    }

    public void setSolverMethod() {
        this.algorithmSelectListener = new AlgorithmSelectListener(this, view);
        this.view.selectionFrame = new MazeSolverSelectionFrame(algorithmSelectListener);
        this.algorithmSelectListener.enable();

        synchronized (view.selectionFrame) {
            while(solver == null) {
                try {
                    view.selectionFrame.wait();
                } catch(InterruptedException e) {
                    System.out.println(e);
                }
            }
            // this.view.mazeDisplay.removeMouseListener(this.mazeClickListener);
        }
    }

    private void setEndpoints() {
        this.mazeClickListener = new MazeClickListener(this.view);
        this.view.mazeDisplay.addMouseListener(this.mazeClickListener);
        this.mazeClickListener.enable();

        synchronized (view) {
            while(maze.startingCell == null || maze.endingCell == null) {
                try {
                    view.wait();
                } catch(InterruptedException e) {
                    System.out.println(e);
                }
            }
            this.view.mazeDisplay.removeMouseListener(this.mazeClickListener);
        }
    }

    public void setMazeGenerator() {
        MazeGenerator generator = MazeGeneratorFactory.getMazeGenerator("RECURSIVE", maze);
        // Have the view listen in on events triggered by the model
        generator.addObserver(this.view);

        this.generator = generator;
    }

    public void setMazeSolver(String algorithm) {
        MazeSolver solver = MazeSolverFactory.getMazeSolver(algorithm, maze);
        // Have the view listen in on events triggered by the model
        solver.addObserver(this.view);

        this.solver = solver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }
}
