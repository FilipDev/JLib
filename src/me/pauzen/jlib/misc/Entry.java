package me.pauzen.jlib.misc;

import java.util.AbstractMap;
import java.util.Map;

public class Entry<K, V> extends AbstractMap.SimpleEntry<K, V> {

    /**
     * Simple AbstractMap.SimpleEntry wrapper.
     */

    public Entry(K key, V value) {
        super(key, value);
    }

    public Entry(Map.Entry<K, V> entry) {
        super(entry);
    }
}
