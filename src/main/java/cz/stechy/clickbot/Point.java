package cz.stechy.clickbot;

/**
 * Jednoduchá přepravka obsahující X-ovou a Y-ovou souřadnici bodu
 */
public final class Point {

    public final int x, y;

    /**
     * Vytvoří nový bod na zadaných souřadnicích
     *
     * @param x X-ová souřadnice
     * @param y Y-ová souřadnice
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isMinusOne() {
        return x == -1 && y == -1;
    }

    @Override
    public String toString() {
        return String.format("x: %d, y: %d", x, y);
    }
}
