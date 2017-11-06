package cz.stechy.clickbot;

import java.util.List;
import java.util.function.Consumer;

public interface IActionParser {

    /**
     * Naparsuje celý dokument
     *
     * @return {@link List<Consumer<IRobotController}
     * @throws Exception
     */
    List<Consumer<IRobotController>> parse() throws Exception;

    /**
     * Vrátí zpoždění před zahájením klikání
     *
     * @return {@link Integer}
     */
    int getDelay();

    /**
     * Vrátí počet opakovacích cyklů
     *
     * @return {@link Integer}
     */
    int getIterationCount();

}
