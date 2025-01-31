package Controller;

import View.RightPanel;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RightPanelController {
    private RightPanel rightPanel;
    private Map<String, Double> totalDebts;

    public RightPanelController(RightPanel rightPanel) {
        this.rightPanel = rightPanel;
        this.rightPanel.setController(this);
        this.totalDebts = new HashMap<>();
    }

    public void addExpense(String name, String category, String paidBy, Date date, double amount, List<String> users) {
        String splitUsers = String.join(", ", users);
        String debts = calculateDebts(paidBy, amount, users);

        DefaultTableModel tableModel = rightPanel.getTableModel();
        tableModel.addRow(new Object[]{name, category, paidBy, date, amount, splitUsers, debts});

        updateTotalDebts(paidBy, amount, users);
        updateTotalDebtsDisplay();
        rightPanel.refreshTable();
    }

    public String calculateDebts(String paidBy, double amount, List<String> users) {
        if (users.isEmpty() || (users.size() == 1 && users.contains(paidBy))) {
            return "";
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

    public void updateTotalDebts(String paidBy, double amount, List<String> users) {
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

            if (balance < 0) {
                debtsDisplay.append(person)
                        .append(" needs to pay ")
                        .append(formatAmount(-balance))
                        .append(" eur.\n");
            } else if (balance > 0) {
                debtsDisplay.append(person)
                        .append(" should receive ")
                        .append(formatAmount(balance))
                        .append(" eur.\n");
            } else {
                debtsDisplay.append(person)
                        .append(" is settled (no debt).\n");
            }
        }

        rightPanel.setTotalDebtsAreaText(debtsDisplay.toString().trim());
    }

    private String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }

    public void clearTableData() {
        DefaultTableModel tableModel = rightPanel.getTableModel();
        tableModel.setRowCount(0);
        totalDebts.clear();
        updateTotalDebtsDisplay();
    }
}