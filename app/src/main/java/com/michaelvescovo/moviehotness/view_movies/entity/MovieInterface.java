package com.michaelvescovo.moviehotness.view_movies.entity;

/**
 * Created by Michael on 29/11/15.
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

    public void setVoteAverage(String voteAverage);

    public String getVoteAverage();

    public void setPlot(String plot);

    public String getPlot();

    public void setBackdropUrl(String backdropUrl);

    public String getBackdropUrl();
}
