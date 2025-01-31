package Controller;

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
    private LeftPanelController leftPanelController;
    private RightPanelController rightPanelController;

    public Controller(UserModel userModel, ExpencesModel expencesModel, LeftPanel leftPanel, RightPanel rightPanel) {
        this.userModel = userModel;
        this.expencesModel = expencesModel;
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;
        this.rightPanelController = new RightPanelController(rightPanel);
        this.leftPanelController = new LeftPanelController(leftPanel, this);
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

        rightPanelController.addExpense(name, category.name(), paidBy, date, amount, users);

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