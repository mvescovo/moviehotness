package com.michaelvescovo.moviehotness.view_movie_details.view;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.view_movie_details.ViewMovieDetails;
import com.michaelvescovo.moviehotness.view_movie_details.ViewMovieDetailsInterface;
import com.michaelvescovo.moviehotness.view_movie_details.data.CloudModel;
import com.michaelvescovo.moviehotness.view_movie_details.data.DataModel;
import com.michaelvescovo.moviehotness.view_movie_details.data.DataResponseInterface;
import com.michaelvescovo.moviehotness.view_movie_details.data.DbModel;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;
import com.michaelvescovo.moviehotness.view_movies.view.AboutActivity;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements PlotFragment.OnFragmentInteractionListener, PresenterInterface {
    private static final String TAG = "DetailActivity";

    DataModel mDbModel;
    DataModel mCloudModel;
    ViewMovieDetailsInterface mViewMovieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int position = getIntent().getIntExtra("position", -1);
        String backdropUrl = getIntent().getStringExtra("backdrop_url");
        String title = getIntent().getStringExtra("title");

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

        DetailFragment fragment = new DetailFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        ImageView backdrop = (ImageView) findViewById(R.id.backdrop);

        if (position != -1) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }

            final String BASE_URL = "https://image.tmdb.org/t/p";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath("w780")
                    .build();

            Picasso.with(this)
                    .load(builtUri.toString() + backdropUrl)
                    .into(backdrop);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    @Override
    public void displayMovie(MovieInterface movie) {

    }

    public void openPlot(View v) {
        // Create new fragment and transaction
        Fragment newFragment = new PlotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("plot", getIntent().getStringExtra("plot"));
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}