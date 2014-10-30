package me.pauzen.jlib.date.time;

import me.pauzen.jlib.date.Date;

public class InaccTime extends Time {

    private int hour, minute;

    public InaccTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public InaccTime(String time) {
        String[] args = time.split(":");
        this.hour = Integer.parseInt(args[0]);
        this.minute = Integer.parseInt(args[1]);
    }

    public static InaccTime currentTime() {
        return Time.currentInnacurateTime();
    }

    public static InaccTime compare(InaccTime inaccTime1, InaccTime inaccTime2) {
        return new InaccTime(Math.abs(Math.abs(inaccTime1.getHour()) - Math.abs(inaccTime2.getHour())), Math.abs(Math.abs(inaccTime1.getMinute()) - Math.abs(inaccTime2.getMinute())));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InaccTime inaccTime = (InaccTime) o;

        return hour == inaccTime.hour && minute == inaccTime.minute;

    }

    @Override
    public int hashCode() {
        int result = hour;
        result = 31 * result + minute;
        return result;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public String toString() {
        return Date.calculateTime(toNumberValue());
    }

    public InaccTime compare(InaccTime inaccTime) {
        return (InaccTime) InaccTime.compareInaccurate(this, inaccTime);
    }

    public long toNumberValue() {
        return (this.getHour() * 60) * 60 * 1000 + this.getMinute() * 60 * 1000;
    }
}
