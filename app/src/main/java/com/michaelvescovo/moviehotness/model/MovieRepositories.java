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

import android.content.Context;

import com.michaelvescovo.moviehotness.Injection;
import com.michaelvescovo.moviehotness.R;

/**
 * Created by Michael Vescovo on 5/01/16.
 *
 */
public class MovieRepositories {

    private MovieRepositories() {
        // no instance
    }

    private static MovieRepository popularMemoryRepository = null;
    private static MovieRepository highestRatedMemoryRepository = null;

    public synchronized static MovieRepository getMovieRepository(Context context, int sortBy) {
        if (sortBy == context.getResources().getInteger(R.integer.popular)) {
            if (null == popularMemoryRepository) {
                popularMemoryRepository = Injection.provideMemoryRepository();
                MovieRepository dbRepository = Injection.provideDbRepository();
                MovieRepository cloudRepository = Injection.provideCloudRepository();

                ((DataModel) popularMemoryRepository).setSuccessor((DataModel) dbRepository);
                ((DataModel) dbRepository).setSuccessor((DataModel) cloudRepository);
            }
            return popularMemoryRepository;
        } else if (sortBy == context.getResources().getInteger(R.integer.highest_rated)) {
            if (null == highestRatedMemoryRepository) {
                highestRatedMemoryRepository = Injection.provideMemoryRepository();
                MovieRepository dbRepository = Injection.provideDbRepository();
                MovieRepository cloudRepository = Injection.provideCloudRepository();

                ((DataModel) highestRatedMemoryRepository).setSuccessor((DataModel) dbRepository);
                ((DataModel) dbRepository).setSuccessor((DataModel) cloudRepository);
            }
            return highestRatedMemoryRepository;
        } else {
            return null;
        }
    }
}
