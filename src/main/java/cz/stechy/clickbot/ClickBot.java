package cz.stechy.clickbot;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ClickBot {

    public static void main(String[] args) throws Exception {
        if (args.length < 1 || args.length > 2) {
            System.out.println("Aplikaci lze spustit s jedním, nebo dvěma parametry");
            System.out.println("===================================================");
            System.out.println("\t 1. cesta k souboru s konstantama");
            System.out.println("\t 2. cesta k souboru s akcema");
            System.out.println("===================================================");
            System.out.println("Cesta k souboru není povinná.");
            System.exit(1);
        }

        int paramIndex = 0;

        final IConstantParser constantParser;
        if (args.length == 1) {
            constantParser = new EmptyConstantParser();
        } else {
            constantParser = new SimpleConstantParser(
                new FileInputStream(args[paramIndex++]));
        }

        final Map<String, Point> pointMap = constantParser.parse();
        final IConstantProvider constantProvider = pointMap::get;
        final IActionParser parser = new SimpleActionParser(
            new FileInputStream(args[paramIndex++]), constantProvider);
        final List<Consumer<IRobotController>> actions = parser.parse();
        final IRobotController robot = new BetterRobot();
        final IActionPlayer player = new RobotPlayer(robot, actions, parser.getIterationCount());
        robot.pause(parser.getDelay());
        player.play();
    }
}
