package edu.duke.compsci290.fpx;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class OtherProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        /* Take in Intent data*/
        Intent receivedIntent = this.getIntent();
        User user = (User) receivedIntent.getSerializableExtra("user_key");

        String nameStr = "Name: " + user.getmName();
        String netIDStr ="NetID: " + user.getmNetID();
        String majorStr = "Major: " + user.getmMajor();
        String yearStr = "Year: "  + user.getmYear();
        String numberStr = "Phone Number: " + user.getmPhoneNumber();
        final String number = user.getmPhoneNumber();
        boolean isGiving = user.getmIsGiving();

       /*Connect textViews/switches/buttons with variables*/

        TextView othernameTV = (TextView)findViewById(R.id.other_name);
        TextView othernetidTV = (TextView)findViewById(R.id.other_netid);
        TextView othermajorTV = (TextView)findViewById(R.id.other_major);
        TextView otheryearTV = (TextView)findViewById(R.id.other_year);
        TextView othernumber = (TextView) findViewById(R.id.number_linkify);

        ImageView otherprofPicIV = (ImageView)findViewById(R.id.other_profile_picture);
        ImageButton othermapBtn = (ImageButton)findViewById(R.id.other_map_button);

        byte[] imageAsBytes = Base64.decode(user.getmPhoto().getBytes(), Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        otherprofPicIV.setImageBitmap(bm);

       /*Set text given data from intent-NOTE: what happens if pull from database is null?*/
        othernameTV.setText(nameStr);
        othernetidTV.setText(netIDStr);
        othermajorTV.setText(majorStr);
        otheryearTV.setText(yearStr);
        othernumber.setText(numberStr);



        othernumber.setText("7046141335");


        Linkify.addLinks(othernumber, Linkify.PHONE_NUMBERS);

        othermapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(OtherProfileActivity.this, MainActivity.class);
                startActivity(intent); //should be map activity later NOTE: do I need to pass anything as an intent here? save state?
            }
        });



    }





}
