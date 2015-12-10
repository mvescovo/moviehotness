package com.michaelvescovo.moviehotness.view_movies;

import com.michaelvescovo.moviehotness.view_movies.data.DataRequestInterface;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;
import com.michaelvescovo.moviehotness.view_movies.view.PresenterInterface;

/**
 * Created by Michael on 4/12/15.
 *
 */
public interface ViewMoviesInterface {
    void setDataRequestInterface(DataRequestInterface dataRequestInterface);

    void getMovies(int sortOrder);

    void setPresenterInterface(PresenterInterface presenterInterface);

    void displayMovies(MovieInterface movie, int sortBy);
}