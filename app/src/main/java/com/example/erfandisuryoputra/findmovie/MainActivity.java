package com.example.erfandisuryoputra.findmovie;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText searchKey;
    Button searchButton;
    searchMoviesTask getMovie;

    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private ArrayList<Movie> mMovieList = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new MovieListAdapter(this, mMovieList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                mMovieList.clear();

                //Hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //Get movie
                String url = "http://www.omdbapi.com/?s=" + searchKey.getText() +"&type=movie&apikey=3f9e318f";
                getMovie = new searchMoviesTask();
                getMovie.context = getApplicationContext();
                getMovie.searchKey = searchKey.getText().toString();
                getMovie.mRecyclerView = mRecyclerView;
                getMovie.mMovieList = mMovieList;
                getMovie.execute(url);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (getMovie != null && getMovie.dialog.isShowing())
            getMovie.cancel(true);
        else
            finish();
    }
}