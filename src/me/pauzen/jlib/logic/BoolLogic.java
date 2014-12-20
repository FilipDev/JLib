package me.pauzen.jlib.logic;

public final class BoolLogic {

    private BoolLogic() {
    }

    public static boolean XOR(boolean a, boolean b) {
        return a != b;
    }

    public static boolean OR(boolean a, boolean b) {
        return a || b;
    }

    public static boolean NOR(boolean a, boolean b) {
        return !a || !b;
    }

    public static boolean NOT(boolean a) {
        return !a;
    }

    public static boolean AND(boolean a, boolean b) {
        return a && b;
    }

    public static boolean NAND(boolean a, boolean b) {
        return !a && !b;
    }

    public static boolean XNOR(boolean a, boolean b) {
        return a == b;
    }
}
