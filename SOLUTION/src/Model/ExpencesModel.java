package Model;
/**
 *
 * Klasa ExpencesModel predstavlja model troskova.
 * Model troskova sadrzi listu troskova.

 * Metoda addExpense dodaje trosak u listu troskova.
 * Metoda getExpenses vraca listu troskova.
 *
 *
 *
 * @see Expense
 * @see ExpencesObservable
 * @see ExpencesObserver *
 * @version 1.0
 *
 */

import java.util.ArrayList;
import java.util.List;

public class ExpencesModel implements ExpencesObservable {

    private List<Expense> expenses;
    private List<ExpencesObserver> observers;

    public ExpencesModel() {
        this.expenses = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        notifyObservers();
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    @Override
    public void addObserver(ExpencesObserver observer) {
        observers.add(observer);
    }


    @Override
    public void notifyObservers() {
        for (ExpencesObserver observer : observers) {
            observer.updateExpenses();
        }
    }
}