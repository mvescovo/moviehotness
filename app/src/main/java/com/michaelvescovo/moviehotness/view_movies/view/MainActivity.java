package com.michaelvescovo.moviehotness.view_movies.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.view_movies.Constants;
import com.michaelvescovo.moviehotness.view_movies.ViewMovies;
import com.michaelvescovo.moviehotness.view_movies.ViewMoviesInterface;
import com.michaelvescovo.moviehotness.view_movies.data.CloudModel;
import com.michaelvescovo.moviehotness.view_movies.data.DataModel;
import com.michaelvescovo.moviehotness.view_movies.data.DataResponseInterface;
import com.michaelvescovo.moviehotness.view_movies.data.DbModel;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;

public class MainActivity extends AppCompatActivity implements PresenterInterface {
    private static final String TAG = "MainActivity";
    DataModel mDbModel;
    DataModel mCloudModel;
    ViewMoviesInterface mViewMovies;
    ViewPager mViewPager;

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
        mDbModel.setDataResponseInterface((DataResponseInterface)mViewMovies);
        mCloudModel.setDataResponseInterface((DataResponseInterface)mViewMovies);

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
        // TODO maybe here use polymorphism?
        getMovies(Constants.POPULAR);
        getMovies(Constants.HIGHEST_RATED);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter pagerAdapter = new MovieSortByPagerAdapter(getSupportFragmentManager());
        ((MovieSortByPagerAdapter) pagerAdapter).addFragment(new MovieGridFragment(), "Popular");
        ((MovieSortByPagerAdapter)pagerAdapter).addFragment(new MovieGridFragment(), "Highest Rated");
        viewPager.setAdapter(pagerAdapter);
    }

    public void getMovies(int sortBy) {
        mViewMovies.getMovies(sortBy);
    }

    @Override
    public void displayMovies(MovieInterface movie, int sortBy) {
        switch (sortBy) {
            case Constants.POPULAR:
                ((MovieGridFragment)((MovieSortByPagerAdapter)mViewPager.getAdapter()).getItem(0)).updateMovies(movie);
                Log.i(TAG, "displayMovies: popular");
                break;
            case Constants.HIGHEST_RATED:
                ((MovieGridFragment)((MovieSortByPagerAdapter)mViewPager.getAdapter()).getItem(1)).updateMovies(movie);
                Log.i(TAG, "displayMovies: highest rated");
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
}