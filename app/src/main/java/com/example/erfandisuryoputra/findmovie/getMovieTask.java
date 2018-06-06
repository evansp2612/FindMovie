package com.example.erfandisuryoputra.findmovie;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.text.Layout;
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

    Context context;
    //String searchKey;

//    public final ProgressDialog dialog = new ProgressDialog(context);

    @Override
    protected void onPreExecute() {
//        this.dialog.setMessage("Searching...");
//        this.dialog.setCanceledOnTouchOutside(false);
//        this.dialog.onBackPressed();
//        this.dialog.show();
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
                poster = poster + "1000.jpg";
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
        //if (response.equals("SUCCESS")) {
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
//        }
//        else {
//            Handler error = new Handler(context.getMainLooper());
//            error.post(new Runnable() {
//                public void run() {
//                    Toast.makeText(context, "\""+searchKey+"\" not found", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        this.dialog.dismiss();
    }
}