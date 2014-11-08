package me.pauzen.jlib.date;

import me.pauzen.jlib.date.time.Time;
import me.pauzen.jlib.date.time.TimeFactory;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Date {

    private Day  day;
    private Time inaccurateTime;

    private boolean tomorrow = false;

    public Date(Day day, Time inaccurateTime) {
        this.day = day;
        this.inaccurateTime = inaccurateTime;
    }

    /**
     * Gets current date.
     *
     * @return New Date Object of the current date.
     */
    public static Date currentDate() {
        return new Date(Day.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)), TimeFactory.getCurrentInnacurateTime());
    }

    /**
     * Converts milliseconds to "# hours # minutes # seconds" format.
     *
     * @param millis Milliseconds to convert.
     * @return Formatted String.
     */
    public static String calculateTime(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return (hours == 0 ? "" : hours + " hours ") + (minutes == 0 ? "" : minutes + " minutes " + (seconds == 0 ? "" : seconds + " seconds"));
    }

    private void setTomorrow() {
        this.tomorrow = true;
    }

    private boolean isTomorrow() {
        return this.tomorrow;
    }

    public Day getDay() {
        return day;
    }

    public Time getInaccurateTime() {
        return inaccurateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Date date = (Date) o;

        return day == date.day && inaccurateTime.equals(date.inaccurateTime);
    }

    @Override
    public String toString() {
        return "Date{" +
                "day=" + day +
                ", inaccurateTime=" + inaccurateTime +
                '}';
    }

    @Override
    public int hashCode() {
        int result = day.hashCode();
        result = 31 * result + inaccurateTime.hashCode();
        result = 31 * result + (tomorrow ? 1 : 0);
        return result;
    }
}