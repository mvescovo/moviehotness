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

package com.michaelvescovo.moviehotness;

import com.michaelvescovo.android.moviehotness.data.MovieRepository;
import com.michaelvescovo.moviehotness.data.FakeCloudModel;
import com.michaelvescovo.moviehotness.data.FakeDbModel;
import com.michaelvescovo.moviehotness.data.FakeMemoryModel;

/**
 * Created by Michael Vescovo on 9/01/16.
 *
 */
public class Injection {

    public static MovieRepository provideMemoryRepository() {
        return new FakeMemoryModel();
    }

    public static MovieRepository provideDbRepository() {
        return new FakeDbModel();
    }

    public static MovieRepository provideCloudRepository() {
        return new FakeCloudModel();
    }
}
