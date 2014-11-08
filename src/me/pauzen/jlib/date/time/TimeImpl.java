package me.pauzen.jlib.date.time;

class TimeImpl implements Time {

    long hours, minutes, seconds;

    protected TimeImpl(long hours, long minutes, long seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public long toMilliseconds() {
        return (this.getHour() * 60) * 60 * 1000 + this.getMinute() * 60 * 1000 + this.getSecond() * 1000;
    }

    @Override
    public Time compareAccurately(Time time) {
        return new TimeImpl(hours - time.getHour(), minutes - time.getMinute(), seconds - time.getSecond());
    }

    @Override
    public Time compareInaccurately(Time time) {
        return new TimeImpl(hours - time.getHour(), minutes - time.getMinute(), 0);
    }

    @Override
    public long getHour() {
        return hours;
    }

    @Override
    public long getMinute() {
        return minutes;
    }

    @Override
    public long getSecond() {
        return seconds;
    }

    @Override
    public String toString() {
        return "TimeImpl{" +
                "hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }

    @Override
    public boolean equals(Time time1) {
        if (this == time1) return true;
        if (time1 == null || getClass() != time1.getClass()) return false;

        TimeImpl time = (TimeImpl) time1;

        return hours == time.hours && minutes == time.minutes && seconds == time.seconds;

    }

    @Override
    public int hashCode() {
        int result = (int) (hours ^ (hours >>> 32));
        result = 31 * result + (int) (minutes ^ (minutes >>> 32));
        result = 31 * result + (int) (seconds ^ (seconds >>> 32));
        return result;
    }
}
