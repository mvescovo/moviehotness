package com.michaelvescovo.moviehotness.view_movies.entity;

import android.graphics.Bitmap;

/**
 * Created by Michael on 29/11/15.
 *
 */
public class MoviePreview implements MoviePreviewInterface {
    private String mId;
    private String mTitle;
    private String mReleaseDate;
    private String mPosterUrl;
    private Bitmap mPoster;
    private String mVoteAverage;
    private String mPlot;
    private String mBackdropUrl;
    private Bitmap mBackdrop;

    public MoviePreview(String id, String title, String releaseDate, String posterUrl, String voteAverage, String plot, String backdropUrl) {
        mId = id;
        mTitle = title;
        mReleaseDate = releaseDate;
        mPosterUrl = posterUrl;
        mVoteAverage = voteAverage;
        mPlot = plot;
        mBackdropUrl = backdropUrl;
    }

    @Override
    public void setId(String id) {
        mId = id;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    @Override
    public String getReleaseDate() {
        return mReleaseDate;
    }

    @Override
    public void setPosterUrl(String posterUrl) {
        mPosterUrl = posterUrl;
    }

    @Override
    public String getPosterUrl() {
        return mPosterUrl;
    }

    @Override
    public void setPoster(Bitmap poster) {
        mPoster = poster;
    }

    @Override
    public Bitmap getPoster() {
        return mPoster;
    }

    @Override
    public void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }

    @Override
    public String getVoteAverage() {
        return mVoteAverage;
    }

    @Override
    public void setPlot(String plot) {
        mPlot = plot;
    }

    @Override
    public String getPlot() {
        return mPlot;
    }

    @Override
    public void setBackdropUrl(String backdropUrl) {
        mBackdropUrl = backdropUrl;
    }

    @Override
    public String getBackdropUrl() {
        return mBackdropUrl;
    }

    @Override
    public void setBackdrop(Bitmap backdrop) {
        mBackdrop = backdrop;
    }

    @Override
    public Bitmap getBackrop() {
        return mBackdrop;
    }
}
