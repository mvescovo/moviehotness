package com.michaelvescovo.moviehotness.view_movies.data;

/**
 * Created by Michael on 4/12/15.
 *
 */
public class DbModel extends DataModel {
    @Override
    public void getMovies(int sortBy) {
        if (successor != null) {
            successor.getMovies(sortBy);
        }
    }
}
