package com.perfresh.food_delivering_app.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.perfresh.food_delivering_app.BuildConfig;
import com.perfresh.food_delivering_app.R;
import com.perfresh.food_delivering_app.utils.OTP_Reciever;
import com.perfresh.food_delivering_app.activities.MainActivity;
import com.perfresh.food_delivering_app.model.user;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class profile_fragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "register";
    public static final int REQUEST_CODE = 1;


    EditText address11,username11,mobileno11,codeEnter;
    Button btnUpload,nextBtn;
    FirebaseAuth fauth;
    String verificaionid;
    TextView name,address1,mobileno1,totalcoins,truefalse2;
    ImageView pback,shareapp;
    PhoneAuthProvider.ForceResendingToken token;
    ProgressBar progressBar;
    Intent intent;
    TextView distance;
    public static String code;
    String lat="26.3900664";
    String lang="80.3214847";
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    public static float distanceInMeters=0;
    Geocoder geocoder;
    LocationManager locationManager;
    String DeviceID;
    public profile_fragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        address11=view.findViewById(R.id.address);
        mobileno11=view.findViewById(R.id.contactno);
        username11=view.findViewById(R.id.username);
        pback=view.findViewById(R.id.pback);
        name=view.findViewById(R.id.name);
        codeEnter = view.findViewById(R.id.codeEnter);
        progressBar = view.findViewById(R.id.progressBar);
        totalcoins = view.findViewById(R.id.totalcoins);
        nextBtn = view.findViewById(R.id.nextBtn);
        shareapp = view.findViewById(R.id.shareapp);
        distance = view.findViewById(R.id.distance);
        truefalse2 = view.findViewById(R.id.truefalse2);
        address1=view.findViewById(R.id.address1);
        mobileno1=view.findViewById(R.id.contactno1);
        btnUpload=view.findViewById(R.id.btnUpload);


        requestpermission();
        fauth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);
        codeEnter.setVisibility(View.GONE);
        btnUpload.setVisibility(View.INVISIBLE);
        distance.setVisibility(View.GONE);

         DeviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkconnection() && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ){
                    if(code.equals(codeEnter.getText().toString())) {
                        while (distance.getText().toString().equals(" ")) {
                            getLocation();
                        }
                        if (!address11.getText().toString().isEmpty() && !username11.getText().toString().isEmpty() && !mobileno11.getText().toString().isEmpty()) {
                            if (truefalse2.getText().toString().equals("true")) {
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("customer detail").child(DeviceID);
                                Map<String, Object> taskMap = new HashMap<String, Object>();
                                taskMap.put("distance", distance.getText().toString());
                                taskMap.put("name", username11.getText().toString());
                                taskMap.put("mobileno", mobileno11.getText().toString());
                                taskMap.put("address", address11.getText().toString());
                                rootRef.updateChildren(taskMap);
                            } else {
                                addcomment();
                            }
                            Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                            ((MainActivity) getActivity()).setCurrentItem(0, true);
                            progressBar.setVisibility(View.GONE);
                            codeEnter.setVisibility(View.GONE);
                            btnUpload.setVisibility(View.INVISIBLE);
                            distance.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getContext(), "All fields Required", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Entered code is wrong", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(), "check your connection or location is OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("customer detail").child(DeviceID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    user u=dataSnapshot.getValue(user.class);
                    Log.d(TAG, "onDataChange: data : "+dataSnapshot.getValue().toString());
                    truefalse2.setText("true");
                    username11.setText(u.getName());
                    address11.setText(u.getAddress());
                    mobileno11.setText(u.getMobileno());
                    name.setText(u.getName());
                    address1.setText(u.getAddress());
                    mobileno1.setText(u.getMobileno());
                    totalcoins.setText(String.valueOf(Math.round(Float.parseFloat(u.getShares())*500+Float.parseFloat(u.getOrders()))));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: called");
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distance.setVisibility(View.GONE);
                nextBtn.setVisibility(View.GONE);
                if(checkconnection()){
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if(!address11.getText().toString().isEmpty() && !username11.getText().toString().isEmpty() && !mobileno11.getText().toString().isEmpty()) {
                            progressBar.setVisibility(View.VISIBLE);
                        settingRequest();
                        String phoneno = mobileno11.getText().toString();
                        Log.e("phoneno", String.valueOf(phoneno.length()));
                            if (phoneno.length() == 10) {
                                String phonenum = "+91" + phoneno;
                                progressBar.setVisibility(View.VISIBLE);
                                requestOtp(phonenum);
                            } else {
                                Toast.makeText(getContext(), "Entered no is wrong", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getContext(),"All fields Required",Toast.LENGTH_LONG).show();
                            nextBtn.setVisibility(View.VISIBLE);
                        }
                    }else{
                        requestpermission();
                        nextBtn.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();
                    nextBtn.setVisibility(View.VISIBLE);
                }

            }
        });

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String message="We are Purfresh, delivering veggies, groceries, dairy products, ice cream, chocolate, cake, " +
                        "with in 30 mins to your home. Hope this helps.\n Stay in. Stay safe.\n\n";
                String shareMessage=message+ "\nLet me recommend you this application\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n"
                        +"Coupon Code : "+ mobileno1.getText().toString()+"\n\n"+"   copy this code to get coins"+"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });


        return view;
    }
    private void addcomment() {
        String DeviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("customer detail");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",username11.getText().toString());
        hashMap.put("DeviceId",DeviceID);
        hashMap.put("shares","1");
        hashMap.put("orders","0");
        hashMap.put("address",address11.getText().toString());
        hashMap.put("no","1");
        hashMap.put("mobileno",mobileno11.getText().toString());
        hashMap.put("distance",distance.getText().toString());
        reference.child(DeviceID).setValue(hashMap);
        address11.setText("");
        username11.setText("");
        mobileno11.setText("");
        Toast.makeText(getContext(),"Registered successfully",Toast.LENGTH_LONG).show();

    }
    private void requestpermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int grant = ContextCompat.checkSelfPermission(getContext(), permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;

            ActivityCompat.requestPermissions(getActivity(), permission_list, 1);
        }
    }





    private void requestOtp(String phonenum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenum, 60L, TimeUnit.SECONDS, getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.VISIBLE);
                codeEnter.setVisibility(View.VISIBLE);
                verificaionid = s;
                token = forceResendingToken;
                new OTP_Reciever().setEditText(codeEnter);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(getContext(), "Click upload to upload your details", Toast.LENGTH_SHORT).show();
                btnUpload.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.GONE);
                codeEnter.setVisibility(View.VISIBLE);
                code=phoneAuthCredential.getSmsCode();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getContext(), "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }








    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), 90000);
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
                            startIntentSenderForResult(status.getResolution().getIntentSender(), 1000, null, 0, 0, 0, null);
//                            status.startResolutionForResult(getActivity(), 1000);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.e("onActivityResult","rohit");
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        ((MainActivity)getActivity()).setCurrentItem (2, true);
                        Toast.makeText(getContext(), "Location Service not Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }
    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestpermission();
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
                getlatlang();
                progressBar.setVisibility(View.INVISIBLE);
                Location loc1 = new Location("");
                loc1.setLatitude(mLastLocation.getLatitude());
                loc1.setLongitude(mLastLocation.getLongitude());

                Location loc2 = new Location("");
                loc2.setLatitude(Double.parseDouble(lat));
                loc2.setLongitude(Double.parseDouble(lang));

                distanceInMeters = loc1.distanceTo(loc2);

//                Toast.makeText(getContext(),String.valueOf(distanceInMeters),Toast.LENGTH_LONG).show();
                distance.setText(String.valueOf(distanceInMeters));


            } else {
                /*if there is no last known location. Which means the device has no data for the loction currently.
                 * So we will get the current location.
                 * For this we'll implement Location Listener and override onLocationChanged*/
                Log.i("Current Location", "No data for location found");

                if (!mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }

    }

    public void getlatlang(){
        DatabaseReference query2 = FirebaseDatabase.getInstance().getReference("LatLang");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lat = dataSnapshot.child("latitude").getValue().toString();
                    lang = dataSnapshot.child("longitude").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        progressBar.setVisibility(View.INVISIBLE);
    }
    public boolean checkconnection(){
        ConnectivityManager manager=(ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork=manager.getActiveNetworkInfo();
        if(null!=activenetwork){
            if(activenetwork.getType()==ConnectivityManager.TYPE_WIFI){
                return true;
            }
            else if(activenetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }
        return false;
    }
}