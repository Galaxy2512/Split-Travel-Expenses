package Model;


/**
 * Klasa ExpencesObserver predstavlja interfejs za promatranje troskova.
 * Metoda updateExpenses obavjestava promatrace o promjeni troskova.
 *
 * @see ExpencesObservable
 *
 * @version 1.0
 */
public interface ExpencesObserver {
    void updateExpenses();
}