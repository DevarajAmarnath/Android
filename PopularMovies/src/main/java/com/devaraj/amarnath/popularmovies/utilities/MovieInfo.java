package com.devaraj.amarnath.popularmovies.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Utility Class that holds all the information about a Movie
public class MovieInfo implements Parcelable {

    private int mVoteCount;
    private int mId;
    private boolean mVideo;
    private int mVoteAverage;
    private String mTitle;
    private int mPopularity;
    private String mPosterPath;
    private String mOriginalLanguage;
    private String mOriginalTitle;
    private int[] mGenreIds;
    private String mBackdropPath;
    private boolean mAdult;
    private String mOverview;
    private String mReleaseDate;

    private MovieInfo(Parcel in) {
        mVoteCount = in.readInt();
        mId = in.readInt();
        mVideo = in.readByte() != 0;
        mVoteAverage = in.readInt();
        mTitle = in.readString();
        mPopularity = in.readInt();
        mPosterPath = in.readString();
        mOriginalLanguage = in.readString();
        mOriginalTitle = in.readString();
        mGenreIds = in.createIntArray();
        mBackdropPath = in.readString();
        mAdult = in.readByte() != 0;
        mOverview = in.readString();
        mReleaseDate = in.readString();
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mVoteCount);
        parcel.writeInt(mId);
        parcel.writeByte((byte) (mVideo ? 1 : 0));
        parcel.writeInt(mVoteAverage);
        parcel.writeString(mTitle);
        parcel.writeInt(mPopularity);
        parcel.writeString(mPosterPath);
        parcel.writeString(mOriginalLanguage);
        parcel.writeString(mOriginalTitle);
        parcel.writeIntArray(mGenreIds);
        parcel.writeString(mBackdropPath);
        parcel.writeByte((byte) (mAdult ? 1 : 0));
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
    }

    public MovieInfo(JSONObject movieInfo) throws JSONException {

        this.mVoteCount = movieInfo.getInt("vote_count");
        this.mId = movieInfo.getInt("id");
        this.mVideo = movieInfo.getBoolean("video");
        this.mVoteAverage = movieInfo.getInt("vote_average");
        this.mTitle = movieInfo.getString("title");
        this.mPopularity = movieInfo.getInt("popularity");
        this.mPosterPath = movieInfo.getString("poster_path");
        this.mOriginalLanguage = movieInfo.getString("original_language");
        this.mOriginalTitle = movieInfo.getString("original_title");

        JSONArray genre_ids = movieInfo.getJSONArray("genre_ids");
        this.mGenreIds = new int[genre_ids.length()];
        for (int genre_index = 0; genre_index < genre_ids.length(); genre_index++) {
            mGenreIds[genre_index] = genre_ids.getInt(genre_index);
        }
        this.mBackdropPath = movieInfo.getString("backdrop_path");
        this.mAdult = movieInfo.getBoolean("adult");
        this.mOverview = movieInfo.getString("overview");
        this.mReleaseDate = movieInfo.getString("release_date");
    }

    public int getVoteAverage() {
        return mVoteAverage;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

}
