package me.pauzen.jlib.date;

public enum Day {

    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

    private static Day[] days = Day.values();

    private int number;

    /**
     * Gets day based on day of the week int value. Index starts at 1.
     *
     * @param day The int day.
     * @return The Day value.
     */
    public static Day valueOf(int day) {
        return days[day - 1];
    }

    /**
     * Gets a Day's day of the week.
     *
     * @return Int value for day of week.
     */
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
