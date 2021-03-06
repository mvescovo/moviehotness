package com.michaelvescovo.moviehotness.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.michaelvescovo.android.moviehotness.data.DataModel;
import com.michaelvescovo.android.moviehotness.data.MovieInterface;

import java.util.List;


/**
 * Created by Michael Vescovo on 11/01/16.
 *
 */
public class FakeMemoryModel extends DataModel {

    List<MovieInterface> cachedMovies;

    @Override
    public void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull Integer page, @NonNull final LoadMoviesCallback callback) {
        if (null == cachedMovies) {
            successor.getMovies(context, sortBy, page, new LoadMoviesCallback() {
                @Override
                public void onMoviesLoaded(List<MovieInterface> movies) {
                    cachedMovies = movies;
                    callback.onMoviesLoaded(cachedMovies);
                }
            });
        } else {
            callback.onMoviesLoaded(cachedMovies);
        }
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull final GetMovieCallback callback) {
        if (null == cachedMovies) {
            successor.getMovie(context, movieId, new GetMovieCallback() {
                @Override
                public void onMovieLoaded(MovieInterface movie) {
                    callback.onMovieLoaded(movie);
                }
            });
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

    }
}
