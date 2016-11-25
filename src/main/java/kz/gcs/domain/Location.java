package kz.gcs.domain;

import java.io.Serializable;
import java.util.Date;

public final class Location implements Serializable, Comparable<Location> {
    private static final long serialVersionUID = 4070830229335439060L;

    private Long id;
    private Date time;
    private String country;
    private String city;
    private Double lat;
    private Double lon;
    private Long gadgetId;


    private boolean read;

    @Override
    public int compareTo(Location o1) {
        return this.getTime().compareTo(o1.getTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Long getGadgetId() {
        return gadgetId;
    }

    public void setGadgetId(Long gadgetId) {
        this.gadgetId = gadgetId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String displayStr() {
        return getCity() + " " + getTime();
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", time=" + time +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", gadgetId=" + gadgetId +
                ", read=" + read +
                '}';
    }
}
