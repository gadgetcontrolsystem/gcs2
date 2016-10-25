package kz.gcs.domain;

import java.io.Serializable;
import java.util.Date;

public final class Movie implements Serializable{
    private static final long serialVersionUID = 9151604405493763818L;
    private long id;
    private String title;
    private String synopsis;
    private String thumbUrl;
    private String posterUrl;
    private int duration;
    private Date releaseDate;
    private int score;

    public void setId(final long id) {
        this.id = id;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setSynopsis(final String synopsis) {
        this.synopsis = synopsis;
    }

    public void setThumbUrl(final String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setPosterUrl(final String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(final Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public int getDuration() {
        return duration;
    }

    public long getId() {
        return id;
    }

}
