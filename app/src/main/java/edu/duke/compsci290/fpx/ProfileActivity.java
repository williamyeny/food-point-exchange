package edu.duke.compsci290.fpx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent receivedIntent = this.getIntent();

        String name = receivedIntent.getStringExtra("name_key"); //get from database
        String netID = receivedIntent.getStringExtra("netid_key");

        TextView nameTV = (TextView)findViewById(R.id.name);
        TextView netidTV = (TextView)findViewById(R.id.netID);
        TextView txHeaderTV = (TextView)findViewById(R.id.tx_header);

        nameTV.setText(name);
        netidTV.setText(netID);
        txHeaderTV.setText("Transactions List");


        Transaction[] transactions = (Transaction[]) receivedIntent.getSerializableExtra("tx_key");//NOTE: load transactions from database here
        System.out.println("Transaction: "+ transactions[0].getmReceiverID() + " " + transactions[0].getmSenderID() + " " + transactions[0].getmDate());
      RecyclerView rv = findViewById(R.id.activity_profile_recycler_view);
        rv.setAdapter(new ProfileAdapter(this, transactions));
       rv.setLayoutManager(new LinearLayoutManager(this));
    }


}
