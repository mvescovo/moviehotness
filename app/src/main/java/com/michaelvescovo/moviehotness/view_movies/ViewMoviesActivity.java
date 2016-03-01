package com.michaelvescovo.moviehotness.view_movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.data.MovieReviewInterface;
import com.michaelvescovo.moviehotness.data.MovieTrailerInterface;
import com.michaelvescovo.moviehotness.view_all_reviews.ViewAllReviewsFragment;
import com.michaelvescovo.moviehotness.view_all_trailers.ViewAllTrailersFragment;
import com.michaelvescovo.moviehotness.view_full_plot.ViewFullPlotFragment;
import com.michaelvescovo.moviehotness.view_full_review.ViewFullReviewFragment;
import com.michaelvescovo.moviehotness.view_movie_details.ViewMovieDetailsFragment;

import java.util.ArrayList;

public class ViewMoviesActivity extends AppCompatActivity implements ViewMoviesFragment.Callback, ViewMovieDetailsFragment.DetailSelectedCallback, ViewAllReviewsFragment.ReviewSelectedCallback {

    ViewPager mViewPager;
    ViewMoviesFragment mPopularMovieGridFragment;
    ViewMoviesFragment mHighestRatedMovieGridFragment;
    ViewMoviesFragment mFavouriteMovieGridFragment;
    Menu mMenu;
    TabLayout mTabLayout;
    public static boolean mTwoPane;
    private static final String DETAIL_FRAGMENT_TAG = "DETAIL_TAG";
    private static final String FULL_PLOT_FRAGMENT_TAG = "FULL_PLOT_TAG";
    private static final String FULL_REVIEW_FRAGMENT_TAG = "FULL_REVIEW_TAG";
    private static final String ALL_TRAILERS_FRAGMENT_TAG = "ALL_TRAILERS_TAG";
    private static final String ALL_REVIEWS_FRAGMENT_TAG = "ALL_REVIEWS_TAG";
    private ArrayList<String> mSelectedMovieIds = new ArrayList<>();
    private NetworkReceiver receiver = new NetworkReceiver();

    @Override
    public void onBackPressed() {
        int lastBackStackIndex = getSupportFragmentManager().getBackStackEntryCount() - 1;

        // Remove selected id only if the user presses back on a main detail screen
        if (mTwoPane) {
            if (getSupportFragmentManager().getBackStackEntryAt(lastBackStackIndex).getName().contentEquals(DETAIL_FRAGMENT_TAG)) {
                if (mSelectedMovieIds.size() > 0) {
                    mSelectedMovieIds.remove(mSelectedMovieIds.size() - 1);
                }
            }
        }

        super.onBackPressed();

        // Close the app if there are no more detail screens selected (so use doesn't see blank screen
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_movies);

        if (findViewById(R.id.fragment_container_scroll_view) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setElevation(0f);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        // Register BroadcastReceiver to track connection changes.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle;
        PagerAdapter pagerAdapter = new MovieSortByPagerAdapter(getSupportFragmentManager());

        bundle = new Bundle();
        bundle.putInt("sortBy", getResources().getInteger(R.integer.popular));
        mPopularMovieGridFragment = new ViewMoviesFragment();
        mPopularMovieGridFragment.setArguments(bundle);
        ((MovieSortByPagerAdapter) pagerAdapter).addFragment(mPopularMovieGridFragment, "Popular");

        bundle = new Bundle();
        bundle.putInt("sortBy", getResources().getInteger(R.integer.highest_rated));
        mHighestRatedMovieGridFragment = new ViewMoviesFragment();
        mHighestRatedMovieGridFragment.setArguments(bundle);
        ((MovieSortByPagerAdapter)pagerAdapter).addFragment(mHighestRatedMovieGridFragment, "Highest Rated");

        bundle = new Bundle();
        bundle.putInt("sortBy", getResources().getInteger(R.integer.favourite));
        mFavouriteMovieGridFragment = new ViewMoviesFragment();
        mFavouriteMovieGridFragment.setArguments(bundle);
        ((MovieSortByPagerAdapter)pagerAdapter).addFragment(mFavouriteMovieGridFragment, "Favourite");

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, mMenu);
        return true;
    }

    @Override
    public void onItemSelected(int sortBy, String movieId) {
        // Only select an item (movie poster) if it's not already selected
        if ((mSelectedMovieIds.size() == 0) || (!mSelectedMovieIds.get(mSelectedMovieIds.size() - 1).contentEquals(movieId))) {
            mSelectedMovieIds.add(movieId);
            ViewMovieDetailsFragment viewMovieDetailsFragment = ViewMovieDetailsFragment.newInstance(sortBy, movieId);
            getSupportFragmentManager().popBackStack(FULL_PLOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().popBackStack(ALL_TRAILERS_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_scroll_view, viewMovieDetailsFragment, DETAIL_FRAGMENT_TAG)
                    .addToBackStack(DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onFullPlotSelected(String title, String plot) {
        ViewFullPlotFragment viewFullPlotFragment = ViewFullPlotFragment.newInstance(title, plot);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_scroll_view, viewFullPlotFragment, FULL_PLOT_FRAGMENT_TAG)
                .addToBackStack(FULL_PLOT_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onAllTrailersSelected(ArrayList<MovieTrailerInterface> trailers) {
        ViewAllTrailersFragment viewAllTrailersFragment = ViewAllTrailersFragment.newInstance(trailers);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_scroll_view, viewAllTrailersFragment, ALL_TRAILERS_FRAGMENT_TAG)
                .addToBackStack(ALL_TRAILERS_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onFullReviewSelected(String author, String content) {
        ViewFullReviewFragment viewFullReviewFragment = ViewFullReviewFragment.newInstance(author, content);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_scroll_view, viewFullReviewFragment, FULL_REVIEW_FRAGMENT_TAG)
                .addToBackStack(FULL_REVIEW_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onAllReviewsSelected(ArrayList<MovieReviewInterface> reviews) {
        ViewAllReviewsFragment viewAllReviewsFragment = ViewAllReviewsFragment.newInstance(reviews);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_scroll_view, viewAllReviewsFragment, ALL_REVIEWS_FRAGMENT_TAG)
                .addToBackStack(ALL_TRAILERS_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                mPopularMovieGridFragment.networkChanged(true);
                mHighestRatedMovieGridFragment.networkChanged(true);
            } else if (networkInfo != null && !networkInfo.isConnected()) {
                mPopularMovieGridFragment.networkChanged(false);
                mHighestRatedMovieGridFragment.networkChanged(false);
            }
        }
    }
}