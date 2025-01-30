package Model;

/**
 * Klasa FoodExpenses predstavlja troskove hrane.
 * Nasljedjuje klasu Expense.
 * @see Expense
 *
 *
 * @version 1.0
 */

import java.util.Date;
import java.util.List;

public class FoodExpenses extends Expense {
    private String restaurant;

    public FoodExpenses(String name, String paidBy, double amount, Date date, List<String> users) {
        super(name, ExpenseCategory.HRANA, paidBy, amount, date, users);
    }

    @Override
    public String getPaidBy() {
        return super.getPaidBy();  // Inherit from parent class
    }

    @Override
    public List<String> getUsers() {
        return super.getUsers();  // Inherit from parent class
    }
}