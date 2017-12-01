package cz.stechy.clickbot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Vylepšená verze třídy {@link Robot}, která rozšiřuje základní monosti.
 * <br>
 * Funkce:
 * <ul>
 *     <li>Kliknutí levého a pravého tlačítka myši</li>
 *     <li>Plynulý pohyb z bodu A do bodu B po přímce</li>
 *     <li>Napsat nějaký text</li>
 * </ul>
 */
public final class BetterRobot implements IRobotController {

    // region Constants

    // Čekání po stisku tlačítka myši
    private static final int DELAY_CLICK_BEFORE = 200;
    // Čekání po uvolnění tlačítka myši
    private static final int DELAY_CLICK_AFTER  = 1000;
    // Čekání po stisku klávesy
    private static final int DELAY_KEY_PRESS = 20;

    // endregion

    // region Variables

    // Instance robota, který ovládá klikání
    private Robot robot;
    // Poslední souřadnice myši
    private Point mousePosition;

    // endregion

    // region Constructors

    /**
     * Vytvoří nového klikacího robota
     *
     * @throws AWTException Pokud se něco zvrtá
     */
    public BetterRobot() throws AWTException {
        this.robot = new Robot();
    }

    // endregion

    // region Public methods

    @Override
    public void pause(int milis) {
        do {
            int modulo = milis % 60000;
            robot.delay(modulo);
            milis -= 60000;
        } while (milis % 60000 > 0);
    }

    @Override
    public void setMousePosition(Point point) {
        robot.mouseMove(point.x, point.y);
        mousePosition = point;
    }

    @Override
    public void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        pause(DELAY_CLICK_BEFORE);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        pause(DELAY_CLICK_AFTER);
    }

    @Override
    public void rightClick() {
        robot.mousePress(InputEvent.BUTTON3_MASK);
        pause(DELAY_CLICK_BEFORE);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
        pause(DELAY_CLICK_AFTER);
    }

    @Override
    public void smoothMove(Point point, int n) {
        final Point original = mousePosition;
        double dx = point.x / (double) n;
        double dy = point.y / (double) n;
        int dt = 100;
        for (int step = 0; step < n; step++) {
            pause(dt);
            setMousePosition(new Point((int) (mousePosition.x + dx * step), (int) (mousePosition.y + dy * step)));
        }

        assert mousePosition.x - point.x == original.x;
        assert mousePosition.y - point.y == original.y;
        pause(DELAY_CLICK_AFTER);
    }

    @Override
    public void write(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }

            robot.keyPress(Character.toUpperCase(c));
            pause(DELAY_KEY_PRESS);
            robot.keyRelease(Character.toUpperCase(c));

            if (Character.isUpperCase(c)) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        }
    }

    @Override
    public BufferedImage createScreenCapture(int x, int y, int width, int height) {
        return robot.createScreenCapture(new Rectangle(x, y, width, height));
    }

    // endregion

}
