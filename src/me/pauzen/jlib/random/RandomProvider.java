package me.pauzen.jlib.random;

import java.util.Random;

public final class RandomProvider {

    private static final Random random = new Random();

    private RandomProvider() {
    }

    public static Random getRandom() {
        return random;
    }
}
