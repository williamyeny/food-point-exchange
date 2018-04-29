package edu.duke.compsci290.fpx;

/**
 * Created by jerry on 4/28/18.
 */

public class GiveRequest {
    long timeMilli;
    double longitude;
    double latitude;

    public GiveRequest(long timeMilli, double longitude, double latitude) {
        this.timeMilli = timeMilli;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getTimeMilli() {
        return timeMilli;
    }

    public void setTimeMilli(long timeMilli) {
        this.timeMilli = timeMilli;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
