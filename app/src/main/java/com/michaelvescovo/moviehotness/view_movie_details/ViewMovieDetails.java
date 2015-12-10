package com.michaelvescovo.moviehotness.view_movie_details;

import com.michaelvescovo.moviehotness.view_movie_details.data.DataRequestInterface;
import com.michaelvescovo.moviehotness.view_movie_details.data.DataResponseInterface;
import com.michaelvescovo.moviehotness.view_movie_details.view.PresenterInterface;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;

/**
 * Created by Michael on 6/12/15.
 *
 */
public class ViewMovieDetails implements ViewMovieDetailsInterface, DataResponseInterface {
    private PresenterInterface mPresenterInterface;
    private DataRequestInterface mDataRequestInterface;

    public ViewMovieDetails(PresenterInterface presenterInterface, DataRequestInterface dataRequestInterface) {
        setPresenterInterface(presenterInterface);
        setDataRequestInterface(dataRequestInterface);
    }

    @Override
    public void setDataRequestInterface(DataRequestInterface dataRequestInterface) {
        mDataRequestInterface = dataRequestInterface;
    }

    @Override
    public void setPresenterInterface(PresenterInterface presenterInterface) {
        mPresenterInterface = presenterInterface;
    }

    @Override
    public void getMovie(int movieId) {
        mDataRequestInterface.getMovie(movieId);
    }

    @Override
    public void displayMovie(MovieInterface movie) {
        mPresenterInterface.displayMovie(movie);

        // TODO maybe save to memory model and db model here
    }
}
