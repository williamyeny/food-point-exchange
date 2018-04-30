package edu.duke.compsci290.fpx;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Philipk on 29/04/18.
 */

public class NetIdActivity extends AppCompatActivity {

    Button idBtn;
    EditText mEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netid);
        idBtn=(Button) findViewById(R.id.btnId);
        mEditText=(EditText) findViewById(R.id.idAuth);
        idBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String netID = mEditText.getText().toString();
                if (TextUtils.isEmpty(netID)){
                    Toast.makeText(getApplicationContext(), "Please enter netID", Toast.LENGTH_SHORT).show();
                }

                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("users").child(netID);
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if( TextUtils.isEmpty((String)dataSnapshot.child("mName").getValue(true))){
                            Log.d("netid", "invalid id");
                            Toast.makeText(getApplicationContext(), "Invalid netID", Toast.LENGTH_SHORT);
                            return;
                        }
                        User user = dataSnapshot.getValue(User.class);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        prefs.edit().putBoolean("isUserLoggedIn", true).commit();
                        prefs.edit().putString("currentUserNetID", user.getmNetID()).commit();
                        UserDbHelper mDbHelper = new UserDbHelper(getApplicationContext());
                        SQLiteDatabase dbwrite = mDbHelper.getWritableDatabase();

                        boolean isGiving = user.getmIsGiving();
                        String finalnetID = user.getmNetID();
                        String major = user.getmMajor();
                        String name = user.getmName();
                        String phone = user.getmPhoneNumber();
                        String profilePictureString64 = user.getmPhoto();
                        String year = user.getmYear();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(UserContract.UserEntry.COLUMN_ISGIVING, isGiving);
                        values.put(UserContract.UserEntry.COLUMN_MAJOR, major);
                        values.put(UserContract.UserEntry.COLUMN_NAME, name);
                        values.put(UserContract.UserEntry.COLUMN_PHONENUMBER, phone);
                        values.put(UserContract.UserEntry.COLUMN_NETID, finalnetID);
                        values.put(UserContract.UserEntry.COLUMN_PHOTO, profilePictureString64);
                        values.put(UserContract.UserEntry.COLUMN_YEAR, year);

                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = dbwrite.insert(UserContract.UserEntry.TABLE_NAME, null, values);
                        Log.d("SQLite", "rowid " + newRowId);
                        dbwrite.close();

                        //Switch activity on over to main map activity
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
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
        });
    }


}
