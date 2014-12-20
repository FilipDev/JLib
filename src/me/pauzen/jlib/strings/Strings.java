package me.pauzen.jlib.strings;

import me.pauzen.jlib.misc.Testing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class Strings {

    private Strings() {
    }

    public static boolean equalsAny(String string, String... strings) {
        return Testing.equalsAny(string, strings) != null;
    }

    public static boolean equalsAnyIgnoreCase(String string, String... strings) {
        for (String s : strings)
            if (string.equalsIgnoreCase(s)) return true;
        return false;
    }

    public static Set<String> match(String string, String... strings) {
        int x = 1;
        Set<String> matchingStrings = new HashSet<>();
        Collections.addAll(matchingStrings, strings);
        String currString = string.substring(0, x);
        while (true) {
            Set<String> matchingStrings1 = getMatching(currString, matchingStrings);
            if (!matchingStrings1.isEmpty()) matchingStrings = matchingStrings1;
            else break;
            currString = currString.substring(0, x);
            x++;
        }
        return matchingStrings;
    }

    public static Set<String> getMatching(String string, Set<String> strings) {
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext())
            if (!iterator.next().contains(string)) iterator.remove();
        return strings;
    }

}
