package com.devmicheledonato.airto.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.devmicheledonato.airto.utils.AirToDateUtils;

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

        public static final String COLUMN_DATE = "time";

        public static final String COLUMN_SUMMARY = "summary";

        public static final String COLUMN_MIN_TEMP = "temp_min";
        public static final String COLUMN_MAX_TEMP = "temp_max";

        public static final String COLUMN_WEATHER_ICON = "weather_icon";

        public static final String COLUMN_IPQA = "ipqa";

        public static final String COLUMN_LEVEL = "car_ban_level";
        public static final String COLUMN_BLOCK_TYPE = "car_ban_block_type";

        /**
         * Builds a URI that adds the weather date to the end of the forecast content URI path.
         * This is used to query details about a single weather entry by date. This is what we
         * use for the detail view query. We assume a normalized date is passed to this method.
         *
         * @param date Normalized date in milliseconds
         * @return Uri to query details about a single weather entry
         */
        public static Uri buildWeatherUriWithDate(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }

        /**
         * Returns just the selection part of the weather query from a normalized today value.
         * This is used to get a weather forecast from today's date. To make this easy to use
         * in compound selection, we embed today's date as an argument in the query.
         *
         * @return The selection part of the weather query for today onwards
         */
        public static String getSqlSelectForTodayOnwards() {
            long today = AirToDateUtils.getTodayAtMidnight();
            return WeatherContract.WeatherEntry.COLUMN_DATE + " >= " + today;
        }

    }

}
