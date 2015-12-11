package com.michaelvescovo.moviehotness.view_movies.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    DataModel mDbModel;
    DataModel mCloudModel;
    ViewMoviesInterface mViewMovies;
    ViewPager mViewPager;
    MovieGridFragment mPopularMovieGridFragment;
    MovieGridFragment mHighestRatedMovieGridFragment;
    int mPopularProgress = 0;
    int mPopularSize = 0;
    boolean mPopularComplete = false;
    int mHighestRatedProgress = 0;
    int mHighestRatedSize = 0;
    boolean mHighestRatedComplete = false;
    ProgressBar mProgressBar;
    ProgressBar mProgressBarIndeterminate;

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
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
}