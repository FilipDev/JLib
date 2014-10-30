package me.pauzen.jlib.date;

import me.pauzen.jlib.date.time.InaccTime;
import me.pauzen.jlib.date.time.Time;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Date {

    private Day       day;
    private InaccTime inaccTime;

    private boolean tomorrow = false;

    public Date(Day day, InaccTime inaccTime) {
        this.day = day;
        this.inaccTime = inaccTime;
    }

    public static Date currentDate() {
        return new Date(Day.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)), Time.currentInnacurateTime());
    }

    public void setTomorrow() {
        this.tomorrow = true;
    }

    public boolean isTomorrow() {
        return this.tomorrow;
    }

    public Day getDay() {
        return day;
    }

    public InaccTime getInaccTime() {
        return inaccTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Date date = (Date) o;

        return day == date.day && inaccTime.equals(date.inaccTime);

    }

    @Override
    public String toString() {
        return "Date{" +
                "day=" + day +
                ", inaccurateTime=" + inaccTime +
                '}';
    }

    @Override
    public int hashCode() {
        int result = day.hashCode();
        result = 31 * result + inaccTime.hashCode();
        return result;
    }

    public static String calculateTime(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return (hours == 0 ? "" : hours + " hours ") + (minutes == 0 ? "" : minutes + " minutes " + (seconds == 0 ? "" : seconds + " seconds"));
    }
}