package Controller;

import Model.ExpencesModel;
import Model.Expense;
import View.RightPanel;

import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * Upravljanje desnim panelom i kalkulacija dugova.
 */
public class RightPanelController {
    private RightPanel rightPanel;
    private ExpencesModel expencesModel; // 🟢 KLJUČNO: referenca na model
    private Map<String, Map<String, Double>> totalDebts;
    private Map<String, Double> totalExpenses;
    private Set<String> uniqueExpenses;

    public RightPanelController(RightPanel rightPanel, ExpencesModel expencesModel) {
        this.rightPanel = rightPanel;
        this.expencesModel = expencesModel; // pohranjujemo
        this.rightPanel.setController(this);

        this.totalDebts = new HashMap<>();
        this.totalExpenses = new HashMap<>();
        this.uniqueExpenses = new HashSet<>();
    }

    /**
     * Ova metoda briše sve u GUI-ju i kreira prikaz iz ExpencesModel-a
     * (stari i novi troškovi zajedno).
     */
    public void rebuildFromModel() {
        // 1️⃣ Očisti stare podatke
        clearTableData(); // briše tablice i resetira mape

        // 2️⃣ Za svaki trošak iz modela, pozovi addExpense
        for (Expense expense : expencesModel.getExpenses()) {
            addExpense(
                    expense.getName(),
                    expense.getCategory().name(),
                    expense.getPaidBy(),
                    expense.getDate(),
                    expense.getAmount(),
                    expense.getUsers()
            );
        }

        // 3️⃣ Sada su i totalDebts i totalExpenses osvježeni kroz addExpense pozive
        updateTotalDebtsDisplay();
        updateUserTotalsArea();
    }

    public void addExpense(String name, String category, String paidBy,
                           java.util.Date date, double amount, List<String> users) {
        // Provjera za duplikat
        String expenseIdentifier = name + category + paidBy + date + amount + users.toString();
        if (uniqueExpenses.contains(expenseIdentifier)) {
            return;
        }
        uniqueExpenses.add(expenseIdentifier);

        // GUI - dodaj red
        String splitUsers = String.join(", ", users);
        String debts = calculateDebts(paidBy, amount, users);
        DefaultTableModel tableModel = rightPanel.getTableModel();
        tableModel.addRow(new Object[]{name, category, paidBy, date, amount, splitUsers, debts});

        // Ažuriraj mape
        updateTotalDebts(paidBy, amount, users);
        updateUserDebtsTable(paidBy, amount);
        updateTotalExpenses(paidBy, amount);

        // Ažuriraj GUI
        updateTotalDebtsDisplay();
        updateUserTotalsArea();
        rightPanel.userSummaryPanel.addUserExpense(paidBy, amount);
        rightPanel.refreshTable();
    }

    private String calculateDebts(String paidBy, double amount, List<String> users) {
        if (users.isEmpty() || (users.size() == 1 && users.contains(paidBy))) {
            return "";
        }
        double splitAmount = amount / users.size();
        StringBuilder debts = new StringBuilder();
        for (String user : users) {
            if (!user.equals(paidBy)) {
                debts.append(user)
                        .append(" duguje ")
                        .append(paidBy)
                        .append(" ")
                        .append(formatAmount(splitAmount))
                        .append(" eur; ");
            }
        }
        return debts.toString().trim();
    }

    private void updateTotalDebts(String paidBy, double amount, List<String> users) {
        int numUsers = users.size();
        double sharePerUser = amount / numUsers;
        for (String user : users) {
            if (!user.equals(paidBy)) {
                totalDebts.putIfAbsent(user, new HashMap<>());
                totalDebts.putIfAbsent(paidBy, new HashMap<>());

                double oldDebt1 = totalDebts.get(user).getOrDefault(paidBy, 0.0);
                double oldDebt2 = totalDebts.get(paidBy).getOrDefault(user, 0.0);

                // user duguje paidBy
                totalDebts.get(user).put(paidBy, oldDebt1 - sharePerUser);

                // paidBy duguje user (pozitivno) - suprotno
                totalDebts.get(paidBy).put(user, oldDebt2 + sharePerUser);
            }
        }
    }

    public void updateTotalDebtsDisplay() {
        StringBuilder debtsDisplay = new StringBuilder("Ukupni iznos dugova između korisnika:\n");
        for (Map.Entry<String, Map<String, Double>> entry : totalDebts.entrySet()) {
            String debtor = entry.getKey();
            for (Map.Entry<String, Double> subEntry : entry.getValue().entrySet()) {
                String creditor = subEntry.getKey();
                double amount = subEntry.getValue();
                if (amount < 0) {
                    debtsDisplay.append(debtor)
                            .append(" duguje ")
                            .append(creditor)
                            .append(" ")
                            .append(formatAmount(-amount))
                            .append(" eur.\n");
                }
            }
        }
        rightPanel.setTotalDebtsAreaText(debtsDisplay.toString().trim());
    }

    private void updateUserDebtsTable(String user, double amount) {
        DefaultTableModel userDebtsTableModel = rightPanel.getUserAmountTableModel();
        userDebtsTableModel.addRow(new Object[]{user, amount});
    }

    private void updateTotalExpenses(String user, double amount) {
        totalExpenses.put(user, totalExpenses.getOrDefault(user, 0.0) + amount);
    }

    private void updateUserTotalsArea() {
        StringBuilder totalsDisplay = new StringBuilder("Ukupni iznos troškova između korisnika:\n");
        for (Map.Entry<String, Double> entry : totalExpenses.entrySet()) {
            String person = entry.getKey();
            double totalAmount = entry.getValue();
            totalsDisplay.append(person)
                    .append(" ")
                    .append(formatAmount(totalAmount))
                    .append(" eur.\n");
        }
        rightPanel.setUserTotalsAreaText(totalsDisplay.toString().trim());
    }

    public void clearTableData() {
        DefaultTableModel tableModel = rightPanel.getTableModel();
        tableModel.setRowCount(0);

        DefaultTableModel userAmountTableModel = rightPanel.getUserAmountTableModel();
        userAmountTableModel.setRowCount(0);

        rightPanel.userSummaryPanel.clearUserExpenses();

        totalDebts.clear();
        totalExpenses.clear();
        uniqueExpenses.clear();

        updateTotalDebtsDisplay();
        updateUserTotalsArea();
    }

    private String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }
}