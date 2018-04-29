package edu.duke.compsci290.fpx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


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
         */



       /* Take in Intent data*/
        Intent receivedIntent = this.getIntent();
        User user = (User) receivedIntent.getSerializableExtra("user_key");

        String name = "Name: " + user.getmName();
        String netID ="NetID: " + user.getmNetID();
        String major = "Major: " + user.getmMajor();
        String year = "Year: "  + user.getmYear();
        String number = "Phone Number: " + user.getmPhoneNumber();
        boolean isGiving = user.getmIsGiving();

       /*Connect textViews/switches/buttons with variables*/

        TextView nameTV = (TextView)findViewById(R.id.name);
        TextView netidTV = (TextView)findViewById(R.id.netID);
        TextView majorTV = (TextView)findViewById(R.id.major_ID);
        TextView yearTV = (TextView)findViewById(R.id.year_id);
        TextView numberTV = (TextView)findViewById(R.id.number_id);
        ImageView profPicIV = (ImageView)findViewById(R.id.profile_picture);

        Switch giverSwitch = (Switch)findViewById(R.id.is_giving_switch);
        ImageButton mapBtn = (ImageButton)findViewById(R.id.map_button);
        ImageButton addTxBtn = (ImageButton) findViewById(R.id.add_transaction);

        final EditText receiverET = (EditText) findViewById(R.id.receiver_input);
        final EditText senderET = (EditText) findViewById(R.id.sender_input);
        final EditText amtET = (EditText) findViewById(R.id.amt_input);

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
                System.out.println(tx.getmSenderID() + tx.getmReceiverID() + tx.getmAmount());

            }
        });



        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent); //should be map activity later NOTE: do I need to pass anything as an intent here? save state?
            }
        });



        /*Profile picture, decoding string*/

        byte[] imageAsBytes = Base64.decode(user.getmPhoto().getBytes(), Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        profPicIV.setImageBitmap(bm);

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





