package com.michaelvescovo.moviehotness.view_movie_details.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
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
import com.michaelvescovo.moviehotness.view_movie_details.entity.Movie;
import com.michaelvescovo.moviehotness.view_movie_details.entity.MovieInterface;
import com.michaelvescovo.moviehotness.view_movies.view.AboutFragment;

public class DetailActivity extends AppCompatActivity implements PlotFragment.OnFragmentInteractionListener, TrailersFragment.OnFragmentInteractionListener, PresenterInterface {
    private static final String TAG = "DetailActivity";
    private DataModel mDbModel;
    private DataModel mCloudModel;
    private ViewMovieDetailsInterface mViewMovieDetails;
    private String mMovieId;
    private String mMovieTitle;
    private Fragment mDetailFragment;
    private String mPlot;
    private ProgressBar mProgressBar;
    private BitmapHelper mBitmapHelper = new BitmapHelper();
    private Menu mMenu;
    private String mMovieTrailer1YouTubeId;
    private int mTrailerCount;
    private String mTrailerName;
    private MovieInterface mMovie;


    /***********************
    * Main lifecycle methods
    *
    * *********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
//        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mDetailFragment = new DetailFragment();
            fragmentTransaction.add(R.id.fragment_container_scroll_view, mDetailFragment, "detailFragment");
            fragmentTransaction.commit();
//        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMovie();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Fragment plotFragment = getSupportFragmentManager().findFragmentByTag("plotFragment");
        Fragment aboutFragment = getSupportFragmentManager().findFragmentByTag("aboutFragment");
        Fragment trailersFragment = getSupportFragmentManager().findFragmentByTag("trailersFragment");
        if ((plotFragment != null && plotFragment.isVisible())
                || (aboutFragment != null && aboutFragment.isVisible())
                || (trailersFragment != null && trailersFragment.isVisible())) {
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


    /******************************
     * Interface overridden methods
     *
     * ***************************/
    @Override
    public void displayMovie(MovieInterface movie) {
        if (movie != null) {
            mMovie = movie;
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            collapsingToolbarLayout.setTitle(movie.getTitle());
            ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
            mBitmapHelper.loadBitmap(backdrop, getFilesDir() + "/" + "backdrop_" + movie.getId());
            mPlot = movie.getPlot();
            mMovieTitle = movie.getTitle();
            ((DetailFragment) mDetailFragment).displayMovie(movie);
            mTrailerCount = movie.getTrailerCount();
            if (movie.getTrailerCount() > 0) {
                mMovieTrailer1YouTubeId = movie.getTrailer(0).getYouTubeId();
                mTrailerName = movie.getTrailer(0).getName();
                ImageView imageView = (ImageView) findViewById(R.id.main_trailer_play_button);
                imageView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.e(TAG, "displayMovie: error getting movie details");
            disableProgressBar();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /******
     * Menu
     *
     * ***/
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
            transaction.replace(R.id.fragment_container_scroll_view, newFragment, "aboutFragment");
            transaction.addToBackStack(null);
            transaction.commit();
            enableCloseButtonAppBarLayout("About Movie Hotness");
        }

        return super.onOptionsItemSelected(item);
    }


    /******************************
     * Helper methods
     *
     * ***************************/
    public void getMovie() {
        mProgressBar = (ProgressBar) findViewById(R.id.view_movie_details_progress_indeterminate);
        mProgressBar.setVisibility(View.VISIBLE);
        mViewMovieDetails.getMovie(mMovieId);
    }

    public void disableProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void openPlot(View v) {
        Fragment newFragment = PlotFragment.newInstance(mPlot, mMovieTitle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_scroll_view, newFragment, "plotFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        enableCloseButtonAppBarLayout("Plot");
    }

    public void openTrailers(View v) {
        enableCloseButtonAppBarLayout("Trailers");
        Fragment newFragment = TrailersFragment.newInstance((Movie) mMovie);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(getSupportFragmentManager().findFragmentByTag("detailFragment"));
        transaction.replace(R.id.fragment_container, newFragment, "trailersFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void enableCloseButtonAppBarLayout(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
        }

        mMenu.findItem(R.id.action_about).setVisible(false);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false, false);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(title);


        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        nestedScrollView.setNestedScrollingEnabled(false);
    }

    public void disableCloseButtonAppBarLayout() {
        getMovie();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }

        mMenu.findItem(R.id.action_about).setVisible(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true, false);

        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        nestedScrollView.setNestedScrollingEnabled(true);

    }

    public void playTrailer(View v) {
        Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(this, mMovieTrailer1YouTubeId, true, true);
        startActivity(intent);
    }
}