package com.sai.newmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sai.newmap.dao.ApplicationDataPreferences;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    FloatingActionButton floatingButton;
    GPSTracker gpsTracker;
    private static final String TAG = "maps";
    String city = "";
    String state = "";
    String country = "";
    String district = "";
    String knownName = "";
    TextView longLatText,addressText;
    CircleOptions circleOptions = new CircleOptions();
    ApplicationDataPreferences applicationDataPreferences;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        applicationDataPreferences = ApplicationDataPreferences.getInstance(MapsActivity.this);

        floatingButton = (FloatingActionButton)findViewById(R.id.floatingButton);
        addressText = (TextView)findViewById(R.id.addressText);
        longLatText = (TextView)findViewById(R.id.longLatText);



        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        gpsTracker = new GPSTracker(MapsActivity.this);
        if(gpsTracker.getIsGPSTrackingEnabled()) {
            Log.d(TAG, "onCreate: gpsTracker" + gpsTracker.getAddressLine(MapsActivity.this));
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                Log.d(TAG, "onCreate:getLatitude " + gpsTracker.getLatitude() + "  " + gpsTracker.getLongitude());
                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onCreate:IOException " + e.getMessage());
            }
            if(addresses != null) {
                Log.d(TAG, "onCreate:addresses " + addresses);
                //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                district = addresses.get(0).getSubAdminArea();
                knownName = addresses.get(0).getFeatureName();
                //String city1 = address.substring(address.indexOf(",") + 1);
                longLatText.setText("Lat: "+gpsTracker.latitude+"  Lon: "+gpsTracker.longitude);
                addressText.setText("Address :"+knownName+","+city+","+district+","+state+","+country);
                Log.d(TAG, "onCreate:cityNamecityName " + " " + district + " " + city + "  " + state + "  " + country + "  " + knownName /*+ "  " + address + "  " + city1*/);

            }
//                    Log.d(TAG, "onCreate:city1city1 " + city1);
//                    Log.d(TAG, "onCreate:addressaddress " + address);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


        CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(gpsTracker.latitude, gpsTracker.longitude));

        googleMap.moveCamera(point);
        googleMap.animateCamera(point);

        circleOptions.center(new LatLng(gpsTracker.latitude, gpsTracker.longitude));
        circleOptions.radius(5000);
        circleOptions.fillColor(R.color.cyan_light);
        circleOptions.clickable(true);
        circleOptions.strokeColor(R.color.cyan_light);
        circleOptions.strokeWidth(0.1f);
        circleOptions.isVisible();

        googleMap.addCircle(circleOptions);


        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(gpsTracker.latitude, gpsTracker.longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(city));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            /*userName.setText(account.getDisplayName());
            userEmail.setText(account.getEmail());
            userId.setText(account.getId());
            try{
                Log.d("main", "handleSignInResult: "+account.getPhotoUrl()+"  "+account);
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }*/

            applicationDataPreferences.setUserName(account.getDisplayName());
            applicationDataPreferences.setUserEmail(account.getEmail());
            applicationDataPreferences.setUserId(account.getId());
            if(account.getPhotoUrl() != null) {
                applicationDataPreferences.setUserImage(account.getPhotoUrl().toString());
            }

        }else{
            //gotoMainActivity();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
