package edu.duke.compsci290.fpx;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
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

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    @TargetApi(26)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //public User(String mNetID, boolean mIsGiving, String mYear, String mMajor, String mName, String mPho
        User user1 = new User("sl362", true, "2019", "ece", "serena liu", "7046141335", "myPhoto");
        //Check if user has already logged in, if so, go to map, if not, go to login/signup page
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if(!prefs.getBoolean("isUserLoggedIn", false)){
            Intent intent = new Intent(this, Starter_Activity.class);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

}
