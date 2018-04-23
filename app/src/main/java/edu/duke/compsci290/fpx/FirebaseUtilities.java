package edu.duke.compsci290.fpx;

import android.annotation.TargetApi;
import android.support.annotation.TransitionRes;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.PipedOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jerry on 4/21/18.
 */

public class FirebaseUtilities {

    protected static Transaction transactions;

    /*
    When user first creates account, important to call this method. Alternatively, if something needs
    to be update like whether the person is giving or receiving, this method will simply overwrite the data
    so it also functions as a update method.
     */
    public static void updateOrCreateUser(String netID, boolean isGiving, String year, String major, String name, String phoneNumber, String photo){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(netID).child("isGiving").setValue(isGiving);
        mDatabase.child("users").child(netID).child("year").setValue(year);
        mDatabase.child("users").child(netID).child("major").setValue(major);
        mDatabase.child("users").child(netID).child("name").setValue(name);
        mDatabase.child("users").child(netID).child("phoneNumber").setValue(phoneNumber);
        mDatabase.child("users").child(netID).child("photo").setValue(photo);

    }

    /*
    Creates a give request in the database. Requests are grouped by date, month-day-year.
    Please give as accurate GPS information as possible.
     */
    @TargetApi(26)
    public static void createGiveRequest(String netID, double latitude, double longitude){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        String monthdayyear = month + "-" + day + "-" + year;
        mDatabase.child("give_requests").child(monthdayyear).child(netID).child("latitude").setValue(latitude);
        mDatabase.child("give_requests").child(monthdayyear).child(netID).child("longitude").setValue(longitude);
        mDatabase.child("give_requests").child(monthdayyear).child(netID).child("currentTimeMilli").setValue(date.getTime());
    }



    public static List<Transaction> getUserTransactions(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = mDatabase.getKey()
        boolean f;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
        Log.d("%%%%%%%%%%%", )
        return new ArrayList<>();
    }

    /*
        Records transaction between two people under their profiles. Records it twice, once under each netID, identitfied by day of transaction
        Only call when the person giving accepts the request.
     */
    @TargetApi(26)
    public static void recordTransaction(String receivernetID, String givernetID, double estimatedTransactionValue){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        String monthdayyear = month + "-" + day + "-" + year;

        mDatabase.child("transactions").child(receivernetID).child(monthdayyear).child("receivernetID").setValue(receivernetID);
        mDatabase.child("transactions").child(receivernetID).child(monthdayyear).child("givernetID").setValue(givernetID);
        mDatabase.child("transactions").child(receivernetID).child(monthdayyear).child("estimatedTransactionValue").setValue(estimatedTransactionValue);

        mDatabase.child("transactions").child(givernetID).child(monthdayyear).child("receivernetID").setValue(givernetID);
        mDatabase.child("transactions").child(givernetID).child(monthdayyear).child("givernetID").setValue(givernetID);
        mDatabase.child("transactions").child(givernetID).child(monthdayyear).child("estimatedTransactionValue").setValue(estimatedTransactionValue);
    }

}
