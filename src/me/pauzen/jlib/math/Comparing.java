package me.pauzen.jlib.math;

public final class Comparing {

    private Comparing() {
    }

    public static int getLargest(Integer... ints) {
        int max = 0;
        for (int x : ints) max = Math.max(x, max);
        return max;
    }

}
