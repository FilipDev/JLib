package me.pauzen.jlib.random;

import java.util.Random;

public final class RandomProvider {

    private static final Random random = new Random();

    /**
     * Prevents initialization.
     */
    private RandomProvider() {
    }

    /**
     * Provides a static instance of a Random object.
     *
     * @return Static instance of Random object.
     */
    public static Random getRandom() {
        return random;
    }
}
