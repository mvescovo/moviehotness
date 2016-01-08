package com.michaelvescovo.moviehotness.model;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Michael on 4/12/15.
 *
 */
public class DbModel extends DataModel {

    @Override
    public void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull LoadMoviesCallback callback) {
        // TODO: 8/01/16 get movies from local db
        successor.getMovies(context, sortBy, callback);
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull GetMovieCallback callback) {
        // TODO: 8/01/16 get movie from local db
        successor.getMovie(context, movieId, callback);
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
