/*
 * Copyright (C) 2014 The Android Open Source Project
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
 *
 * I have made modifications. Based on Udacity lesson.
 */

package com.michaelvescovo.moviehotness.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Michael Vescovo on 16/01/16.
 *
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final Uri TEST_MOVIE_DIR = MovieHotnessContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_TRAILER_WITH_MOVIE_ID_DIR = MovieHotnessContract.TrailerEntry.buildTrailerWithMovieIdUri(TestUtilities.TEST_MOVIE_ID);
    private static final Uri TEST_REVIEW_WITH_MOVIE_ID_DIR = MovieHotnessContract.ReviewEntry.buildReviewWithMovieIdUri(TestUtilities.TEST_MOVIE_ID);

    public void testUriMatcher() {

        UriMatcher testMatcher = MovieHotnessProvider.buildUriMatcher();

        assertEquals("Error: The MOVIE URI was matched incorrectly.", testMatcher.match(TEST_MOVIE_DIR), MovieHotnessProvider.MOVIE);
        assertEquals("Error: The TRAILER URI was matched incorrectly.", testMatcher.match(TEST_TRAILER_WITH_MOVIE_ID_DIR), MovieHotnessProvider.TRAILER_WITH_MOVIE_ID);
        assertEquals("Error: The REVIEW URI was matched incorrectly.", testMatcher.match(TEST_REVIEW_WITH_MOVIE_ID_DIR), MovieHotnessProvider.REVIEW_WITH_MOVIE_ID);
    }
}
