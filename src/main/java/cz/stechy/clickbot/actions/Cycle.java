package cz.stechy.clickbot.actions;

import cz.stechy.clickbot.IRobotController;
import java.util.List;
import java.util.function.Consumer;

public class Cycle implements Consumer<IRobotController> {

    private final int iterations;
    private final List<Consumer<IRobotController>> actions;

    public Cycle(int iterations, List<Consumer<IRobotController>> actions) {
        this.iterations = iterations;
        this.actions = actions;
    }

    @Override
    public void accept(IRobotController robot) {
        for (int i = 0; i < iterations; i++) {
            System.out.println("Iteration: " + (i+1));
            actions.forEach(action -> {
//                System.out.println(action);
//                if (action instanceof Cycle) {
//                    action.accept(robot);
//                }
                action.accept(robot);
            });
        }
    }

    @Override
    public String toString() {
        return "Cycle " + iterations;
    }
}
