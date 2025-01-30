package Controller;

/**
 * Klasa Controller predstavlja kontroler.
 * Kontroler sadrzi modele korisnika i troskova, kao i panele.
 * Kontroler obradjuje dogadjaje sa levog panela i prosledjuje ih desnom panelu.
 * Kontroler takodje obradjuje dogadjaje sa meni bara.
 * Kontroler takodje obradjuje dogadjaje sa desne strane panela.
 *
 * @version 1.0
 *
 * Metode:
 * - processLeftPanelEvent(LeftPanelEvent event) : void - obradjuje dogadjaje sa levog panela
 * - exitApplication() : void - izlazi iz aplikacije
 * - handleExit() : void - obradjuje dogadjaj za izlazak iz aplikacije
 * - handleDeleteAllData() : void - obradjuje dogadjaj za brisanje svih podataka
 * - getExpencesModel() : ExpencesModel - vraca model troskova
 * - getRightPanel() : RightPanel - vraca desni panel
 *
 * @see UserModel
 * @see ExpencesModel
 * @see LeftPanel
 * @see RightPanel
 *
 */

import Model.*;
import View.LeftPanel;
import View.LeftPanelEvent;
import View.RightPanel;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class Controller {
    private UserModel userModel;
    private ExpencesModel expencesModel;
    private LeftPanel leftPanel;
    private RightPanel rightPanel;

    public Controller(UserModel userModel, ExpencesModel expencesModel, LeftPanel leftPanel, RightPanel rightPanel) {
        this.userModel = userModel;
        this.expencesModel = expencesModel;
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;
        this.rightPanel.setController(this);
        this.expencesModel.addObserver(this.rightPanel);
    }

    public void processLeftPanelEvent(LeftPanelEvent event) {
        String name = event.getName();
        ExpenseCategory category = ExpenseCategory.valueOf(event.getCategory().toUpperCase());
        String paidBy = event.getPaidBy();
        Date date = event.getDate();
        double amount = event.getAmount();
        List<String> users = event.getUsers();

        Expense expense = new Expense(name, category, paidBy, amount, date, users).createExpense(name, category, amount, date, paidBy, users);
        expencesModel.addExpense(expense);

        rightPanel.addExpense(name, category.name(), paidBy, date, amount, users);

        DatabaseConnection.saveExpenseToDatabase(expense);
    }

    public void exitApplication() {
        System.exit(0);
    }

    public void handleExit() {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            exitApplication();
        }
    }

    public void handleDeleteAllData() {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ALL data?", "Warning", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseConnection.deleteAllData();
        }
    }

    public ExpencesModel getExpencesModel() {
        return expencesModel;
    }

    public RightPanel getRightPanel() {
        return rightPanel;
    }
}