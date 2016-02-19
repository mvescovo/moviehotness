package com.michaelvescovo.moviehotness.view_all_reviews;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.data.MovieReview;
import com.michaelvescovo.moviehotness.data.MovieReviewInterface;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.michaelvescovo.moviehotness.custom.matcher.WithItemText.withItemText;

/**
 * Created by Michael Vescovo on 13/01/16.
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewAllReviewsScreenTest {

    private static final String REVIEW_AUTHOR_THE_MARTIAN = "Frank Ochieng";
    private static final String REVIEW_CONTENT_THE_MARTIAN = "The Martian’ is definitely in the creative wheelhouse of filmmaker Ridley Scott whose Science Fiction sensibilities are grounded in colorful futuristic fantasies that tiptoe in grand whimsy.  The veteran auteur responsible for such pop cultural high-minded spectacles in ‘Alien’, ‘Blade Runner’ and even the mixed bag reception of ‘Prometheus’ certainly brings a sophisticated and thought-provoking vibe to the probing aura of ‘The Martian’.";
    private static final MovieReview REVIEW_THE_MARTIAN = new MovieReview(REVIEW_AUTHOR_THE_MARTIAN, REVIEW_CONTENT_THE_MARTIAN);
    private static ArrayList<MovieReviewInterface> mReviews =  new ArrayList<>();

    @Rule
    public ActivityTestRule<ViewAllReviewsActivity> mViewAllReviewsActivityTestRule = new ActivityTestRule<>(ViewAllReviewsActivity.class, true, false);

    @Before
    public void intentWithStubbedReviews() {

        mReviews.add(REVIEW_THE_MARTIAN);

        Intent intent = new Intent();
        intent.putExtra(ViewAllReviewsActivity.EXTRA_REVIEWS, mReviews);
        mViewAllReviewsActivityTestRule.launchActivity(intent);

    }

    @Test
    public void allReviews_DisplayedInUi() throws Exception {

        // Scroll notes list to added note, by finding its description
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText(REVIEW_AUTHOR_THE_MARTIAN))));

        // Verify note is displayed on screen
        onView(withItemText(REVIEW_AUTHOR_THE_MARTIAN)).check(matches(isDisplayed()));
    }
}
