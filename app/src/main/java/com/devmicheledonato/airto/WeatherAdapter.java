package com.devmicheledonato.airto;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmicheledonato.airto.utils.AirToDateUtils;
import com.devmicheledonato.airto.utils.AirToWeatherUtils;

/**
 * Created by Michele on 09/04/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private static final String TAG = WeatherAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_TOMORROW = 1;
    private static final int VIEW_TYPE_FUTURE_DAY = 2;

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    /*
     * Flag to determine if we want to use a separate view for the list item that represents
     * today. This flag will be true when the phone is in portrait mode and false when the phone
     * is in landscape. This flag will be set in the constructor of the adapter by accessing
     * boolean resources.
     */
    private boolean mUseTodayLayout;

    private Cursor mCursor;

    public WeatherAdapter(@NonNull Context context) {
        mContext = context;
        mUseTodayLayout = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder");

        int layoutId;
//        switch (viewType) {
//            case VIEW_TYPE_TODAY:
//                Log.d(TAG, "VIEW_TYPE_TODAY");
//                layoutId = R.layout.weather_today_list_item;
//                break;
//            case VIEW_TYPE_TOMORROW:
//                Log.d(TAG, "VIEW_TYPE_TOMORROW");
//                layoutId = R.layout.weather_tomorrow_list_item;
//                break;
//            case VIEW_TYPE_FUTURE_DAY:
//                Log.d(TAG, "VIEW_TYPE_FUTURE_DAY");
//                layoutId = R.layout.weather_list_item;
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
//        }

        //TODO REMOVE
        layoutId = R.layout.weather_list_item;

        //TODO, perchè non vedo il giorno corrente???

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

        mCursor.moveToPosition(position);

        // Weather icon
        String weatherIcon = mCursor.getString(MainActivity.INDEX_WEATHER_ICON);

        int weatherImageId = AirToWeatherUtils.getResourceIdForWeatherCondition(mContext, weatherIcon);

        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                // Get large icon
                break;
            case VIEW_TYPE_TOMORROW:
                // Get ? icon
                break;
            case VIEW_TYPE_FUTURE_DAY:
                // Get small icon
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        holder.weatherIcon.setImageResource(weatherImageId);

        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String dateString = AirToDateUtils.getReadableDateString(mContext, dateInMillis);
        holder.date.setText(dateString);

        holder.weatherDescription.setText(mCursor.getString(MainActivity.INDEX_WEATHER_SUMMARY));

        double minTemp = mCursor.getLong(MainActivity.INDEX_WEATHER_MIN_TEMP);
        String minString = AirToWeatherUtils.formatTemperature(mContext, minTemp);
        holder.min.setText(minString);

        double maxTemp = mCursor.getLong(MainActivity.INDEX_WEATHER_MAX_TEMP);
        String maxString = AirToWeatherUtils.formatTemperature(mContext, maxTemp);
        holder.max.setText(maxString);

    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else if (position == 1) {
            return VIEW_TYPE_TOMORROW;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView weatherIcon;
        TextView date;
        TextView weatherDescription;
        TextView min;
        TextView max;
        TextView ipqa;

        public ViewHolder(View itemView) {
            super(itemView);
            weatherIcon = (ImageView) itemView.findViewById(R.id.weather_icon);
            date = (TextView) itemView.findViewById(R.id.date);
            weatherDescription = (TextView) itemView.findViewById(R.id.weather_description);
            min = (TextView) itemView.findViewById(R.id.low_temperature);
            max = (TextView) itemView.findViewById(R.id.high_temperature);
        }
    }
}
