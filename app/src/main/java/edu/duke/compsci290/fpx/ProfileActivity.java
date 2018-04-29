package edu.duke.compsci290.fpx;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /*
           Still need to work on/implement
           1. profile picture
           2. database, getting transactions
           3. change to mapactivity on button instead of main activity
           4. isGiving button on press changes database stuff


         */



       /* Take in Intent data*/
        Intent receivedIntent = this.getIntent();
        User user = (User) receivedIntent.getSerializableExtra("user_key");

        String name = user.getmName();
        String netID =user.getmNetID();
        String major = user.getmMajor();
        String year = user.getmYear();
        String number = user.getmPhoneNumber();
        boolean isGiving = user.getmIsGiving();

       /*Connect textViews/switches/buttons with variables*/

        TextView nameTV = (TextView)findViewById(R.id.name);
        TextView netidTV = (TextView)findViewById(R.id.netID);
        TextView majorTV = (TextView)findViewById(R.id.major_ID);
        TextView yearTV = (TextView)findViewById(R.id.year_id);
        TextView numberTV = (TextView)findViewById(R.id.number_id);
        Switch giverSwitch = (Switch)findViewById(R.id.is_giving_switch);
        ImageButton mapBtn = (ImageButton)findViewById(R.id.map_button);

        mapBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent); //should be map activity later NOTE: do I need to pass anything as an intent here? save state?
            }
        });

       /*Set text given data from intent-NOTE: what happens if pull from database is null?*/
        nameTV.setText(name);
        netidTV.setText(netID);
        majorTV.setText(major);
        yearTV.setText(year);
        numberTV.setText(number);
        giverSwitch.setChecked(isGiving);

       /* When the giver switch is clicked, must set this value on the database*/

//        giverSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //do stuff with firebase here?
//            }
//        });


        /*LOAD THESE TRANSACTIONS STRAIGHT FROM DATABASE*/
        Transaction testT1 = new Transaction("sl362", "pmk13", 32);
        Transaction testT2 = new Transaction("sl362", "pmk13", 37);
        Transaction testT3 = new Transaction("sl362", "pmk13", 39);
        Transaction testT4 = new Transaction("sl362", "pmk13", 50);
        Transaction[] transactions = new Transaction[]{testT1, testT2, testT3, testT4};

        RecyclerView rv = findViewById(R.id.activity_profile_recycler_view);
        rv.setAdapter(new ProfileAdapter(this, transactions));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }


}





