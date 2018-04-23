package edu.duke.compsci290.fpx;

/**
 * Created by jerry on 4/22/18.
 */

public class User {
    String mnetID;
    boolean misGiving;
    String myear;
    String mmajor;
    String mname;
    String mphoneNumber;
    String mphoto;

    public User(String mnetID, boolean misGiving, String myear, String mmajor, String mname, String mphoneNumber, String mphoto) {
        this.mnetID = mnetID;
        this.misGiving = misGiving;
        this.myear = myear;
        this.mmajor = mmajor;
        this.mname = mname;
        this.mphoneNumber = mphoneNumber;
        this.mphoto = mphoto;
    }


}
