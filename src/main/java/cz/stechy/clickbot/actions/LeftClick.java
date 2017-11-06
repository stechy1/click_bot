package cz.stechy.clickbot.actions;

import cz.stechy.clickbot.IRobotController;
import cz.stechy.clickbot.Point;
import java.util.function.Consumer;

public class LeftClick implements Consumer<IRobotController> {

    private final Point point;

    public LeftClick(Point point) {
        this.point = point;
    }

    @Override
    public void accept(IRobotController robot) {
        if (point.isMinusOne()) {
            robot.leftClick();
        } else {
            robot.leftClick(point);
        }
    }

    @Override
    public String toString() {
        return String.format("LeftClick: {%s}", point.toString());
    }
}
