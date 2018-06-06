/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.erfandisuryoputra.findmovie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.erfandisuryoputra.findmovie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Shows how to implement a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private final ArrayList<Movie> mMovieList;
    private final LayoutInflater mInflater;
    private Context context;

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView titleItemView;
        public final TextView yearItemView;
        public final ImageView posterItemView;
        final MovieListAdapter mAdapter;
        String id;

        /**
         * Creates a new custom view holder to hold the view to display in the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views for the RecyclerView.
         */
        public MovieViewHolder(View itemView, MovieListAdapter adapter) {
            super(itemView);
            titleItemView = (TextView) itemView.findViewById(R.id.title);
            yearItemView = (TextView) itemView.findViewById(R.id.year);
            posterItemView = (ImageView) itemView.findViewById(R.id.poster);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // All we do here is prepend "Clicked! " to the text in the view, to verify that
            // the correct item was clicked. The underlying data does not change.
            Intent intent = new Intent(context, viewMovie.class);
            intent.putExtra("imdbID", id);
            context.startActivity(intent);
        }
    }

    public MovieListAdapter(Context context, ArrayList<Movie> movieList) {
        mInflater = LayoutInflater.from(context);
        this.mMovieList = movieList;
        this.context = context;
    }

    /**
     * Inflates an item view and returns a new view holder that contains it.
     * Called when the RecyclerView needs a new view holder to represent an item.
     *
     * @param parent The view group that holds the item views.
     * @param viewType Used to distinguish views, if more than one type of item view is used.
     * @return a view holder.
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(R.layout.movielist, parent, false);
        return new MovieViewHolder(mItemView, this);
    }

    /**
     * Sets the contents of an item at a given position in the RecyclerView.
     * Called by RecyclerView to display the data at a specificed position.
     *
     * @param holder The view holder for that position in the RecyclerView.
     * @param position The position of the item in the RecycerView.
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        // Retrieve the data for that position.
        Movie mCurrent = mMovieList.get(position);
        // Add the data to the view holder.
        holder.titleItemView.setText(mCurrent.title);
        holder.yearItemView.setText(mCurrent.year);
        Picasso.get().load(mCurrent.poster).error(R.drawable.poster).into(holder.posterItemView);
        holder.id = mCurrent.imdbID;
    }

    /**
     * Returns the size of the container that holds the data.
     *
     * @return Size of the list of data.
     */
    @Override
    public int getItemCount() {
        return mMovieList.size();
    }
}

