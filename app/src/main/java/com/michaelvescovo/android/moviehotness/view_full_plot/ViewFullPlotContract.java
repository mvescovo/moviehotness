package com.michaelvescovo.android.moviehotness.view_full_plot;

/**
 * Created by Michael Vescovo on 17/12/16.
 *
 */

public class ViewFullPlotContract {

    interface View {

        void showAttributionUi();
    }

    interface UserActionsListener {

        void openAttribution();
    }

}
