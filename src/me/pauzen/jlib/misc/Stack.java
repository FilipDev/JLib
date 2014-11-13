package me.pauzen.jlib.misc;

public final class Stack {

    private Stack() {
    }

    public static String stackTrace() {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            stringBuilder.append(element.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
