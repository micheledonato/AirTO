package com.devmicheledonato.airto.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.devmicheledonato.airto.R;
import com.devmicheledonato.airto.utils.AirToDateUtils;

import java.util.Date;

/**
 * Created by Michele on 06/12/2017.
 */

public class AirToPreferences {

    /**
     * Returns true if the user prefers to see notifications from Sunshine, false otherwise. This
     * preference can be changed by the user within the SettingsFragment.
     *
     * @param context Used to access SharedPreferences
     * @return true if the user prefers to see notifications, false otherwise
     */
    public static boolean areWeatherNotificationsEnabled(Context context) {
        /* Key for accessing the preference for showing notifications */
        String displayNotificationsKey = context.getString(R.string.pref_enable_weather_notifications_key);

        /*
         * In Sunshine, the user has the ability to say whether she would like notifications
         * enabled or not. If no preference has been chosen, we want to be able to determine
         * whether or not to show them. To do this, we reference a bool stored in bools.xml.
         */
        boolean shouldDisplayNotificationsByDefault = context
                .getResources()
                .getBoolean(R.bool.show_notifications_by_default);

        /* As usual, we use the default SharedPreferences to access the user's preferences */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        /* If a value is stored with the key, we extract it here. If not, use a default. */
        boolean shouldDisplayNotifications = sp
                .getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault);

        return shouldDisplayNotifications;
    }

    public static boolean areCarBanNotificationsEnabled(Context context) {
        /* Key for accessing the preference for showing notifications */
        String displayNotificationsKey = context.getString(R.string.pref_enable_car_ban_notifications_key);

        /*
         * In Sunshine, the user has the ability to say whether she would like notifications
         * enabled or not. If no preference has been chosen, we want to be able to determine
         * whether or not to show them. To do this, we reference a bool stored in bools.xml.
         */
        boolean shouldDisplayNotificationsByDefault = context
                .getResources()
                .getBoolean(R.bool.show_notifications_by_default);

        /* As usual, we use the default SharedPreferences to access the user's preferences */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        /* If a value is stored with the key, we extract it here. If not, use a default. */
        boolean shouldDisplayNotifications = sp
                .getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault);

        return shouldDisplayNotifications;
    }

    /**
     * Returns the last time that a notification was shown (in UNIX time)
     *
     * @param context Used to access SharedPreferences
     * @return UNIX time of when the last notification was shown
     */
    public static long getLastNotificationTimeInMillis(Context context, String lastNotificationKey) {
        /* Key for accessing the time at which Sunshine last displayed a notification */
//        String lastNotificationKey = context.getString(R.string.pref_last_notification_key);

        /* As usual, we use the default SharedPreferences to access the user's preferences */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        /*
         * Here, we retrieve the time in milliseconds when the last notification was shown. If
         * SharedPreferences doesn't have a value for lastNotificationKey, we return 0. The reason
         * we return 0 is because we compare the value returned from this method to the current
         * system time. If the difference between the last notification time and the current time
         * is greater than one day, we will show a notification again. When we compare the two
         * values, we subtract the last notification time from the current system time. If the
         * time of the last notification was 0, the difference will always be greater than the
         * number of milliseconds in a day and we will show another notification.
         */
        long lastNotificationTime = sp.getLong(lastNotificationKey, 0);

        return lastNotificationTime;
    }

    /**
     * Returns the elapsed time in milliseconds since the last notification was shown. This is used
     * as part of our check to see if we should show another notification when the weather is
     * updated.
     *
     * @param context Used to access SharedPreferences as well as use other utility methods
     * @return Elapsed time in milliseconds since the last notification was shown
     */
    public static long getEllapsedTimeSinceLastNotification(Context context, String lastNotificationKey) {
        long lastNotificationTimeMillis = AirToPreferences.getLastNotificationTimeInMillis(context, lastNotificationKey);
        long timeSinceLastNotification = System.currentTimeMillis() - lastNotificationTimeMillis;
        return timeSinceLastNotification;
    }

    /**
     * @param context
     * @return
     */
    public static boolean isSevenOclock(Context context, String lastNotificationKey) {

        long nowTimeMillis = new Date().getTime();
        long lastNotificationTimeMillis = AirToPreferences.getLastNotificationTimeInMillis(context, lastNotificationKey);
        long tomorrowAtSevenFromLstaNotificationTimeMillis = AirToDateUtils.getTomorrowAtMidnightFromTimeInMillis(lastNotificationTimeMillis) + AirToDateUtils.SEVEN_HOURS;

        if (nowTimeMillis - lastNotificationTimeMillis >= tomorrowAtSevenFromLstaNotificationTimeMillis - lastNotificationTimeMillis) {
            return true;
        }
        return false;
    }

    /**
     * Saves the time that a notification is shown. This will be used to get the ellapsed time
     * since a notification was shown.
     *
     * @param context            Used to access SharedPreferences
     * @param timeOfNotification Time of last notification to save (in UNIX time)
     */
    public static void saveLastNotificationTime(Context context, long timeOfNotification, String lastNotificationKey) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(lastNotificationKey, timeOfNotification);
        editor.apply();
    }

}
