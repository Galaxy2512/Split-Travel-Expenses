package Model;


/**
 * Klasa ExpencesObservable predstavlja interfejs za promatranje troskova.
 * Metoda addObserver dodaje promatraca troskova.
 * Metoda notifyObservers obavjestava promatrace o promjeni troskova.
 *
 *
 * @see ExpencesObserver
 *
 * @version 1.0
 */
public interface ExpencesObservable {
    void addObserver(ExpencesObserver observer);
    void notifyObservers();
}