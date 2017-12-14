package com.devmicheledonato.airto;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.devmicheledonato.airto.data.WeatherContract;
import com.devmicheledonato.airto.sync.SyncUtils;
import com.devmicheledonato.airto.utils.AirToNetworkUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = MainActivity.class.getSimpleName();

    /*
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * weather data.
     */
    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SUMMARY,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON,
            WeatherContract.WeatherEntry.COLUMN_IPQA,
            WeatherContract.WeatherEntry.COLUMN_LEVEL,
            WeatherContract.WeatherEntry.COLUMN_BLOCK_TYPE
    };
    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_SUMMARY = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_MAX_TEMP = 3;
    public static final int INDEX_WEATHER_ICON = 4;
    public static final int INDEX_WEATHER_IPQA = 5;
    public static final int INDEX_CAR_BAN_LEVEL = 6;
    public static final int INDEX_CAR_BAN_BLOCK_TYPE = 7;

    /**
     * This ID will be used to identify the Loader responsible for loading our weather forecast.
     */
    private static final int ID_WEATHER_LOADER = 86;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mErrorData;

    private RecyclerView mRecyclerView;
    private WeatherAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean hideCarBan = false;
    private String[] mCarBan;
    private String mIpqaInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        initRecyclerView();
        initSwipeRefreshLayout();

        mErrorData = (TextView) findViewById(R.id.error_data_text_view);
        showData();

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(ID_WEATHER_LOADER, null, this);

        SyncUtils.initialize(this);
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView");
        mRecyclerView = (RecyclerView) findViewById(R.id.weather_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new WeatherAdapter(this);
        mAdapter.setOnIpqaIconClickListener(new WeatherAdapter.OnIpqaIconClickListener() {
            @Override
            public void onIpqaIconClick() {
                IpqaInfoFragment ipqaInfoFragment = IpqaInfoFragment.newInstance(mIpqaInfo);
                ipqaInfoFragment.show(getSupportFragmentManager(), IpqaInfoFragment.FRAGMENT_TAG);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSwipeRefreshLayout() {
        Log.d(TAG, "initSwipeRefreshLayout");
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipeColor));
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        Log.d(TAG, "onCreateLoader");

        switch (loaderId) {
            case ID_WEATHER_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        selection,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished " + data.getCount());
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.swapCursor(data);
        showCarTrafficBanIcon(data);
        saveIpqaInfo(data);
        if (data.getCount() != 0) {
            showData();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.swapCursor(null);
        showCarTrafficBanIcon(null);
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh ");
        SyncUtils.startImmediateSync(this);

        if (!AirToNetworkUtils.isOnline(this) && mAdapter.getItemCount() == 0) {
            showError(getString(R.string.no_network_no_weather_data));
        } else if (!AirToNetworkUtils.isOnline(this)) {
            showError(getString(R.string.no_network));
        }

    }

    private void showData() {
        if (mAdapter.getItemCount() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mErrorData.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mErrorData.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String errorMessage) {
        /* First, hide the loading indicator */
        mSwipeRefreshLayout.setRefreshing(false);
        /* Then, show error message */
        Snackbar.make(findViewById(R.id.activity_main),
                errorMessage, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        if (hideCarBan) {
            menu.findItem(R.id.action_car_traffic_ban).setVisible(false);
        }
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_car_traffic_ban) {
            CarTrafficBanFragment carTrafficBanFragment = CarTrafficBanFragment.newInstance(mCarBan);
            carTrafficBanFragment.show(getSupportFragmentManager(), CarTrafficBanFragment.FRAGMENT_TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCarTrafficBanIcon(Cursor carBanCursor) {
        Log.d(TAG, "showCarTrafficBanIcon");

        if (carBanCursor == null) {
            hideCarBan = true;
            // Invalidate menu so onCreateOptionsMenu(...) is called again
            invalidateOptionsMenu();
        } else if (carBanCursor.moveToFirst()) {

            mCarBan = new String[4];

            String levelToday = carBanCursor.getString(INDEX_CAR_BAN_LEVEL);
            String blockTypeToday = carBanCursor.getString(INDEX_CAR_BAN_BLOCK_TYPE);

            mCarBan[0] = levelToday;
            mCarBan[1] = blockTypeToday;

            if (carBanCursor.moveToNext()) {
                String levelTomorrow = carBanCursor.getString(INDEX_CAR_BAN_LEVEL);
                String blockTypeTomorrow = carBanCursor.getString(INDEX_CAR_BAN_BLOCK_TYPE);

                mCarBan[2] = levelTomorrow;
                mCarBan[3] = blockTypeTomorrow;
            }
        }
    }

    private void saveIpqaInfo(Cursor ipqaCursor) {
        Log.d(TAG, "saveIpqaInfo");

        if (ipqaCursor == null) {

        } else if (ipqaCursor.moveToFirst()) {
            mIpqaInfo = ipqaCursor.getString(INDEX_WEATHER_IPQA);
        }
    }

}
