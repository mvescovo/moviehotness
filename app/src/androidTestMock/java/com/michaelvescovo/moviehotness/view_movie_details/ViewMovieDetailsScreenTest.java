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

package com.michaelvescovo.moviehotness.view_movie_details;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.model.FakeEndPoint;
import com.michaelvescovo.moviehotness.model.Movie;
import com.michaelvescovo.moviehotness.model.MovieTrailer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.michaelvescovo.moviehotness.custom.matcher.CollapsingToolBarLayoutTitleMatcher.withCollapsingToolbarLayoutTitle;
import static com.michaelvescovo.moviehotness.custom.matcher.ImageViewHasDrawableMatcher.hasDrawable;
import static com.michaelvescovo.moviehotness.custom.matcher.RatingBarMatcher.withRatingBar;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by Michael Vescovo on 9/01/16.
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewMovieDetailsScreenTest {

    private static final String ID_THE_MARTIAN = "286217";
    private static final String TITLE_THE_MARTIAN = "The Martian";
    private static final String RELEASE_DATE_THE_MARTIAN = "2015-09-11";
    private static final String POSTER_URL_THE_MARTIAN = "/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg";
    private static final String VOTE_AVERAGE_THE_MARTIAN = "7.67";
    private static final String VOTE_AVERAGE_RESULT_THE_MARTIAN = (float)Math.round(7.67 / 2 * 4) / 4f + "";
    private static final String PLOT_THE_MARTIAN = "During a manned mission to Mars, Astronaut Mark Watney is presumed dead after a fierce storm and left behind by his crew. But Watney has survived and finds himself stranded and alone on the hostile planet. With only meager supplies, he must draw upon his ingenuity, wit and spirit to subsist and find a way to signal to Earth that he is alive.";
    private static final String BACKDROP_URL_THE_MARTIAN = "/sy3e2e4JwdAtd2oZGA2uUilZe8j.jpg";
    private static final Movie THE_MARTIAN = new Movie(ID_THE_MARTIAN, TITLE_THE_MARTIAN, RELEASE_DATE_THE_MARTIAN, POSTER_URL_THE_MARTIAN, VOTE_AVERAGE_THE_MARTIAN, PLOT_THE_MARTIAN, BACKDROP_URL_THE_MARTIAN);
    private static final MovieTrailer TRAILER_THE_MARTIAN = new MovieTrailer("#1ZiS7akYy4yA", "Ant-Man Official Trailer");

    @Rule
    public ActivityTestRule<ViewMovieDetailsActivity> mViewMovieDetailsActivityTestRule = new ActivityTestRule<>(ViewMovieDetailsActivity.class, true, false);

    @Before
    public void intentWithStubbedMovieIdAndSortBy() {
        THE_MARTIAN.addTrailer(TRAILER_THE_MARTIAN);
        FakeEndPoint.addMovie(THE_MARTIAN);

        // Lazily start the Activity from the ActivityTestRule this time to inject the start Intent
        Intent intent = new Intent();
        intent.putExtra(ViewMovieDetailsActivity.EXTRA_SORT_BY, 0);
        intent.putExtra(ViewMovieDetailsActivity.EXTRA_MOVIE_ID, THE_MARTIAN.getId());
        mViewMovieDetailsActivityTestRule.launchActivity(intent);

        registerIdlingResource();
    }

    @Test
    public void movieDetails_DisplayedInUi() throws Exception {

        // Title
        onView(withId(R.id.toolbar_layout)).check(matches(withCollapsingToolbarLayoutTitle(TITLE_THE_MARTIAN)));

        // Release date
        onView(withId(R.id.fragment_detail_release_date)).check(matches(withText(RELEASE_DATE_THE_MARTIAN)));

        // Poster
        onView(withId(R.id.fragment_detail_poster)).check(matches(allOf(hasDrawable(), isDisplayed())));

        // Rating
        onView(withId(R.id.fragment_detail_rating)).check(matches(withRatingBar(VOTE_AVERAGE_RESULT_THE_MARTIAN)));

        // Plot
        onView(withId(R.id.fragment_detail_plot)).check(matches(withText(PLOT_THE_MARTIAN)));
        if (THE_MARTIAN.getPlot().length() > mViewMovieDetailsActivityTestRule.getActivity().getResources().getInteger(R.integer.short_plot_max_chars)) {
            onView(withId(R.id.fragment_detail_read_more)).check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.fragment_detail_read_more)).check(matches(not(isDisplayed())));
        }

        // Backdrop
        onView(withId(R.id.backdrop)).check(matches(allOf(hasDrawable(), isDisplayed())));

        // Trailers
        if (THE_MARTIAN.getTrailerCount() > 0) {
            onView(withId(R.id.main_trailer_play_button)).check(matches(isDisplayed()));
            onView(withId(R.id.more_trailers)).check(matches(withText(R.string.fragment_detail_view_trailers)));
        } else {
            onView(withId(R.id.main_trailer_play_button)).check(matches(not(isDisplayed())));
            onView(withId(R.id.more_trailers)).check(matches(not(isDisplayed())));
        }
//        Thread.sleep(15000);
    }

    private void registerIdlingResource() {
        Espresso.registerIdlingResources(mViewMovieDetailsActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(mViewMovieDetailsActivityTestRule.getActivity().getCountingIdlingResource());
    }
}
