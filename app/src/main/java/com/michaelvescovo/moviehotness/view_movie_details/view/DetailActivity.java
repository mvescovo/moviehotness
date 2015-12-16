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

import com.google.android.youtube.player.YouTubeIntents;
import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.util.BitmapHelper;
import com.michaelvescovo.moviehotness.view_movie_details.ViewMovieDetails;
import com.michaelvescovo.moviehotness.view_movie_details.ViewMovieDetailsInterface;
import com.michaelvescovo.moviehotness.view_movie_details.data.CloudModel;
import com.michaelvescovo.moviehotness.view_movie_details.data.DataModel;
import com.michaelvescovo.moviehotness.view_movie_details.data.DataResponseInterface;
import com.michaelvescovo.moviehotness.view_movie_details.data.DbModel;
import com.michaelvescovo.moviehotness.view_movie_details.entity.MovieInterface;
import com.michaelvescovo.moviehotness.view_movies.view.AboutFragment;

public class DetailActivity extends AppCompatActivity implements PlotFragment.OnFragmentInteractionListener, TrailersFragment.OnFragmentInteractionListener, PresenterInterface {
    private static final String TAG = "DetailActivity";
    DataModel mDbModel;
    DataModel mCloudModel;
    ViewMovieDetailsInterface mViewMovieDetails;
    int mPosition;
    String mMovieId;
    String mMovieTitle;
    Fragment mDetailFragment;
    Fragment mTrailersFragment;
    String mPlot;
    ProgressBar mProgressBar;
    BitmapHelper mBitmapHelper = new BitmapHelper();
    Menu mMenu;
    String mMovieTrailer1YouTubeId;

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
//        mTrailersFragment = new TrailersFragment();
//        fragmentTransaction.add(R.id.fragment_container_trailers, mTrailersFragment);
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
            mMovieTitle = movie.getTitle();
            ((DetailFragment) mDetailFragment).displayMovie(movie);
            if (movie.getTrailerCount() > 0) {
                mMovieTrailer1YouTubeId = movie.getTrailer(0).getYouTubeId();
                ImageView imageView = (ImageView) findViewById(R.id.main_trailer_play_button);
                imageView.setVisibility(View.VISIBLE);
            }
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
        enableCloseButtonAppBarLayout("Plot");
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
            transaction.replace(R.id.fragment_container, newFragment, "aboutFragment");
            transaction.addToBackStack(null);
            transaction.commit();
            enableCloseButtonAppBarLayout("About Movie Hotness");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        Fragment plotFragment = getSupportFragmentManager().findFragmentByTag("plotFragment");
        Fragment aboutFragment = getSupportFragmentManager().findFragmentByTag("aboutFragment");
        if ((plotFragment != null && plotFragment.isVisible()) || (aboutFragment != null && aboutFragment.isVisible())) {
            getSupportFragmentManager().popBackStack();
            disableCloseButtonAppBarLayout();
            return true;
        } else {
            return super.onSupportNavigateUp();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        disableCloseButtonAppBarLayout();
    }

    public void enableCloseButtonAppBarLayout(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
        }

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false, false);
        mMenu.findItem(R.id.action_about).setVisible(false);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(title);
    }

    public void disableCloseButtonAppBarLayout() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true, false);
        mMenu.findItem(R.id.action_about).setVisible(true);
    }

    public void playTrailer(View v) {
        Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(this, mMovieTrailer1YouTubeId, true, true);
        startActivity(intent);
    }
}