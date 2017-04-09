package com.devmicheledonato.airto.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Michele on 09/04/2017.
 */

public class WeatherContract {

    /**
     * The Content authority is a name for the entire content provider.
     */
    public static final String CONTENT_AUTHORITY = "com.devmicheledonato.airto";

    /**
     * Use CONTENT_AUTHORITY to create the base of all Uri's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible paths that can be append to BASE_CONTENT_URI to form valid Uri's that the app can handle.
     * For instance,
     * <p>
     * content://com.devmicheledonato.airto/weather/
     * [       BASE_CONTENT_URI           ][PATH_WEATHER]
     * <p>
     * is a valid path for looking at weather data.
     */
    public static final String PATH_WEATHER = "weather";

    public static final class WeatherEntry implements BaseColumns {

        /**
         * The base CONTENT_URI used to query the Weather table from content provider.
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        /**
         * Used internally as the name of our weather table.
         */
        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_DATE = "dt";

        // Inner "main" jsonObject
        public static final String COLUMN_TEMP = "temp";
        public static final String COLUMN_MIN_TEMP = "temp_min";
        public static final String COLUMN_MAX_TEMP = "temp_max";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_HUMIDITY = "humidity";
        // Inner "weather" jsonArray
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_WEATHER_MAIN = "weather_main";
        public static final String COLUMN_WEATHER_DESCRIPTION = "weather_description";
        public static final String COLUMN_WEATHER_ICON = "weather_icon";

    }

}
