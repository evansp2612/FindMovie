package com.example.erfandisuryoputra.findmovie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
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
                String url = "http://www.omdbapi.com/?t=" + searchKey.getText().toString() +"&apikey=3f9e318f";
                getMoviesTask task = new getMoviesTask();
                task.execute(url);
            }
        });
    }

    private class getMoviesTask extends AsyncTask<String,String,String> {
        String title = "";
        String poster = "";
        String year = "";
        String genre = "";
        String director = "";
        String plot = "";
        String rating = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                JSONObject response = new JSONObject(result);

                title = response.getString("Title");
                poster= response.getString("Poster");
                year = response.getString("Year");
                genre = response.getString("Genre");
                director= response.getString("Director");
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
                TextView textView = (TextView) findViewById(R.id.movieName);
                textView.setText(title + " ("+year+")");

                textView = (TextView) findViewById(R.id.movieGenre);
                textView.setText(genre);

                ImageView posterimg = (ImageView) findViewById(R.id.moviePoster);
                SetPoster task = new SetPoster(posterimg);
                task.execute(poster);

                textView = (TextView) findViewById(R.id.movieDirector);
                textView.setText("Director: "+director);

                textView = (TextView) findViewById(R.id.movieImdbRating);
                textView.setText("IMDB Rating: "+rating);

                textView = (TextView) findViewById(R.id.moviePlot);
                textView.setText(plot);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                }
            }
        }
    }

    private class SetPoster extends AsyncTask<String, Void, Bitmap> {
        ImageView poster;
        public SetPoster(ImageView bmImage) {
            this.poster = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap image) {
            poster.setImageBitmap(image);
        }
    }
}