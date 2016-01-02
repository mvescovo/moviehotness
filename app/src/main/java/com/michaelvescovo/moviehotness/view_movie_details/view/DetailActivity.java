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
    private String mMovieId; // saved
    private String mCurrentFragment; // saved
    private MovieInterface mMovie; // saved
    private ViewMovieDetailsInterface mViewMovieDetails; // not saved
    private Menu mMenu; // not saved



    /***********************
     * Main lifecycle methods
     *
     * *********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Log.i(TAG, "onCreate: savedinstancestate does not exist");
            mMovieId = getIntent().getStringExtra("movie_id");
            mCurrentFragment = "details_fragment";
        } else {
            Log.i(TAG, "onCreate: savedinstancestate does exist");
            mMovieId = savedInstanceState.getString("movie_id");
            mCurrentFragment = savedInstanceState.getString("current_fragment");
            mMovie = (Movie)savedInstanceState.getSerializable("movie");
        }

        /*
        * Setup Application
        *
        * */
        DataModel dbModel = new DbModel();
        DataModel cloudModel = new CloudModel(this);
        dbModel.setSuccessor(cloudModel);
        mViewMovieDetails = new ViewMovieDetails(this, dbModel);
        dbModel.setDataResponseInterface((DataResponseInterface) mViewMovieDetails);
        cloudModel.setDataResponseInterface((DataResponseInterface) mViewMovieDetails);

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
        Fragment detailFragment = new DetailFragment();
        fragmentTransaction.replace(R.id.fragment_container_scroll_view, detailFragment, "detailFragment");
        fragmentTransaction.commit();

        if (savedInstanceState != null) {

        }

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("movie_id", mMovieId);

        Fragment plotFragment = getSupportFragmentManager().findFragmentByTag("plotFragment");
        Fragment aboutFragment = getSupportFragmentManager().findFragmentByTag("aboutFragment");
        Fragment trailersFragment = getSupportFragmentManager().findFragmentByTag("trailersFragment");

        if (plotFragment != null && plotFragment.isVisible()) {
            outState.putString("current_fragment", "plot_fragment");
        } else if (aboutFragment != null && aboutFragment.isVisible()) {
            outState.putString("current_fragment", "about_fragment");
        } else if (trailersFragment != null && trailersFragment.isVisible()) {
            outState.putString("current_fragment", "trailers_fragment");
        } else {
            outState.putString("current_fragment", "details_fragment");
        }

        outState.putSerializable("movie", (Movie)mMovie);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStart() {
        super.onStart();

        getMovie();
    }

    @Override
    protected void onResume() {
        super.onResume();

        supportInvalidateOptionsMenu();

        if (mCurrentFragment.equals("plot_fragment")) {
            openPlot(null);
        } else if (mCurrentFragment.equals("about_fragment")) {
            openAbout();
        } else if (mCurrentFragment.equals("trailers_fragment")) {
            openTrailers(null);
        }
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
            new BitmapHelper().loadBitmap(backdrop, getFilesDir() + "/" + "backdrop_" + movie.getId());
            ((DetailFragment)getSupportFragmentManager().findFragmentByTag("detailFragment")).displayMovie(movie);
            if (movie.getTrailerCount() > 0) {
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
            openAbout();
        }

        return super.onOptionsItemSelected(item);
    }


    /******************************
     * Helper methods
     *
     * ***************************/
    public void getMovie() {
        findViewById(R.id.view_movie_details_progress_indeterminate).setVisibility(View.VISIBLE);
        mViewMovieDetails.getMovie(mMovieId);
    }

    public void disableProgressBar() {
        findViewById(R.id.view_movie_details_progress_indeterminate).setVisibility(View.INVISIBLE);
    }

    public void openAbout() {
        enableCloseButtonAppBarLayout("About Movie Hotness");
        mCurrentFragment = "plot_fragment";
        Fragment newFragment = new AboutFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_scroll_view, newFragment, "aboutFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openPlot(View v) {
        enableCloseButtonAppBarLayout("Plot");
        mCurrentFragment = "plot_fragment";
        Fragment newFragment = PlotFragment.newInstance(mMovie.getPlot(), mMovie.getTitle());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_scroll_view, newFragment, "plotFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openTrailers(View v) {
        enableCloseButtonAppBarLayout("Trailers");
        mCurrentFragment = "trailers_fragment";
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

        if (mMenu != null) {
            mMenu.findItem(R.id.action_about).setVisible(false);
        }

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

        if (mMenu != null) {
            mMenu.findItem(R.id.action_about).setVisible(true);
        }

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true, false);

        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        nestedScrollView.setNestedScrollingEnabled(true);

    }

    public void playTrailer1(View v) {
        Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(this, mMovie.getTrailer(0).getYouTubeId(), true, true);
        startActivity(intent);
    }
}