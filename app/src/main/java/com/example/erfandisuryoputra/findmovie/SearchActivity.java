package com.example.erfandisuryoputra.findmovie;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_search);

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
                if (searchKey.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter movie's title", Toast.LENGTH_SHORT).show();
                    return;
                }

                mMovieList.clear();

                //Hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (Util.haveConnection(getApplicationContext())) {
                    //Get movie
                    String url = "http://www.omdbapi.com/?s=" + searchKey.getText() + "&type=movie&apikey=3f9e318f";
                    getMovie = new searchMoviesTask();
                    getMovie.context = getApplicationContext();
                    getMovie.activity = SearchActivity.this;
                    getMovie.searchKey = searchKey.getText().toString();
                    getMovie.mRecyclerView = mRecyclerView;
                    getMovie.mMovieList = mMovieList;
                    getMovie.execute(url);
                }
                else
                    Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
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