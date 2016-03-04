package com.michaelvescovo.moviehotness.view_all_trailers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.data.MovieTrailerInterface;

import java.util.ArrayList;

public class ViewAllTrailersActivity extends AppCompatActivity {

    public static final String TRAILERS = "trailers";
    ArrayList<MovieTrailerInterface> mTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_trailers);

        if (getIntent() != null) {
            mTrailers = (ArrayList<MovieTrailerInterface>) getIntent().getSerializableExtra(TRAILERS);
        }

        initFragment(ViewAllTrailersFragment.newInstance(mTrailers));
    }

    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_trailers, detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Make it so the activity closes instead of going up to it's parent (like the back button)
        finish();
        return true;
    }
}