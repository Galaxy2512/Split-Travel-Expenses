package View;

import Controller.Controller;
import Model.ExpencesObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RightPanel klasa predstavlja panel na kojem se prikazuju troskovi.
 * Panel sadrzi tabelu sa troskovima i dugovanjima.
 * Panel sadrzi dugme za brisanje troskova.
 * Panel implementira interfejse ExpencesObserver i RightPanelListener.
 * Metode updateExpenses i rightPanelEventOccurred azuriraju troskove.
 * Metoda setController postavlja kontroler.
 * Metoda addExpense dodaje trosak.
 * Metoda calculateDebts racuna dugovanja.
 * Metoda updateTotalDebts azurira dugovanja.
 * Metoda updateTotalDebtsDisplay azurira dugovanja na panelu.
 * Metoda refreshTable osvezava tabelu.
 * Metoda clearTableData brise sve troskove.
 * Metoda formatAmount formatira iznos.
 * Metoda initializeComponents inicijalizuje komponente.
 * Metoda layoutComponents rasporedjuje komponente.
 * Metoda activateComponents aktivira komponente.
 *
 * @version 1.0
 *
 * @see ExpencesObserver
 * @see RightPanelListener
 * @see Controller
 *
 */

public class RightPanel extends JPanel implements ExpencesObserver, RightPanelListener {
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JTextArea totalDebtsArea;
    private Map<String, Double> totalDebts;
    private Controller controller;
    private JButton clearButton;

    public RightPanel() {
        setLayout(new BorderLayout());
        totalDebts = new HashMap<>();
        initializeComponents();
        layoutComponents();
        activateComponents();
    }

    private void initializeComponents() {
        tableModel = new DefaultTableModel(new Object[]{"Title of expense", "Category", "Paid By", "Date", "Amount", "Split Between", "Debts"}, 0);
        expenseTable = new JTable(tableModel);
        expenseTable.setPreferredScrollableViewportSize(new Dimension(500, 300));

        totalDebtsArea = new JTextArea();
        totalDebtsArea.setEditable(false);
        totalDebtsArea.setPreferredSize(new Dimension(500, 100));

        clearButton = new JButton("Clear");
    }

    private void layoutComponents() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JScrollPane(totalDebtsArea), BorderLayout.CENTER);
        bottomPanel.add(clearButton, BorderLayout.SOUTH);

        add(new JScrollPane(expenseTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void activateComponents() {
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTableData();
            }
        });
    }

    private void clearTableData() {
        tableModel.setRowCount(0);
        totalDebts.clear();
        updateTotalDebtsDisplay();
    }

    public void addExpense(String name, String category, String paidBy, Date date, double amount, List<String> users) {
        String splitUsers = String.join(", ", users);
        String debts = calculateDebts(paidBy, amount, users);

        // Debugging print statements
        System.out.println("Adding Expense - Name: " + name + ", Paid By: " + paidBy + ", Amount: " + amount + ", Debts: " + debts);

        tableModel.addRow(new Object[]{name, category, paidBy, date, amount, splitUsers, debts});

        updateTotalDebts(paidBy, amount, users);
        updateTotalDebtsDisplay( );
        refreshTable(); // Force UI update
    }


    public String calculateDebts(String paidBy, double amount, List<String> users) {
        if (users.isEmpty() || (users.size() == 1 && users.contains(paidBy))) {
            return ""; // No debts if only one person paid
        }

        double splitAmount = amount / users.size();
        StringBuilder debts = new StringBuilder();

        for (String user : users) {
            if (!user.equals(paidBy)) {
                debts.append(user)
                        .append(" owes ")
                        .append(paidBy)
                        .append(" ")
                        .append(String.format("%.2f", splitAmount))
                        .append(" euros; ");
            }
        }

        return debts.toString().trim();
    }



    private void updateTotalDebts(String paidBy, double amount, List<String> users) {
        int numUsers = users.size();
        double sharePerUser = amount / numUsers;
        for (String user : users) {
            if (!user.equals(paidBy)) {
                totalDebts.put(user, totalDebts.getOrDefault(user, 0.0) - sharePerUser);
                totalDebts.put(paidBy, totalDebts.getOrDefault(paidBy, 0.0) + sharePerUser);
            }
        }
    }

    public void updateTotalDebtsDisplay() {
        StringBuilder debtsDisplay = new StringBuilder("Total Debts:\n");

        for (Map.Entry<String, Double> entry : totalDebts.entrySet()) {
            String person = entry.getKey();
            double balance = entry.getValue();

            if (balance < 0) { // Person owes money
                debtsDisplay.append(person)
                        .append(" needs to pay ")
                        .append(formatAmount(-balance)) // Convert negative to positive for clarity
                        .append(" eur.\n");
            } else if (balance > 0) { // Person is owed money
                debtsDisplay.append(person)
                        .append(" should receive ")
                        .append(formatAmount(balance))
                        .append(" eur.\n");
            } else { // No balance
                debtsDisplay.append(person)
                        .append(" is settled (no debt).\n");
            }
        }

        totalDebtsArea.setText(debtsDisplay.toString().trim());
    }




    // Utility method to format numbers (e.g., round to 2 decimals)
    private String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public void updateExpenses() {
        updateTotalDebtsDisplay();
    }

    @Override
    public void rightPanelEventOccurred(RightPanelEvent event) {
        // Handle the event (if needed)
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void refreshTable() {
        tableModel.fireTableDataChanged();
    }
}