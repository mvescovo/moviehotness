package com.michaelvescovo.moviehotness.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Michael Vescovo on 5/01/16.
 *
 */
public class MemoryModel extends DataModel {

    List<MovieInterface> cachedMovies;

    @Override
    public synchronized void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull final LoadMoviesCallback callback) {
        checkNotNull(callback);
        if (cachedMovies == null) {
            if (successor != null) {
                successor.getMovies(context, sortBy, new LoadMoviesCallback() {
                    @Override
                    public void onMoviesLoaded(List<MovieInterface> movies) {
                        cachedMovies = movies;
                        callback.onMoviesLoaded(cachedMovies);
                    }
                });
            }

        } else {
            callback.onMoviesLoaded(cachedMovies);
        }
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull final GetMovieCallback callback) {
        checkNotNull(movieId);
        checkNotNull(callback);
        if (cachedMovies == null) {
            if (successor != null) {
                successor.getMovie(context, movieId, new GetMovieCallback() {
                    @Override
                    public void onMovieLoaded(MovieInterface movie) {
                        callback.onMovieLoaded(movie);
                    }
                });
            }
        } else {
            for (int i = 0; i < cachedMovies.size(); i++) {
                if (cachedMovies.get(i).getId().contentEquals(movieId)) {
                    callback.onMovieLoaded(cachedMovies.get(i));
                }
            }
        }
    }

    @Override
    public void refreshData() {
        cachedMovies =  null;
    }
}
