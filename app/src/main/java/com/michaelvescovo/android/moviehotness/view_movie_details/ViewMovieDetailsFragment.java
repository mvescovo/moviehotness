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

package com.michaelvescovo.android.moviehotness.view_movie_details;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.google.android.youtube.player.YouTubeIntents;
import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.MovieInterface;
import com.michaelvescovo.android.moviehotness.data.MovieRepositories;
import com.michaelvescovo.android.moviehotness.data.MovieReviewInterface;
import com.michaelvescovo.android.moviehotness.data.MovieTrailerInterface;
import com.michaelvescovo.android.moviehotness.util.EspressoIdlingResource;

import java.io.File;
import java.util.ArrayList;

import static com.michaelvescovo.android.moviehotness.R.id.fab;

public class ViewMovieDetailsFragment extends Fragment implements ViewMovieDetailsContract.View {

    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String SORT_BY = "SORT_BY";
    private ViewMovieDetailsContract.UserActionsListener mActionsListener;
    private String mTitle;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private AppBarLayout mAppbar;
    private ImageView mBackdrop;
    private ImageView mDetailposterView;
    private TextView mReleaseDateView;
    private TextView mPlotView;
    private RatingBar mRatingBarView;
    private Button mMoreTrailersButton;
    private ImageView mMainTrailersButton;
    private ArrayList<MovieTrailerInterface> mTrailers;
    private ArrayList<MovieReviewInterface> mReviews;
    private TextView mReviewTitle;
    private TextView mReviewAuthorLabel;
    private TextView mReviewContent;
    private TextView mReviewContentReadMore;
    private TextView mReviewAuthor;
    private Button mReviewAllReviewsButton;
    private FloatingActionButton mFab;
    private MovieInterface mMovie;
    private Callback mCallback;
    private ShareActionProvider mShareActionProvider;
    private Palette mPalette;

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
        mActionsListener = new ViewMovieDetailsPresenter(getContext(),
                MovieRepositories.getMovieRepository(getContext(),
                        getArguments().getInt(SORT_BY)), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View root = inflater.inflate(R.layout.fragment_view_movie_details, container, false);

        mAppbar = (AppBarLayout) root.findViewById(R.id.appbar);

        mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mCallback.onSetSupportActionbar(mToolbar, true, null);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) root.findViewById(R.id.toolbar_layout);

        mBackdrop = (ImageView) root.findViewById(R.id.backdrop);

        mDetailposterView = (ImageView) root.findViewById(R.id.fragment_detail_poster);
        if (mDetailposterView != null) {
            ViewCompat.setTransitionName(mDetailposterView, getString(R.string.transition_poster));
        }

        final View plotSharedView = root.findViewById(R.id.plot_preview_shared_view);
        ViewCompat.setTransitionName(plotSharedView, getString(R.string.transition_plot));

        mReleaseDateView = (TextView) root.findViewById(R.id.fragment_detail_release_date);

        mPlotView = (TextView) root.findViewById(R.id.fragment_detail_plot);
        mPlotView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openFullPlot(
                        plotSharedView,
                        mTitle,
                        mPlotView.getText().toString()
                );
            }
        });

        TextView plotMore = (TextView) root.findViewById(R.id.fragment_detail_read_more);
        plotMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openFullPlot(
                        plotSharedView,
                        mTitle,
                        mPlotView.getText().toString()
                );
            }
        });

        mRatingBarView = (RatingBar) root.findViewById(R.id.fragment_detail_rating);

        mMoreTrailersButton = (Button) root.findViewById(R.id.more_trailers_button);
        mMoreTrailersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openAllTrailers(mTrailers);
            }
        });

        mMainTrailersButton = (ImageView) root.findViewById(R.id.main_trailer_play_button);
        mMainTrailersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.playFirstTrailer(mTrailers.get(0).getYouTubeId());
            }
        });

        // Reviews
        mReviewTitle = (TextView) root.findViewById(R.id.review_title);
        mReviewAuthorLabel = (TextView) root.findViewById(R.id.review_author_label);
        mReviewAuthor = (TextView) root.findViewById(R.id.review_author);
        mReviewContent = (TextView) root.findViewById(R.id.review_content);
        mReviewContentReadMore = (TextView) root.findViewById(R.id.review_content_read_more);
        mReviewAllReviewsButton = (Button) root.findViewById(R.id.review_all_reviews_button);

        final CardView reviewCard = (CardView) root.findViewById(R.id.full_review_card);
        ViewCompat.setTransitionName(reviewCard, getString(R.string.transition_review));

        mReviewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openFullReview(
                        reviewCard,
                        mReviews.get(0).getAuthor(),
                        mReviews.get(0).getContent());
            }
        });

        mReviewContentReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openFullReview(
                        reviewCard,
                        mReviews.get(0).getAuthor(),
                        mReviews.get(0).getContent()
                );
            }
        });

        mReviewAllReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.openAllReviews(mReviews);
            }
        });

        // Favourites FAB
        mFab = (FloatingActionButton) root.findViewById(fab);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String movieId = getArguments().getString(MOVIE_ID);
        mActionsListener.loadMovieDetails(movieId, false);
        mActionsListener.loadFavouriteFab(movieId);
    }

    @Override
    public void showMovieDetails(MovieInterface movie) {

        mMovie = movie;

        // Title
        if (movie.getTitle() != null) {
            mTitle = movie.getTitle();
        } else {
            mTitle = getContext().getResources().getString(R.string.title_unavailable);
        }

        if (mCollapsingToolbarLayout != null) {
            mCollapsingToolbarLayout.setTitle(mTitle);
        }

        // Release date
        if (movie.getReleaseDate() != null) {
            mReleaseDateView.setText(movie.getReleaseDate());
        } else {
            mReleaseDateView.setText(getContext().getString(R.string.release_date_unavailable));
        }

        // Poster
        if (mDetailposterView != null) {
            EspressoIdlingResource.increment();
            // Load poster from local storage if selecting from favourites
            if ((getArguments() != null) && (getArguments().getInt(SORT_BY) == getResources()
                    .getInteger(R.integer.favourite))) {
                String filename = movie.getId() + ".png";
                File file = new File(getContext().getFilesDir(), filename);
                Glide.with(this)
                        .load(file)
                        .asBitmap()
                        .error(R.drawable.no_image)
                        .into(new ImageViewTarget<Bitmap>(mDetailposterView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement();
                                }
                                mDetailposterView.setImageBitmap(resource);
                                getPalette(resource, new GetPaletteCallback() {
                                    @Override
                                    public void onPaletteCreated() {
                                        setToolbarColours();
                                    }
                                });                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement();
                                }
                            }
                        });
            } else {
                Glide.with(this)
                        .load("https://image.tmdb.org/t/p/"
                                + getResources().getString(R.string.poster_large)
                                + movie.getPosterUrl())
                        .asBitmap()
                        .error(R.drawable.no_image)
                        .into(new ImageViewTarget<Bitmap>(mDetailposterView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement();
                                }
                                mDetailposterView.setImageBitmap(resource);
                                if (!getResources().getBoolean(R.bool.two_pane)) {
                                    getPalette(resource, new GetPaletteCallback() {
                                        @Override
                                        public void onPaletteCreated() {
                                            setToolbarColours();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                super.onResourceReady(resource, glideAnimation);
                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement();
                                }
                            }
                        });

            }
        }

        // Rating
        if (movie.getVoteAverage() != null) {
            mRatingBarView.setStepSize((float) 0.25);
            mRatingBarView.setRating(Float.parseFloat(movie.getVoteAverage()) / 2);
            mRatingBarView.setVisibility(View.VISIBLE);
        }

        // Plot
        if (movie.getPlot() != null) {
            mPlotView.setText(movie.getPlot());
            if (movie.getPlot().length() > getResources()
                    .getInteger(R.integer.preview_text_max_chars)) {
                if (getView() != null) {
                    TextView textViewMore = (TextView) getView()
                            .findViewById(R.id.fragment_detail_read_more);
                    textViewMore.setVisibility(View.VISIBLE);
                }
            }
        } else {
            mPlotView.setText(getContext().getString(R.string.plot_unavailable));
        }

        // Backdrop
        if (mBackdrop != null) {
            EspressoIdlingResource.increment();
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/"
                            + getResources().getString(R.string.poster_xx_large)
                            + movie.getBackdropUrl())
                    .asBitmap()
                    .into(new ImageViewTarget<Bitmap>(mBackdrop) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                EspressoIdlingResource.decrement();
                            }
                            mBackdrop.setImageBitmap(resource);
                            if (getResources().getBoolean(R.bool.two_pane)) {
                                getPalette(resource, new GetPaletteCallback() {
                                    @Override
                                    public void onPaletteCreated() {
                                        setToolbarColours();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                EspressoIdlingResource.decrement();
                            }
                        }
                    });
        }

        // Trailers
        mTrailers = movie.getTrailers();
        if (movie.getTrailerCount() > 0) {
            mMoreTrailersButton.setVisibility(View.VISIBLE);
            mMainTrailersButton.setVisibility(View.VISIBLE);
        }

        // Reviews
        mReviews = movie.getReviews();
        if (movie.getReviewCount() > 0) {
            mReviewTitle.setVisibility(View.VISIBLE);
            mReviewAuthorLabel.setVisibility(View.VISIBLE);
            mReviewAuthor.setVisibility(View.VISIBLE);
            if (movie.getReview(0).getAuthor() != null) {
                mReviewAuthor.setText(movie.getReview(0).getAuthor());
            } else {
                mReviewAuthor.setText(getContext().getResources()
                        .getString(R.string.author_unavailable));
            }
            mReviewContent.setVisibility(View.VISIBLE);
            if (movie.getReview(0).getContent() != null) {
                mReviewContent.setText(movie.getReview(0).getContent());
                if (movie.getReview(0).getContent().length() > getResources()
                        .getInteger(R.integer.preview_text_max_chars)) {
                    mReviewContentReadMore.setVisibility(View.VISIBLE);
                }
            } else {
                mReviewAuthor.setText(getContext().getResources()
                        .getString(R.string.review_unavailable));
            }
            mReviewAllReviewsButton.setVisibility(View.VISIBLE);
        } else {
            mReviewTitle.setText(R.string.review_title_no_review);
            mReviewTitle.setVisibility(View.VISIBLE);
        }
    }

    private void getPalette(Bitmap bitmap, final GetPaletteCallback callback) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                mPalette = p;
                callback.onPaletteCreated();
            }
        });
    }

    private void setToolbarColours() {
        int defaultVibrant = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        int defaultVibrantTitleColour = ContextCompat.getColor(getContext(), R.color.textPrimaryDark);
        int defaultDarkVibrant = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        Palette.Swatch vibrantSwatch = mPalette.getVibrantSwatch();
        Palette.Swatch darkVibrantSwatch = mPalette.getDarkVibrantSwatch();

        if (vibrantSwatch != null && darkVibrantSwatch != null) {
            int vibrant = vibrantSwatch.getRgb();
            int vibrantTitleColour = vibrantSwatch.getTitleTextColor();
            int darkVibrant = darkVibrantSwatch.getRgb();

            mCollapsingToolbarLayout.setStatusBarScrimColor(darkVibrant);
            mCollapsingToolbarLayout.setContentScrimColor(vibrant);
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(vibrantTitleColour);
            mCallback.onSetThemeColours(mPalette, false);
        } else {
            mCollapsingToolbarLayout.setStatusBarScrimColor(defaultDarkVibrant);
            mCollapsingToolbarLayout.setContentScrimColor(defaultVibrant);
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(defaultVibrantTitleColour);
            mCallback.onSetThemeColours(mPalette, true);
        }
    }

    @Override
    public void showMissingMovie() {
        mTitle = "Movie not found.";
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity()
                .findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mTitle);
    }

    @Override
    public void setFavouriteFab(int imageResource, final boolean addFavourite) {
        mFab.setImageResource(imageResource);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addFavourite) {
                    mActionsListener.addFavouriteMovie(mMovie);
                } else {
                    mActionsListener.removeFavouriteMovie(mMovie.getId());
                }
            }
        });
    }

    @Override
    public void showSnackbar(int stringResource) {
        showSnackbar(getContext().getResources().getString(stringResource));
    }

    @Override
    public void showFirstTrailerUi(String youTubeId) {
        if (YouTubeIntents.isYouTubeInstalled(getContext())) {
            Intent intent = YouTubeIntents
                    .createPlayVideoIntentWithOptions(getContext(), youTubeId, true, true);
            startActivity(intent);
        } else {
            showSnackbar(getResources().getString(R.string.youtube_not_installed));
        }
    }

    @Override
    public void showFullPlotUi(View sharedView, String title, String plot) {
        mCallback.onFullPlotSelected(
                sharedView,
                mTitle,
                mPlotView.getText().toString()
        );
    }

    @Override
    public void showAllTrailersUi(ArrayList<MovieTrailerInterface> trailers) {
        mCallback.onAllTrailersSelected(mTrailers);
    }

    @Override
    public void showFullReview(View sharedView, String author, String content) {
        mCallback.onFullReviewSelected(
                sharedView,
                mReviews.get(0).getAuthor(),
                mReviews.get(0).getContent());
    }

    @Override
    public void showAllReviewsUi(ArrayList<MovieReviewInterface> reviews) {
        mCallback.onAllReviewsSelected(mReviews);
    }

    @Override
    public void showAttributionUi() {
        mCallback.onAboutSelected();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mTrailers.size() > 0) {
            menu.findItem(R.id.menu_item_share).setVisible(true);
            MenuItem item = menu.findItem(R.id.menu_item_share);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        }
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            // In two pane this action is already implemented in the movieDetailsFragment
            if (!getResources().getBoolean(R.bool.two_pane)) {
                mActionsListener.openAttribution();
            }
        } else if (id == R.id.menu_item_share) {
            Uri.Builder builder = new Uri.Builder();
            String url = builder.scheme("https")
                    .authority("www.youtube.com")
                    .appendPath("watch")
                    .appendQueryParameter("v", mTrailers.get(0).getYouTubeId())
                    .build()
                    .toString();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, mTitle + " "
                    + getResources().getText(R.string.share_message) + " " + url);
            intent.setType("text/plain");
            setShareIntent(intent);
            getActivity().startActivity(Intent.createChooser(intent,
                    getResources().getText(R.string.send_to)));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of detail clicks.
     */
    public interface Callback {

        void onSetSupportActionbar(@NonNull Toolbar toolbar, @NonNull Boolean upEnabled,
                                   @Nullable Integer homeAsUpIndicator);

        void onAboutSelected();

        void onFullPlotSelected(View sharedView, String title, String plot);

        void onAllTrailersSelected(ArrayList<MovieTrailerInterface> trailers);

        void onFullReviewSelected(View sharedView, String author, String content);

        void onAllReviewsSelected(ArrayList<MovieReviewInterface> reviews);

        void onSetThemeColours(Palette palette, Boolean restoreDefaults);
    }

    interface GetPaletteCallback {
        void onPaletteCreated();
    }

    public class InstallYoutube implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.google.android.youtube")));
            } catch (android.content.ActivityNotFoundException e) {
                e.printStackTrace();
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.youtube")));
            }
        }
    }

    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.nested_scroll_view),
                message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(getResources().getColor(R.color.black, getResources()
                    .newTheme()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbarView.setBackgroundColor(getResources().getColor(R.color.white, getResources()
                    .newTheme()));
        }
        snackbar.show();
    }
}