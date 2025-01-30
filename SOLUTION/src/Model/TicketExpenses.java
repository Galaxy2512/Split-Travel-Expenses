package Model;

/**
 * Klasa TicketExpenses predstavlja troskove karata.
 * Nasljedjuje klasu Expense.
 * @see Expense
 *
 *
 * @version 1.0
 */

import java.util.Date;
import java.util.List;

import static Model.ExpenseCategory.KARTE;

public class TicketExpenses extends Expense {
    public TicketExpenses(String name, String paidBy, double amount, Date date, List<String> users) {
        super(name, KARTE, paidBy, amount, date, users);
    }

    @Override
    public ExpenseCategory getCategory() {
        return KARTE;
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