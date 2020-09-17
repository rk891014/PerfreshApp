package com.example.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class GetLocation extends  AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    TextView latitude,longitude,adrressr,distance;
    ProgressBar progressBar;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Geocoder geocoder;
    List<Address> addresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getlocation);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        distance = findViewById(R.id.distance);
        adrressr = findViewById(R.id.address);
        progressBar = findViewById(R.id.progressbar);
        geocoder = new Geocoder(this, Locale.getDefault());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();



    }





    /*Ending the updates for the location service*/
//    @Override
//    protected void onStop() {
//        mGoogleApiClient.disconnect();
//        super.onStop();
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }



    public void settingRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);    // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1000);   // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(GetLocation.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "Location Service not Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            /*Getting the location after aquiring location service*/
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                progressBar.setVisibility(View.INVISIBLE);
                latitude.setText("Latitude: " + String.valueOf(mLastLocation.getLatitude()));
                longitude.setText("Longitude: " + String.valueOf(mLastLocation.getLongitude()));
                Location loc1 = new Location("");
                loc1.setLatitude(mLastLocation.getLatitude());
                loc1.setLongitude(mLastLocation.getLongitude());

                Location loc2 = new Location("");
                loc2.setLatitude(26.3943898);
                loc2.setLongitude(80.3220673);

                float distanceInMeters = loc1.distanceTo(loc2);

//                Toast.makeText(GetLocation.this,String.valueOf(distanceInMeters),Toast.LENGTH_LONG).show();


                double earthRadius = 6371000; //meters
                double dLat = Math.toRadians(mLastLocation.getLatitude()-26.3943898);
                double dLng = Math.toRadians(mLastLocation.getLongitude()-80.3220673);
                double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(Math.toRadians(26.3943898)) * Math.cos(Math.toRadians(80.3220673)) *
                                Math.sin(dLng/2) * Math.sin(dLng/2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                float dist = (float) (earthRadius * c);
                Log.e("distance",String.valueOf(dist));
                distance.setText(String.valueOf(dist));






                try {
                    addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                    adrressr.setText(address);

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("LatLang");
                    Map<String,Object> taskMap = new HashMap<String,Object>();
                    taskMap.put("latitude", mLastLocation.getLatitude());
                    taskMap.put("longitude", mLastLocation.getLongitude());
                    rootRef.updateChildren(taskMap);
//                    Toast.makeText(GetLocation.this,"rohit"+knownName,Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                /*if there is no last known location. Which means the device has no data for the loction currently.
                 * So we will get the current location.
                 * For this we'll implement Location Listener and override onLocationChanged*/
                Log.i("Current Location", "No data for location found");

                if (!mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, GetLocation.this);
            }
        }

    }

    /*When Location changes, this method get called. */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(GetLocation.this,"location changed",Toast.LENGTH_LONG).show();
        latitude.setText("Latitude: " + String.valueOf(mLastLocation.getLatitude()));
        longitude.setText("Longitude: " + String.valueOf(mLastLocation.getLongitude()));
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("LatLang");
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put("latitude", mLastLocation.getLatitude());
        taskMap.put("longitude", mLastLocation.getLongitude());
        rootRef.updateChildren(taskMap);
    }

}