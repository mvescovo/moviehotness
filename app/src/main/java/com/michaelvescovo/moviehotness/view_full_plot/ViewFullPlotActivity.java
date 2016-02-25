package com.michaelvescovo.moviehotness.view_full_plot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.michaelvescovo.moviehotness.R;

public class ViewFullPlotActivity extends AppCompatActivity {

    public static final String PLOT = "plot";
    public static final String TITLE = "title";
    private String mPlot;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_plot);

        if (getIntent() != null) {
            mPlot = getIntent().getStringExtra(PLOT);
            mTitle = getIntent().getStringExtra(TITLE);
        }

        initFragment(ViewFullPlotFragment.newInstance(mTitle, mPlot));
    }

    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_full_plot, detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Make it so the activity closes instead of going up to it's parent (like the back button)
        finish();
        return true;
    }
}
