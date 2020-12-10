package com.track_test_app;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class GoogleService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 4000;
    private DatabaseReference reference;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;
    TinyDB tinyDB;
    // flag for GPS status
    boolean canGetLocation = false;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3; // 3 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 4 * 1; // 4 Sec


    public GoogleService() {
        Log.e("no permission", "permission");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("no", "no");
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);
        tinyDB = new TinyDB(getApplicationContext());
        String user_id = tinyDB.getString("Id");
        reference = FirebaseDatabase.getInstance().getReference().child("Tracking").child(user_id);
        Log.e("servie", "service");

        fn_getlocation();
    }

    @Override
    public void onLocationChanged(Location location) {

        // here you should pass the color and the user name dont forget

        Log.e("alla hassan", String.valueOf(location));
        saveLocation(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void fn_getlocation() {
        Log.e("fn_getlocation", "getLocation");

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnable = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnable && !isNetworkEnable) {
                Log.e("no network provider", "no network provider");
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnable) {
                    //check the network permission
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.e("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            saveLocation(location);
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnable) {
                    if (location == null) {
                        //check the network permission
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                        }
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.e("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                saveLocation(location);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("expept",e.toString());
        }

//        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//        isGPSEnable = Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
//        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        Log.e("fn_getlocation", "getLocation");
//        if (!isGPSEnable && !isNetworkEnable) {
//
//            Log.e("no per", "permission");
//
//        } else {
//
//            if (isGPSEnable) {
//                location = null;
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions
//                    Log.e("no permiss", "permission");
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//
//
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, this);
//                if (locationManager != null) {
//                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    if (location != null) {
//                        Log.e("latitude111", location.getLatitude() + "");
//                        Log.e("longitude111", location.getLongitude() + "");
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
////                        fn_update(location);
//                    }
//                }
//
//            } else if (isNetworkEnable) {
//                location = null;
//
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, this);
//                if (locationManager != null) {
//                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    if (location != null) {
//
//                        Log.e("latitude999", location.getLatitude() + "");
//                        Log.e("longitude999", location.getLongitude() + "");
//
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
////                        fn_update(location);
//                    }
//                }
//
//            }
//
//        }

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

//    private void fn_update(Location location) {
//
//        intent.putExtra("latutide", location.getLatitude() + "");
//        intent.putExtra("longitude", location.getLongitude() + "");
//        sendBroadcast(intent);
//    }

    private void saveLocation(Location location) {

        Model_Location location1 = new Model_Location();
        String name = tinyDB.getString("Name");

        if (name.equals("0") || name.equals("")) {

            Log.e("Didnt_Login", "Didnt_Login");
        } else {
            location1.setLang(location.getLongitude());
            location1.setLat(location.getLatitude());
            location1.setName(name);

            reference.setValue(location1);

        }


    }


}