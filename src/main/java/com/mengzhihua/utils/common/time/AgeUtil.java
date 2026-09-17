package com.mengzhihua.utils.common.time;


import java.time.LocalDate;
import java.time.Period;

/**
 * Age / birthday helpers (Hutool {@code AgeUtil} style).
 */
public final class AgeUtil {

    private AgeUtil() {
    }

    public static int age(LocalDate birthday) {
        return age(birthday, DateTimeUtil.today());
    }

    public static int age(LocalDate birthday, LocalDate today) {
        if (birthday == null || today == null || birthday.isAfter(today)) {
            return 0;
        }
        return Period.between(birthday, today).getYears();
    }

    public static LocalDate nextBirthday(LocalDate birthday) {
        return nextBirthday(birthday, DateTimeUtil.today());
    }

    public static LocalDate nextBirthday(LocalDate birthday, LocalDate today) {
        if (birthday == null || today == null) {
            return null;
        }
        LocalDate next = birthday.withYear(today.getYear());
        if (next.isBefore(today)) {
            next = birthday.withYear(today.getYear() + 1);
        }
        return next;
    }

    public static long daysUntilBirthday(LocalDate birthday) {
        LocalDate next = nextBirthday(birthday);
        return next == null ? 0 : java.time.temporal.ChronoUnit.DAYS.between(DateTimeUtil.today(), next);
    }

    public static boolean isBirthday(LocalDate birthday) {
        return isBirthday(birthday, DateTimeUtil.today());
    }

    public static boolean isBirthday(LocalDate birthday, LocalDate today) {
        return birthday != null && today != null
                && birthday.getMonth() == today.getMonth()
                && birthday.getDayOfMonth() == today.getDayOfMonth();
    }
}
