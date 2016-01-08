package com.michaelvescovo.moviehotness.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Michael Vescovo on 5/01/16.
 *
 */
public class MemoryModel extends DataModel {

    @VisibleForTesting
    List<MovieInterface> mCachedMovies;

    @Override
    public void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull final LoadMoviesCallback callback) {
        checkNotNull(callback);
        if (mCachedMovies == null) {
            successor.getMovies(context, sortBy, new LoadMoviesCallback() {
                @Override
                public void onMoviesLoaded(List<MovieInterface> movies) {
                    mCachedMovies = ImmutableList.copyOf(movies);
                    callback.onMoviesLoaded(mCachedMovies);
                }
            });
        } else {
            callback.onMoviesLoaded(mCachedMovies);
        }
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull final GetMovieCallback callback) {
        checkNotNull(movieId);
        checkNotNull(callback);
        if (mCachedMovies == null) {
            successor.getMovie(context, movieId, new GetMovieCallback() {
                @Override
                public void onMovieLoaded(MovieInterface movie) {
                    callback.onMovieLoaded(movie);
                }
            });
        } else {
            for (int i = 0; i < mCachedMovies.size(); i++) {
                if (mCachedMovies.get(i).getId().contentEquals(movieId)) {
                    callback.onMovieLoaded(mCachedMovies.get(i));
                }
            }
        }
    }

    @Override
    public void saveMovie(@NonNull MovieInterface movie) {
        successor.saveMovie(movie);
    }

    @Override
    public void refreshData() {
        mCachedMovies =  null;
        successor.refreshData();
    }
}
