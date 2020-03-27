package view.drawable;

import controller.MazeController;
import model.MazeConstants;
import model.SolverType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.concurrent.Flow;

public class GUIPanel extends JPanel implements ActionListener {
    private final MazeController mazeController;

    public GUIPanel(MazeController mazeController) {
        this.mazeController = mazeController;
        initGUIPanel();
    }

    private void initGUIPanel() {
        setLayout(new GridBagLayout());

        Insets insets = new Insets(5, 0, 0, 0);

        // Maze Size Inputs
        JPanel customMazeSizeInputs = initCustomMazeSizeInputs();
        addComponent(this, customMazeSizeInputs, 0, 0, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);

        // Generate Maze Button
        JButton generateMazeButton = new JButton("Generate");
        generateMazeButton.setActionCommand("generate");
        generateMazeButton.addActionListener(mazeController.getMazeGeneratorListener());
        addComponent(this, generateMazeButton, 0, 1, 1, 1,
                GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, insets);

        // Solve Method Radio
        JPanel solveMethodRadio = initSolveMethodRadio();
        addComponent(this, solveMethodRadio, 0, 2, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);

        // Solve Maze Button
        JButton solveMazeButton = new JButton("Solve");
        solveMazeButton.setActionCommand("solve");
        solveMazeButton.addActionListener(mazeController.getMazeSolverListener());
        addComponent(this, solveMazeButton, 0, 3, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);

        // Reset Maze Button
        JButton resetMazeButton = new JButton("Reset");
        resetMazeButton.setActionCommand("reset");
        resetMazeButton.addActionListener(mazeController.getMazeResetListener());
        addComponent(this, resetMazeButton, 0, 4, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setActionCommand("exit");
        exitButton.addActionListener(this);
        addComponent(this, exitButton, 0, 5, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);
    }

    private void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth,
                              int gridheight, int anchor, int fill, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight,
                1.0, 1.0, anchor, fill, insets, 0, 0);
        this.add(component, gbc);
    }

    private JPanel initSolveMethodRadio() {
        JPanel solveMethodRadioPanel = new JPanel(new FlowLayout());

        solveMethodRadioPanel.setBorder(BorderFactory.createTitledBorder("Solve Method"));

        Box solveMethodRadioBox = Box.createVerticalBox();
        ButtonGroup solveMethodRadioButtonGroup = new ButtonGroup();

        for (SolverType solverType : SolverType.values()) {
            JRadioButton solverTypeOption = new JRadioButton(solverType.getName());
            solverTypeOption.addActionListener(mazeController.getMazeSolverSelectionRadioListener());

            if (solverType == mazeController.getSolverType()) {
                solverTypeOption.setSelected(true);
            }

            solveMethodRadioBox.add(solverTypeOption);
            solveMethodRadioButtonGroup.add(solverTypeOption);
        }

        solveMethodRadioPanel.add(solveMethodRadioBox);

        Dimension guiDimension = new Dimension(125, 100);
        solveMethodRadioPanel.setMinimumSize(guiDimension);
        solveMethodRadioPanel.setPreferredSize(guiDimension);

        return solveMethodRadioPanel;
    }

    private JPanel initCustomMazeSizeInputs() {
        JPanel customMazeSizeInputsPanel = new JPanel(new GridLayout(0, 1));

        SpinnerModel numRowsSpinner = new SpinnerNumberModel(
                MazeConstants.DEFAULT_NUM_ROWS,
                MazeConstants.MIN_NUM_ROWS,
                MazeConstants.MAX_NUM_ROWS,
                1
        );
        SpinnerModel numColsSpinner = new SpinnerNumberModel(
                MazeConstants.DEFAULT_NUM_COLS,
                MazeConstants.MIN_NUM_COLS,
                MazeConstants.MAX_NUM_COLS,
                1
        );

        addLabeledSpinner(customMazeSizeInputsPanel, "Num Rows (5-50):", numRowsSpinner);
        addLabeledSpinner(customMazeSizeInputsPanel, "Num Cols (5-50):", numColsSpinner);

        return customMazeSizeInputsPanel;
    }

    static protected JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model) {
        JLabel l = new JLabel(label);
        c.add(l);

        JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);

        return spinner;
    }

    @Override
    public void actionPerformed(ActionEvent mazeGuiButtonClick) {
        String buttonIntention = mazeGuiButtonClick.getActionCommand();

        if (buttonIntention == "exit") {
            System.exit(0);
        }
    }
}
