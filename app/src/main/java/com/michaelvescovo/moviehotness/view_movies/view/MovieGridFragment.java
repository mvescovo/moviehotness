/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.michaelvescovo.moviehotness.view_movies.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.view_movies.entity.MoviePreviewInterface;

public class MovieGridFragment extends Fragment {
    private static final String TAG = "MovieGridFragment";
    private RecyclerView.Adapter mAdapter;
    private int mSortBy = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getInt("sortBy");
            mAdapter = (RecyclerView.Adapter) savedInstanceState.getSerializable("adapter");
        } else {
            mAdapter = new PosterAdapter(mSortBy);
            if (getArguments() != null) {
                mSortBy = getArguments().getInt("sortBy");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        return recyclerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity)getActivity()).getMovies(mSortBy);
    }

    public void updateMovies(MoviePreviewInterface movie) {
        if (mAdapter != null) {
            ((PosterAdapter)mAdapter).updateDataset(movie);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("sortBy", mSortBy);
        outState.putSerializable("adapter", (PosterAdapter) mAdapter);
    }
}