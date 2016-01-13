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


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeIntents;
import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.model.MovieInterface;
import com.michaelvescovo.moviehotness.model.MovieRepositories;
import com.michaelvescovo.moviehotness.model.MovieReviewInterface;
import com.michaelvescovo.moviehotness.model.MovieTrailerInterface;
import com.michaelvescovo.moviehotness.util.EspressoIdlingResource;
import com.michaelvescovo.moviehotness.view_all_trailers.ViewAllTrailersActivity;
import com.michaelvescovo.moviehotness.view_attribution.AttributionActivity;
import com.michaelvescovo.moviehotness.view_full_plot.ViewFullPlotActivity;
import com.michaelvescovo.moviehotness.view_full_review.ViewFullReviewActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewMovieDetailsFragment extends Fragment implements ViewMovieDetailsContract.View {
    private static final String TAG = "ViewMovieDetailsFragment";
    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String SORT_BY = "SORT_BY";
    private ViewMovieDetailsContract.UserActionsListener mActionsListener;
    private String mTitle;
    private ImageView mDetailposter;
    private TextView mReleaseDate;
    private TextView mPlot;
    private RatingBar mRatingBar;
    private ArrayList<MovieTrailerInterface> mTrailers;
    private Button mMoreTrailers;
    private ArrayList<MovieReviewInterface> mReviews;

    public static ViewMovieDetailsFragment newInstance(int sortBy, String movieId) {
        Bundle arguments = new Bundle();
        arguments.putInt(SORT_BY, sortBy);
        arguments.putString(MOVIE_ID, movieId);
        ViewMovieDetailsFragment fragment = new ViewMovieDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionsListener = new ViewMovieDetailsPresenter(getContext(), MovieRepositories.getMovieRepository(getContext(), getArguments().getInt(SORT_BY)), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_view_movie_details, container, false);

        mDetailposter = (ImageView) root.findViewById(R.id.fragment_detail_poster);
        mReleaseDate = (TextView) root.findViewById(R.id.fragment_detail_release_date);

        mPlot = (TextView) root.findViewById(R.id.fragment_detail_plot);
        mPlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openFullPlot(mTitle, mPlot.getText().toString());
            }
        });

        TextView plotMore = (TextView) root.findViewById(R.id.fragment_detail_read_more);
        plotMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openFullPlot(mTitle, mPlot.getText().toString());
            }
        });

        mRatingBar = (RatingBar) root.findViewById(R.id.fragment_detail_rating);
        mMoreTrailers = (Button) root.findViewById(R.id.more_trailers_button);

        mMoreTrailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openAllTrailers(mTrailers);
            }
        });

        ImageView playFirstTrailerButton = (ImageView) getActivity().findViewById(R.id.main_trailer_play_button);
        playFirstTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openFirstTrailer(mTrailers.get(0).getYouTubeId());
            }
        });

        TextView review = (TextView) root.findViewById(R.id.review_content);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openFullReview(mReviews.get(0).getAuthor(), mReviews.get(0).getContent());
            }
        });

        TextView reviewMore = (TextView) root.findViewById(R.id.review_content_read_more);
        reviewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openFullReview(mReviews.get(0).getAuthor(), mReviews.get(0).getContent());
            }
        });

        //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        String movieId = getArguments().getString(MOVIE_ID);
        mActionsListener.loadMovieDetails(movieId, false);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            // TODO set a progress indicator
        }
    }

    @Override
    public void showMovieDetails(MovieInterface movie) {

//        Log.i(TAG, "showMovieDetails: id: " + movie.getId());
//        Log.i(TAG, "showMovieDetails: title: " + movie.getTitle());
//        Log.i(TAG, "showMovieDetails: release date: " + movie.getReleaseDate());
//        Log.i(TAG, "showMovieDetails: poster: " + movie.getPosterUrl());
//        Log.i(TAG, "showMovieDetails: vote average: " + movie.getVoteAverage());
//        Log.i(TAG, "showMovieDetails: plot: " + movie.getPlot());
//        Log.i(TAG, "showMovieDetails: backdrop: " + movie.getBackdropUrl());

        // Title
        mTitle = movie.getTitle();
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mTitle);

        // Release date
        mReleaseDate.setText(movie.getReleaseDate());

        // Poster
        EspressoIdlingResource.increment();
        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/" + getResources().getString(R.string.poster_large) + movie.getPosterUrl()).into(mDetailposter, new Callback() {
            @Override
            public void onSuccess() {
                EspressoIdlingResource.decrement();
            }

            @Override
            public void onError() {
                EspressoIdlingResource.decrement();
            }
        });

        // Rating
        mRatingBar.setStepSize((float) 0.25);
        mRatingBar.setRating(Float.parseFloat(movie.getVoteAverage()) / 2);
        mRatingBar.setVisibility(View.VISIBLE);

        // Plot
        mPlot.setText(movie.getPlot());
        if (movie.getPlot().length() > getResources().getInteger(R.integer.short_plot_max_chars)) {
            if (getView() != null) {
                TextView textViewMore = (TextView) getView().findViewById(R.id.fragment_detail_read_more);
                textViewMore.setVisibility(View.VISIBLE);
            }
        }

        // Backdrop
        ImageView backdrop = (ImageView) getActivity().findViewById(R.id.backdrop);
        EspressoIdlingResource.increment();
        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/" + getResources().getString(R.string.poster_large) + movie.getBackdropUrl()).into(backdrop, new Callback() {
            @Override
            public void onSuccess() {
                EspressoIdlingResource.decrement();
            }

            @Override
            public void onError() {
                EspressoIdlingResource.decrement();
            }
        });

        // Trailers
        mTrailers = movie.getTrailers();
        if (movie.getTrailerCount() > 0) {
//            Log.i(TAG, "showMovieDetails: " + movie.getTrailer(0).getName() + movie.getTrailer(0).getYouTubeId());
            mMoreTrailers.setVisibility(View.VISIBLE);
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.main_trailer_play_button);
            imageView.setVisibility(View.VISIBLE);
        }

        // Reviews
        mReviews = movie.getReviews();
        if (movie.getReviewCount() > 0) {
            TextView reviewTitle = (TextView) getActivity().findViewById(R.id.review_title);
            reviewTitle.setVisibility(View.VISIBLE);
            TextView reviewAuthorLabel = (TextView) getActivity().findViewById(R.id.review_author_label);
            reviewAuthorLabel.setVisibility(View.VISIBLE);
            TextView reviewAuthor = (TextView) getActivity().findViewById(R.id.review_author);
            reviewAuthor.setVisibility(View.VISIBLE);
            reviewAuthor.setText(movie.getReview(0).getAuthor());
            TextView reviewContent = (TextView) getActivity().findViewById(R.id.review_content);
            reviewContent.setVisibility(View.VISIBLE);
            reviewContent.setText(movie.getReview(0).getContent());
            TextView reviewContentReadMore = (TextView) getActivity().findViewById(R.id.review_content_read_more);
            reviewContentReadMore.setVisibility(View.VISIBLE);
            Button reviewAllReviewsButton = (Button) getActivity().findViewById(R.id.review_all_reviews_button);
            reviewAllReviewsButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMissingMovie() {
        mTitle = "Movie not found.";
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mTitle);
    }

    @Override
    public void showFirstTrailerUi(String youTubeId) {
        Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(getContext(), youTubeId, true, true);
        startActivity(intent);
    }

    @Override
    public void showFullPlotUi(String title, String plot) {
        Intent intent = new Intent(getContext(), ViewFullPlotActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("plot", plot);
        startActivity(intent);
    }

    @Override
    public void showAllTrailersUi(ArrayList<MovieTrailerInterface> trailers) {
        Intent intent = new Intent(getContext(), ViewAllTrailersActivity.class);
        intent.putExtra("trailers", mTrailers);
        startActivity(intent);
    }

    @Override
    public void showFullReview(String author, String content) {
        Intent intent = new Intent(getContext(), ViewFullReviewActivity.class);
        intent.putExtra(ViewFullReviewActivity.EXTRA_AUTHOR, author);
        intent.putExtra(ViewFullReviewActivity.EXTRA_CONTENT, content);
        startActivity(intent);
    }

    @Override
    public void showAllReviewsUi(ArrayList<MovieReviewInterface> reviews) {
//        Intent intent = new Intent(getContext(), ViewAllTrailersActivity.class);
//        intent.putExtra("trailers", mTrailers);
//        startActivity(intent);
    }

    @Override
    public void showAttributionUi() {
        Intent intent = new Intent(getContext(), AttributionActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            mActionsListener.openAttribution();
        }

        return super.onOptionsItemSelected(item);
    }
}