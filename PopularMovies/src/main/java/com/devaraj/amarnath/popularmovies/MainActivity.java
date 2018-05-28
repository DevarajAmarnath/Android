package com.devaraj.amarnath.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.devaraj.amarnath.popularmovies.utilities.MovieInfo;
import com.devaraj.amarnath.popularmovies.utilities.TmDbApiUtilities;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
                                                                MovieRecyclerViewAdapter.MovieRecyclerViewAdapterOnClickHandler {

    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;

    @SuppressWarnings("FieldCanBeLocal")
    private final int GRID_SPAN_COUNT_PORTRAIT  = 2;
    @SuppressWarnings("FieldCanBeLocal")
    private final int GRID_SPAN_COUNT_LANDSCAPE = 4;

    private static final int LOADER_ID = 0xDEADBEAF;
    private static final String SEARCH_QUERY_TYPE_EXTRA = "QueryType";
    public  static final String INTENT_MOVIE_INFO_EXTRA = "MovieInfo";
    private static final String SAVED_MOVIE_INFO_JSON_EXTRA = "MovieJson";

    private String mMovieInfoJsonData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // We need a gird layout, Create a layout manager for grid view
        GridLayoutManager layoutManager = null;

        // may be a better idea to move this to XML? Create different column information for different screens?
        @SuppressWarnings("ConstantConditions")
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        if(null != display) {
            int orientation = display.getRotation();
            if ((orientation == Surface.ROTATION_90) || (orientation == Surface.ROTATION_270)) {
                layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT_LANDSCAPE);
            } else {
                layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT_PORTRAIT);
            }
        }
        // Set the layout manager to the recycler view
        RecyclerView mMovieGridRecyclerView = findViewById(R.id.rv_movie_grid);
        mMovieGridRecyclerView.setLayoutManager(layoutManager);
        mMovieGridRecyclerView.setHasFixedSize(true);

        // Now we need to feed the recycler view using our own adapter
        mMovieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this);
        mMovieGridRecyclerView.setAdapter(mMovieRecyclerViewAdapter);

        if (savedInstanceState != null) {
            mMovieInfoJsonData = savedInstanceState.getString(SAVED_MOVIE_INFO_JSON_EXTRA);
            mMovieRecyclerViewAdapter.setMovieInfo(mMovieInfoJsonData);
        } else {
            // by default, load popular movies
            queryTmdb(TmDbApiUtilities.TMDB_MOVIE_TYPE_POPULAR);
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return ((netInfo != null) && (netInfo.isConnectedOrConnecting()));
    }

    private void queryTmdb(String queryType) {
        if(isNetworkAvailable()) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString(SEARCH_QUERY_TYPE_EXTRA, queryType);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Object> githubSearchLoader = loaderManager.getLoader(LOADER_ID);
            if (githubSearchLoader == null) {
                loaderManager.initLoader(LOADER_ID, queryBundle, this);
            } else {
                loaderManager.restartLoader(LOADER_ID, queryBundle, this);
            }
        }else{
            Toast.makeText(this, "No Internet Connection. Try again later", Toast.LENGTH_LONG).show();
        }
    }

    static class MovieAsyncClassLoader extends AsyncTaskLoader<String> {
        private final Bundle mBundle;
        private String mQueryResultCache = null;

        MovieAsyncClassLoader(@NonNull Context context, Bundle args) {
            super(context);
            mBundle = args;
        }

        @Override
        public void deliverResult(@Nullable String data) {
            mQueryResultCache = data;
            super.deliverResult(data);
        }

        @Override
        protected void onStartLoading() {
            if(mQueryResultCache == null) {
                forceLoad();
            } else {
                deliverResult(mQueryResultCache);
            }
        }

        @Nullable
        @Override
        public String loadInBackground() {
            String result = null;
            String queryType = mBundle.getString(SEARCH_QUERY_TYPE_EXTRA);
            if ((queryType == null) || (queryType.isEmpty())){
                return null;
            }

            try {
                result = TmDbApiUtilities.getHttpResponse(TmDbApiUtilities.getMovieListUrl(queryType));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
         return  new MovieAsyncClassLoader(this,args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (data != null) {
            mMovieInfoJsonData = data;
            mMovieRecyclerViewAdapter.setMovieInfo(mMovieInfoJsonData);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_MOVIE_INFO_JSON_EXTRA, mMovieInfoJsonData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);
        return true;
    }

    public void onGroupItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_popular:
                queryTmdb(TmDbApiUtilities.TMDB_MOVIE_TYPE_POPULAR);
                break;
            case R.id.menu_item_top_rated:
                queryTmdb(TmDbApiUtilities.TMDB_MOVIE_TYPE_TOP_RATED);
                break;
            case R.id.menu_item_upcoming:
                queryTmdb(TmDbApiUtilities.TMDB_MOVIE_TYPE_UPCOMING);
                break;
        }
        if (item.isChecked()) {
            item.setChecked(false);
        }
        else {
            item.setChecked(true);
        }
    }

    @Override
    public void onClick(MovieInfo movieInfo) {
        // Go to the detail activity screen and pass on the movie information
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(INTENT_MOVIE_INFO_EXTRA, movieInfo);
        startActivity(detailActivityIntent);
    }
}
