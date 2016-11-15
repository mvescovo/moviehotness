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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by Michael Vescovo on 15/01/16.
 *
 */
public class TestDb extends AndroidTestCase {

    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieHotnessDbHelper.DATABASE_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {

        // build a HashSet of all of the table names to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieHotnessContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieHotnessContract.TrailerEntry.TABLE_NAME);
        tableNameHashSet.add(MovieHotnessContract.ReviewEntry.TABLE_NAME);

        // Create the db using the helper
        SQLiteDatabase db = new MovieHotnessDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: The database has not been created correctly", c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means the database doesn't contain all tables
        assertTrue("Error: The database was created without all tables", tableNameHashSet.isEmpty());

        // verify the tables contain the correct columns
        verifyMovieColumnsExist(db, c);
        verifyTrailerColumnsExist(db, c);
        verifyReviewColumnsExist(db, c);

        db.close();
        c.close();
    }

    public void verifyMovieColumnsExist(SQLiteDatabase db, Cursor c) {
        c = db.rawQuery("PRAGMA table_info(" + MovieHotnessContract.MovieEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieHotnessContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieHotnessContract.MovieEntry.COLUMN_TITLE);
        movieColumnHashSet.add(MovieHotnessContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnHashSet.add(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL);
        movieColumnHashSet.add(MovieHotnessContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        movieColumnHashSet.add(MovieHotnessContract.MovieEntry.COLUMN_PLOT);
        movieColumnHashSet.add(MovieHotnessContract.MovieEntry.COLUMN_BACKDROP_URL);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that the database doesn't contain all of the required MovieEntry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns", movieColumnHashSet.isEmpty());
    }

    public void verifyTrailerColumnsExist(SQLiteDatabase db, Cursor c) {
        c = db.rawQuery("PRAGMA table_info(" + MovieHotnessContract.TrailerEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> trailerColumnHashSet = new HashSet<String>();
        trailerColumnHashSet.add(MovieHotnessContract.TrailerEntry._ID);
        trailerColumnHashSet.add(MovieHotnessContract.TrailerEntry.COLUMN_YOUTUBE_ID);
        trailerColumnHashSet.add(MovieHotnessContract.TrailerEntry.COLUMN_NAME);
        trailerColumnHashSet.add(MovieHotnessContract.TrailerEntry.COLUMN_MOVIE_ID);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            trailerColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that the database doesn't contain all of the required MovieEntry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns", trailerColumnHashSet.isEmpty());
    }

    public void verifyReviewColumnsExist(SQLiteDatabase db, Cursor c) {
        c = db.rawQuery("PRAGMA table_info(" + MovieHotnessContract.ReviewEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> reviewColumnHashSet = new HashSet<String>();
        reviewColumnHashSet.add(MovieHotnessContract.ReviewEntry._ID);
        reviewColumnHashSet.add(MovieHotnessContract.ReviewEntry.COLUMN_AUTHOR);
        reviewColumnHashSet.add(MovieHotnessContract.ReviewEntry.COLUMN_CONTENT);
        reviewColumnHashSet.add(MovieHotnessContract.ReviewEntry.COLUMN_MOVIE_ID);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            reviewColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that the database doesn't contain all of the required MovieEntry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns", reviewColumnHashSet.isEmpty());
    }

    public void testMovieTable() {

        insertMovie();
    }

    public void testTrailerTable() {

        SQLiteDatabase db = new MovieHotnessDbHelper(mContext).getWritableDatabase();

        ContentValues contentValues = TestUtilities.createTrailerValues(TestUtilities.TEST_MOVIE_ID);

        // Insert ContentValues into database and get a row ID back
        Long rowId = db.insert(MovieHotnessContract.TrailerEntry.TABLE_NAME, null, contentValues);
        assertTrue("Error: Failure to insert Location Values", rowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(MovieHotnessContract.TrailerEntry.TABLE_NAME, null, null, null, null, null, null);

        // Move the cursor to a valid database row
        assertTrue(cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Not valid record", cursor, contentValues);

        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }

    public void testReviewTable() {

        SQLiteDatabase db = new MovieHotnessDbHelper(mContext).getWritableDatabase();

        ContentValues contentValues = TestUtilities.createReviewValues(TestUtilities.TEST_MOVIE_ID);

        // Insert ContentValues into database and get a row ID back
        Long rowId = db.insert(MovieHotnessContract.ReviewEntry.TABLE_NAME, null, contentValues);
        assertTrue("Error: Failure to insert Location Values", rowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(MovieHotnessContract.ReviewEntry.TABLE_NAME, null, null, null, null, null, null);

        // Move the cursor to a valid database row
        assertTrue(cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Not valid record", cursor, contentValues);

        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }

    public long insertMovie() {
        SQLiteDatabase db = new MovieHotnessDbHelper(mContext).getWritableDatabase();

        ContentValues contentValues = TestUtilities.createMovieValues();

        // Insert ContentValues into database and get a row ID back
        Long rowId = db.insert(MovieHotnessContract.MovieEntry.TABLE_NAME, null, contentValues);
        assertTrue("Error: Failure to insert Location Values", rowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(MovieHotnessContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);

        // Move the cursor to a valid database row
        assertTrue(cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Not valid record", cursor, contentValues);

        // Finally, close the cursor and database
        cursor.close();
        db.close();

        return rowId;
    }
}
