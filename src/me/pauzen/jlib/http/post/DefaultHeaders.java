package me.pauzen.jlib.http.post;

import me.pauzen.jlib.misc.Entry;

import java.util.Map;

public enum DefaultHeaders {

    URLENCODED(new Entry<>("Content-Type", "application/x-www-form-urlencoded")),
    USERAGENT(new Entry<>("User-Agent", "Mozilla/5.0"));

    private Map.Entry type;

    DefaultHeaders(Map.Entry type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type.getKey() + ": " + this.type.getValue();
    }

}
