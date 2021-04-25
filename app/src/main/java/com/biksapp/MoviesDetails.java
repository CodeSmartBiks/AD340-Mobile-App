package com.biksapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;



public class MoviesDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);

        // Declaring view for population
        TextView description = findViewById(R.id.movieDescription);
        TextView title = findViewById(R.id.movieTitle);
        TextView year = findViewById(R.id.movieYear);
        TextView director = findViewById(R.id.movieDirector);
        ImageView image = findViewById(R.id.movieImage);

        // this is just calling the previous activity and getting all the information
        String[] movieDesc = getIntent().getStringArrayExtra("EXTRA_MOVIE_DETAILS");

        //setting and displaying all the different movie details for the page
        title.setText(movieDesc[0]);
        year.setText(movieDesc[1]);
        director.setText(movieDesc[2]);
        Picasso.get().load(movieDesc[3]).into(image);
        description.setText(movieDesc[4]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar,menu);
        return true;
    }
}