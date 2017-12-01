package cz.stechy.clickbot;

import java.util.HashMap;
import java.util.Map;

public class EmptyConstantParser implements IConstantParser {

    @Override
    public Map<String, Object> parse() throws Exception {
        return new HashMap<>();
    }
}
