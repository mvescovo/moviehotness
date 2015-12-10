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

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;

public class MovieGridFragment extends Fragment {
    private static final String TAG = "MovieGridFragment";
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mAdapter = new PosterAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(layoutManager);

        // make it so that the number of columns in the grid is automatic on different screen sizes and orientations
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewWidth = mRecyclerView.getMeasuredWidth();
                float cardViewWidth = getActivity().getResources().getDimension(R.dimen.card_layout_width);
                int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                if (newSpanCount < 1)
                    newSpanCount = 1;
                ((GridLayoutManager) layoutManager).setSpanCount(newSpanCount);
                layoutManager.requestLayout();
            }
        });

        return mRecyclerView;
    }

    public void updateMovies(MovieInterface movie) {
        ((PosterAdapter)mAdapter).updateDataset(movie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}