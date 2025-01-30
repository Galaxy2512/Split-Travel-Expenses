package View;


/**
 * Klasa MenuBarEvent predstavlja dogadjaj koji se desava u MenuBar-u.
 * Dogadjaj sadrzi akcioni komandu.
 *
 * @version 1.0
 *
 * Method menuBarEventOccurred se poziva kada se desi dogadjaj na meni baru.
 *
 */
public class MenuBarEvent {

    private String actionCommand;

    public MenuBarEvent(String actionCommand) {
        this.actionCommand = actionCommand;
    }

    public String getActionCommand() {
        return actionCommand;
    }
}