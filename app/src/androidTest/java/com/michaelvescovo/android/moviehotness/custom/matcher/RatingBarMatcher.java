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

package com.michaelvescovo.android.moviehotness.custom.matcher;

import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Michael Vescovo on 10/01/16.
 *
 */
public class RatingBarMatcher {

    // Customer matcher to check rating bar rating
    public static Matcher<View> withRatingBar(String text) {
        return withRatingBar(is(text));
    }

    public static Matcher<View> withRatingBar(final Matcher<? extends CharSequence> charSequenceMatcher) {
        checkNotNull(charSequenceMatcher);
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with content description: ");
                charSequenceMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
//                return charSequenceMatcher.matches("7.67");
                return charSequenceMatcher.matches(Float.toString(((AppCompatRatingBar)view).getRating()));
            }
        };
    }
}
