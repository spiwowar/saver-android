package com.berlejbej.saver.utils;

import android.content.Context;

import com.berlejbej.saver.database.DBHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Szymon on 2017-01-30.
 */
public class DateUtils {

    public static SimpleDateFormat getDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static long getDayCount(Date dateStart, Date dateEnd) {
        long diff = -1;
        try {
            //time is always 00:00:00 so rounding should help to ignore the missing hour when going from winter to summer time as well as the extra hour in the other direction
            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
        } catch (Exception e) {
            //handle the exception according to your own situation
        }
        String date = date2String(dateStart);
        System.out.println("date2string: " + date2String(dateStart));
        System.out.println("string2Date: " + string2Date(date));
        return diff;
    }

    public static Date string2Date(String dateString){
        Date dateDate = null;
        SimpleDateFormat simpleDateFormat = getDateFormat();
        try {
            dateDate = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateDate;
    }

    public static String date2String(Date dateDate){
        String dateString = null;
        SimpleDateFormat simpleDateFormat = getDateFormat();
        dateString = simpleDateFormat.format(dateDate);
        return dateString;
    }

    public static int getMonthsCount(Date startDate, Date endDate){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        return diffMonth;
    }

    public static String getTodayDateString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.dateFormat);
        return dateFormat.format(new Date());
    }

    public static Date getTodayDate(){
        // today date, but clock to 00:00:00
        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        return new Date(today.getTimeInMillis());
    }

    public static String getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(day);
    }

    public static String getCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        return String.valueOf(month);
    }

    public static String getCurrentYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    public static boolean isDateInThisMonth(Date date){
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        Calendar calendarToday = Calendar.getInstance();
        calendarDate.setTime(getTodayDate());

        int yearDate = calendarDate.get(Calendar.YEAR);
        int yearToday = calendarToday.get(Calendar.YEAR);

        if (yearDate != yearToday){
            return false;
        }

        int monthDate = calendarDate.get(Calendar.MONTH);
        int monthToday = calendarToday.get(Calendar.MONTH);

        if (monthDate == monthToday){
            return true;
        }

        return false;
    }

    public static int compareMonths(Date date1, Date date2){
        // returns -1 if date1.month < date2.month
        // returns 0 if date1.month = date2.month
        // returns 1 if date1.month > date2.month

        Calendar calendarDate1 = Calendar.getInstance();
        calendarDate1.setTime(date1);
        Calendar calendarDate2 = Calendar.getInstance();
        calendarDate2.setTime(date2);

        int yearDate1 = calendarDate1.get(Calendar.YEAR);
        int yearDate2 = calendarDate2.get(Calendar.YEAR);

        if (yearDate1 < yearDate2){
            return -1;
        }
        else if (yearDate1 > yearDate2){
            return 1;
        }

        int monthDate1 = calendarDate1.get(Calendar.MONTH);
        int monthDate2 = calendarDate2.get(Calendar.MONTH);

        if (monthDate1 < monthDate2){
            return -1;
        }
        else if (monthDate1 > monthDate2){
            return 1;
        }
        return 0;
    }

    public static boolean isFirstMonth(Context context) {
        DBHandler dbHandler = DBHandler.getInstance(context);
        Date startApplicationDate = string2Date(dbHandler.getStartDate());
        Date todayDate = getTodayDate();

        Calendar startCalendar = Calendar.getInstance();
        Calendar todayCalendar = Calendar.getInstance();
        startCalendar.setTime(startApplicationDate);
        todayCalendar.setTime(todayDate);

        if (startCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                startCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH)){
            return true;
        }
        return false;

    }

    public static String getDateFromMilliseconds(long milliseconds){
        SimpleDateFormat formatter = getDateFormat();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        return formatter.format(calendar.getTime());
    }

    public static long getMillisecondsFromDate(String date){
        SimpleDateFormat formatter = getDateFormat();

        try {
            Date mDate = formatter.parse(date);
            return mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getMilliseconds(String year, String month, String day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
        return calendar.getTimeInMillis();
    }

    public static String getDay(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String getMonth(long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return String.valueOf(calendar.get(Calendar.MONTH) + 1);
    }

    public static String getYear(long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static String[] splitDate(String date){
        // [0] - year
        // [1] - month
        // [2] - day
        return date.split("-");
    }

}
