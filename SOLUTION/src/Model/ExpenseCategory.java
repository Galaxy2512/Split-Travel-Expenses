package Model;

/**
 * Enumeracija ExpenseCategory predstavlja kategorije troskova.
 * Kategorije troskova su: KARTE, SMJESTAJ, HRANA, PICE, OTHER.
 * Kategorija OTHER se koristi kada se trosak ne moze svrstati u nijednu od prethodno navedenih kategorija.
 * ENUM je u Java-i posebna vrsta klase koja predstavlja skup konstanti.
 * @version 1.0
 *
 */

public enum ExpenseCategory {
    KARTE,
    SMJESTAJ,
    HRANA,
    PICE,
    OTHER
}