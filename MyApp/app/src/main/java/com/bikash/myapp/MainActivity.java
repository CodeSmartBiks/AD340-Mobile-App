package com.bikash.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Send = findViewById(R.id.Send);
        Button ZipOne = findViewById(R.id.ZipOne);
        Button ZipTwo = findViewById(R.id.ZipTwo);
        Button ZipThree = findViewById(R.id.ZipThree);
        Button ZipFour = findViewById(R.id.ZipFour);
        Send.setOnClickListener(this);
        ZipOne.setOnClickListener(this);
        ZipTwo.setOnClickListener(this);
        ZipThree.setOnClickListener(this);
        ZipFour.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Send:
                Toast.makeText(this, "Send Button Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ZipOne:
                Toast.makeText(this, "Zip One Button Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ZipTwo:
                Toast.makeText(this, "Zip two Button Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ZipThree:
                Toast.makeText(this, "Zip three Button Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ZipFour:
                Toast.makeText(this, "Zip four Button Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}