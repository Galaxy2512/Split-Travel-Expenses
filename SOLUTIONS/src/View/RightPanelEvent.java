package View;

import java.util.EventObject;

/**
 * Klasa RightPanelEvent predstavlja dogadjaj koji se desava u RightPanel-u.
 * Dogadjaj sadrzi korisnika i njegovu trenutnu kolicinu novca.
 *
 * @version 1.0
 *
 *
 */

public class RightPanelEvent extends EventObject {
    private String user;
    private double balance;

    public RightPanelEvent(Object source, String user, double balance) {
        super(source);
        this.user = user;
        this.balance = balance;
    }

    public String getUser() {
        return user;
    }

    public double getBalance() {
        return balance;
    }
}