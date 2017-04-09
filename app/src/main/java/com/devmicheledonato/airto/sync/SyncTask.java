package com.devmicheledonato.airto.sync;

import android.content.Context;

import com.devmicheledonato.airto.utils.NetworkUtils;

import java.net.URL;

/**
 * Created by Michele on 09/04/2017.
 */

public class SyncTask {

    synchronized public static void syncWeather(Context context) {

        try {
            URL weatherRequestUrl = NetworkUtils.buildUrl();

            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
