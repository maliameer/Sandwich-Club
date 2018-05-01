package com.android.movies;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import com.android.movies.model.Movie;

import com.android.movies.utils.RestUtils;

/*
    Initially implemented using simple BaseAdapter then switched to RecyclerView.Adapter.<br>
    Just keeping this code intact if it may be needed later for simple implementation.
*/
public class MovieInfoGridViewAdapter extends BaseAdapter {

    private static LayoutInflater layoutInflater;

    private LinkedList<Movie> movies;

    private Context context;

    public MovieInfoGridViewAdapter(Context context, LinkedList<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public LinkedList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(LinkedList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ((movies != null) ? movies.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Movie movie = movies.get(position);

        View rowView = layoutInflater.inflate(R.layout.movies_list, null);

        TextView titleTextView = rowView.findViewById(R.id.movie_title_tv);
        titleTextView.setText(movie.getOriginalTitle());

        ImageView posterImageView = rowView.findViewById(R.id.movie_poster_iv);
        posterImageView.setTooltipText(movie.getOriginalTitle());

        String posterPath = RestUtils.MOVIE_IMAGES_BASE_URL + movie.getMoviePosterImageUrl();
        Picasso.with(context).load(posterPath).into(posterImageView);
        posterImageView.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              System.out.println("Movie \"" + movie.getOriginalTitle() + "\" is selected.");
              Intent intent = new Intent(context, DetailActivity.class);
              intent.putExtra(DetailActivity.MOVIE_ID, movie.getId());
              context.startActivity(intent);

            }

        });

        return rowView;

    }

}