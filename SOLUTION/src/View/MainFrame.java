package View;

/**
 * MainFrame klasa predstavlja glavni prozor aplikacije.
 * MainFrame sadrzi meni bar i panel za prikaz podataka.
 *
 * @version 1.0
 *
 * Metoda createComponents postavlja meni bar i kontroler.
 *
 * @see ViewPanel
 *
 */

import Controller.Controller;
import Controller.LeftPanelController;
import Controller.RightPanelController;
import Model.ExpencesModel;
import Model.UserModel;

import javax.swing.*;

public class MainFrame extends JFrame {
    private ViewPanel viewPanel;
    private MenuBar menuBar;
    private Controller controller;
    private LeftPanelController leftPanelController;
    private RightPanelController rightPanelController;

    public MainFrame() {
        UserModel userModel = new UserModel();
        ExpencesModel expencesModel = new ExpencesModel();
        viewPanel = new ViewPanel();
        controller = new Controller(userModel, expencesModel, viewPanel.getLeftPanel(), viewPanel.getRightPanel());
        leftPanelController = new LeftPanelController(viewPanel.getLeftPanel(), controller);
        rightPanelController = new RightPanelController(viewPanel.getRightPanel());

        setTitle("Expense Tracker");
        setSize(800, 600);
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createComponents();
    }

    private void createComponents() {
        add(viewPanel);
        menuBar = new MenuBar();
        setJMenuBar(menuBar);

        menuBar.setMenuBarListener(e -> {
            if (e.getActionCommand().equals("Exit")) {
                System.exit(0);
            }
        });

        viewPanel.setController(leftPanelController, rightPanelController);
        menuBar.setController(controller);
    }
}