package Model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa Expense predstavlja trošak.
 * Trošak sadrži naziv, kategoriju, osobu koja je platila, iznos, datum i korisnike među kojima je trošak podijeljen.
 * Također sadrži korisnike koji duguju i iznose dugova.
 *
 * @version 1.0
 */
public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private ExpenseCategory category;
    private String paidBy;
    private double amount;
    private Date date;
    private List<String> users;
    private List<String> debtUsers = new ArrayList<>();
    private List<Double> debtAmounts = new ArrayList<>();

    /**
     * Konstruktor koji stvara novi trošak sa specificiranim nazivom, kategorijom, osobom koja je platila, iznosom, datumom i korisnicima.
     *
     * @param name naziv troška
     * @param category kategorija troška
     * @param paidBy osoba koja je platila
     * @param amount iznos troška
     * @param date datum troška
     * @param users korisnici među kojima je trošak podijeljen
     */
    public Expense(String name, ExpenseCategory category, String paidBy, double amount, Date date, List<String> users) {
        this.name = name;
        this.category = category;
        this.paidBy = paidBy;
        this.amount = amount;
        this.date = date;
        this.users = users;
    }

    /**
     * Postavlja dugove za trošak.
     *
     * @param debtUsers korisnici koji duguju
     * @param debtAmounts iznosi dugova
     */
    public void setDebts(List<String> debtUsers, List<Double> debtAmounts) {
        this.debtUsers = debtUsers;
        this.debtAmounts = debtAmounts;
    }

    /**
     * Vraća dugove kao string.
     *
     * @return string koji predstavlja dugove
     */
    public String getDebts() {
        StringBuilder debts = new StringBuilder();
        for (int i = 0; i < debtUsers.size(); i++) {
            debts.append(debtUsers.get(i))
                    .append(" duguje ")
                    .append(paidBy)
                    .append(" ")
                    .append(String.format("%.2f", debtAmounts.get(i)))
                    .append(" eura;\n");
        }
        return debts.toString().trim();
    }

    // Getter metode
    public String getName() {
        return name;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public List<String> getUsers() {
        return users;
    }

    public List<String> getSplitBetween() {
        return users;
    }

    /**
     * Stvara instancu troška.
     *
     * @param name naziv troška
     * @param category kategorija troška
     * @param amount iznos troška
     * @param date datum troška
     * @param paidBy osoba koja je platila
     * @param users korisnici među kojima je trošak podijeljen
     * @return nova instanca troška
     */
    public static Expense createExpense(String name, ExpenseCategory category, double amount, Date date, String paidBy, List<String> users) {
        return new Expense(name, category, paidBy, amount, date, users);
    }
}