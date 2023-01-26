package org.citycult.datastorage.util;

import org.citycult.datastorage.util.DateHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DateHelperTest {

    private Date getDateTime(int year, int month, int day, int hour,
                             int minute, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, seconds);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date getDateTime(int year, int month, int day, int hour, int minute) {
        return getDateTime(year, month, day, hour, minute, 0);
    }

    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date getTime(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private void compare(Date date, Date dateExp) {
        if (date == null || dateExp == null)
            Assert.fail("date or dateExp is null!");
        if (date.getTime() != dateExp.getTime()) {
            Assert.fail("Wrong date parsed! " + date.toString() + " != "
                    + dateExp.toString());
        }
    }

    @Test
    public void testParseString() {
        Date date, dateExp;

        date = getDate(2011, Calendar.JANUARY, 01);
        dateExp = DateHelper.parse("sdf01.01.2011sf");
        compare(date, dateExp);

        dateExp = getDate(1970, Calendar.FEBRUARY, 03);
        date = DateHelper.parse("sdf03.02.70sf");
        compare(date, dateExp);

        dateExp = getDate(1970, Calendar.MARCH, 04);
        date = DateHelper.parse("sdf04.03.'70sf");
        compare(date, dateExp);

        dateExp = getDate(1970, Calendar.APRIL, 05);
        date = DateHelper.parse("dsf05.04.sdf");
        compare(date, dateExp);

        dateExp = getDate(2014, Calendar.MAY, 06);
        date = DateHelper.parse("sf06.Mai.2014sdf");
        compare(date, dateExp);

        dateExp = getDateTime(2013, Calendar.JANUARY, 2, 9, 33, 22);
        date = DateHelper.parse("Jan 02, 2013 09:33:22 AM");
        compare(date, dateExp);

        dateExp = getDateTime(1970, Calendar.FEBRUARY, 3, 15, 30);
        date = DateHelper.parse("03.02. 15.30");
        compare(date, dateExp);

        dateExp = getDateTime(2011, Calendar.FEBRUARY, 3, 15, 30, 0);
        date = DateHelper.parse("2011-02-03T15:30:00");
        compare(date, dateExp);

        dateExp = getDateTime(2011, Calendar.FEBRUARY, 3, 15, 30);
        date = DateHelper.parse("03.02.2011 15:30");
        compare(date, dateExp);

        dateExp = getDateTime(2011, Calendar.FEBRUARY, 3, 15, 30);
        date = DateHelper.parse("03.02.2011, 15.30 Uhr");
        compare(date, dateExp);

        dateExp = getDateTime(2011, Calendar.FEBRUARY, 3, 15, 0);
        date = DateHelper.parse("03.02.2011, 15 Uhr");
        compare(date, dateExp);

        dateExp = getDate(1970, Calendar.SEPTEMBER, 1);
        date = DateHelper.parse("SA01SEP");
        compare(date, dateExp);

        dateExp = getDate(1970, Calendar.OCTOBER, 2);
        date = DateHelper.parse("DI02OKT");
        compare(date, dateExp);

        dateExp = getTime(15, 30);
        date = DateHelper.parse("15.30 Uhr");
        compare(date, dateExp);

        dateExp = getTime(15, 0);
        date = DateHelper.parse("15 Uhr");
        compare(date, dateExp);

        dateExp = getTime(15, 30);
        date = DateHelper.parse("15:30 Uhr");
        compare(date, dateExp);
    }

    @Test
    public void testParseFormatString() {
        Date date, dateExp;
        dateExp = getDate(1970, Calendar.MARCH, 5);
        date = DateHelper.parse(DateHelper.DateFormat.DDMM, "sd05.03.ad");
        compare(date, dateExp);
    }

    @Test
    public void testFormat() {
        String date, dateExp;
        dateExp = "01.01.1970";
        date = DateHelper.format(DateHelper.DateFormat.DDMMYYYY,
                getDate(1970, Calendar.JANUARY, 1));

        if (!date.equals(dateExp)) {
            Assert.fail("Wrong date parsed! " + date.toString() + " != "
                    + dateExp.toString());
        }
    }

    @Test
    public void testCombineDateTime() {
        Date date = getDate(2011, Calendar.APRIL, 2);
        Date time = getTime(5, 6);
        Date combined = DateHelper.combineDateTime(date, time);
        Date expected = getDateTime(2011, Calendar.APRIL, 2, 5, 6);
        compare(combined, expected);

        date = DateHelper.parse(DateHelper.DateFormat.DDMMYYYY,
                "sd05.03.2011.ad");
        time = DateHelper.parse(DateHelper.DateFormat.HHMM_UHR,
                "sd15.30 Uhr.ad");
        combined = DateHelper.combineDateTime(date, time);
        expected = getDateTime(2011, Calendar.MARCH, 5, 15, 30);
        compare(combined, expected);

    }

}
