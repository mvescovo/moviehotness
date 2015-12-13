package com.michaelvescovo.moviehotness.view_movie_details.view;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.util.BitmapHelper;
import com.michaelvescovo.moviehotness.view_movie_details.ViewMovieDetails;
import com.michaelvescovo.moviehotness.view_movie_details.ViewMovieDetailsInterface;
import com.michaelvescovo.moviehotness.view_movie_details.data.CloudModel;
import com.michaelvescovo.moviehotness.view_movie_details.data.DataModel;
import com.michaelvescovo.moviehotness.view_movie_details.data.DataResponseInterface;
import com.michaelvescovo.moviehotness.view_movie_details.data.DbModel;
import com.michaelvescovo.moviehotness.view_movie_details.entity.MovieInterface;
import com.michaelvescovo.moviehotness.view_movies.view.AboutActivity;

public class DetailActivity extends AppCompatActivity implements PlotFragment.OnFragmentInteractionListener, PresenterInterface {
    private static final String TAG = "DetailActivity";
    DataModel mDbModel;
    DataModel mCloudModel;
    ViewMovieDetailsInterface mViewMovieDetails;
    int mPosition;
    String mMovieId;
    Fragment mDetailFragment;
    String mPlot;
    ProgressBar mProgressBar;
    BitmapHelper mBitmapHelper = new BitmapHelper();
    Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPosition = getIntent().getIntExtra("position", -1);
        mMovieId = getIntent().getStringExtra("movie_id");

        super.onCreate(savedInstanceState);

        /*
        * Setup Application
        *
        * */
        mDbModel = new DbModel();
        mCloudModel = new CloudModel(this);
        mDbModel.setSuccessor(mCloudModel);
        mViewMovieDetails = new ViewMovieDetails(this, mDbModel);
        mDbModel.setDataResponseInterface((DataResponseInterface)mViewMovieDetails);
        mCloudModel.setDataResponseInterface((DataResponseInterface) mViewMovieDetails);

        /*
        * Setup view
        *
        * */
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mDetailFragment = new DetailFragment();
        fragmentTransaction.add(R.id.fragment_container, mDetailFragment);
        fragmentTransaction.commit();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void getMovie() {
        mProgressBar = (ProgressBar) findViewById(R.id.view_movie_details_progress_indeterminate);
        mProgressBar.setVisibility(View.VISIBLE);
        mViewMovieDetails.getMovie(mMovieId);
    }

    @Override
    public void displayMovie(MovieInterface movie) {
        if (movie != null) {
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            collapsingToolbarLayout.setTitle(movie.getTitle());
            ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
            mBitmapHelper.loadBitmap(backdrop, getFilesDir() + "/" + "backdrop_" + movie.getId());
            mPlot = movie.getPlot();
            ((DetailFragment) mDetailFragment).displayMovie(movie);
        } else {
            Log.e(TAG, "displayMovie: error getting movie details");
            disableProgressBar();
        }
    }

    public void disableProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void openPlot(View v) {
        Fragment newFragment = new PlotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("plot", mPlot);
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment, "plotFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        enablePlotAppBarLayout();
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
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("plotFragment");
        if (fragment != null && fragment.isVisible()) {
            getSupportFragmentManager().popBackStack();
            disablePlotAppBarLayout();
            return true;
        } else {
            return super.onSupportNavigateUp();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        disablePlotAppBarLayout();
    }

    public void enablePlotAppBarLayout() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
        }

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false, false);
        mMenu.findItem(R.id.action_about).setVisible(false);
    }

    public void disablePlotAppBarLayout() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true, false);
        mMenu.findItem(R.id.action_about).setVisible(true);
    }
}