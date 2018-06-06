package com.example.erfandisuryoputra.findmovie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Layout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class getMovieTask extends AsyncTask<String,String,String> {
    String title = "";
    String poster = "";
    String year = "";
    String genre = "";
    String runtime = "";
    String director = "";
    String plot = "";
    String rating = "";

    TextView titleView;
    ImageView posterView;
    TextView genreView;
    TextView runtimeView;
    TextView directorView;
    TextView plotView;
    TextView ratingView;

    Activity activity;

    public ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.onBackPressed();
        dialog.show();
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
                poster = poster + "800.jpg";
            }
            year = response.getString("Year");
            genre = response.getString("Genre");
            runtime = response.getString("Runtime");
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
        titleView.setText(title + " ("+year+")");
        Picasso.get().load(poster).error(R.drawable.poster).into(posterView);
        genreView.setText(genre);
        runtimeView.setText(runtime);
        directorView.setText("Director: "+director);
        ratingView.setText("IMDB Rating: "+rating);
        plotView.setText(plot);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            plotView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        dialog.dismiss();
    }
}