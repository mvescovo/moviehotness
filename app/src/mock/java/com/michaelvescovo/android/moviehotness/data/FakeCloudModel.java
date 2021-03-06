package com.michaelvescovo.moviehotness.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.michaelvescovo.android.moviehotness.data.DataModel;

/**
 * Created by Michael Vescovo on 11/01/16.
 *
 */
public class FakeCloudModel extends DataModel {

    @Override
    public void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull Integer page, @NonNull LoadMoviesCallback callback) {
        callback.onMoviesLoaded(FakeEndPoint.loadPersistedMovies());
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull GetMovieCallback callback) {
        for (int i = 0; i < FakeEndPoint.loadPersistedMovies().size(); i++) {
            if (FakeEndPoint.loadPersistedMovies().get(i).getId().contentEquals(movieId)) {
                callback.onMovieLoaded(FakeEndPoint.loadPersistedMovies().get(i));
            }
        }
    }

    @Override
    public void refreshData() {
        // no data deleted on cloud.
    }
}
