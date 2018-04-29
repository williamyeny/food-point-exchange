package edu.duke.compsci290.fpx;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isGiving = true;
    private Button broadcastButton;
    private ImageButton locateButton;
    private LatLng location;
    private ImageView setLocationImage;
    private Marker setLocationMarker;
    private ImageButton profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        broadcastButton = findViewById(R.id.broadcastButton);
        locateButton = findViewById(R.id.locateButton);
        setLocationImage = findViewById(R.id.setLocationImage);
        profileButton = findViewById(R.id.profileButton);

        if (!isGiving) {
            setLocationImage.setVisibility(View.GONE);
        }
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


        /*
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        String monthdayyear = month + "-" + day + "-" + year;

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("give_requests").child(monthdayyear);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                ArrayList<GiveRequest> requests = new ArrayList<>();
                for(DataSnapshot d: snapshots) {
                    long timeMilli = (long) d.child("currentTimeMilli").getValue(true);
                    double longitude = (double) d.child("longitude").getValue(true);
                    double lat = (double) d.child("latitude").getValue(true);
                    GiveRequest req = new GiveRequest(timeMilli, longitude, lat);
                    //only add request to list if it happened in the last 15 minutes.
                    if(System.currentTimeMillis() - timeMilli < 900000){
                        requests.add(req);
                    }
                    Log.d("FIREBSE", "netid" + timeMilli + "long lat" + longitude + " " + lat);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase fucked", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        dbref.addListenerForSingleValueEvent(postListener);

        */

        // add markers here
//        mMap.addMarker(new MarkerOptions()
//                .position( new LatLng(36.000919, -78.939372))
//                .title("Duke University")
//                .snippet("test")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open profile activity here
            }
        });

        broadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setLocationImage.getVisibility() == View.VISIBLE) {
                    double lat = mMap.getCameraPosition().target.latitude;
                    double lng = mMap.getCameraPosition().target.longitude;

                    setLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title("My Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.setlocation_scaled)));

                    setLocationImage.setVisibility(View.GONE);
                    broadcastButton.setText("Stop broadcasting location");

                    // update user lat/lng here
                } else {
                    setLocationImage.setVisibility(View.VISIBLE);
                    if (setLocationMarker != null) {
                        setLocationMarker.remove();
                    }
                    broadcastButton.setText("Start broadcasting location");

                    // remove user lat/lng here
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

    public void updateMarkers() {

    }



    /*
            // if GPS permission is granted...
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("permission granted", "true");

            // grab gps location
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            Log.d("currLatLng", currLatLng.toString());

        } else { // gps permission not available
            currLatLng = new LatLng(36.000919, -78.939372); // set to duke's lat/lng if gps not available
            // request location
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    12);
        }
     */
}
