package com.michaelvescovo.moviehotness.view_movies.view;

import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;


/**
 * Created by Michael on 4/12/15.
 *
 */
public interface PresenterInterface {
    void displayMovies(MovieInterface movie, int sortBy, int resultsSize);
}
