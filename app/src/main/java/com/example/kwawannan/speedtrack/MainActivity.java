package com.example.kwawannan.speedtrack;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.AwesomeSpeedometer;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView textView;
    AwesomeSpeedometer awesomeSpeedometer;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;


    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AppRate.with(this)
//                .setInstallDays(0) // default 10, 0 means install day.
//                .setLaunchTimes(2) // default 10
//                .setRemindInterval(2) // default 1
//                .setShowLaterButton(true) // default true
//                .setDebug(true) // default false
//                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
//                    @Override
//                    public void onClickButton(int which) {
////                        Log.d(MainActivity.class.getName(), Integer.toString(which));
////                        launchMarket();
//                        Toast.makeText(this," went wrong", Toast.LENGTH_LONG).show();
//
//                    }
//                })
//                .monitor();
//
//        // Show a dialog if meets conditions
//        AppRate.showRateDialogIfMeetsConditions(this);
        awesomeSpeedometer = (AwesomeSpeedometer) findViewById(R.id.speedView);
        awesomeSpeedometer.setUnit("MPH");
        awesomeSpeedometer.setSpeedTextSize(110);
        awesomeSpeedometer.setUnitTextSize(70);

        textView = (TextView) findViewById(R.id.textView);

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        }else{
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }


        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        this.onLocationChanged(null);
        Toast.makeText(this, "Please drive around to see change in speed", Toast.LENGTH_LONG).show();

        //Ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest2 = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest2);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


        if (Helper.isAppRunning(MainActivity.this, "com.your.desired.app")) {
            // App is running
        } else {
            // App is not running
        }




    }

    @Override
    public void onLocationChanged(Location location) {


        try {
            if (location == null) {
                textView.setText("0.00 M/H");

            } else {
                float mCurrentSpeed = location.getSpeed() * 2;
                //tweaking the speed for accuracy
                if (mCurrentSpeed > 3) {
                    mCurrentSpeed = mCurrentSpeed + 3;
                } else {

                }

                Log.i("gps", String.valueOf(mCurrentSpeed));
                textView.setText(Float.toString(mCurrentSpeed) + "M/s");
                awesomeSpeedometer.speedTo((int) mCurrentSpeed);


            }
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    private void launchMarket(){
        Uri uri = Uri.parse("market://details?id=" + "com.annan.kwaw.courtcounterscoreboardapp");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
