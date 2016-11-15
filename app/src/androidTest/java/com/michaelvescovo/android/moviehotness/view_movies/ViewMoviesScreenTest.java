/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * I've changed the code but it's similar to the example I'm following.
 * Possibly the best Android example I've ever seen so far. Really nice design.
 *
 * https://codelabs.developers.google.com/codelabs/android-testing
 *
 */

package com.michaelvescovo.android.moviehotness.view_movies;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.michaelvescovo.android.moviehotness.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Michael Vescovo on 9/01/16.
 *
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ViewMoviesScreenTest {

    @Rule
    public ActivityTestRule<ViewMoviesActivity> mViewMoviesActivityTestRule = new ActivityTestRule<>(ViewMoviesActivity.class);

    @Test
    public void clickAboutMenuItem_opensAttributionUi() throws Exception {
        // Open the menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click on the about menu item
        onView(withText("About")).perform(click());

        // Check if the attribution screen is displayed
        onView(withId(R.id.activity_about_attribution)).check(matches(isDisplayed()));
    }
}
