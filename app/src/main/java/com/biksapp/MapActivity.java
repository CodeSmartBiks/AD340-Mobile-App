package com.biksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng currentLocation = new LatLng(47.79469261671784, -122.30271454279551);
        mMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Seattle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }
}*/

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    //local variables

    ArrayList<Cameras> cameraList = null;
    private GoogleMap map;
    double[] coordinate;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        loadCamData();
    }

    private void fetchLastLocation() {
        ///permission check
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE );
        }
        Task<Location> task= fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                //if finds the location proceeds
                if(location !=null){
                    currentLocation= location;
                    Toast.makeText(getApplicationContext(),currentLocation.getLatitude()
                    + ""+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment= (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //looping through all the list of cam to display
        for (int i=0; i < cameraList.size(); i++) {
            Cameras camera = cameraList.get(i);
            LatLng latLng = new LatLng(camera.getCoordinate()[0], camera.getCoordinate()[1]);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                    .title(camera.description).snippet(camera.getImageUrl());
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            Marker marker= googleMap.addMarker((markerOptions));
            marker.setTag(i);
        }
        //lat long for the location and adding the marker
        LatLng latLng= new LatLng( 47.5673, -122.2641);
        Marker marker= googleMap.addMarker(new MarkerOptions().
                position(latLng).title("Current Location"));
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        marker.setTag(0);

//        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
//        MarkerOptions markerOptions= new MarkerOptions().position(latLng).title("Current Location");
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
//        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

    //loading the traffic cam from previous assignment
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

}
