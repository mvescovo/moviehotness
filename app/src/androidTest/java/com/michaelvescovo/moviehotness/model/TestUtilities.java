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

package com.michaelvescovo.moviehotness.model;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Michael Vescovo on 15/01/16.
 *
 */
public class TestUtilities {

    static final String TEST_MOVIE_ID = "286217";
    static final String TEST_MOVIE_TITLE = "The Martian";
    static final String TEST_MOVIE_RELEASE_DATE = "2015-09-11";
    static final String TEST_MOVIE_POSTER_URL = "/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg";
    static final String TEST_MOVIE_VOTE_AVERAGE = "7.67";
    static final String TEST_MOVIE_PLOT = "During a manned mission to Mars, Astronaut Mark Watney is presumed dead after a fierce storm and left behind by his crew. But Watney has survived and finds himself stranded and alone on the hostile planet. With only meager supplies, he must draw upon his ingenuity, wit and spirit to subsist and find a way to signal to Earth that he is alive.";
    static final String TEST_MOVIE_BACKDROP_URL = "/sy3e2e4JwdAtd2oZGA2uUilZe8j.jpg";

    static final String TEST_TRAILER_YOUTUBE_ID = "#1ZiS7akYy4yA";
    static final String TEST_TRAILER_NAME = "Ant-Man Official Trailer";

    static final String TEST_REVIEW_AUTHOR = "Frank Ochieng";
    static final String TEST_REVIEW_CONTENT = "The Martian’ is definitely in the creative wheelhouse of filmmaker Ridley Scott whose Science Fiction sensibilities are grounded in colorful futuristic fantasies that tiptoe in grand whimsy.  The veteran auteur responsible for such pop cultural high-minded spectacles in ‘Alien’, ‘Blade Runner’ and even the mixed bag reception of ‘Prometheus’ certainly brings a sophisticated and thought-provoking vibe to the probing aura of ‘The Martian’.";

    static ContentValues createMovieValues() {

        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
        movieValues.put(MovieHotnessContract.MovieEntry.COLUMN_TITLE, TEST_MOVIE_TITLE);
        movieValues.put(MovieHotnessContract.MovieEntry.COLUMN_RELEASE_DATE, TEST_MOVIE_RELEASE_DATE);
        movieValues.put(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL, TEST_MOVIE_POSTER_URL);
        movieValues.put(MovieHotnessContract.MovieEntry.COLUMN_VOTE_AVERAGE, TEST_MOVIE_VOTE_AVERAGE);
        movieValues.put(MovieHotnessContract.MovieEntry.COLUMN_PLOT, TEST_MOVIE_PLOT);
        movieValues.put(MovieHotnessContract.MovieEntry.COLUMN_BACKDROP_URL, TEST_MOVIE_BACKDROP_URL);

        return movieValues;
    }

    static ContentValues createTrailerValues(String movieId) {

        ContentValues trailerValues = new ContentValues();
        trailerValues.put(MovieHotnessContract.TrailerEntry.COLUMN_MOVIE_ID, movieId);
        trailerValues.put(MovieHotnessContract.TrailerEntry.COLUMN_YOUTUBE_ID, TEST_TRAILER_YOUTUBE_ID);
        trailerValues.put(MovieHotnessContract.TrailerEntry.COLUMN_NAME, TEST_TRAILER_NAME);

        return trailerValues;
    }

    static ContentValues createReviewValues(String movieId) {

        ContentValues reviewValues = new ContentValues();
        reviewValues.put(MovieHotnessContract.ReviewEntry.COLUMN_MOVIE_ID, movieId);
        reviewValues.put(MovieHotnessContract.ReviewEntry.COLUMN_AUTHOR, TEST_REVIEW_AUTHOR);
        reviewValues.put(MovieHotnessContract.ReviewEntry.COLUMN_CONTENT, TEST_REVIEW_CONTENT);

        return reviewValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value '" + expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new com.michaelvescovo.moviehotness.utils.PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
