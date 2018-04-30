package edu.duke.compsci290.fpx;

import junit.framework.Assert;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Serena on 4/29/18.
 */

public class ProfileTest {

    Profile sProfile= new Profile("Serena","sl32");
    Profile pProfile= new Profile("phil","pmk13");
    @Test
    public void corrId(){
        assertEquals("sl32", sProfile.getID());

    }

    @Test
    public void corrName(){
        assertEquals("phil",pProfile.getName());
    }
}
