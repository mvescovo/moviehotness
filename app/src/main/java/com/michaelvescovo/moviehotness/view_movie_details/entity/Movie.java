package com.michaelvescovo.moviehotness.view_movie_details.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael on 11/12/15.
 *
 */
public class Movie implements MovieInterface, Serializable {
    private String mId;
    private String mTitle;
    private String mReleaseDate;
    private String mPosterUrl;
    private Bitmap mPoster;
    private String mVoteAverage;
    private String mPlot;
    private String mBackdropUrl;
    private Bitmap mBackdrop;
    private ArrayList<MovieTrailerInterface> mTrailers = new ArrayList<>();

    public Movie(String id, String title, String releaseDate, String posterUrl, String voteAverage, String plot, String backdropUrl) {
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

    @Override
    public void addTrailer(MovieTrailerInterface trailer) {
        mTrailers.add(trailer);
    }

    @Override
    public MovieTrailerInterface getTrailer(int index) {
        return mTrailers.get(index);
    }

    @Override
    public ArrayList<MovieTrailerInterface> getTrailers() {
        return mTrailers;
    }

    @Override
    public int getTrailerCount() {
        return mTrailers.size();
    }
}
