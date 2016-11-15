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

package com.michaelvescovo.android.moviehotness.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Michael Vescovo on 17/01/16.
 *
 */
public class TestMovieHotnessProvider extends AndroidTestCase {
    /*
        This test checks to make sure that the content provider is registered correctly.
     */
    public void testProviderRegistry() {

        PackageManager pm = mContext.getPackageManager();

        // Define the component name based on the package name from the context and the MovieHotnessProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(), MovieHotnessProvider.class.getName());

        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieHotnessProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MovieHotnessContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieHotnessContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieHotnessProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {

        // content://com.michaelvescovo.android.moviehotness/movie/
        String type = mContext.getContentResolver().getType(MovieHotnessContract.MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.michaelvescovo.android.moviehotness/movie/
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_DIR_TYPE",
                MovieHotnessContract.MovieEntry.CONTENT_DIR_TYPE, type);

        // content://com.michaelvescovo.android.moviehotness/trailer/
        type = mContext.getContentResolver().getType(MovieHotnessContract.TrailerEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.michaelvescovo.android.moviehotness/trailer/
        assertEquals("Error: the TrailerEntry CONTENT_URI should return TrailerEntry.CONTENT_DIR_TYPE",
                MovieHotnessContract.TrailerEntry.CONTENT_DIR_TYPE, type);

        // content://com.michaelvescovo.android.moviehotness/preview/
        type = mContext.getContentResolver().getType(MovieHotnessContract.ReviewEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.michaelvescovo.android.moviehotness/review/
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry.CONTENT_TYPE",
                MovieHotnessContract.ReviewEntry.CONTENT_DIR_TYPE, type);
    }

    public void testMovieQuery() {

        // insert a test record into the database
        MovieHotnessDbHelper dbHelper = new MovieHotnessDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = TestUtilities.createMovieValues();
        long movieRowId = db.insert(MovieHotnessContract.MovieEntry.TABLE_NAME, null, contentValues);
        assertTrue("Unable to insert MovieEntry into the database", movieRowId != -1);
        db.close();

        // test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                MovieHotnessContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", cursor, contentValues);
    }

    public void testTrailerQuery() {

        // insert a test record into the database
        MovieHotnessDbHelper dbHelper = new MovieHotnessDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues trailerValues = TestUtilities.createTrailerValues(TestUtilities.TEST_MOVIE_ID);
        long trailerRowId = db.insert(MovieHotnessContract.TrailerEntry.TABLE_NAME, null, trailerValues);
        assertTrue("Unable to insert TrailerEntry into the database", trailerRowId != -1);
        db.close();

        // test the basic content provider query
        Cursor trailerCursor = mContext.getContentResolver().query(
                MovieHotnessContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicTrailerQuery", trailerCursor, trailerValues);
    }

    public void testReviewQuery() {

        // insert a test record into the database
        MovieHotnessDbHelper dbHelper = new MovieHotnessDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues reviewValues = TestUtilities.createReviewValues(TestUtilities.TEST_MOVIE_ID);
        long reviewRowId = db.insert(MovieHotnessContract.ReviewEntry.TABLE_NAME, null, reviewValues);
        assertTrue("Unable to insert ReviewEntry into the database", reviewRowId != -1);
        db.close();

        // test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                MovieHotnessContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicReviewQuery", cursor, reviewValues);
    }

    public void testInsertReadProvider() {
        /*
        * Test movie values
        *
        * */
        ContentValues movieValues = TestUtilities.createMovieValues();

        // Register a content observer for the insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieHotnessContract.MovieEntry.CONTENT_URI, true, tco);
        Uri movieUri = mContext.getContentResolver().insert(MovieHotnessContract.MovieEntry.CONTENT_URI, movieValues);

        // Did the content observer get called? If this fails, the insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // Verify we got a row back.
        assertTrue(movieUri != null);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is the primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieHotnessContract.MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.", cursor, movieValues);

        /*
        * Test trailer values
        *
        * */
        ContentValues trailerValues = TestUtilities.createTrailerValues(TestUtilities.TEST_MOVIE_ID);

        // Register a content observer for the insert.  This time, directly with the content resolver
        tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieHotnessContract.TrailerEntry.CONTENT_URI, true, tco);
        Uri trailerUri = mContext.getContentResolver().insert(MovieHotnessContract.TrailerEntry.CONTENT_URI, trailerValues);

        // Did the content observer get called? If this fails, the insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // Verify we got a row back.
        assertTrue(trailerUri != null);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is the primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                MovieHotnessContract.TrailerEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.", cursor, trailerValues);

        /*
        * Test review values
        *
        * */
        ContentValues reviewValues = TestUtilities.createReviewValues(TestUtilities.TEST_MOVIE_ID);

        // Register a content observer for the insert.  This time, directly with the content resolver
        tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieHotnessContract.ReviewEntry.CONTENT_URI, true, tco);
        Uri reviewUri = mContext.getContentResolver().insert(MovieHotnessContract.ReviewEntry.CONTENT_URI, reviewValues);

        // Did the content observer get called? If this fails, the insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // Verify we got a row back.
        assertTrue(reviewUri != null);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is the primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                MovieHotnessContract.ReviewEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.", cursor, reviewValues);
    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for a movie delete.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieHotnessContract.MovieEntry.CONTENT_URI, true, movieObserver);

        // Register a content observer for a trailer delete.
        TestUtilities.TestContentObserver trailerObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieHotnessContract.TrailerEntry.CONTENT_URI, true, trailerObserver);

        // Register a content observer for a review delete.
        TestUtilities.TestContentObserver reviewObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieHotnessContract.ReviewEntry.CONTENT_URI, true, reviewObserver);

        deleteAllRecordsFromProvider();

        // If either of these fail, most-likely not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        movieObserver.waitForNotificationOrFail();
        trailerObserver.waitForNotificationOrFail();
        reviewObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(movieObserver);
        mContext.getContentResolver().unregisterContentObserver(trailerObserver);
        mContext.getContentResolver().unregisterContentObserver(reviewObserver);
    }

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieHotnessContract.MovieEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MovieHotnessContract.TrailerEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MovieHotnessContract.ReviewEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieHotnessContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MovieHotnessContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Trailer table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MovieHotnessContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Review table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void testUpdateMovie() {
        ContentValues values = TestUtilities.createMovieValues();

        Uri movieUri = mContext.getContentResolver().insert(MovieHotnessContract.MovieEntry.CONTENT_URI, values);
        long movieRowId = ContentUris.parseId(movieUri);

        assertTrue(movieRowId != -1);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MovieHotnessContract.MovieEntry._ID, movieRowId);
        updatedValues.put(MovieHotnessContract.MovieEntry.COLUMN_TITLE, "Ant Man");

        // Create a cursor with observer to make sure the content provider is notifying
        // the observers as expected
        Cursor movieCursor = mContext.getContentResolver().query(MovieHotnessContract.MovieEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        movieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieHotnessContract.MovieEntry.CONTENT_URI, updatedValues, MovieHotnessContract.MovieEntry._ID + "= ?",
                new String[] { Long.toString(movieRowId)});
        assertEquals(count, 1);

        // If the code is failing here, it means that the content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        movieCursor.unregisterContentObserver(tco);
        movieCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                MovieHotnessContract.MovieEntry.CONTENT_URI,
                null,   // projection
                MovieHotnessContract.MovieEntry._ID + " = " + movieRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateMovie. Error validating movie entry update.", cursor, updatedValues);

        cursor.close();
    }

    public void testUpdateTrailer() {
        ContentValues values = TestUtilities.createTrailerValues(TestUtilities.TEST_MOVIE_ID);

        Uri trailerUri = mContext.getContentResolver().insert(MovieHotnessContract.TrailerEntry.CONTENT_URI, values);
        long trailerRowId = ContentUris.parseId(trailerUri);

        assertTrue(trailerRowId != -1);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MovieHotnessContract.TrailerEntry._ID, trailerRowId);
        updatedValues.put(MovieHotnessContract.TrailerEntry.COLUMN_NAME, "New updated trailer name");

        // Create a cursor with observer to make sure the content provider is notifying
        // the observers as expected
        Cursor trailerCursor = mContext.getContentResolver().query(MovieHotnessContract.TrailerEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        trailerCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieHotnessContract.TrailerEntry.CONTENT_URI, updatedValues, MovieHotnessContract.TrailerEntry._ID + "= ?",
                new String[] { Long.toString(trailerRowId)});
        assertEquals(count, 1);

        // If the code is failing here, it means that the content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        trailerCursor.unregisterContentObserver(tco);
        trailerCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                MovieHotnessContract.TrailerEntry.CONTENT_URI,
                null,   // projection
                MovieHotnessContract.TrailerEntry._ID + " = " + trailerRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateTrailer. Error validating trailer entry update.", cursor, updatedValues);

        cursor.close();
    }

    public void testUpdateReview() {
        ContentValues values = TestUtilities.createReviewValues(TestUtilities.TEST_MOVIE_ID);

        Uri reviewUri = mContext.getContentResolver().insert(MovieHotnessContract.ReviewEntry.CONTENT_URI, values);
        long reviewRowId = ContentUris.parseId(reviewUri);

        assertTrue(reviewRowId != -1);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MovieHotnessContract.ReviewEntry._ID, reviewRowId);
        updatedValues.put(MovieHotnessContract.ReviewEntry.COLUMN_AUTHOR, "New updated author");

        // Create a cursor with observer to make sure the content provider is notifying
        // the observers as expected
        Cursor reviewCursor = mContext.getContentResolver().query(MovieHotnessContract.ReviewEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        reviewCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieHotnessContract.ReviewEntry.CONTENT_URI, updatedValues, MovieHotnessContract.ReviewEntry._ID + "= ?",
                new String[] { Long.toString(reviewRowId)});
        assertEquals(count, 1);

        // If the code is failing here, it means that the content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        reviewCursor.unregisterContentObserver(tco);
        reviewCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                MovieHotnessContract.ReviewEntry.CONTENT_URI,
                null,   // projection
                MovieHotnessContract.ReviewEntry._ID + " = " + reviewRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateReview. Error validating review entry update.", cursor, updatedValues);

        cursor.close();
    }
}
