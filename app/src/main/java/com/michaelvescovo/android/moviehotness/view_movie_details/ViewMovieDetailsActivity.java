package com.michaelvescovo.android.moviehotness.view_movie_details;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.MovieReviewInterface;
import com.michaelvescovo.android.moviehotness.data.MovieTrailerInterface;
import com.michaelvescovo.android.moviehotness.util.EspressoIdlingResource;
import com.michaelvescovo.android.moviehotness.view_all_reviews.ViewAllReviewsActivity;
import com.michaelvescovo.android.moviehotness.view_all_trailers.ViewAllTrailersActivity;
import com.michaelvescovo.android.moviehotness.view_attribution.AttributionActivity;
import com.michaelvescovo.android.moviehotness.view_full_plot.ViewFullPlotActivity;
import com.michaelvescovo.android.moviehotness.view_full_review.ViewFullReviewActivity;

import java.util.ArrayList;

import static android.R.attr.author;


public class ViewMovieDetailsActivity extends AppCompatActivity implements ViewMovieDetailsFragment.Callback {

    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String SORT_BY = "SORT_BY";
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_movie_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        }

        int sortBy = getIntent().getIntExtra(SORT_BY, -1);
        String movieId = getIntent().getStringExtra(MOVIE_ID);
        initFragment(ViewMovieDetailsFragment.newInstance(sortBy, movieId));
    }

    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_scroll_view, detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    public void onSetSupportActionbar(@NonNull Toolbar toolbar, @NonNull Boolean upEnabled,
                                      @Nullable Integer homeAsUpIndicator) {
        mToolbar = toolbar;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(upEnabled);
            if (homeAsUpIndicator != null) {
                actionBar.setHomeAsUpIndicator(homeAsUpIndicator);
            }
        }
    }

    @Override
    public void onAboutSelected() {
        Intent intent = new Intent(this, AttributionActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this
        ).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onFullPlotSelected(View sharedView, String title, String plot) {
        Intent intent = new Intent(this, ViewFullPlotActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("plot", plot);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedView,
                ViewCompat.getTransitionName(sharedView)
        ).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onAllTrailersSelected(ArrayList<MovieTrailerInterface> trailers) {
        Intent intent = new Intent(this, ViewAllTrailersActivity.class);
        intent.putExtra(ViewAllTrailersActivity.TRAILERS, trailers);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this
        ).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onFullReviewSelected(View sharedView, String authory, String content) {
        Intent intent = new Intent(this, ViewFullReviewActivity.class);
        intent.putExtra(ViewFullReviewActivity.AUTHOR, author);
        intent.putExtra(ViewFullReviewActivity.CONTENT, content);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedView,
                ViewCompat.getTransitionName(sharedView)
        ).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onAllReviewsSelected(ArrayList<MovieReviewInterface> reviews) {
        Intent intent = new Intent(this, ViewAllReviewsActivity.class);
        intent.putExtra(ViewAllReviewsActivity.REVIEWS, reviews);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this
        ).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onSetThemeColours(Palette palette, Boolean restoreDefaults) {
        // Nothing to set on the Activity in single pane.
    }
}