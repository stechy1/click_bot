package cz.stechy.clickbot;

import java.awt.image.BufferedImage;

public interface IRobotController {

    /**
     * Počká zadaný počet milisekund
     *
     * @param milis Jak dlouho se má čekat v milisekundích
     */
    void pause(int milis);

    /**
     * Přesune myš na zadané souřadnice
     *
     * @param coordinates Souřadnice myši
     */
    void setMousePosition(Point coordinates);

    /**
     * Klikne levým tlačítkem myši na aktuálních souřadnicích
     */
    void leftClick();

    /**
     * Klikne levým tlačitkem myši na zadaných souřadnicicí
     *
     * @param coordinates Souřadnice myši
     */
    default void leftClick(Point coordinates) {
        setMousePosition(coordinates);
        leftClick();
    }

    /**
     * Klikne pravým tlačítkem myši na aktuálních souřadnicích
     */
    void rightClick();

    /**
     * Klikne pravým tlačitkem myši na zadaných souřadnicicí
     *
     * @param coordinates Souřadnice myši
     */
    default void rightClick(Point coordinates) {
        setMousePosition(coordinates);
        rightClick();
    }

    /**
     * Plynule přesune kurzor na zadanou pozici v n krocích
     *
     * @param coordinates Cílová souřadnice myši
     * @param n Počet kroků
     */
    void smoothMove(Point coordinates, int n);

    /**
     * Zapíše text na obrazovku
     *
     * @param s Text, který se má zapsat
     */
    void write(String s);

    /**
     * Vytvoří screenshot obrazovky
     *
     * @param x X-ová souřadnice levého horního rohu
     * @param y Y-ová souřadnice levého horního rohu
     * @param width Šířka screenshotu
     * @param height Výška screenshotu
     * @return {@link BufferedImage}
     */
    BufferedImage createScreenCapture(int x, int y, int width, int height);
}
