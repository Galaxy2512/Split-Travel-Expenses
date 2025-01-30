package View;

/**
 * Interface LeftPanelListener predstavlja interfejs za osluskivace leve strane panela.
 * Metoda leftPanelEventOccurred se poziva kada se desi dogadjaj na levoj strani panela.
 *
 * @see LeftPanelEvent
 *
 * @version 1.0
 *
 *
 */

public interface LeftPanelListener {
    void leftPanelEventOccurred(LeftPanelEvent event);
}
