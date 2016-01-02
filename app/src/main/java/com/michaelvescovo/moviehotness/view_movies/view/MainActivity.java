package com.michaelvescovo.moviehotness.view_movies.view;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.view_movies.Constants;
import com.michaelvescovo.moviehotness.view_movies.ViewMovies;
import com.michaelvescovo.moviehotness.view_movies.ViewMoviesInterface;
import com.michaelvescovo.moviehotness.view_movies.data.CloudModel;
import com.michaelvescovo.moviehotness.view_movies.data.DataModel;
import com.michaelvescovo.moviehotness.view_movies.data.DataResponseInterface;
import com.michaelvescovo.moviehotness.view_movies.data.DbModel;
import com.michaelvescovo.moviehotness.view_movies.entity.MoviePreviewInterface;

public class MainActivity extends AppCompatActivity implements PresenterInterface {
    private static final String TAG = "MainActivity";
    DataModel mDbModel; // not saved
    DataModel mCloudModel; // not saved
    ViewMoviesInterface mViewMovies; // not saved
    ViewPager mViewPager; // not saved
    MovieGridFragment mPopularMovieGridFragment; // not saved
    MovieGridFragment mHighestRatedMovieGridFragment; // not saved
    int mPopularProgress = 0; // not saved
    int mPopularSize = 0; // not saved
    boolean mPopularComplete = false; // not saved
    int mHighestRatedProgress = 0; // not saved
    int mHighestRatedSize = 0; // not saved
    boolean mHighestRatedComplete = false; // not saved
    ProgressBar mProgressBar; // not saved
    ProgressBar mProgressBarIndeterminate; // not saved
    Menu mMenu; // not saved
    TabLayout mTabLayout; // not saved

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        * Setup Application
        *
        * */
        mDbModel = new DbModel();
        mCloudModel = new CloudModel(this);
        mDbModel.setSuccessor(mCloudModel);
        mViewMovies = new ViewMovies(this, mDbModel);
        mDbModel.setDataResponseInterface((DataResponseInterface) mViewMovies);
        mCloudModel.setDataResponseInterface((DataResponseInterface) mViewMovies);

        /*
        * Setup view
        *
        * */
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case Constants.POPULAR:
                        if (!mPopularComplete) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mProgressBarIndeterminate.setVisibility(View.VISIBLE);
                            mProgressBar.setMax(mPopularSize);
                            mProgressBar.setProgress(mPopularProgress);
                        }
                        if (mPopularComplete) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mProgressBarIndeterminate.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case Constants.HIGHEST_RATED:
                        if (!mHighestRatedComplete) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mProgressBarIndeterminate.setVisibility(View.VISIBLE);
                            mProgressBar.setMax(mHighestRatedSize);
                            mProgressBar.setProgress(mHighestRatedProgress);
                        }
                        if (mHighestRatedComplete) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mProgressBarIndeterminate.setVisibility(View.INVISIBLE);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (savedInstanceState != null) {
            if (getSupportFragmentManager().findFragmentByTag("about_fragment") != null) {
                if (getSupportFragmentManager().findFragmentByTag("about_fragment").isVisible()) {
                    Log.i(TAG, "onCreate: about visible");
                }
            }
//            if (aboutFragment != null && aboutFragment.isVisible()) {
//                Log.i(TAG, "onCreate: about visible");
//                enableCloseButtonAppBarLayout();
//            }
            Log.i(TAG, "onCreate: about not visible");

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle;
        PagerAdapter pagerAdapter = new MovieSortByPagerAdapter(getSupportFragmentManager());

        bundle = new Bundle();
        bundle.putInt("sortBy", Constants.POPULAR);
        mPopularMovieGridFragment = new MovieGridFragment();
        mPopularMovieGridFragment.setArguments(bundle);
        ((MovieSortByPagerAdapter) pagerAdapter).addFragment(mPopularMovieGridFragment, "Popular");

        bundle = new Bundle();
        bundle.putInt("sortBy", Constants.HIGHEST_RATED);
        mHighestRatedMovieGridFragment = new MovieGridFragment();
        mHighestRatedMovieGridFragment.setArguments(bundle);
        ((MovieSortByPagerAdapter)pagerAdapter).addFragment(mHighestRatedMovieGridFragment, "Highest Rated");

        viewPager.setAdapter(pagerAdapter);
    }

    public void getMovies(int sortBy) {
        mViewMovies.getMovies(sortBy);
        mPopularComplete = false;
        mHighestRatedComplete = false;
        mProgressBar = (ProgressBar) findViewById(R.id.view_movies_progress);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBarIndeterminate = (ProgressBar) findViewById(R.id.view_movies_progress_indeterminate);
        mProgressBarIndeterminate.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayMovies(MoviePreviewInterface movie, int sortBy, int resultsSize) {
        switch (sortBy) {
            case Constants.POPULAR:
                mPopularSize = resultsSize;
                ((MovieGridFragment)((MovieSortByPagerAdapter)mViewPager.getAdapter()).getItem(0)).updateMovies(movie);
                updateProgressBar(sortBy);
                break;
            case Constants.HIGHEST_RATED:
                mHighestRatedSize = resultsSize;
                ((MovieGridFragment)((MovieSortByPagerAdapter)mViewPager.getAdapter()).getItem(1)).updateMovies(movie);
                updateProgressBar(sortBy);
                break;
        }
    }

    public void updateProgressBar(int sortBy) {
        switch (sortBy) {
            case Constants.POPULAR:
                mPopularProgress++;
                if (mPopularProgress == mPopularSize) {
                    mPopularComplete = true;
                }

                if (mPopularMovieGridFragment.getUserVisibleHint()) {
                    mProgressBar.setMax(mPopularSize);
                    mProgressBar.setProgress(mPopularProgress);
                    if (mPopularComplete) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mProgressBarIndeterminate.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case Constants.HIGHEST_RATED:
                mHighestRatedProgress++;
                if (mHighestRatedProgress == mHighestRatedSize) {
                    mHighestRatedComplete = true;
                }

                if (mHighestRatedMovieGridFragment.getUserVisibleHint()) {
                    mProgressBar.setMax(mHighestRatedSize);
                    mProgressBar.setProgress(mHighestRatedProgress);
                    if (mHighestRatedComplete) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mProgressBarIndeterminate.setVisibility(View.INVISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, mMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Fragment newFragment = new AboutFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment, "about_fragment");
            transaction.addToBackStack(null);
            transaction.commit();
            enableCloseButtonAppBarLayout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
//        Fragment plotFragment = getSupportFragmentManager().findFragmentByTag("plotFragment");
//        Fragment aboutFragment = getSupportFragmentManager().findFragmentByTag("aboutFragment");
//        if ((plotFragment != null && plotFragment.isVisible()) || (aboutFragment != null && aboutFragment.isVisible())) {
//            getSupportFragmentManager().popBackStack();
//            disableCloseButtonAppBarLayout();
//            return true;
//        } else {
//            return super.onSupportNavigateUp();
//        }
        getSupportFragmentManager().popBackStack();
        disableCloseButtonAppBarLayout();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Fragment aboutFragment = getSupportFragmentManager().findFragmentByTag("about_fragment");
            if (aboutFragment != null && aboutFragment.isVisible()) {
                disableCloseButtonAppBarLayout();
            }
        }
        super.onBackPressed();
    }

    public void enableCloseButtonAppBarLayout() {
        mViewPager.setVisibility(View.INVISIBLE);
        if (mMenu != null) {
            mMenu.findItem(R.id.action_about).setVisible(false);
        }
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.removeView(mTabLayout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("About Movie Hotness");
        toolbar.setNavigationIcon(R.drawable.ic_close_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: pressed back from about");
                onBackPressed();
            }
        });
    }

    public void disableCloseButtonAppBarLayout() {
        mViewPager.setVisibility(View.VISIBLE);
        mMenu.findItem(R.id.action_about).setVisible(true);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        if (mTabLayout == null) {
            appBarLayout.addView(mTabLayout);
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Movie Hotness");
        toolbar.setNavigationIcon(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}