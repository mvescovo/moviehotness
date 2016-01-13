package com.michaelvescovo.moviehotness.view_all_reviews;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Michael Vescovo on 13/01/16.
 *
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ViewAllReviewsScreenTest {

    @Rule
    public ActivityTestRule<ViewAllReviewsActivity> mViewAllReviewsActivityTestRule = new ActivityTestRule<>(ViewAllReviewsActivity.class, true, false);



    @Test
    public void allReviews_DisplayedInUi() throws Exception {
//        fail();
    }
}
