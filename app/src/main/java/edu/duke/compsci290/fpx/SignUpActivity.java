package edu.duke.compsci290.fpx;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Philipk on 29/04/18.
 */

public class SignUpActivity extends AppCompatActivity{


    ImageView mImageView;
    Button uploadBtn;
    Button camBtn;
    Button signupBtn;
    private static final int PICK_IMAGE=1;
    Bitmap profilePicture;
    static final int REQUEST_IMAGE_CAPTURE = 2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        mImageView= (ImageView) findViewById(R.id.imageupload);
        uploadBtn=(Button)findViewById(R.id.etUpload);
        camBtn = (Button) findViewById(R.id.etCamera);
        signupBtn = (Button) findViewById(R.id.btnSignUp);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
                //uploadImage();
            }

        });

        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }

        });

        signupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText netIDText = (EditText) findViewById(R.id.user_NetID);
                EditText majorText = (EditText) findViewById(R.id.user_Major);
                EditText nameText = (EditText) findViewById(R.id.user_Name);
                EditText phoneText = (EditText) findViewById(R.id.user_Phonenumber);
                EditText yearText = (EditText) findViewById(R.id.user_Year);
                Switch isGivingSwitch = (Switch) findViewById(R.id.is_giving_switch);

                String netID = netIDText.getText().toString();
                String major = majorText.getText().toString();
                String name = nameText.getText().toString();
                String phone = phoneText.getText().toString();
                String year = yearText.getText().toString();
                boolean isGiving = isGivingSwitch.isChecked();

                //ensure user completed all fields
                Log.d("WTF", netID + " " + major + " " + name + " " + phone + " " + year);
                if (TextUtils.isEmpty(netID) || TextUtils.isEmpty(major) || TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(phone) || TextUtils.isEmpty(year) || profilePicture == null) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields and set/create a profile picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                //convert bitmap image to string64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                profilePicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String profilePictureString64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                //all fields complete... add user to db and launch main activity...
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean("isUserLoggedIn", true).commit();
                prefs.edit().putString("currentUserNetID", netID).commit();
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

                //write to firebase db
                FirebaseUtilities.updateOrCreateUser(new User(netID, isGiving,year, major, name, phone, profilePictureString64));


                //Switch activity on over to main map activity
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //first call super
        super.onActivityResult(requestCode, resultCode, data);

        //called when camera input is used
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePicture = imageBitmap;
            mImageView.setImageBitmap(imageBitmap);
        }

        //called when camera roll input is used
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profilePicture = selectedImage;
                mImageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }


    }






}



