package com.mengzhihua.utils.common.time;


import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 二十四节气（Hutool {@code SolarTerms} 近似公式，1900-2099）。
 */
public final class SolarTermUtil {

    public enum Term {
        XIAO_HAN("小寒"), DA_HAN("大寒"),
        LI_CHUN("立春"), YU_SHUI("雨水"),
        JING_ZHE("惊蛰"), CHUN_FEN("春分"),
        QING_MING("清明"), GU_YU("谷雨"),
        LI_XIA("立夏"), XIAO_MAN("小满"),
        MANG_ZHONG("芒种"), XIA_ZHI("夏至"),
        XIAO_SHU("小暑"), DA_SHU("大暑"),
        LI_QIU("立秋"), CHU_SHU("处暑"),
        BAI_LU("白露"), QIU_FEN("秋分"),
        HAN_LU("寒露"), SHUANG_JIANG("霜降"),
        LI_DONG("立冬"), XIAO_XUE("小雪"),
        DA_XUE("大雪"), DONG_ZHI("冬至");

        private final String chinese;

        Term(String chinese) {
            this.chinese = chinese;
        }

        public String chinese() {
            return chinese;
        }

        public static Term ofChinese(String name) {
            if (name == null) {
                return null;
            }
            for (Term term : values()) {
                if (term.chinese.equals(name) || term.name().equalsIgnoreCase(name)) {
                    return term;
                }
            }
            return null;
        }
    }

    private static final double[] C20 = {
            6.11, 20.84, 4.6295, 19.4599, 6.3826, 21.4155, 5.59, 20.888,
            6.318, 21.86, 6.5, 22.2, 7.928, 23.65, 8.35, 23.95,
            8.44, 23.822, 9.098, 24.218, 8.218, 23.08, 7.9, 22.6
    };
    private static final double[] C21 = {
            5.4055, 20.12, 3.87, 18.73, 5.63, 20.646, 4.81, 20.1,
            5.52, 21.04, 5.678, 21.37, 7.108, 22.83, 7.5, 23.13,
            7.646, 23.042, 8.318, 23.438, 7.438, 22.36, 7.18, 21.94
    };

    private SolarTermUtil() {
    }

    public static LocalDate date(int year, Term term) {
        if (term == null || year < 1900 || year > 2099) {
            return null;
        }
        return LocalDate.of(year, term.ordinal() / 2 + 1, dayOf(year, term));
    }

    public static LocalDate date(int year, String name) {
        return date(year, Term.ofChinese(name));
    }

    public static int dayOf(int year, Term term) {
        if (term == null) {
            throw new IllegalArgumentException("term must not be null");
        }
        double[] c = year < 2000 ? C20 : C21;
        int y = year % 100;
        return (int) (y * 0.2422 + c[term.ordinal()]) - y / 4;
    }

    public static String name(LocalDate date) {
        if (date == null) {
            return null;
        }
        for (Term term : Term.values()) {
            if (date.equals(date(date.getYear(), term))) {
                return term.chinese();
            }
        }
        return null;
    }

    public static Map<String, String> yearTerms(int year) {
        Map<String, String> terms = new LinkedHashMap<>();
        for (Term term : Term.values()) {
            LocalDate date = date(year, term);
            if (date != null) {
                terms.put(term.chinese(), date.toString());
            }
        }
        return terms;
    }

    public static boolean isTerm(LocalDate date) {
        return name(date) != null;
    }

    public static String normalizeName(String name) {
        Term term = Term.ofChinese(name == null ? null : name.trim().toUpperCase(Locale.ROOT));
        return term == null ? null : term.chinese();
    }
}
