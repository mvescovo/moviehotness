package com.michaelvescovo.moviehotness.view_movie_details;

import com.michaelvescovo.moviehotness.view_movie_details.data.DataRequestInterface;
import com.michaelvescovo.moviehotness.view_movie_details.entity.MovieInterface;
import com.michaelvescovo.moviehotness.view_movie_details.view.PresenterInterface;

/**
 * Created by Michael on 6/12/15.
 *
 */
public interface ViewMovieDetailsInterface {
    void setDataRequestInterface(DataRequestInterface dataRequestInterface);

    void getMovie(String movieId);

    void setPresenterInterface(PresenterInterface presenterInterface);

    void displayMovie(MovieInterface movie);
}
