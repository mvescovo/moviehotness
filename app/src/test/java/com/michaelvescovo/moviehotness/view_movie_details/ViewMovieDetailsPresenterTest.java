/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * I've changed the code but it's similar to the example I'm following.
 * Possibly the best Android example I've ever seen so far. Really nice design.
 *
 * https://codelabs.developers.google.com/codelabs/android-testing
 *
 */

package com.michaelvescovo.moviehotness.view_movie_details;

import android.content.Context;

import com.michaelvescovo.moviehotness.model.Movie;
import com.michaelvescovo.moviehotness.model.MovieRepository;
import com.michaelvescovo.moviehotness.model.MovieTrailerInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by Michael Vescovo on 9/01/16.
 *
 */
public class ViewMovieDetailsPresenterTest {

    private static final String INVALID_ID = "INVALID_ID";
    private static final String ID_TEST = "286217";
    private static final String TITLE_TEST = "The Martian";
    private static final String RELEASE_DATE_TEST = "2015-09-11";
    private static final String POSTER_URL_TEST = "/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg";
    private static final String VOTE_AVERAGE_TEST = "7.67";
    private static final String PLOT_TEST = "During a manned mission to Mars, Astronaut Mark Watney is presumed dead after a fierce storm and left behind by his crew. But Watney has survived and finds himself stranded and alone on the hostile planet. With only meager supplies, he must draw upon his ingenuity, wit and spirit to subsist and find a way to signal to Earth that he is alive.";
    private static final String BACKDROP_URL_TEST = "/sy3e2e4JwdAtd2oZGA2uUilZe8j.jpg";

    @Mock
    private MovieRepository mMovieRepository;

    @Mock
    private ViewMovieDetailsContract.View mViewMovieDetailsView;

    @Mock
    private Context mContext;

    @Mock
    private ArrayList<MovieTrailerInterface> mTrailers;

    @Captor
    private ArgumentCaptor<MovieRepository.GetMovieCallback> mGetMovieCallbackCaptor;

    private ViewMovieDetailsPresenter mViewMovieDetailsPresenter;

    @Before
    public void setupViewMovieDetailsPresenter() {
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mViewMovieDetailsPresenter = new ViewMovieDetailsPresenter(mContext, mMovieRepository, mViewMovieDetailsView);
    }

    @Test
    public void getMovieFromRepositoryAndLoadIntoView() {
        // Given an initialised ViewMovieDetailsPresenter with stubbed movie
        Movie movie = new Movie(ID_TEST, TITLE_TEST, RELEASE_DATE_TEST, POSTER_URL_TEST, VOTE_AVERAGE_TEST, PLOT_TEST, BACKDROP_URL_TEST);

        // When ViewMovieDetailsPresenter is asked to load movie
        mViewMovieDetailsPresenter.loadMovieDetails(movie.getId(), false);

        // Then the movie is loaded from the model, a callback is captured and the progress indicator is shown
        verify(mViewMovieDetailsView).setProgressIndicator(true);
        verify(mMovieRepository).getMovie(eq(mContext), eq(movie.getId()), mGetMovieCallbackCaptor.capture());


        // When movie is finally loaded
        mGetMovieCallbackCaptor.getValue().onMovieLoaded(movie);

        // Then progress indicator is hidden and details are shown in UI
        verify(mViewMovieDetailsView).setProgressIndicator(false);
        verify(mViewMovieDetailsView).showMovieDetails(movie);
    }

    @Test
    public void getUnknownMovieFromRepositoryAndLoadIntoView() {
        // When loading a movie is requested with an invalid movie ID.
        mViewMovieDetailsPresenter.loadMovieDetails(INVALID_ID, false);

        // Then the movie with invalid id is attempted to load from model, callback is captured and progress indicator is shown.
        verify(mViewMovieDetailsView).setProgressIndicator(true);
        verify(mMovieRepository).getMovie(eq(mContext), eq(INVALID_ID), mGetMovieCallbackCaptor.capture());

        // When movie is finally loaded
        mGetMovieCallbackCaptor.getValue().onMovieLoaded(null);

        // Then progress indicator is hidden and missing movie UI is shown
        verify(mViewMovieDetailsView).setProgressIndicator(false);
        verify(mViewMovieDetailsView).showMissingMovie();
    }

    @Test
    public void getFirstTrailerAndCallYouTube() {
        // When loading the trailer is requested
        mViewMovieDetailsPresenter.openFirstTrailer(anyString());

        // Then the view is called to update the view
        verify(mViewMovieDetailsView).showFirstTrailerUi(anyString());
    }

    @Test
    public void getFullPlotAndLoadIntoView() {
        // When loading the full plot is requested
        mViewMovieDetailsPresenter.openFullPlot(TITLE_TEST, PLOT_TEST);

        // Then the view is called to update the view
        verify(mViewMovieDetailsView).showFullPlotUi(TITLE_TEST, PLOT_TEST);
    }

    @Test
    public void getAllTrailersAndLoadIntoView() {
        // When loading all trailers is requested
        mViewMovieDetailsPresenter.openAllTrailers(mTrailers);

        // Then the view is called to update the view
        verify(mViewMovieDetailsView).showAllTrailersUi(mTrailers);
    }

    @Test
    public void getAttributionAndLoadIntoView() {
        // When loading attribution is requested
        mViewMovieDetailsPresenter.openAttribution();

        // Then the view is called to update the view
        verify(mViewMovieDetailsView).showAttributionUi();
    }
}
