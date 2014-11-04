package me.pauzen.jlib.misc;

import java.util.AbstractMap;
import java.util.Map;

public class Entry<K, V> extends AbstractMap.SimpleEntry<K, V> {

    /**
     * Basic AbstractMap.SimpleEntry wrapper to reduce amount needing to be typed.
     */

    public Entry(K key, V value) {
        super(key, value);
    }

    public Entry(Map.Entry<K, V> entry) {
        super(entry);
    }
}
