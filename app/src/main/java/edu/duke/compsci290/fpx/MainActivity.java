package edu.duke.compsci290.fpx;

import android.content.DialogInterface;
import android.content.Intent;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        FirebaseUtilities.updateOrCreateUser("zl150", false,"2021", "CS/STATS", "Jerry Liu", "8586632671", "poop");
        FirebaseUtilities.updateOrCreateUser("wy35", true,"2020", "CS", "Will Ye", "6316496635", "poop");
        FirebaseUtilities.createGiveRequest("wy35", 36.000941, -78.939265);
        FirebaseUtilities.recordTransaction(new Transaction("wy35", "zl150", 10));
        final TextView helloTextView = (TextView) findViewById(R.id.testtext);
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
                Log.d("FIREBASE%%%%%%%%%%%", dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase fucked", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        dbref.addValueEventListener(postListener);

        startActivity(new Intent(this, MapsActivity.class));
    }

}
