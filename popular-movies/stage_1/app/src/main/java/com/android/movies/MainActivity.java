package com.android.movies;

import java.io.IOException;
import java.util.LinkedList;

import android.os.Bundle;

import android.support.v4.app.LoaderManager;

import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.android.movies.model.Movie;

import com.android.movies.utils.JsonUtils;

import com.android.movies.utils.RestUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LinkedList<Movie>> {

    public static final int MOVIES_LOADER_ID = 0;

    private Integer currentPageNumber = 1;
    private Integer totalNumberOfPages = 1;

    private String currentUrl = RestUtils.POPULAR_MOVIES_URL;

    private LinkedList<Movie> moviesInfo = new LinkedList<Movie>();

    RecyclerView recyclerView;

    MovieInfoRecyclerViewAdapter movieInfoRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestUtils.setStrictMode();

        recyclerView = findViewById(R.id.movies_recycler_view);

        /*
            Keeping things simple here by using hard-coded Column Span Count. It is understood that it is not feasible for devices of different size.
            It can be dynamically managed by EITHER adding GlobalLayoutListener on RecyclerView's ViewTreeObserver OR customizing GridLayoutManager by extending it.
            But as said keeping this "Stage 1" implementation simple.
        */
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));

        movieInfoRecyclerViewAdapter = new MovieInfoRecyclerViewAdapter(MainActivity.this, null);
        recyclerView.setAdapter(movieInfoRecyclerViewAdapter);

        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);

    }

    @Override
    public Loader<LinkedList<Movie>> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<LinkedList<Movie>>(this) {

            @Override
            protected void onStartLoading() {

                if (moviesInfo != null && !moviesInfo.isEmpty()) {
                    deliverResult(moviesInfo);
                } else {
                    forceLoad();
                }

            }

            @Override
            public LinkedList<Movie> loadInBackground() {

                String jsonPayload = "";
                try {
                    jsonPayload = RestUtils.getJsonPayload(currentUrl);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                LinkedList<Movie> movies = null;
                if (!jsonPayload.isEmpty()) {

                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    try {
                        jsonObject = new JSONObject(jsonPayload);
                        currentPageNumber = jsonObject.getInt("page");
                        totalNumberOfPages = jsonObject.getInt("total_pages");
                        jsonArray = jsonObject.getJSONArray("results");
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }

                    movies = new LinkedList<Movie>();
                    for (int index = 0; index < jsonArray.length(); index++) {

                        try {
                            movies.add(JsonUtils.getMovieInstance(jsonArray.getJSONObject(index)));
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }

                    }

                }

                return movies;

            }

            public void deliverResult(LinkedList<Movie> movies) {
                moviesInfo.addAll(movies);
                super.deliverResult(moviesInfo);
                movieInfoRecyclerViewAdapter.setMovies(moviesInfo);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<LinkedList<Movie>> loader, LinkedList<Movie> movies) {
    }

    @Override
    public void onLoaderReset(Loader<LinkedList<Movie>> loader) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void invalidateData() {
        moviesInfo = new LinkedList<Movie>();
        movieInfoRecyclerViewAdapter.setMovies(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int selectedMenuItemId = menuItem.getItemId();

        if (selectedMenuItemId == R.id.load_more_movies) {

            if (currentPageNumber >= totalNumberOfPages) {
                return true;
            } else {

                this.currentUrl = RestUtils.getPagedUrl(this.currentUrl, ++currentPageNumber);
                System.out.println("New Page URL: " + this.currentUrl);

                if (currentPageNumber >= totalNumberOfPages) {
                    menuItem.setEnabled(false);//No more pages left, so disabling the Menu Item
                } else {
                    menuItem.setEnabled(true);
                }

            }

        } else {

            int titleId = -1;
            if (selectedMenuItemId == R.id.popular_movies) {
                titleId = R.string.app_name;
                this.currentUrl = RestUtils.POPULAR_MOVIES_URL;
            } else if (selectedMenuItemId == R.id.top_rated_movies) {
                titleId = R.string.top_rated_movies;
                this.currentUrl = RestUtils.TOP_RATED_MOVIES_URL;
            }

            if (titleId != -1) {
                setTitle(getString(titleId));
            }

            System.out.println("Selected Menu Item Id: " + selectedMenuItemId + "; Current URL: " + this.currentUrl);
            invalidateData();

        }
        getSupportLoaderManager().getLoader(MOVIES_LOADER_ID).forceLoad();

        return super.onOptionsItemSelected(menuItem);

    }

}