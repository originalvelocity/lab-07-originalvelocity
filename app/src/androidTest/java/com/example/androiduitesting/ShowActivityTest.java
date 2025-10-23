package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // initialize Espresso Intents before each test
        Intents.init();
    }

    @After
    public void tearDown() {
        // release Espresso Intents after each test
        Intents.release();
    }

    /**
     * Test Case 1: Check whether the activity correctly switched
     */
    @Test
    public void testActivitySwitch() {
        // add a city to the list
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("Edmonton"));
        onView(withId(R.id.button_confirm)).perform(click());

        // click on the city item in the list
        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0)
                .perform(click());

        // verify that ShowActivity was launched
        Intents.intended(IntentMatchers.hasComponent(ShowActivity.class.getName()));
    }

    /**
     * Test Case 2: Test whether the city name is consistent
     */
    @Test
    public void testCityNameConsistency() {
        String testCity = "Vancouver";

        // add a city to the list
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText(testCity));
        onView(withId(R.id.button_confirm)).perform(click());

        // click on the city item in the list
        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0)
                .perform(click());

        // verify that the correct city name is displayed in ShowActivity
        onView(withId(R.id.textView_cityName))
                .check(matches(withText(testCity)));

        // verify that the TextView is displayed
        onView(withId(R.id.textView_cityName))
                .check(matches(isDisplayed()));
    }

    /**
     * Test Case 3: Test the "back" button
     */
    @Test
    public void testBackButton() {
        String testCity = "San Jose";

        // add a city to the list
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText(testCity));
        onView(withId(R.id.button_confirm)).perform(click());

        // click on the city item to go to ShowActivity
        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0)
                .perform(click());

        // verify we're in ShowActivity
        onView(withId(R.id.button_back))
                .check(matches(isDisplayed()));

        // click the back button
        onView(withId(R.id.button_back))
                .perform(click());

        // verify we're back in MainActivity
        onView(withId(R.id.city_list))
                .check(matches(isDisplayed()));

        onView(withText(testCity))
                .check(matches(isDisplayed()));
    }
}