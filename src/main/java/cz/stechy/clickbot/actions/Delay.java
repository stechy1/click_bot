package cz.stechy.clickbot.actions;

import cz.stechy.clickbot.IRobotController;
import java.util.function.Consumer;

public class Delay implements Consumer<IRobotController> {

    private final int delay;

    public Delay(int delay) {
        this.delay = delay;
    }

    @Override
    public void accept(IRobotController robot) {
        robot.pause(delay);
    }

    @Override
    public String toString() {
        return String.format("Delay: %d ms", delay);
    }
}
