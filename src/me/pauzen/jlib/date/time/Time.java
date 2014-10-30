package me.pauzen.jlib.date.time;


import java.util.Calendar;

public abstract class Time {

    public static InaccTime currentInnacurateTime() {
        return new InaccTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE));
    }

    public static Time compareInaccurate(InaccTime inaccTime1, InaccTime inaccTime2) {
        InaccTime comparedTime = new InaccTime(inaccTime1.getHour() - inaccTime2.getHour(), inaccTime1.getMinute() - inaccTime2.getMinute());
        if (comparedTime.toNumberValue() < 0)
            return null;
        return comparedTime;
    }

    public static Time compareAccurate(AccTime accTime1, AccTime accTime2) {
        return new AccTime(Math.abs(Math.abs(accTime1.getHour()) - Math.abs(accTime2.getHour())), Math.abs(Math.abs(accTime1.getMinute()) - Math.abs(accTime2.getMinute())), Math.abs(Math.abs(accTime1.getSecond()) - Math.abs(accTime2.getSecond())));
    }
}
