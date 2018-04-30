package edu.duke.compsci290.fpx;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isGiving = false;
    private String netID = "yung boi";
    private Button broadcastButton;
    private ImageButton locateButton;
    private LatLng location;
    private ImageView setLocationImage;
    private Marker setLocationMarker;
    private ImageButton profileButton;
    private ArrayList<Marker> markers;
    private DatabaseReference dbref;
    private String timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Retrieve data from sqlite
        UserDbHelper mDbHelper = new UserDbHelper(getApplicationContext());
        SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
        String[] columns = new String[]{
                UserContract.UserEntry.COLUMN_NETID,
                UserContract.UserEntry.COLUMN_ISGIVING,
                UserContract.UserEntry.COLUMN_MAJOR,
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_PHONENUMBER,
                UserContract.UserEntry.COLUMN_PHOTO,
                UserContract.UserEntry.COLUMN_YEAR};
        Cursor c = dbRead.query(UserContract.UserEntry.TABLE_NAME, columns, null, null, null, null, null);
        List<Object> list = new ArrayList<Object>() {};
        int netid = c.getColumnIndex(UserContract.UserEntry.COLUMN_NETID);
        int giving = c.getColumnIndex(UserContract.UserEntry.COLUMN_ISGIVING);
        int major = c.getColumnIndex(UserContract.UserEntry.COLUMN_MAJOR);
        int name = c.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
        int phoneNumber = c.getColumnIndex(UserContract.UserEntry.COLUMN_PHONENUMBER);
        int photo = c.getColumnIndex(UserContract.UserEntry.COLUMN_PHOTO);
        int iyear = c.getColumnIndex(UserContract.UserEntry.COLUMN_YEAR);
        c.moveToLast();
        User user = new User(c.getString(netid), c.getInt(giving) != 0, c.getString(iyear), c.getString(major), c.getString(name), c.getString(phoneNumber), c.getString(photo));
        Log.d("SQLITE retrieval", user.getmNetID() + user.getmName());
        isGiving = user.getmIsGiving();
        netID = user.getmNetID();

        broadcastButton = findViewById(R.id.broadcastButton);
        locateButton = findViewById(R.id.locateButton);
        setLocationImage = findViewById(R.id.setLocationImage);
        profileButton = findViewById(R.id.profileButton);

        if (!isGiving) {
            setLocationImage.setVisibility(View.GONE);
            broadcastButton.setVisibility(View.GONE);
        }
        markers = new ArrayList<>();

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        timestamp = month + "-" + day + "-" + year;
        dbref = FirebaseDatabase.getInstance().getReference().child("give_requests").child(timestamp);
    }

    protected void onDestroy(){
        super.onDestroy();
        // remove user lat/lng
        dbref.child(netID).removeValue();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(14.0f);
        Log.d("status", "created");

        LatLng dukeLatLng = new LatLng(36.000919, -78.939372);

        // move camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dukeLatLng, 18.0f));


        updateMarkers();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String netIDMarker = marker.getTitle();
                Log.d("marker", netIDMarker);

                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("users").child(netIDMarker);
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User otherUser = dataSnapshot.getValue(User.class);
                        //Switch activity on over to other profile activity
                        Intent intent = new Intent(getApplicationContext(), OtherProfileActivity.class);
                        intent.putExtra("user_key", otherUser);
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

                return false;
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NOTE: LOAD ALL STUFF FROM DATABASE
                UserDbHelper mDbHelper = new UserDbHelper(getApplicationContext());
                SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
                String[] columns = new String[]{
                        UserContract.UserEntry.COLUMN_NETID,
                        UserContract.UserEntry.COLUMN_ISGIVING,
                        UserContract.UserEntry.COLUMN_MAJOR,
                        UserContract.UserEntry.COLUMN_NAME,
                        UserContract.UserEntry.COLUMN_PHONENUMBER,
                        UserContract.UserEntry.COLUMN_PHOTO,
                        UserContract.UserEntry.COLUMN_YEAR};
                Cursor c = dbRead.query(UserContract.UserEntry.TABLE_NAME, columns, null, null, null, null, null);
                List<Object> list = new ArrayList<Object>() {};
                int netid = c.getColumnIndex(UserContract.UserEntry.COLUMN_NETID);
                int giving = c.getColumnIndex(UserContract.UserEntry.COLUMN_ISGIVING);
                int major = c.getColumnIndex(UserContract.UserEntry.COLUMN_MAJOR);
                int name = c.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
                int phoneNumber = c.getColumnIndex(UserContract.UserEntry.COLUMN_PHONENUMBER);
                int photo = c.getColumnIndex(UserContract.UserEntry.COLUMN_PHOTO);
                int iyear = c.getColumnIndex(UserContract.UserEntry.COLUMN_YEAR);
                c.moveToLast();
                User user = new User(c.getString(netid), c.getInt(giving) != 0, c.getString(iyear), c.getString(major), c.getString(name), c.getString(phoneNumber), c.getString(photo));
                Log.d("SQLITE retrieval", user.getmNetID() + user.getmName());

                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                intent.putExtra("user_key", user);
                startActivity(intent);

            }
        });

        broadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setLocationImage.getVisibility() == View.VISIBLE) {
                    double lat = mMap.getCameraPosition().target.latitude;
                    double lng = mMap.getCameraPosition().target.longitude;

                    lat = Math.round(lat*1000000.0)/1000000.0;
                    lng = Math.round(lng*1000000.0)/1000000.0;

                    setLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.setlocation_scaled)));

                    setLocationImage.setVisibility(View.GONE);
                    broadcastButton.setText("Stop broadcasting location");

                    // update user lat/lng here
//                    HashMap<String, Double> user = new HashMap<>();
//                    user.put("latitude", lat);
//                    user.put("longitude", lng);
//                    user.put("currentTimeMilli", 1.2);
//                    dbref.child(netID).setValue(user);
                    FirebaseUtilities.createGiveRequest(netID, lat, lng);
                } else {
                    setLocationImage.setVisibility(View.VISIBLE);
                    if (setLocationMarker != null) {
                        setLocationMarker.remove();
                    }
                    broadcastButton.setText("Start broadcasting location");

                    // remove user lat/lng here
                    dbref.child(netID).removeValue();
                }

            }
        });

        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get current location
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                if (ContextCompat.checkSelfPermission(MapsActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permission granted", "true");

                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);

                    lm.requestSingleUpdate(criteria, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            // grab gps location
                            if (location != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(location.getLatitude(), location.getLongitude()),
                                        18.0f));
                            } else {
                                Toast.makeText(MapsActivity.this, "We couldn't get your location!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {}

                        @Override
                        public void onProviderEnabled(String s) {}

                        @Override
                        public void onProviderDisabled(String s) {}
                    }, null);

                } else { // gps permission not available
                    // request location
                    ActivityCompat.requestPermissions(MapsActivity.this,
                            new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                            12);
                }
            }
        });



    }

    public int updateMarkers() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
//                ArrayList<GiveRequest> requests = new ArrayList<>();
                for (int i = markers.size()-1; i >= 0; i--) {
                    markers.get(i).remove();
                    markers.remove(i);
                }
                Log.d("a1234", "a1234");
                for(DataSnapshot d: snapshots) {
//                    long timeMilli = (long) d.child("currentTimeMilli").getValue(true);
                    String netIDMarker =  d.getKey();
                    Log.d("net id", netIDMarker);

                    double lng = (double) d.child("longitude").getValue(true);
                    double lat = (double) d.child("latitude").getValue(true);




                    Log.d("read-lng", String.valueOf(lng));
                    Log.d("read-lat", String.valueOf(lat));
                    Marker m = mMap.addMarker(new MarkerOptions()
                        .position( new LatLng(lat, lng))
                        .title(netIDMarker)
                        .snippet("test")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

                    markers.add(m);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase fucked", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        dbref.addValueEventListener(postListener);

        return 0;

    }

}
