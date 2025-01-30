package Model;

/**
 * Klasa AccommodationExpenses predstavlja smjestajne troskove.
 * Smjestajni troskovi sadrze lokaciju i broj nocenja.
 * Nasljedjuje klasu Expense.
 * @see Expense
 * @version 1.0
 *
 *
 */

import java.util.Date;
import java.util.List;

import static Model.ExpenseCategory.SMJESTAJ;

public class AccommodationExpenses extends Expense {
    private String location;
    private int nights;

    public AccommodationExpenses(String name, String paidBy, double amount, Date date, List<String> users) {
        super(name, SMJESTAJ, paidBy, amount, date, users);
    }

    @Override
    public String getPaidBy() {
        return super.getPaidBy();
    }

    @Override
    public List<String> getUsers() {
        return super.getUsers();
    }

    @Override
    public String toString() {
        return String.format("Accommodation: %s at %s for %d nights", getName(), location, nights);
    }
}