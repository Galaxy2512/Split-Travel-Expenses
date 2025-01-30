package Controller;

import View.MenuBarEvent;
import View.MenuBarListener;
import Model.DatabaseConnection;
import Model.Expense;
import Model.ExpenseCategory;

import javax.swing.*;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataHandle {

    private Controller controller;
    private MenuBarListener menuBarListener;

    public void handleDeleteAllData() {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ALL data?", "Warning", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseConnection.deleteAllData();
            controller.getExpencesModel().clearExpenses();
            controller.getRightPanel().refreshTable();
        }
    }

    public void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose export file");

        // Adding filters for different formats
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Binary Files", "bin"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            // Get the selected file filter description
            String extension = "";
            String description = fileChooser.getFileFilter().getDescription();
            if (description.contains("CSV")) {
                extension = "csv";
            } else if (description.contains("Text")) {
                extension = "txt";
            } else if (description.contains("Binary")) {
                extension = "bin";
            }

            // Ensure correct extension if missing
            if (!filePath.toLowerCase().endsWith("." + extension)) {
                selectedFile = new File(filePath + "." + extension);
            }

            try {
                if (extension.equals("bin")) {
                    exportBinaryData(selectedFile);
                } else {
                    exportTextData(selectedFile, extension.equals("csv"));
                }
                JOptionPane.showMessageDialog(null, "Data exported successfully.", "Export", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error exporting data: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportTextData(File file, boolean isCsv) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String data = getExportData();
            if (!isCsv) {
                data = data.replace(",", "\t");
            }
            writer.write(data);
        }
    }

    private void exportBinaryData(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            String data = getExportData();
            oos.writeObject(data);
        }
    }

    public void handleImport() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] data = line.split(",", -1);
                if (data.length < 6) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                try {
                    String name = data[0].trim();
                    ExpenseCategory category = ExpenseCategory.valueOf(data[1].trim().toUpperCase());
                    String paidBy = data[2].trim();
                    Date date = Date.valueOf(data[3].trim());
                    double amount = Double.parseDouble(data[4].trim().replace(",", "."));
                    List<String> users = Arrays.asList(data[5].trim().split(";"));

                    // Parsing debts
                    List<String> debtUsers = new ArrayList<>();
                    List<Double> debtAmounts = new ArrayList<>();
                    if (data.length > 6 && !data[6].trim().equalsIgnoreCase("null")) {
                        String[] debtEntries = data[6].split(";");
                        for (String entry : debtEntries) {
                            entry = entry.trim();
                            if (!entry.isEmpty()) {
                                String[] parts = entry.split(" owes ");
                                if (parts.length == 2) {
                                    String debtor = parts[0].trim();
                                    String[] amountParts = parts[1].split(" ");
                                    if (amountParts.length == 2) {
                                        debtUsers.add(debtor);
                                        debtAmounts.add(Double.parseDouble(amountParts[0]));
                                    }
                                }
                            }
                        }
                    }

                    // Creating and adding expense
                    Expense expense = new Expense(name, category, paidBy, amount, date, users).createExpense(name, category, amount, date, paidBy, users);
                    expense.setDebts(debtUsers, debtAmounts);
                    controller.getExpencesModel().addExpense(expense);
                    controller.getRightPanel().addExpense(name, category.name(), paidBy, date, amount, users);

                    DatabaseConnection.saveExpenseToDatabase(expense);

                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid line due to format error: " + line);
                }
            }

            controller.getRightPanel().refreshTable();
            JOptionPane.showMessageDialog(null, "Data imported successfully.", "Import", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error importing data: " + e.getMessage(), "Import Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getExportData() {
        StringBuilder data = new StringBuilder("Name,Category,Paid By,Date,Amount,Split Between,Debts\n");
        for (Expense expense : controller.getExpencesModel().getExpenses()) {
            String paidBy = (expense.getPaidBy() == null || expense.getPaidBy().isEmpty()) ? "N/A" : expense.getPaidBy();
            String debts = expense.getDebts();

            data.append(String.format("%s,%s,%s,%s,%.2f,%s,\"%s\"\n",
                    expense.getName(),
                    expense.getCategory().name(),
                    paidBy,
                    new Date(expense.getDate().getTime()),
                    expense.getAmount(),
                    String.join(";", expense.getUsers()),
                    debts.replace(";", ", ") // For better CSV format
            ));
        }

        return data.toString();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setMenuBarListener(MenuBarListener menuBarListener) {
        this.menuBarListener = menuBarListener;
    }


}