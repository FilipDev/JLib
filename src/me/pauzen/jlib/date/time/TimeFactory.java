package me.pauzen.jlib.date.time;

import java.util.Calendar;

public final class TimeFactory {

    private TimeFactory() {
    }

    public static Time getInaccurateTime(int hour, int minute) {
        return new TimeImpl(hour, minute, 0);
    }

    public static Time getAccurateTime(int hour, int minute, int second) {
        return new TimeImpl(hour, minute, second);
    }

    public static Time getCurrentInnacurateTime() {
        Calendar calendar = Calendar.getInstance();
        return new TimeImpl(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
    }

    public static Time getCurrentAccurateTime() {
        Calendar calendar = Calendar.getInstance();
        return new TimeImpl(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

}
