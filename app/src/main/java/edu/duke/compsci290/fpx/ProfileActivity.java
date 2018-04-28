package edu.duke.compsci290.fpx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       /* Take in Intent data*/
        Intent receivedIntent = this.getIntent();
        String name = receivedIntent.getStringExtra("name_key"); //get from database
        String netID = receivedIntent.getStringExtra("netid_key");
        String major = receivedIntent.getStringExtra("major_key");
        int year = receivedIntent.getIntExtra("year_key",2018);



       /*Connect textViews with variables*/

        TextView nameTV = (TextView)findViewById(R.id.name);
        TextView netidTV = (TextView)findViewById(R.id.netID);
        TextView majorTV = (TextView)findViewById(R.id.major_ID);
        TextView yearTV = (TextView)findViewById(R.id.year_id);
        Switch giverSwitch = (Switch)findViewById(R.id.is_giving_switch);

       /*Set text given data from intent-NOTE: what happens if pull from database is null?*/
        nameTV.setText(name);
        netidTV.setText(netID);
        majorTV.setText(major);
        yearTV.setText(Integer.toString(year));

       /* When the giver switch is clicked, must set this value on the database*/

//        giverSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //do stuff with database here?
//            }
//        });



        Transaction[] transactions = (Transaction[]) receivedIntent.getSerializableExtra("tx_key");//NOTE: load transactions from database here


        RecyclerView rv = findViewById(R.id.activity_profile_recycler_view);
        rv.setAdapter(new ProfileAdapter(this, transactions));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }


}





