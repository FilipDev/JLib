package me.pauzen.jlib.date;

public enum Day {

    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

    private static Day[] days = Day.values();

    private int number;

    public static Day valueOf(int day) {
        return days[day - 1];
    }

    public int getNumber() {
        return number;
    }

    private void setNumber(int i) {
        this.number = i;
    }
    static {
        for (int i = 0; i < days.length; i++) days[i].setNumber(i);
    }

}
