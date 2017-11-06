package cz.stechy.clickbot.actions;

import cz.stechy.clickbot.IRobotController;
import cz.stechy.clickbot.Point;
import java.util.function.Consumer;

public class RightClick implements Consumer<IRobotController> {

    private final Point point;

    public RightClick(Point point) {
        this.point = point;
    }

    @Override
    public void accept(IRobotController robot) {
        if (point.isMinusOne()) {
            robot.rightClick();
        } else {
            robot.rightClick(point);
        }
    }

    @Override
    public String toString() {
        return String.format("RightClick: {%s}", point.toString());
    }
}
