package com.devmicheledonato.airto.utils;

import android.net.Uri;
import android.util.Log;

import com.devmicheledonato.airto.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Michele on 09/04/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast";

    private static final String CITY_PARAM = "id";
    private static final String TURIN_ID = "3165524";

    // API_KEY
    private static final String API_PARAM = "appid";

    private static final String UNITS_PARAM = "units";
    private static final String METRIC = "metric";
    private static final String IMPERIAL = "imperial";

    /**
     * Builds the URL used to talk to the weather server.
     *
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl() {
        Uri weatherQueryUri = Uri.parse(WEATHER_URL).buildUpon()
                .appendQueryParameter(CITY_PARAM, TURIN_ID)
                .appendQueryParameter(API_PARAM, BuildConfig.OW_API_KEY)
                .appendQueryParameter(UNITS_PARAM, METRIC)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
