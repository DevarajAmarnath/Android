package com.devaraj.amarnath.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.devaraj.amarnath.popularmovies.utilities.MovieInfo;
import com.devaraj.amarnath.popularmovies.utilities.TmDbApiUtilities;
import com.devaraj.amarnath.popularmovies.utilities.TmDbJsonUtilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieImageViewHolder> {
    private MovieInfo[] mMovieInfo = null;

    private final MovieRecyclerViewAdapterOnClickHandler mClickHandler;

    public MovieRecyclerViewAdapter(MovieRecyclerViewAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public interface MovieRecyclerViewAdapterOnClickHandler {
        void onClick(MovieInfo movieInfo);
    }

    @NonNull
    @Override
    public MovieImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
        return new MovieImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieImageViewHolder holder, int position) {
        if(mMovieInfo != null){
            holder.movieProgressBar.setVisibility(View.VISIBLE);
            String poster_url = TmDbApiUtilities.getImageUrl(TmDbApiUtilities.TMDB_MOVIE_IMAGE_SIZE_780, mMovieInfo[position].getPosterPath());
            Picasso.get().load(poster_url).into(holder.movieImageView, new Callback() {
                @Override
                public void onSuccess() {
                    holder.movieImageView.setVisibility(View.VISIBLE);
                    holder.movieProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    holder.movieImageViewError.setVisibility(View.VISIBLE);
                    holder.movieImageView.setVisibility(View.GONE);
                    holder.movieProgressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mMovieInfo == null){
            return 0;
        }
        return mMovieInfo.length;
    }

    public class MovieImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView movieImageView;
        final ImageView movieImageViewError;
        final ProgressBar movieProgressBar;
        MovieImageViewHolder(View itemView) {
            super(itemView);
            movieImageView = itemView.findViewById(R.id.iv_grid_movie);
            movieImageViewError = itemView.findViewById(R.id.iv_grid_movie_error);
            movieProgressBar = itemView.findViewById(R.id.pb_image_loading);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(mMovieInfo[getAdapterPosition()]);
        }
    }

    public void setMovieInfo(String jsonString) {
        try {
            mMovieInfo = TmDbJsonUtilities.getMovieIdsFromJson(jsonString);
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
