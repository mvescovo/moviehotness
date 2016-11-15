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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Michael Vescovo on 16/01/16.
 *
 */
public class MovieHotnessProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieHotnessDbHelper mOpenHelper;

    private static final SQLiteQueryBuilder sMovies;
    private static final SQLiteQueryBuilder sTrailers;
    private static final SQLiteQueryBuilder sReviews;

    static {
        sMovies = new SQLiteQueryBuilder();
        sMovies.setTables(MovieHotnessContract.MovieEntry.TABLE_NAME);

        sTrailers = new SQLiteQueryBuilder();
        sTrailers.setTables(MovieHotnessContract.TrailerEntry.TABLE_NAME);

        sReviews = new SQLiteQueryBuilder();
        sReviews.setTables(MovieHotnessContract.ReviewEntry.TABLE_NAME);
    }

    static final int MOVIE = 100;
    static final int TRAILER_WITH_MOVIE_ID = 200;
    static final int REVIEW_WITH_MOVIE_ID = 300;

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        uriMatcher.addURI(MovieHotnessContract.CONTENT_AUTHORITY, MovieHotnessContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(MovieHotnessContract.CONTENT_AUTHORITY, MovieHotnessContract.PATH_TRAILER, TRAILER_WITH_MOVIE_ID);
        uriMatcher.addURI(MovieHotnessContract.CONTENT_AUTHORITY, MovieHotnessContract.PATH_REVIEW, REVIEW_WITH_MOVIE_ID);

        // 3) Return the new matcher!
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieHotnessDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                retCursor = getMovies(projection, selection, selectionArgs, sortOrder);
                break;
            }
            case TRAILER_WITH_MOVIE_ID: {
                retCursor = getTrailers(projection, selection, selectionArgs, sortOrder);
                break;
            }
            case REVIEW_WITH_MOVIE_ID: {
                retCursor = getReviews(projection, selection, selectionArgs, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getMovies(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return sMovies.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTrailers(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return sTrailers.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviews(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return sReviews.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieHotnessContract.MovieEntry.CONTENT_DIR_TYPE;
            case TRAILER_WITH_MOVIE_ID:
                return MovieHotnessContract.TrailerEntry.CONTENT_DIR_TYPE;
            case REVIEW_WITH_MOVIE_ID:
                return MovieHotnessContract.ReviewEntry.CONTENT_DIR_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieHotnessContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieHotnessContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER_WITH_MOVIE_ID: {
                long _id = db.insert(MovieHotnessContract.TrailerEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieHotnessContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW_WITH_MOVIE_ID: {
                long _id = db.insert(MovieHotnessContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieHotnessContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE: {
                rowsDeleted = db.delete(MovieHotnessContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TRAILER_WITH_MOVIE_ID: {
                rowsDeleted = db.delete(MovieHotnessContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEW_WITH_MOVIE_ID: {
                rowsDeleted = db.delete(MovieHotnessContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE: {
                rowsUpdated = db.update(MovieHotnessContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case TRAILER_WITH_MOVIE_ID: {
                rowsUpdated = db.update(MovieHotnessContract.TrailerEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case REVIEW_WITH_MOVIE_ID: {
                rowsUpdated = db.update(MovieHotnessContract.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
