package com.mengzhihua.utils.common.time;


import java.time.LocalDate;

/**
 * Julian Day Number (Meeus / Fliegel–Van Flandern). {@code 2000-01-01} → {@code 2451545}.
 */
public final class JulianDayUtil {

    private JulianDayUtil() {
    }

    public static long of(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;
        return day + (153L * m + 2) / 5 + 365L * y + y / 4 - y / 100 + y / 400 - 32045;
    }

    public static long of(String isoDate) {
        return of(LocalDate.parse(isoDate));
    }

    public static LocalDate toLocalDate(long julianDayNumber) {
        long l = julianDayNumber + 68569;
        long n = 4 * l / 146097;
        l = l - (146097 * n + 3) / 4;
        long i = 4000 * (l + 1) / 1461001;
        l = l - 1461 * i / 4 + 31;
        long j = 80 * l / 2447;
        int day = (int) (l - 2447 * j / 80);
        l = j / 11;
        int month = (int) (j + 2 - 12 * l);
        int year = (int) (100 * (n - 49) + i + l);
        return LocalDate.of(year, month, day);
    }

    /**
     * Julian Date at 00:00 UTC ({@code JDN - 0.5}).
     */
    public static double julianDate(LocalDate date) {
        return of(date) - 0.5;
    }
}
