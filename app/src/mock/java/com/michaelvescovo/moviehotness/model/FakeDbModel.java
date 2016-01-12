package com.michaelvescovo.moviehotness.model;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Michael Vescovo on 11/01/16.
 *
 */
public class FakeDbModel extends DataModel {

    @Override
    public void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull final LoadMoviesCallback callback) {
        successor.getMovies(context, sortBy, new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<MovieInterface> movies) {
                callback.onMoviesLoaded(movies);
            }
        });
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull final GetMovieCallback callback) {
        successor.getMovie(context, movieId, new GetMovieCallback() {
            @Override
            public void onMovieLoaded(MovieInterface movie) {
                callback.onMovieLoaded(movie);
            }
        });
    }

    @Override
    public void saveMovie(@NonNull MovieInterface movie) {

    }

    @Override
    public void refreshData() {

    }
}
