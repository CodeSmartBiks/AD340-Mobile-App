package com.biksapp;


import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrafficCameraActivity extends AppCompatActivity {

    private String URLstring = " https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
    private static ProgressDialog mProgressDialog;
    private ListView listView;
    private ListAdapter listAdapter;
    public static final String TAG = "Basic Network Demo";
    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;

    ArrayList<Cameras> cameraList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_listview);

        //Checking the connection
        if (checkNetwork()){
            retrieveJSON();
        }else if(!checkNetwork()){
            Toast.makeText(TrafficCameraActivity.this,"Please connect to Internet.",Toast.LENGTH_LONG).show();
            Toast.makeText(TrafficCameraActivity.this,"Please connect your device!",Toast.LENGTH_LONG).show();
        }

        listView = findViewById(R.id.listView);
        retrieveJSON();
    }

    // main function to read the data
    private void retrieveJSON() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray features = obj.getJSONArray("Features");
                            for (int j = 0; j < features.length(); j++) {
                                JSONArray cameras = features.getJSONObject(j).getJSONArray("Cameras");
                                Cameras camera = new Cameras(cameras.getJSONObject(0).getString("Description"),
                                        cameras.getJSONObject(0).getString("ImageUrl"),
                                        cameras.getJSONObject(0).getString("Type"));
                                cameraList.add(camera);
                            }
                            setupListView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //toast message if any error
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void setupListView() {
//        removeSimpleProgressDialog();  //will remove progress dialog
        listAdapter = new ListAdapter(this, cameraList);
        listView.setAdapter(listAdapter);
    }
    //remove the dialogbox
    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkNetwork() {
        boolean have_WIFI = false;
        boolean have_MobData = false;
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                Log.i(TAG, getString(R.string.wifi_connection));
                have_WIFI = true;
            }
            if (mobileConnected) {
                Log.i(TAG, getString(R.string.mobile_connection));
                have_MobData = true;
            }
            else {
                Log.i(TAG, getString(R.string.no_wifi_or_mobile));
            }

        }
        return have_MobData || have_WIFI;
    }
    // this  function is for the navigation bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }
}