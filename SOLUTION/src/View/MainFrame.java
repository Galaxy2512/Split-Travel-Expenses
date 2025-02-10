package View;

/**
 * MainFrame klasa predstavlja glavni prozor aplikacije.
 * MainFrame sadrži meni bar i panel za prikaz podataka.
 *
 * @version 1.0
 *
 * Metoda createComponents postavlja meni bar i kontroler.
 *
 * @see ViewPanel
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
        // Inicijalizacija modela
        UserModel userModel = new UserModel();
        ExpencesModel expencesModel = new ExpencesModel();

        // Inicijalizacija prikaza i kontrolera
        viewPanel = new ViewPanel();
        controller = new Controller(userModel, expencesModel, viewPanel.getLeftPanel(), viewPanel.getRightPanel());
        leftPanelController = new LeftPanelController(viewPanel.getLeftPanel(), controller);
        rightPanelController = new RightPanelController(viewPanel.getRightPanel(), expencesModel); // Prosljeđujemo i model troškova

        // Konfiguracija glavnog prozora
        setTitle("Expense Tracker");
        setSize(800, 600);
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createComponents();
    }

    private void createComponents() {
        add(viewPanel);

        // Inicijalizacija i povezivanje MenuBar-a
        menuBar = new MenuBar();
        setJMenuBar(menuBar);

        // Povezivanje kontrolera s panelima i MenuBar-om
        viewPanel.setController(leftPanelController, rightPanelController);
        menuBar.setController(controller);
    }
}
