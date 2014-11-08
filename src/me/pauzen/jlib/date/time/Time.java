package me.pauzen.jlib.date.time;

public interface Time {

    public long toMilliseconds();

    public Time compareAccurately(Time time);

    public Time compareInaccurately(Time time);

    public long getHour();

    public long getMinute();

    public long getSecond();

    public int hashCode();

    public boolean equals(Time time);

    public String toString();
}
