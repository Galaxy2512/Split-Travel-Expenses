package Model;

/**
 * Klasa User predstavlja korisnika.
 * Korisnik ima id, ime i stanje racuna.
 *
 * @version 1.0
 *
 *
 */

public class User {
    private int id;
    private String name;
    private double balance;

    public User(String name, double initialBalance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        this.name = name;
        this.balance = initialBalance;
    }


    public String getName() {
        return name;
    }



}
