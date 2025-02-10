package Controller;

import Model.*;
import View.LeftPanel;
import View.LeftPanelEvent;
import View.RightPanel;

import javax.swing.*;
import java.sql.Date;
import java.util.List;

/**
 * Glavni kontroler
 */
public class Controller {
    private UserModel userModel;
    private ExpencesModel expencesModel;
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private LeftPanelController leftPanelController;
    private RightPanelController rightPanelController;


    /**
     * Konstruktor kontrolera
     *
     * @param userModel     model korisnika
     * @param expencesModel model troškova
     * @param leftPanel     lijevi panel
     * @param rightPanel    desni panel
     */
    public Controller(UserModel userModel, ExpencesModel expencesModel, LeftPanel leftPanel, RightPanel rightPanel) {
        this.userModel = userModel;
        this.expencesModel = expencesModel;
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;

        // Kreiraj kontrolere za lijevi i desni panel
        this.rightPanelController = new RightPanelController(rightPanel, expencesModel);
        this.leftPanelController = new LeftPanelController(leftPanel, this);

        // Poveži model troškova s desnim panelom
        this.expencesModel.addObserver(this.rightPanel);
    }

    /**
     * Obrada događaja iz lijevog panela
     *
     * @param event događaj iz lijevog panela
     */
    public void processLeftPanelEvent(LeftPanelEvent event) {
        String name = event.getName();
        ExpenseCategory category = ExpenseCategory.valueOf(event.getCategory().toUpperCase());
        String paidBy = event.getPaidBy();
        Date date = new Date(event.getDate().getTime());
        double amount = event.getAmount();
        List<String> users = event.getUsers();

        // Kreiraj novi trošak
        Expense expense = new Expense(name, category, paidBy, amount, date, users);
        expense.setDebts(event.getDebtUsers(), event.getDebtAmounts());

        // 1️⃣ Dodajemo trošak u model
        expencesModel.addExpense(expense);

        // 2️⃣ Dodajemo trošak u desni panel (GUI)
        rightPanelController.addExpense(name, category.name(), paidBy, date, amount, users);

        // 3️⃣ Spremi trošak u bazu podataka
        DatabaseConnection.saveExpenseToDatabase(
                expense.getName(),
                expense.getCategory().name(),
                expense.getPaidBy(),
                new Date(expense.getDate().getTime()),
                expense.getAmount(),
                expense.getUsers()
        );

        // 4️⃣ Nakon dodavanja novog troška, preračunaj sve troškove
        rightPanelController.rebuildFromModel();
    }

    /**
     * Dohvati model troškova
     *
     * @return model troškova
     */
    public ExpencesModel getExpencesModel() {
        return expencesModel;
    }

    /**
     * Dohvati desni panel
     *
     * @return desni panel
     */
    public RightPanel getRightPanel() {
        return rightPanel;
    }

    /**
     * Dohvati kontroler desnog panela
     *
     * @return kontroler desnog panela
     */
    public RightPanelController getRightPanelController() {
        return rightPanelController;
    }

    /**
     * Dohvati lijevi panel
     *
     * @return lijevi panel
     */
    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    /**
     * Izlazak iz aplikacije
     */
    public void exitApplication() {
        System.exit(0);
    }

    /**
     * Obradi akciju izlaska uz potvrdu korisnika
     */
    public void handleExit() {
        int result = JOptionPane.showConfirmDialog(null,
                "Jeste li sigurni da želite izaći?",
                "Izlaz",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            exitApplication();
        }
    }
}