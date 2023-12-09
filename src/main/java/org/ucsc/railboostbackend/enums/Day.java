package org.ucsc.railboostbackend.enums;

public enum Day {
    SUNDAY(7), MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6);

    private final int value;

    Day(int value) { this.value = value; }

    public static Day valueOfInt(int value) {
        for (Day e : values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }
}
