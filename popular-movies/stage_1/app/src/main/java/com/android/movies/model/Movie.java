package com.android.movies.model;

public class Movie {

    private Integer id;
    private String originalTitle;
    private String moviePosterImageUrl;
    private String plotSynopsis;
    private Double averageVote;
    private String releaseDate;

    public Movie(Integer id, String originalTitle, String moviePosterImageUrl, String plotSynopsis, Double averageVote, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.moviePosterImageUrl = moviePosterImageUrl;
        this.plotSynopsis = plotSynopsis;
        this.averageVote = averageVote;
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMoviePosterImageUrl() {
        return moviePosterImageUrl;
    }

    public void setMoviePosterImageUrl(String moviePosterImageUrl) { this.moviePosterImageUrl = moviePosterImageUrl; }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setAverageVote(Double averageVote) {
        this.averageVote = averageVote;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlotSynopsis() { return plotSynopsis; }

    public Double getAverageVote() {
        return averageVote;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Movie Details:\n");
        stringBuilder.append("\tOriginal Title = " + originalTitle + "\n");
        stringBuilder.append("\tRelease Date = " + releaseDate + "\n");
        stringBuilder.append("\tMovie Poster Image URL = " + moviePosterImageUrl + "\n");
        stringBuilder.append("\tAverage Vote = " + averageVote + "\n");
        stringBuilder.append("\tPlot Synopsis = " + plotSynopsis + "\n");

        return stringBuilder.toString();

    }

}