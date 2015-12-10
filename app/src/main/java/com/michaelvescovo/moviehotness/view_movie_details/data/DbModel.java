package com.michaelvescovo.moviehotness.view_movie_details.data;

/**
 * Created by Michael on 7/12/15.
 *
 */
public class DbModel extends DataModel {

    @Override
    public void getMovie(int movieId) {
        if (successor != null) {
            successor.getMovie(movieId);
        }
    }
}
