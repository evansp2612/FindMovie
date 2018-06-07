package com.example.erfandisuryoputra.findmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class viewMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_view_movie);
        //Get movie
        String url = "http://www.omdbapi.com/?i=" + getIntent().getStringExtra("imdbID") +"&type=movie&apikey=3f9e318f";
        getMovieTask getMovie = new getMovieTask();
        getMovie.titleView = (TextView) findViewById(R.id.movieName);
        getMovie.posterView = (ImageView) findViewById(R.id.moviePoster);
        getMovie.genreView = (TextView) findViewById(R.id.movieGenre);
        getMovie.runtimeView = (TextView) findViewById(R.id.movieRuntime);
        getMovie.directorView = (TextView) findViewById(R.id.movieDirector);
        getMovie.plotView = (TextView) findViewById(R.id.moviePlot);
        getMovie.ratingView = (TextView) findViewById(R.id.movieImdbRating);
        getMovie.activity = viewMovie.this;
        getMovie.execute(url);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
