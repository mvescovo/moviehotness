package com.michaelvescovo.moviehotness.view_movies;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.michaelvescovo.moviehotness.R;


public class ViewMoviesActivity extends AppCompatActivity {

    ViewPager mViewPager;
    ViewMoviesFragment mPopularMovieGridFragment;
    ViewMoviesFragment mHighestRatedMovieGridFragment;
    Menu mMenu;
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
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

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, mMenu);
        return true;
    }
}