package me.pauzen.jlib.date;

import java.util.HashMap;
import java.util.Map;

public enum Day {

    SUNDAY(0), MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6);

    private static Map<Integer, Day> dayMap = new HashMap<>();

    private int number;

    Day(int i) {
        this.number = i;
    }

    private static void addDay(int i, Day day) {
        dayMap.put(i, day);
    }

    public static Day valueOf(int day) {
        return Day.dayMap.get(day - 1);
    }

    public int getNumber() {
        return number;
    }

    static {
        for (Day day : Day.values())
            addDay(day.getNumber(), day);
    }

}
