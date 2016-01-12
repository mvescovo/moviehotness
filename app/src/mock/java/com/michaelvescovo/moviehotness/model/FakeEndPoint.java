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

package com.michaelvescovo.moviehotness.model;

import java.util.ArrayList;

/**
 * Created by Michael Vescovo on 12/01/16.
 *
 */
public final class FakeEndPoint {

    // Movie with a trailer and all images and other details available
    private static final String ID_TEST = "102899";
    private static final String TITLE_TEST = "Ant-Man";
    private static final String RELEASE_DATE_TEST = "2015-06-29";
    private static final String POSTER_URL_TEST = "/D6e8RJf2qUstnfkTslTXNTUAlT.jpg";
    private static final String VOTE_AVERAGE_TEST = "6.88";
    private static final String PLOT_TEST = "Armed with the astonishing ability to shrink in scale but increase in strength, con-man Scott Lang must embrace his inner-hero and help his mentor, Dr. Hank Pym, protect the secret behind his spectacular Ant-Man suit from a new generation of towering threats. Against seemingly insurmountable obstacles, Pym and Lang must plan and pull off a heist that will save the world.";
    private static final String BACKDROP_URL_TEST = "/kvXLZqY0Ngl1XSw7EaMQO0C1CCj.jpg";
    private static final Movie ANTMAN = new Movie(ID_TEST, TITLE_TEST, RELEASE_DATE_TEST, POSTER_URL_TEST, VOTE_AVERAGE_TEST, PLOT_TEST, BACKDROP_URL_TEST);
    private static final MovieTrailer TRAILER = new MovieTrailer("8E8N8EKbpV4", "The Martian Official Trailer 1 HD");

    static {
        DATA = new ArrayList<>();
        ANTMAN.addTrailer(TRAILER);
        addMovie(ANTMAN);
    }

    private final static ArrayList<MovieInterface> DATA;

    public static void addMovie(MovieInterface movie) {
        DATA.add(movie);
    }

    public static ArrayList<MovieInterface> loadPersistedMovies() {
        return DATA;
    }
}
