package edu.duke.compsci290.fpx;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    @Override
    @TargetApi(26)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUtilities.updateOrCreateUser(new User("zl150", false,"2021", "CS/STATS", "Jerry Liu", "8586632671", "poop"));
        FirebaseUtilities.updateOrCreateUser(new User("wy35", true,"2020", "CS", "Will Ye", "6316496635", "poop"));
        FirebaseUtilities.createGiveRequest("wy35", 36.000941, -78.939265);
        FirebaseUtilities.createGiveRequest("zl150", 36.000941, -78.939265);
        FirebaseUtilities.recordTransaction(new Transaction("wy35", "zl150", 10));
        final TextView helloTextView = (TextView) findViewById(R.id.testtext);
        final Button testButton = (Button) findViewById(R.id.test_button);


        /*PROFILE TESTING STUFF*/
        final Profile p = new Profile("Serena Liu", "sl362");
        Transaction t1 = new Transaction("sl362", "pmk13", 32);
        Transaction t2 = new Transaction("sl362", "pmk13", 36);
        final Transaction[] transactions= new Transaction[]{t1,t2};
        testButton.setText(p.getName());
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =  new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("name_key", p.getName() );
                intent.putExtra("netid_key", p.getID());
                intent.putExtra("tx_key", transactions);

                startActivity(intent);
            }
        });

        /*
        testing sqlite
         */
        //UserContract.UserDbHelper mDbHelper = new UserContract.UserDbHelper(getApplicationContext());

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
                    long timsdfs = (long) d.child("currentTimeMilli").getValue(true);
                    double longitude = (double) d.child("longitude").getValue(true);
                    double lat = (double) d.child("latitude").getValue(true);
                    GiveRequest req = new GiveRequest(timsdfs, longitude, lat);
                    requests.add(req);
                    Log.d("FIREBSE", "netid" + timsdfs + "long lat" + longitude + " " + lat);
                }
                Log.d("FIREBASE IS AWESOME", "# of requests: " + requests.size());
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
