package me.pauzen.jlib;

public class Capacitor {

    public boolean state = false;

    public void flip() {
        state = !state;
    }

    public boolean getState() {
        return state;
    }

}
