package com.devmicheledonato.airto.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.devmicheledonato.airto.MainActivity;
import com.devmicheledonato.airto.R;
import com.devmicheledonato.airto.data.AirToPreferences;
import com.devmicheledonato.airto.data.WeatherContract;

/**
 * Created by Michele on 26/06/2017.
 */

public class AirToNotificationUtils {

    private static final String TAG = AirToNotificationUtils.class.getSimpleName();

    /*
     * The columns of data that we are interested in displaying within our notification to let
     * the user know there is new weather data available.
     */
    public static final String[] WEATHER_NOTIFICATION_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_IPQA
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able
     * to access the data from our query. If the order of the Strings above changes, these
     * indices must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_WEATHER_ICON = 0;
    public static final int INDEX_MAX_TEMP = 1;
    public static final int INDEX_MIN_TEMP = 2;
    public static final int INDEX_IPQA = 3;

    public static final String[] CAR_BAN_NOTIFICATION_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_LEVEL,
            WeatherContract.WeatherEntry.COLUMN_BLOCK_TYPE
    };

    public static final int INDEX_CAR_BAN_LEVEL = 0;
    public static final int INDEX_CAR_BAN_BLOCK_TYPE = 1;

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 3004 is in no way significant.
     */
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    private static final int CAR_BAN_NOTIFICATION_ID = 3005;


    public static void notifyUserOfNewWeather(Context context) {
        Log.d(TAG, "notifyUserOfNewWeather");

        long dateTimeMillis = AirToDateUtils.getTodayAtMidnight();
        long dateNormalizedMillis = AirToDateUtils.getNormalizedDate(dateTimeMillis);

        Uri todayWeatherUri = WeatherContract.WeatherEntry.
                buildWeatherUriWithDate(dateNormalizedMillis);

        Cursor todayWeatherCursor = context.getContentResolver().query(
                todayWeatherUri,
                WEATHER_NOTIFICATION_PROJECTION,
                null,
                null,
                null);

        if (todayWeatherCursor != null && todayWeatherCursor.moveToFirst()) {
            Log.d(TAG, "todayWeatherCursor ok");
            String weatherIcon = todayWeatherCursor.getString(INDEX_WEATHER_ICON);
            double max = todayWeatherCursor.getDouble(INDEX_MAX_TEMP);
            double min = todayWeatherCursor.getDouble(INDEX_MIN_TEMP);
            String ipqa = todayWeatherCursor.getString(INDEX_IPQA);

            int weatherImageId = AirToWeatherUtils.getResourceIdForWeatherCondition(context, weatherIcon);
            String notificationTitle = context.getString(R.string.app_name);
            String notificationText = getWeatherNotificationText(context, weatherIcon, max, min, ipqa);

            sendNotification(context, weatherImageId, notificationTitle, notificationText, WEATHER_NOTIFICATION_ID);

            /*
             * Since we just showed a notification, save the current time. That way, we can check
             * next time the weather is refreshed if we should show another notification.
             */
            AirToPreferences.saveLastNotificationTime(context, AirToDateUtils.getNormalizedDate(System.currentTimeMillis()),
                    context.getString(R.string.pref_last_weather_notification_key));
        }

        /* Always close your cursor when you're done with it to avoid wasting resources. */
        todayWeatherCursor.close();
    }

    public static void notifyUserOfCarBan(Context context) {
        Log.d(TAG, "notifyUserOfCarBan");

        long dateTimeMillis = AirToDateUtils.getTomorrowAtMidnightFromTimeInMillis(System.currentTimeMillis());
        long dateNormalizedMillis = AirToDateUtils.getNormalizedDate(dateTimeMillis);

        Uri tomorrowWeatherUri = WeatherContract.WeatherEntry.
                buildWeatherUriWithDate(dateNormalizedMillis);

        Cursor carBanCursor = context.getContentResolver().query(
                tomorrowWeatherUri,
                CAR_BAN_NOTIFICATION_PROJECTION,
                null,
                null,
                null);

        if (carBanCursor != null && carBanCursor.moveToFirst()) {

            String level = carBanCursor.getString(INDEX_CAR_BAN_LEVEL);
            String blockType = carBanCursor.getString(INDEX_CAR_BAN_BLOCK_TYPE);

            if (level != null && blockType != null) {
                int imageId = R.drawable.ic_car_ban;
                String notificationTitle = context.getString(R.string.app_name);
                String notificationText = "Domani blocco auto livello " + level + "\n" + blockType;

                sendNotification(context, imageId, notificationTitle, notificationText, CAR_BAN_NOTIFICATION_ID);

                AirToPreferences.saveLastNotificationTime(context, AirToDateUtils.getNormalizedDate(System.currentTimeMillis()),
                        context.getString(R.string.pref_last_car_ban_notification_key));
            }
        }

        carBanCursor.close();
    }

    private static void sendNotification(Context context, int imageId, String notificationTitle, String notificationText, int notificationId) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(imageId)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private static String getWeatherNotificationText(Context context, String weatherIcon, double max, double min, String ipqa) {

        String description = AirToWeatherUtils.getForecastDescriptionForWeatherCondition(context, weatherIcon);

        String notificationFormat = context.getString(R.string.format_notification);

        String notificationText = String.format(notificationFormat,
                description,
                AirToWeatherUtils.formatTemperature(context, max),
                AirToWeatherUtils.formatTemperature(context, min),
                AirToWeatherUtils.getIpqaString(context, ipqa));

        return notificationText;
    }

}
