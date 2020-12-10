package com.track_test_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btnsignin;
    EditText editpassword, editphone;
    ImageView showpasswordview;
    int showPassword = 0;
    TinyDB tinyDB;
    public static LinearLayout loading;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tinyDB = new TinyDB(this);

        btnsignin = findViewById(R.id.btnSignIn);
        editphone = findViewById(R.id.editPhone);
        editpassword = findViewById(R.id.editPassword);
        showpasswordview = findViewById(R.id.showpassword);
        showpasswordview.setImageResource(R.drawable.invisible_svg);
        loading = findViewById(R.id.loading);

        getLocationPermission(MainActivity.this);
        tinyDB.putString("Name", "0");
        showpasswordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (showPassword == 0) {
                    showPassword = 1;
                    editpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    showpasswordview.setImageResource(R.drawable.visibility_svg);


                } else if (showPassword == 1) {
                    editpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword = 0;
                    showpasswordview.setImageResource(R.drawable.invisible_svg);


                }

            }
        });
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editphone.getText().toString().equals("")) {


                    editphone.setError(getString(R.string.enter_phone));

                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(1)
                            .playOn(findViewById(R.id.editPhone));


                } else if (editpassword.getText().toString().equals("")) {


                    editpassword.setError(getString(R.string.enter_password));

                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(1)
                            .playOn(findViewById(R.id.editPassword));


                } else {

//                    if (mLocationPermissionsGranted == true) {
                        if (editphone.getText().toString().equals("01110110111") && editpassword.getText().toString().equals("123123")) {

//                        loading.setVisibility(View.VISIBLE);
                            startActivity(new Intent(MainActivity.this, Tracking.class));

                            tinyDB.putString("Name", "alla hassan");
                            tinyDB.putString("Id", "User_1");


                        } else if (editphone.getText().toString().equals("01001002003") && editpassword.getText().toString().equals("123456")) {

//                        loading.setVisibility(View.VISIBLE);
                            startActivity(new Intent(MainActivity.this, Tracking.class));

                            tinyDB.putString("Name", "khaled nabwy");
                            tinyDB.putString("Id", "User_2");

                        } else if (editphone.getText().toString().equals("01004005006") && editpassword.getText().toString().equals("456456")) {

//                        loading.setVisibility(View.VISIBLE);
                            startActivity(new Intent(MainActivity.this, Tracking.class));
                            tinyDB.putString("Name", "ebrahim mohamed");
                            tinyDB.putString("Id", "User_3");

                        } else if (editphone.getText().toString().equals("01112224445") && editpassword.getText().toString().equals("456789")) {

//                        loading.setVisibility(View.VISIBLE);
                            startActivity(new Intent(MainActivity.this, Tracking.class));
                            tinyDB.putString("Name", "ali ahmed");
                            tinyDB.putString("Id", "User_4");

                        } else if (editphone.getText().toString().equals("011199988877") && editpassword.getText().toString().equals("789789")) {

//                        loading.setVisibility(View.VISIBLE);
                            startActivity(new Intent(MainActivity.this, Tracking.class));
                            tinyDB.putString("Name", "abdelrhman esam");
                            tinyDB.putString("Id", "User_5");

                        } else {

                            editphone.setError("please check your phone");

                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(1)
                                    .playOn(findViewById(R.id.editPhone));


                            editpassword.setError("please check your number");

                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(1)
                                    .playOn(findViewById(R.id.editPassword));
                        }


//                    } else {
//
//                        Log.e("errorNoPermission", "No Permission");
//                    }


                }

//                startActivity(new Intent(SignIn.this, MainActivity.class));
//                finish();

            }
        });

    }

    public static void stopLocationTracking(Context context) {

        Intent intent = new Intent(context, GoogleService.class);
        context.stopService(intent);


    }


    public static void startLocationTracking(Context context) {

        getLocationPermission(context);


    }


    //GET PREMISSION IF YES OR NO
    public static void getLocationPermission(Context context) {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(context),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(context,
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
//                getDeviceLocation(context);

//                loading.setVisibility(View.GONE);
//                try {
//                    Intent intent = new Intent(context, GoogleService.class);
//                    context.startService(intent);
//                    Log.e("start", "start");
//                } catch (Exception e) {
//
//                    Log.e("didnt_start", "didnt_start");
//
//                }


            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);

                loading.setVisibility(View.GONE);
//
//                requestPermissions(permissions,LOCATION_PERMISSION_REQUEST_CODE);
//                if (ContextCompat.checkSelfPermission(this,
//                        COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Objects.requireNonNull(this),
//                        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
//                    mLocationPermissionsGranted = true;
//                    getDeviceLocation();
//                }

            }
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
            loading.setVisibility(View.GONE);

//            requestPermissions(permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

}