package me.pauzen.jlib.misc.threading;

import java.util.List;

public final class MultiThreading {

    ArgRunnable argRunnable;

    public MultiThreading(ArgRunnable argRunnable) {
        this.argRunnable = argRunnable;
    }

    public ArgRunnable getArgRunnable() {
        return argRunnable;
    }

    public void setArgRunnable(ArgRunnable argRunnable) {
        this.argRunnable = argRunnable;
    }

    public List<Object> start() {
        return null;
    }
}
