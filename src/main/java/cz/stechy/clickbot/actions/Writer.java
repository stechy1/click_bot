package cz.stechy.clickbot.actions;

import cz.stechy.clickbot.IRobotController;
import java.util.function.Consumer;

public class Writer implements Consumer<IRobotController> {

    private final String s;

    public Writer(String s) {
        this.s = s;
    }

    @Override
    public void accept(IRobotController robot) {
        robot.write(s);
    }

    @Override
    public String toString() {
        return String.format("Write {%s}", s);
    }
}
