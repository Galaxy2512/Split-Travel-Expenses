package View;

import java.util.Date;
import java.util.List;

/**
 * The LeftPanelEvent klasa predstavlja događaj koji se javlja kada korisnik unese podatke o trošku.
 * Dogadjaj sadrzi sve podatke o trošku.
 *
 * @version 1.0
 */
public class LeftPanelEvent {
    private String name;
    private String category;
    private String paidBy;
    private Date date;
    private double amount;
    private List<String> users;
    private List<String> debtUsers;
    private List<Double> debtAmounts;

    /**
     * Konstruktor koji stvara novi LeftPanelEvent sa specificiranim podacima o trošku.
     *
     * @param name the name of the expense
     * @param category the category of the expense
     * @param paidBy the person who paid for the expense
     * @param date the date of the expense
     * @param amount the amount of the expense
     * @param users the users involved in the expense
     * @param debtUsers the users who owe money
     * @param debtAmounts the amounts owed by each user
     */
    public LeftPanelEvent(String name, String category, String paidBy, Date date, double amount, List<String> users, List<String> debtUsers, List<Double> debtAmounts) {
        this.name = name;
        this.category = category;
        this.paidBy = paidBy;
        this.date = date;
        this.amount = amount;
        this.users = users;
        this.debtUsers = debtUsers;
        this.debtAmounts = debtAmounts;
    }

    /**
     * Returns the name of the expense.
     *
     * @return the name of the expense
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the category of the expense.
     *
     * @return the category of the expense
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the person who paid for the expense.
     *
     * @return the person who paid for the expense
     */
    public String getPaidBy() {
        return paidBy;
    }

    /**
     * Returns the date of the expense.
     *
     * @return the date of the expense
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the amount of the expense.
     *
     * @return the amount of the expense
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the users involved in the expense.
     *
     * @return the users involved in the expense
     */
    public List<String> getUsers() {
        return users;
    }

    /**
     * Returns the users who owe money.
     *
     * @return the users who owe money
     */
    public List<String> getDebtUsers() {
        return debtUsers;
    }

    /**
     * Returns the amounts owed by each user.
     *
     * @return the amounts owed by each user
     */
    public List<Double> getDebtAmounts() {
        return debtAmounts;
    }
}