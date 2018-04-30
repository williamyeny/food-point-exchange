package edu.duke.compsci290.fpx;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       /*
          Still need to work on/implement
          1. profile picture-base64 string
          2. database, getting transactions
          3. change to mapactivity on button instead of main activity
          4. isGiving button on press changes database stuff
          5. logout should send to the right activity
          6. transaction should be written to database when added
        */



      /* Take in Intent data*/
        Intent receivedIntent = this.getIntent();
        final User user = (User) receivedIntent.getSerializableExtra("user_key");

        String nameStr = "Name: " + user.getmName();
        String netIDStr ="NetID: " + user.getmNetID();
        String majorStr = "Major: " + user.getmMajor();
        String yearStr = "Year: "  + user.getmYear();
        String numberStr = "Phone Number: " + user.getmPhoneNumber();
        final String number = user.getmPhoneNumber();
        boolean isGiving = user.getmIsGiving();

      /*Connect textViews/switches/buttons with variables*/

        TextView nameTV = (TextView)findViewById(R.id.name);
        TextView netidTV = (TextView)findViewById(R.id.netID);
        TextView majorTV = (TextView)findViewById(R.id.major_ID);
        TextView yearTV = (TextView)findViewById(R.id.year_id);
        TextView numberTV = (TextView) findViewById(R.id.number_id);
        Button logoutBtn = (Button) findViewById(R.id.logout_button);
        ImageView profPicIV = (ImageView)findViewById(R.id.profile_picture);

        Switch giverSwitch = (Switch)findViewById(R.id.is_giving_switch);
        ImageButton mapBtn = (ImageButton)findViewById(R.id.map_button);
        ImageButton addTxBtn = (ImageButton) findViewById(R.id.add_transaction);


        final EditText senderET = (EditText) findViewById(R.id.sender_input);
        final EditText receiverET = (EditText) findViewById(R.id.receiver_input);
        final EditText amtET = (EditText) findViewById(R.id.amt_input);

        senderET.setInputType(InputType.TYPE_NULL);
        receiverET.setInputType(InputType.TYPE_NULL);
        amtET.setInputType(InputType.TYPE_NULL);

        senderET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senderET.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });


        addTxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receiver = receiverET.getText().toString();
                String sender = senderET.getText().toString();
                String amt = amtET.getText().toString();
                int amount=0;

                //Checks to see if fields are full, and amount is an integer
                if (sender.matches("")) {
                    Toast.makeText(ProfileActivity.this, "You did not enter a sender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (receiver.matches("")) {
                    Toast.makeText(ProfileActivity.this, "You did not enter a receiver", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (amt.matches("")) {
                    Toast.makeText(ProfileActivity.this, "You did not enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    amount = Integer.parseInt(amt);
                }catch (NumberFormatException ex) {
                    Toast.makeText(ProfileActivity.this, "You did not enter a number in amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                Transaction tx = new Transaction(sender, receiver, amount); //log this to the database
                FirebaseUtilities.recordTransaction(tx);
                senderET.setInputType(InputType.TYPE_NULL);
                receiverET.setInputType(InputType.TYPE_NULL);
                amtET.setInputType(InputType.TYPE_NULL);

                senderET.setText("");
                receiverET.setText("");
                amtET.setText("");


            }
        });



        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ProfileActivity.this, MapsActivity.class);
                startActivity(intent); //should be map activity later NOTE: do I need to pass anything as an intent here? save state?
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NOTE: CHANGE THE DATABASE CURRENT ID
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean("isUserLoggedIn", false);
                Intent intent =  new Intent(ProfileActivity.this, Starter_Activity.class);
                startActivity(intent); //should be login screen later...also erase currentMID in database?
            }
        });

       /*Profile picture, decoding string*/

        byte[] imageAsBytes = Base64.decode(user.getmPhoto().getBytes(), Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        profPicIV.setImageBitmap(bm);

      /*Set text given data from intent-NOTE: what happens if pull from database is null?*/
        nameTV.setText(nameStr);
        netidTV.setText(netIDStr);
        majorTV.setText(majorStr);
        yearTV.setText(yearStr);
        numberTV.setText(numberStr);
        giverSwitch.setChecked(isGiving);

      /* When the giver switch is clicked, must set this value on the database*/

        giverSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("changing giving", "lol");
                User u = user;
                u.setmIsGiving(!u.getmIsGiving());
                FirebaseUtilities.updateOrCreateUser(u);
                String netID = u.getmNetID();
                String major = u.getmMajor();
                String name = u.getmName();
                String phone = u.getmPhoneNumber();
                String year = u.getmYear();
                String profilePictureString64 = u.getmPhoto();
                boolean isGiving = u.getmIsGiving();
                UserDbHelper mDbHelper = new UserDbHelper(getApplicationContext());
                SQLiteDatabase dbwrite = mDbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(UserContract.UserEntry.COLUMN_ISGIVING, isGiving);
                values.put(UserContract.UserEntry.COLUMN_MAJOR, major);
                values.put(UserContract.UserEntry.COLUMN_NAME, name);
                values.put(UserContract.UserEntry.COLUMN_PHONENUMBER, phone);
                values.put(UserContract.UserEntry.COLUMN_NETID, netID);
                values.put(UserContract.UserEntry.COLUMN_PHOTO, profilePictureString64);
                values.put(UserContract.UserEntry.COLUMN_YEAR, year);

                // Insert the new row, returning the primary key value of the new row
                long newRowId = dbwrite.insert(UserContract.UserEntry.TABLE_NAME, null, values);
                Log.d("SQLite", "rowid " + newRowId);
                dbwrite.close();
            }
        });

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("transactions").child(user.getmNetID());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Transaction> trans = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    trans.add(ds.getValue(Transaction.class));
                }
                Transaction[] transactions = trans.toArray(new Transaction[trans.size()]);
                RecyclerView rv = findViewById(R.id.activity_profile_recycler_view);
                rv.setAdapter(new ProfileAdapter(getApplicationContext(), transactions));
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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











