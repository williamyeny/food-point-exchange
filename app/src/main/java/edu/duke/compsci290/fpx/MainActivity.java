package edu.duke.compsci290.fpx;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    @TargetApi(26)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if user has already logged in, if so, go to map, if not, go to login/signup page
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if(!prefs.getBoolean("isUserLoggedIn", false)){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else{
            //start map activity
        }


        // Rest of the stuff below here is all junk and only for testing purposes

        FirebaseUtilities.updateOrCreateUser(new User("zl150", false,"2021", "CS/STATS", "Jerry Liu", "8586632671", "poop"));
        FirebaseUtilities.updateOrCreateUser(new User("wy35", true,"2020", "CS", "Will Ye", "6316496635", "poop"));
        FirebaseUtilities.createGiveRequest("wy35", 36.020941, -78.933265);
        FirebaseUtilities.createGiveRequest("zl150", 36.050941, -78.939265);
        FirebaseUtilities.createGiveRequest("sl362", 36.070941, -78.936265);
        FirebaseUtilities.recordTransaction(new Transaction("wy35", "zl150", 10));
        final TextView helloTextView = (TextView) findViewById(R.id.testtext);
        final Button testButton = (Button) findViewById(R.id.test_button);


        /*PROFILE TESTING STUFF*/
        final Profile p = new Profile("Serena Liu", "sl362");
        //public User(String mNetID, boolean mIsGiving, String mYear, String mMajor, String mName, String mPhoneNumber, String mPhoto) {
        final User u = new User("sl362", true, "2019", "ECE", "Serena Liu", "7046141335", "photo");

        Transaction t1 = new Transaction("sl362", "pmk13", 32);
        Transaction t2 = new Transaction("sl362", "pmk13", 36);

        testButton.setText(p.getName());
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =  new Intent(MainActivity.this, ProfileActivity.class);

                intent.putExtra("user_key", u);

                startActivity(intent);
            }
        });

        /*
        testing sqlite
         */
        UserDbHelper mDbHelper = new UserDbHelper(getApplicationContext());
        SQLiteDatabase dbwrite = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_ISGIVING, true);
        values.put(UserContract.UserEntry.COLUMN_MAJOR, "CS");
        values.put(UserContract.UserEntry.COLUMN_NAME, "Will Ye");
        values.put(UserContract.UserEntry.COLUMN_PHONENUMBER, "8586632671");
        values.put(UserContract.UserEntry.COLUMN_NETID, "wy35");
        values.put(UserContract.UserEntry.COLUMN_PHOTO, "poop");
        values.put(UserContract.UserEntry.COLUMN_YEAR, "2021");

// Insert the new row, returning the primary key value of the new row
        long newRowId = dbwrite.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        Log.d("SQLite", "rowid " + newRowId);
        dbwrite.close();

        SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
        String[] columns = new String[]{
                UserContract.UserEntry.COLUMN_NETID,
                UserContract.UserEntry.COLUMN_ISGIVING,
                UserContract.UserEntry.COLUMN_MAJOR,
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_PHONENUMBER,
                UserContract.UserEntry.COLUMN_PHOTO,
                UserContract.UserEntry.COLUMN_YEAR};
        Cursor c = dbRead.query(UserContract.UserEntry.TABLE_NAME, columns, null, null, null, null, null);
        List<Object> list = new ArrayList<Object>() {};
        int netid = c.getColumnIndex(UserContract.UserEntry.COLUMN_NETID);
        int giving = c.getColumnIndex(UserContract.UserEntry.COLUMN_ISGIVING);
        int major = c.getColumnIndex(UserContract.UserEntry.COLUMN_MAJOR);
        int name = c.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
        int phoneNumber = c.getColumnIndex(UserContract.UserEntry.COLUMN_PHONENUMBER);
        int photo = c.getColumnIndex(UserContract.UserEntry.COLUMN_PHOTO);
        int iyear = c.getColumnIndex(UserContract.UserEntry.COLUMN_YEAR);
        c.moveToLast();
        User user = new User(c.getString(netid), c.getInt(giving) != 0, c.getString(iyear), c.getString(major), c.getString(name), c.getString(phoneNumber), c.getString(photo));
        Log.d("SQLITE POGGERS", user.getmNetID() + user.getmName());










        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        String monthdayyear = month + "-" + day + "-" + year;

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("give_requests").child(monthdayyear);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                ArrayList<GiveRequest> requests = new ArrayList<>();
                for(DataSnapshot d: snapshots) {
                    long timeMilli = (long) d.child("currentTimeMilli").getValue(true);
                    double longitude = (double) d.child("longitude").getValue(true);
                    double lat = (double) d.child("latitude").getValue(true);
                    GiveRequest req = new GiveRequest(timeMilli, longitude, lat);
                    //only add request to list if it happened in the last 15 minutes.
                    if(System.currentTimeMillis() - timeMilli < 900000){
                        requests.add(req);
                    }
                    Log.d("FIREBSE", "netid" + timeMilli + "long lat" + longitude + " " + lat);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase fucked", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        dbref.addListenerForSingleValueEvent(postListener);
        /*
        mUserReference.addChildEventListener(userListener);
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("users").child("zl150");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FIREBASE%%%%%%%%%%%", (String)dataSnapshot.child("mName").getValue(true));
                User user = dataSnapshot.getValue(User.class);
                Log.d("FIREBASE%%%%%%%%%%%", user.getmNetID() + user.getmName() + user.getmIsGiving());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase fucked", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };


        dbref.addListenerForSingleValueEvent(postListener);
        */

    }

}
