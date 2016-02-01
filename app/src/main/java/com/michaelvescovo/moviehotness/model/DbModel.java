package com.michaelvescovo.moviehotness.model;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 4/12/15.
 *
 */
public class DbModel extends DataModel {

    @Override
    public synchronized void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull LoadMoviesCallback callback) {
        // TODO: 8/01/16 get movies from local db
        if (successor != null) {
            successor.getMovies(context, sortBy, callback);
        } else {
            List<MovieInterface> cachedMovies = new ArrayList<>();
            String id;
            String title;
            String releaseDate;
            String posterUrl;
            String voteAverage;
            String plot;
            String backdropUrl;
            Cursor cursor = context.getContentResolver().query(MovieHotnessContract.MovieEntry.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID);
                int titleIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_TITLE);
                int releaseDateIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_RELEASE_DATE);
                int posterUrlIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL);
                int voteAverageIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                int plotIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_PLOT);
                int backdropUrlIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_BACKDROP_URL);
                id = cursor.getString(idIndex);
                title = cursor.getString(titleIndex);
                releaseDate = cursor.getString(releaseDateIndex);
                posterUrl = cursor.getString(posterUrlIndex);
                voteAverage = cursor.getString(voteAverageIndex);
                plot = cursor.getString(plotIndex);
                backdropUrl = cursor.getString(backdropUrlIndex);
                MovieInterface movie = new Movie(id, title, releaseDate, posterUrl, voteAverage, plot, backdropUrl);
                cachedMovies.add(movie);
            }
            cursor.close();
            callback.onMoviesLoaded(cachedMovies);
        }
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull GetMovieCallback callback) {
        // TODO: 8/01/16 get movie from local db
        if (successor != null) {
            successor.getMovie(context, movieId, callback);
        }
    }

    @Override
    public void saveMovie(@NonNull MovieInterface movie) {
        // TODO: 8/01/16 save a movie to local db
    }

    @Override
    public void refreshData() {
        // TODO: 8/01/16 delete data from local db
    }
}
