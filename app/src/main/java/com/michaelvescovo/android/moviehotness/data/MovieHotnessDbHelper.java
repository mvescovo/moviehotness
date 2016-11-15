package com.michaelvescovo.android.moviehotness.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Michael Vescovo on 15/01/16.
 *
 */
public class MovieHotnessDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "movie_hotness.db";

    public MovieHotnessDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieHotnessContract.MovieEntry.TABLE_NAME + " (" +
                MovieHotnessContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieHotnessContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieHotnessContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL + " TEXT, " +
                MovieHotnessContract.MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT, " +
                MovieHotnessContract.MovieEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                MovieHotnessContract.MovieEntry.COLUMN_BACKDROP_URL + " TEXT, " +

                " UNIQUE (" + MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieHotnessContract.TrailerEntry.TABLE_NAME + " (" +
                MovieHotnessContract.TrailerEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieHotnessContract.TrailerEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieHotnessContract.TrailerEntry.COLUMN_YOUTUBE_ID + " TEXT NOT NULL, " +
                MovieHotnessContract.TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + MovieHotnessContract.TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieHotnessContract.MovieEntry.TABLE_NAME + " (" + MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID + ")," +

                " UNIQUE (" + MovieHotnessContract.TrailerEntry.COLUMN_YOUTUBE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_TRAILER_TABLE);

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieHotnessContract.ReviewEntry.TABLE_NAME + " (" +
                MovieHotnessContract.ReviewEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieHotnessContract.ReviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieHotnessContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MovieHotnessContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + MovieHotnessContract.ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieHotnessContract.MovieEntry.TABLE_NAME + " (" + MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID + "));";

        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieHotnessContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieHotnessContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieHotnessContract.ReviewEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieHotnessContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieHotnessContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieHotnessContract.ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
