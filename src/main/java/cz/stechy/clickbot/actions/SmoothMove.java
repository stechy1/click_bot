package cz.stechy.clickbot.actions;

import cz.stechy.clickbot.IRobotController;
import cz.stechy.clickbot.Point;
import java.util.function.Consumer;

public class SmoothMove implements Consumer<IRobotController> {

    private final Point point;
    private final int n;

    public SmoothMove(Point point, int n) {
        this.point = point;
        this.n = n;
    }

    @Override
    public void accept(IRobotController robot) {
        robot.smoothMove(point, n);
    }

    @Override
    public String toString() {
        return String.format("SmoothMove {%s, n:%d}", point.toString(), n);
    }
}
