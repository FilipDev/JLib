package me.pauzen.jlib.http.cookie;

import me.pauzen.jlib.misc.Entry;

import java.util.HashMap;
import java.util.Map;

public class Cookie {

    private Map<String, String> values = new HashMap<>();
    private String name;
    private String val;

    public Cookie(Entry<String, String> entry) {
        registerCookie(entry.getValue());
    }

    public Cookie(String value) {
        registerCookie(value);
    }

    public Cookie() {
    }

    public String getContent() {
        return this.values.get(name);
    }

    public Cookie setContent(String value) {
        this.values.put(name, value);
        return this;
    }

    public Cookie addValue(String key, String value) {
        this.values.put(key, value);
        return this;
    }

    public Cookie addValue(String keyVal) {
        String[] strings = keyVal.split("=");
        if (strings.length == 1) this.values.put(strings[0], null);
        else this.values.put(strings[0], strings[1]);
        return this;
    }

    public void registerCookie(String string) {
        this.val = string;
        String[] strings = string.split("; ");
        setName1(strings[0]);
        for (String string1 : strings) addValue(string1);
    }

    private void setName1(String string) {
        String[] strings = string.split("=");
        this.name = strings[0];
    }

    public String getName() {
        return name;
    }

    public Cookie setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, String> getValues() {
        return this.values;
    }

    @Override
    public String toString() {
        return val;
    }

}
