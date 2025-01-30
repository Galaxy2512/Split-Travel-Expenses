package View;

/**
 * Interface MenuBarListener predstavlja interfejs za osluskivace meni bara.
 * Metoda menuBarEventOccurred se poziva kada se desi dogadjaj na meni baru.
 *
 * @see MenuBarEvent
 *
 *
 * @version 1.0
 *
 */

public interface MenuBarListener {
    void menuBarEventOccurred(MenuBarEvent event);
}