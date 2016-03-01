package com.michaelvescovo.moviehotness.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Michael Vescovo on 5/01/16.
 *
 */
public class MemoryModel extends DataModel {

    private List<MovieInterface> mCachedMovies = new ArrayList<>();
    private int mLastPage = 0;

    @Override
    public synchronized void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull final Integer page, @NonNull final LoadMoviesCallback callback) {
        checkNotNull(callback);

        if ((page != mLastPage) && (successor != null)) {
            successor.getMovies(context, sortBy, page, new LoadMoviesCallback() {
                @Override
                public void onMoviesLoaded(List<MovieInterface> movies) {
                    mCachedMovies.addAll(movies);
                    mLastPage++;
                    if (callback != null) {
                        callback.onMoviesLoaded(mCachedMovies);
                    }
                }
            });
        } else {
            if (callback != null) {
                callback.onMoviesLoaded(mCachedMovies);
            }
        }
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull final GetMovieCallback callback) {
        checkNotNull(movieId);
        checkNotNull(callback);
        if (mCachedMovies.size() > 0) {
            for (int i = 0; i < mCachedMovies.size(); i++) {
                if (mCachedMovies.get(i).getId().contentEquals(movieId)) {
                    callback.onMovieLoaded(mCachedMovies.get(i));
                }
            }
        } else if (successor != null) {
            successor.getMovie(context, movieId, new GetMovieCallback() {
                @Override
                public void onMovieLoaded(MovieInterface movie) {
                    callback.onMovieLoaded(movie);
                }
            });
        } else {
            callback.onMovieLoaded(null);
        }
    }

    @Override
    public void refreshData() {
        mCachedMovies.clear();
        mLastPage = 0;
    }
}
