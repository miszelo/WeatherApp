package com.weatherka;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void onCityInput() {
        onView(withId(R.id.cityNameInput)).check(matches(isDisplayed()));
    }

    @Test
    public void checkEmptyEditText() {
        onView(withId(R.id.showBtn))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Kraków"))
                .check(matches(isDisplayed()));
    }
}