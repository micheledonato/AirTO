package com.devmicheledonato.airto.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.devmicheledonato.airto.R;
import com.devmicheledonato.airto.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Michele on 10/04/2017.
 */

public class AirToJsonUtils {

    private static final String TAG = AirToJsonUtils.class.getSimpleName();

    private static final String DAILY = "daily";

    private static final String DATA = "data";

    private static final class ChildData {
        private static final String TIME = "time";
        private static final String SUMMARY = "summary";
        private static final String ICON = "icon";
        private static final String MIN = "temperatureMin";
        private static final String MAX = "temperatureMax";
    }

    public static ContentValues[] getWeatherContentValuesFromJson(Context context, String forecastJsonStr, String[] ipqa) throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        JSONObject jsonDaily = forecastJson.getJSONObject(DAILY);
        JSONArray jsonWeatherArray = jsonDaily.getJSONArray(DATA);

        ContentValues[] weatherContentValues = new ContentValues[jsonWeatherArray.length()];

        for (int i = 0; i < jsonWeatherArray.length(); i++) {

            /* Get the JSON object representing the day */
            JSONObject dayForecast = jsonWeatherArray.getJSONObject(i);
            long dateTimeMillis = dayForecast.getLong(ChildData.TIME);

            long dateNormalizedMillis = AirToDateUtils.getDateAtMidday(dateTimeMillis * 1000);
//            Log.d(TAG, "Date at midday: " + dateNormalizedMillis);

            String summary = dayForecast.getString(ChildData.SUMMARY);
            String icon = dayForecast.getString(ChildData.ICON);
            double temp_min = dayForecast.getDouble(ChildData.MIN);
            double temp_max = dayForecast.getDouble(ChildData.MAX);

            ContentValues weatherValues = new ContentValues();
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateNormalizedMillis);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SUMMARY, summary);

            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, temp_min);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, temp_max);

            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, icon);

            if (ipqa != null) {
                if (AirToDateUtils.getDayName(context, dateNormalizedMillis).equals(context.getString(R.string.today))) {
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_IPQA, ipqa[2]);
                } else if (AirToDateUtils.getDayName(context, dateNormalizedMillis).equals(context.getString(R.string.tomorrow))) {
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_IPQA, ipqa[3]);
                }
            }

            weatherContentValues[i] = weatherValues;
        }
        return weatherContentValues;
    }
}
