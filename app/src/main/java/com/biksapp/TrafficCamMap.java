package com.biksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.biksapp.Cameras;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class TrafficCamMap extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ArrayList<Cameras> cameraList = null;
    private boolean permissionDenied = false;
    private GoogleMap map;
    private GoogleMap mMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    double[] coordinate;
    private static final int REQUEST_CODE = 101;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_cam_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment supportMapFragment =  (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(TrafficCamMap.this::onMapReady);
        //loadCamData();
    }

    private void fetchLastLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.
//                PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED) {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation=location;
                    Toast.makeText(getApplicationContext(),"My Location is: "+currentLocation.getLatitude()+"-"+currentLocation.
                            getLongitude(),Toast.LENGTH_LONG).show();
                    SupportMapFragment supportMapFragment =  (SupportMapFragment) getSupportFragmentManager().
                            findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(TrafficCamMap.this::onMapReady);

                }
                loadCamData();
            }
        });
    }
    private void loadCamData() {
        this.cameraList = new ArrayList<>();
        String camUrl = " https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, camUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray features = obj.getJSONArray("Features");
                            for (int j = 0; j < features.length(); j++) {
                                JSONObject point = features.getJSONObject(j);
                                JSONArray points = point.getJSONArray("PointCoordinate");
                                coordinate = new double[]{points.getDouble(0), points.getDouble(1)};
                                JSONArray cameras = features.getJSONObject(j).getJSONArray("Cameras");
                                Cameras camera = new Cameras(cameras.getJSONObject(0).getString("Description"),
                                        cameras.getJSONObject(0).getString("ImageUrl"),
                                        cameras.getJSONObject(0).getString("Type"), coordinate);
                                cameraList.add(camera);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (int i=0; i < cameraList.size(); i++) {
            Cameras camera = cameraList.get(i);
            LatLng position = new LatLng(camera.getCoordinate()[0], camera.getCoordinate()[1]);
            MarkerOptions markerOptions = new MarkerOptions().position(position)
                    .title(camera.description).snippet(camera.getImageUrl());
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(position));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
            Marker marker= googleMap.addMarker((markerOptions));
            marker.setTag(i);
        }
        LatLng latLng= new LatLng( 47.5673, -122.2641);
        Marker marker= googleMap.addMarker(new MarkerOptions().
                position(latLng).title("Current Location"));
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        marker.setTag(0);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }
}
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(TrafficCamMap.this::onMapReady);
//    }
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                mMap = googleMap;
//                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//                    @Override
//                    public void onMapLoaded() {
//                        mMap = googleMap;
//                       loadCamData();
//                    }
//                });
//            }
//
//
//
//
//
//    public void getLastLocation() {
//        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);
//        try {
//            locationClient.getLastLocation()
//                    .addOnSuccessListener(new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // GPS location can be null if GPS is switched off
//                            if (location != null) {
//                                if (mMap != null) {
//                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
//                                }
//                            }
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
//                            e.printStackTrace();
//                        }
//                    });
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getLastLocation();
//                }
//                break;
//        }
//
//    }
//    private void loadCamData() {
//        this.cameraList = new ArrayList<>();
//        String camUrl = " https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, camUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            JSONArray features = obj.getJSONArray("Features");
//                            for (int j = 0; j < features.length(); j++) {
//                                JSONObject point = features.getJSONObject(j);
//                                JSONArray points = point.getJSONArray("PointCoordinate");
//                                coordinate = new double[]{points.getDouble(0), points.getDouble(1)};
//                                JSONArray cameras = features.getJSONObject(j).getJSONArray("Cameras");
//                                Cameras camera = new Cameras(cameras.getJSONObject(0).getString("Description"),
//                                        cameras.getJSONObject(0).getString("ImageUrl"),
//                                        cameras.getJSONObject(0).getString("Type"), coordinate);
//                                cameraList.add(camera);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
//}
