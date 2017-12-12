package com.devmicheledonato.airto.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.devmicheledonato.airto.R;
import com.devmicheledonato.airto.data.AirToPreferences;
import com.devmicheledonato.airto.data.WeatherContract;
import com.devmicheledonato.airto.utils.AirToNetworkUtils;
import com.devmicheledonato.airto.utils.AirToJsonUtils;
import com.devmicheledonato.airto.utils.AirToNotificationUtils;

import java.net.URL;

/**
 * Created by Michele on 09/04/2017.
 */

public class SyncTask {

    private static final String TAG = SyncTask.class.getSimpleName();

    synchronized public static void syncWeather(Context context) {

        try {
            URL weatherRequestUrl = AirToNetworkUtils.buildUrl();

            String jsonWeatherResponse = AirToNetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            String[] ipqaResponse = null;
            try {
                ipqaResponse = AirToNetworkUtils.getIPQAData();
            } catch (Exception e) {
                Log.w(TAG, "ipqaResponse has failed but continue to process weatherResponse.");
                e.printStackTrace();
            }

            String[] carTrafficBanResponse = null;
            try {
                carTrafficBanResponse = AirToNetworkUtils.getCarTrafficBan();
            } catch (Exception e) {
                Log.w(TAG, "carTrafficBanResponse has failed but continue to process weatherResponse.");
                e.printStackTrace();
            }

            ContentValues[] weatherValues = AirToJsonUtils.getWeatherContentValuesFromJson(context, jsonWeatherResponse, ipqaResponse, carTrafficBanResponse);

            if (weatherValues != null && weatherValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();

                /* Delete old weather data because we don't need to keep multiple days' data */
                contentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null);

                /* Insert our new weather data into AirTo's ContentProvider */
                contentResolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        weatherValues);

                /*
                 * Finally, after we insert data into the ContentProvider, determine whether or not
                 * we should notify the user that the weather has been refreshed.
                 */
                boolean weatherNotificationsEnabled = AirToPreferences.areWeatherNotificationsEnabled(context);

                /*
                 * If the last notification was shown was more than 1 day ago, we want to send
                 * another notification to the user that the weather has been updated. Remember,
                 * it's important that you shouldn't spam your users with notifications.
                 */
//                long timeSinceLastNotification = AirToPreferences.getEllapsedTimeSinceLastNotification(context);
//
//                boolean oneDayPassedSinceLastNotification = false;
//
//                if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
//                    oneDayPassedSinceLastNotification = true;
//                }

                boolean sevenOclockSinceWeatherNotification = AirToPreferences.isSevenOclock(context, context.getString(R.string.pref_last_weather_notification_key));

                /*
                 * We only want to show the notification if the user wants them shown and we
                 * haven't shown a notification in the past day.
                 */
                if (weatherNotificationsEnabled && sevenOclockSinceWeatherNotification) {
                    AirToNotificationUtils.notifyUserOfNewWeather(context);
                }

                boolean carBanNotificationsEnabled = AirToPreferences.areCarBanNotificationsEnabled(context);
                boolean sevenOclockSinceCarBanNotification = AirToPreferences.isSevenOclock(context, context.getString(R.string.pref_last_car_ban_notification_key));
                if (carBanNotificationsEnabled && sevenOclockSinceCarBanNotification) {
                    AirToNotificationUtils.notifyUserOfCarBan(context);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "weatherResponse and ipqaResponse have failed.");
            e.printStackTrace();
        }
    }
}
