package kz.gcs.domain;

import java.io.Serializable;
import java.util.Date;

public final class MovieRevenue implements Serializable{

    private static final long serialVersionUID = -7649487661329004175L;
    private Date timestamp;
    private String title;
    private Double revenue;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(final Double revenue) {
        this.revenue = revenue;
    }

}
