package com.biksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.BreakIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    String[] btnStringArray = {"Trails Hike", "Traffic Cam Map", "Traffic Cam", "Movies"};
    private EditText textInputEmail, textInputPassword, textInputUsername;
    private String userName;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        ButtonAdapter adapter = new ButtonAdapter(MainActivity.this, btnStringArray);

        gridView.setAdapter(new ButtonAdapter(this, btnStringArray));
        gridView.setAdapter(adapter);

        textInputEmail = findViewById(R.id.email);
        textInputPassword = findViewById(R.id.password);
        textInputUsername = findViewById(R.id.username);

        // initiate shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        userName= sharedPreferences.getString("USERNAME","");
        email= sharedPreferences.getString("EMAIL","");
        password= sharedPreferences.getString("PASSWORD","");

        textInputUsername.setText(userName);
        textInputEmail.setText(email);
        textInputPassword.setText(password);

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
            startActivity(new Intent(MainActivity.this, MapActivity.class));
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
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }


    private void signIn() {
        Log.d("FIREBASE", "signIn");


        // 1 - validate name, email, and password entries
        String email = textInputEmail.getText().toString();
        String password = textInputPassword.getText().toString();
        String username = textInputUsername.getText().toString();

        Log.d("Password", "Not working");
        Log.d("UserName", username);

        if (username.isEmpty()) {
            textInputUsername.setError("Please enter a username");
            textInputUsername.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            textInputEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputEmail.setError("Invalid email format");
            textInputEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            textInputPassword.setError("Please enter a valid password");
            textInputPassword.requestFocus();
            return;
        } else if (validatePassword(password)== false){
            textInputPassword.setError("Invalid password format");
            textInputPassword.requestFocus();
        return;
    }

            // 2 - save valid entries to shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("USERNAME",username);
        edit.putString("EMAIL",email);
        edit.putString("PASSWORD",password);
        edit.apply();

            // 3 - sign into Firebase
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .

                            addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful());

                                    if (task.isSuccessful()) {
                                        // update profile
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("FIREBASE", "User profile updated.");
                                                            // Go to FirebaseActivity
                                                            startActivity(new Intent(MainActivity.this, FirebaseActivity.class));
                                                        }
                                                    }
                                                });

                                    } else {
                                        Log.d("FIREBASE", "sign-in failed");

                                        Toast.makeText(MainActivity.this, "Sign In Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
        }
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.btnInfo:
                    signIn();
                    break;
            }
        }
            public boolean validatePassword ( final String password){
                Pattern pattern;
                Matcher matcher;
                final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[A-Z]).{4,}";
                pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher = pattern.matcher(password);

                return matcher.matches();
            }
    }

