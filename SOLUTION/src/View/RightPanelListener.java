package View;


/**
 * Interface RightPanelListener predstavlja interfejs za osluskivace desne strane panela.
 * Metoda rightPanelEventOccurred se poziva kada se desi dogadjaj na desnoj strani panela.
 *
 * @see RightPanelEvent
 *
 * @version 1.0
 *
 */
public interface RightPanelListener {
    void rightPanelEventOccurred(RightPanelEvent event);
}
