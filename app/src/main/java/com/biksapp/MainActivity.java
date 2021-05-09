package com.biksapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String[] btnStringArray = {"Trails Hike", "Traffic Cam Map", "Traffic Cam","Movies"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        ButtonAdapter adapter = new ButtonAdapter(MainActivity.this, btnStringArray);

        gridView.setAdapter(new ButtonAdapter(this, btnStringArray));
        gridView.setAdapter(adapter);
    }

    // this is the code for the toast not needed for second HW after
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnInfo:
//                Toast.makeText(this, "Info", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btnLakes:
//                Toast.makeText(this, "Lakes", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btnMountains:
//                Toast.makeText(this, "Mountains", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btnMovies:
//                startActivity(new Intent(MainActivity.this,MoviesActivity.class));
//                break;
//            case R.id.btnTrails:
//                Toast.makeText(this, "Trails", Toast.LENGTH_SHORT).show();
//                break;
//        }

    //this function is a click event to  show the toast and navigate to movies activity
    public void makeToast(View view) {
        Button btn = (Button) view;
        if (btn.getText() == "Movies") {
            startActivity(new Intent(MainActivity.this, MoviesActivity.class));

        } else if (btn.getText() == "Traffic Cam") {
            startActivity(new Intent(MainActivity.this, TrafficCameraActivity.class));
        } else if (btn.getText() == "Traffic Cam Map") {
            startActivity(new Intent(MainActivity.this, TrafficCamMap.class));
        } else {
            Toast.makeText(getApplicationContext(), btn.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    public class ButtonAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        private String[] btnStringArray;

        public ButtonAdapter(Context c, String[] myStringArray) {
            context = c;
            this.btnStringArray = myStringArray;

        }

        @Override
        public int getCount() {
            return btnStringArray.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grid_item, null);
            }
            Button btn = convertView.findViewById(R.id.button);
            btn.setText(btnStringArray[position]);

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar,menu);
        return true;
    }
}
