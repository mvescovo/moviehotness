package com.michaelvescovo.android.moviehotness.view_movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.Movie;
import com.michaelvescovo.android.moviehotness.data.MovieHotnessContract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Michael Vescovo on 19/02/16.
 *
 */
public class PosterCursorAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ViewMoviesContract.UserActionsListener mActionsListener;
    private Cursor mCursor;

    public PosterCursorAdapter(Context context, ViewMoviesContract.UserActionsListener actionsListener) {
        mContext = context;
        mActionsListener = actionsListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_preview_card, parent, false);
        return new MovieCursorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Posters are from local storage when getting details from local db. PosterUrl holds the full filename.
        ImageView imageView = (ImageView) ((MovieCursorViewHolder) holder).getView().findViewById(R.id.poster_image);
        int posterUrlColumnIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL);
        String posterUrl;

        if (mCursor != null) {
            mCursor.moveToPosition(position);
            posterUrl = mCursor.getString(posterUrlColumnIndex);

            String filename = posterUrl;
            File file = new File(mContext.getFilesDir(), filename);
            Picasso.with(mContext).load(file).error(R.drawable.no_image).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    public void swapCursor(Cursor movies) {
        mCursor = movies;
        notifyDataSetChanged();
    }

    public class MovieCursorViewHolder extends RecyclerView.ViewHolder  {

        private View mView;

        public MovieCursorViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID);
                    int titleIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_TITLE);
                    int releaseDateIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_RELEASE_DATE);
                    int posterUrlIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL);
                    int voteAverageIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                    int plotIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_PLOT);
                    int backdropUrlIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_BACKDROP_URL);

                    mCursor.moveToPosition(getAdapterPosition());
                    String id = mCursor.getString(idIndex);
                    String title = mCursor.getString(titleIndex);
                    String releaseDate = mCursor.getString(releaseDateIndex);
                    String posterUrl = mCursor.getString(posterUrlIndex);
                    String voteAverage = mCursor.getString(voteAverageIndex);
                    String plot = mCursor.getString(plotIndex);
                    String backdropUrl = mCursor.getString(backdropUrlIndex);
                    Movie movie = new Movie(id, title, releaseDate, posterUrl, voteAverage, plot, backdropUrl);

                    if (mActionsListener != null) {
                        mActionsListener.openMovieDetails(movie);
                    }
                }
            });
        }

        public View getView() {
            return mView;
        }
    }
}