package com.devmicheledonato.airto.sync;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.devmicheledonato.airto.utils.AirToNetworkUtils;
import com.devmicheledonato.airto.utils.OpenWeatherJsonUtils;

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

            ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonWeatherResponse, ipqaResponse);

            if (weatherValues != null && weatherValues.length != 0) {

            }

        } catch (Exception e) {
            Log.e(TAG, "weatherResponse and ipqaResponse have failed.");
            e.printStackTrace();
        }
    }
}
