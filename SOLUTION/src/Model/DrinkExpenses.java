package Model;

/**
 * Klasa DrinkExpenses predstavlja troskove pica.
 * Nasljedjuje klasu Expense.
 * @see Expense
 *
 *
 * @version 1.0
 *
 *
 */

import java.util.Date;
import java.util.List;

import static Model.ExpenseCategory.PICE;

public class DrinkExpenses extends Expense {
    private String type;

    public DrinkExpenses(String name, String paidBy, double amount, Date date, List<String> users) {
        super(name, PICE, paidBy, amount, date, users);
    }

    @Override
    public String getPaidBy() {
        return super.getPaidBy();
    }

    @Override
    public List<String> getUsers() {
        return super.getUsers();
    }
}