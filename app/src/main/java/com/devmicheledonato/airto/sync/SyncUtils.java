package com.devmicheledonato.airto.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Michele on 28/05/2017.
 */

public class SyncUtils {

    private static final String TAG = SyncUtils.class.getSimpleName();

    private static final String AIRTO_SYNC_TAG = "airto-sync";

    private static boolean sInitialized;

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;


    synchronized public static void initialize(@NonNull final Context context) {
        Log.d(TAG, "initialize");
        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
        if (sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        startImmediateSync(context);
    }

    public static void startImmediateSync(@NonNull final Context context) {
        if (!sInitialized) return;
        Log.d(TAG, "startImmediateSync");
        context.startService(new Intent(context, WeatherSyncIntentService.class));
    }

    private static void scheduleFirebaseJobDispatcherSync(@NonNull Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job syncJob = dispatcher.newJobBuilder()
                .setService(FirebaseJobService.class)
                .setTag(AIRTO_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncJob);

    }
}
