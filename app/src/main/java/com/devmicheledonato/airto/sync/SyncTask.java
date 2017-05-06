package com.devmicheledonato.airto.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.devmicheledonato.airto.data.WeatherContract;
import com.devmicheledonato.airto.utils.AirToNetworkUtils;
import com.devmicheledonato.airto.utils.AirToJsonUtils;

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

            ContentValues[] weatherValues = AirToJsonUtils.getWeatherContentValuesFromJson(context, jsonWeatherResponse, ipqaResponse);

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


            }

        } catch (Exception e) {
            Log.e(TAG, "weatherResponse and ipqaResponse have failed.");
            e.printStackTrace();
        }
    }
}
