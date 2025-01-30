package Model;


/**
 * Klasa Expense predstavlja trosak.
 * Trosak sadrzi naziv, kategoriju, osobu koja je platila trosak, datum, iznos, listu korisnika i dugove.
 *
 * @see ExpenseCategory
 * @see AccommodationExpenses
 * @see DrinkExpenses
 * @see FoodExpenses
 * @see TicketExpenses
 * @see ExpenseCategory
 *
 * @version 1.0
 *
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Expense {
    private String name;
    private ExpenseCategory category;
    private String paidBy;
    private Date date;
    private double amount;
    private List<String> users;
    private String debts;

    public Expense(String name, ExpenseCategory category, String paidBy, double amount, Date date, List<String> users) {
        this.name = name;
        this.category = category;
        this.paidBy = (paidBy == null || paidBy.isEmpty()) ? "Unknown" : paidBy;
        this.amount = amount;
        this.date = date;
        this.users = users;
    }

    public Expense createExpense(String name, ExpenseCategory category, double amount, Date date, String paidBy, List<String> users) {
        Expense expense;
        switch (category) {
            case KARTE:
                expense = new TicketExpenses(name, paidBy, amount, date, users);
                break;
            case SMJESTAJ:
                expense = new AccommodationExpenses(name, paidBy, amount, date, users);
                break;
            case HRANA:
                expense = new FoodExpenses(name, paidBy, amount, date, users);
                break;
            case PICE:
                expense = new DrinkExpenses(name, paidBy, amount, date, users);
                break;
            default:
                expense = new Expense(name, ExpenseCategory.OTHER, paidBy, amount, date, users);
                break;
        }
        return expense;
    }

    public void setDebts(List<String> debtUsers, List<Double> debtAmounts) {
        if (debtUsers.isEmpty() || debtAmounts.isEmpty()) {
            debtUsers = new ArrayList<>();
            debtAmounts = new ArrayList<>();
            double perUserDebt = amount / users.size();
            for (String user : users) {
                if (!user.equals(paidBy)) {
                    debtUsers.add(user);
                    debtAmounts.add(perUserDebt);
                }
            }
        }

        StringBuilder debtString = new StringBuilder();
        for (int i = 0; i < debtUsers.size(); i++) {
            debtString.append(debtUsers.get(i))
                    .append(" owes ")
                    .append(paidBy)
                    .append(" ")
                    .append(String.format("%.2f", debtAmounts.get(i)))
                    .append(" euros; ");
        }
        this.debts = debtString.toString().trim();
    }

    public String getName() {
        return name;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public List<String> getUsers() {
        return users;
    }

    public String getDebts() {
        if (debts == null) {
            setDebts(new ArrayList<>(), new ArrayList<>());
        }
        return debts;
    }

    public CharSequence getSplitBetween() {
        return String.join(", ", users);
    }
}