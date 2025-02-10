package Controller;

import Model.*;
import View.LeftPanel;
import View.LeftPanelEvent;
import View.RightPanel;

import javax.swing.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        this.rightPanelController = new RightPanelController(rightPanel, expencesModel);
        this.leftPanelController = new LeftPanelController(leftPanel, this);

        this.expencesModel.addObserver(this.rightPanel);
    }

    public void processLeftPanelEvent(LeftPanelEvent event) {
        String name = event.getName();
        ExpenseCategory category = ExpenseCategory.valueOf(event.getCategory().toUpperCase());
        String paidBy = event.getPaidBy();
        Date date = new Date(event.getDate().getTime());
        double amount = event.getAmount();
        List<String> users = event.getUsers();

        Expense expense = new Expense(name, category, paidBy, amount, date, users);
        expense.setDebts(event.getDebtUsers(), event.getDebtAmounts());

        expencesModel.addExpense(expense);

        rightPanelController.addExpense(name, category.name(), paidBy, date, amount, users);

        DataBaseController.saveExpenseToDatabase(
                expense.getName(),
                expense.getCategory().name(),
                expense.getPaidBy(),
                new Date(expense.getDate().getTime()),
                expense.getAmount(),
                expense.getUsers()
        );

        rightPanelController.rebuildFromModel();
    }

    public ExpencesModel getExpencesModel() {
        return expencesModel;
    }

    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public RightPanelController getRightPanelController() {
        return rightPanelController;
    }

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    public void exitApplication() {
        System.exit(0);
    }

    public void handleExit() {
        int result = JOptionPane.showConfirmDialog(null,
                "Jeste li sigurni da želite izaći?",
                "Izlaz",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            exitApplication();
        }
    }

    public void loadDataFromDatabase() {
        if (this == null) return;

        expencesModel.clearExpenses();
        leftPanel.clearUsers();
        rightPanel.refreshTable();

        List<String> dbUsers = DataBaseController.getAllUsers();
        for (String user : dbUsers) {
            leftPanel.addUser(user);
        }

        ResultSet rsExpenses = DataBaseController.getAllExpenses();

        try {
            while (rsExpenses.next()) {
                String name = rsExpenses.getString("name");
                String category = rsExpenses.getString("category");
                String paidBy = rsExpenses.getString("paid_by");
                java.sql.Date date = rsExpenses.getDate("date");
                double amount = rsExpenses.getDouble("amount");

                int expenseId = rsExpenses.getInt("id");
                List<String> expenseUsers = DataBaseController.getUsersForExpense(expenseId);

                Expense expense = new Expense(
                        name,
                        ExpenseCategory.valueOf(category.toUpperCase()),
                        paidBy,
                        amount,
                        new java.sql.Date(date.getTime()),
                        expenseUsers
                );
                expencesModel.addExpense(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        rightPanelController.rebuildFromModel();
        rightPanel.refreshTable();

        JOptionPane.showMessageDialog(null,
                "Data successfully loaded from database!",
                "Load Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void deleteAllData() {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete ALL data?", "Warning", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DataBaseController.deleteAllData();
            expencesModel.clearExpenses();
            leftPanel.clearUsers();
            rightPanel.refreshTable();
            rightPanel.setTotalDebtsAreaText("");
        }
    }
}