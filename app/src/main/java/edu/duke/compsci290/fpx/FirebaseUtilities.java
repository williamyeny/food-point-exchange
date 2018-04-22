package edu.duke.compsci290.fpx;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by jerry on 4/21/18.
 */

public class FirebaseUtilities {

    public static void createUser(String netID, String year, String major, String name, String phoneNumber, String photo){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(netID).child("year").setValue(year);
        mDatabase.child("users").child(netID).child("major").setValue(major);
        mDatabase.child("users").child(netID).child("name").setValue(name);
        mDatabase.child("users").child(netID).child("phoneNumber").setValue(phoneNumber);
        mDatabase.child("users").child(netID).child("photo").setValue(photo);

    }

    public static void createGiveRequest(String netID, double latitude, double longitude, long currentTimeMilli){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("give_requests").child(netID).child("latitude").setValue(latitude);
        mDatabase.child("give_requests").child(netID).child("longitude").setValue(longitude);
        mDatabase.child("give_requests").child(netID).child("currentTimeMilli").setValue(currentTimeMilli);
    }

}
