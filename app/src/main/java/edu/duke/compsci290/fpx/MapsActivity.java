package edu.duke.compsci290.fpx;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isGiving = true;
    private Button broadcastButton;
    private LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!isGiving) {
            findViewById(R.id.setLocationMarker).setVisibility(View.GONE);
        }

        broadcastButton = findViewById(R.id.broadcastButton);

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

//        LatLng dukeDefault = new LatLng(36.000919, -78.939372);

        // get current location
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LatLng currLatLng;

        // if GPS permission is granted...
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("permission granted", "true");

            // grab gpa location
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } else { // gps permission not available
            currLatLng = new LatLng(36.000919, -78.939372); // set to duke's lat/lng if gps not available
            // request location
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    12);
        }

        // move camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 18.0f));

        mMap.addMarker(new MarkerOptions()
                .position( new LatLng(36.000919, -78.939372))
                .title("Duke University")
                .snippet("test")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));


        if (isGiving) {
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    Log.d("position", mMap.getCameraPosition().toString());
                }
            });
        }

        broadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void updateMarkers() {

    }
}
