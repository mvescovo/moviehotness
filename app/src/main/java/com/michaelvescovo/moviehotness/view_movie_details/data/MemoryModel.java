package com.michaelvescovo.moviehotness.view_movie_details.data;

import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;

/**
 * Created by Michael on 7/12/15.
 *
 */
public class MemoryModel extends DataModel {
    private MovieInterface mMovie;

    @Override
    public void getMovie(int movidId) {
        if (mMovie != null) {
            mDataResponseInterface.displayMovie(mMovie);
        } else if (successor != null) {
            successor.getMovie(movidId);
        }
    }
}
