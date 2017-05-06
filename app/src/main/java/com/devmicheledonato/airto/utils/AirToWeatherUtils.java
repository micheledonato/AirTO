package com.devmicheledonato.airto.utils;

import android.content.Context;
import android.util.Log;

import com.devmicheledonato.airto.R;

/**
 * Created by Michele on 01/05/2017.
 */

public class AirToWeatherUtils {

    private static final String TAG = AirToWeatherUtils.class.getSimpleName();

    private static final String CLEAR_DAY = "ic_clear_day";
    private static final String CLEAR_NIGHT = "ic_clear_night";
    private static final String CLOUDY = "ic_cloudy";
    private static final String FOG = "ic_fog";
    private static final String HAIL = "ic_hail";
    private static final String PARTLY_CLOUDY_DAY = "ic_partly_cloudy_day";
    private static final String PARTLY_CLOUDY_NIGHT = "ic_partly_cloudy_night";
    private static final String RAIN = "ic_rain";
    private static final String SNOW = "ic_snow";
    private static final String THUNDERSTORM = "ic_thunderstorm";
    private static final String TORNADO = "ic_tornado";
    private static final String WIND = "ic_wind";
    private static final String SLEET = "ic_sleet";

    /**
     * Temperature data is stored in Celsius by our app. Depending on the user's preference,
     * the app may need to display the temperature in Fahrenheit. This method will perform that
     * temperature conversion if necessary. It will also format the temperature so that no
     * decimal points show. Temperatures will be formatted to the following form: "21°"
     *
     * @param context     Android Context to access preferences and resources
     * @param temperature Temperature in degrees Celsius (°C)
     * @return Formatted temperature String in the following form:
     * "21°"
     */
    public static String formatTemperature(Context context, double temperature) {
//        if (!SunshinePreferences.isMetric(context)) {
//            temperature = celsiusToFahrenheit(temperature);
//        }

        int temperatureFormatResourceId = R.string.format_temperature;

        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(temperatureFormatResourceId), temperature);
    }

    public static int getResourceIdForWeatherCondition(Context context, String weatherIcon) {

        weatherIcon = weatherIcon.replaceAll("-", "_");
        weatherIcon = "ic_" + weatherIcon;
        Log.d(TAG, "weatherIcon: " + weatherIcon);

        int weatherImageId;
        switch (weatherIcon) {
            case CLEAR_DAY:
            case CLEAR_NIGHT:
            case CLOUDY:
            case FOG:
            case HAIL:
            case PARTLY_CLOUDY_DAY:
            case PARTLY_CLOUDY_NIGHT:
            case RAIN:
            case SNOW:
            case THUNDERSTORM:
            case TORNADO:
            case WIND:
                weatherImageId = context.getResources().getIdentifier(weatherIcon, "drawable",
                        context.getPackageName());
                break;
            case SLEET:
                weatherImageId = R.drawable.ic_snow;
                break;
            default:
                weatherImageId = R.drawable.ic_cloudy;
        }

        Log.d(TAG, "weatherImageId: " + weatherImageId);
        return weatherImageId;
    }
}
