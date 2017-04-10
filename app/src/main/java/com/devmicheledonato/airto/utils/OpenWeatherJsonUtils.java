package com.devmicheledonato.airto.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Michele on 10/04/2017.
 */

public class OpenWeatherJsonUtils {

    private static final String TAG = OpenWeatherJsonUtils.class.getSimpleName();

    private static final String OWM_MESSAGE_CODE = "cod";

    private static final String OWM_LIST = "list";

    private static final String OWM_DATE = "dt";

    private static final String OWM_MAIN = "main";

    private static final class ChildMain {
        private static final String TEMPERATURE = "temp";
        private static final String MAX = "temp_max";
        private static final String MIN = "temp_min";
        private static final String PRESSURE = "pressure";
        private static final String HUMIDITY = "humidity";
    }

    private static final String OWM_WEATHER = "weather";

    private static final class ChildWeather {
        private static final String ID = "id";
        private static final String MAIN = "main";
        private static final String DESCRIPTION = "description";
        private static final String ICON = "icon";
    }


    public static ContentValues[] getWeatherContentValuesFromJson(Context context, String forecastJsonStr, String[] ipqa) throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }

            JSONArray jsonWeatherArray = forecastJson.getJSONArray(OWM_LIST);
            for (int i = 0; i < jsonWeatherArray.length(); i++) {

                /* Get the JSON object representing the day */
                JSONObject dayForecast = jsonWeatherArray.getJSONObject(i);
                long dateTimeMillis = dayForecast.getLong(OWM_DATE);

                long dateNormalizedMillis = AirToDateUtils.normalizeDate(dateTimeMillis * 1000);
                Log.d(TAG, "Date normalized: " + AirToDateUtils.getReadableDateString(context, dateNormalizedMillis));

                JSONObject main = dayForecast.getJSONObject(OWM_MAIN);
                double temp = main.getDouble(ChildMain.TEMPERATURE);
                double temp_min = main.getDouble(ChildMain.MIN);
                double temp_max = main.getDouble(ChildMain.MAX);
                double pressure = main.getDouble(ChildMain.PRESSURE);
                int humidity = main.getInt(ChildMain.HUMIDITY);

                JSONArray weatherArray = dayForecast.getJSONArray(OWM_WEATHER);
                for (int j = 0; j < weatherArray.length(); j++) {
                    JSONObject weather = weatherArray.getJSONObject(j);
                    int weatherId = weather.getInt(ChildWeather.ID);
                    String weatherMain = weather.getString(ChildWeather.MAIN);
                    String weatherDescription = weather.getString(ChildWeather.DESCRIPTION);
                    String weatherIcon = weather.getString(ChildWeather.ICON);
                }
            }

        }

        return null;
    }
}
