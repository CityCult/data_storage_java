package org.citycult.datastorage.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class to parse and format frequntly used date formats.
 *
 * @author cpieloth
 */
public class DateHelper {

    private static Logger log = LoggerFactory.getLogger(DateHelper.class);

    public static final Map<String, String> MONTH_TO_NUMBER_DE;

    static {
        Map<String, String> tmp = new HashMap<String, String>();
        tmp.put("Januar", "01.");
        tmp.put("Februar", "02.");
        tmp.put("März", "03.");

        tmp.put("April", "04.");
        tmp.put("Mai", "05.");
        tmp.put("Juni", "06.");

        tmp.put("Juli", "07.");
        tmp.put("August", "08.");
        tmp.put("September", "09.");

        tmp.put("Oktober", "10.");
        tmp.put("November", "11.");
        tmp.put("Dezember", "12.");
        MONTH_TO_NUMBER_DE = Collections.unmodifiableMap(tmp);
    }

    /**
     * Bundle of SimpleDateFormat and the related regex.
     * The regex is used for date detection and the SimpleDateFormat to create a Java Date object.
     * ATTENTION: When adding new enums ...
     * - sort entries from long/complex regex to small/simple regex! (long first, small last)
     * - Date detection requires the matched data in group(1) of the regex
     * - add a new test case to DateHelperTest!
     */
    public static enum DateFormat implements IDateFormat {
        EEEDDMMMYYYYHHMMSS("EEE, dd MMM yyyy hh:mm:ss",
            ".*(\\w\\w\\w, \\d\\d \\w\\w\\w \\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d).*", Locale.US),
        MMMDYYYYHHMMSSA("MMM d, yyyy hh:mm:ss a", ".*(\\w\\w\\w \\d+, \\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d \\w\\w).*"),
        YYYYMMDDTHHMMSS("yyyy-MM-dd'T'HH:mm:ss", ".*(\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d).*"),
        YYYYMMDD_HHMMSS("yyyy/MM/dd HH:mm:ss", ".*(\\d\\d\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d).*"),
        DDMMYYYYc_HHMM1("dd. MMM. yyyy, HH:mm", ".*(\\d\\d\\. \\w\\w\\w\\. \\d\\d\\d\\d, \\d\\d:\\d\\d).*"),
        DDMMYYYYc_HHMM2("dd.MM.yyyy, HH:mm", ".*(\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d, \\d\\d:\\d\\d).*"),
        DDMMYYYYc_HHMM3("dd.MM.yyyy, HH.mm", ".*(\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d, \\d\\d\\.\\d\\d).*"),
        DDMMYYYY_HHMM1("dd.MM.yyyy HH:mm", ".*(\\d\\d\\.\\d\\d.\\d\\d\\d\\d \\d\\d:\\d\\d).*"),
        DDMMYYYY_HHMM2("dd MM.yyyy HH:mm", ".*(\\d\\d \\d\\d.\\d\\d\\d\\d \\d\\d:\\d\\d).*"),
        DDMMYYYY_HHMM3("dd.MM.yyyy HH.mm", ".*(\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d \\d\\d\\.\\d\\d).*"),
        DDMMYYYYc_HH("dd.MM.yyyy, HH", ".*(\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d, \\d\\d).*"),
        DDMMMYYYY("dd. MMM. yyyy", ".*(\\d\\d\\. \\w\\w\\w\\. \\d\\d\\d\\d).*"),
        DDMMMYYYY2("dd.MMM.yyyy", ".*(\\d\\d\\.\\w\\w\\w\\.\\d\\d\\d\\d).*"),
        DDMMYYYY("dd.MM.yyyy", ".*(\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d).*"),
        DMYYYY("dd.MM.yyyy", ".*(\\d+\\.\\d+\\.\\d\\d\\d\\d).*"),
        EEDDMMM("EEddMMM", ".*(\\w\\w\\d\\d\\w\\w\\w).*"),
        YYYMMDD("yyy-MM-dd", ".*(\\d\\d\\d\\d-\\d\\d-\\d\\d).*"),
        DDMM_HHMM("dd.MM. HH.mm", ".*(\\d\\d\\.\\d\\d. \\d\\d\\.\\d\\d).*"),
        DDMM_YY("dd.MM.''yy", ".*(\\d\\d\\.\\d\\d\\.\'\\d\\d).*"),
        DDMMYY("dd.MM.yy", ".*(\\d\\d\\.\\d\\d\\.\\d\\d).*"),
        THHMMSS("HH:mm:ss", ".*(T\\d\\d:\\d\\d:\\d\\d).*"),
        HHMM_UHR("HH.mm' Uhr'", ".*(\\d\\d\\.\\d\\d Uhr).*"),
        DDMM("dd.MM.", ".*(\\d\\d\\.\\d\\d\\.).*"),
        HHMM("HH:mm", ".*(\\d\\d:\\d\\d).*"),
        HHMMDOT("HH.mm", ".*(\\d\\d\\.\\d\\d).*"),
        HH_UHR("HH' Uhr'", ".*(\\d\\d Uhr).*"),
        HHUHR("HH'Uhr'", ".*(\\d\\dUhr).*");

        private final SimpleDateFormat dateFormat;
        private final Pattern pattern;
        private final String datePattern;

        private DateFormat(String datePattern, String pattern) {
            this(datePattern, pattern, Locale.GERMAN);
        }

        private DateFormat(String datePattern, String pattern, Locale locale) {
            this.datePattern = datePattern;
            this.dateFormat = new SimpleDateFormat(datePattern, locale);
            this.pattern = Pattern.compile(pattern);
        }

        @Override
        public SimpleDateFormat getFormat() {
            return dateFormat;
        }

        @Override
        public Pattern getPattern() {
            return pattern;
        }

        @Override
        public String getDatePattern() {
            return datePattern;
        }

        @Override
        public Date parse(String value) {
            if (value == null)
                return null;

            Matcher m = pattern.matcher(value);
            if (m.matches()) {
                try {
                    return dateFormat.parse(m.group(1));
                } catch (ParseException e) {
                    log.error(this.name() + "-  Could not parse date! " + value);
                }
            }

            log.warn("Format does not match!");
            return null;
        }

        @Override
        public String format(Date date) {
            return dateFormat.format(date);
        }

        @Override
        public boolean matches(String value) {
            if (value == null)
                return false;
            return pattern.matcher(value).matches();
        }
    }

    /**
     * Parses a string and converts it to a date, if a pattern is found.
     *
     * @param value
     * @return date or null
     */
    public static Date parse(String value) {
        for (IDateFormat f : DateFormat.values()) {
            if (f.matches(value)) {
                return f.parse(value);
            }
        }
        log.warn("No format matches for: " + value);
        return null;
    }

    /**
     * Parses the string with the given format to a date.
     *
     * @param format
     * @param value
     * @return date or null
     */
    public static Date parse(IDateFormat format, String value) {
        return format.parse(value);
    }

    /**
     * Creates a string in the given format.
     *
     * @param format Format the string
     * @param date   date to convert
     * @return date string
     */
    public static String format(IDateFormat format, Date date) {
        return format.format(date);
    }

    /**
     * Combines the day, month and year with hour, minutes, seconds and sets
     * milliseconds to 0.
     *
     * @param date date with the day, month, year to set.
     * @param time date with the hours, minutes, seconds to set.
     * @return new date with day, month, year from date and hours, minutes,
     * seconds from time.
     */
    public static Date combineDateTime(Date date, Date time) {
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);

        Calendar calTime = Calendar.getInstance();
        calTime.setTime(time);

        Calendar calRes = Calendar.getInstance();
        calRes.set(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH),
            calDate.get(Calendar.DAY_OF_MONTH),
            calTime.get(Calendar.HOUR_OF_DAY),
            calTime.get(Calendar.MINUTE), calTime.get(Calendar.SECOND));
        calRes.set(Calendar.MILLISECOND, 0);

        return calRes.getTime();
    }

    public static String getYearAsString() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    public static Date getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * Extract the year, month and day of month from a date
     *
     * @param date Date to extract year, month and day.
     * @return Date without hours, minutes and seconds.
     */
    public static Date getYearMonthDay(final Date date) {
        Calendar calIn = Calendar.getInstance();
        calIn.setTime(date);

        Calendar calOut = Calendar.getInstance();
        calOut.clear();
        calOut.set(Calendar.YEAR, calIn.get(Calendar.YEAR));
        calOut.set(Calendar.MONTH, calIn.get(Calendar.MONTH));
        calOut.set(Calendar.DAY_OF_MONTH, calIn.get(Calendar.DAY_OF_MONTH));

        return calOut.getTime();
    }

    public static Date getNow() {
        return getNow(0);
    }

    public static Date getNow(long offsetMillis) {
        long time = System.currentTimeMillis();
        time += offsetMillis;
        return new Date(time);
    }

    public static interface IDateFormat {

        public SimpleDateFormat getFormat();

        public Pattern getPattern();

        public String getDatePattern();

        public boolean matches(String value);

        public Date parse(String value);

        public String format(Date date);

    }

    public static class DateRange {

        private final Date start;
        private final Date end;

        public DateRange(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        public DateRange(Date start) {
            this.start = start;
            this.end = DateHelper.MAX_DATE;
        }

        public DateRange(long start, long end) {
            this.start = new Date(start);
            this.end = new Date(end);
        }

        public DateRange(long start) {
            this.start = new Date(start);
            this.end = DateHelper.MAX_DATE;
        }

        public Date getStart() {
            return this.start;
        }

        public Date getEnd() {
            return this.end;
        }

        @Override
        public String toString() {
            return start + " " + end;
        }
    }

    public static String getDayOfWeekDE(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EEEEE", Locale.GERMAN);
        return format.format(date);
    }

    public static String replaceMonthNameByNumberDE(String value) {
        // e.g. 21. August 2011 12:30 Uhr
        // to 21.08.2011 12:30 Uhr
        final String prePattern = ".*\\d+.(\\s*";
        final String postPatter = "\\s*)\\d\\d\\d\\d.*";
        Pattern pattern;
        Matcher m;
        for (Map.Entry<String, String> entry : MONTH_TO_NUMBER_DE.entrySet()) {
            pattern = Pattern.compile(prePattern + entry.getKey() + postPatter);
            m = pattern.matcher(value);
            if (m.matches()) {
                return value.replaceAll(m.group(1), entry.getValue());
            }

        }
        return value;
    }

    public static final long SECOND_MILLISECONDS = 1000;
    public static final long MINUTE_MILLISECONDS = 60 * 1000;
    public static final long HOUR_MILLISECONDS = 60 * 60 * 1000;
    public static final long DAY_MILLISECONDS = 60 * 60 * 24 * 1000;
    public static final long WEEK_MILLISECONDS = DAY_MILLISECONDS * 7;
    public static final Date MIN_DATE = new Date(0);
    public static final Date MAX_DATE = DateHelper.parse("2999-12-31");

}
