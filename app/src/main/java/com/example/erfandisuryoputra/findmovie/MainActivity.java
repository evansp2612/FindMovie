package com.example.erfandisuryoputra.findmovie;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText searchKey;
    Button searchButton;
    getMoviesTask getMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        searchKey = (EditText) findViewById(R.id.editText);
        searchButton = (Button) findViewById(R.id.button);
        searchKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId
                        == EditorInfo.IME_ACTION_DONE)) {
                    searchButton.performClick();
                }
                return false;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //Get movie
                String url = "http://www.omdbapi.com/?t=" + searchKey.getText() +"&type=movie&apikey=3f9e318f";
                getMovies = new getMoviesTask();
                getMovies.execute(url);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (getMovies != null && getMovies.dialog.isShowing())
            getMovies.cancel(true);
        else
            finish();
    }

    private class getMoviesTask extends AsyncTask<String,String,String> {
        String title = "";
        String poster = "";
        String year = "";
        String genre = "";
        String director = "";
        String plot = "";
        String rating = "";

        TextView titleView = (TextView) findViewById(R.id.movieName);
        ImageView posterView = (ImageView) findViewById(R.id.moviePoster);
        TextView genreView = (TextView) findViewById(R.id.movieGenre);
        TextView directorView = (TextView) findViewById(R.id.movieDirector);
        TextView plotView = (TextView) findViewById(R.id.moviePlot);
        TextView ratingView = (TextView) findViewById(R.id.movieImdbRating);

        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Searching...");
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.onBackPressed();
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                JSONObject response = new JSONObject(result);

                if (response.getString("Response").equals("False"))
                    return "FAIL";

                title = response.getString("Title");
                poster = response.getString("Poster");
                //Resize poster
                if (!poster.equals("N/A")){
                    poster = poster.substring(0, poster.length()-7);
                    poster = poster + "600.jpg";
                }
                year = response.getString("Year");
                genre = response.getString("Genre");
                director = response.getString("Director");
                plot = response.getString("Plot");
                rating = response.getString("imdbRating");

                return "SUCCESS";
            } catch (IOException e) {
                e.printStackTrace();
                return "FAIL";
            } catch (JSONException e) {
                e.printStackTrace();
                return "FAIL";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response.equals("SUCCESS")) {
                titleView.setText(title + " ("+year+")");
                Picasso.get().load(poster).error(R.drawable.poster).into(posterView);
                genreView.setText(genre);
                directorView.setText("Director: "+director);
                ratingView.setText("IMDB Rating: "+rating);
                plotView.setText(plot);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    plotView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                }
            }
            else {
                Handler error = new Handler(getApplicationContext().getMainLooper());
                error.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "\""+searchKey.getText()+"\" not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            this.dialog.dismiss();
        }
    }
}