package me.pauzen.jlib.misc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public final class Stack {

    private Stack() {
    }

    public static Map<String, Object> getStackVars(Thread thread) {
        thread.countStackFrames();
        return null;
    }

    public static String stackTraceCurrentThread() {
        return stackTraceThread(Thread.currentThread());
    }

    public static String stackTraceThread(Thread thread) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement element : thread.getStackTrace()) {
            stringBuilder.append(element.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public static String[] stackTraceAllActiveThreads() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<Thread> threadClass = Thread.class;
        Method getThreads = threadClass.getDeclaredMethod("getThreads");
        getThreads.setAccessible(true);
        Thread[] threads = (Thread[]) getThreads.invoke(null);
        ArrayList<String> stackTraces = new ArrayList<>(threads.length);
        for (Thread thread : threads) stackTraces.add(stackTraceThread(thread));
        return stackTraces.toArray(new String[threads.length]);
    }

}
