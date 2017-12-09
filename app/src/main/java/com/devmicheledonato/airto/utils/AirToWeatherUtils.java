package com.devmicheledonato.airto.utils;

import android.content.Context;
import android.util.Log;

import com.devmicheledonato.airto.R;

import static com.devmicheledonato.airto.utils.AirToNetworkUtils.IPQA_NA;

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

    private static final String OTTIMA = "ic_ottima";
    private static final String BUONA = "ic_buona";
    private static final String ACCETTABILE = "ic_accettabile";
    private static final String CATTIVA = "ic_cattiva";
    private static final String PESSIMA = "ic_pessima";


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

    public static String getIpqaString(Context context, String ipqa) {

        String ipqaString = "N.D.";
        if (ipqa != null && !ipqa.equals(IPQA_NA)) {
            ipqaString = ipqa.toUpperCase();
        }
        return ipqaString;
    }

    public static int getResourceIdForWeatherCondition(Context context, String weatherIcon) {

        weatherIcon = weatherIcon.replaceAll("-", "_");
        weatherIcon = "ic_" + weatherIcon;
//        Log.d(TAG, "weatherIcon: " + weatherIcon);

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
                weatherImageId = context.getResources().getIdentifier(weatherIcon, "drawable", context.getPackageName());
                break;
            case SLEET:
                weatherImageId = R.drawable.ic_snow;
                break;
            default:
                weatherImageId = R.drawable.ic_cloudy;
        }

//        Log.d(TAG, "weatherImageId: " + weatherImageId);
        return weatherImageId;
    }

    public static String getForecastDescriptionForWeatherCondition(Context context, String weatherIcon) {

        weatherIcon = weatherIcon.replaceAll("-", "_");
        weatherIcon = "ic_" + weatherIcon;

        String forecastDescription;
        switch (weatherIcon) {
            case CLEAR_DAY:
            case CLEAR_NIGHT:
                forecastDescription = context.getString(R.string.clear);
                break;
            case CLOUDY:
                forecastDescription = context.getString(R.string.cloudy);
                break;
            case FOG:
                forecastDescription = context.getString(R.string.fog);
                break;
            case HAIL:
                forecastDescription = context.getString(R.string.hail);
                break;
            case PARTLY_CLOUDY_DAY:
            case PARTLY_CLOUDY_NIGHT:
                forecastDescription = context.getString(R.string.partly_cloudy);
                break;
            case RAIN:
                forecastDescription = context.getString(R.string.rain);
                break;
            case SNOW:
                forecastDescription = context.getString(R.string.snow);
                break;
            case THUNDERSTORM:
                forecastDescription = context.getString(R.string.thunderstorm);
                break;
            case TORNADO:
                forecastDescription = context.getString(R.string.tornado);
                break;
            case WIND:
                forecastDescription = context.getString(R.string.wind);
                break;
            case SLEET:
                forecastDescription = context.getString(R.string.snow);
                break;
            default:
                forecastDescription = context.getString(R.string.cloudy);
        }

        return forecastDescription;
    }

    public static int getResourseIdForIpqaCondition(Context context, String ipqa) {

        String ipqaCondition = "";
        try {
            ipqaCondition = "ic_" + ipqa.toLowerCase();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        int ipqaImageId;

        switch (ipqaCondition) {
            case OTTIMA:
            case BUONA:
            case ACCETTABILE:
            case CATTIVA:
            case PESSIMA:
                ipqaImageId = context.getResources().getIdentifier(ipqaCondition, "drawable", context.getPackageName());
                break;
            default:
                ipqaImageId = R.drawable.ic_non_disponibile;
        }

        return ipqaImageId;
    }
}
