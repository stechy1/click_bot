package cz.stechy.clickbot;

import java.util.List;
import java.util.function.Consumer;

/**
 * Přehrávač příkazů
 */
public final class RobotPlayer implements IActionPlayer {

    // region Variables

    private final IRobotController robot;
    private final List<Consumer<IRobotController>> actions;
    private final int iterations;

    // endregion

    // region Constructors

    /**
     * Vytvoří nový přehrávač příkazů
     *
     * @param robot {@link IRobotController}
     * @param actions Seznam akcí, který se má vykonat
     * @param iterations Počet iterací
     */
    public RobotPlayer(IRobotController robot, List<Consumer<IRobotController>> actions,
        int iterations) {
        this.robot = robot;
        this.actions = actions;
        this.iterations = iterations;
    }

    // endregion

    // region Public methods

    /**
     * Spustí vykonávání poadovaných akcí
     */
    @Override
    public void play() {
        for (int i = 0; i < iterations; i++) {
            System.out.println("Iteration: " + (i+1));
            actions.forEach(action -> {
                System.out.println(action);
                action.accept(robot);
            });
        }
    }

    // endregion
}
