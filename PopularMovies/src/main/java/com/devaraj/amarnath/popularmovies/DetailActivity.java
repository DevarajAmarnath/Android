package com.devaraj.amarnath.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devaraj.amarnath.popularmovies.utilities.MovieInfo;
import com.devaraj.amarnath.popularmovies.utilities.TmDbApiUtilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.devaraj.amarnath.popularmovies.utilities.TmDbApiUtilities.TMDB_MOVIE_IMAGE_SIZE_500;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView mSynopsis = findViewById(R.id.tv_synopsis);
        TextView mTitle = findViewById(R.id.tv_title);
        TextView mUserRating = findViewById(R.id.tv_rating);
        TextView mReleaseDate = findViewById(R.id.tv_release_date);
        ImageView mPosterImage = findViewById(R.id.poster_image);
        final ProgressBar mPosterProgressbar = findViewById(R.id.pb_detail_activity);

        // Get the movie information from the intent and display movie information
        Intent startedIntent = getIntent();
        if (startedIntent != null) {
            if (startedIntent.hasExtra(MainActivity.INTENT_MOVIE_INFO_EXTRA)) {
                @SuppressWarnings("ConstantConditions")
                MovieInfo movieInfo = startedIntent.getExtras().getParcelable("MovieInfo");
                if(movieInfo != null) {
                    mTitle.setText(movieInfo.getOriginalTitle());
                    mUserRating.setText(getResources().getString(R.string.movie_rating, movieInfo.getVoteAverage()));
                    mReleaseDate.setText(movieInfo.getReleaseDate().substring(0, 4));
                    mSynopsis.setText(movieInfo.getOverview());
                    String poster_url = TmDbApiUtilities.getImageUrl(TMDB_MOVIE_IMAGE_SIZE_500, movieInfo.getPosterPath());
                    Picasso.get().load(poster_url).into(mPosterImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            mPosterProgressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            mPosterProgressbar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    });
                }
            }
        }

    }
}
