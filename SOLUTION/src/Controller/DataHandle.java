// Java
package Controller;

import View.MenuBarEvent;
import View.MenuBarListener;
import Model.Expense;
import Model.ExpenseCategory;

import javax.swing.*;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa DataHandle upravlja operacijama izvoza i uvoza podataka, kao i brisanjem svih podataka.
 *
 * @version 1.0
 */
public class DataHandle {

    private Controller controller;
    private MenuBarListener menuBarListener;

    /**
     * Obradjuje akciju brisanja svih podataka, tražeći potvrdu korisnika prije brisanja.
     */
    public void handleDeleteAllData() {
        int confirm = JOptionPane.showConfirmDialog(null, "Jeste li sigurni da želite obrisati SVE podatke?", "Upozorenje", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DataBaseController.deleteAllData(); //iz baze podataka
            controller.getExpencesModel().clearExpenses();
            controller.getRightPanel().refreshTable();
        }
    }

    /**
     * Obradjuje akciju izvoza podataka u odabrani format (CSV, TXT, BIN).
     */
    public void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Odaberite datoteku za izvoz");

        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV datoteke", "csv"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Tekstualne datoteke", "txt"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Binarne datoteke", "bin"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            String extension = "";
            String description = fileChooser.getFileFilter().getDescription();
            if (description.contains("CSV")) {
                extension = "csv";
            } else if (description.contains("Tekstualne")) {
                extension = "txt";
            } else if (description.contains("Binarne")) {
                extension = "bin";
            }

            if (!filePath.toLowerCase().endsWith("." + extension)) {
                selectedFile = new File(filePath + "." + extension);
            }

            try {
                if (extension.equals("bin")) {
                    exportBinaryData(selectedFile);
                } else {
                    exportTextData(selectedFile, extension.equals("csv"));
                }
                JOptionPane.showMessageDialog(null, "Podaci su uspješno izvezeni.", "Izvoz", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Greška pri izvozu podataka: " + ex.getMessage(), "Greška pri izvozu", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Pomoćna metoda za izvoz podataka u tekstualni format (CSV ili TXT).
     *
     * @param file datoteka u koju se podaci izvoze
     * @param isCsv boolean koji označava je li format CSV
     * @throws IOException ako dođe do greške pri pisanju u datoteku
     */
    private void exportTextData(File file, boolean isCsv) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String data = getExportData();
            if (!isCsv) {
                data = data.replace(",", "\t");
            }
            writer.write(data);
        }
    }

    /**
     * Pomoćna metoda za izvoz podataka u binarni format.
     *
     * @param file datoteka u koju se podaci izvoze
     * @throws IOException ako dođe do greške pri pisanju u datoteku
     */
    private void exportBinaryData(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(controller.getExpencesModel().getExpenses());
        }
    }

    /**
     * Generira podatke za izvoz u tekstualni format.
     *
     * @return string koji sadrži podatke za izvoz
     */
    private String getExportData() {
        StringBuilder data = new StringBuilder("Ime,Kategorija,Platio,Datum,Iznos,Raspodijeljeno,Obveze\n");
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
                    debts
            ));
        }

        return data.toString();
    }

    /**
     * Obradjuje akciju uvoza podataka iz odabrane datoteke.
     */
    public void handleImport() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder lineBuilder = new StringBuilder();
            boolean firstLine = true;

            String line;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Preskoči zaglavlje
                }

                lineBuilder.append(line);
                if (line.endsWith("\"")) {
                    String fullLine = lineBuilder.toString();
                    lineBuilder.setLength(0); // Clear the builder

                    String[] data = fullLine.split(",", -1);
                    if (data.length < 7) {
                        System.out.println("Preskačem nevažeći redak: " + fullLine);
                        continue; // Preskoči nevažeće retke
                    }

                    try {
                        String name = data[0].trim();
                        ExpenseCategory category = ExpenseCategory.valueOf(data[1].trim().toUpperCase());
                        String paidBy = data[2].trim();
                        Date date = Date.valueOf(data[3].trim());
                        double amount = Double.parseDouble(data[4].trim().replace(",", "."));
                        List<String> users = Arrays.asList(data[5].trim().split(";"));
                        List<String> debtUsers = new ArrayList<>();
                        List<Double> debtAmounts = new ArrayList<>();

                        if (!data[6].trim().isEmpty()) {
                            String[] debts = data[6].trim().split("; ");
                            for (String debt : debts) {
                                String[] debtInfo = debt.split(" duguje ");
                                if (debtInfo.length == 2) {
                                    debtUsers.add(debtInfo[0]);
                                    String[] amountInfo = debtInfo[1].split(" ");
                                    debtAmounts.add(Double.parseDouble(amountInfo[0]));
                                }
                            }
                        }

                        Expense expense = new Expense(name, category, paidBy, amount, date, users);
                        expense.setDebts(debtUsers, debtAmounts);

                        controller.getExpencesModel().addExpense(expense);
                        controller.getRightPanel().addExpense(name, category.name(), paidBy, date, amount, users, expense.getDebts());

                        DataBaseController.saveExpenseToDatabase(
                                expense.getName(),
                                expense.getCategory().name(),
                                expense.getPaidBy(),
                                new Date(expense.getDate().getTime()),
                                expense.getAmount(),
                                expense.getUsers()
                        );

                    } catch (IllegalArgumentException e) {
                        System.out.println("Preskačem nevažeći redak zbog greške u formatu: " + fullLine);
                    }
                }
            }

            controller.getRightPanel().refreshTable();
            JOptionPane.showMessageDialog(null, "Podaci su uspješno uvezeni.", "Uvoz", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Greška pri uvozu podataka: " + e.getMessage(), "Greška pri uvozu", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Postavlja kontroler.
     *
     * @param controller kontroler
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Postavlja MenuBarListener.
     *
     * @param menuBarListener MenuBarListener
     */
    public void setMenuBarListener(MenuBarListener menuBarListener) {
        this.menuBarListener = menuBarListener;
    }
}