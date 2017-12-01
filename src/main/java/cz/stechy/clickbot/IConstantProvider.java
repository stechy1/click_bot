package cz.stechy.clickbot;

/**
 * Poskytovatel základních bodů na obrazovce pro obecné věci
 */
@FunctionalInterface
public interface IConstantProvider {

    <T> T valueOf(String name);

}
