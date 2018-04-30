package edu.duke.compsci290.fpx;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.v4.app.ActivityCompat;
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

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUtilities.updateOrCreateUser(new User("zl150", false,"2021", "CS/STATS", "Jerry Liu", "8586632671", "poop"));
        FirebaseUtilities.updateOrCreateUser(new User("wy35", true,"2020", "CS", "Will Ye", "6316496635", "poop"));
        FirebaseUtilities.createGiveRequest("wy35", 36.000941, -78.939265);
        FirebaseUtilities.recordTransaction(new Transaction("wy35", "zl150", 10));
        final TextView helloTextView = (TextView) findViewById(R.id.testtext);
        final Button testButton = (Button) findViewById(R.id.test_button);
        final Button testButton2= (Button) findViewById(R.id.test_button2);


        /*PROFILE TESTING STUFF*/
        final Profile p = new Profile("Serena Liu", "sl362");
        final User u = new User("sl362", true, "2019", "ECE", "Serena Liu", "7046141335", "myPhoto");

        Transaction t1 = new Transaction("sl362", "pmk13", 32);
        Transaction t2 = new Transaction("sl362", "pmk13", 36);

        testButton.setText(u.getmName());
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =  new Intent(MainActivity.this, ProfileActivity.class);

                intent.putExtra("user_key", u);

                startActivity(intent);
            }
        });

        /* OTHER PROFILE TESTING STUFF*/

        final User u1 = new User("sl362", true, "2019", "ECE", "Serena Liu", "7046141335", "myPhoto");


        testButton2.setText(u1.getmName());

        testButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 =  new Intent(MainActivity.this, OtherProfileActivity.class);

                intent2.putExtra("user_key", u1);

                startActivity(intent2);
            }
        });


        DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference().child("transactions").child("zl150");
        ChildEventListener userListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Transaction transaction = dataSnapshot.getValue(Transaction.class);
                Log.d("firebase", "onChildAdded:" + transaction.getmSenderID() + transaction.getmAmount() + transaction.getmDate() + transaction.getmReceiverID());

                helloTextView.setText(transaction.getmSenderID() + transaction.getmAmount() + transaction.getmDate() + transaction.getmReceiverID());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //necessary method to inherit but we have no use for it
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                //necessary method to inherit but we have no use for it
            }
            @Override

            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                //necessary method to inherit but we have no use for it
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Transaction failed, log a message
                Log.w("firebase is done fucked", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
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

    }


}
