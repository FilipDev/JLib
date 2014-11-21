package me.pauzen.jlib.misc.threading;

public abstract class ArgRunnable implements Runnable {

    public abstract Object run(Object... args);

    @Override
    public void run() {
        run(null);
    }

}
