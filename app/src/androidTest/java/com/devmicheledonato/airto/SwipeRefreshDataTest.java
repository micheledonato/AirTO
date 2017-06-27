package com.devmicheledonato.airto;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by Michele on 30/05/2017.
 */

@RunWith(JUnit4.class)
public class SwipeRefreshDataTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void swipeRefreshDataLayout_callOnRefreshMethod() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.swipe_refresh))
                .perform(ViewActions.swipeDown())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void clickItemOnRecyclerView() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.weather_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(5, ViewActions.click()));
    }
}
