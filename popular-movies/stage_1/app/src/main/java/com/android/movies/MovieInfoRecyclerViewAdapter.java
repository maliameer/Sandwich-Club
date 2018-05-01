package com.android.movies;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ImageView;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import com.android.movies.model.Movie;

import com.android.movies.utils.RestUtils;

public class MovieInfoRecyclerViewAdapter extends RecyclerView.Adapter<MovieInfoRecyclerViewAdapter.MovieAdapterViewHolder> {

    private Context context;

    private static LayoutInflater layoutInflater;

    private LinkedList<Movie> movies;

    public LinkedList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(LinkedList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ((movies != null) ? movies.size() : 0);
    }

    public MovieInfoRecyclerViewAdapter(Context context, LinkedList<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView posterImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.movie_title_tv);
            posterImageView = view.findViewById(R.id.movie_poster_iv);
        }

    }

    @Override
    public  MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movies_list, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, final int position) {

        final Movie movie = movies.get(position);

        movieAdapterViewHolder.titleTextView.setText(movie.getOriginalTitle());
        movieAdapterViewHolder.posterImageView.setTooltipText(movie.getOriginalTitle());

        String posterPath = RestUtils.MOVIE_IMAGES_BASE_URL + movie.getMoviePosterImageUrl();
        Picasso.with(context).load(posterPath).into(movieAdapterViewHolder.posterImageView);
        movieAdapterViewHolder.posterImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                System.out.println("Movie \"" + movie.getOriginalTitle() + "\" is selected.");
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.MOVIE_ID, movie.getId());
                context.startActivity(intent);

            }

        });

    }

}