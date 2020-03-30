package controller.listeners;

import controller.MazeChangeListener;
import controller.MazeController;

import javax.swing.event.ChangeEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MazeCustomNumColsListener extends MazeChangeListener {

    public MazeCustomNumColsListener(MazeController mazeController) {
        super(mazeController);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner colsSpinner = (JSpinner) e.getSource();
        SpinnerNumberModel colsSpinnerModel = (SpinnerNumberModel)(colsSpinner.getModel());
        int numCols = (int) colsSpinnerModel.getNumber();
        mazeController.setMazeNumCols(numCols);
    }
}
