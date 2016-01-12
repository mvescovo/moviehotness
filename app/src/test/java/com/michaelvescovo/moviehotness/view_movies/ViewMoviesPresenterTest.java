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

package com.michaelvescovo.moviehotness.view_movies;

import android.content.Context;

import com.google.common.collect.Lists;
import com.michaelvescovo.moviehotness.model.Movie;
import com.michaelvescovo.moviehotness.model.MovieInterface;
import com.michaelvescovo.moviehotness.model.MovieRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by Michael Vescovo on 8/01/16.
 *
 */
public class ViewMoviesPresenterTest {

    private static List<MovieInterface> MOVIES = Lists.newArrayList(
            (MovieInterface) new Movie("286217", "The Martian", "2015-09-11", "/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg", "7.67", "During a manned mission to Mars, Astronaut Mark Watney is presumed dead after a fierce storm and left behind by his crew. But Watney has survived and finds himself stranded and alone on the hostile planet. With only meager supplies, he must draw upon his ingenuity, wit and spirit to subsist and find a way to signal to Earth that he is alive.", "/sy3e2e4JwdAtd2oZGA2uUilZe8j.jpg"),
            (MovieInterface) new Movie("102899", "Ant-Man", "2015-07-17", "/D6e8RJf2qUstnfkTslTXNTUAlT.jpg", "6.88", "Armed with the astonishing ability to shrink in scale but increase in strength, con-man Scott Lang must embrace his inner-hero and help his mentor, Dr. Hank Pym, protect the secret behind his spectacular Ant-Man suit from a new generation of towering threats. Against seemingly insurmountable obstacles, Pym and Lang must plan and pull off a heist that will save the world.", "/kvXLZqY0Ngl1XSw7EaMQO0C1CCj.jpg")
    );

    private static List<MovieInterface> EMPTY_MOVIES = new ArrayList<>(0);

    @Mock
    private MovieRepository mMovieRepository;

    @Mock
    private ViewMoviesContract.View mViewMoviesView;

    @Mock
    private Context mContext;

    @Captor
    private ArgumentCaptor<MovieRepository.LoadMoviesCallback> mLoadMoviesCallbackCaptor;

    private ViewMoviesPresenter mViewMoviesPresenter;

    @Before
    public void setupViewMoviesPresenter() {
        MockitoAnnotations.initMocks(this);
        mViewMoviesPresenter = new ViewMoviesPresenter(mContext, mMovieRepository, mViewMoviesView);
    }

    @Test
    public void loadMoviesFromRepositoryAndLoadIntoView() {

        // Given an initialized ViewMoviesPresenter with initialized movies
        // When loading of Movies is requested
        mViewMoviesPresenter.loadMovies(anyInt(), true);

        // Callback is captured and invoked with stubbed movies
        verify(mMovieRepository).getMovies(eq(mContext), anyInt(), mLoadMoviesCallbackCaptor.capture());
        mLoadMoviesCallbackCaptor.getValue().onMoviesLoaded(EMPTY_MOVIES);

        // Then progress indicator is hidden and movies are shown in UI
        verify(mViewMoviesView).setProgressIndicator(false);
        verify(mViewMoviesView).showMovies(EMPTY_MOVIES);
    }

    @Test
    public void clickOnMovie_ShowsDetailUi() {
        MovieInterface requestedMovie = new Movie("286217", "The Martian", "2015-10-02", "/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg", "7.67", "During a manned mission to Mars, Astronaut Mark Watney is presumed dead after a fierce storm and left behind by his crew. But Watney has survived and finds himself stranded and alone on the hostile planet. With only meager supplies, he must draw upon his ingenuity, wit and spirit to subsist and find a way to signal to Earth that he is alive.", "/sy3e2e4JwdAtd2oZGA2uUilZe8j.jpg");

        // When open movie details is requested
        mViewMoviesPresenter.openMovieDetails(requestedMovie);

        // Then movie detail UI is shown
        verify(mViewMoviesView).showMovieDetailUi(any(String.class));
    }

    @Test
    public void clickOnAboutMenuItem_ShowsAttributionUi() {
        // When open attribution/about is requested
        mViewMoviesPresenter.openAttribution();

        // Then attribution UI is shown
        verify(mViewMoviesView).showAttributionUi();
    }
}
