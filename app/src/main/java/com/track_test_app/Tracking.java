package com.track_test_app;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tracking extends AppCompatActivity implements OnMapReadyCallback {


    ImageView currentlocation;
    private List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
    private static double current_lat;
    private static double current_lng;
    private GoogleMap mMap;
    private static double lat = 0.0;
    private static double lng = 0.0;
    private Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    double mylng, mylat;
    TextView txtmylocation;
    Button btnconfirmlocation;
    TinyDB tinyDB;
    String latitute = "";
    String langitute = "";
    private static String place_name;
    private GpsTracker gpsTracker;

    private DatabaseReference reference;
    int id = 0;
    private LocationManager manager;
    private final int MIN_TIME = 1000; // 1 sec
    private final int MIN_DISTANCE = 1; // 1 metter

    List<Model_Location> listss = new ArrayList<>();
    List<Marker> markers = new ArrayList<>();
    Marker myMarker;
    String Group = "Group_";
    String groupID = "";

    String Event = "Event_";
    String eventID = "";
    boolean camermove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        tinyDB = new TinyDB(this);

        groupID = Group + tinyDB.getString("GroupId");
        eventID = Event + tinyDB.getString("eventID");

        reference = FirebaseDatabase.getInstance().getReference().child("Tracking");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        Log.e("listMembers", String.valueOf(Event_Details_Group_Details.groupMembersList.size()));

        readChanges();
//        MainActivity.startLocationTracking(Tracking.this);

        Intent intent = new Intent(Tracking.this, GoogleService.class);
        startService(intent);
        Log.e("start", "start");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.animateCamera(CameraUpdateFactory.zoomTo(7));
//        mMap.setMinZoomPreference();


    }

    private void readChanges() {


        Model_Location location = new Model_Location();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listss.clear();

                if (dataSnapshot.getChildrenCount() != 0) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Log.e("eeeeee", "onDataChange: entered list adding");
                        Model_Location post = new Model_Location();
                        post = snapshot.getValue(Model_Location.class);
                        post.setTag(snapshot.getKey());

                        listss.add(post);

                    }
                    Log.e("error", listss.size() + "......" + listss.get(0).getTag());
                    creteMarksandUpdate();
                } else {

//                    saveData(Event_Details_Group_Details.groupMembersList);
                    Log.e("noData", String.valueOf(dataSnapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("error", databaseError.toString());

            }
        });
    }


    /// to set the UserId value in the location change

//    private void saveData(List<ModelGroupMembersDatum> groupMembersList) {
//
//        String User = "User_";
//        String userId = User + "20";
//
//        for (int i = 0; i < groupMembersList.size(); i++) {
//
//
//            userId = User + groupMembersList.get(i).getId();
//            reference = FirebaseDatabase.getInstance().getReference().child("Tracking").child(eventID).child(groupID).child(userId);
//
//            Model_Location location = new Model_Location();
//
//            double lat = 0;
//
//            location.setColor("");
//            location.setLang(lat);
//            location.setLang(lat);
//
//            reference.setValue(location);
//
////            reference.setValue(groupMembersList.get(i).getId());
//
//        }
////        Model_Location location1 = new Model_Location();
////
////        location1.setLang(23.78518800);
////        location1.setLat(21.4703805);
////
////        reference.setValue(location1);
//    }


    // create new marker or update existing one
    private void creteMarksandUpdate() {

        for (int i = 0; i < listss.size(); i++) {
            if (markers.size() == 0) {
//                if (listss.get(0).getLang() == 0) {
//
//                    Log.e("noUser", "noUser");
//
//                } else {

                createMarker(listss.get(i).getLat(), listss.get(i).getLang(), listss.get(i).getTag(), listss.get(i).getName(), listss.get(i).getColor());
                Log.e("createMark", listss.get(i).getTag());

//                }

            } else if (markers.size() <= i) {

//                if (listss.get(0).getLang() == 0) {
//
//                    Log.e("noUser", "noUser");
//
//                } else {

                createMarker(listss.get(i).getLat(), listss.get(i).getLang(), listss.get(i).getTag(), listss.get(i).getName(), listss.get(i).getColor());
                Log.e("createMark", "markerCreated");

//                }

            } else {

//                Marker marker = markers.get(i);
                if (markers.get(i).getTag().equals(listss.get(i).getTag())) { //if a marker has desired tag
                    //Do something in the way. Hmmmm. Yeah
                    Log.e("markerUpdates", listss.get(i).getTag());
                    markers.get(i).setPosition(new LatLng(listss.get(i).getLat(), listss.get(i).getLang()));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(markers.get(i).getPosition()));


                } else {

//                    if (listss.get(0).getLang() == 0) {
//
//                        Log.e("noUser", "noUser");
//
//                    } else {

                    createMarker(listss.get(i).getLat(), listss.get(i).getLang(), listss.get(i).getTag(), listss.get(i).getName(), listss.get(i).getColor());
                    Log.e("create", "markerCreated");

//                    }
                }
            }


        }


    }


    // create new marker
    public void createMarker(double latitude, double longitude, String tag, String name, String color) {

        Marker addMarker;


        addMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(name));

//        addMarker.setIcon(getMarkerIcon(color));
        if (tag.equals(tinyDB.getString("Id"))) {

            addMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));

        } else {
            addMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.man));
        }


        addMarker.setTag(tag);

        if (camermove == false) {

            mMap.moveCamera(CameraUpdateFactory.newLatLng(addMarker.getPosition()));
            camermove = true;

        }

        markers.add(addMarker);


//        return markers;
    }


    // method definition
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }


}