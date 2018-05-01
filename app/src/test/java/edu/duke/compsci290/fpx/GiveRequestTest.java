package edu.duke.compsci290.fpx;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Serena on 4/29/18.
 */

public class GiveRequestTest {

    long timeMilli = 273289371;
    double longitude = 83.437874;
    double latitude= 47.3483749;
    GiveRequest gr = new GiveRequest(timeMilli,longitude,latitude);


    @Test
    public void setAndGetTimeTest() {
        assertEquals(gr.getLongitude(), longitude);
        assertEquals(gr.getLatitude(), latitude);
        assertEquals(gr.getTimeMilli(), timeMilli);
        timeMilli = 74832983;
        longitude=78.483729;
        latitude = 43.43892;

        gr.setLatitude(latitude);
        gr.setLongitude(longitude);
        gr.setTimeMilli(timeMilli);

        assertEquals(gr.getLongitude(), longitude);
        assertEquals(gr.longitude, longitude);
        assertEquals(gr.getLatitude(), latitude);
        assertEquals(gr.latitude, latitude);
        assertEquals(gr.timeMilli, timeMilli);

    }


}
