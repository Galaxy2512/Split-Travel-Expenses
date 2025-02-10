package View;

import Controller.Controller;
import Model.DatabaseConnection;
import Controller.DataHandle;
import Model.Expense;
import Model.ExpenseCategory;


import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * MenuBar klasa predstavlja glavni meni aplikacije.
 * Omogućava korisniku izvoz, uvoz, brisanje i učitavanje podataka iz baze.
 *
 * @version 2.1
 */
public class MenuBar extends JMenuBar {

    private Controller controller;
    private DataHandle dataHandle; // 🟢 Nova instanca DataHandle klase

    public MenuBar() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadDataItem = new JMenuItem("Load Data");
        JMenuItem exportItem = new JMenuItem("Export Data...");
        JMenuItem importItem = new JMenuItem("Import Data...");
        JMenuItem deleteAllItem = new JMenuItem("Delete ALL Data");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(loadDataItem);
        fileMenu.add(exportItem);
        fileMenu.add(importItem);
        fileMenu.addSeparator();
        fileMenu.add(deleteAllItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        add(fileMenu);

        // 🔹 Dodavanje DataHandle klase
        dataHandle = new DataHandle();

        // 🔹 Postavljanje akcija dugmadi
        loadDataItem.addActionListener(e -> loadDataFromDatabase());
        deleteAllItem.addActionListener(e -> {
            if (controller != null) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete ALL data?", "Warning", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DatabaseConnection.deleteAllData();
                    controller.getExpencesModel().clearExpenses();
                    controller.getLeftPanel().clearUsers();
                    controller.getRightPanel().refreshTable();
                    controller.getRightPanel().setTotalDebtsAreaText("");
                }
            }
        });

        exportItem.addActionListener(e -> dataHandle.exportData()); // 🟢 Implementacija EXPORTA
        importItem.addActionListener(e -> dataHandle.handleImport()); // 🟢 Implementacija IMPORTA

        exitItem.addActionListener(e -> System.exit(0));
    }

    public void setController(Controller controller) {
        this.controller = controller;
        dataHandle.setController(controller); // 🟢 Povezujemo DataHandle s kontrolerom
    }

    /**
     * Učitava podatke iz baze i prikazuje ih.
     */
    private void loadDataFromDatabase() {
        if (controller == null) return;

        // 1️⃣ Očisti postojeće podatke u modelima
        controller.getExpencesModel().clearExpenses();
        controller.getLeftPanel().clearUsers(); // 🔹 Brišemo stare korisnike
        controller.getRightPanel().refreshTable();

        // 2️⃣ Učitavanje korisnika iz baze i dodavanje u LeftPanel
        List<String> dbUsers = DatabaseConnection.getAllUsers();
        for (String user : dbUsers) {
            controller.getLeftPanel().addUser(user); // 🔹 Dodajemo korisnike u aplikaciju
        }

        // 3️⃣ Učitavanje troškova iz baze
        ResultSet rsExpenses = DatabaseConnection.getAllExpenses();

        try {
            while (rsExpenses.next()) {
                String name = rsExpenses.getString("name");
                String category = rsExpenses.getString("category");
                String paidBy = rsExpenses.getString("paid_by");
                java.sql.Date date = rsExpenses.getDate("date");
                double amount = rsExpenses.getDouble("amount");

                // Dohvati korisnike povezane s troškom
                int expenseId = rsExpenses.getInt("id");
                List<String> expenseUsers = DatabaseConnection.getUsersForExpense(expenseId);

                // Kreiraj trošak i dodaj ga u model
                Expense expense = new Expense(
                        name,
                        ExpenseCategory.valueOf(category.toUpperCase()),
                        paidBy,
                        amount,
                        new java.sql.Date(date.getTime()),
                        expenseUsers
                );
                controller.getExpencesModel().addExpense(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // 4️⃣ Ponovno prikazujemo sve troškove u GUI-ju
        controller.getRightPanelController().rebuildFromModel();
        controller.getRightPanel().refreshTable();

        JOptionPane.showMessageDialog(null,
                "Data successfully loaded from database!",
                "Load Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }


}
