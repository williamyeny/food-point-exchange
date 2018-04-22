package edu.duke.compsci290.fpx;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUtilities.createUser("zl150", "2021", "CS/STATS", "Jerry Liu", "8586632671", "poop");
        FirebaseUtilities.createGiveRequest("zl150", 36.000941, -78.939265, System.currentTimeMillis());
    }


}
