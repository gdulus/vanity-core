package vanity.celebrity

enum ZodiacSign {

    RAM(3, 21, 4, 19),
    BULL(4, 20, 5, 20),
    TWINS(5, 21, 6, 20),
    CRAB(6, 21, 7, 22),
    LION(7, 23, 8, 22),
    MAIDEN(8, 23, 9, 22),
    SCALES(9, 23, 10, 22),
    SCORPION(10, 23, 11, 21),
    ARCHER(11, 22, 12, 21),
    GOAT(12, 22, 1, 19),
    WATER_BEARER(1, 20, 2, 18),
    FISH(2, 19, 3, 20)

    private final int monthStart
    private final int dayStart
    private final int monthEnd
    private final int dayEnd

    ZodiacSign(int monthStart, int dayStart, int monthEnd, int dayEnd) {
        this.monthStart = monthStart
        this.dayStart = dayStart
        this.monthEnd = monthEnd
        this.dayEnd = dayEnd
    }

    public static ZodiacSign findByDate(final Date date) {
        int month = date.month + 1
        int dayOfTheMonth = date.date
        values().find {
            ((month == it.monthStart && dayOfTheMonth >= it.dayStart && dayOfTheMonth <= 31)
                    || (month == it.monthEnd && dayOfTheMonth >= 1 && dayOfTheMonth <= it.dayEnd))
        }
    }
}
