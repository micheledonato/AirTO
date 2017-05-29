package com.devmicheledonato.airto.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;

import com.devmicheledonato.airto.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michele on 10/04/2017.
 */

public class AirToDateUtils {

    private static final String TAG = AirToDateUtils.class.getSimpleName();

    /* Milliseconds in a day */
    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);
    /* Milliseconds in a half day */
    public static final long HALF_DAY = TimeUnit.HOURS.toMillis(12);

    /**
     * Date at midday
     *
     * @param date
     * @return
     */
    public static long getDateAtMidday(long date) {
        return date + HALF_DAY;
    }

    /**
     * This method returns the number of days since the epoch (January 01, 1970, 12:00 Midnight UTC)
     * in UTC time from the current date.
     *
     * @param utcDate A date in milliseconds in UTC time.
     * @return The number of days from the epoch to the date argument.
     */
    private static long elapsedDaysSinceEpoch(long utcDate) {
        return TimeUnit.MILLISECONDS.toDays(utcDate);
    }

    public static long getTodayAtMidnight() {
        // today
        Calendar date = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTimeInMillis();
    }

    /**
     * Returns a date string in the format specified, which shows an abbreviated date without a
     * year.
     *
     * @param context      Used by DateUtils to format the date in the current locale
     * @param timeInMillis Time in milliseconds since the epoch (local time)
     * @return The formatted date string
     */
    public static String getReadableDateString(Context context, long timeInMillis) {
        int flags = DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_NO_YEAR
                | DateUtils.FORMAT_SHOW_WEEKDAY;
//                | DateUtils.FORMAT_SHOW_TIME;

        return DateUtils.formatDateTime(context, timeInMillis, flags);
    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "Wednesday".
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The date in milliseconds (UTC time)
     * @return the string day of the week
     */
    public static String getDayName(Context context, long dateInMillis) {
        /*
         * If the date is today, return the localized version of "Today" instead of the actual
         * day name.
         */
        long daysFromEpochToProvidedDate = elapsedDaysSinceEpoch(dateInMillis);
        long daysFromEpochToToday = elapsedDaysSinceEpoch(System.currentTimeMillis());

        int daysAfterToday = (int) (daysFromEpochToProvidedDate - daysFromEpochToToday);
        Log.d(TAG, dateInMillis + " - " + System.currentTimeMillis());
        Log.d(TAG, daysFromEpochToProvidedDate + " - " + daysFromEpochToToday + " = " + daysAfterToday);

        switch (daysAfterToday) {
            case 0:
                return context.getString(R.string.today);
            case 1:
                return context.getString(R.string.tomorrow);
            default:
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", getCurrentLocale(context));
                return dayFormat.format(dateInMillis);
        }
    }

    /**
     * Replace the day name with Today or Tomorrow
     *
     * @param context
     * @param dateInMillis
     * @return the string of friendly date
     */
    public static String getFriendlyDateString(Context context, long dateInMillis) {
        String dayName = getDayName(context, dateInMillis);
        String readableDate = getReadableDateString(context, dateInMillis);
        String localizedDayName = new SimpleDateFormat("EEEE", getCurrentLocale(context)).format(dateInMillis);
        return readableDate.replace(localizedDayName, dayName);
    }

    /**
     * Provide the current Locale of device configuration
     *
     * @param context
     * @return current Locale
     */
    private static Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
