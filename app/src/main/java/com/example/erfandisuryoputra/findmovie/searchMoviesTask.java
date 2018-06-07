package com.example.erfandisuryoputra.findmovie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class searchMoviesTask extends AsyncTask<String,String,String> {

    ArrayList<Movie> mMovieList;
    RecyclerView mRecyclerView;
    Activity activity;
    Context context;
    String searchKey;

    public ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Searching...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.onBackPressed();
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            JSONObject response = new JSONObject(result);

            if (response.getString("Response").equals("False")) {
                Handler error = new Handler(context.getMainLooper());
                error.post(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "\""+searchKey+"\" not found", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
                cancel(true);
            }

            JSONArray allmovies = response.getJSONArray("Search");
            for(int i=0;i < allmovies.length();i++){
                JSONObject movie = allmovies.getJSONObject(i);
                String title = movie.getString("Title");
                String year = movie.getString("Year");
                String poster = movie.getString("Poster");
                String id = movie.getString("imdbID");
                mMovieList.add(new Movie(title, year, poster, id));
            }
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
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        else {
            Handler error = new Handler(context.getMainLooper());
            error.post(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Can't connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        }
        dialog.dismiss();
    }
}