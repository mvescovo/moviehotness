package com.michaelvescovo.moviehotness.view_movie_details.entity;

import android.graphics.Bitmap;

/**
 * Created by Michael on 11/12/15.
 *
 */
public interface MovieInterface {
    public  void setId(String id);

    public String getId();

    public void setTitle(String title);

    public String getTitle();

    public void setReleaseDate(String releaseDate);

    public String getReleaseDate();

    public void setPosterUrl(String posterUrl);

    public String getPosterUrl();

    public void setPoster(Bitmap poster);

    public Bitmap getPoster();

    public void setVoteAverage(String voteAverage);

    public String getVoteAverage();

    public void setPlot(String plot);

    public String getPlot();

    public void setBackdropUrl(String backdropUrl);

    public String getBackdropUrl();

    public void setBackdrop(Bitmap backdrop);

    public Bitmap getBackrop();
}
