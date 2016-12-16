package com.michaelvescovo.android.moviehotness.view_all_trailers;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.michaelvescovo.android.moviehotness.BuildConfig;
import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.MovieTrailerInterface;
import com.michaelvescovo.android.moviehotness.view_movies.ViewMoviesActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Michael Vescovo on 22/02/16.
 *
 */
public class ViewAllTrailersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_all_trailers, container, false);
        List<MovieTrailerInterface> trailers = (List<MovieTrailerInterface>) getArguments().getSerializable(ViewAllTrailersActivity.TRAILERS);
        RecyclerView.Adapter adapter = new TrailerAdapter(trailers);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.all_trailers);

        if (!ViewMoviesActivity.mTwoPane) {
            ((ViewAllTrailersActivity)getActivity()).setSupportActionBar(toolbar);
            if (((ViewAllTrailersActivity)getActivity()).getSupportActionBar() != null) {
                ((ViewAllTrailersActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((ViewAllTrailersActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            }
        }
        return root;
    }

    public static ViewAllTrailersFragment newInstance(ArrayList<MovieTrailerInterface> trailers) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ViewAllTrailersActivity.TRAILERS, trailers);
        ViewAllTrailersFragment fragment = new ViewAllTrailersFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private class TrailerAdapter extends RecyclerView.Adapter implements Serializable {

        private List<MovieTrailerInterface> mDataset = new ArrayList<>();

        TrailerAdapter(List<MovieTrailerInterface> dataset) {
            mDataset = dataset;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(R.layout.trailer_card, parent, false);
            return new TrailerViewHolder(v, mDataset);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (mDataset.size() > position + 1) {
                View border = ((TrailerViewHolder) holder).getView().findViewById(R.id.border);
                border.setVisibility(View.VISIBLE);
            }

            TextView textViewName = (TextView) ((TrailerViewHolder) holder).getView().findViewById(R.id.trailer_name);
            YouTubeThumbnailView youTubeThumbnailView = (YouTubeThumbnailView) ((TrailerViewHolder) holder).getView().findViewById(R.id.youTube_thumbnail_trailer);

            if (textViewName != null) {
                textViewName.setText(mDataset.get(position).getName());
            }

            if (youTubeThumbnailView != null) {
                youTubeThumbnailView.initialize(BuildConfig.YOU_TUBE_DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo(mDataset.get(position).getYouTubeId());
                        if (youTubeThumbnailLoader.hasPrevious()) {
                            youTubeThumbnailLoader.release();
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    private class TrailerViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private List<MovieTrailerInterface> mAdapter;

        TrailerViewHolder(View itemView, List<MovieTrailerInterface> adapter) {
            super(itemView);
            mView = itemView;
            mAdapter = adapter;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String youTubeId = mAdapter.get(getAdapterPosition()).getYouTubeId();

                    if (YouTubeIntents.isYouTubeInstalled(v.getContext())) {
                        Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(v.getContext().getApplicationContext(), youTubeId, true, true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar.make(v, v.getContext().getResources().getString(R.string.youtube_not_installed), Snackbar.LENGTH_LONG);
                        View snackbarView = snackbar.getView();
                        int snackbarTextId = android.support.design.R.id.snackbar_text;
                        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setTextColor(v.getContext().getResources().getColor(R.color.black, v.getContext().getResources().newTheme()));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            snackbarView.setBackgroundColor(v.getContext().getResources().getColor(R.color.white, v.getContext().getResources().newTheme()));
                        }
                        snackbar.setAction(R.string.install, new InstallYoutube());
                        snackbar.show();
                    }
                }
            });
        }

        public View getView() {
            return mView;
        }

        class InstallYoutube implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                try {
                    mView.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.google.android.youtube"))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                catch (android.content.ActivityNotFoundException e) {
                    e.printStackTrace();
                    mView.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.youtube"))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        }
    }
}
